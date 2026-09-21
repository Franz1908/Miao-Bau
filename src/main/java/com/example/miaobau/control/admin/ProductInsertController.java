package com.example.miaobau.control.admin;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.model.ProductBean;
import com.example.miaobau.utils.ParseUtil;
import com.example.miaobau.utils.ProductValidator;

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

/*
 * Controller per l'inserimento di un nuovo prodotto, lato admin.
 * Sotto /admin/*, protetto dal filtro. doGet mostra il form; doPost valida tutti
 * i campi (obbligatori, formati, coerenza sconto, lunghezze) e, se tutto è valido,
 * costruisce il ProductBean e lo salva.
 */
@WebServlet("/admin/product/insert")
public class ProductInsertController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lettura di tutti i parametri del form. onSale è una checkbox: presente nella
        // request solo se spuntata, quindi "!= null" significa "è spuntata".
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

        // --- Validazione dei campi obbligatori ---
        if (name == null || name.isBlank()) {
            errors.add("Inserire un nome");
        }

        // Categoria/specie: conversione sicura, errore se mancante/non valida.
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

        // Prezzo: numerico e positivo.
        BigDecimal price = ParseUtil.parseBigDecimalOrNull(priceStr);
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            errors.add("Inserire un prezzo valido");
        }

        // IVA: numerica e nell'intervallo 0-100.
        BigDecimal vat = ParseUtil.parseBigDecimalOrNull(vatStr);
        if (vat == null || vat.compareTo(BigDecimal.ZERO) < 0 || vat.compareTo(new BigDecimal("100")) > 0) {
            errors.add("Inserire un'IVA valida");
        }

        // --- Coerenza dello sconto ---
        // Se in sconto, la percentuale deve essere valida (0 < x <= 100).
        // Se NON in sconto, la percentuale va azzerata (coerenza col vincolo DB).
        BigDecimal discountPercentage = ParseUtil.parseBigDecimalOrNull(discountStr);
        if (onSale) {
            if (discountPercentage == null
                    || discountPercentage.compareTo(BigDecimal.ZERO) <= 0
                    || discountPercentage.compareTo(new BigDecimal("100")) > 0) {
                errors.add("Percentuale di sconto non valida");
            }
        } else {
            discountPercentage = null;   // niente sconto -> nessuna percentuale
        }

        // Peso OPZIONALE: valido solo se, quando presente, è un numero positivo.
        BigDecimal weight = ParseUtil.parseBigDecimalOrNull(weightStr);
        if (weightStr != null && !weightStr.isBlank()) {          // l'utente ha scritto qualcosa
            if (weight == null) {
                errors.add("Il peso inserito non è valido");       // caso 1: non convertibile
            } else if (weight.compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Il peso deve essere maggiore di zero"); // caso 2: convertibile ma non positivo
            }
        }

        // Lunghezze massime (coerenti coi VARCHAR del DB): controllo lato server.
        ProductValidator.validateLenght(name, "Nome", 150, errors);
        ProductValidator.validateLenght(brand, "Marca", 50, errors);
        ProductValidator.validateLenght(description, "Descrizione", 2500, errors);
        ProductValidator.validateLenght(ingredients, "Ingredienti", 2500, errors);

        // Un solo punto di uscita in caso di errori: torna al form con la lista.
        if (!errors.isEmpty()) {
            request.setAttribute("errorMessage", errors);
            request.getRequestDispatcher("/view/admin/Insert.jsp").forward(request, response);
            return;
        }

        // Tutto valido: costruisce il bean (productID null = nuovo) e salva.
        ProductBean product = ProductValidator.buildProduct(null, name, brand, description,
                categoryId, speciesId, price, vat, onSale, discountPercentage,
                image, weight, ingredients, size, color, material);

        try {
            new ProductDAO().doSave(product);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        response.sendRedirect(request.getContextPath() + "/admin/catalog");
    }

    // Mostra il form di inserimento.
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/admin/Insert.jsp");
        dispatcher.forward(request, response);
    }

}