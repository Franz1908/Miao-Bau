package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.CustomerBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerDAO {

    public int doSave(CustomerBean customerBean) throws SQLException {
        String query = "INSERT INTO customer (first_name, last_name, email, phone, birth_date, password_hash) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            ps.setString(1, customerBean.getFirstName());
            ps.setString(2, customerBean.getLastName());
            ps.setString(3, customerBean.getEmail());
            ps.setString(4, customerBean.getPhone());

            if (customerBean.getBirthDate() != null) {
                ps.setDate(5, java.sql.Date.valueOf(customerBean.getBirthDate()));
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }
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

            try(ResultSet rs = ps.executeQuery()){
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

}
