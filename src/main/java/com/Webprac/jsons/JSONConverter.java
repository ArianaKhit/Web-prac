package com.Webprac.jsons;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.AttributeConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class JSONConverter implements AttributeConverter<JsonNode, String> {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(JsonNode jsonObj) {
        if (jsonObj == null) {
            return null;
        }

        String jsonStr = jsonObj.toString();
        if (jsonStr == "") {
            return null;
        }
        return  jsonStr;
    }

    @Override
    public JsonNode convertToEntityAttribute(String jsonStr) {
        if (jsonStr == null) {
            return null;
        }

        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonStr);
        } catch (final IOException e) {
            System.out.println("JSON error:");
            System.out.println(e.toString());
        }
        return jsonNode;
    }
}