package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.CategoryBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public List<CategoryBean> doRetriveAll() throws SQLException {
        List<CategoryBean> categorys = new ArrayList<>();
        String query = "SELECT * FROM category";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                CategoryBean category = new CategoryBean();
                category.setCategoryID(rs.getInt("category_id"));
                category.setCategoryName(rs.getString("category_name"));
                categorys.add(category);
            }

        }

        return categorys;
    }


}
