package com.esun.shop.service;

import com.esun.shop.model.OrderCreateRequest;
import com.esun.shop.model.Product;
import com.esun.shop.repository.OrderRepository;
import com.esun.shop.repository.ProductRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class OrderService {
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;

	public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
		this.orderRepository = orderRepository;
		this.productRepository = productRepository;
	}

	private String generateOrderId() {
		String ts = DateTimeFormatter.ofPattern("yyyyMMddHHmmss").format(LocalDateTime.now());
		return "Ms" + ts + (int) (Math.random() * 900 + 100);
	}

	@Transactional
	public String createOrder(OrderCreateRequest req) {
		int total = 0;
		for (OrderCreateRequest.Item it : req.items) {
			Product p = productRepository.findById(it.productId);
			if (it.quantity > p.getQuantity()) {
				throw new IllegalArgumentException("購買數量大於庫存: " + p.getProductId());
			}
			if (!it.price.equals(p.getPrice())) {
				throw new IllegalArgumentException("單價不一致: " + p.getProductId());
			}
			total += it.price * it.quantity;
		}
		String orderId = generateOrderId();
		orderRepository.createOrderHeader(orderId, req.memberId, total);
		for (OrderCreateRequest.Item it : req.items) {
			var od = new com.esun.shop.model.OrderDetail();
			od.setOrderId(orderId);
			od.setProductId(it.productId);
			od.setQuantity(it.quantity);
			od.setStandPrice(it.price);
			od.setItemPrice(it.price * it.quantity);
			orderRepository.addOrderItemAndUpdateStock(orderId, od);
		}
		return orderId;
	}

	@Transactional
	public void markPaid(String orderId) {
		orderRepository.markOrderPaid(orderId);
	}
}