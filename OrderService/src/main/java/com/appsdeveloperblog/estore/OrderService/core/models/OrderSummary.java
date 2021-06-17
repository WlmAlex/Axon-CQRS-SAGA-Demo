package com.appsdeveloperblog.estore.OrderService.core.models;

import com.appsdeveloperblog.estore.OrderService.cmd.commands.OrderStatus;
import lombok.Value;

@Value
public class OrderSummary {

    private final String orderId;
    private final OrderStatus orderStatus;
    private final String reason;
}
