package com.example.shalom.driverapp.controller;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;

import com.example.shalom.driverapp.R;
import com.example.shalom.driverapp.model.backend.DBManagerFactory;
import com.example.shalom.driverapp.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

public class availableRidesFragment extends Fragment {
    ExpandableListViewAdapter rideExpandableListAdapter;
    View view;
    EditText filter;
    String driversid;
    private ExpandableListView lv;
    public static List<Ride> ridelist = new ArrayList<>();
    public void getIntance(String driversid) {
        this.driversid = driversid;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_available_rides, container, false);
        lv = (ExpandableListView) view.findViewById(R.id.ride_lv);
        filter = (EditText) view.findViewById(R.id.distance_filter);
        lv.setTextFilterEnabled(true);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    rideExpandableListAdapter.resetToPreList();
                }

                rideExpandableListAdapter.getFilter().filter(s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final Context context = this.getContext();
        ridelist = DBManagerFactory.getBL().getNotTreatedRides();
        rideExpandableListAdapter = new ExpandableListViewAdapter(context, ridelist,driversid);
        lv.setAdapter(rideExpandableListAdapter);
        return view;
    }
}
