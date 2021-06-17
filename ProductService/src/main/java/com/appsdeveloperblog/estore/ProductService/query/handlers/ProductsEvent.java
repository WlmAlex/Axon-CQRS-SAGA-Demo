package com.appsdeveloperblog.estore.ProductService.query.handlers;

import com.appsdeveloperblog.estore.Core.events.ProductReservationCancelledEvent;
import com.appsdeveloperblog.estore.Core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.ProductService.core.events.ProductCreatedEvent;

public interface ProductsEvent {

    void on(ProductCreatedEvent event) throws Exception;

    void on(ProductReservedEvent event);
    void on(ProductReservationCancelledEvent event);

    /*void on(ProductUpdatedEvent event);

    void on(ProductDeletedEvent event);*/
}
