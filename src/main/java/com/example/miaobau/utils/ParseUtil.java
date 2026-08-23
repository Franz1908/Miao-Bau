package com.example.miaobau.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

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

    public static LocalDateTime parseDateFromOrNull(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(date.trim()).atStartOfDay();
        } catch (DateTimeParseException dtpe) {
            return null;
        }
    }

    public static LocalDateTime parseDateToOrNull(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        try{
            return LocalDate.parse(date.trim()).atTime(23, 59, 59);
        } catch (DateTimeParseException dtpe) {
            return null;
        }
    }

}
