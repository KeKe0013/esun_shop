package com.esun.shop.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class OrderDetail {
    private Integer orderItemSn;
    @NotBlank
    private String orderId;
    @NotBlank
    private String productId;
    @NotNull @Min(1)
    private Integer quantity;
    @NotNull @Min(0)
    private Integer standPrice;
    @NotNull @Min(0)
    private Integer itemPrice;

    public Integer getOrderItemSn() { return orderItemSn; }
    public void setOrderItemSn(Integer orderItemSn) { this.orderItemSn = orderItemSn; }
    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getStandPrice() { return standPrice; }
    public void setStandPrice(Integer standPrice) { this.standPrice = standPrice; }
    public Integer getItemPrice() { return itemPrice; }
    public void setItemPrice(Integer itemPrice) { this.itemPrice = itemPrice; }
}
