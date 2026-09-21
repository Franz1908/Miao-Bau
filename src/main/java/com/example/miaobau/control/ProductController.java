package com.example.miaobau.control;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.model.ProductBean;
import com.example.miaobau.utils.ParseUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

/*
 * Controller per la pagina di dettaglio di un singolo prodotto.
 * Riceve l'id del prodotto dall'URL (?productId=...), lo recupera dal DB e lo
 * passa alla JSP. Usa doRetrieveById (versione cliente) che restituisce solo prodotti
 * attivi, quindi un prodotto cancellato non è visibile nemmeno manipolando l'id.
 */
@WebServlet("/product")
public class ProductController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO productDAO = new ProductDAO();

        try {
            // Conversione sicura del parametro: se manca o non è un numero, torna null
            Integer productID = ParseUtil.parseIntOrNull(request.getParameter("productId"));

            ProductBean product = null;
            // Recupera dal DB solo se l'id è valido; altrimenti product resta null.
            if (productID != null) {
                product = productDAO.doRetrieveById(productID);
            }
            // Passa il prodotto alla JSP. Può essere null (id mancante/invalido, oppure
            // prodotto inesistente/cancellato): la JSP gestisce questo caso mostrando
            // il messaggio "prodotto non trovato" invece di andare in errore.
            request.setAttribute("product", product);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        // Sempre forward alla stessa JSP.
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Product.jsp");
        dispatcher.forward(request, response);
    }
}