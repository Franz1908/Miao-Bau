package com.example.miaobau.model;

import java.math.BigDecimal;

public class ProductBean {

    private int productID;
    private int categoryID;
    private int speciesID;
    private String name;
    private String description;
    private BigDecimal price;
    private BigDecimal vat;
    private boolean onSale;
    private BigDecimal discountPercentage;
    private String image;
    private String brand;
    private boolean isDeleted;
    private BigDecimal weight;
    private String ingredients;
    private String size;
    private String color;
    private String material;

    public ProductBean(){

    }

    public void setProductID(int productID){
        this.productID = productID;
    }

    public void setCategoryID(int categoryID){
        this.categoryID = categoryID;
    }

    public void setSpeciesID(int speciesID){
        this.speciesID = speciesID;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setPrice(BigDecimal price){
        this.price = price;
    }

    public void setVat(BigDecimal vat){
        this.vat = vat;
    }

    public void setOnSale(boolean onSale){
        this.onSale = onSale;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage){
        this.discountPercentage = discountPercentage;
    }

    public void  setImage(String image){
        this.image = image;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public void setDeleted(boolean isDeleted){
        this.isDeleted = isDeleted;
    }

    public void setWeight(BigDecimal weight){
        this.weight = weight;
    }

    public void setIngredients(String ingredients){
        this.ingredients = ingredients;
    }

    public void setSize(String size){
        this.size = size;
    }

    public void setColor(String color){
        this.color = color;
    }

    public void setMaterial(String material){
        this.material = material;
    }

    public int getProductID() {
        return productID;
    }

    public String getName() {
        return name;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public int getSpeciesID() {
        return speciesID;
    }

    public BigDecimal getPrice(){
        return price;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public String getBrand() {
        return brand;
    }

    public String getColor() {
        return color;
    }

    public String getImage() {
        return image;
    }

    public String getIngredients() {
        return ingredients;
    }

    public String getMaterial() {
        return material;
    }

    public String getSize() {
        return size;
    }

}
