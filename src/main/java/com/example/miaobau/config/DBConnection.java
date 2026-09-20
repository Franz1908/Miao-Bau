package com.example.miaobau.config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import java.sql.Connection;
import java.sql.SQLException;

/*
 * Gestisce la connessione al database tramite un CONNECTION POOL.
 * Invece di aprire e chiudere una connessione nuova a ogni query,
 * il pool tiene pronte alcune connessioni già aperte e le riusa.
 */
public class DBConnection {

    // Il DataSource (che incapsula il pool) è unico e condiviso da tutta l'app:
    // static perché ne basta uno solo per l'intera applicazione.
    private static final DataSource dataSource;

    // Blocco static: eseguito UNA sola volta, al caricamento della classe.
    // Qui si configura e crea il pool, prima di qualsiasi uso.
    static {
        PoolProperties props = new PoolProperties();

        // Coordinate del database: URL (host, porta, nome DB + timezone per evitare
        // problemi di fuso con le date), driver JDBC di MySQL, credenziali.
        props.setUrl("jdbc:mysql://localhost:3306/miao_bau?serverTimezone=UTC");
        props.setDriverClassName("com.mysql.cj.jdbc.Driver");
        props.setUsername("root");
        props.setPassword("password");

        // Dimensionamento del pool:
        //  - maxActive: massimo di connessioni contemporanee erogabili
        //  - initialSize: connessioni aperte già all'avvio, pronte all'uso
        props.setMaxActive(10);
        props.setInitialSize(5);

        // Crea il DataSource e gli applica la configurazione appena definita.
        dataSource = new DataSource();
        dataSource.setPoolProperties(props);
    }

    // Costruttore privato: impedisce di istanziare la classe. È una utility di
    // soli membri static, non ha senso crearne oggetti.
    private DBConnection() {
    }

    // Punto d'accesso unico per ottenere una connessione dal pool.
    // il close la rimette nel pool, non la distrugge.
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}