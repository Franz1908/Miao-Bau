package com.example.miaobau.utils;

import com.example.miaobau.model.ProductBean;

import java.math.BigDecimal;
import java.util.List;

/*
 * Utility di supporto per la gestione dei prodotti lato admin.
 * Raccoglie logica condivisa tra i controller di inserimento e modifica
 */
public class ProductValidator {

    /*
     * Valida che un campo di testo non superi la lunghezza massima consentita
     * (di solito la stessa del VARCHAR nel DB), aggiungendo un errore alla lista
     * se lo supera.
     */
    public static void validateLenght(String value, String fieldName, int maxLength, List<String> errors) {
        if (value != null && value.length() > maxLength) {   // null-safe: salta se il campo manca
            errors.add(fieldName + " è troppo lungo (massimo " + maxLength + " caratteri)");
        }
    }

    /*
     * Costruisce un ProductBean a partire dai valori inviati dal form.
     * productID è opzionale: presente in modifica, assente (null) in inserimento
     * per questo lo si imposta solo se non è null.
     */
    public static ProductBean buildProduct(Integer productID, String name, String brand, String description, Integer categoryId, Integer speciesId, BigDecimal price, BigDecimal vat,
                                           boolean onSale, BigDecimal discountPercentage, String image, BigDecimal weight, String ingredients, String size, String color, String material) {
        ProductBean product = new ProductBean();

        // productID solo in modifica: in inserimento è null (lo genera il DB)
        if (productID != null) product.setProductID(productID);

        // Campi obbligatori: impostati direttamente
        product.setName(name);
        product.setBrand(brand);
        product.setDescription(description);

        // category e species impostati solo se presenti
        if (categoryId != null) product.setCategoryID(categoryId);
        if (speciesId != null) product.setSpeciesID(speciesId);

        // Prezzo/IVA/sconto: numerici già convertiti a monte (BigDecimal)
        product.setPrice(price);
        product.setVat(vat);
        product.setOnSale(onSale);
        product.setDiscountPercentage(discountPercentage);

        // Campi di testo OPZIONALI: emptyToNull trasforma "" in null, così nel DB
        // si salva NULL (assenza di valore) invece di una stringa vuota.
        product.setImage(ParseUtil.emptyToNull(image));
        product.setWeight(weight);
        product.setIngredients(ParseUtil.emptyToNull(ingredients));
        product.setSize(ParseUtil.emptyToNull(size));
        product.setColor(ParseUtil.emptyToNull(color));
        product.setMaterial(ParseUtil.emptyToNull(material));

        return product;
    }

}