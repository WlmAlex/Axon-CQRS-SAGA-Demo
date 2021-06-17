package com.appsdeveloperblog.estore.OrderService.query.handlers;

import com.appsdeveloperblog.estore.OrderService.core.models.Order;
import com.appsdeveloperblog.estore.OrderService.core.models.OrderSummary;
import com.appsdeveloperblog.estore.OrderService.query.querys.FindOrderQuery;

public interface OrderQueryHandler {

    OrderSummary findByOrderId(FindOrderQuery findOrderQuery);
}
