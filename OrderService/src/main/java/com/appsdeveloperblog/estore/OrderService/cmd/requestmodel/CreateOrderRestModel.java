package com.appsdeveloperblog.estore.OrderService.cmd.requestmodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRestModel {

    @NotBlank(message = "must provide a valid product id")
    private String productId;
    @Min(value = 1, message = "quantity value should be euqal or greater than 1")
    @Max(value = 10, message = "quantity value should be less or equal than 10")
    private Integer quantity;
    @NotBlank(message = "must provide a valid address id")
    private String addressId;
}
