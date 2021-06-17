package com.appsdeveloperblog.estore.ProductService.cmd.RestController;

import com.appsdeveloperblog.estore.ProductService.cmd.commands.CreateProductCommand;
import com.appsdeveloperblog.estore.ProductService.cmd.dtos.CreateProductResponse;
import com.appsdeveloperblog.estore.ProductService.cmd.requestmodel.CreateProductRestModel;
import com.appsdeveloperblog.estore.ProductService.core.dtos.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/products")
@RequiredArgsConstructor
public class ProductsCommandController {

    private final Environment environment;
    private final CommandGateway commandGateway;

    @PostMapping(path = "/")
    public ResponseEntity<BaseResponse> createProduct(@Valid @RequestBody CreateProductRestModel createProductRestModel) {
        CreateProductCommand createProductCommand = CreateProductCommand.builder().price(createProductRestModel.getPrice())
                .quantity(createProductRestModel.getQuantity())
                .title(createProductRestModel.getTitle())
                .productId(UUID.randomUUID().toString())
                .build();
        commandGateway.send(createProductCommand);
        return new ResponseEntity<>(new CreateProductResponse(createProductCommand.getProductId(), "create product successfully"), HttpStatus.CREATED);
    }
}
