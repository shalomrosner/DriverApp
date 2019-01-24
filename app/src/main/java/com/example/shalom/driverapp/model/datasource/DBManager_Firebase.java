package com.example.shalom.driverapp.model.datasource;

import android.content.Context;
import android.location.Location;

import com.example.shalom.driverapp.model.backend.CurrentLocation;
import com.example.shalom.driverapp.model.backend.IDBManager;
import com.example.shalom.driverapp.model.entities.Driver;
import com.example.shalom.driverapp.model.entities.Ride;
import com.example.shalom.driverapp.model.entities.TypeOfRide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DBManager_Firebase implements IDBManager {

    public interface Action<T> {
        void onSuccess(T obj);

        void onFailure(Exception exception);

        void onProgress(String status, double percent);
    }


    private static DatabaseReference RidesRef, DriversRef;
    public  List<Ride> rideList= new ArrayList<>();
    public  List<Driver> driverList= new ArrayList<>();

    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RidesRef = database.getReference("rides");
        DriversRef = database.getReference("drivers");
    }

    @Override
    public Void addDriver(final Driver driver) {
        String key = driver.getId();
        DriversRef.child(key).setValue(driver);
        return null;
    }

    @Override
    public List<Ride> getNotTreatedRides() {
        boolean flag = true;
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rideList = notifyRides;
                for (Ride ride : rideList) {
                    if (ride.getTypeOfRide() != TypeOfRide.available)
                        rideList.remove(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rideList;
    }
    @Override
    public List<Ride> getRidesInProgress() {
        boolean flag = true;
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rideList = notifyRides;
                for (Ride ride : rideList) {
                    if (ride.getTypeOfRide() != TypeOfRide.occupied)
                        rideList.remove(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rideList;
    }
    @Override
    public List<Ride> getFinishedRides() {
        boolean flag = true;
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rideList = notifyRides;
                for (Ride ride : rideList) {
                    if (ride.getTypeOfRide() != TypeOfRide.finished)
                        rideList.remove(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rideList;

    }

    @Override
    public List<Ride> getDriversRides(final String driverId) {
        boolean flag = true;
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rideList = notifyRides;
                for (Ride ride : rideList) {
                    if (ride.getTypeOfRide() != TypeOfRide.finished || !ride.getDriverId().equals(driverId))
                        rideList.remove(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rideList;
    }

    @Override
    public List<Ride> getNotYetTreatedRidesWithGivenDest(final String destCity, final Context context) {
        boolean flag = true;
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rideList = notifyRides;
                for (Ride ride : rideList)
                {
                    String ridelo = CurrentLocation.getCity(ride.getEndLocation(),context);
                    if (ride.getTypeOfRide() != TypeOfRide.available || !destCity.toString().equals( ridelo.toString()))
                        rideList.remove(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rideList;

    }

    @Override
    public List<Ride> getRidesByDate(final String date) {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        boolean flag = true;
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rideList = notifyRides;
                for (Ride ride : rideList) {
                    String temp= simpleDateFormat.format(ride.getEndTime());
                    if (ride.getTypeOfRide() != TypeOfRide.finished || !temp.toString().equals(date))
                        rideList.remove(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rideList;
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
    public List<Ride> getRidesByPrice(final float price) {

        boolean flag = true;
        notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> notifyRides) {
                rideList = notifyRides;
                for (Ride ride : rideList)
                {
                    double ridepay = 12+(ride.getStartLocation().distanceTo(ride.getEndLocation()) / 1000) * 5;
                    if (ridepay > price)
                        rideList.remove(ride);
                }
            }
            @Override
            public void onFailure(Exception exception) {

            }
        });
        return rideList;

    }

    @Override
    public void rideIsBeingTreated(Ride ride) throws Exception {
        if (ride.getTypeOfRide() == TypeOfRide.available)
            ride.setTypeOfRide(TypeOfRide.occupied);
        else
            throw new Exception("the ride is not available!");
    }
    @Override
    public void rideIsFinished(Ride ride) throws Exception {
        if (ride.getTypeOfRide() == TypeOfRide.occupied)
            ride.setTypeOfRide(TypeOfRide.finished);
        else
            throw new Exception("the ride has not started!");
    }
    @Override
    public Void updateRide(final Ride toUpdate) {
        final String key = (toUpdate.getCelNumber());
        RidesRef.child(key).setValue(toUpdate);
        return null;
    }

    private  ChildEventListener rideRefChildEventListener;

    public void notifyToRideList(final NotifyDataChange<List<Ride>> notifyDataChange) {
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
                    String cel = dataSnapshot.getKey();
                    ride.setCelNumber(cel);
                    rideList.add(ride);
                    notifyDataChange.OnDataChanged(rideList);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String cel = dataSnapshot.getKey();
                    ride.setCelNumber(cel);
                    for (int i = 0; i < rideList.size(); i++) {
                        if (rideList.get(i).getCelNumber().equals(cel)) {
                            rideList.set(i, ride);
                            break;
                        }
                    }
                    notifyDataChange.OnDataChanged(rideList);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Ride ride = dataSnapshot.getValue(Ride.class);
                    String cel = dataSnapshot.getKey();
                    ride.setCelNumber(cel);
                    for (int i = 0; i < rideList.size(); i++) {
                        if (rideList.get(i).getCelNumber() == cel) {
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

    public  void stopNotifyToRideList() {
        if (rideRefChildEventListener != null) {
            RidesRef.removeEventListener(rideRefChildEventListener);
            rideRefChildEventListener = null;
        }
    }


    private  ChildEventListener driverRefChildEventListener;

    public  void notifyToDriverList(final NotifyDataChange<List<Driver>> notifyDataChange) {
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

    public  void stopNotifyToDriverList() {
        if (driverRefChildEventListener != null) {
            DriversRef.removeEventListener(driverRefChildEventListener);
            driverRefChildEventListener = null;
        }
    }
}
