package com.example.miaobau.control;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.model.CartBean;
import com.example.miaobau.model.ProductBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cart")
public class CartController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/view/Cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        CartBean cart = (CartBean) session.getAttribute("cart");

        if (cart == null) {
            cart = new CartBean();
            session.setAttribute("cart", cart);
        }

        String action = request.getParameter("action");

        if ("clear".equals(action)) {
            cart.clearCart();
        } else if (action != null) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            switch (action) {
                case "add":
                case "increase":
                    add(cart, productId);
                    break;
                case "remove":
                    remove(cart, productId);
                    break;
                case "decrease":
                    decrease(cart, productId);
                    break;
            }
        }

        response.sendRedirect(request.getContextPath() + "/cart");
    }

    private void add(CartBean cart, int productId) throws ServletException {
        try {
            ProductBean product = new ProductDAO().doRetrieveById(productId);
            cart.addToCart(product);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    private void remove(CartBean cart, int productId) {
        cart.removeFromCart(productId);
    }

    private void decrease(CartBean cart, int productId) {
        cart.decreaseQuantity(productId);
    }
    
}
