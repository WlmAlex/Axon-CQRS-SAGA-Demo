package com.appsdeveloperblog.estore.OrderService.query.handlers;

import com.appsdeveloperblog.estore.OrderService.core.events.OrderApprovedEvent;
import com.appsdeveloperblog.estore.OrderService.core.events.OrderCreatedEvent;
import com.appsdeveloperblog.estore.OrderService.core.events.OrderRejectedEvent;

public interface OrderEvents {

    void on(OrderCreatedEvent orderCreatedEvent);
    void on(OrderApprovedEvent orderApprovedEvent);
    void on(OrderRejectedEvent orderRejectedEvent);
}
