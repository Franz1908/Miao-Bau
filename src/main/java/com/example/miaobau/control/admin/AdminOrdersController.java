package com.example.miaobau.control.admin;

import com.example.miaobau.dao.CustomerDAO;
import com.example.miaobau.dao.OrdersDAO;
import com.example.miaobau.model.CustomerBean;
import com.example.miaobau.model.OrdersBean;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

/*
 * Controller dell'elenco ordini lato ADMIN, con FILTRI opzionali per email
 * cliente e intervallo di date. Sotto /admin/*, protetto dal filtro.
 * Senza filtri mostra tutti gli ordini; con filtri, solo quelli corrispondenti.
 */
@WebServlet("/admin/orders")
public class AdminOrdersController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String dateFromStr = request.getParameter("dateFrom");
        String dateToStr = request.getParameter("dateTo");

        // Normalizza l'email vuota a null, così il DAO la tratta come "filtro assente"
        // (non aggiunge la clausola) invece di cercare gli ordini con email "".
        if (email == null || email.isBlank()) {
            email = null;
        }

        // Conversione sicura delle date. I due metodi sono diversi apposta:
        // parseDateFromOrNull -> inizio giornata (00:00), per il "dal giorno X";
        // parseDateToOrNull   -> fine giornata (23:59:59), per il "fino al giorno X",
        LocalDateTime dateFrom = ParseUtil.parseDateFromOrNull(dateFromStr);
        LocalDateTime dateTo = ParseUtil.parseDateToOrNull(dateToStr);

        // Validazione logica dell'intervallo: se ci sono entrambe le date e la
        // iniziale è successiva alla finale, l'intervallo è scorretto -> mostra errore
        // e non esegue la ricerca.
        if (dateFrom != null && dateTo != null && dateFrom.isAfter(dateTo)) {
            request.setAttribute("filterError", "L'intervallo di date non è valido: la data iniziale è successiva a quella finale.");
            request.getRequestDispatcher("/view/admin/Orders.jsp").forward(request, response);
            return;
        }

        try {
            // Il DAO costruisce la query dinamicamente in base ai filtri presenti
            // (email/date). I parametri null vengono ignorati (nessuna clausola).
            List<OrdersBean> orders = new OrdersDAO().doRetrieveFiltered(email, dateFrom, dateTo);
            request.setAttribute("orders", orders);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        request.getRequestDispatcher("/view/admin/Orders.jsp").forward(request, response);
    }
}