package com.example.miaobau.control;

import com.example.miaobau.dao.AddressDAO;
import com.example.miaobau.dao.OrdersDAO;
import com.example.miaobau.model.AddressBean;
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

/*
 * Controller della fattura di un ordine (lato cliente).
 * Recupera lo stesso set di dati del dettaglio ordine (testata + indirizzo +
 * righe) ma fa forward a Invoice.jsp.
 */
@WebServlet("/secure/invoice")
public class InvoiceController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Cliente garantito dal filtro /secure: usato per il controllo di proprietà.
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");
        OrdersDAO ordersDAO = new OrdersDAO();

        // Id ordine dall'URL, conversione sicura: se manca/non valido -> redirect
        // uniforme allo storico.
        Integer orderID = ParseUtil.parseIntOrNull(request.getParameter("orderId"));
        if (orderID == null) {
            response.sendRedirect(request.getContextPath() + "/secure/orders");
            return;
        }
        try {
            OrdersBean order = ordersDAO.doRetrieveByIdWithCustomer(orderID);
            // Ordine inesistente O non del cliente loggato mando errore 403.
            if (order == null || order.getCustomerID() != customer.getCustomerID()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);  // 403
                return;
            }
            // Ordine valido e di proprietà: carico indirizzo (senza filtro is_deleted,
            // per mostrarlo anche se cancellato) e righe congelate.
            AddressBean address = new AddressDAO().doRetriveByID(order.getAddressID());
            order.setItems(ordersDAO.doRetrieveItemsByOrder(orderID));
            request.setAttribute("order", order);
            request.setAttribute("address", address);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
        // Vista fattura.
        request.getRequestDispatcher("/view/Invoice.jsp").forward(request, response);
    }
}