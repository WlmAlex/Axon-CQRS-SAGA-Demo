package com.appsdeveloperblog.estore.ProductService.core.events;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductDeletedEvent {

    private String productId;
}
