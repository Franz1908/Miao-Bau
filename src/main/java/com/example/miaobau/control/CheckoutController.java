package com.example.miaobau.control;

import com.example.miaobau.dao.AddressDAO;
import com.example.miaobau.dao.OrdersDAO;
import com.example.miaobau.model.AddressBean;
import com.example.miaobau.model.CartBean;
import com.example.miaobau.model.CustomerBean;
import com.example.miaobau.model.OrdersBean;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/secure/checkout")
public class CheckoutController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        CustomerBean customerBean = (CustomerBean) session.getAttribute("customer");
        CartBean cart = (CartBean) session.getAttribute("cart");
        String addressChoice = request.getParameter("addressChoice");
        Integer addressID;
        AddressBean addressBean;
        AddressDAO addressDAO = new AddressDAO();
        List<String> errors = new ArrayList<>();

        if (cart == null || cart.getCart().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        if (addressChoice == null) {
            errors.add("Aggiungi o seleziona un indirizzo di spedizione");
            showSummaryWithErrors(request, response, customerBean.getCustomerID(), errors);
            return;
        }
        else if (addressChoice.equals("new")) {
            String street = request.getParameter("street");
            String civicNumber = request.getParameter("civicNumber");
            String postalCode = request.getParameter("postalCode");
            String city = request.getParameter("city");
            String country = request.getParameter("country");

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
                showSummaryWithErrors(request, response, customerBean.getCustomerID(), errors);
                return;
            }

            addressBean = new AddressBean();
            addressBean.setCity(city);
            addressBean.setPostalCode(postalCode);
            addressBean.setStreet(street);
            addressBean.setCivicNumber(civicNumber);
            addressBean.setCountry(country);
            addressBean.setCustomerID(customerBean.getCustomerID());
            try {
                 addressID = addressDAO.doSave(addressBean);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
        else {
            addressID = ParseUtil.parseIntOrNull(addressChoice);
            if (addressID == null) {
                response.sendRedirect(request.getContextPath() + "/secure/checkout");
                return;
            }

            try {
                addressBean = addressDAO.doRetriveByID(addressID);
            } catch (SQLException e) {
                throw new ServletException(e);
            }

            if (addressBean == null || addressBean.getCustomerID() != customerBean.getCustomerID()) {
                response.sendRedirect(request.getContextPath() + "/secure/checkout");
                return;
            }
        }

        OrdersBean ordersBean = new OrdersBean();
        ordersBean.setAddressID(addressID);
        ordersBean.setTotalPrice(cart.getTotal());
        ordersBean.setOrderDate(LocalDateTime.now());
        ordersBean.setCustomerID(customerBean.getCustomerID());

        try {
            int orderID = new OrdersDAO().doSave(ordersBean, cart);
            cart.clearCart();
            response.sendRedirect(request.getContextPath() + "/secure/confirmation?orderId=" + orderID);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CartBean cartBean = (CartBean) request.getSession().getAttribute("cart");
        CustomerBean customerBean = (CustomerBean) request.getSession().getAttribute("customer");

        if (cartBean == null || cartBean.getCart().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/cart");
            return;
        }

        try {
            List<AddressBean> addressesBean = new AddressDAO().doRetrieveByCustomer(customerBean.getCustomerID());
            request.setAttribute("addresses", addressesBean);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/OrderSummary.jsp");
        dispatcher.forward(request, response);
    }

    private void showSummaryWithErrors(HttpServletRequest request, HttpServletResponse response, int customerID, List<String> errors)
            throws ServletException, IOException {
        try {
            List<AddressBean> addresses = new AddressDAO().doRetrieveByCustomer(customerID);
            request.setAttribute("addresses", addresses);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        request.setAttribute("errors", errors);
        request.getRequestDispatcher("/view/OrderSummary.jsp").forward(request, response);
    }
}
