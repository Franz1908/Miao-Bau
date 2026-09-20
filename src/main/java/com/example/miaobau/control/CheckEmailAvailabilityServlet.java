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

@WebServlet("/emailCheck")
public class CheckEmailAvailabilityServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String email = request.getParameter("email");
        JSONObject jsObject = new JSONObject();

        try {
            boolean available = new CustomerDAO().doRetriveByEmail(email) == null;
            jsObject.put("available", available);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        out.println(jsObject);
    }
}
