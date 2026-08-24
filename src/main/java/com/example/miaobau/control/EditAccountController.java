package com.example.miaobau.control;

import com.example.miaobau.dao.CustomerDAO;
import com.example.miaobau.dao.ProductDAO;
import com.example.miaobau.model.CustomerBean;
import com.example.miaobau.model.ProductBean;
import com.example.miaobau.utils.ParseUtil;
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
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/account/edit")
public class EditAccountController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerBean customerBean = (CustomerBean) request.getSession().getAttribute("customer");

        if (customerBean == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String birthDateStr = request.getParameter("birthDate");
        String phone = request.getParameter("phone");
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        List<String> errors = new ArrayList<>();
        String emailRegex = "^[\\w.+-]+@[\\w-]+\\.[\\w.-]+$";
        CustomerDAO customerDAO = new CustomerDAO();

        try {
            // --- Validazione dati anagrafici ---
            if (firstName == null || firstName.isBlank()) {
                errors.add("Inserire un nome valido");
            }
            if (lastName == null || lastName.isBlank()) {
                errors.add("Inserire un cognome valido");
            }
            if (email == null || email.isBlank()) {
                errors.add("Inserire una email");
            } else if (!email.matches(emailRegex)) {
                errors.add("Inserire una email valida");
            } else if (!email.equals(customerBean.getEmail())) {
                // email cambiata: verifica che non sia già di un altro
                CustomerBean existing = customerDAO.doRetriveByEmail(email);
                if (existing != null) {
                    errors.add("Email già registrata");
                }
            }

            // conversione data di nascita (opzionale)
            LocalDate birthDate = null;
            if (birthDateStr != null && !birthDateStr.isBlank()) {
                try {
                    birthDate = LocalDate.parse(birthDateStr.trim());
                } catch (DateTimeParseException e) {
                    errors.add("Data di nascita non valida");
                }
            }

            // --- Validazione password (solo se si vuole cambiarla) ---
            boolean changePassword = newPassword != null && !newPassword.isBlank();
            String newHash = null;

            if (changePassword) {
                if (currentPassword == null || currentPassword.isBlank()) {
                    errors.add("Inserire la password corrente");
                } else {
                    // recupera l'hash con l'email ATTUALE (in sessione), non la nuova
                    CustomerBean current = customerDAO.doRetriveByEmail(customerBean.getEmail());
                    if (current == null || !PasswordUtil.verifyPassword(currentPassword, current.getPasswordHash())) {
                        errors.add("La password corrente è errata");
                    }
                }
                if (newPassword.length() < 8 || newPassword.length() > 16) {
                    errors.add("La password deve avere tra 8 e 16 caratteri");
                }
                if (!newPassword.matches(".*\\d.*") || !newPassword.matches(".*[^a-zA-Z0-9].*")) {
                    errors.add("La password deve contenere almeno un numero e un carattere speciale");
                }
                if (!newPassword.equals(confirmPassword)) {
                    errors.add("Le password non coincidono");
                }
            }

            // --- Se ci sono errori, torna al form senza salvare nulla ---
            if (!errors.isEmpty()) {
                request.setAttribute("errors", errors);
                request.getRequestDispatcher("/view/EditAccount.jsp").forward(request, response);
                return;
            }

            // --- Nessun errore: aggiorno ---
            customerBean.setFirstName(firstName);
            customerBean.setLastName(lastName);
            customerBean.setEmail(email);
            customerBean.setPhone(phone == null || phone.isBlank() ? null : phone.trim());
            customerBean.setBirthDate(birthDate);

            customerDAO.doUpdate(customerBean);

            if (changePassword) {
                newHash = PasswordUtil.hashPassword(newPassword);
                customerDAO.doUpdatePassword(customerBean.getCustomerID(), newHash);
            }

        } catch (SQLException e) {
            throw new ServletException(e);
        }

        // aggiorno il customer in sessione con i nuovi dati (azzerando l'hash)
        customerBean.setPasswordHash(null);
        request.getSession().setAttribute("customer", customerBean);

        response.sendRedirect(request.getContextPath() + "/account");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CustomerBean customerBean = (CustomerBean) request.getSession().getAttribute("customer");

        if (customerBean == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/EditAccount.jsp");
        dispatcher.forward(request, response);
    }

}
