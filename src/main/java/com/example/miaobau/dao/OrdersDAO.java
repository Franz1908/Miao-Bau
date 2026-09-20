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

/*
 * DAO per gli ordini (tabelle orders + order_item).
 */
public class OrdersDAO {

    /*
     * Salva un ordine completo (testata + righe) in modo transazionale.
     * Qui NON si usa try-with-resources sulla connessione, perché serve controllo
     * manuale su commit/rollback: si disattiva l'autocommit, si eseguono le due
     * insert, e solo se entrambe riescono si fa commit; a fronte di un errore,
     * rollback annulla tutto.
     */
    public int doSave(OrdersBean order, CartBean cart) throws SQLException {
        Connection connection = null;

        try {
            connection = DBConnection.getConnection();
            connection.setAutoCommit(false);   // inizio transazione: i comandi non vengono confermati subito
            int orderID;

            // 1) Inserimento della TESTATA dell'ordine (tabella orders).
            //    RETURN_GENERATED_KEYS per recuperare l'order_id generato, che serve
            //    poi a collegare le righe alla testata.
            try (PreparedStatement psOrder = connection.prepareStatement(
                    "INSERT INTO orders (customer_id, order_date, total_price, address_id) VALUES (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS)) {

                psOrder.setInt(1, order.getCustomerID());
                psOrder.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
                psOrder.setBigDecimal(3, cart.getTotal());
                psOrder.setInt(4, order.getAddressID());
                psOrder.executeUpdate();

                // Recupero dell'id generato per collegare le righe.
                try (ResultSet generatedKeys = psOrder.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        orderID = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Creazione ordine fallita, nessun id ottenuto");
                    }
                }
            }

            // 2) Inserimento delle RIGHE dell'ordine (tabella order_item), una per
            //    prodotto nel carrello. I valori vengono CONGELATI: prezzo, IVA e
            //    nome del prodotto sono copiati qui e NON cambieranno più, anche se
            //    il prodotto venisse modificato o cancellato in futuro (dato storico).
            try (PreparedStatement psItem = connection.prepareStatement(
                    "INSERT INTO order_item (order_id, product_id, quantity, unit_price, vat_frozen, product_name) "
                            + "VALUES (?, ?, ?, ?, ?, ?)")) {

                for (CartItem item : cart.getCart().values()) {
                    psItem.setInt(1, orderID);                                  // collega alla testata
                    psItem.setInt(2, item.getProduct().getProductID());
                    psItem.setInt(3, item.getQuantity());
                    // Prezzo congelato = quello effettivamente pagato: scontato se in
                    // offerta, pieno altrimenti. Coerente con il totale del carrello.
                    if (item.getProduct().isOnSale()) {
                        psItem.setBigDecimal(4, item.getProduct().getDiscountedPrice());
                    }
                    else {
                        psItem.setBigDecimal(4, item.getProduct().getPrice());
                    }
                    psItem.setBigDecimal(5, item.getProduct().getVat());        // IVA congelata
                    psItem.setString(6, item.getProduct().getName());          // nome congelato
                    psItem.executeUpdate();
                }
            }

            connection.commit();   // entrambe le insert riuscite: confermo tutto insieme
            return orderID;

        } catch (SQLException e) {
            // Qualcosa è andato storto: annullo TUTTO (testata + eventuali righe già
            // inserite), così non resta un ordine a metà.
            if (connection != null) {
                connection.rollback();
            }
            throw e;
        } finally {
            // Prima di restituire la connessione al pool, ripristino l'autocommit
            // al suo stato di default. Poi chiudo (rilascio al pool).
            if (connection != null) {
                connection.setAutoCommit(true);
                connection.close();
            }
        }
    }

    // Ordini di un cliente, dal più recente (per lo storico ordini lato cliente).
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

    /*
     * Righe di un ordine (i prodotti acquistati). Legge i valori CONGELATI da
     * order_item (nome, prezzo, IVA al momento dell'acquisto).
     */
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
                    orderItem.setProductName(rs.getString("product_name"));   // nome congelato
                    orderItem.setQuantity(rs.getInt("quantity"));
                    orderItem.setUnitPrice(rs.getBigDecimal("unit_price"));    // prezzo congelato
                    orderItem.setVatFrozen(rs.getBigDecimal("vat_frozen"));    // IVA congelata
                    ordersItem.add(orderItem);
                }
            }
        }

        return ordersItem;
    }

    // Testata di un ordine per id.
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

    /*
     * Testata ordine + dati del cliente, con JOIN su customer (per il dettaglio
     * ordine lato ADMIN, che mostra chi ha ordinato).
     */
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
                    // Campi "di comodo" del bean, riempiti dalla join (alias della query).
                    order.setCustomerFirstName(rs.getString("customer_first_name"));
                    order.setCustomerLastName(rs.getString("customer_last_name"));
                    order.setCustomerEmail(rs.getString("customer_email"));
                    order.setCustomerPhone(rs.getString("customer_phone"));
                }
            }
        }

        return order;
    }

    /*
     * Ricerca ordini con filtri opzionali (email cliente, data da, data a), per la
     * vista admin. La query è costruita dinamicamente: si parte da "WHERE 1=1"
     * (sempre vero, comodo per aggiungere clausole con AND senza dover gestire il
     * primo AND) e si aggiunge una condizione solo per i filtri effettivamente
     * presenti. I valori vanno in una lista, poi associati ai ? nell'ordine.
     */
    public List<OrdersBean> doRetrieveFiltered(String email, LocalDateTime dateFrom, LocalDateTime dateTo) throws SQLException {
        List<OrdersBean> orders = new ArrayList<>();
        StringBuilder query = new StringBuilder(
                "SELECT o.*, " +
                        "c.first_name AS customer_first_name, c.last_name AS customer_last_name, " +
                        "c.email AS customer_email " +
                        "FROM orders o " +
                        "JOIN customer c ON o.customer_id = c.customer_id " +
                        "WHERE 1=1");
        List<Object> params = new ArrayList<>();   // valori da associare ai ?, in ordine

        // Ogni filtro presente aggiunge una clausola e il suo valore alla lista.
        if (email != null && !email.isBlank()) {
            query.append(" AND c.email LIKE ?");
            params.add("%" + email.trim() + "%");   // LIKE con % = ricerca parziale
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

            // Associa i valori ai ? nell'ordine in cui sono stati aggiunti.
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));   // i+1 perché i parametri partono da 1
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

    /*
     * Helper condiviso (DRY): mappa i campi BASE della testata ordine, comuni a
     * tutte le query sopra. I dati cliente (dove presenti via join) vengono
     * aggiunti dai singoli metodi, perché non tutte le query li recuperano.
     */
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
