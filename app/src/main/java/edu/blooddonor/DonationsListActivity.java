package edu.blooddonor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.Donation;

public class DonationsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(edu.blooddonor.R.layout.activity_donations_list);
        presentAllDonatons();
    }

//    public void presentAllDonatons(View v) {
    public void presentAllDonatons() {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Donation> donations = db.getAllDonations();

        ListView myListView = (ListView) findViewById(edu.blooddonor.R.id.DLAL_listView);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, donListToString(donations));
        if (myListView != null) {
            myListView.setAdapter(listViewAdapter);
        }
    }

    ArrayList<String> donListToString(List<Donation> donations ) {
        ArrayList<String> out = new ArrayList<>();
        for (Donation d : donations) {
            out.add(d.toString());
        }
        return out;
    }

    public void onClick_endThisActivity(View v) {
        finish();
    }
}
