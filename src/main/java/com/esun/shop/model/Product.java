package com.esun.shop.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Product {
    @NotBlank
    private String productId;
    @NotBlank
    private String productName;
    @NotNull @Min(0)
    private Integer price;
    @NotNull @Min(0)
    private Integer quantity;

    public Product() {}

    public Product(String productId, String productName, Integer price, Integer quantity) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
