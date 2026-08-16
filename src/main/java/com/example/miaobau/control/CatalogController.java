package com.example.miaobau.control;

import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.model.CategoryBean;
import com.example.miaobau.model.ProductBean;
import com.example.miaobau.model.SpeciesBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/catalog")
public class CatalogController extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ProductDAO productDAO = new ProductDAO();
        String speciesParam = request.getParameter("speciesId");
        String categoryParam = request.getParameter("categoryId");

        try {
            List<ProductBean> products;
            String title;

            if (speciesParam == null) {
                // Nessun filtro: tutto il catalogo
                products = productDAO.doRetrieveAll();
                title = "Catalogo prodotti";

            } else if (categoryParam == null) {
                // Solo specie
                int speciesId = Integer.parseInt(speciesParam);
                products = productDAO.doRetrieveBySpecies(speciesId);
                title = "Prodotti per " + findSpeciesName(speciesId);

            } else {
                // Specie + categoria
                int speciesId = Integer.parseInt(speciesParam);
                int categoryId = Integer.parseInt(categoryParam);
                products = productDAO.doRetrieveBySpeciesAndCategory(speciesId, categoryId);
                title = "Prodotti per " + findSpeciesName(speciesId)
                        + ": " + findCategoryName(categoryId);
            }

            request.setAttribute("products", products);
            request.setAttribute("title", title);

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Catalog.jsp");
        dispatcher.forward(request, response);
    }

    // Cerca il nome della specie nell'application scope (caricato dal listener)
    private String findSpeciesName(int speciesId) {
        List<SpeciesBean> species = (List<SpeciesBean>) getServletContext().getAttribute("species");
        for (SpeciesBean s : species) {
            if (s.getSpeciesID() == speciesId) {
                return s.getSpeciesName();
            }
        }
        return "";
    }

    // Cerca il nome della categoria nell'application scope
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