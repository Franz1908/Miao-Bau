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
import java.util.ArrayList;
import java.util.List;

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
        List<String> errors = new ArrayList<>();
        String emailRegex = "^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$";

        // Controllo dei campi obbligatori
        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add("Inserire un nome");
        }

        if (lastName == null || lastName.trim().isEmpty()) {
            errors.add("Inserire un cognome");
        }

        if (email == null || email.trim().isEmpty()) {
            errors.add("Inserire un'email");
        }

        if (password == null || password.trim().isEmpty()) {
            errors.add("Inserire una password");
        }

        /*
         * Se manca almeno un campo obbligatorio,
         * non ha senso continuare con gli altri controlli.
         */
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/view/Register.jsp").forward(request, response);
            return;
        }

        // Controllo formato email
        if (!email.matches(emailRegex)) {
            errors.add("Email non valida");
        }

        // Controllo lunghezza password
        if (password.length() < 8 || password.length() > 16) {
            errors.add("La password deve avere minimo 8 caratteri e massimo 16 caratteri");
        }

        // Controllo presenza di almeno un numero
        if (!password.matches(".*\\d.*")) {
            errors.add("La password deve contenere almeno un numero");
        }

        // Controllo presenza di almeno un carattere speciale
        if (!password.matches(".*[^a-zA-Z0-9].*")) {
            errors.add("La password deve contenere almeno un carattere speciale");
        }

        /*
         * Se ci sono errori nei controlli di formato,
         * non procedo con il controllo sul database.
         */
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/view/Register.jsp").forward(request, response);
            return;
        }

        // Controllo se l'email è già registrata
        try {
            if (customerDAO.doRetriveByEmail(email) != null) {
                errors.add("E-mail già registrata");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Se l'email è già registrata, fermo la registrazione
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/view/Register.jsp").forward(request, response);
            return;
        }

        // Creazione del CustomerBean
        customerBean.setFirstName(firstName);
        customerBean.setLastName(lastName);
        customerBean.setEmail(email);
        customerBean.setPasswordHash(PasswordUtil.hashPassword(password));

        // Controllo data di nascita opzionale
        if (birthDateStr != null && !birthDateStr.isEmpty()) {
            birthDate = LocalDate.parse(birthDateStr);
            customerBean.setBirthDate(birthDate);
        }

        // Controllo telefono opzionale
        if (telephone != null && !telephone.isEmpty()) {
            customerBean.setPhone(telephone);
        }

        // Salvataggio dell'utente
        try {
            customerDAO.doSave(customerBean);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        // Registrazione completata
        response.sendRedirect(request.getContextPath() + "/login");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession().getAttribute("customer") != null) {
            response.sendRedirect(request.getContextPath() + "/account");
        }
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/Register.jsp");
        dispatcher.forward(request, response);
    }
}
