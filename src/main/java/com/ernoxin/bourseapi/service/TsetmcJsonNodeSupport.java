package com.ernoxin.bourseapi.service;

import com.fasterxml.jackson.databind.JsonNode;

final class TsetmcJsonNodeSupport {

    private TsetmcJsonNodeSupport() {
    }

    static String textOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asText().trim();
    }

    static String rawTextOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asText();
    }

    static Integer intOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asInt();
    }

    static Long longOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asLong();
    }

    static Double doubleOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asDouble();
    }

    static Double firstDoubleOrNull(JsonNode node, String... fields) {
        for (String field : fields) {
            Double value = doubleOrNull(node, field);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    static Boolean booleanOrNull(JsonNode node, String field) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }
        return fieldNode.asBoolean();
    }
}
