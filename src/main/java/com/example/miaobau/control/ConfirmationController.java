package com.example.miaobau.control;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Controller della pagina di conferma ordine (mostrata dopo un acquisto riuscito).
 * Riceve l'id dell'ordine appena creato (passato dal CheckoutController nel
 * redirect) e lo gira alla JSP, che mostra il messaggio "ordine effettuato" col
 * numero..
 */
@WebServlet("/secure/confirmation")
public class ConfirmationController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Legge l'orderId dall'URL (arriva come parametro dal redirect del checkout)
        // e lo passa alla JSP come attributo, così la conferma può mostrare il numero.
        String orderID = request.getParameter("orderId");
        request.setAttribute("orderId", orderID);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Confirmation.jsp");
        dispatcher.forward(request, response);
    }
}