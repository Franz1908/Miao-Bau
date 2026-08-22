package com.example.miaobau.utils;

import java.math.BigDecimal;

public class ParseUtil {

    // per campi opzionali: null se vuoto o non valido
    public static BigDecimal parseBigDecimalOrNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // per interi opzionali/con controllo
    public static Integer parseIntOrNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
