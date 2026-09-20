package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.CustomerBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * DAO per la tabella customer. Gestisce registrazione (insert), login/lookup
 * per email, elenco utenti (admin), e aggiornamento dei dati e della password.
 */
public class CustomerDAO {

    // Inserisce un nuovo cliente (registrazione). Il password_hash è già calcolato
    // a monte (PasswordUtil): il DAO salva l'hash, mai la password in chiaro.
    public int doSave(CustomerBean customerBean) throws SQLException {
        String query = "INSERT INTO customer (first_name, last_name, email, phone, birth_date, password_hash) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)){

            setCustomerParameters(ps, customerBean);         // parametri 1-5 (dati anagrafici)
            ps.setString(6, customerBean.getPasswordHash()); // parametro 6: qui è la password
            return ps.executeUpdate();
        }
    }

    /*
     * Recupera un cliente dalla sua email (usata come identificatore di login).
     * Restituisce il bean COMPLETO di password_hash, perché serve al login per
     * verificare la password. Null se l'email non esiste.
     */
    public CustomerBean doRetriveByEmail(String email) throws SQLException {
        CustomerBean customerBean = null;
        String query = "SELECT * FROM customer WHERE email = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){   // email unica: al massimo una riga
                    customerBean = new CustomerBean();
                    customerBean.setCustomerID(rs.getInt("customer_id"));
                    customerBean.setFirstName(rs.getString("first_name"));
                    customerBean.setLastName(rs.getString("last_name"));
                    customerBean.setEmail(rs.getString("email"));
                    customerBean.setPasswordHash(rs.getString("password_hash"));
                    customerBean.setPhone(rs.getString("phone"));
                    // La data può essere NULL nel DB (campo opzionale): converto in
                    // LocalDate solo se presente, per evitare NullPointerException.
                    java.sql.Date birthDate = rs.getDate("birth_date");
                    if (birthDate != null) {
                        customerBean.setBirthDate(birthDate.toLocalDate());
                    }
                }
            }
        }

        return customerBean;
    }

    /*
     * Elenca tutti i clienti (per la vista admin).
     */
    public List<CustomerBean> doRetrieveAll() throws SQLException {
        List<CustomerBean> customers = new ArrayList<>();
        String query = "SELECT customer_id, first_name, last_name, email, birth_date, phone FROM customer";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                CustomerBean customer = new CustomerBean();
                customer.setCustomerID(rs.getInt("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                java.sql.Date birthDate = rs.getDate("birth_date");
                if (birthDate != null) {
                    customer.setBirthDate(birthDate.toLocalDate());
                }
                customers.add(customer);
            }
        }

        return customers;
    }

    /*
     * Aggiorna i dati ANAGRAFICI del cliente (non la password).
     * Il parametro 6 qui è il customer_id del WHERE (quale riga aggiornare).
     */
    public void doUpdate(CustomerBean customerBean) throws SQLException {
        String query =
                "UPDATE customer SET " +
                        "first_name = ?, " +
                        "last_name = ?, " +
                        "email = ?, " +
                        "phone = ?, " +
                        "birth_date = ? " +
                        "WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            setCustomerParameters(ps, customerBean);          // parametri 1-5 (dati anagrafici)
            ps.setInt(6, customerBean.getCustomerID());       // parametro 6: qui è l'id del WHERE
            ps.executeUpdate();
        }
    }

    /*
     * Aggiorna SOLO la password (hash). Metodo separato da doUpdate: si chiama
     * unicamente quando l'utente cambia davvero la password, così i due tipi di
     * aggiornamento restano indipendenti e la password non viene mai toccata
     * da un aggiornamento dei soli dati anagrafici.
     */
    public void doUpdatePassword(int customerID, String password) throws SQLException {
        String query = "UPDATE customer SET password_hash = ? WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, password);   // l'hash già calcolato
            ps.setInt(2, customerID);
            ps.executeUpdate();
        }

    }

    /*
     * Helper condiviso: imposta i parametri COMUNI a insert e update,
     * cioè i dati anagrafici (posizioni 1-5). NON imposta il parametro 6, che
     * ha significato diverso nei due casi (password nell'insert, id nel WHERE
     * dell'update)
     */
    private void setCustomerParameters(PreparedStatement ps, CustomerBean customerBean) throws SQLException {
        ps.setString(1, customerBean.getFirstName());
        ps.setString(2, customerBean.getLastName());
        ps.setString(3, customerBean.getEmail());
        ps.setString(4, customerBean.getPhone());
        if (customerBean.getBirthDate() != null) {
            ps.setDate(5, java.sql.Date.valueOf(customerBean.getBirthDate()));
        } else {
            ps.setNull(5, java.sql.Types.DATE);   // NULL tipizzato per colonna DATE
        }
    }

}