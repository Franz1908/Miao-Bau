package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.AdminBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    public AdminBean doRetriveByUsername(String username) throws SQLException {
        AdminBean adminBean = null;
        String query = "SELECT * FROM admin WHERE username = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    adminBean = new AdminBean();
                    adminBean.setAdminID(rs.getInt("admin_id"));
                    adminBean.setEmail(rs.getString("email"));
                    adminBean.setUsername(rs.getString("username"));
                    adminBean.setPasswordHash(rs.getString("password_hash"));
                }
            }
        }

        return adminBean;
    }

}
