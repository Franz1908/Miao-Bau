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
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;

@WebServlet("/register")
public class RegisterController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerDAO customerDAO = new CustomerDAO();
        CustomerBean customerBean = new CustomerBean();
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String telephone = request.getParameter("telephone");
        String birthDateStr = request.getParameter("birthDate");
        LocalDate birthDate = null;

        customerBean.setFirstName(firstName);
        customerBean.setLastName(lastName);
        customerBean.setEmail(email);
        customerBean.setPasswordHash(PasswordUtil.hashPassword(password));

        if(birthDateStr != null && !birthDateStr.isEmpty()){
            birthDate = LocalDate.parse(birthDateStr);
            customerBean.setBirthDate(birthDate);
        }

        if(telephone != null && !telephone.isEmpty()){
            customerBean.setPhone(telephone);
        }

        try {
            customerDAO.doSave(customerBean);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        response.sendRedirect(request.getContextPath() + "/login");

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getSession().getAttribute("customer") != null){
            response.sendRedirect(request.getContextPath() + "/account");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Register.jsp");
        dispatcher.forward(request, response);
    }
}
