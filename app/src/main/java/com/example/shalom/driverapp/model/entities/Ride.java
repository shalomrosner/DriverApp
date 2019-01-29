package com.example.shalom.driverapp.model.entities;

import java.sql.Time;
import java.util.Date;

public class Ride {
    TypeOfRide typeOfRide;
    MyLocation startLocation;
    MyLocation endLocation;
    Date startTime;
    Date endTime;
    String name;
    String celNumber;
    String email;
    String driverId;
    private Date whenLoadToFirebase;

    public Ride(TypeOfRide typeOfRide, MyLocation startLocation, MyLocation endLocation, Time startTime, Time endTime, String name, String celNumber, String email, String driverId) {
        this.typeOfRide = typeOfRide;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.startTime = startTime;
        this.endTime = endTime;
        this.name = name;
        this.celNumber = celNumber;
        this.email = email;
        this.driverId = driverId;
    }

    public Ride() {
        this.typeOfRide = TypeOfRide.available;
        this.email = "";
        this.name = "";
        this.endLocation = new MyLocation();
        this.startLocation = new MyLocation();
        this.celNumber = "";
    }

    public MyLocation getStartLocation() {

        return startLocation;
    }

    public void setStartLocation(MyLocation startLocation) {
        this.startLocation = startLocation;
    }

    public MyLocation   getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(MyLocation endLocation) {
        this.endLocation = endLocation;
    }

    public TypeOfRide getTypeOfRide() {
        return typeOfRide;
    }

    public void setTypeOfRide(TypeOfRide typeOfRide) {
        this.typeOfRide = typeOfRide;
    }


    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }
    public Date getWhenLoadToFirebase() {
        return whenLoadToFirebase;
    }

    public void setWhenLoadToFirebase(Date whenLoadToFirebase) {
        this.whenLoadToFirebase = whenLoadToFirebase;
    }
}