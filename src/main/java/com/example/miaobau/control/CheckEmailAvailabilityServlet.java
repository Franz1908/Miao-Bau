package com.example.miaobau.control;

import com.example.miaobau.dao.CustomerDAO;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

// Servlet AJAX per il controllo email in registrazione: dato un indirizzo,
// risponde in JSON se è disponibile (cioè non ancora registrato).
@WebServlet("/emailCheck")
public class CheckEmailAvailabilityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Content-Type JSON:
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String email = request.getParameter("email");   // email digitata dall'utente
        JSONObject jsObject = new JSONObject();

        try {
            // disponibile = nessun cliente trovato con quell'email (== null)
            boolean available = new CustomerDAO().doRetriveByEmail(email) == null;
            jsObject.put("available", available);
        } catch (SQLException e) {
            // errore DB rilanciato come ServletException (come nelle altre servlet)
            throw new ServletException(e);
        }

        // serializzo l'oggetto JSON { available: true/false } nel body
        out.println(jsObject);
    }
}