package com.example.miaobau.control.admin;

import com.example.miaobau.dao.AdminDAO;
import com.example.miaobau.model.AdminBean;
import com.example.miaobau.utils.PasswordUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/*
 * Controller del login amministratore. Analogo al login cliente, con due
 * differenze: l'admin si identifica per USERNAME (non email), e a login riuscito
 * si va alla dashboard admin. Il mapping /admin/login è l'eccezione consentita
 * dall'AdminFilter (deve essere raggiungibile senza essere già loggati).
 * Sicurezza: verifica via hash, hash rimosso dalla sessione, messaggio generico.
 */
@WebServlet("/admin/login")
public class AdminLoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        AdminDAO adminDAO = new AdminDAO();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("AdminErrorMessage", "Credenziali errate");
            request.getRequestDispatcher("/view/admin/Login.jsp").forward(request, response);
            return;
        }
        username = username.trim();

        try {
            // Recupera l'admin per username (con l'hash, per la verifica).
            AdminBean adminBean = adminDAO.doRetriveByUsername(username);
            // Login riuscito solo se l'admin esiste e la password (ri-hashata) coincide.
            if(adminBean != null && PasswordUtil.verifyPassword(password, adminBean.getPasswordHash())){
                HttpSession session = request.getSession();
                adminBean.setPasswordHash(null);              // hash fuori dalla sessione
                session.setAttribute("admin", adminBean);     // da qui l'admin è "loggato"
                response.sendRedirect(request.getContextPath() + "/admin/home");
            }
            else{
                // Credenziali errate o username inesistente.
                request.setAttribute("AdminErrorMessage", "Credenziali errate");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/view/admin/Login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    /*
     * Mostra il form di login admin. Se già loggato come admin, salta alla dashboard
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("admin") != null){
            response.sendRedirect(request.getContextPath() + "/admin/home");
            return;
        }
        request.getRequestDispatcher("/view/admin/Login.jsp").forward(request, response);
    }
}