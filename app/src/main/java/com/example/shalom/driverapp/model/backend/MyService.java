package com.example.shalom.driverapp.model.backend;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.shalom.driverapp.model.datasource.NotifyDataChange;
import com.example.shalom.driverapp.model.entities.Ride;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MyService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        DBManagerFactory.getBL().notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.SECOND,-10);
                Date date = calendar.getTime();
                for (Ride ride : obj) {
                    if (ride.getWhenLoadToFirebase().after(date)) {
                        Intent intent = new Intent(MyService.this, MyReceiver.class);
                        sendBroadcast(intent);
                    }
                }
            }
            @Override
            public void onFailure(Exception exception) {
            }
        });
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        DBManagerFactory.getBL().stopNotifyToRideList();
        Toast.makeText(getApplicationContext()," Service destroy",Toast.LENGTH_LONG);
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
