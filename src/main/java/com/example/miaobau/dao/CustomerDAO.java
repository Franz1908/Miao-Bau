package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.CustomerBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    public int doSave(CustomerBean customerBean) throws SQLException {
        String query = "INSERT INTO customer (first_name, last_name, email, phone, birth_date, password_hash) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            setCustomerParameters(ps, customerBean);
            ps.setString(6, customerBean.getPasswordHash());
            return ps.executeUpdate();
        }
    }

    public CustomerBean doRetriveByEmail(String email) throws SQLException {
        CustomerBean customerBean = null;
        String query = "SELECT * FROM customer WHERE email = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    customerBean = new CustomerBean();
                    customerBean.setCustomerID(rs.getInt("customer_id"));
                    customerBean.setFirstName(rs.getString("first_name"));
                    customerBean.setLastName(rs.getString("last_name"));
                    customerBean.setEmail(rs.getString("email"));
                    customerBean.setPasswordHash(rs.getString("password_hash"));
                    customerBean.setPhone(rs.getString("phone"));
                    java.sql.Date birthDate = rs.getDate("birth_date");
                    if (birthDate != null) {
                        customerBean.setBirthDate(birthDate.toLocalDate());
                    }
                }
            }
        }

        return customerBean;
    }

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

           setCustomerParameters(ps, customerBean);
            ps.executeUpdate();
        }
    }

    public void doUpdatePassword(int customerID, String password) throws SQLException {
        String query = "UPDATE customer SET password_hash = ? WHERE customer_id = ?";

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, password);
            ps.setInt(2, customerID);
            ps.executeUpdate();
        }

    }

    private void setCustomerParameters(PreparedStatement ps, CustomerBean customerBean) throws SQLException {
        ps.setString(1, customerBean.getFirstName());
        ps.setString(2, customerBean.getLastName());
        ps.setString(3, customerBean.getEmail());
        ps.setString(4, customerBean.getPhone());
        if (customerBean.getBirthDate() != null) {
            ps.setDate(5, java.sql.Date.valueOf(customerBean.getBirthDate()));
        } else {
            ps.setNull(5, java.sql.Types.DATE);
        }
        ps.setInt(6, customerBean.getCustomerID());
    }

}
