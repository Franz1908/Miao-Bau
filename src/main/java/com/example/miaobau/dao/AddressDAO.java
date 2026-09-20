package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.AddressBean;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
 * DAO per la tabella address (indirizzi di spedizione dei clienti).
 * Gestisce lettura, inserimento e cancellazione (soft delete) degli indirizzi.
 * La cancellazione è "soft" (is_deleted = TRUE) invece che fisica, così gli ordini
 * passati che riferiscono un indirizzo restano validi anche dopo la cancellazione.
 */
public class AddressDAO {

    /*
     * Recupera gli indirizzi ATTIVI di un cliente (per l'account e la scelta al
     * checkout). Il filtro is_deleted = FALSE esclude quelli cancellati: nella
     * scelta non devono comparire indirizzi eliminati.
     */
    public List<AddressBean> doRetrieveByCustomer(int customerID) throws SQLException {
        List<AddressBean> addresses = new ArrayList<>();
        String query = "SELECT * FROM address WHERE customer_id = ? AND is_deleted = FALSE";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, customerID);   // riempie il ? con l'id (PreparedStatement = no SQL injection)

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    addresses.add(mapAddress(rs));   // usa l'helper condiviso di mapping
                }
            }
        }

        return addresses;
    }

    /*
     * Inserisce un nuovo indirizzo e RESTITUISCE l'id generato dal DB.
     * L'id serve al checkout quando l'utente aggiunge un indirizzo "al volo":
     * appena salvato, va collegato all'ordine tramite il suo id.
     */
    public int doSave(AddressBean address) throws SQLException {
        String query = "INSERT INTO address (customer_id, street, civic_number, postal_code, city, country) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        // RETURN_GENERATED_KEYS: chiede al driver di conservare la chiave auto-generata,
        // per recuperarla dopo l'insert  (executeUpdate da solo darebbe solo
        // il numero di righe, non l'id).
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, address.getCustomerID());
            ps.setString(2, address.getStreet());
            ps.setString(3, address.getCivicNumber());
            ps.setString(4, address.getPostalCode());
            ps.setString(5, address.getCity());
            ps.setString(6, address.getCountry());
            ps.executeUpdate();   // esegue l'inserimento

            // Recupera l'id auto-generato appena creato.
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);   // prima colonna = l'id nuovo
                } else {
                    // Non dovrebbe accadere: se non torna l'id, meglio fallire
                    throw new SQLException("Creazione indirizzo fallita, nessun id ottenuto");
                }
            }
        }
    }

    /*
     * Cancellazione SOFT: non elimina la riga, imposta is_deleted = TRUE.
     * Così gli ordini passati che riferiscono questo indirizzo restano integri;
     * l'indirizzo semplicemente non comparirà più tra quelli attivi del cliente.
     */
    public void doDelete(int addressID) throws SQLException {
        String query = "UPDATE address SET is_deleted = TRUE WHERE address_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, addressID);
            ps.executeUpdate();
        }
    }

    /*
     * Recupera un singolo indirizzo per id. NON filtra is_deleted: serve anche
     * per lo storico ordini, dove un ordine può riferire un indirizzo poi
     * cancellato che va comunque mostrato (dato storico). Usato anche per il
     * controllo di proprietà (verificare che l'indirizzo sia del cliente).
     */
    public AddressBean doRetriveByID(int addressID) throws SQLException {
        AddressBean address = null;   // null se non esiste
        String query = "SELECT * FROM address WHERE address_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, addressID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {          // if (non while): al massimo una riga, l'id è chiave primaria
                    address = mapAddress(rs);
                }
            }
        }

        return  address;
    }

    /*
     * Helper privato: mappa una riga del ResultSet in un AddressBean.
     */
    private AddressBean mapAddress(ResultSet rs) throws SQLException {
        AddressBean address = new AddressBean();
        address.setAddressID(rs.getInt("address_id"));
        address.setCustomerID(rs.getInt("customer_id"));
        address.setCity(rs.getString("city"));
        address.setStreet(rs.getString("street"));
        address.setCivicNumber(rs.getString("civic_number"));
        address.setCountry(rs.getString("country"));
        address.setPostalCode(rs.getString("postal_code"));
        address.setDeleted(rs.getBoolean("is_deleted"));
        return address;
    }

}