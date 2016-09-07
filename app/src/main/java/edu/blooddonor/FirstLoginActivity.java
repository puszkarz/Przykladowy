package edu.blooddonor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import edu.blooddonor.geoAPI.DistanceListActivity;
import edu.blooddonor.geoAPI.MapsActivity;
import edu.blooddonor.sqliteDB.DatabaseHelper;
import edu.blooddonor.model.User;

public class FirstLoginActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    private String _bloodType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(edu.blooddonor.R.layout.activity_first_login);
    }

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
            case edu.blooddonor.R.id.manage_users:
                activity = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(activity);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick_showPopUpBloodTypes(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        popup.setOnMenuItemClickListener(FirstLoginActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(edu.blooddonor.R.menu.bloodtype_menu, popup.getMenu());
        popup.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case edu.blooddonor.R.id.APlus:
                Toast.makeText(getBaseContext(), "You selected A+", Toast.LENGTH_SHORT).show();
                _bloodType = "A+";
                return true;
            case edu.blooddonor.R.id.AMinus:
                Toast.makeText(getBaseContext(), "You selected A-", Toast.LENGTH_SHORT).show();
                _bloodType = "A-";
                return true;
            case edu.blooddonor.R.id.BPlus:
                Toast.makeText(getBaseContext(), "You selected B+", Toast.LENGTH_SHORT).show();
                _bloodType = "B+";
                return true;
            case edu.blooddonor.R.id.BMinus:
                Toast.makeText(getBaseContext(), "You selected B-", Toast.LENGTH_SHORT).show();
                _bloodType = "B-";
                return true;
            case edu.blooddonor.R.id.ABPlus:
                Toast.makeText(getBaseContext(), "You selected AB+", Toast.LENGTH_SHORT).show();
                _bloodType = "AB+";
                return true;
            case edu.blooddonor.R.id.ABMinus:
                Toast.makeText(getBaseContext(), "You selected AB-", Toast.LENGTH_SHORT).show();
                _bloodType = "AB-";
                return true;
            case edu.blooddonor.R.id.ZeroPlus:
                Toast.makeText(getBaseContext(), "You selected 0+", Toast.LENGTH_SHORT).show();
                _bloodType = "0+";
                return true;
            case edu.blooddonor.R.id.ZeroMinus:
                Toast.makeText(getBaseContext(), "You selected 0-", Toast.LENGTH_SHORT).show();
                _bloodType = "0-";
                return true;
            default:
                return false;
        }
    }

    public void onClick_addUser(View v){
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( edu.blooddonor.R.id.FLL_f_addNick);

        if (tv1 != null && !_bloodType.equalsIgnoreCase("")) {
            CharSequence nick = tv1.getText();
            db.insertUser(new User(nick.toString(), _bloodType));
            _bloodType = "";
            tv1.setText(edu.blooddonor.R.string.debugOK);

        }
        finish();
    }
}