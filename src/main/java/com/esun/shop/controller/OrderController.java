package com.esun.shop.controller;

import com.esun.shop.model.OrderCreateRequest;
import com.esun.shop.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 建立訂單（同時在資料庫新增訂單資料並更新庫存）
    @PostMapping
    public String create(@Valid @RequestBody OrderCreateRequest req) {
        return orderService.createOrder(req);
    }

    // 標記訂單為已付款（若需要）
    @PostMapping("/{orderId}/pay")
    public void pay(@PathVariable String orderId) {
        orderService.markPaid(orderId);
    }
}
