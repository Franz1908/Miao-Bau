package com.example.miaobau.model;

public class CategoryBean {

    private int categoryID;
    private String categoryName;

    public CategoryBean(){}

    public void setCategoryID(int categoryID){
        this.categoryID = categoryID;
    }

    public void setCategoryName(String categoryName){
        this.categoryName = categoryName;
    }

    public int getCategoryID(){
        return categoryID;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
