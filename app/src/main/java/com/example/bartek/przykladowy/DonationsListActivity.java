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

import java.util.ArrayList;
import java.util.List;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.Donation;

public class DonationsListActivity extends AppCompatActivity {

    List<Donation> donations;
    ArrayList<String> donations_string = new ArrayList<>();
    ArrayAdapter<String> listViewAdapter;
    ListView myListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations_list);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        donations = db.getAllDonations();
        donations_string = donListToString(donations);
        presentAllDonations();
    }

//    public void presentAllDonations(View v) {
    public void presentAllDonations() {
        myListView = (ListView) findViewById(R.id.DLAL_listView);
        listViewAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, donations_string);
        if (myListView != null) {
            myListView.setAdapter(listViewAdapter);
        }

        registerForContextMenu(myListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_donation_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()){
            case R.id.delete:
                Donation donation = donations.get(info.position);
                donations.remove(info.position);
                db.deleteDonation(donation.get_id());
                donations_string.remove(info.position);
                listViewAdapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
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
