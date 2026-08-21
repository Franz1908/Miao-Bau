package com.example.miaobau.dao;

import com.example.miaobau.config.DBConnection;
import com.example.miaobau.model.ProductBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

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

    public ProductBean doRetrieveById(int id) throws SQLException {
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

    public List<ProductBean> doRetriveDiscountedProduct() throws SQLException{
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE on_sale = true";

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(mapRow(rs));
            }

        }

        return products;
    }

    public List<ProductBean> doRetrieveBySpecies(int speciesId) throws SQLException{
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE species_id = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, speciesId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }
        }

        return products;
    }

    public List<ProductBean> doRetriveByCategory(int categoryId) throws SQLException{
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE category_id = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setInt(1, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }
        }

        return products;
    }

    public List<ProductBean> doRetrieveBySpeciesAndCategory(int speciesId, int categoryId) throws SQLException{
        List<ProductBean> products = new ArrayList<>();
        String query = "SELECT * FROM product WHERE category_id = ? AND species_id = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            ps.setInt(1, categoryId);
            ps.setInt(2, categoryId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    products.add(mapRow(rs));
                }
            }

        }

        return products;
    }

    public void doSave(ProductBean productBean) throws SQLException {
        String query =
                "INSERT INTO product " +
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

    public void doUpdate(ProductBean productBean) throws SQLException{
        String query =
                "UPDATE product SET " +
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

            setProductParameters(ps, productBean);
            ps.setInt(16, productBean.getProductID());
            ps.executeUpdate();
        }
    }

    public void doDelete(int productID) throws SQLException {
        String query = "UPDATE product SET is_deleted = TRUE WHERE product_id = ?";

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query)){

            ps.setInt(1, productID);
            ps.executeUpdate();
        }
    }

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

    // Metodo di supporto: trasforma la riga corrente del ResultSet in un ProductBean.
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
