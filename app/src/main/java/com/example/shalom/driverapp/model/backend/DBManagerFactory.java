package com.example.shalom.driverapp.model.backend;

import com.example.shalom.driverapp.model.datasource.DBManager_Firebase;

public class DBManagerFactory  {


    public static DBManager_Firebase getBL() {
        return new DBManager_Firebase();

    }


}
