package com.example.miaobau.model;

import java.time.LocalDate;

public class CustomerBean {

    private int customerID;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate birthDate;
    private String passwordHash;

    public CustomerBean(){}

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }


    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public int getCustomerID() {
        return customerID;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getLastName() {
        return lastName;
    }
}
