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

@WebServlet("/catalog")
public class CatalogController extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ProductDAO productDAO = new ProductDAO();
        Integer speciesID = ParseUtil.parseIntOrNull(request.getParameter("speciesId"));
        Integer categoryID = ParseUtil.parseIntOrNull(request.getParameter("categoryId"));

        try {
            List<ProductBean> products;
            String title;

            if (speciesID == null) {
                // Nessun filtro valido di specie: tutto il catalogo
                products = productDAO.doRetrieveAll();
                title = "Catalogo prodotti";
            } else if (categoryID == null) {
                // Solo specie
                products = productDAO.doRetrieveBySpecies(speciesID);
                title = "Prodotti per " + findSpeciesName(speciesID);
            } else {
                // Specie + categoria
                products = productDAO.doRetrieveBySpeciesAndCategory(speciesID, categoryID);
                title = "Prodotti per " + findSpeciesName(speciesID)
                        + ": " + findCategoryName(categoryID);
            }

            request.setAttribute("products", products);
            request.setAttribute("title", title);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Catalog.jsp");
        dispatcher.forward(request, response);
    }

    private String findSpeciesName(int speciesId) {
        List<SpeciesBean> species = (List<SpeciesBean>) getServletContext().getAttribute("species");
        for (SpeciesBean s : species) {
            if (s.getSpeciesID() == speciesId) {
                return s.getSpeciesName();
            }
        }
        return "";
    }

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