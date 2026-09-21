package com.example.miaobau.control;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.model.ProductBean;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

// Servlet AJAX per l'autocomplete della ricerca: riceve il testo digitato
// e risponde con un JSON dei prodotti che corrispondono.
@WebServlet("/search")
public class SearchProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Content-Type JSON
        response.setContentType("application/json");
        JSONArray jsonArray = new JSONArray();
        PrintWriter out = response.getWriter();
        String q = request.getParameter("q");   // testo digitato dall'utente

        // niente testo allora rispondo con array vuoto senza interrogare il DB
        if (q == null || q.isBlank()) {
            out.println(jsonArray);
            return;
        }

        try {
            // cerco i prodotti col nome che contiene q (metodo con LIKE nel DAO)
            List<ProductBean> products = new ProductDAO().doRetrieveByName(q);
            // costruisco un array di oggetti { id, name }: solo i dati che servono
            // alla tendina, per non trasferire l'intero prodotto
            for (ProductBean product : products) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", product.getProductID());
                jsonObject.put("name", product.getName());
                jsonArray.put(jsonObject);   // aggiungo l'oggetto all'array
            }
        } catch (SQLException e) {
            // errore DB rilanciato come ServletException (come nelle altre servlet)
            throw new ServletException(e);
        }

        // serializzo l'array JSON nel body della risposta
        out.println(jsonArray);
    }
}