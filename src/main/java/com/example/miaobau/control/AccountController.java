package com.example.miaobau.control;

import com.example.miaobau.dao.AddressDAO;
import com.example.miaobau.model.AddressBean;
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

@WebServlet("/account")
public class AccountController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerBean customerBean = (CustomerBean) request.getSession().getAttribute("customer");

        if (customerBean != null) {
            try {
                List<AddressBean> addresses = new AddressDAO().doRetrieveByCustomer(customerBean.getCustomerID());
                request.setAttribute("addresses", addresses);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Account.jsp");
        dispatcher.forward(request, response);
    }
}
