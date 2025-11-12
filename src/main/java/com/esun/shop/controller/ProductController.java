package com.esun.shop.controller;

import com.esun.shop.model.Product;
import com.esun.shop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 顯示庫存 > 0 的商品清單（顧客頁用）
    @GetMapping("/available")
    public List<Product> listAvailable() {
        return productService.listAvailable();
    }

    // 新增商品（管理員頁用）
    @PostMapping
    public void addProduct(@Valid @RequestBody Product p) {
        productService.addProduct(p);
    }
}
