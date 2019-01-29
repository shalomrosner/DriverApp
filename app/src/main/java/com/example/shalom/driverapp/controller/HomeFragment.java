package com.example.shalom.driverapp.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shalom.driverapp.R;


public class HomeFragment extends Fragment {
    View view;
    String driversid;
    public void getIntance(String driversid) {
        this.driversid = driversid;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        progressRidesFragment progressRidesFragment1 = new progressRidesFragment();
        progressRidesFragment1.getIntance(driversid);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container_rides, progressRidesFragment1);
        transaction.commit();
    }





}
