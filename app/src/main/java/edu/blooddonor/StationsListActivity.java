package edu.blooddonor;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.blooddonor.sqliteDB.DatabaseHelper;
import edu.blooddonor.model.Station;

/**
 * Activity displaying the list of Stations.
 *
 * Activity is used to pick a Station from the list whilst making a new donation.
 * When the Station is picked a static field _chosenStation of AddDonationActivity class is set.
 *
 * @author madasionka
 *
 */

public class StationsListActivity extends AppCompatActivity {

    List<Station> stations;
    ArrayList<String> stations_string = new ArrayList<>();
    ArrayAdapter<String> listViewAdapter;
    ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stations_list);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        stations = db.getAllStations();
        stations_string = statListToString(stations);
        presentAllStations();
    }

    public void presentAllStations() {
        myListView = (ListView) findViewById(R.id.SLAL_listView);
        listViewAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, stations_string);
        if (myListView != null) {
            myListView.setAdapter(listViewAdapter);
        }
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Station _pickedStation = stations.get(position);
                Toast.makeText(getBaseContext(), "You selected " + _pickedStation.get_name(), Toast.LENGTH_SHORT).show();
                AddDonationActivity.set_chosenStation(_pickedStation);
                finish();
            }
        });
    }

    private static ArrayList<String> statListToString(List<Station> stations) {
        ArrayList<String> out = new ArrayList<>();
        for (Station st : stations) {
            out.add(st.toString());
        }
        return out;
    }

    public static void presentAllStations(DatabaseHelper db, Activity activity, ListView myListView) {
        List<Station> stations = db.getAllStations();
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(activity,
                android.R.layout.simple_list_item_1, statListToString(stations));
        if (myListView != null) {
            myListView.setAdapter(listViewAdapter);
        }
    }

}
