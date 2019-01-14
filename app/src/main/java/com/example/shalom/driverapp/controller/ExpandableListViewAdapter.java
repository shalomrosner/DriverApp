package com.example.shalom.driverapp.controller;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.example.shalom.driverapp.R;
import com.example.shalom.driverapp.model.backend.CurrentLocation;
import com.example.shalom.driverapp.model.entities.Ride;

import java.util.ArrayList;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter implements Filterable {
    private Context context;
    private List<Ride> originalRideList;
    private List<Ride> rideList;
    private String id;
    private CurrentLocation location;
    private Location driversLocation;

    public ExpandableListViewAdapter(Context context, List<Ride> rideList, String id) {
        this.context = context;
        this.rideList = rideList;
        this.id = id;
        this.originalRideList = rideList;
        this.location = new CurrentLocation();
        this.driversLocation = location.getLocation();

    }

    @Override
    public int getGroupCount() {
        return rideList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return rideList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return rideList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Ride ride = (Ride) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.fragment_available_rides, null);

        }
        float distnace= ride.getStartLocation().distanceTo(driversLocation);
        return null;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if ((constraint == null) || (constraint.length() == 0)) {
                    results.values = originalRideList;
                    results.count = originalRideList.size();
                } else if (!Character.isDigit(constraint.charAt(constraint.length() - 1))) {
                    results.values = rideList;
                    results.count = rideList.size();
                } else {
                    List<Ride> newRideList = new ArrayList<Ride>();
                    for (Ride ride : rideList) {
                        float distance = (ride.getStartLocation().distanceTo(driversLocation));
                        if (distance <= Float.valueOf(constraint.toString()))
                            newRideList.add(ride);
                    }
                    results.values = newRideList;
                    results.count = newRideList.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                rideList = (List<Ride>) results.values;
            }


        };
    }

}

