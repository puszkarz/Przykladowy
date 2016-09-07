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

import edu.blooddonor.geoAPI.DistanceListActivity;
import edu.blooddonor.geoAPI.MapsActivity;
import edu.blooddonor.sqliteDB.DatabaseHelper;
import edu.blooddonor.model.Station;
import edu.blooddonor.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        printWelcomeOrLogin();

    }

    @Override
    protected void onResume() {
        super.onResume();
        printWelcomeOrLogin();
    }

    private void printWelcomeOrLogin(){
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        int noUser = db.getUsersCount();
        setContentView(R.layout.activity_main);
        if (noUser == 0 ) {
            Intent firstActivity = new Intent(getApplicationContext(), FirstLoginActivity.class);
            startActivity(firstActivity);
        }
        TextView textView = (TextView) findViewById(R.id.ML_txt_mainWelcome);
        User user = db.getUser(1);
        if (textView != null && user != null)
            textView.setText("Welcome " + user.get_nick() + "!");
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
        Intent activity;
        switch (item.getItemId()) {
            case edu.blooddonor.R.id.show_donations:
                activity = new Intent(getApplicationContext(), DonationsListActivity.class);
                startActivity(activity);
                return true;
            case edu.blooddonor.R.id.show_stations:
                activity = new Intent(getApplicationContext(), StationsListActivity.class);
                startActivity(activity);
                return true;
            case R.id.show_map:
                activity = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(activity);
                return true;
            case R.id.show_distance:
                activity = new Intent(getApplicationContext(), DistanceListActivity.class);
                startActivity(activity);
                return true;
            case edu.blooddonor.R.id.manage_donations:
                activity = new Intent(getApplicationContext(), ManageDonationsActivity.class);
                startActivity(activity);
                return true;
            case edu.blooddonor.R.id.manage_stations: //TODO: chyba wywalić
                setContentView(edu.blooddonor.R.layout.activity_manage_stations);
                return true;
            case edu.blooddonor.R.id.manage_users: //TODO: dać jako settings
                setContentView(edu.blooddonor.R.layout.activity_manage_users);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick_backToMain(View v) {
        setContentView(edu.blooddonor.R.layout.activity_main);
    }

    public void onClick_addStation(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( edu.blooddonor.R.id.MSL_f_addStationName);

        if (tv1 != null) {
            CharSequence name = tv1.getText();
            db.insertStation(new Station(name.toString(), "adres", 0.0, 0.0));

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
            String log = "Id: " + cn.get_id() + " ,Nick: " + cn.get_nick() + " ,Blood type:" + cn.get_bloodType();
            Log.d("Name: ", log);
        }
    }


}
