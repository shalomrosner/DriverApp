package com.example.shalom.driverapp.model.datasource;

import android.location.Location;

import com.example.shalom.driverapp.model.backend.IDBManager;
import com.example.shalom.driverapp.model.entities.Driver;
import com.example.shalom.driverapp.model.entities.Ride;
import com.example.shalom.driverapp.model.entities.TypeOfRide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBManager_Firebase implements IDBManager {
    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }

    public interface NotifyDataChange<T> {
        void OnDataChanged(T obj);

        void onFailure(Exception exception);
    }

    private static DatabaseReference RidesRef, DriversRef;
    static List<Ride> rideList;
    public static List<Driver> driverList;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RidesRef = database.getReference("rides");
        DriversRef = database.getReference("drivers");
        rideList = new ArrayList<>();
        driverList = new ArrayList<>();
    }

    @Override
    public Void addDriver(final Driver driver) {
        String key = driver.getId();
        DriversRef.child(key).setValue(driver);
        return null;
    }

    @Override
    public List<Ride> getNotTreatedRides() {
        List<Ride> NotTreatedRides = new ArrayList<>();
        for (Ride ride : rideList) {
            if (ride.getTypeOfRide() == TypeOfRide.available)
                NotTreatedRides.add(ride);
        }
        return NotTreatedRides;
    }

    @Override
    public List<Ride> getFinishedRides() {
        List<Ride> FinishedRides = new ArrayList<>();
        for (Ride ride : rideList) {
            if (ride.getTypeOfRide() == TypeOfRide.finished)
                FinishedRides.add(ride);
        }
        return FinishedRides;
    }

    @Override
    public List<Ride> getDriversRides(Driver driver) {
        List<Ride> DriversRides = new ArrayList<>();
        for (Ride ride : rideList) {
            if (ride.getDriverId() == driver.getId())
                DriversRides.add(ride);
        }
        return DriversRides;
    }

    @Override
    public List<Ride> getNotYetTreatedRidesWithGivenDest(Location destCity) {

        List<Ride> NotYetTreatedRidesWithGivenDest = new ArrayList<>();
        for (Ride ride : rideList) {
            if (destCity == ride.getEndLocation())
                NotYetTreatedRidesWithGivenDest.add(ride);
        }
        return NotYetTreatedRidesWithGivenDest;
    }

    @Override
    public List<Ride> getRidesByDate(Date date) {
        List<Ride> RidesByDate = new ArrayList<>();
        for (Ride ride : rideList) {
            if (ride.getStartTime().getMonth() == date.getMonth() &&
                    ride.getStartTime().equals(date.getDate()))
                RidesByDate.add(ride);
        }
        return RidesByDate;
    }

    @Override
    public List<Ride> getNotYetTreatedRidesWithGivenInDistance(Location drivers_given, float given_distance) {
        List<Ride> NotYetTreatedRidesWithGivenInDistance = new ArrayList<>();
        for (Ride ride : rideList) {
            float[] results = new float[5];
            Location.distanceBetween(drivers_given.getLatitude(), drivers_given.getLongitude(),
                    ride.getStartLocation().getLatitude(), ride.getStartLocation().getLongitude(), results);

            float distance = drivers_given.distanceTo(ride.getStartLocation());
            if (distance < given_distance)
                NotYetTreatedRidesWithGivenInDistance.add(ride);
        }
        return NotYetTreatedRidesWithGivenInDistance;
    }


    @Override
    public List<Ride> getRidesByPrice() {

        return null;
    }

    private static ChildEventListener rideRefChildEventListener;

    public static void notifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (rideRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify ride list"));
                return;
            }
            rideList.clear();

            rideRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String email = dataSnapshot.getKey();
                    ride.setEmail(email);
                    rideList.add(ride);
                    notifyDataChange.OnDataChanged(rideList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String email = dataSnapshot.getKey();
                    ride.setEmail(email);
                    for (int i = 0; i < rideList.size(); i++) {
                        if (rideList.get(i).getEmail().equals(email)) {
                            rideList.set(i, ride);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(rideList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String email = dataSnapshot.getKey();
                    ride.setEmail(email);
                    for (int i = 0; i < rideList.size(); i++) {
                        if (rideList.get(i).getEmail() == email) {
                            rideList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(rideList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };


            RidesRef.addChildEventListener(rideRefChildEventListener);
        }
    }

    public static void stopNotifyToRideList() {
        if (rideRefChildEventListener != null) {
            RidesRef.removeEventListener(rideRefChildEventListener);
            rideRefChildEventListener = null;
        }
    }


    private static ChildEventListener driverRefChildEventListener;

    public static void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange) {
        if (notifyDataChange != null) {
            if (driverRefChildEventListener != null) {
                notifyDataChange.onFailure(new Exception("first unNotify driver list"));
                return;
            }
            driverList.clear();

            driverRefChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    try {
                        driver.setId(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    driverList.add(driver);
                    notifyDataChange.OnDataChanged(driverList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    try {
                        driver.setId(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).getId().equals(id)) {
                            driverList.set(i, driver);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(driverList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Driver driver = dataSnapshot.getValue(Driver.class);
                    String id = dataSnapshot.getKey();
                    try {
                        driver.setId(id);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < driverList.size(); i++) {
                        if (driverList.get(i).getId() == id) {
                            driverList.remove(i);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(driverList);
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    notifyDataChange.onFailure(databaseError.toException());
                }
            };


            DriversRef.addChildEventListener(driverRefChildEventListener);
        }
    }

    public static void stopNotifyToDriverList() {
        if (driverRefChildEventListener != null) {
            DriversRef.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;
        }
    }
}
