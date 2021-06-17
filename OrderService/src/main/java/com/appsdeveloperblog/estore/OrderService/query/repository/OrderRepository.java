package com.appsdeveloperblog.estore.OrderService.query.repository;

import com.appsdeveloperblog.estore.OrderService.core.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
    Order findByOrderId(String orderId);
}
