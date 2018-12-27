package com.example.shalom.driverapp.model.backend;

import android.location.Location;

import com.example.shalom.driverapp.model.entities.Driver;

public interface IDBManager {
    void addDriver(Driver driver);
    void getNotYetTreatedRides();
    void getFinishedRides();
    void getDriversRides(Driver driver);
    void getNotYetTreatedRidesWithGivenDest(Location destCity);
    void getRidesByDate();
    void getNotYetTreatedRidesWithGivenInDistance();
    void getRidesByPrice();

}
