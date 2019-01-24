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
import com.example.shalom.driverapp.model.datasource.NotifyDataChange;
import com.example.shalom.driverapp.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

public class progressRidesFragment extends Fragment {
    progressExpandableListViewAdapter rideExpandableListAdapter;
    View view;
    EditText filter;
    String driversid;
    private ExpandableListView lv;
    public static List<Ride> ridelist = new ArrayList<>();
    public void getIntance(String driversid) {
        this.driversid = driversid;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_progress_rides, container, false);
        lv = (ExpandableListView) view.findViewById(R.id.ride_lv_progress);
        filter = (EditText) view.findViewById(R.id.filter_progress);
        lv.setTextFilterEnabled(true);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    rideExpandableListAdapter.resetToPreList();
                }

                rideExpandableListAdapter.getDistanceFilter().filter(s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final Context context = this.getContext();
        ridelist = DBManagerFactory.getBL().getRidesInProgress();
        DBManagerFactory.getBL().notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                if (ridelist.size() != 0) {
                    rideExpandableListAdapter = new progressExpandableListViewAdapter(context, ridelist,driversid);
                    lv.setAdapter(rideExpandableListAdapter);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });

        return view;
    }


    
}
