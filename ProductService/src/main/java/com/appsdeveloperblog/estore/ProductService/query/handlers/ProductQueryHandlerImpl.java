package com.appsdeveloperblog.estore.ProductService.query.handlers;

import com.appsdeveloperblog.estore.ProductService.core.data.ProductEntity;
import com.appsdeveloperblog.estore.ProductService.query.dtos.ProductRestModel;
import com.appsdeveloperblog.estore.ProductService.query.querys.FindAllProductsQuery;
import com.appsdeveloperblog.estore.ProductService.query.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductQueryHandlerImpl implements ProductQueryHandler {

    private final ProductRepository productRepository;

    @Override
    @QueryHandler
    public List<ProductRestModel> findAllProducts(FindAllProductsQuery query) {
        List<ProductEntity> productEntityList = productRepository.findAll();
        List<ProductRestModel> productRestModelList = productEntityList.stream().map(productEntity -> ProductRestModel.builder()
                .productId(productEntity.getProductId())
                .price(productEntity.getPrice())
                .quantity(productEntity.getQuantity())
                .title(productEntity.getTitle())
                .build()
        ).collect(Collectors.toList());
        return productRestModelList;
    }
}
