package com.appsdeveloperblog.estore.PaymentService.cmd.aggregate;

import com.appsdeveloperblog.estore.Core.commands.ProcessPaymentCommand;
import com.appsdeveloperblog.estore.PaymentService.core.events.PaymentProcessedEvent;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
public class PaymentAggregate {


    @AggregateIdentifier
    private String paymentId;
    private String orderId;

    @CommandHandler
    public PaymentAggregate(ProcessPaymentCommand processPaymentCommand) {
        String orderId = processPaymentCommand.getOrderId();
        String paymentId = processPaymentCommand.getPaymentId();
        if (StringUtils.isBlank(orderId)) {
            throw new IllegalArgumentException("order id must have a valid value, orderId=" + orderId);
        }
        if (StringUtils.isBlank(paymentId)) {
            throw new IllegalArgumentException("payment id must have a valid value, paymentId=" + paymentId);
        }
        PaymentProcessedEvent paymentProcessedEvent = PaymentProcessedEvent.builder()
                .orderId(orderId)
                .paymentId(paymentId)
                .build();
        AggregateLifecycle.apply(paymentProcessedEvent);
    }

    @EventSourcingHandler
    public void on(PaymentProcessedEvent event) {
        this.orderId = event.getOrderId();
        this.paymentId = event.getPaymentId();
    }
}
