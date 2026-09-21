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

/*
 * Controller dello storico ordini del cliente.
 * Mappato sotto /secure/*, quindi il CustomerFilter garantisce già che ci sia un
 * cliente loggato.
 */
@WebServlet("/secure/orders")
public class OrderHistoryController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        // Il cliente c'è di sicuro (garantito dal filtro /secure), lo uso per
        // recuperare i suoi ordini tramite il suo id.
        CustomerBean customer = (CustomerBean) session.getAttribute("customer");

        try {
            // Recupera gli ordini del cliente loggato (già ordinati dal più recente
            // nel DAO). La lista può essere vuota (nessun ordine): la JSP mostrerà
            // lo stato vuoto.
            List<OrdersBean> orders = new OrdersDAO().doRetriveByCustomer(customer.getCustomerID());
            request.setAttribute("orders", orders);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Orders.jsp");
        dispatcher.forward(request, response);

    }
}