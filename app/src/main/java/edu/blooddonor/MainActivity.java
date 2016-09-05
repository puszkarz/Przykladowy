package edu.blooddonor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.Station;
import info.sqlite.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        int noUser = db.getUsersCount();
        logListUsers(db);
        if (noUser > 0)
            setContentView(edu.blooddonor.R.layout.activity_main);
        else
        {
            Intent firstActivity = new Intent(getApplicationContext(), FirstLoginActivity.class);
            startActivity(firstActivity);
        }

        logListUsers(db);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(edu.blooddonor.R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case edu.blooddonor.R.id.show_donations:
                Intent donationsActivity = new Intent(getApplicationContext(), DonationsListActivity.class);
                startActivity(donationsActivity);
                return true;
            case edu.blooddonor.R.id.show_stations:
                Intent stationsActivity = new Intent(getApplicationContext(), StationsListActivity.class);
                startActivity(stationsActivity);
                return true;
            case edu.blooddonor.R.id.manage_donations:
                Intent manageDonationsActivity = new Intent(getApplicationContext(), ManageDonationsActivity.class);
                startActivity(manageDonationsActivity);
                return true;
            case edu.blooddonor.R.id.manage_stations:
                setContentView(edu.blooddonor.R.layout.activity_manage_stations);
                return true;
            case edu.blooddonor.R.id.manage_users:
                setContentView(edu.blooddonor.R.layout.activity_manage_users);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick_showDonations(View v) {
        Intent donationsActivity = new Intent(getApplicationContext(), DonationsListActivity.class);
        startActivity(donationsActivity);
    }

    public void onClick_showStations(View v) {
        Intent stationsActivity = new Intent(getApplicationContext(), StationsListActivity.class);
        startActivity(stationsActivity );
    }

    public void onClick_showMaps(View v) {
        Intent mapsActivity = new Intent(getApplicationContext(), MapsActivity.class);
        startActivity(mapsActivity);
    }

    public void onClick_showManageStations(View v) {
        setContentView(edu.blooddonor.R.layout.activity_manage_stations);
    }

    public void onClick_showManageUsers(View v) {
        setContentView(edu.blooddonor.R.layout.activity_manage_users);
    }

    public void onClick_showManageDonations(View v) {
        setContentView(edu.blooddonor.R.layout.activity_manage_donations);
    }

    public void onClick_backToMain(View v) {
        setContentView(edu.blooddonor.R.layout.activity_main);
    }

    public void onClick_addStation(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( edu.blooddonor.R.id.MSL_f_addStationName);

        if (tv1 != null) {
            CharSequence name = tv1.getText();
            db.insertStation(new Station(name.toString(), "", 0, 0));

            tv1.setText(edu.blooddonor.R.string.debugOK);
            logListStations(db);
        }
    }

    public void onClick_deleteStation(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( edu.blooddonor.R.id.MDL_f_delStationId);

        if (tv1 != null) {
            CharSequence station_id = tv1.getText();
            db.deleteStation(Integer.parseInt(station_id.toString()));

            tv1.setText(edu.blooddonor.R.string.debugOK);
            logListStations(db);
        }
    }

    public void onClick_addUser(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( edu.blooddonor.R.id.MSL_f_addStationName);
        TextView tv2 = (TextView) findViewById( edu.blooddonor.R.id.typefield );
        if (tv1 != null && tv2 != null) {
            CharSequence nick = tv1.getText();
            CharSequence type = tv2.getText();
            db.insertUser(new User(nick.toString(), type.toString()));

            tv1.setText(edu.blooddonor.R.string.debugOK);
            tv2.setText(edu.blooddonor.R.string.debugOK);
            logListUsers(db);
        }
    }

    public void onClick_deleteUser(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( edu.blooddonor.R.id.MDL_f_delStationId);
        if (tv1 != null) {
            CharSequence users_id = tv1.getText();
            db.deleteUser(Integer.parseInt(users_id.toString()));

            tv1.setText(edu.blooddonor.R.string.debugOK);
            logListUsers(db);
        }
    }


    // Writing Stations to Log
    private void logListStations(DatabaseHelper db) {
        Log.d("Reading: ", "Reading all stations..");
        List<Station> stations = db.getAllStations();
        for (Station cn : stations) {
            String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name();
            Log.d("Name: ", log);
        }
    }

    // Writing Users to Log
    private void logListUsers(DatabaseHelper db) {
        Log.d("Reading: ", "Reading all users..");
        List<User> users = db.getAllUsers();
        for (User cn : users) {
            String log = "Id: " + cn.get_id() + " ,Nick: " + cn.get_nick() + "Blood type:" + cn.get_bloodTypeID();
            Log.d("Name: ", log);
        }
    }


}
