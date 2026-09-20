package com.example.miaobau.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

/*
 * Utility per l'hashing sicuro delle password con l'algoritmo PBKDF2.
 */
public class PasswordUtil {

    // Numero di iterazioni dell'algoritmo: più è alto, più è lento (e sicuro)
    // calcolare un hash
    private static final int ITERATIONS = 100_000;

    // Lunghezza in byte del salt (valore casuale unico per ogni password).
    private static final int SALT_DIMENSION = 16;

    // Lunghezza in bit dell'hash prodotto.
    private static final int DIMENSION = 256;

    /*
     * Calcola l'hash di una password in chiaro, generando un salt casuale.
     * Restituisce una stringa "salt:hash" (entrambi in Base64) da salvare nel DB.
     */
    public static String hashPassword(String password){
        // Genera un salt casuale diverso per ogni password: così due utenti
        // con la stessa password avranno hash diversi.
        byte[] salt = new byte[SALT_DIMENSION];
        new SecureRandom().nextBytes(salt);   // SecureRandom = casualità robusta per la crittografia

        // Prepara i parametri per PBKDF2: password, salt, iterazioni, lunghezza hash.
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, DIMENSION);

        try {
            // Factory per l'algoritmo PBKDF2 con funzione hash SHA-256.
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            // Esegue effettivamente il calcolo e ottiene i byte dell'hash.
            byte[] hash = factory.generateSecret(spec).getEncoded();

            // Salva salt e hash insieme (Base64 per averli come testo), separati da ':'.
            // Il salt va salvato accanto all'hash perché servirà per verificare la password.
            return Base64.getEncoder().encodeToString(salt) + ":" + Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException  | InvalidKeySpecException e) {
            // Se l'algoritmo non è disponibile o i parametri sono errati
            throw new RuntimeException(e);
        }
    }

    /*
     * Verifica se una password in chiaro corrisponde all'hash salvato.
     * 'stored' è la stringa "salt:hash" prodotta da hashPassword.
     */
    public static boolean verifyPassword(String password, String stored){
        // Ricava salt e hash separando la stringa salvata sul ':'.
        String[] parts = stored.split(":");

        // Decodifica dal Base64 per tornare ai byte originali.
        byte[] salt = Base64.getDecoder().decode(parts[0]);
        byte[] storedHash = Base64.getDecoder().decode(parts[1]);

        // Ricalcola l'hash della password fornita usando LO STESSO salt di quando
        // fu creata: solo così i due hash possono risultare uguali se la password è giusta.
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, DIMENSION);

        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] computedHash = factory.generateSecret(spec).getEncoded();

            // Confronta i byte dell'hash appena calcolato con quello salvato:
            // se coincidono, la password è corretta.
            return Arrays.equals(storedHash, computedHash);
        } catch (NoSuchAlgorithmException  | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}
