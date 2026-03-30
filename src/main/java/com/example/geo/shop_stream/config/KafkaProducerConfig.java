package com.example.geo.shop_stream.config;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean
    public KafkaProducer<String, String> kafkaProducer() {
        Properties props = new Properties();

        // Which Kafka broker to connect to
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Every message has a key and a value — both serialized as plain strings/JSON
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Wait for the leader + all in-sync replicas to confirm the write
        // "all" = strongest durability guarantee, good for orders
        props.put(ProducerConfig.ACKS_CONFIG, "all");

        // Retry up to 3 times if the broker is temporarily unavailable
        props.put(ProducerConfig.RETRIES_CONFIG, 3);

        // Batch up to 16KB before sending — reduces network round trips
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);

        // Wait up to 1ms to fill a batch — tiny latency trade-off for throughput
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);

        return new KafkaProducer<>(props);
    }
}
