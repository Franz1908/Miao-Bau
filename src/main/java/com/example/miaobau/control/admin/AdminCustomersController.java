package com.example.miaobau.control.admin;

import com.example.miaobau.dao.CustomerDAO;
import com.example.miaobau.model.CustomerBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/admin/customers")
public class AdminCustomersController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<CustomerBean> customers = new CustomerDAO().doRetrieveAll();
            request.setAttribute("customers", customers);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/admin/Customers.jsp");
        dispatcher.forward(request, response);
    }
}
