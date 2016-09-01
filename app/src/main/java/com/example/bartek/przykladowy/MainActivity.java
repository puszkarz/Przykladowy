package com.example.bartek.przykladowy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.Donation;
import info.sqlite.model.Station;
import info.sqlite.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClick_showDonations(View v) {
        setContentView(R.layout.activity_donations_list);
    }

    public void onClick_showManageStations(View v) {
        setContentView(R.layout.activity_manage_stations);
    }

    public void onClick_showManageUsers(View v) {
        setContentView(R.layout.activity_manage_users);
    }

    public void onClick_showManageDonations(View v) {
        setContentView(R.layout.activity_manage_donations);
    }

    public void onClick_backToMain(View v) {
        setContentView(R.layout.activity_main);
    }

    public void onClick_addStation(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( R.id.namefield );
        CharSequence name = tv1.getText();

        db.createStation(new Station(name.toString(), "", 0, 0));

        tv1.setText("Udalo sie");

        Log.d("Reading: ", "Reading all stations..");
        List<Station> stations = db.getAllStations();

        for (Station cn : stations) {
            String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

    }

    public void onClick_deleteStation(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( R.id.idfield );
        CharSequence station_id = tv1.getText();

        db.deleteStation(Integer.parseInt(station_id.toString()));

        tv1.setText("Udalo sie");

        Log.d("Reading: ", "Reading all stations..");
        List<Station> stations = db.getAllStations();

        for (Station cn : stations) {
            String log = "Id: " + cn.get_id() + " ,Name: " + cn.get_name();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

    }

    public void onClick_addUser(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( R.id.namefield );
        CharSequence nick = tv1.getText();

        TextView tv2 = (TextView) findViewById( R.id.typefield );
        CharSequence type = tv2.getText();

        db.createUser(new User(nick.toString(), type.toString()));

        tv1.setText("Udalo sie 1");
        tv2.setText("Udalo sie 2");

        Log.d("Reading: ", "Reading all users..");
        List<User> users = db.getAllUsers();

        for (User cn : users) {
            String log = "Id: " + cn.get_id() + " ,Nick: " + cn.get_nick();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

    }

    public void onClick_deleteUser(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( R.id.idfield );
        CharSequence users_id = tv1.getText();

        db.deleteUser(Integer.parseInt(users_id.toString()));

        tv1.setText("Udalo sie");

        Log.d("Reading: ", "Reading all users..");
        List<User> users = db.getAllUsers();

        for (User cn : users) {
            String log = "Id: " + cn.get_id() + " ,Nick: " + cn.get_nick();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

    }

    public void onClick_addDonation(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( R.id.useridfield );
        CharSequence userid = tv1.getText();

        TextView tv2 = (TextView) findViewById( R.id.stationidfield );
        CharSequence stationid = tv2.getText();

        db.createDonation(new Donation("a", "a", 0, Integer.parseInt(userid.toString()), Integer.parseInt(stationid.toString())));

        tv1.setText("Udalo sie 1");
        tv2.setText("Udalo sie 2");

        Log.d("Reading: ", "Reading all donations..");
        List<Donation> donations = db.getAllDonations();

        for (Donation cn : donations) {
            String log = "Id: " + cn.get_id() + " , St id: " + cn.get_station_id() + " , Us id: " + cn.get_user_id();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

        Log.e("Donation Count", "donation count " + db.getDonationsCount());

    }

    public void onClick_deleteDonation(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( R.id.idfield );
        CharSequence donation_id = tv1.getText();

        db.deleteDonation(Integer.parseInt(donation_id.toString()));

        tv1.setText("Udalo sie");

        Log.d("Reading: ", "Reading all donations..");
        List<Donation> donations = db.getAllDonations();

        for (Donation cn : donations) {
            String log = "Id: " + cn.get_id() + " , St id: " + cn.get_station_id() + " , Us id: " + cn.get_user_id();
            // Writing Contacts to log
            Log.d("Name: ", log);
        }

    }
}
