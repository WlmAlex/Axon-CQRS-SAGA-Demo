package com.appsdeveloperblog.estore.PaymentService.querys.repository;

import com.appsdeveloperblog.estore.PaymentService.core.model.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
}
