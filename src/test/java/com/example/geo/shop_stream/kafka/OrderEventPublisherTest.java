package com.example.geo.shop_stream.kafka;

import com.example.geo.shop_stream.dto.OrderPlacedEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderEventPublisherTest {

    private static final String TOPIC = "topic_orderPlaced";

    @Mock
    private KafkaProducer<String, String> kafkaProducerMock;

    @Mock
    private ObjectMapper objectMapperMock;

    private OrderEventPublisher orderEventPublisher;

    @BeforeEach
    void setUp() {
        orderEventPublisher = new OrderEventPublisher(kafkaProducerMock, objectMapperMock, TOPIC);
    }

    @Test
    void publish_success() throws Exception {
        ArgumentCaptor<ProducerRecord<String, String>> captor =
                ArgumentCaptor.captor();

        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent("1234", "33", List.of("prod_A", "prod_B"), new BigDecimal("99.90"), Instant.now());
        when(objectMapperMock.writeValueAsString(orderPlacedEvent)).thenReturn("json");

        orderEventPublisher.publish(orderPlacedEvent);

        verify(kafkaProducerMock).send(captor.capture(), any());

        ProducerRecord<String, String> capturedRecord = captor.getValue();
        assertEquals("topic_orderPlaced", capturedRecord.topic());
        assertEquals("1234", capturedRecord.key());
        assertEquals("json", capturedRecord.value());
    }

    @Test
    void publish_throwsException() {
        OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent("1234", "33", List.of("prod_A", "prod_B"), new BigDecimal("99.90"), Instant.now());
        when(kafkaProducerMock.send(any())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> orderEventPublisher.publish(orderPlacedEvent));
    }
}