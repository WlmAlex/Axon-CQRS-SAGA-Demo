package com.appsdeveloperblog.estore.ProductService.query.handlers;

import com.appsdeveloperblog.estore.ProductService.query.dtos.ProductRestModel;
import com.appsdeveloperblog.estore.ProductService.query.querys.FindAllProductsQuery;

import java.util.List;

public interface ProductQueryHandler {

    List<ProductRestModel> findAllProducts(FindAllProductsQuery query);
}
