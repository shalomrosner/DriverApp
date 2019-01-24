package com.example.shalom.driverapp.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.shalom.driverapp.R;
import com.example.shalom.driverapp.model.backend.DBManagerFactory;
import com.example.shalom.driverapp.model.datasource.NotifyDataChange;
import com.example.shalom.driverapp.model.entities.Driver;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String driverId;
    String driverEmail;
    Driver driver;
    String driverPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DBManagerFactory.getBL().notifyToDriverList(new NotifyDataChange<List<Driver>>() {
            @Override
            public void OnDataChanged(List<Driver> drivers) {
                Intent intent = getIntent();
                driverEmail = intent.getExtras().getString("driver email");
                driverPassword = intent.getExtras().getString("driver password");
                for (Driver driver1 : drivers) {

                    if (driver1.getEmail().matches(driverEmail) && driver1.getPassword().matches(driverPassword)) {
                        driverId = driver1.getId();
                        driver = driver1;
                    }
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        HomeFragment homeFragment = new HomeFragment();
        homeFragment.getIntance(driver);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            HomeFragment homeFragment = new HomeFragment();
            homeFragment.getIntance(driver);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
        } else if (id == R.id.nav_finished) {
            finishedRidesFragment finishedRidesFragment1 = new finishedRidesFragment();
            finishedRidesFragment1.getIntance(driverId);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, finishedRidesFragment1).commit();
        } else if (id == R.id.nav_available) {

            availableRidesFragment availableRidesFragment1 = new availableRidesFragment();
            availableRidesFragment1.getIntance(driverId);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, availableRidesFragment1).commit();

        } else if (id == R.id.nav_leave) {
            Toast.makeText(getApplicationContext(), "goodbye", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}





























