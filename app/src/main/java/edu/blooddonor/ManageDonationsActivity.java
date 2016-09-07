package edu.blooddonor;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.List;

import edu.blooddonor.geoAPI.DistanceListActivity;
import edu.blooddonor.geoAPI.MapsActivity;
import edu.blooddonor.sqliteDB.DatabaseHelper;
import edu.blooddonor.model.Donation;

public class ManageDonationsActivity extends AppCompatActivity implements android.support.v7.widget.PopupMenu.OnMenuItemClickListener {
    Calendar calendar = Calendar.getInstance();
    int _year = 0;
    int _month = 0;
    int _day = 0;

    String _donationsType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(edu.blooddonor.R.layout.activity_manage_donations);


        Button dateButton = (Button)findViewById(edu.blooddonor.R.id.MDL_but_pickDate);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ManageDonationsActivity.this,listener,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int day) {
            Toast.makeText(getBaseContext(), "Selected date:" + month + "/" + day + "/" + year, Toast.LENGTH_LONG).show();
            _year = year;
            _month = month;
            _day = day;
        }
    };

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


    public void onClick_showPopUpDonationsTypes(View v) {
        android.support.v7.widget.PopupMenu popup = new android.support.v7.widget.PopupMenu(this, v);
        popup.setOnMenuItemClickListener(ManageDonationsActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.donationtype_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.whole_blood:
                _donationsType = "whole_blood";
                Toast.makeText(getBaseContext(), "You selected Whole Blood", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.blood_plasma:
                _donationsType = "blood_plasma";
                Toast.makeText(getBaseContext(), "You selected Blood Plasma", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.blood_cells:
                _donationsType = "blood_cells";
                Toast.makeText(getBaseContext(), "You selected Blood Cells", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.red_cells:
                _donationsType = "red_cells";
                Toast.makeText(getBaseContext(), "You selected Red Cells", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.white_cells:
                _donationsType = "white_cells";
                Toast.makeText(getBaseContext(), "You selected White Cells", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick_addDonation(View v) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(_year, _month+1, _day);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 35);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(getApplicationContext(), NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 100, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv_volume = (TextView) findViewById( R.id.MDL_f_addVolume);
        TextView tv_stationId = (TextView) findViewById( R.id.MDL_f_stationId);
        if ((tv_volume != null) && (tv_stationId != null) && (_year != 0)
                && (_month != 0) && (_day != 0) && !_donationsType.equals("")) {
            CharSequence volume = tv_volume.getText();
            CharSequence stationId =  tv_stationId.getText();
            String dateInString = _year + "/" + _month + "/" + _day;

            int blood_volume =  computeVolume(_donationsType, Integer.parseInt(volume.toString()));
            Donation donation = new Donation(dateInString, _donationsType, Integer.parseInt(volume.toString()), blood_volume, 1, Integer.parseInt(stationId.toString()));
            db.insertDonation(donation);

            tv_volume.setText(R.string.debugOK);
            tv_stationId.setText(R.string.debugOK);
            logListDonations(db);

            _year = 0;
            _month = 0;
            _day = 0;
            _donationsType = "";

            logListDonations(db);
        }
    }


    //@TODO ogarnac jak sie przelicza
    private int computeVolume(String donationsType, int volume) {
        switch (donationsType) {
            case "whole_blood":
                return volume;
            case "blood_plasma":
                return volume;
            case "blood_cells":
                return volume;
            case "red_cells":
                return volume;
            case "white_cells":
                return volume;

        }
        return 0;
    }

    // Writing Donations to Log
    private void logListDonations(DatabaseHelper db) {
        Log.d("Reading: ", "Reading all donations..");
        List<Donation> donations = db.getAllDonations();
        for (Donation cn : donations) {
            String log = "Id: " + cn.get_id() + " , St id: " + cn.get_station_id() + " , Us id: " + cn.get_user_id() + " , Date: " + cn.get_date() + " , Volume: " + cn.get_volume();
            Log.d("Name: ", log);
        }
        Log.e("Donation Count", "donation count " + db.getDonationsCount());
    }

    public void onClick_deleteDonation(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( edu.blooddonor.R.id.MDL_f_delStationId);
        if (tv1 != null) {
            CharSequence donation_id = tv1.getText();
            db.deleteDonation(Integer.parseInt(donation_id.toString()));

            tv1.setText(edu.blooddonor.R.string.debugOK);
            logListDonations(db);
        }

    }

}
