package com.appsdeveloperblog.estore.ProductService.query.repository;

import com.appsdeveloperblog.estore.ProductService.core.data.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, String> {

    ProductEntity findByProductId(String productId);
    ProductEntity findByproductIdOrTitle(String productId, String title);
}
