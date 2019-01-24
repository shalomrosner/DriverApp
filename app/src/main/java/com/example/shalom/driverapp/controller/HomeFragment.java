package com.example.shalom.driverapp.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shalom.driverapp.R;
import com.example.shalom.driverapp.model.entities.Driver;


public class HomeFragment extends Fragment {
    View view;
    TextView welcome;
    Driver driver;
    public void getIntance(Driver driver) {
        this.driver = driver;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        welcome = (TextView) view.findViewById(R.id.welcome_Name);
       // welcome.setText("Welcome"+driver.getFullName() + "to getTaxi");

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        progressRidesFragment progressRidesFragment1 = new progressRidesFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_rides, progressRidesFragment1);
        transaction.commit();
    }





}
