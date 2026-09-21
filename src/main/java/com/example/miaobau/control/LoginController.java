package com.example.miaobau.control;

import com.example.miaobau.dao.CustomerDAO;
import com.example.miaobau.model.CustomerBean;
import com.example.miaobau.utils.PasswordUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/*
 * Controller del login cliente.
 * doPost verifica le credenziali e, se corrette, mette il cliente in sessione;
 */
@WebServlet("/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        CustomerDAO customerDAO = new CustomerDAO();

        // Controllo di presenza dei campi.
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("loginError", "Email o password errati");
            request.getRequestDispatcher("/view/Login.jsp").forward(request, response);
            return;
        }
        email = email.trim();   // ora è sicuro, non è null

        try {
            // Recupera il cliente per email (include l'hash, che serve per la verifica).
            CustomerBean customerBean = customerDAO.doRetriveByEmail(email);

            // Login riuscito solo se: l'utente esiste E la password fornita, ri-hashata,
            // coincide con l'hash salvato. verifyPassword confronta gli hash.
            if(customerBean != null && PasswordUtil.verifyPassword(password, customerBean.getPasswordHash())){
                HttpSession session = request.getSession();

                // Azzero l'hash prima di mettere il bean in sessione
                customerBean.setPasswordHash(null);

                // Recupera l'eventuale destinazione salvata dal CustomerFilter (la pagina
                // /secure che l'utente voleva raggiungere prima di essere mandato al login).
                String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");

                // Mette il cliente in sessione: da qui è "loggato".
                session.setAttribute("customer", customerBean);

                // Se non c'era una destinazione salvata, vai all'account (default).
                if (redirectAfterLogin == null) {
                    response.sendRedirect(request.getContextPath() + "/account");
                    return;
                }
                // Altrimenti: consuma la destinazione (rimuovila) e
                // riportaci l'utente (es. torna al checkout che voleva fare).
                session.removeAttribute("redirectAfterLogin");
                response.sendRedirect(redirectAfterLogin);
            }
            else{
                // Credenziali errate O utente inesistente: stesso messaggio generico
                // in entrambi i casi.
                request.setAttribute("loginError", "Email o password errati");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    /*
     * Mostra la pagina di login. Se l'utente è loggato, non ha senso rivedere
     * il form: redirect all'account.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("customer") != null){
            response.sendRedirect(request.getContextPath() + "/account");
            return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Login.jsp");
        dispatcher.forward(request, response);
    }
}