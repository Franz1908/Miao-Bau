package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.CartBean;
import com.example.miaobau.model.CartItem;
import com.example.miaobau.model.OrdersBean;

import java.sql.*;

public class OrdersDAO {

    public int doSave(OrdersBean order, CartBean cart) throws SQLException {
        Connection connection = null;
        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);   // inizio transazione

            int orderID;

            // Inserimento della testata dell'ordine
            try (PreparedStatement psOrder = connection.prepareStatement(
                    "INSERT INTO orders (customer_id, order_date, total_price) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {

                psOrder.setInt(1, order.getCustomerID());
                psOrder.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
                psOrder.setBigDecimal(3, cart.getTotal());
                psOrder.executeUpdate();

                // Recupero dell'id generato per collegare le righe
                try (ResultSet generatedKeys = psOrder.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creazione ordine fallita, nessun id ottenuto");
                    }
                }
            }

            // Inserimento delle righe dell'ordine, con prezzo e IVA congelati
            try (PreparedStatement psItem = connection.prepareStatement(
                    "INSERT INTO order_item (order_id, product_id, quantity, unit_price, vat_frozen) "
                            + "VALUES (?, ?, ?, ?, ?)")) {

                for (CartItem item : cart.getCart().values()) {
                    psItem.setInt(1, orderID);
                    psItem.setInt(2, item.getProduct().getProductID());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setBigDecimal(4, item.getProduct().getPrice());   // prezzo congelato
                    psItem.setBigDecimal(5, item.getProduct().getVat());     // IVA congelata
                    psItem.executeUpdate();
                }
            }

            connection.commit();   // tutto ok: confermo la transazione
            return orderID;

        } catch (SQLException e) {
            if (connection != null) {
                connection.rollback();   // errore: annullo tutto
            }
            throw e;
        } finally {
            if (connection != null) {
                connection.setAutoCommit(true);   // ripristino prima di restituire al pool
                connection.close();
            }
        }
    }

}
