package com.example.miaobau.utils;

import com.example.miaobau.model.ProductBean;

import java.math.BigDecimal;
import java.util.List;

public class ProductValidator {

    // valida la lunghezza dei campi
    public static void validateLenght(String value, String fieldName, int maxLength, List<String> errors) {
        if (value != null && value.length() > maxLength) {
            errors.add(fieldName + " è troppo lungo (massimo " + maxLength + " caratteri)");
        }
    }

    // costruisce il ProductBean dai valori inviati (per salvataggio o per ripopolare il form dopo un errore)
    public static ProductBean buildProduct(Integer productID, String name, String brand, String description, Integer categoryId, Integer speciesId, BigDecimal price, BigDecimal vat,
                                     boolean onSale, BigDecimal discountPercentage, String image, BigDecimal weight, String ingredients, String size, String color, String material) {
        ProductBean product = new ProductBean();
        if (productID != null) product.setProductID(productID);
        product.setName(name);
        product.setBrand(brand);
        product.setDescription(description);
        if (categoryId != null) product.setCategoryID(categoryId);
        if (speciesId != null) product.setSpeciesID(speciesId);
        product.setPrice(price);
        product.setVat(vat);
        product.setOnSale(onSale);
        product.setDiscountPercentage(discountPercentage);
        product.setImage(ParseUtil.emptyToNull(image));
        product.setWeight(weight);
        product.setIngredients(ParseUtil.emptyToNull(ingredients));
        product.setSize(ParseUtil.emptyToNull(size));
        product.setColor(ParseUtil.emptyToNull(color));
        product.setMaterial(ParseUtil.emptyToNull(material));
        return product;
    }

}
