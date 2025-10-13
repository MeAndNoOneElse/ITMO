package org.example;

import java.math.BigDecimal;

public interface RequestParser {
  BigDecimal[] getBigDecimals(String requestString) throws IllegalArgumentException;
  String getOriginalYString();
}