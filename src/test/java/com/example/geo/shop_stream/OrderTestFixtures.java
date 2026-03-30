package com.example.geo.shop_stream;

import com.example.geo.shop_stream.dto.CreateOrderRequest;
import com.example.geo.shop_stream.model.Order;

import java.math.BigDecimal;
import java.util.List;

public class OrderTestFixtures {

    public static CreateOrderRequest createOrderRequest() {
        return new CreateOrderRequest(
                "cust-1",
                List.of("prod-A", "prod-B"),
                new BigDecimal("99.90")
        );
    }

    public static Order createOrder(CreateOrderRequest request) {
        return new Order(
                "some-uuid",
                request.customerId(),
                request.productIds(),
                request.totalAmount()
        );
    }

}
