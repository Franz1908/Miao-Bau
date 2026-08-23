package com.example.miaobau.control.admin;

import com.example.miaobau.dao.CustomerDAO;
import com.example.miaobau.dao.OrdersDAO;
import com.example.miaobau.model.CustomerBean;
import com.example.miaobau.model.OrdersBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/orders")
public class AdminOrdersController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            List<OrdersBean> orders = new OrdersDAO().doRetriveAll();
            request.setAttribute("orders", orders);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/admin/Orders.jsp");
        dispatcher.forward(request, response);
    }
}
