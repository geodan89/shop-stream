package com.example.geo.shop_stream.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

// Java 21 record — immutable, no boilerplate
public record OrderPlacedEvent(
        String orderId,
        String customerId,
        List<String> productIds,
        BigDecimal totalAmount,
        Instant placedAt
) {}
