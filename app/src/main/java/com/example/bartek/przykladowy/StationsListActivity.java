package com.example.bartek.przykladowy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.Station;

public class StationsListActivity extends AppCompatActivity {

    List<Station> stations;
    ArrayList<String> stations_string = new ArrayList<>();
    ArrayAdapter<String> listViewAdapter;
    ListView myListView;

    static Station _pickedStation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations_list);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        stations = db.getAllStations();
        stations_string = statListToString(stations);
        presentAllStations();
    }

    public static Station get_pickedStation() {
        return _pickedStation;
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
                setContentView(R.layout.settings);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void presentAllStations() {
        myListView = (ListView) findViewById(R.id.SLAL_listView);
        listViewAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, stations_string);
        if (myListView != null) {
            myListView.setAdapter(listViewAdapter);
        }

        registerForContextMenu(myListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.pick_station_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.pick:
                _pickedStation = stations.get(info.position);
                Toast.makeText(getBaseContext(), "You selected" + _pickedStation.get_name(), Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onContextItemSelected(item);
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
