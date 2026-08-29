package com.example.miaobau.control;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.model.ProductBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/home")
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();

        try {
            List<ProductBean> discountedProducts = productDAO.doRetriveDiscountedProducts();
            List<ProductBean> popularProducts = productDAO.doRetrivePopularProducts();
            request.setAttribute("discountedProducts", discountedProducts);
            request.setAttribute("popularProducts", popularProducts);
        } catch (SQLException e) {
            throw new ServletException(e);
        }


        RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
        dispatcher.forward(request, response);
    }
}
