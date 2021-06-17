package com.appsdeveloperblog.estore.OrderService.core.events;

import com.appsdeveloperblog.estore.OrderService.cmd.commands.OrderStatus;
import lombok.Value;

@Value
public class OrderApprovedEvent {

    private final String orderId;
    private final OrderStatus orderStatus = OrderStatus.APPROVED;
}
