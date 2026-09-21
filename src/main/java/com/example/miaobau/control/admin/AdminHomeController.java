package com.example.miaobau.control.admin;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/*
 * Controller della home/dashboard dell'area admin: la pagina di atterraggio dopo
 * il login admin, da cui si accede alle sezioni (prodotti, ordini, clienti).
 */
@WebServlet("/admin/home")
public class AdminHomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/admin/Home.jsp");
        dispatcher.forward(request, response);
    }
}