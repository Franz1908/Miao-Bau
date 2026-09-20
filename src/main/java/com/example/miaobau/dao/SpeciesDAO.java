package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.SpeciesBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * DAO (Data Access Object) per la tabella species.
 */
public class SpeciesDAO {

    // Recupera tutte le specie (Cane, Gatto). Usato all'avvio dall'AppContextListener.
    public List<SpeciesBean> doRetriveAll() throws SQLException {
        List<SpeciesBean> species = new ArrayList<>();   // lista vuota da riempire (mai null)
        String query = "SELECT * FROM species";

        // try-with-resources: Connection, PreparedStatement e ResultSet vengono
        // chiusi automaticamente alla fine del blocco, anche in caso di eccezione.
        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery()){

            // Scorre le righe del risultato una a una.
            while(rs.next()){
                SpeciesBean specie = new SpeciesBean();
                // Mappa colonna del DB -> campo del bean.
                specie.setSpeciesID(rs.getInt("species_id"));
                specie.setSpeciesName(rs.getString("species_name"));
                species.add(specie);
            }

        }

        return species;   // lista (eventualmente vuota) di tutte le specie
    }

}