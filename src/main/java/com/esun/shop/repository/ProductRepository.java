package com.esun.shop.repository;

import com.esun.shop.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private static class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product p = new Product();
            p.setProductId(rs.getString("ProductID"));
            p.setProductName(rs.getString("ProductName"));
            p.setPrice(rs.getInt("Price"));
            p.setQuantity(rs.getInt("Quantity"));
            return p;
        }
    }

    public List<Product> findAllAvailable() {
        return jdbcTemplate.query("SELECT ProductID, ProductName, Price, Quantity FROM dbo.Product WHERE Quantity > 0",
                new ProductRowMapper());
    }

    public Product findById(String productId) {
        return jdbcTemplate.queryForObject(
                "SELECT ProductID, ProductName, Price, Quantity FROM dbo.Product WHERE ProductID = ?",
                new ProductRowMapper(), productId);
    }

    public void insert(Product p) {
        jdbcTemplate.update(
                "EXEC dbo.sp_insert_product ?, ?, ?, ?",
                p.getProductId(), p.getProductName(), p.getPrice(), p.getQuantity()
        );
    }
}
