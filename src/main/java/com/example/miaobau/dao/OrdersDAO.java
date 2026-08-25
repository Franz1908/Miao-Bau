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
                    "INSERT INTO orders (customer_id, order_date, total_price, address_id) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {

                psOrder.setInt(1, order.getCustomerID());
                psOrder.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
                psOrder.setBigDecimal(3, cart.getTotal());
                psOrder.setInt(4, order.getAddressID());
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
                    orders.add(mapOrderBase(rs));
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

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
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
        OrdersBean order = null;
        String query = "SELECT * FROM orders WHERE order_id = ?";

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, orderID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    order = mapOrderBase(rs);
                }
            }
        }

        return order;
    }

    public OrdersBean doRetrieveByIdWithCustomer(int orderID) throws SQLException {
        OrdersBean order = null;
        String query =
                "SELECT o.*, " +
                        "       c.first_name AS customer_first_name, " +
                        "       c.last_name AS customer_last_name, " +
                        "       c.email AS customer_email, " +
                        "       c.phone AS customer_phone " +
                        "FROM orders o " +
                        "JOIN customer c ON o.customer_id = c.customer_id " +
                        "WHERE o.order_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, orderID);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    order = mapOrderBase(rs);
                    order.setCustomerFirstName(rs.getString("customer_first_name"));
                    order.setCustomerLastName(rs.getString("customer_last_name"));
                    order.setCustomerEmail(rs.getString("customer_email"));
                    order.setCustomerPhone(rs.getString("customer_phone"));
                }
            }
        }

        return order;
    }

    public List<OrdersBean> doRetrieveFiltered(String email, LocalDateTime dateFrom, LocalDateTime dateTo) throws SQLException {
        List<OrdersBean> orders = new ArrayList<>();
        StringBuilder query = new StringBuilder(
                "SELECT o.*, " +
                        "c.first_name AS customer_first_name, c.last_name AS customer_last_name, " +
                        "c.email AS customer_email " +
                        "FROM orders o " +
                        "JOIN customer c ON o.customer_id = c.customer_id " +
                        "WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (email != null && !email.isBlank()) {
            query.append(" AND c.email LIKE ?");
            params.add("%" + email.trim() + "%");
        }

        if (dateFrom != null) {
            query.append(" AND o.order_date >= ?");
            params.add(Timestamp.valueOf(dateFrom));
        }

        if (dateTo != null) {
            query.append(" AND o.order_date <= ?");
            params.add(Timestamp.valueOf(dateTo));
        }

        query.append(" ORDER BY o.order_date DESC");

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    OrdersBean order = mapOrderBase(rs);
                    order.setCustomerFirstName(rs.getString("customer_first_name"));
                    order.setCustomerLastName(rs.getString("customer_last_name"));
                    order.setCustomerEmail(rs.getString("customer_email"));
                    orders.add(order);
                }
            }
        }

        return orders;
    }

    private OrdersBean mapOrderBase (ResultSet rs) throws SQLException {
        OrdersBean order = new OrdersBean();
        order.setCustomerID(rs.getInt("customer_id"));
        order.setOrderID(rs.getInt("order_id"));
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setOrderDate(rs.getObject("order_date", LocalDateTime.class));
        order.setAddressID(rs.getInt("address_id"));
        return order;
    }

}
