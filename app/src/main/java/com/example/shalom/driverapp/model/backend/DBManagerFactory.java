package com.example.shalom.driverapp.model.backend;

import android.location.Location;

import com.example.shalom.driverapp.model.entities.Driver;
import com.example.shalom.driverapp.model.entities.Ride;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DBManagerFactory implements IDBManager {
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
    static List<Driver> driverList;

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RidesRef = database.getReference("rides");
        DriversRef = database.getReference("drivers");
        rideList = new ArrayList<>();
        driverList = new ArrayList<>();
    }
  /*  static IDBManager db_manager = null;

    public static IDBManager getBL()
    {
        if (db_manager == null)
            db_manager = new DBManagerFactory();
        return db_manager;
    }*/

    @Override
    public void addDriver(final Driver driver) {
        String key = driver.getId();
        DriversRef.child(key).setValue(driver);
    }

    @Override
    public void getNotYetTreatedRides() {

    }

    @Override
    public void getFinishedRides() {

    }

    @Override
    public void getDriversRides(Driver driver) {

    }

    @Override
    public void getNotYetTreatedRidesWithGivenDest(Location destCity) {

    }

    @Override
    public void getRidesByDate() {

    }

    @Override
    public void getNotYetTreatedRidesWithGivenInDistance() {

    }

    @Override
    public void getRidesByPrice() {

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
}
