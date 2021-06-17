package com.appsdeveloperblog.estore.PaymentService.querys.handlers;

import com.appsdeveloperblog.estore.PaymentService.core.events.PaymentProcessedEvent;
import com.appsdeveloperblog.estore.PaymentService.core.model.PaymentEntity;
import com.appsdeveloperblog.estore.PaymentService.querys.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("payment-group")
@RequiredArgsConstructor
public class PaymentEventsHandlerImpl implements PaymentEventsHandler {

    private final PaymentRepository paymentRepository;

    @Override
    @EventHandler
    public void on(PaymentProcessedEvent event) {
        PaymentEntity paymentEntity = PaymentEntity.builder()
                .orderId(event.getOrderId())
                .paymentId(event.getPaymentId())
                .build();
        paymentRepository.save(paymentEntity);
    }
}
