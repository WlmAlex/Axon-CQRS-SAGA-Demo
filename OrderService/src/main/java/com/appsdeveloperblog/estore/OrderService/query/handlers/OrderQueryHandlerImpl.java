package com.appsdeveloperblog.estore.OrderService.query.handlers;

import com.appsdeveloperblog.estore.OrderService.core.models.Order;
import com.appsdeveloperblog.estore.OrderService.core.models.OrderSummary;
import com.appsdeveloperblog.estore.OrderService.query.querys.FindOrderQuery;
import com.appsdeveloperblog.estore.OrderService.query.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderQueryHandlerImpl implements OrderQueryHandler {

    private final OrderRepository orderRepository;

    @QueryHandler
    public OrderSummary findByOrderId(FindOrderQuery findOrderQuery) {
        Order order = orderRepository.findByOrderId(findOrderQuery.getOrderId());
        return new OrderSummary(order.getOrderId(), order.getOrderStatus(), Strings.EMPTY);
    }
}
