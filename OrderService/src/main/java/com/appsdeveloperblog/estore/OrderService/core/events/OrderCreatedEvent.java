package com.appsdeveloperblog.estore.OrderService.core.events;

import com.appsdeveloperblog.estore.OrderService.cmd.commands.OrderStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderCreatedEvent {

    public String orderId;
    private String userId;
    private String productId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
}
