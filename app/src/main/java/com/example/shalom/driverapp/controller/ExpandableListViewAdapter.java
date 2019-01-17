package com.example.shalom.driverapp.controller;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.shalom.driverapp.R;
import com.example.shalom.driverapp.model.backend.CurrentLocation;
import com.example.shalom.driverapp.model.backend.DBManagerFactory;
import com.example.shalom.driverapp.model.entities.Ride;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter implements Filterable {
    private Context context;
    private List<Ride> originalRideList;
    private List<Ride> rideList;
    private String driversid;
    private CurrentLocation location;

    public ExpandableListViewAdapter(Context context, List<Ride> rideList, String id) {
        this.context = context;
        this.rideList = rideList;
        this.driversid = id;
        this.originalRideList = rideList;
        location = new CurrentLocation(context);
        location.getLocation(context);

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
        ExpandableListViewAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ExpandableListViewAdapter.ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_group, null);
            viewHolder.passenger_location = (TextView) convertView.findViewById(R.id.passenger_location);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.distance);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ExpandableListViewAdapter.ViewHolder) (convertView.getTag());
        }
        viewHolder.passenger_location.setText(location.getPlace(ride.getStartLocation(), context));
        float distnace = ride.getStartLocation().distanceTo(location.locationA);
        viewHolder.distance.setText(String.valueOf(distnace));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Ride ride = (Ride) getChild(groupPosition, childPosition);
        final ExpandableListViewAdapter.ViewHolder2 viewHolder2;
        if (convertView == null) {
            viewHolder2 = new ExpandableListViewAdapter.ViewHolder2();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.ride_item, null);
            viewHolder2.full_name = (TextView) convertView.findViewById(R.id.Name);
            viewHolder2.phoneNumber = (TextView) convertView.findViewById(R.id.phone);
            viewHolder2.dest = (TextView) convertView.findViewById(R.id.destination);
            viewHolder2.callButton = (Button) convertView.findViewById(R.id.call_ButtonID);
            viewHolder2.messageButton = (Button) convertView.findViewById(R.id.message_ButtonID);
            viewHolder2.takeRideButton = (Button) convertView.findViewById(R.id.take_ButtonID);
            convertView.setTag(viewHolder2);
        } else {
            viewHolder2 = (ExpandableListViewAdapter.ViewHolder2) (convertView.getTag());
        }
        viewHolder2.dest.setText(location.getPlace(ride.getEndLocation(), context));
        viewHolder2.phoneNumber.setText(ride.getCelNumber());
        viewHolder2.full_name.setText(ride.getName());
        viewHolder2.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder2.callButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + ride.getCelNumber()));
                        context.startActivity(callIntent);
                    }
                });
            }
        });
        viewHolder2.messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder2.messageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent smsIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "smsto", ride.getCelNumber(), null));
                        context.startActivity(smsIntent);
                    }
                });
            }
        });
        viewHolder2.takeRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder2.takeRideButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            DBManagerFactory.getBL().rideIsBeingTreated(ride);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Date date = Calendar.getInstance().getTime();
                        ride.setStartTime(date);
                        ride.setDriverId(driversid);
                        new AsyncTask<Void,Void,Void>() {
                            @Override
                            protected Void doInBackground(Void... voids) {
                                return DBManagerFactory.getBL().updateRide(ride);
                            }
                        }.execute();
                    }
                });
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
                        float distance = (ride.getStartLocation().distanceTo(location.locationA));
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

    public class ViewHolder {
        TextView passenger_location;
        TextView distance;
    }

    public class ViewHolder2 {
        TextView full_name;
        TextView phoneNumber;
        TextView dest;
        Button callButton;
        Button messageButton;
        Button emailButton;
        Button takeRideButton;
    }
}

