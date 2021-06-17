package com.appsdeveloperblog.estore.PaymentService.querys.handlers;

import com.appsdeveloperblog.estore.PaymentService.core.events.PaymentProcessedEvent;

public interface PaymentEventsHandler {
    void on(PaymentProcessedEvent event);
}
