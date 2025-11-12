package com.esun.shop.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Order {
    @NotBlank
    private String orderId;
    @NotBlank
    private String memberId;
    @NotNull @Min(0)
    private Integer price;
    @NotNull
    private Integer payStatus; // 0:未付款 1:已付款

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }
    public Integer getPayStatus() { return payStatus; }
    public void setPayStatus(Integer payStatus) { this.payStatus = payStatus; }
}
