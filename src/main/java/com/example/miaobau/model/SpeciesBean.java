package com.example.miaobau.model;

public class SpeciesBean {

    private int speciesID;
    private String speciesName;

    public SpeciesBean(){}

    public void setSpeciesID(int speciesID){
        this.speciesID = speciesID;
    }

    public void setSpeciesName(String speciesName){
        this.speciesName = speciesName;
    }

    public int getSpeciesID(){
        return speciesID;
    }

    public String getSpeciesName(){
        return speciesName;
    }

}
