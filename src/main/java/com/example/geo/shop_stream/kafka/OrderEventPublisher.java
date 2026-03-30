package com.example.geo.shop_stream.kafka;

import com.example.geo.shop_stream.dto.OrderPlacedEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PreDestroy;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class OrderEventPublisher {

    private final KafkaProducer<String, String> producer;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.orders-placed}")
    private String topic;

    public OrderEventPublisher(KafkaProducer<String, String> producer,
                               ObjectMapper objectMapper) {
        this.producer = producer;
        this.objectMapper = objectMapper;
    }

    public void publish(OrderPlacedEvent event) {
        try {
            String json = objectMapper.writeValueAsString(event);

            // The key is the orderId — Kafka uses it to decide which partition
            // Same orderId always goes to the same partition → ordered delivery per order
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(topic, event.orderId(), json);

            // send() is non-blocking — it returns a Future
            // Here we add a callback instead of blocking on .get()
            producer.send(record, (metadata, exception) -> {
                if (exception != null) {
                    // TODO: in production you'd push to a retry queue
                    System.err.println("Failed to publish event: " + exception.getMessage());
                } else {
                    System.out.printf("Published to %s [partition=%d, offset=%d]%n",
                            metadata.topic(), metadata.partition(), metadata.offset());
                }
            });

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
    }

    // Called by Spring when the app shuts down — flushes pending batches
    @PreDestroy
    public void close() {
        producer.flush();
        producer.close();
    }
}
