package com.example.miaobau.model;

public class CategoryBean {

    private int categoriID;
    private String categoryName;

    public CategoryBean(){}

    public void setCategoriID(int categoriID){
        this.categoriID = categoriID;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public int getCategoriID(){
        return categoriID;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
