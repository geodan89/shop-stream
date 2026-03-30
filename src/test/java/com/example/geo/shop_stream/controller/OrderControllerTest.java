package com.example.geo.shop_stream.controller;

import com.example.geo.shop_stream.OrderTestFixtures;
import com.example.geo.shop_stream.dto.CreateOrderRequest;
import com.example.geo.shop_stream.model.Order;
import com.example.geo.shop_stream.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderService orderServiceMock;

    @Test
    void placeOrder_success() throws Exception {
        CreateOrderRequest request = OrderTestFixtures.createOrderRequest();
        Order savedOrder = OrderTestFixtures.createOrder(request);
        when(orderServiceMock.placeOrder(any(CreateOrderRequest.class))).thenReturn(savedOrder);
        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.customerId").value("cust-1"))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.productIds[0]").value("prod-A"))
                .andExpect(jsonPath("$.productIds[1]").value("prod-B"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.totalAmount").value(99.90));
    }

    @Test
    void placeOrder_throwsException() throws Exception {
        CreateOrderRequest request = OrderTestFixtures.createOrderRequest();
        String requestJson = objectMapper.writeValueAsString(request);

        when(orderServiceMock.placeOrder(any(CreateOrderRequest.class))).thenThrow(RuntimeException.class);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is5xxServerError());
    }
}