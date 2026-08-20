package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.CartBean;
import com.example.miaobau.model.CartItem;
import com.example.miaobau.model.OrderItemBean;
import com.example.miaobau.model.OrdersBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
                    "INSERT INTO order_item (order_id, product_id, quantity, unit_price, vat_frozen, product_name) "
                            + "VALUES (?, ?, ?, ?, ?, ?)")) {

                for (CartItem item : cart.getCart().values()) {
                    psItem.setInt(1, orderID);
                    psItem.setInt(2, item.getProduct().getProductID());
                    psItem.setInt(3, item.getQuantity());
                    psItem.setBigDecimal(4, item.getProduct().getPrice());   // prezzo congelato
                    psItem.setBigDecimal(5, item.getProduct().getVat());     // IVA congelata
                    psItem.setString(6, item.getProduct().getName());
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

    public List<OrdersBean> doRetriveByCustomer(int customerID) throws SQLException {
        List<OrdersBean> orders = new ArrayList<>();
        String query = "SELECT * FROM orders WHERE customer_id = ? ORDER BY order_date DESC";

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, customerID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    OrdersBean order = new OrdersBean();
                    order.setCustomerID(rs.getInt("customer_id"));
                    order.setOrderID(rs.getInt("order_id"));
                    order.setTotalPrice(rs.getBigDecimal("total_price"));
                    order.setOrderDate(rs.getObject("order_date", LocalDateTime.class));
                    orders.add(order);
                }
            }
        }

        return orders;
    }

    public List<OrderItemBean> doRetrieveItemsByOrder(int orderId) throws SQLException {
        List<OrderItemBean> ordersItem = new ArrayList<>();
        String query = "SELECT * FROM order_item WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, orderId);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    OrderItemBean orderItem = new OrderItemBean();
                    orderItem.setOrderID(rs.getInt("order_id"));
                    orderItem.setProductID(rs.getInt("product_id"));
                    orderItem.setProductName(rs.getString("product_name"));
                    orderItem.setQuantity(rs.getInt("quantity"));
                    orderItem.setUnitPrice(rs.getBigDecimal("unit_price"));
                    orderItem.setVatFrozen(rs.getBigDecimal("vat_frozen"));
                    ordersItem.add(orderItem);
                }
            }
        }

        return ordersItem;
    }

    public OrdersBean doRetriveByID(int orderID) throws SQLException {
        OrdersBean order = new OrdersBean();
        String query = "SELECT * FROM orders WHERE order_id = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, orderID);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    order.setOrderID(rs.getInt("order_id"));
                    order.setCustomerID(rs.getInt("customer_id"));
                    order.setOrderDate(rs.getObject("order_date", LocalDateTime.class));
                    order.setTotalPrice(rs.getBigDecimal("total_price"));
                }
            }
        }

        return order;
    }

}
