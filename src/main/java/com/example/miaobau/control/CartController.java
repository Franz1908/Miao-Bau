package com.example.miaobau.control;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.model.CartBean;
import com.example.miaobau.model.ProductBean;
import com.example.miaobau.utils.ParseUtil;

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
            Integer productID = ParseUtil.parseIntOrNull(request.getParameter("productId"));
            if (productID != null) {
                switch (action) {
                    case "add":
                    case "increase":
                        Integer quantity = ParseUtil.parseIntOrNull(request.getParameter("quantity"));
                        add(cart, productID, quantity != null ? quantity : 1);
                        break;
                    case "remove":
                        remove(cart, productID);
                        break;
                    case "decrease":
                        decrease(cart, productID);
                        break;
                }
            }

        }

        String back = request.getHeader("Referer");
        if (back == null) {
            back = request.getContextPath() + "/catalog";
        }
        response.sendRedirect(back);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/view/Cart.jsp").forward(request, response);
    }

    private void add(CartBean cart, int productId, int quantity) throws ServletException {
        try {
            ProductBean product = new ProductDAO().doRetrieveById(productId);
            if (product == null) {
                return;
            }
            if(quantity < 1){
                quantity = 1;
            }
            cart.addToCart(product, quantity);
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
