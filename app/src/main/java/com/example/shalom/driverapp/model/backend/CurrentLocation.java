package com.example.shalom.driverapp.model.backend;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CurrentLocation {
    Context context;
    LocationManager locationManager;
    LocationListener locationListener;
    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    public static Location locationA = new Location("A");

    public CurrentLocation(Context context) {
        this.context = context;
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                if (isGPSEnabled)
                    locationA.set(location);
                if (isNetworkEnabled)
                    locationA.set(location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    public void getLocation(Context context) {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        int locationOff = 0;
        try {
            //gets the status mode of the location's settings in the device
            locationOff = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        //if the GPS in the the device is off
        if (locationOff == 0) {
            //Moves to the device's location settings and asks to enable the GPS
            Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            Toast.makeText(context, "please turn on your gps location", Toast.LENGTH_SHORT).show();
            context.startActivity(onGPS);
        } else {
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2);
                }else{
                    isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    if (!isGPSEnabled && !isNetworkEnabled) {
                        Toast.makeText(context, "Please Set Network/GPS ON", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (isNetworkEnabled)
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                    if (isGPSEnabled)
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                }
            } catch(Exception e){
                Toast.makeText(context, "Error: " + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    //this func get location and convert it to name of location
    public static String getPlace(Location location, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (addresses.size() > 0) {
                String cityName = addresses.get(0).getAddressLine(0);
                return cityName;
            }

            return "no place: \n (" + location.getLongitude() + " , " + location.getLatitude() + ")";
        } catch (
                IOException e)

        {
            e.printStackTrace();
        }
        return "IOException ...";
    }
    public static String getCity(Location location, Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> list = null;
        try {
            list = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (list != null & list.size() > 0) {
                Address address = list.get(0);
                return (String) address.getLocality();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return "IOException ...";
    }

  /*  private void getLocation() {

        //     Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 5);

        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 5) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the location", Toast.LENGTH_SHORT).show();
            }
        }

    }*/
}

