package com.example.miaobau.control.admin;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.model.ProductBean;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/product/update")
public class ProductUpdateController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer productID = ParseUtil.parseIntOrNull(request.getParameter("productId"));
        if (productID == null) {
            response.sendRedirect(request.getContextPath() + "/admin/catalog");
            return;
        }
        String name = request.getParameter("name");
        String brand = request.getParameter("brand");
        String description = request.getParameter("description");
        String categoryIdStr = request.getParameter("categoryId");
        String speciesIdStr = request.getParameter("speciesId");
        String priceStr = request.getParameter("price");
        String vatStr = request.getParameter("vat");
        boolean onSale = request.getParameter("onSale") != null;
        String discountStr = request.getParameter("discountPercentage");
        String image = request.getParameter("image");
        String weightStr = request.getParameter("weight");
        String ingredients = request.getParameter("ingredients");
        String size = request.getParameter("size");
        String color = request.getParameter("color");
        String material = request.getParameter("material");
        List<String> errors = new ArrayList<>();

        // Campi obbligatori
        if (name == null || name.isBlank()) {
            errors.add("Inserire un nome");
        }

        Integer categoryId = ParseUtil.parseIntOrNull(categoryIdStr);
        if (categoryId == null) {
            errors.add("Inserire una categoria valida");
        }

        Integer speciesId = ParseUtil.parseIntOrNull(speciesIdStr);
        if (speciesId == null) {
            errors.add("Inserire una specie valida");
        }

        if (brand == null || brand.isBlank()) {
            errors.add("Inserire una marca valida");
        }

        if (description == null || description.isBlank()) {
            errors.add("Inserire una descrizione valida");
        }

        BigDecimal price = ParseUtil.parseBigDecimalOrNull(priceStr);
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Inserire un prezzo valido");
        }

        BigDecimal vat = ParseUtil.parseBigDecimalOrNull(vatStr);
        if (vat == null || vat.compareTo(BigDecimal.ZERO) < 0 || vat.compareTo(new BigDecimal("100")) > 0) {
            errors.add("Inserire un'IVA valida");
        }

        // Coerenza sconto
        BigDecimal discountPercentage = ParseUtil.parseBigDecimalOrNull(discountStr);
        if (onSale) {
            if (discountPercentage == null
                    || discountPercentage.compareTo(BigDecimal.ZERO) <= 0
                    || discountPercentage.compareTo(new BigDecimal("100")) > 0) {
                errors.add("Percentuale di sconto non valida");
            }
        } else {
            // se non è in sconto, la percentuale non deve essere valorizzata
            discountPercentage = null;
        }

        // Peso opzionale: valido solo se, quando presente, è un numero positivo
        BigDecimal weight = ParseUtil.parseBigDecimalOrNull(weightStr);
        if (weightStr != null && !weightStr.isBlank()) {
            if (weight == null) {
                errors.add("Il peso inserito non è valido");
            } else if (weight.compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Il peso deve essere maggiore di zero");
            }
        }

        if (!errors.isEmpty()) {
            forwardWithErrors(request, response, buildProduct(productID, name, brand, description,
                    categoryId, speciesId, price, vat, onSale, discountPercentage,
                    image, weight, ingredients, size, color, material), errors);
            return;
        }

        //Controllo lunghezza caratteri
        validateLenght(name, "Nome", 150, errors);
        validateLenght(brand, "Marca", 50, errors);
        validateLenght(description, "Descrizione", 2500, errors);
        validateLenght(ingredients, "Ingredienti", 2500, errors);

        if (!errors.isEmpty()) {
            forwardWithErrors(request, response, buildProduct(productID, name, brand, description,
                    categoryId, speciesId, price, vat, onSale, discountPercentage,
                    image, weight, ingredients, size, color, material), errors);
            return;
        }

        // Creazione e popolamento del ProductBean
        ProductBean product = buildProduct(productID, name, brand, description,
                categoryId, speciesId, price, vat, onSale, discountPercentage,
                image, weight, ingredients, size, color, material);

        try {
            new ProductDAO().doUpdate(product);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        response.sendRedirect(request.getContextPath() + "/admin/catalog");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer productID = ParseUtil.parseIntOrNull(request.getParameter("productId"));

        if (productID == null) {
            response.sendRedirect(request.getContextPath() + "/admin/catalog");
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        try {
            ProductBean productBean = productDAO.doRetrieveByIdForAdmin(productID);
            request.setAttribute("product", productBean);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/admin/Update.jsp");
        dispatcher.forward(request, response);
    }

    // helper locale: trasforma stringa vuota in null (per non salvare "" al posto di NULL)
    private String emptyToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }

    //helper locale: valida la lunghezza dei campi
    private void validateLenght(String value, String fieldName, int maxLength, List<String> errors) {
        if (value != null && value.length() > maxLength) {
            errors.add(fieldName + " è troppo lungo (massimo " + maxLength + " caratteri)");
        }
    }

    // costruisce il ProductBean dai valori inviati (per salvataggio o per ripopolare il form dopo un errore)
    private ProductBean buildProduct(int productID, String name, String brand, String description, Integer categoryId, Integer speciesId, BigDecimal price, BigDecimal vat,
                                     boolean onSale, BigDecimal discountPercentage, String image, BigDecimal weight, String ingredients, String size, String color, String material) {
        ProductBean product = new ProductBean();
        product.setProductID(productID);
        product.setName(name);
        product.setBrand(brand);
        product.setDescription(description);
        if (categoryId != null) product.setCategoryID(categoryId);
        if (speciesId != null) product.setSpeciesID(speciesId);
        product.setPrice(price);
        product.setVat(vat);
        product.setOnSale(onSale);
        product.setDiscountPercentage(discountPercentage);
        product.setImage(emptyToNull(image));
        product.setWeight(weight);
        product.setIngredients(emptyToNull(ingredients));
        product.setSize(emptyToNull(size));
        product.setColor(emptyToNull(color));
        product.setMaterial(emptyToNull(material));
        return product;
    }

    // in caso di errori, rimanda al form di modifica con i dati inseriti e l'elenco errori
    private void forwardWithErrors(HttpServletRequest request, HttpServletResponse response,ProductBean product, List<String> errors) throws ServletException, IOException {
        request.setAttribute("product", product);
        request.setAttribute("errorMessage", errors);
        request.getRequestDispatcher("/view/admin/Update.jsp").forward(request, response);
    }

}
