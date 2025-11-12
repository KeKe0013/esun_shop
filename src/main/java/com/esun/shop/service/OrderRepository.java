package com.esun.shop.repository;

import com.esun.shop.model.OrderDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {
    private final JdbcTemplate jdbcTemplate;

    public OrderRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createOrderHeader(String orderId, String memberId, int totalPrice) {
        jdbcTemplate.update("EXEC dbo.sp_create_order_header ?, ?, ?", orderId, memberId, totalPrice);
    }

    public void addOrderItemAndUpdateStock(String orderId, OrderDetail item) {
        jdbcTemplate.update("EXEC dbo.sp_add_item_update_stock ?, ?, ?, ?, ?", orderId, item.getProductId(),
                item.getQuantity(), item.getStandPrice(), item.getItemPrice());
    }

    public void markOrderPaid(String orderId) {
        jdbcTemplate.update("EXEC dbo.sp_mark_order_paid ?", orderId);
    }
}