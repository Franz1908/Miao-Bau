package com.example.miaobau.control;

import com.example.miaobau.dao.OrdersDAO;
import com.example.miaobau.model.CartBean;
import com.example.miaobau.model.CustomerBean;
import com.example.miaobau.model.OrdersBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet("/secure/checkout")
public class CheckoutController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        CustomerBean customerBean = (CustomerBean) session.getAttribute("customer");

        CartBean cart = (CartBean) session.getAttribute("cart");
        if(cart == null || cart.getCart().isEmpty()){
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        OrdersBean order = new OrdersBean();
        order.setCustomerID(customerBean.getCustomerID());
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(cart.getTotal());

        try {
            int orderID = new OrdersDAO().doSave(order, cart);
            cart.clearCart();
            response.sendRedirect(request.getContextPath() + "/secure/confirmation?orderId=" + orderID);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }
}
