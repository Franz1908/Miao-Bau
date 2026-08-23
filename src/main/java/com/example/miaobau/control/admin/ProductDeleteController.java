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

@WebServlet("/admin/product/delete")
public class ProductDeleteController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer productID = ParseUtil.parseIntOrNull(request.getParameter("productId"));

        if (productID == null) {
            response.sendRedirect(request.getContextPath() + "/admin/catalog");
            return;
        }

        try {
            new ProductDAO().doDelete(productID);
            response.sendRedirect(request.getContextPath() + "/admin/catalog");
        } catch (SQLException e) {
            throw new ServletException(e);
        }

    }
}
