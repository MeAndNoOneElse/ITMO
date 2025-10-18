package org.example;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;

public class JsonParser implements RequestParser {
    private static final MathContext MC = new MathContext(100);
    private String originalYString = null;

    @Override
    public BigDecimal[] getBigDecimals(String requestString) throws IllegalArgumentException {
        try {
            requestString = requestString.replaceAll("[{}\"]", "");
            String[] parts = requestString.split(",");

            if (parts.length < 3) {
                throw new IllegalArgumentException("JSON must contain exactly 3 parameters: x, y, r");
            }

            Map<String, String> params = new HashMap<>();

            for (String part : parts) {
                String[] keyValue = part.split(":");
                if (keyValue.length == 2) {
                    String key = keyValue[0].trim().toLowerCase();
                    String value = keyValue[1].trim().replace(",", ".");
                    params.put(key, value);
                }
            }

            if (!params.containsKey("x") || !params.containsKey("y") || !params.containsKey("r")) {
                throw new IllegalArgumentException("JSON must contain all three parameters: x, y, r");
            }

            String xStr = params.get("x");
            String yStr = params.get("y");
            String rStr = params.get("r");

            this.originalYString = yStr;

            if (!yStr.matches("^-?\\d*\\.?\\d+$")) {
                throw new IllegalArgumentException("Y must be a valid decimal number");
            }
            if (yStr.length() > 100) {
                throw new IllegalArgumentException("The Y value is too long (maximum 100 characters)");
            }

            BigDecimal x = new BigDecimal(xStr, MC);
            BigDecimal y = new BigDecimal(yStr, MC);
            BigDecimal r = new BigDecimal(rStr, MC);

            return new BigDecimal[] { x, y, r };
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JSON format: " + e.getMessage());
        }
    }

    @Override
    public String getOriginalYString() {
        return originalYString;
    }
}