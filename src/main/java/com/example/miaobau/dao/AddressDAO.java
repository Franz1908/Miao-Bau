package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.AddressBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO {

    public List<AddressBean> doRetrieveByCustomer(int customerID) throws SQLException {
        List<AddressBean> addresses = new ArrayList<>();
        String query = "SELECT * FROM address WHERE customer_id = ? AND is_deleted = FALSE";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, customerID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    addresses.add(mapAddress(rs));
                }
            }
        }

        return addresses;
    }

    public int doSave(AddressBean address) throws SQLException {
        String query = "INSERT INTO address (customer_id, street, civic_number, postal_code, city, country) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, address.getCustomerID());
            ps.setString(2, address.getStreet());
            ps.setString(3, address.getCivicNumber());
            ps.setString(4, address.getPostalCode());
            ps.setString(5, address.getCity());
            ps.setString(6, address.getCountry());
            return ps.executeUpdate();
        }
    }

    public void doDelete(int addressID) throws SQLException {
        String query = "UPDATE address SET is_deleted = TRUE WHERE address_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, addressID);
            ps.executeUpdate();
        }
    }

    public AddressBean doRetriveByID(int addressID) throws SQLException {
        AddressBean address = null;
        String query = "SELECT * FROM address WHERE address_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, addressID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    address = mapAddress(rs);

                }
            }
        }

        return  address;
    }

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
