package com.appsdeveloperblog.estore.ProductService.cmd.aggregate;

import com.appsdeveloperblog.estore.Core.commands.CancelProductReservationCommand;
import com.appsdeveloperblog.estore.Core.commands.ReserveProductCommand;
import com.appsdeveloperblog.estore.Core.events.ProductReservationCancelledEvent;
import com.appsdeveloperblog.estore.Core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.ProductService.cmd.commands.CreateProductCommand;
import com.appsdeveloperblog.estore.ProductService.core.events.ProductCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.math.BigDecimal;

@Aggregate(snapshotTriggerDefinition = "productSnapshotTriggerDefinition")
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate() {
    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) throws Exception {

        ProductCreatedEvent productCreatedEvent = ProductCreatedEvent.builder().productId(createProductCommand.getProductId())
                .price(createProductCommand.getPrice())
                .quantity(createProductCommand.getQuantity())
                .title(createProductCommand.getTitle())
                .build();
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent event) {
        this.productId = event.getProductId();
        this.price = event.getPrice();
        this.quantity = event.getQuantity();
        this.title = event.getTitle();
    }

    @CommandHandler
    public void handle(ReserveProductCommand reserveProductCommand) {
        if (quantity < reserveProductCommand.getQuantity()) {
            throw new IllegalArgumentException("Insufficient number of items in stock");
        }

        ProductReservedEvent productReservedEvent = ProductReservedEvent.builder()
                .orderId(reserveProductCommand.getOrderId())
                .productId(reserveProductCommand.getProductId())
                .quantity(reserveProductCommand.getQuantity())
                .userId(reserveProductCommand.getUserId())
                .build();
        AggregateLifecycle.apply(productReservedEvent);
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        this.quantity -= productReservedEvent.getQuantity();
    }

    @CommandHandler
    public void on(CancelProductReservationCommand command) {
        ProductReservationCancelledEvent productReservationCancelledEvent = ProductReservationCancelledEvent.builder()
                .orderId(command.getOrderId())
                .productId(command.getProductId())
                .quantity(command.getQuantity())
                .userId(command.getUserId())
                .reason(command.getReason())
                .build();
        AggregateLifecycle.apply(productReservationCancelledEvent);
    }

    @EventSourcingHandler
    public void handle(ProductReservationCancelledEvent event) {
        this.quantity += event.getQuantity();
    }
}
