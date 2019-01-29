package com.example.shalom.driverapp.controller;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.shalom.driverapp.R;
import com.example.shalom.driverapp.model.backend.CurrentLocation;
import com.example.shalom.driverapp.model.backend.DBManagerFactory;
import com.example.shalom.driverapp.model.entities.MyLocation;
import com.example.shalom.driverapp.model.entities.Ride;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class progressExpandableListViewAdapter extends BaseExpandableListAdapter implements Filterable {
    private Context context;
    private List<Ride> originalRideList;
    private List<Ride> rideList;
    Filter distanceFilter;
    private CurrentLocation location;

    public progressExpandableListViewAdapter(Context context, List<Ride> rideList, String id) {
        this.context = context;
        this.rideList = rideList;
        this.originalRideList = rideList;
        location = new CurrentLocation(context);
        location.getLocation();

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
        progressExpandableListViewAdapter.ViewHolder1 viewHolder;
        if (convertView == null) {
            viewHolder = new progressExpandableListViewAdapter.ViewHolder1();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.progress_ride_item, null);
            viewHolder.full_name = (TextView) convertView.findViewById(R.id.name_progress);
            viewHolder.dest = (TextView) convertView.findViewById(R.id.dest_progress);
            viewHolder.start_location = (TextView) convertView.findViewById(R.id.start_loc_progress);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (progressExpandableListViewAdapter.ViewHolder1) (convertView.getTag());
        }
        viewHolder.full_name.setText(ride.getName());
        viewHolder.start_location.setText(location.getPlace(ride.getStartLocation()));
        viewHolder.dest.setText(location.getPlace(ride.getEndLocation()));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Ride ride = (Ride) getGroup(groupPosition);
        progressExpandableListViewAdapter.ViewHolder2 viewHolder;
        if (convertView == null) {
            viewHolder = new progressExpandableListViewAdapter.ViewHolder2();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.progress_child_item, null);
            viewHolder.FinishRide = (FloatingActionButton) convertView.findViewById(R.id.finishRide);
            viewHolder.current_location = (TextView) convertView.findViewById(R.id.current_location_progress);
            viewHolder.Payment = (TextView) convertView.findViewById(R.id.price_progress);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (progressExpandableListViewAdapter.ViewHolder2) (convertView.getTag());
        }
        viewHolder.current_location.setText(location.getPlace(location.locationA));
        double ridepay = 12+(ride.getStartLocation().distanceTo(location.locationA) / 1000) * 5;
        viewHolder.Payment.setText(String.valueOf(ridepay)+ " $");
        viewHolder.FinishRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    DBManagerFactory.getBL().rideIsFinished(ride);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Date date = Calendar.getInstance().getTime();
                ride.setEndTime(date);
                MyLocation c =new MyLocation();
                c.set(location.locationA);
                ride.setEndLocation(c);
                new AsyncTask<Void,Void,Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {
                        return DBManagerFactory.getBL().updateRide(ride);
                    }
                }.execute();
            }
        });

        return convertView;
        }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void resetToPreList() {
        rideList = originalRideList;
    }


    public Filter getDistanceFilter() {

        if(distanceFilter == null)
            distanceFilter = new Filter() {
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
                            float distance = (ride.getStartLocation().distanceTo(location.locationA));
                            distance /= 100;
                            int temp = (int)(distance);
                            distance = (float)(temp) / 10;
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
                    notifyDataSetChanged();
                }
            };;
        return distanceFilter;


    }
    @Override
    public Filter getFilter() {

        return getDistanceFilter();
    }

    private class ViewHolder1 {


        private TextView dest,full_name, start_location;

    }
    public class ViewHolder2 {
        protected FloatingActionButton FinishRide;
        TextView Payment;
        TextView current_location;
    }

}


