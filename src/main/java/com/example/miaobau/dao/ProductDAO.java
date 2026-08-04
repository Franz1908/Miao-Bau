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

        try(Connection connection = DBConnection.getConnection();
            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
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

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    products.add(mapRow(rs));
                }
            }
        }

        return products;

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
