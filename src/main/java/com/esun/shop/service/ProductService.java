package com.esun.shop.service;

import com.esun.shop.model.Product;
import com.esun.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> listAvailable() {
        return productRepository.findAllAvailable();
    }

    @Transactional
    public void addProduct(Product p) {
        productRepository.insert(p);
    }

    public Product getById(String id) {
        return productRepository.findById(id);
    }
}
