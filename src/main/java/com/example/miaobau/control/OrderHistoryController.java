package com.example.miaobau.control;

import com.example.miaobau.dao.OrdersDAO;
import com.example.miaobau.model.CustomerBean;
import com.example.miaobau.model.OrdersBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/orders")
public class OrderHistoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");

        if(customer == null){
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            List<OrdersBean> orders = new OrdersDAO().doRetriveByCustomer(customer.getCustomerID());
            request.setAttribute("orders", orders);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Orders.jsp");
        dispatcher.forward(request, response);

    }
}
