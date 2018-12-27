package com.example.shalom.driverapp.model.entities;

public class Driver {
    String firstName;
    String lastName;
    String id;
    String celNumber;
    String email;

    public Driver(String firstName, String lastName, String id, String celNumber, String email, String bankAccountNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.celNumber = celNumber;
        this.email = email;
        this.bankAccountNumber = bankAccountNumber;
    }
    public Driver(){}
    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCelNumber() {
        return celNumber;
    }

    public void setCelNumber(String celNumber) {
        this.celNumber = celNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    String bankAccountNumber;


}
