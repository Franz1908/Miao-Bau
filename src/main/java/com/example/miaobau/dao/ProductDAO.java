package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.ProductBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * DAO per la tabella product. I metodi lato cliente
 * filtrano i prodotti cancellati (is_deleted = FALSE), quelli lato admin no
 * (l'admin deve vedere e ripristinare anche i cancellati).
 */
public class ProductDAO {

    // Catalogo completo (solo prodotti attivi) — lato cliente.
    public List<ProductBean> doRetrieveAll() throws SQLException {
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE is_deleted = false";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }

        }

        return products;
    }

    /*
     * Singolo prodotto per id, SOLO se attivo (is_deleted = FALSE). Versione
     * cliente: così un prodotto cancellato non è raggiungibile manipolando l'id
     * nell'URL. Per l'admin esiste doRetrieveByIdForAdmin (senza filtro).
     */
    public ProductBean doRetrieveById(int id) throws SQLException {
        ProductBean product = null;
        String query = "SELECT * FROM product WHERE product_id = ? AND is_deleted = FALSE";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = mapRow(rs);
                }
            }

        }

        return product;
    }

    // Prodotti in offerta (per il filtro "sconti" e la home).
    public List<ProductBean> doRetriveDiscountedProducts() throws SQLException{
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE on_sale = true AND is_deleted = FALSE";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }

        }

        return products;
    }

    /*
     * Prodotti più POPOLARI: i 10 più venduti. Fa JOIN con order_item, raggruppa
     * per prodotto e ordina per somma delle quantità vendute (decrescente).
     * "Popolare" = più venduto, calcolato dai dati reali degli ordini.
     */
    public List<ProductBean> doRetrivePopularProducts() throws SQLException {
        List<ProductBean> products = new ArrayList<>();
        String query =  "SELECT p.* " +
                "FROM product p " +
                "JOIN order_item oi ON oi.product_id = p.product_id " +
                "WHERE p.is_deleted = FALSE " +
                "GROUP BY p.product_id " +
                "ORDER BY SUM(oi.quantity) DESC " +   // somma delle quantità vendute
                "LIMIT 10;";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }

        }

        return products;
    }

    // Prodotti di una specie (navigazione per animale) — solo attivi.
    public List<ProductBean> doRetrieveBySpecies(int speciesID) throws SQLException{
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE species_id = ? AND is_deleted = false";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, speciesID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }
        }

        return products;
    }

    // Prodotti di una categoria — solo attivi. (Al momento non usato dai controller,
    // ma disponibile per completezza dell'API del DAO.)
    public List<ProductBean> doRetriveByCategory(int categoryID) throws SQLException{
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE category_id = ? and is_deleted = FALSE";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, categoryID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }
        }

        return products;
    }

    // Navigazione combinata specie + categoria — solo attivi.
    public List<ProductBean> doRetrieveBySpeciesAndCategory(int speciesID, int categoryID) throws SQLException{
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE category_id = ? AND species_id = ? AND is_deleted = FALSE";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            ps.setInt(1, categoryID);
            ps.setInt(2, speciesID);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }

        }

        return products;
    }

    /*
     * Inserisce un nuovo prodotto (admin). Usa setProductParameters per i 15 campi;
     * is_deleted non è nell'insert perché ha DEFAULT FALSE nel DB (nuovo = attivo).
     */
    public void doSave(ProductBean productBean) throws SQLException {
        String query =  "INSERT INTO product " +
                "(category_id, species_id, name, description, price, vat, " +
                "on_sale, discount_percentage, image, brand, " +
                "weight, ingredients, size, color, material) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            setProductParameters(ps, productBean);
            ps.executeUpdate();
        }
    }

    /*
     * Aggiorna un prodotto (admin). Riusa setProductParameters per i 15 campi
     * (posizioni 1-15) e imposta il 16° parametro col product_id del WHERE.
     */
    public void doUpdate(ProductBean productBean) throws SQLException{
        String query =  "UPDATE product SET " +
                "category_id = ?, " +
                "species_id = ?, " +
                "name = ?, " +
                "description = ?, " +
                "price = ?, " +
                "vat = ?, " +
                "on_sale = ?, " +
                "discount_percentage = ?, " +
                "image = ?, " +
                "brand = ?, " +
                "weight = ?, " +
                "ingredients = ?, " +
                "size = ?, " +
                "color = ?, " +
                "material = ? " +
                "WHERE product_id = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            setProductParameters(ps, productBean);           // parametri 1-15
            ps.setInt(16, productBean.getProductID());       // 16° = id del WHERE
            ps.executeUpdate();
        }
    }

    // Cancellazione SOFT: is_deleted = TRUE. Il prodotto sparisce dal catalogo
    // ma resta nel DB, così gli ordini storici che lo riferiscono restano validi.
    public void doDelete(int productID) throws SQLException {
        String query = "UPDATE product SET is_deleted = TRUE WHERE product_id = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            ps.setInt(1, productID);
            ps.executeUpdate();
        }
    }

    // Ripristino: annulla il soft delete (is_deleted = FALSE).
    public void doRestore(int productID) throws SQLException {
        String query = "UPDATE product SET is_deleted = FALSE WHERE product_id = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            ps.setInt(1, productID);
            ps.executeUpdate();
        }
    }

    /*
     * Singolo prodotto per id, ANCHE se cancellato (nessun filtro is_deleted).
     * Versione admin: serve per modificare o ripristinare un prodotto soft-deleted,
     * che la versione cliente (doRetrieveById) non restituirebbe.
     */
    public ProductBean doRetrieveByIdForAdmin(int id) throws SQLException {
        ProductBean product = null;
        String query = "SELECT * FROM product WHERE product_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    product = mapRow(rs);
                }
            }

        }

        return product;
    }

    /*
     * Tutti i prodotti inclusi i cancellati (nessun filtro), per il catalogo admin:
     * l'admin deve vedere anche i soft-deleted per poterli ripristinare.
     */
    public List<ProductBean> doRetriveAllForAdmin() throws SQLException {
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM product";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                products.add(mapRow(rs));
            }

        }

        return products;
    }

    /*
     * Ricerca prodotti per nome (parziale, LIKE con %), max 10 risultati.
     */
    public List<ProductBean> doRetrieveByName(String productName) throws SQLException {
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE name LIKE ? AND is_deleted = FALSE LIMIT 10";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, "%" + productName + "%");   // % attorno = corrispondenza parziale

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }

        }

        return products;
    }

    /*
     * Helper condiviso: imposta i 15 campi comuni a insert e update
     * (posizioni 1-15). Non tocca is_deleted (gestito da delete/restore) né
     * il product_id (impostato da doUpdate come 16° parametro).
     */
    private void setProductParameters(PreparedStatement ps, ProductBean productBean) throws SQLException {
        ps.setInt(1, productBean.getCategoryID());
        ps.setInt(2, productBean.getSpeciesID());
        ps.setString(3, productBean.getName());
        ps.setString(4, productBean.getDescription());
        ps.setBigDecimal(5, productBean.getPrice());
        ps.setBigDecimal(6, productBean.getVat());
        ps.setBoolean(7, productBean.isOnSale());
        ps.setBigDecimal(8, productBean.getDiscountPercentage());
        ps.setString(9, productBean.getImage());
        ps.setString(10, productBean.getBrand());
        ps.setBigDecimal(11, productBean.getWeight());
        ps.setString(12, productBean.getIngredients());
        ps.setString(13, productBean.getSize());
        ps.setString(14, productBean.getColor());
        ps.setString(15, productBean.getMaterial());
    }

    // Helper condiviso: trasforma la riga corrente del ResultSet in un
    // ProductBean. Usato da tutti i metodi di lettura, così la mappatura
    // colonna->campo è scritta una volta sola.
    private ProductBean mapRow(ResultSet rs) throws SQLException {
        ProductBean product = new ProductBean();
        product.setProductID(rs.getInt("product_id"));
        product.setCategoryID(rs.getInt("category_id"));
        product.setSpeciesID(rs.getInt("species_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setVat(rs.getBigDecimal("vat"));
        product.setOnSale(rs.getBoolean("on_sale"));
        product.setDiscountPercentage(rs.getBigDecimal("discount_percentage"));
        product.setImage(rs.getString("image"));
        product.setBrand(rs.getString("brand"));
        product.setDeleted(rs.getBoolean("is_deleted"));
        product.setWeight(rs.getBigDecimal("weight"));
        product.setColor(rs.getString("color"));
        product.setIngredients(rs.getString("ingredients"));
        product.setSize(rs.getString("size"));
        product.setMaterial(rs.getString("material"));
        return product;
    }
}