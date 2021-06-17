package com.appsdeveloperblog.estore.OrderService.cmd.aggregate;

import com.appsdeveloperblog.estore.OrderService.cmd.commands.ApproveOrderCommand;
import com.appsdeveloperblog.estore.OrderService.cmd.commands.CreateOrderCommand;
import com.appsdeveloperblog.estore.OrderService.cmd.commands.OrderStatus;
import com.appsdeveloperblog.estore.OrderService.cmd.commands.RejectOrderCommand;
import com.appsdeveloperblog.estore.OrderService.core.events.OrderApprovedEvent;
import com.appsdeveloperblog.estore.OrderService.core.events.OrderCreatedEvent;
import com.appsdeveloperblog.estore.OrderService.core.events.OrderRejectedEvent;
import lombok.NoArgsConstructor;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
@NoArgsConstructor
public class OrderAggregate {

    @AggregateIdentifier
    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {

        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.builder()
                .orderId(createOrderCommand.getOrderId())
                .addressId(createOrderCommand.getAddressId())
                .orderStatus(createOrderCommand.getOrderStatus())
                .productId(createOrderCommand.getProductId())
                .quantity(createOrderCommand.getQuantity())
                .userId(createOrderCommand.getUserId())
                .build();
        AggregateLifecycle.apply(orderCreatedEvent);

    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        this.orderId = orderCreatedEvent.getOrderId();
        this.addressId = orderCreatedEvent.getAddressId();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
        this.productId = orderCreatedEvent.getProductId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.userId = orderCreatedEvent.getUserId();
    }

    @CommandHandler
    public void handle(ApproveOrderCommand command) {
        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(command.getOrderId());
        AggregateLifecycle.apply(orderApprovedEvent);
    }

    @EventSourcingHandler
    public void on(OrderApprovedEvent event) {
        this.orderId = event.getOrderId();
        this.orderStatus = event.getOrderStatus();
    }

    @CommandHandler
    public void handle(RejectOrderCommand rejectOrderCommand) {
        OrderRejectedEvent orderRejectedEvent = new OrderRejectedEvent(rejectOrderCommand.getOrderId(), rejectOrderCommand.getReason());
        AggregateLifecycle.apply(orderRejectedEvent);
    }

    @EventSourcingHandler
    public void on(OrderRejectedEvent event) {
        this.orderStatus = event.getOrderStatus();
    }
}
