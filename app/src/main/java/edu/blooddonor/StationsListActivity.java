package edu.blooddonor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import edu.blooddonor.sqliteDB.DatabaseHelper;
import edu.blooddonor.model.Station;

public class StationsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(edu.blooddonor.R.layout.activity_stations_list);
        presentAllStations();
    }

    public void presentAllStations() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Station> stations = db.getAllStations();

        ListView myListView = (ListView) findViewById(edu.blooddonor.R.id.SLAL_listView);
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
