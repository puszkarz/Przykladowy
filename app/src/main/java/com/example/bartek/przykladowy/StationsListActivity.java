package com.example.bartek.przykladowy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.Station;

public class StationsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations_list);
        presentAllStations();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_donations:
                Intent donationsActivity = new Intent(getApplicationContext(), DonationsListActivity.class);
                startActivity(donationsActivity);
                return true;
            case R.id.show_stations:
                Intent stationsActivity = new Intent(getApplicationContext(), StationsListActivity.class);
                startActivity(stationsActivity);
                return true;
            case R.id.manage_donations:
                Intent manageDonationsActivity = new Intent(getApplicationContext(), ManageDonationsActivity.class);
                startActivity(manageDonationsActivity);
                return true;
            case R.id.manage_stations:
                setContentView(R.layout.activity_manage_stations);
                return true;
            case R.id.manage_users:
                setContentView(R.layout.activity_manage_users);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void presentAllStations() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Station> stations = db.getAllStations();

        ListView myListView = (ListView) findViewById(R.id.SLAL_listView);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, statListToString(stations));
        if (myListView != null) {
            myListView.setAdapter(listViewAdapter);
        }
    }

    ArrayList<String> statListToString(List<Station> stations) {
        ArrayList<String> out = new ArrayList<>();
        for (Station st : stations) {
            out.add(st.toString());
        }
        return out;
    }

    public void onClick_endThisActivity(View v) {
        finish();
    }
}
