package com.example.geo.shop_stream.model;

import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StringListConverterTest {

    private final StringListConverter stringListConverter = new StringListConverter();

    @Test
    void convertToDatabaseColumn_success() {
        List<String> productList = List.of("product_A", "product_B");
        String expected = "[\"product_A\",\"product_B\"]";

        String result = stringListConverter.convertToDatabaseColumn(productList);

        assertEquals(expected, result);
    }

    @Test
    void convertToDatabaseColumn_emptyList() {
        List<String> productList = List.of();

        String expected = "[]";

        String result = stringListConverter.convertToDatabaseColumn(productList);

        assertEquals(expected, result);
    }

    @Test
    void convertToEntityAttribute_success() {
        List<String> expected = List.of("product_A", "product_B");
        String json = "[\"product_A\",\"product_B\"]";

        List<String> result = stringListConverter.convertToEntityAttribute(json);

        assertEquals(expected, result);
    }

    @Test
    void convertToEntityAttribute_emptyJson() {
        List<String> expected = List.of();
        String json = "[]";

        List<String> result = stringListConverter.convertToEntityAttribute(json);

        assertEquals(expected, result);
    }
}