package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.AdminBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/*
 * DAO per la tabella admin. Espone solo la lettura per username, che serve
 * al login amministratore. Non ci sono metodi di scrittura
 * perché gli admin non si gestiscono dall'applicazione (sono inseriti a mano
 * nel DB).
 */
public class AdminDAO {

    /*
     * Recupera un admin dal suo username (unico). Restituisce il bean se esiste,
     * null altrimenti. L'admin viene identificato per username
     */
    public AdminBean doRetriveByUsername(String username) throws SQLException {
        AdminBean adminBean = null;   // null se lo username non esiste
        String query = "SELECT * FROM admin WHERE username = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            ps.setString(1, username);   // ? riempito col PreparedStatement: no SQL injection

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){   // if: lo username è unico, al massimo una riga
                    adminBean = new AdminBean();
                    // Mappa colonne -> campi del bean. Si recupera anche l'hash della
                    // password, che servirà al controller per verificare le credenziali.
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