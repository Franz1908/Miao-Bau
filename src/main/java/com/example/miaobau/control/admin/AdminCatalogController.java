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
import java.sql.SQLException;
import java.util.List;

/*
 * Controller del catalogo lato ADMIN (elenco prodotti per la gestione).
 * Sotto /admin/*, quindi protetto dall'AdminFilter (solo admin loggati).
 * Differenza rispetto al catalogo cliente: usa doRetriveAllForAdmin, che
 * include ANCHE i prodotti soft-deleted, perché l'admin deve poterli vedere per
 * modificarli o ripristinarli. Il catalogo cliente invece li nasconde.
 */
@WebServlet("/admin/catalog")
public class AdminCatalogController extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();
        List<ProductBean> products;

        try {
            // Tutti i prodotti, cancellati inclusi (vista di gestione).
            products = productDAO.doRetriveAllForAdmin();
            request.setAttribute("adminProducts", products);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/admin/Catalog.jsp");
        dispatcher.forward(request, response);
    }

}