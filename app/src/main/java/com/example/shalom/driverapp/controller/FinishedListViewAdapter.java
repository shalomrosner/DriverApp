package com.example.shalom.driverapp.controller;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.Context;
import android.content.OperationApplicationException;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shalom.driverapp.R;
import com.example.shalom.driverapp.model.backend.CurrentLocation;
import com.example.shalom.driverapp.model.entities.Ride;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FinishedListViewAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<Ride> originalRideList;
    private List<Ride> rideList;
    Filter distanceFilter, priceFilter, dateFilter;
    private CurrentLocation location;

    public FinishedListViewAdapter(Context context, List<Ride> rideList) {
        this.context = context;
        this.rideList = rideList;
        this.originalRideList = rideList;
        location = new CurrentLocation(context);
        location.getLocation(context);

    }
    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return rideList.size();
    }

    @Override
    public Object getItem(int position) {
        return rideList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Ride ride = (Ride) getItem(position);
        FinishedListViewAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new FinishedListViewAdapter.ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.finish_ride_item, null);
            viewHolder.AddContacts = (FloatingActionButton) convertView.findViewById(R.id.AddContacts);
            viewHolder.timeEndOfRide = (TextView) convertView.findViewById(R.id.end_of_ride);
            viewHolder.Payment = (TextView) convertView.findViewById(R.id.paymentInput);
            convertView.setTag(viewHolder);

        } else {
            viewHolder = (FinishedListViewAdapter.ViewHolder) (convertView.getTag());
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        viewHolder.timeEndOfRide.setText(simpleDateFormat.format(ride.getEndTime()).toString());
        double ridepay = (ride.getStartLocation().distanceTo(ride.getEndLocation()) / 1000) * 5;
        viewHolder.Payment.setText(String.valueOf(ridepay)+ " $");
        viewHolder.AddContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
                int rawContactInsertIndex = ops.size();
                ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build());
                //INSERT NAME
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, ride.getName()) // Name of the person
                        .build());
                //INSERT PHONE MOBILE
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, ride.getCelNumber()) // Number of the person
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                        .build()); //
                //INSERT EMAIL
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.DATA, ride.getEmail())
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                        .build()); //
                //INSERT ADDRESS: FULL
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS, location.getPlace(ride.getStartLocation(), context))
                        .withValue(ContactsContract.CommonDataKinds.StructuredPostal.TYPE, ContactsContract.CommonDataKinds.StructuredPostal.TYPE_WORK)
                        .build());
                // SAVE CONTACT IN BCR Structure
                Uri newContactUri = null;
                //PUSH EVERYTHING TO CONTACTS
                try {
                    ContentProviderResult[] res = context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
                    if (res != null && res[0] != null) {
                        newContactUri = res[0].uri;
                    } else Toast.makeText(context, "Contact not added.", Toast.LENGTH_LONG).show();
                } catch (RemoteException e) {
                    // error
                    Toast.makeText(context, "Error (1) adding contact.", Toast.LENGTH_LONG).show();
                    newContactUri = null;
                } catch (OperationApplicationException e) {
                    // error
                    Toast.makeText(context, "Error (2) adding contact.", Toast.LENGTH_LONG).show();
                    newContactUri = null;
                }
                Toast.makeText(context, "Contact added to system contacts.", Toast.LENGTH_LONG).show();

                if (newContactUri == null) {
                    Toast.makeText(context, "Error creating contact", Toast.LENGTH_LONG);

                }
            }
        });

        return convertView;
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
    public Filter getDateFilter() {
        if(dateFilter == null)
            dateFilter = new Filter() {
                protected FilterResults performFiltering(CharSequence constraint) {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    FilterResults results = new FilterResults();
                    if ((constraint == null) || (constraint.length() == 0)) {
                        results.values = originalRideList;
                        results.count = originalRideList.size();
                    }
                    else {
                       //  List<Ride> newRideList = DBManagerFactory.getBL().getRidesByDate(constraint.toString());
                        List<Ride> newRideList = new ArrayList<Ride>();
                        for (Ride ride : originalRideList) {
                            String temp= simpleDateFormat.format(ride.getEndTime());
                            if ( temp.toString().equals(constraint.toString()))
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


            };
        return dateFilter;
    }
    public Filter getPaymentFilter() {
        if(priceFilter == null)
            priceFilter =  new Filter() {
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if ((constraint == null) || (constraint.length() == 0)) {
                    results.values = originalRideList;
                    results.count = originalRideList.size();
                }
                else {
                 //   List<Ride> newRideList = DBManagerFactory.getBL().getRidesByPrice(Float.valueOf(constraint.toString()));
                    List<Ride> newRideList = new ArrayList<Ride>();
                    for (Ride ride : originalRideList) {
                        double ridepay = (ride.getStartLocation().distanceTo(ride.getEndLocation()) / 1000) * 5;
                        if (ridepay <= Float.valueOf(constraint.toString()))
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


        };
        return priceFilter;

    }

    public void resetToPreList() {
        rideList = originalRideList;
    }

    @Override
    public Filter getFilter() {

        return getDateFilter();
    }

    private class ViewHolder {

        protected FloatingActionButton AddContacts;
        private TextView distance,timeEndOfRide, Payment;

    }
}
