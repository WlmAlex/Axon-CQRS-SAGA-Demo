package com.appsdeveloperblog.estore.ProductService.query.RestControllers;

import com.appsdeveloperblog.estore.ProductService.query.dtos.ProductRestModel;
import com.appsdeveloperblog.estore.ProductService.query.querys.FindAllProductsQuery;
import lombok.RequiredArgsConstructor;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/productLookup")
@RequiredArgsConstructor
public class ProductQueryController {

    private final QueryGateway queryGateway;

    @GetMapping
    public List<ProductRestModel> getProducts() {
        List<ProductRestModel> productRestModelList = queryGateway.query(FindAllProductsQuery.builder().build(), ResponseTypes.multipleInstancesOf(ProductRestModel.class)).join();
        return productRestModelList;
    }
}
