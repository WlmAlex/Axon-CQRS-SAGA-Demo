package com.appsdeveloperblog.estore.ProductService.core.dtos;

public class BaseResponse {

    private String message;

    public BaseResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
