package com.example.miaobau.control;

import com.example.miaobau.dao.AddressDAO;
import com.example.miaobau.model.AddressBean;
import com.example.miaobau.model.CustomerBean;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/secure/address/delete")
public class AddressDeleteController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerBean customerBean = (CustomerBean) request.getSession().getAttribute("customer");
        Integer addressID = ParseUtil.parseIntOrNull(request.getParameter("addressId"));
        AddressDAO addressDAO = new AddressDAO();

        try {
            if (addressID != null) {
                AddressBean addressBean = addressDAO.doRetriveByID(addressID);
                if (addressBean.getCustomerID() == customerBean.getCustomerID()) {
                    addressDAO.doDelete(addressID);
                }
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        response.sendRedirect(request.getContextPath() + "/account");
    }
}
