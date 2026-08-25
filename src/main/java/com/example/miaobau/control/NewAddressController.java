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
import java.util.ArrayList;
import java.util.List;

@WebServlet("/secure/address/new")
public class NewAddressController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String street = request.getParameter("street");
        String civicNumber = request.getParameter("civicNumber");
        String postalCode = request.getParameter("postalCode");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        CustomerBean customerBean = (CustomerBean) request.getSession().getAttribute("customer");
        List<String> errors = new ArrayList<>();

        if (street == null || street.isBlank()) {
            errors.add("Inserire una via valida");
        }

        if (civicNumber == null || civicNumber.isBlank()) {
            errors.add("Inserire un numero civico valido");
        }

        if (postalCode == null || postalCode.isBlank() || !postalCode.matches("\\d{5}")) {
            errors.add("Inserire un CAP valido");
        }

        if (city == null || city.isBlank()) {
            errors.add("Inserire una città valida");
        }

        if (country == null || country.isBlank()) {
            errors.add("Inserire una paese valido");
        }

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/view/NewAddress.jsp").forward(request, response);
            return;
        }

        AddressBean addressBean = new AddressBean();
        addressBean.setCity(city);
        addressBean.setPostalCode(postalCode);
        addressBean.setStreet(street);
        addressBean.setCivicNumber(civicNumber);
        addressBean.setCountry(country);
        addressBean.setCustomerID(customerBean.getCustomerID());

        try {
            new AddressDAO().doSave(addressBean);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        response.sendRedirect(request.getContextPath() + "/account");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/NewAddress.jsp");
        dispatcher.forward(request, response);
    }

}
