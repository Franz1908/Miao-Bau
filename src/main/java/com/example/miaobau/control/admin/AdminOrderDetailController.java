package com.example.miaobau.control.admin;

import com.example.miaobau.dao.AddressDAO;
import com.example.miaobau.dao.OrdersDAO;
import com.example.miaobau.model.AddressBean;
import com.example.miaobau.model.OrdersBean;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/order-detail")
public class AdminOrderDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer orderID = ParseUtil.parseIntOrNull(request.getParameter("orderId"));
        OrdersDAO ordersDAO = new OrdersDAO();
        AddressBean address;

        if (orderID == null) {
            response.sendRedirect(request.getContextPath() + "/admin/orders");
            return;
        }

        try {
            OrdersBean order = ordersDAO.doRetrieveByIdWithCustomer(orderID);
            if (order == null) {
                response.sendRedirect(request.getContextPath() + "/admin/orders");
                return;
            }
            order.setItems(ordersDAO.doRetrieveItemsByOrder(orderID));
            address = new AddressDAO().doRetriveByID(order.getAddressID());
            request.setAttribute("order", order);
            request.setAttribute("address", address);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/admin/OrderDetail.jsp");
        dispatcher.forward(request, response);
    }
}
