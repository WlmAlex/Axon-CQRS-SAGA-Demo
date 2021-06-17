package com.appsdeveloperblog.estore.OrderService.core.events;

import com.appsdeveloperblog.estore.OrderService.cmd.commands.OrderStatus;
import lombok.Value;

@Value
public class OrderRejectedEvent {

    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;
}
