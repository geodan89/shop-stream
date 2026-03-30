package com.example.geo.shop_stream.model;

public enum OrderStatus {

    PENDING,      // just placed, waiting for inventory check
    CONFIRMED,    // inventory service approved it
    FAILED        // out of stock or processing error
}
