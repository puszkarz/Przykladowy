package edu.blooddonor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import edu.blooddonor.geoAPI.DistanceListActivity;
import edu.blooddonor.geoAPI.MapsActivity;
import edu.blooddonor.sqliteDB.DatabaseHelper;
import edu.blooddonor.model.User;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        printWelcomeOrLogin();

    }

    @Override
    protected void onResume() {
        super.onResume();
        printWelcomeOrLogin();
    }

    private void printWelcomeOrLogin(){
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        int noUser = db.getUsersCount();
        if (noUser == 0 ) {
            Intent firstActivity = new Intent(getApplicationContext(), FirstLoginActivity.class);
            startActivity(firstActivity);
        } else {
            setContentView(R.layout.activity_main);
            User user = db.getUser(1);
            TextView textView = (TextView) findViewById(R.id.ML_txt_mainNick);
            TextView textViewNum = (TextView) findViewById(R.id.ML_txt_youDonatedNum);
            if (textView != null && textViewNum != null && user != null) {
                String nick = user.get_nick();
                if (nick != null) {
                    String welcomeTxt = "Welcome " + nick + "!";
                    textView.setText(welcomeTxt);
                    String donatedBloodNumTxt = Double.toString(db.getBloodVolumeSum()) + " ml.";
                    textViewNum.setText(donatedBloodNumTxt);
                }
            }
        }
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
}
