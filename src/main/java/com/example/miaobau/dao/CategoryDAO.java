package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.CategoryBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * DAO per la tabella category. Stessa struttura di SpeciesDAO.
 */
public class CategoryDAO {

    // Recupera tutte le categorie (Cibo secco, Giochi, Cucce, ...).
    public List<CategoryBean> doRetriveAll() throws SQLException {
        List<CategoryBean> categories = new ArrayList<>();
        String query = "SELECT * FROM category";

        // try-with-resources: chiude da sé connessione, statement e result set
        // (la connessione torna al pool).
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                CategoryBean category = new CategoryBean();
                category.setCategoryID(rs.getInt("category_id"));       // colonna -> bean
                category.setCategoryName(rs.getString("category_name"));
                categories.add(category);
            }

        }

        return categories;
    }

}