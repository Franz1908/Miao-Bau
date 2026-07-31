package com.example.miaobau.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private static final DataSource dataSource;

    static {
        PoolProperties props = new PoolProperties();

        // Indirizzo del database
        props.setUrl("jdbc:mysql://localhost:3306/miao_bau?serverTimezone=UTC");
        props.setDriverClassName("com.mysql.cj.jdbc.Driver");
        props.setUsername("root");
        props.setPassword("password");

        // Dimensione del pool
        props.setMaxActive(10);
        props.setInitialSize(5);

        dataSource = new DataSource();
        dataSource.setPoolProperties(props);
    }

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
