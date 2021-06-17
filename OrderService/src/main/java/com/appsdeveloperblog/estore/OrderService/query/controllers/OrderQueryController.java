package com.appsdeveloperblog.estore.OrderService.query.controllers;

import com.appsdeveloperblog.estore.OrderService.core.models.OrderSummary;
import com.appsdeveloperblog.estore.OrderService.query.querys.FindOrderQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1/orders")
@RequiredArgsConstructor
public class OrderQueryController {

    private final QueryGateway queryGateway;

    @GetMapping("/{orderId}")
    public OrderSummary findByOrderId(@PathVariable("orderId") String orderId) {
        //String orderId = UUID.randomUUID().toString();
        SubscriptionQueryResult<OrderSummary, OrderSummary> subscriptionQuery = queryGateway.subscriptionQuery(
                new FindOrderQuery(orderId), ResponseTypes.instanceOf(OrderSummary.class),
                ResponseTypes.instanceOf(OrderSummary.class));
        return subscriptionQuery.updates().blockFirst();
    }
}
