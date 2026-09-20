package com.example.miaobau.utils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/*
 * Utility per convertire in modo SICURO i parametri delle richieste (sempre String)
 * nei tipi Java corrispondenti. I parametri arrivano dal client come stringhe e
 * possono essere assenti, vuoti o malformati: questi metodi centralizzano la
 * conversione restituendo null invece di lanciare eccezioni.
 */
public class ParseUtil {

    // Converte in BigDecimal (per prezzi, peso, ecc.): null se vuoto o non numerico.
    // Utile per i campi opzionali e per evitare NumberFormatException nei controller.
    public static BigDecimal parseBigDecimalOrNull(String value) {
        if (value == null || value.isBlank()) {   // parametro assente o vuoto
            return null;
        }
        try {
            return new BigDecimal(value.trim());   // trim: tollera spazi accidentali
        } catch (NumberFormatException e) {
            return null;                           // testo non numerico -> null invece di crashare
        }
    }

    // Converte in Integer (per id, quantità, ecc.): null se vuoto o non numerico.
    // Restituisce Integer (oggetto) e non int proprio per poter tornare null.
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

    // Converte una data (formato "AAAA-MM-GG") nell'ISTANTE DI INIZIO giornata (00:00).
    // Pensato per il filtro "dal giorno X": include tutto ciò che accade da inizio giornata.
    public static LocalDateTime parseDateFromOrNull(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(date.trim()).atStartOfDay();   // 00:00:00 del giorno
        } catch (DateTimeParseException dtpe) {
            return null;                                          // data malformata -> null
        }
    }

    // Converte una data nell'ISTANTE DI FINE giornata (23:59:59).
    // Pensato per il filtro "fino al giorno X": include tutto ciò che accade
    // entro la fine di quel giorno.
    public static LocalDateTime parseDateToOrNull(String date) {
        if (date == null || date.isBlank()) {
            return null;
        }
        try{
            return LocalDate.parse(date.trim()).atTime(23, 59, 59);   // fine giornata
        } catch (DateTimeParseException dtpe) {
            return null;
        }
    }

    // Normalizza i campi di testo opzionali: una stringa vuota diventa null,
    // così nel DB si salva NULL (assenza di valore) invece di "" (stringa vuota).
    // Fa anche il trim per non salvare spazi inutili.
    public static String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

}