package com.appsdeveloperblog.estore.ProductService.core.errorhandling;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class ErrorMessage {

    private String message;
    private LocalDateTime timestamp;
}
