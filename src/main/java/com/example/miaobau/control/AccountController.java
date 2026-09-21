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

/*
 * Controller della pagina account.
 * È una pagina "a doppio stato", e per questo non sta sotto /secure: deve
 * funzionare sia per i loggati sia per i non loggati. Quando l'utente è loggato,
 * la pagina fa da cruscotto: mostra i dati e i suoi indirizzi. Gli indirizzi si recuperano dal db
 */
@WebServlet("/account")
public class AccountController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerBean customerBean = (CustomerBean) request.getSession().getAttribute("customer");

        // Solo se loggato: recupera gli indirizzi del cliente e li passa alla JSP
        // (per la sezione "I miei indirizzi"). Se non loggato, non serve: la JSP
        // mostrerà i link accedi/registrati e questo blocco viene saltato.
        if (customerBean != null) {
            try {
                List<AddressBean> addresses = new AddressDAO().doRetrieveByCustomer(customerBean.getCustomerID());
                request.setAttribute("addresses", addresses);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }

        // Sempre forward alla stessa JSP: sarà lei a mostrare la versione giusta
        // (dati+indirizzi se customer presente, accedi/registrati se assente).
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Account.jsp");
        dispatcher.forward(request, response);
    }
}