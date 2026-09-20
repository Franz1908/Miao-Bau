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

@WebServlet("/search")
public class SearchProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JSONArray jsonArray = new JSONArray();
        PrintWriter out = response.getWriter();
        String q = request.getParameter("q");

        if (q == null || q.isBlank()) {
            out.println(jsonArray);
            return;
        }

        try {
            List<ProductBean> products = new ProductDAO().doRetrieveByName(q);
            for (ProductBean product : products) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", product.getProductID());
                jsonObject.put("name", product.getName());
                jsonArray.put(jsonObject);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        out.println(jsonArray);
    }
}
