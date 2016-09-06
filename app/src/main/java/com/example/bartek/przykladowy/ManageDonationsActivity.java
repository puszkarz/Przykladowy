package com.example.bartek.przykladowy;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.*;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.DatePicker;
import android.widget.PopupMenu;

import java.util.Calendar;
import java.util.List;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.Donation;

/**
 * Created by magda on 05.09.16.
 */
public class ManageDonationsActivity extends AppCompatActivity implements android.support.v7.widget.PopupMenu.OnMenuItemClickListener {
    Calendar calendar = Calendar.getInstance();
    int _year = 0;
    int _month = 0;
    int _day = 0;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_donations);


        Button dateButton = (Button)findViewById(R.id.MDL_but_pickDate);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ManageDonationsActivity.this,listener,calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
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


    public void onClick_showPopUpDonationsTypes(View v) {
        android.support.v7.widget.PopupMenu popup = new android.support.v7.widget.PopupMenu(this, v);
        popup.setOnMenuItemClickListener(ManageDonationsActivity.this);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.donationtype_menu, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
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
        TextView tv1 = (TextView) findViewById( R.id.MDL_f_userId);
        TextView tv2 = (TextView) findViewById( R.id.MDL_f_stationId);
        if (tv1 != null && tv2 != null &&  _year != 0 && _month != 0 && _day !=0) {
            CharSequence userId = tv1.getText();
            CharSequence stationId = tv2.getText();
            String dateInString = _year + "/" + _month + "/" + _day;
            db.insertDonation(new Donation(dateInString, "type", 0, 0, Integer.parseInt(userId.toString()), Integer.parseInt(stationId.toString())));

            tv1.setText(R.string.debugOK);
            tv2.setText(R.string.debugOK);
            logListDonations(db);

            _year = 0;
            _month = 0;
            _day = 0;
        }
    }

    // Writing Donations to Log
    private void logListDonations(DatabaseHelper db) {
        Log.d("Reading: ", "Reading all donations..");
        List<Donation> donations = db.getAllDonations();
        for (Donation cn : donations) {
            String log = "Id: " + cn.get_id() + " , St id: " + cn.get_station_id() + " , Us id: " + cn.get_user_id() + " , Date: " + cn.get_date();
            Log.d("Name: ", log);
        }
        Log.e("Donation Count", "donation count " + db.getDonationsCount());
    }

    public void onClick_deleteDonation(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( R.id.MDL_f_delStationId);
        if (tv1 != null) {
            CharSequence donation_id = tv1.getText();
            db.deleteDonation(Integer.parseInt(donation_id.toString()));

            tv1.setText(R.string.debugOK);
            logListDonations(db);
        }

    }

}
