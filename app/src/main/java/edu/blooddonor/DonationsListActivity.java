package edu.blooddonor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import edu.blooddonor.geoAPI.DistanceListActivity;
import edu.blooddonor.geoAPI.MapsActivity;
import edu.blooddonor.sqliteDB.DatabaseHelper;
import edu.blooddonor.model.Donation;

/**
 * Activity displaying list of Donations.
 *
 * This activity is used to display list of all user's Donations.
 * By long pressing an element of the list the user can choose from a pop up menu to delete an donation.
 *
 * @author madasionka
 *
 */

public class DonationsListActivity extends AppCompatActivity {

    private static final String LOG_TAG = "DonListAct: ";

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
        Intent activity;
        switch (item.getItemId()) {
            case edu.blooddonor.R.id.show_donations:
                activity = new Intent(getApplicationContext(), DonationsListActivity.class);
                startActivity(activity);
                return true;
            case edu.blooddonor.R.id.show_stations:
                setContentView(R.layout.activity_stations_list);
                StationsListActivity.presentAllStations(new DatabaseHelper(getApplicationContext()),
                        this, (ListView) findViewById(R.id.SLAL_listView));
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
                activity = new Intent(getApplicationContext(), AddDonationActivity.class);
                startActivity(activity);
                return true;
            case edu.blooddonor.R.id.manage_users:
                activity = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(activity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_donations_list);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        donations = db.getAllDonations();
        donations_string = donListToString(donations);
        Log.d(LOG_TAG,  "onResume Donations");
        presentAllDonations();
    }

    public void presentAllDonations() {
        myListView = (ListView) findViewById(R.id.DLAL_listView);
        Log.d(LOG_TAG,  "presenting all");
        listViewAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, donations_string);
        if (myListView != null) {
            myListView.setAdapter(listViewAdapter);
            registerForContextMenu(myListView);
        }
    }

    private ArrayList<String> donListToString(List<Donation> donations ) {
        ArrayList<String> out = new ArrayList<>();
        for (Donation d : donations) {
            out.add(d.toString());
        }
        return out;
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_donation_menu, menu);
    }

}
