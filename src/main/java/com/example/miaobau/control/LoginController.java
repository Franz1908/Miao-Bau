package com.example.miaobau.control;

import com.example.miaobau.dao.CustomerDAO;
import com.example.miaobau.model.CustomerBean;
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

@WebServlet("/login")
public class LoginController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email").trim();
        String password = request.getParameter("password");
        CustomerDAO customerDAO = new CustomerDAO();

        try {
            CustomerBean customerBean = customerDAO.doRetriveByEmail(email);
            if(customerBean != null && PasswordUtil.verifyPassword(password, customerBean.getPasswordHash())){
                HttpSession session = request.getSession();
                customerBean.setPasswordHash(null);
                String redirectAfterLogin = (String) session.getAttribute("redirectAfterLogin");
                session.setAttribute("customer", customerBean);
                if (redirectAfterLogin == null) {
                    response.sendRedirect(request.getContextPath() + "/account");
                    return;
                }
                session.removeAttribute("redirectAfterLogin");
                response.sendRedirect(redirectAfterLogin);
            }
            else{
                request.setAttribute("loginError", "Email o password errati");
                RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Login.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("customer") != null){
            response.sendRedirect(request.getContextPath() + "/account");
            return;
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Login.jsp");
        dispatcher.forward(request, response);
    }
}
