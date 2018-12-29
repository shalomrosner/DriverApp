package com.example.shalom.driverapp.model.backend;

import android.location.Location;

import com.example.shalom.driverapp.model.entities.Driver;
import com.example.shalom.driverapp.model.entities.Ride;

import java.util.Date;
import java.util.List;

public interface IDBManager {
    void addDriver(final Driver driver);
    List<Ride> getNotTreatedRides();
    List<Ride> getFinishedRides();
    List<Ride> getDriversRides(Driver driver);
    List<Ride> getNotYetTreatedRidesWithGivenDest(Location destCity);
    List<Ride> getRidesByDate(Date date);
    List<Ride> getNotYetTreatedRidesWithGivenInDistance(Location drivers_given, float given_distance);
    List<Ride> getRidesByPrice();

}
