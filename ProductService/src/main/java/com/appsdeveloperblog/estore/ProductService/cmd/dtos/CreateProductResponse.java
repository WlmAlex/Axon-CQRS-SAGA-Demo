package com.appsdeveloperblog.estore.ProductService.cmd.dtos;

import com.appsdeveloperblog.estore.ProductService.core.dtos.BaseResponse;

public class CreateProductResponse extends BaseResponse {
    private String id;

    public CreateProductResponse(String message) {
        super(message);
    }

    public CreateProductResponse(String id, String message) {
        super(message);
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
