package com.appsdeveloperblog.estore.OrderService.query.handlers;

import com.appsdeveloperblog.estore.OrderService.core.events.OrderApprovedEvent;
import com.appsdeveloperblog.estore.OrderService.core.events.OrderCreatedEvent;
import com.appsdeveloperblog.estore.OrderService.core.events.OrderRejectedEvent;
import com.appsdeveloperblog.estore.OrderService.core.models.Order;
import com.appsdeveloperblog.estore.OrderService.query.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@ProcessingGroup("order-group")
@RequiredArgsConstructor
public class OrderEventsHandlerImpl implements OrderEvents {

    private final OrderRepository orderRepository;

    @Override
    @EventHandler
    public void on(OrderCreatedEvent orderCreatedEvent) {
        Order order = Order.builder().build();
        BeanUtils.copyProperties(orderCreatedEvent, order);
        orderRepository.save(order);
    }

    @Override
    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
        Order order = orderRepository.findByOrderId(orderApprovedEvent.getOrderId());
        if(order == null) {
            //todo do something about it
            return;
        }
        order.setOrderStatus(orderApprovedEvent.getOrderStatus());
        orderRepository.save(order);
    }

    @Override
    public void on(OrderRejectedEvent orderRejectedEvent) {
        Order order = orderRepository.findByOrderId(orderRejectedEvent.getOrderId());
        if(order == null) {
            //todo do something about it
            return;
        }
        order.setOrderStatus(orderRejectedEvent.getOrderStatus());
        orderRepository.save(order);
    }
}
