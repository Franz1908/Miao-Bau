package com.example.miaobau.model;

public class AddressBean {

    private int addressID;
    private int customerID;
    private String street;
    private String civicNumber;
    private String postalCode;
    private String city;
    private String country;
    private boolean isDeleted;

    public AddressBean() {}

    public void setAddressID(int addressID) {
        this.addressID = addressID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCivicNumber(String civicNumber) {
        this.civicNumber = civicNumber;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public int getAddressID() {
        return addressID;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getStreet() {
        return street;
    }

    public String getCivicNumber() {
        return civicNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

}
