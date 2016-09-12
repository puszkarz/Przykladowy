package edu.blooddonor;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import edu.blooddonor.geoAPI.DistanceListActivity;
import edu.blooddonor.geoAPI.MapsActivity;
import edu.blooddonor.sqliteDB.DatabaseHelper;
import edu.blooddonor.model.User;

/**
 * Activity displaying the main screen of the application.
 *
 * This activity is used to display a welcome message, total number of ml of blood donated so far,
 * progress bar which displays the ratio of donated blood to the amount needed to gain another badge.
 *
 * @author madasionka puszkarz
 *
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        printWelcomeOrLogin();
        printProgress();
    }

    private void printProgress(){
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.ML_progressBar);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        double progress = db.getBloodVolumeSum();
        double goal;
        if (progress < 6000)
            goal = 6000;
        else if (progress < 12000)
            goal = 12000;
        else
            goal = 18000;
        int percentage = (int) (progress/goal*100);
        if (progressBar != null) {
            progressBar.setProgress(percentage);
            progressBar.setScaleY(3f);
            progressBar.getProgressDrawable().setColorFilter(
                    Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);
        }
        TextView textViewPercent = (TextView) findViewById(R.id.ML_txt_youAchieved);
        String achievementTxt;
        if (progress< 18000)
            achievementTxt = "You achieved " + percentage + "% towards your next badge!";
        else
            achievementTxt = "You collected all of the badges!";
        if (textViewPercent != null)
            textViewPercent.setText(achievementTxt);
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
            if (textView != null) {
                textView.invalidate();
            }
            TextView textViewNum = (TextView) findViewById(R.id.ML_txt_youDonatedNum);
            if (textViewNum != null) {
                textViewNum.invalidate();
            }
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
}
