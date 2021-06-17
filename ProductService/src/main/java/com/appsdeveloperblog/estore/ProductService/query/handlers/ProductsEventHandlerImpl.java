package com.appsdeveloperblog.estore.ProductService.query.handlers;

import com.appsdeveloperblog.estore.Core.events.ProductReservationCancelledEvent;
import com.appsdeveloperblog.estore.Core.events.ProductReservedEvent;
import com.appsdeveloperblog.estore.ProductService.core.data.ProductEntity;
import com.appsdeveloperblog.estore.ProductService.core.events.ProductCreatedEvent;
import com.appsdeveloperblog.estore.ProductService.query.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.eventhandling.ResetHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@ProcessingGroup("product-group")
public class ProductsEventHandlerImpl implements ProductsEvent {

    private final ProductRepository productRepository;
    private final Logger log = LoggerFactory.getLogger(ProductsEventHandlerImpl.class);

    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {

    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @Override
    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {
        ProductEntity productEntity = ProductEntity.builder().productId(event.getProductId())
                .price(event.getPrice())
                .quantity(event.getQuantity())
                .title(event.getTitle())
                .build();
        try {
            productRepository.save(productEntity);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        //throw new Exception("Exception happened while execution @Commandhandler method~");
    }

    @EventHandler
    public void on(ProductReservedEvent event) {
        ProductEntity productEntity = productRepository.findByProductId(event.getProductId());
        //log.debug("ProductReservedEvent: Current product quantity: " + productEntity.getQuantity());
        productEntity.setQuantity(productEntity.getQuantity() - event.getQuantity());
        productRepository.save(productEntity);

        //log.debug("ProductReservedEvent: NEW product quantity: " + productEntity.getQuantity());
        log.info("ProductReservedEvent is called for productId= "
                + event.getProductId() + " and orderId= " + event.getOrderId());
    }

    @Override
    @EventHandler
    public void on(ProductReservationCancelledEvent event) {
        ProductEntity productEntity = productRepository.findByProductId(event.getProductId());
        //log.debug("ProductReservationCancelledEvent: Current product quantity: " + productEntity.getQuantity());

        productEntity.setQuantity(productEntity.getQuantity() + event.getQuantity());
        productRepository.save(productEntity);
        //log.debug("ProductReservationCancelledEvent: New product quantity: " + productEntity.getQuantity());

        log.info("ProductReservationCancelledEvent is called for productId= "
                + event.getProductId() + " and orderId= " + event.getOrderId());
    }

    @ResetHandler
    private void reset() {
        productRepository.deleteAll();
    }
}
