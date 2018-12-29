package com.example.shalom.driverapp.model.backend;

import android.location.Location;

import com.example.shalom.driverapp.model.datasource.DBManager_Firebase;
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

public class DBManagerFactory  {

    static IDBManager db_manager = null;

    public static IDBManager getBL() {
        if (db_manager == null)
            db_manager = new DBManager_Firebase();
        return db_manager;
    }


}
