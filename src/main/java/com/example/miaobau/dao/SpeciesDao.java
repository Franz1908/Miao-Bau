package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.SpeciesBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SpeciesDao {

    public List<SpeciesBean> doRetriveAll() throws SQLException {
        List<SpeciesBean> species = new ArrayList<>();
        String query = "SELECT * FROM species";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                SpeciesBean specie = new SpeciesBean();
                specie.setSpeciesID(rs.getInt("species_id"));
                specie.setSpeciesName(rs.getString("species_name"));
                species.add(specie);
            }

        }

        return species;
    }

}
