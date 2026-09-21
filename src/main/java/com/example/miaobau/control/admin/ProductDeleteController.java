package com.example.miaobau.control.admin;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/*
 * Controller per la cancellazione (SOFT delete) di un prodotto, lato admin.
 * Sotto /admin/*, protetto dal filtro. Non elimina fisicamente: il DAO imposta
 is_deleted = TRUE, così il prodotto sparisce dal catalogo cliente ma resta nel
 DB (gli ordini storici che lo  * riferiscono restano validi) e l'admin può ripristinarlo.
 */
@WebServlet("/admin/product/delete")
public class ProductDeleteController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer productID = ParseUtil.parseIntOrNull(request.getParameter("productId"));

        // Id mancante o non valido: torna al catalogo admin senza fare nulla (guard clause).
        if (productID == null) {
            response.sendRedirect(request.getContextPath() + "/admin/catalog");
            return;
        }

        try {
            new ProductDAO().doDelete(productID);   // soft delete
            // Torna al catalogo aggiornato.
            response.sendRedirect(request.getContextPath() + "/admin/catalog");
        } catch (SQLException e) {
            throw new ServletException(e);
        }

    }
}