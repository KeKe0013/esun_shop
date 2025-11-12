package com.esun.shop.model;

import java.util.List;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderCreateRequest {

    public static class Item {
        @NotBlank
        public String productId;
        @NotNull @Min(1)
        public Integer quantity;
        @NotNull @Min(0)
        public Integer price;
    }

    @NotBlank
    public String memberId;
    @NotEmpty
    public List<Item> items;
}
