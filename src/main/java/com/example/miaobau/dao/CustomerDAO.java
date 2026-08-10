package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.CustomerBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

}
