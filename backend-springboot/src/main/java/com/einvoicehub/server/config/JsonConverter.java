package com.einvoicehub.server.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Converter
public class JsonConverter implements AttributeConverter<Object, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object attribute) {
        if (attribute == null) {
            return null;
        }

        try {
            // Nếu là String, return
            if (attribute instanceof String) {
                return (String) attribute;
            }

            // Nếu JsonNode -> String
            if (attribute instanceof JsonNode) {
                return objectMapper.writeValueAsString((JsonNode) attribute);
            }

            // Các kiểu (Map, List, Object...) -> JSON String
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error converting object to JSON string", e);
        }
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }

        try {
            // parse  như JsonNode
            JsonNode jsonNode = objectMapper.readTree(dbData);

            // Nếu là JSON Array return List
            if (jsonNode.isArray()) {
                return objectMapper.readValue(dbData, new TypeReference<List<Object>>() {});
            }

            // Nếu là JSON Object return Map
            if (jsonNode.isObject()) {
                return objectMapper.readValue(dbData, new TypeReference<Map<String, Object>>() {});
            }
            return dbData;
        } catch (JsonProcessingException e) {
            // Nếu không phải JSON , return String
            return dbData;
        }
    }

    //JsonNode thành Map
    public static Map<String, Object> jsonNodeToMap(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return new HashMap<>();
        }
        return objectMapper.convertValue(jsonNode, new TypeReference<Map<String, Object>>() {});
    }

    //JsonNode thành List
    public static List<Object> jsonNodeToList(JsonNode jsonNode) {
        if (jsonNode == null || jsonNode.isNull()) {
            return new ArrayList<>();
        }
        return objectMapper.convertValue(jsonNode, new TypeReference<List<Object>>() {});
    }

    //JSON String thành JsonNode
    public static JsonNode parseJson(String jsonString) {
        if (jsonString == null || jsonString.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Error parsing JSON string", e);
        }
    }
}
