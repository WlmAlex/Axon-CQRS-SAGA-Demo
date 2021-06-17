package com.appsdeveloperblog.estore.OrderService.sage;

import com.appsdeveloperblog.estore.Core.commands.CancelProductReservationCommand;
import com.appsdeveloperblog.estore.Core.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.estore.Core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.Core.events.ProductReservationCancelledEvent;
import com.appsdeveloperblog.estore.Core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.Core.model.User;
import com.appsdeveloperblog.estore.Core.query.FetchUserPaymentDetailsQuery;
import com.appsdeveloperblog.estore.OrderService.cmd.commands.ApproveOrderCommand;
import com.appsdeveloperblog.estore.OrderService.cmd.commands.RejectOrderCommand;
import com.appsdeveloperblog.estore.OrderService.core.events.OrderApprovedEvent;
import com.appsdeveloperblog.estore.OrderService.core.events.OrderCreatedEvent;
import com.appsdeveloperblog.estore.OrderService.core.events.OrderRejectedEvent;
import com.appsdeveloperblog.estore.OrderService.core.models.OrderSummary;
import com.appsdeveloperblog.estore.OrderService.query.querys.FindOrderQuery;
import com.appsdeveloperblog.estore.PaymentService.core.events.PaymentProcessedEvent;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.deadline.DeadlineManager;
import org.axonframework.deadline.annotation.DeadlineHandler;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Saga
public class OrderSaga {

    private static final String PAYMENT_PROCESSING_TIMEOUT_DEADLINE = "payment-processing-deadline";
    private final Logger log = LoggerFactory.getLogger(OrderSaga.class);
    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    @Autowired
    private transient DeadlineManager deadlineManager;

    @Autowired
    private transient QueryUpdateEmitter queryUpdateEmitter;
    private String scheduleId;

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderCreatedEvent event) {
        ReserveProductCommand reserveProductCommand = ReserveProductCommand.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .build();
        log.info("OrdercreatedEvent handled with orderId " + event.getOrderId());
        commandGateway.send(reserveProductCommand, (commandMessage, commandResultMessage) -> {
            if (commandResultMessage.isExceptional()) {
                // start a compensation method
                RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(event.getOrderId(),
                        commandResultMessage.exceptionResult().getMessage());
                commandGateway.send(rejectOrderCommand);
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent event) {
        //Process payment event
        log.info("ProductReservedEvent is called for productId= "
                + event.getProductId() + " and orderId= " + event.getOrderId());
        FetchUserPaymentDetailsQuery query = FetchUserPaymentDetailsQuery.builder().userId(event.getUserId()).build();
        User userPaymentDetails = null;
        try {
            userPaymentDetails = queryGateway.query(query, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception e) {
            log.error(e.getMessage());
            cancerProductReservation(event, "Could not fetch user payment details");
            return;
        }

        if (userPaymentDetails == null) {
            //start compensation
            cancerProductReservation(event, "UserDetail info is null, start compensation process");
            return;
        }
        log.info("Successfully fetched user payment details for user: " + userPaymentDetails.getFirstName());
        ProcessPaymentCommand paymentCommand = ProcessPaymentCommand.builder()
                .orderId(event.getOrderId())
                .paymentId(UUID.randomUUID().toString())
                .paymentDetails(userPaymentDetails.getPaymentDetails())
                .build();
        scheduleId = deadlineManager.schedule(Duration.of(10, ChronoUnit.SECONDS),
                PAYMENT_PROCESSING_TIMEOUT_DEADLINE, event);
        /*try {
            TimeUnit.SECONDS.sleep(11);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        String result = null;
        try {
            result = commandGateway.sendAndWait(paymentCommand);
        } catch (Exception e) {
            log.error(e.getMessage());
            //start compensation process
            cancerProductReservation(event, e.getMessage());
            return;
        }

        if (result == null) {
            log.info("The payment command resulted in NULL. Start compensating transaction");
            //start compensating transaction
            cancerProductReservation(event, "Could not process user payment with provided payment details");
        }
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void on(PaymentProcessedEvent paymentProcessedEvent) {
        cancelDeadline();
        //send an approve order command
        ApproveOrderCommand approveOrderCommand = new ApproveOrderCommand(paymentProcessedEvent.getOrderId());
        commandGateway.send(approveOrderCommand);

    }

    private void cancelDeadline() {
        if (StringUtils.isNotBlank(scheduleId)) {
            deadlineManager.cancelSchedule(PAYMENT_PROCESSING_TIMEOUT_DEADLINE, scheduleId);
            scheduleId = null;
        }
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderApprovedEvent event) {
        log.info("order is approved, order saga is completed for order id = " + event.getOrderId());
        //SagaLifecycle.end();
        queryUpdateEmitter.emit(FindOrderQuery.class, query -> true,
                new OrderSummary(event.getOrderId(), event.getOrderStatus(), Strings.EMPTY));
    }

    private void cancerProductReservation(ProductReservedEvent event, String reason) {
        cancelDeadline();
        CancelProductReservationCommand cancelProductReservationCommand = CancelProductReservationCommand.builder()
                .orderId(event.getOrderId())
                .productId(event.getProductId())
                .quantity(event.getQuantity())
                .userId(event.getUserId())
                .reason(reason)
                .build();
        commandGateway.send(cancelProductReservationCommand);
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void on(ProductReservationCancelledEvent event) {
        //Create a RejectOrderCommand
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(event.getOrderId(), event.getReason());
        commandGateway.send(rejectOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void on(OrderRejectedEvent event) {
        log.info("Successfully rejected order with id " + event.getOrderId());
        queryUpdateEmitter.emit(FindOrderQuery.class, query -> true,
                new OrderSummary(event.getOrderId(), event.getOrderStatus(), event.getReason()));
    }

    @DeadlineHandler(deadlineName = PAYMENT_PROCESSING_TIMEOUT_DEADLINE)
    public void paymentDeadlineHandler(ProductReservedEvent event) {
        log.info("Payment processing deadline took place, Sending a compensating command to cancel the product reservation");
        cancerProductReservation(event, "payment timeout");
    }
}
