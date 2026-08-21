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

@WebServlet("/admin/login")
public class AdminLoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username").trim();
        String password = request.getParameter("password");
        AdminDAO adminDAO = new AdminDAO();

        try {
            AdminBean adminBean = adminDAO.doRetriveByUsername(username);
            if(adminBean != null && PasswordUtil.verifyPassword(password, adminBean.getPasswordHash())){
                HttpSession session = request.getSession();
                adminBean.setPasswordHash(null);
                session.setAttribute("admin", adminBean);
                response.sendRedirect(request.getContextPath() + "/admin/home");
            }
            else{
                request.setAttribute("AdminErrorMessage", "Credenziali errate");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/view/admin/Login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("admin") != null){
            response.sendRedirect(request.getContextPath() + "/admin/home");
            return;
        }
        request.getRequestDispatcher("/view/admin/Login.jsp").forward(request, response);
    }
}
