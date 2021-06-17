package com.appsdeveloperblog.estore.ProductService.cmd.requestmodel;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
public class CreateProductRestModel {

    @NotBlank(message = "must provide a title")
    private String title;
    @Min(value = 0, message = "price must be set greater than 0")
    private BigDecimal price;
    @Min(value = 1, message = "quantity must be equal or greater than 1")
    @Max(value = 10, message = "quantity must be equal or lower than 10")
    private Integer quantity;
}
