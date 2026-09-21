package com.example.miaobau.control;

import com.example.miaobau.dao.AddressDAO;
import com.example.miaobau.model.AddressBean;
import com.example.miaobau.model.CustomerBean;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Controller per l'aggiunta di un nuovo indirizzo dall'area account.
 * Mappato sotto /secure/*, quindi il cliente è garantito loggato (CustomerFilter).
 */
@WebServlet("/secure/address/new")
public class NewAddressController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lettura dei 5 campi dell'indirizzo dal form.
        String street = request.getParameter("street");
        String civicNumber = request.getParameter("civicNumber");
        String postalCode = request.getParameter("postalCode");
        String city = request.getParameter("city");
        String country = request.getParameter("country");
        // Il cliente in sessione serve per legare l'indirizzo al proprietario.
        CustomerBean customerBean = (CustomerBean) request.getSession().getAttribute("customer");
        List<String> errors = new ArrayList<>();

        // Validazione lato server dei campi obbligatori (accumulo errori in lista).
        if (street == null || street.isBlank()) {
            errors.add("Inserire una via valida");
        }

        if (civicNumber == null || civicNumber.isBlank()) {
            errors.add("Inserire un numero civico valido");
        }

        // Il CAP deve essere esattamente 5 cifre (regex \d{5}), formato italiano.
        if (postalCode == null || postalCode.isBlank() || !postalCode.matches("\\d{5}")) {
            errors.add("Inserire un CAP valido");
        }

        if (city == null || city.isBlank()) {
            errors.add("Inserire una città valida");
        }

        if (country == null || country.isBlank()) {
            errors.add("Inserire una paese valido");
        }

        // Se qualcosa non va: torna al form con gli errori.
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            request.getRequestDispatcher("/view/NewAddress.jsp").forward(request, response);
            return;
        }

        // Costruzione del bean con i dati validati.
        AddressBean addressBean = new AddressBean();
        addressBean.setCity(city);
        addressBean.setPostalCode(postalCode);
        addressBean.setStreet(street);
        addressBean.setCivicNumber(civicNumber);
        addressBean.setCountry(country);
        // Il customerID viene preso dalla sessione
        addressBean.setCustomerID(customerBean.getCustomerID());

        try {
            new AddressDAO().doSave(addressBean);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        // POST-Redirect-GET: dopo il salvataggio redirect all'account
        response.sendRedirect(request.getContextPath() + "/account");
    }

    // Mostra il form di inserimento (pagina separata raggiunta dal link "Aggiungi
    // indirizzo" nell'account).
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/view/NewAddress.jsp");
        dispatcher.forward(request, response);
    }

}