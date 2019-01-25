package com.example.shalom.driverapp.controller;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shalom.driverapp.R;
import com.example.shalom.driverapp.model.backend.DBManagerFactory;
import com.example.shalom.driverapp.model.datasource.NotifyDataChange;
import com.example.shalom.driverapp.model.entities.Ride;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class finishedRidesFragment extends Fragment {
    FinishedListViewAdapter rideListAdapter;
    View view;
    EditText filter;
    TextView date_filter;
    Date date;
    String driversid;
    Button dateButton;
    private ListView lv;
    LinearLayout linearLayoutdate;
    final Context context = this.getContext();
    public static List<Ride> ridelist = new ArrayList<>();
    public void getIntance(String driversid) {
        this.driversid = driversid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_finished_rides, container, false);
        lv = (ListView) view.findViewById(R.id.ride_finished_lv);
        filter = (EditText) view.findViewById(R.id.finished_filter);
        date_filter  = (TextView) view.findViewById(R.id.date_filter);
        lv.setTextFilterEnabled(true);
        filter.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count < before) {
                    rideListAdapter.resetToPreList();
                }

                rideListAdapter.getDistanceFilter().filter(s.toString());

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final Context context = this.getContext();
        linearLayoutdate =view.findViewById(R.id.layout_date_filter);
        dateButton = view.findViewById(R.id.Date_picker);
        ridelist = DBManagerFactory.getBL().getDriversFinishedRides(driversid);
        DBManagerFactory.getBL().notifyToRideList(new NotifyDataChange<List<Ride>>() {
            @Override
            public void OnDataChanged(List<Ride> obj) {
                if (ridelist.size() != 0) {
                    rideListAdapter = new FinishedListViewAdapter(context,ridelist);
                    lv.setAdapter(rideListAdapter);


                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return view;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_finished_distance) {
            filter.setVisibility(View.VISIBLE);
            linearLayoutdate.setVisibility(View.INVISIBLE);
            filter.setInputType(InputType.TYPE_CLASS_NUMBER);
            filter.setText("");
            filter.setHint(filter.getHint());
            filter.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count < before) {
                        rideListAdapter.resetToPreList();
                    }

                    rideListAdapter.getDistanceFilter().filter(s.toString());

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            return true;
        } else if (id == R.id.action_price) {
            filter.setVisibility(View.VISIBLE);
            linearLayoutdate.setVisibility(View.INVISIBLE);
            filter.setInputType(InputType.TYPE_CLASS_NUMBER);
            filter.setText("");
            filter.setHint(filter.getHint());
            filter.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count < before) {
                        rideListAdapter.resetToPreList();
                    }

                    rideListAdapter.getPaymentFilter().filter(s.toString());

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            return true;
        } else if (id == R.id.action_date) {
            filter.setVisibility(View.INVISIBLE);
            linearLayoutdate.setVisibility(View.VISIBLE);
            dateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            Calendar cal = Calendar.getInstance();
                            cal.set(Calendar.YEAR, year);
                            cal.set(Calendar.MONTH, month);
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            date = cal.getTime();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            date_filter.setText(simpleDateFormat.format(date).toString());
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();

                }
            });


            date_filter.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count < before) {
                        rideListAdapter.resetToPreList();
                    }

                    rideListAdapter.getDateFilter().filter(s.toString());

                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.finished, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }




}
