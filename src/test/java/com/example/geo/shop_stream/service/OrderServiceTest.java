package com.example.geo.shop_stream.service;

import com.example.geo.shop_stream.OrderTestFixtures;
import com.example.geo.shop_stream.dto.CreateOrderRequest;
import com.example.geo.shop_stream.dto.OrderPlacedEvent;
import com.example.geo.shop_stream.kafka.OrderEventPublisher;
import com.example.geo.shop_stream.model.Order;
import com.example.geo.shop_stream.model.OrderStatus;
import com.example.geo.shop_stream.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepositoryMock;
    @Mock
    private OrderEventPublisher orderEventPublisherMock;

    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(orderRepositoryMock, orderEventPublisherMock);
    }

    @Test
    void placeOrder_shouldSaveOrderAndPublishEvent() {
        CreateOrderRequest request = OrderTestFixtures.createOrderRequest();
        Order savedOrder = OrderTestFixtures.createOrder(request);
        when(orderRepositoryMock.save(any(Order.class))).thenReturn(savedOrder);

        Order result = orderService.placeOrder(request);

        assertEquals("cust-1", result.getCustomerId());
        assertEquals(OrderStatus.PENDING, result.getStatus());
        assertEquals(List.of("prod-A", "prod-B"), result.getProductIds());
        assertEquals(0, result.getTotalAmount().compareTo(new BigDecimal("99.90")));
        assertNull(result.getUpdatedAt());
        assertNotNull(result.getCreatedAt());
        assertNotNull(result.getId());

        verify(orderRepositoryMock).save(any(Order.class));
        verify(orderEventPublisherMock).publish(any(OrderPlacedEvent.class));
    }

    @Test
    void placeOrder_orderIsNotSaved() {
        CreateOrderRequest request = OrderTestFixtures.createOrderRequest();
        when(orderRepositoryMock.save(any(Order.class))).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> orderService.placeOrder(request));

        verify(orderEventPublisherMock, never()).publish(any(OrderPlacedEvent.class));
    }
}