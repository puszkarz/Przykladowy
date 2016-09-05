package com.example.bartek.przykladowy;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.List;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.Donation;

/**
 * Created by magda on 05.09.16.
 */
public class ManageDonationsActivity extends AppCompatActivity{
    Calendar calendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getBaseContext(), "Selected date:" + monthOfYear + "/" + dayOfMonth + "/" + year, Toast.LENGTH_LONG).show();
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



    public void onClick_addDonation(View v) {
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        TextView tv1 = (TextView) findViewById( R.id.MDL_f_userId);
        TextView tv2 = (TextView) findViewById( R.id.MDL_f_stationId);
        if (tv1 != null && tv2 != null) {
            CharSequence userId = tv1.getText();
            CharSequence stationId = tv2.getText();
            db.insertDonation(new Donation("a", "a", 0, Integer.parseInt(userId.toString()), Integer.parseInt(stationId.toString())));

            tv1.setText(R.string.debugOK);
            tv2.setText(R.string.debugOK);
            logListDonations(db);
        }
    }

    // Writing Donations to Log
    private void logListDonations(DatabaseHelper db) {
        Log.d("Reading: ", "Reading all donations..");
        List<Donation> donations = db.getAllDonations();
        for (Donation cn : donations) {
            String log = "Id: " + cn.get_id() + " , St id: " + cn.get_station_id() + " , Us id: " + cn.get_user_id();
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

    public void onClick_endThisActivity(View v) {
        finish();
    }

}
