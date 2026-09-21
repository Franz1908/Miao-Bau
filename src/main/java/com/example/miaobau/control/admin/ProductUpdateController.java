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
 * Controller per la MODIFICA di un prodotto esistente, lato admin.
 * Sotto /admin/*, protetto dal filtro. doGet precompila il form coi dati attuali
 * del prodotto; doPost valida (stessa logica dell'inserimento) e aggiorna.
 * In caso di errori, ripopola il form coi dati inseriti (così l'admin non riscrive
 * tutto) tramite l'helper forwardWithErrors.
 */
@WebServlet("/admin/product/update")
public class ProductUpdateController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // L'id del prodotto da modificare (campo hidden del form). Senza, non si sa
        // cosa aggiornare
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

        // --- Validazione (identica all'inserimento) ---
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

        // Coerenza sconto: se in sconto la percentuale dev'essere valida; altrimenti
        // si azzera (coerenza col vincolo CHECK del DB).
        BigDecimal discountPercentage = ParseUtil.parseBigDecimalOrNull(discountStr);
        if (onSale) {
            if (discountPercentage == null
                    || discountPercentage.compareTo(BigDecimal.ZERO) <= 0
                    || discountPercentage.compareTo(new BigDecimal("100")) > 0) {
                errors.add("Percentuale di sconto non valida");
            }
        } else {
            discountPercentage = null;
        }

        // Peso opzionale: due casi separati correttamente (non convertibile / non
        // positivo). compareTo chiamato solo nel ramo else, dove weight non è null.
        BigDecimal weight = ParseUtil.parseBigDecimalOrNull(weightStr);
        if (weightStr != null && !weightStr.isBlank()) {
            if (weight == null) {
                errors.add("Il peso inserito non è valido");
            } else if (weight.compareTo(BigDecimal.ZERO) <= 0) {
                errors.add("Il peso deve essere maggiore di zero");
            }
        }

        // Lunghezze massime dei campi.
        ProductValidator.validateLenght(name, "Nome", 150, errors);
        ProductValidator.validateLenght(brand, "Marca", 50, errors);
        ProductValidator.validateLenght(description, "Descrizione", 2500, errors);
        ProductValidator.validateLenght(ingredients, "Ingredienti", 2500, errors);

        // In caso di errori: ricostruisce il bean coi dati inseriti (compreso il
        // productID) e torna al form di modifica ripopolato + errori. Così l'admin
        // corregge senza riscrivere tutto.
        if (!errors.isEmpty()) {
            forwardWithErrors(request, response, ProductValidator.buildProduct(productID, name, brand, description,
                    categoryId, speciesId, price, vat, onSale, discountPercentage,
                    image, weight, ingredients, size, color, material), errors);
            return;
        }

        // Tutto valido: costruisce il bean CON il productID (è una modifica, non un
        // inserimento) e aggiorna.
        ProductBean product = ProductValidator.buildProduct(productID, name, brand, description,
                categoryId, speciesId, price, vat, onSale, discountPercentage,
                image, weight, ingredients, size, color, material);

        try {
            new ProductDAO().doUpdate(product);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        response.sendRedirect(request.getContextPath() + "/admin/catalog");
    }

    /*
     * Mostra il form di modifica precompilato. Recupera il prodotto per id con
     * doRetrieveByIdForAdmin (versione admin: include anche i prodotti cancellati,
     * così l'admin può modificare/ripristinare un soft-deleted).
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer productID = ParseUtil.parseIntOrNull(request.getParameter("productId"));

        if (productID == null) {
            response.sendRedirect(request.getContextPath() + "/admin/catalog");
            return;
        }

        ProductDAO productDAO = new ProductDAO();
        try {
            // Recupera anche se cancellato (a differenza del cliente).
            ProductBean productBean = productDAO.doRetrieveByIdForAdmin(productID);
            request.setAttribute("product", productBean);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/admin/Update.jsp");
        dispatcher.forward(request, response);
    }

    // Helper: torna al form di modifica coi dati inseriti (ripopolamento) e gli
    // errori, per non far riscrivere tutto all'admin dopo un errore di validazione.
    private void forwardWithErrors(HttpServletRequest request, HttpServletResponse response,ProductBean product, List<String> errors) throws ServletException, IOException {
        request.setAttribute("product", product);
        request.setAttribute("errorMessage", errors);
        request.getRequestDispatcher("/view/admin/Update.jsp").forward(request, response);
    }

}