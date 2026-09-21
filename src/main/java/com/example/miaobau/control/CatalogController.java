package com.example.miaobau.control;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.model.CategoryBean;
import com.example.miaobau.model.ProductBean;
import com.example.miaobau.model.SpeciesBean;
import com.example.miaobau.utils.ParseUtil;

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
 * Controller del catalogo. È il punto d'ingresso per vedere i prodotti in tutte
 * le modalità: ricerca per nome, filtri (sconti/popolari), navigazione per specie
 * e categoria, o l'intero catalogo. In base ai parametri ricevuti sceglie quale
 * query eseguire e con quale titolo mostrare la pagina.
 */
@WebServlet("/catalog")
public class CatalogController extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // Legge tutti i possibili parametri (conversione sicura per gli id numerici).
        // Non tutti saranno presenti: la combinazione presente decide il comportamento.
        Integer speciesID = ParseUtil.parseIntOrNull(request.getParameter("speciesId"));
        Integer categoryID = ParseUtil.parseIntOrNull(request.getParameter("categoryId"));
        String filter = request.getParameter("filter");   // "sale" o "popular"
        String q = request.getParameter("q");             // termine di ricerca
        ProductDAO productDAO = new ProductDAO();
        List<ProductBean> products;
        String title;

        // Catena di casi in ordine di PRIORITÀ (mutuamente esclusivi): il primo che
        // combacia decide. Un unico try/catch per tutte le query (gestione errore comune).
        try {
            // 1) Ricerca per nome: se c'è un termine q, ha la precedenza.
            if (q != null && !q.isBlank()) {
                products = productDAO.doRetrieveByName(q);
                title = "Risultati ricerca per: " + q;
            }
            // 2) Filtro sconti.
            else if (filter != null && filter.equals("sale")) {
                products = productDAO.doRetriveDiscountedProducts();
                title = "Prodotti in sconto";

                // 3) Filtro popolari.
            } else if (filter != null && filter.equals("popular")) {
                products = productDAO.doRetrivePopularProducts();
                title = "Prodotti popolari";

                // 4) Nessuna specie selezionata: tutto il catalogo.
            } else if (speciesID == null) {
                products = productDAO.doRetrieveAll();
                title = "Catalogo prodotti";

                // 5) Solo specie (nessuna categoria): prodotti di quella specie.
            } else if (categoryID == null) {
                products = productDAO.doRetrieveBySpecies(speciesID);
                title = "Prodotti per " + findSpeciesName(speciesID);

                // 6) Specie + categoria: navigazione combinata.
            } else {
                products = productDAO.doRetrieveBySpeciesAndCategory(speciesID, categoryID);
                title = "Prodotti per " + findSpeciesName(speciesID)
                        + ": " + findCategoryName(categoryID);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        // La lista può essere vuota (ricerca senza risultati, categoria senza prodotti):
        // la JSP mostrerà lo stato vuoto.
        request.setAttribute("products", products);
        request.setAttribute("title", title);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Catalog.jsp");
        dispatcher.forward(request, response);
    }

    /*
     * Trova il NOME di una specie dato il suo id, per comporre il titolo della pagina.
     * Legge la lista specie dal ServletContext (precaricata all'avvio dal listener):
     * così non serve una query al DB solo per ottenere un nome. Ritorna "" se non
     * trovata.
     */
    private String findSpeciesName(int speciesId) {
        List<SpeciesBean> species = (List<SpeciesBean>) getServletContext().getAttribute("species");
        for (SpeciesBean s : species) {
            if (s.getSpeciesID() == speciesId) {
                return s.getSpeciesName();
            }
        }
        return "";
    }

    /*
     * Come findSpeciesName ma per le categorie. Anche qui i dati vengono dal
     * ServletContext, non dal DB.
     */
    private String findCategoryName(int categoryId) {
        List<CategoryBean> categories = (List<CategoryBean>) getServletContext().getAttribute("categories");
        for (CategoryBean c : categories) {
            if (c.getCategoryID() == categoryId) {
                return c.getCategoryName();
            }
        }
        return "";
    }
}