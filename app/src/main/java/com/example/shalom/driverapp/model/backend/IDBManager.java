package com.example.shalom.driverapp.model.backend;

import android.content.Context;
import android.location.Location;

import com.example.shalom.driverapp.model.entities.Driver;
import com.example.shalom.driverapp.model.entities.Ride;

import java.util.List;

public interface IDBManager {
    Void addDriver(final Driver driver);

    List<Ride> getNotTreatedRides();
    List<Ride> getDriversRidesInProgress(final String driverId);
    List<Ride> getDriversFinishedRides(final String driverId);

    List<Ride> getDriversRides(final String driverId);

     List<Ride> getNotYetTreatedRidesWithGivenDest(final String destCity, final Context context);
    List<Ride> getRidesByDate(final String date);

    List<Ride> getNotYetTreatedRidesWithGivenInDistance(Location drivers_given, float given_distance);

    List<Ride> getRidesByPrice(final float price);

    void rideIsBeingTreated(Ride ride) throws Exception;

    void rideIsFinished(Ride ride) throws Exception;

    Void updateRide(final Ride ride);

}
