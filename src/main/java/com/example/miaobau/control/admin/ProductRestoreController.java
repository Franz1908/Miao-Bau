package com.example.miaobau.control.admin;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/*
 * Controller per il RIPRISTINO di un prodotto soft-deleted, lato admin.
 * Speculare a ProductDeleteController: annulla il soft delete (is_deleted = FALSE),
 * riportando il prodotto nel catalogo cliente.
 */
@WebServlet("/admin/product/restore")
public class ProductRestoreController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer productID = ParseUtil.parseIntOrNull(request.getParameter("productId"));

        if (productID == null) {
            response.sendRedirect(request.getContextPath() + "/admin/catalog");
            return;
        }

        try {
            new ProductDAO().doRestore(productID);   // annulla il soft delete
            response.sendRedirect(request.getContextPath() + "/admin/catalog");
        } catch (SQLException e) {
            throw new ServletException(e);
        }

    }

}