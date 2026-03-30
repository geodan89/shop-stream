package com.example.geo.shop_stream.service;

import com.example.geo.shop_stream.dto.CreateOrderRequest;
import com.example.geo.shop_stream.dto.OrderPlacedEvent;
import com.example.geo.shop_stream.kafka.OrderEventPublisher;
import com.example.geo.shop_stream.model.Order;
import com.example.geo.shop_stream.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderEventPublisher eventPublisher;

    public OrderService(OrderRepository orderRepository, OrderEventPublisher eventPublisher) {
        this.orderRepository = orderRepository;
        this.eventPublisher = eventPublisher;
    }

    public Order placeOrder(CreateOrderRequest request) {
        // 1. Save to DB first — if Kafka publish fails, the order record still exists
        Order order = new Order(
                UUID.randomUUID().toString(),
                request.customerId(),
                request.productIds(),
                request.totalAmount()
        );
        Order saved = orderRepository.save(order);
        OrderPlacedEvent event = new OrderPlacedEvent(
                saved.getId(),
                saved.getCustomerId(),
                saved.getProductIds(),
                saved.getTotalAmount(),
                saved.getCreatedAt()
        );
        eventPublisher.publish(event);
        return saved;
    }
}
