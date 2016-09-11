package edu.blooddonor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.TextView;
import java.util.List;
import android.util.Log;

import edu.blooddonor.geoAPI.DistanceListActivity;
import edu.blooddonor.geoAPI.MapsActivity;
import edu.blooddonor.model.User;
import edu.blooddonor.sqliteDB.DatabaseHelper;

/**
 * Activity updating user's information.
 *
 * Activity is used to update user's nick and bloodtype.
 * The user can also delete his account and by doing so all of his donations.
 *
 * @author madasionka
 *
 */

public class SettingsActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
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

    public void onClick_UpdateNick(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv_nick = (TextView) findViewById( R.id.MUL_updateNick);
        if (tv_nick != null) {
            CharSequence nick = tv_nick.getText();
            User user = db.getUser(1);
            user.set_nick(nick.toString());
            db.updateUser(user);

            tv_nick.setText(R.string.debugOK);
            logListUsers(db);
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

    public void onClick_showUpdatePopUpBloodTypes(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(SettingsActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.bloodtype_menu, popup.getMenu());
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.APlus:
                Toast.makeText(getBaseContext(), "You selected A+.", Toast.LENGTH_SHORT).show();
                updateBloodType("A+");
                return true;
            case R.id.AMinus:
                Toast.makeText(getBaseContext(), "You selected A-.", Toast.LENGTH_SHORT).show();
                updateBloodType("A-");
                return true;
            case R.id.BPlus:
                Toast.makeText(getBaseContext(), "You selected B+.", Toast.LENGTH_SHORT).show();
                updateBloodType("B+");
                return true;
            case R.id.BMinus:
                Toast.makeText(getBaseContext(), "You selected B-.", Toast.LENGTH_SHORT).show();
                updateBloodType("B-");
                return true;
            case R.id.ABPlus:
                Toast.makeText(getBaseContext(), "You selected AB+.", Toast.LENGTH_SHORT).show();
                updateBloodType("AB+");
                return true;
            case R.id.ABMinus:
                Toast.makeText(getBaseContext(), "You selected AB-.", Toast.LENGTH_SHORT).show();
                updateBloodType("AB-");
                return true;
            case R.id.ZeroPlus:
                Toast.makeText(getBaseContext(), "You selected 0+.", Toast.LENGTH_SHORT).show();
                updateBloodType("0+");
                return true;
            case R.id.ZeroMinus:
                Toast.makeText(getBaseContext(), "You selected 0-.", Toast.LENGTH_SHORT).show();
                updateBloodType("0-");
                return true;
            default:
                return false;
        }
    }

    private void updateBloodType(String s) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        User user = db.getUser(1);
        user.set_bloodType(s);
        db.updateUser(user);
    }

    public void onClick_deleteUser(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        db.deleteUser(1);
        db.deleteAllDonations();
        logListUsers(db);
        finish();
    }
}
