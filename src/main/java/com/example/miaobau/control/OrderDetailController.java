package com.example.miaobau.control;

import com.example.miaobau.dao.OrdersDAO;
import com.example.miaobau.model.CustomerBean;
import com.example.miaobau.model.OrdersBean;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/secure/order-detail")
public class OrderDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
        OrdersDAO ordersDAO = new OrdersDAO();

        Integer orderID = ParseUtil.parseIntOrNull(request.getParameter("orderId"));
        if (orderID == null) {
            response.sendRedirect(request.getContextPath() + "/secure/orders");
            return;
        }

        try {
            OrdersBean order = ordersDAO.doRetriveByID(orderID);

            if (order == null || order.getCustomerID() != customer.getCustomerID()) {
                response.sendRedirect(request.getContextPath() + "/secure/orders");
                return;
            }

            order.setItems(ordersDAO.doRetrieveItemsByOrder(orderID));
            request.setAttribute("order", order);

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        request.getRequestDispatcher("/view/OrderDetail.jsp").forward(request, response);
    }
}
