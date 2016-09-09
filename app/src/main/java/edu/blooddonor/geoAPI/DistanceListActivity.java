package edu.blooddonor.geoAPI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import edu.blooddonor.DonationsListActivity;
import edu.blooddonor.AddDonationActivity;
import edu.blooddonor.R;
import edu.blooddonor.SettingsActivity;
import edu.blooddonor.StationsListActivity;
import edu.blooddonor.model.Station;
import edu.blooddonor.sqliteDB.DatabaseHelper;

/**
 * Activity displaying list of Stations with relative distances (wrt. current position).
 *
 * This activity is used to display list of Stations with relative distances
 * wrt. current position of the app user. Current location is determined using
 * Google LocationServices API.
 *
 * @author puszkarz
 *
 */

public class DistanceListActivity extends AppCompatActivity implements
        LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String LOG_TAG = "DistActivity: ";

    private GoogleApiClient mGoogleApiClient;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        super.onCreate(savedInstanceState);
        setContentView(edu.blooddonor.R.layout.activity_distance_list);
        db = new DatabaseHelper(getApplicationContext());
        checkAndUpdateStations(db);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLastLocation != null) {
                onLocationChanged(mLastLocation);
            }
        }
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

    private static void checkAndUpdateStations(DatabaseHelper db) {
        List<Station> stations =  db.getAllStations();
        for (Station st : stations) {
            if (!st.isWellDefined()) {
                Log.d(LOG_TAG, st.toString());
                new UpdateStationGeoTask(st, db).execute();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        checkAndUpdateStations(db);
        Log.d(LOG_TAG, "Location has changed, adding new marker.");
        if (location != null) {
            ListView targetListView = (ListView) findViewById(R.id.DLAL_listView);
            new DistanceMatrixTask(targetListView, getApplicationContext())
                    .execute(new LatLng(location.getLatitude(), location.getLongitude()));
        } else {
            Log.d(LOG_TAG, "Location jest nullem :(");
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 7);
        }
        startLocationUpdates();
        // performing onLocationChanged task with last (old) location
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            onLocationChanged(mLastLocation);
        }
    }

    private void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(LOG_TAG, "Pragne zgody w startLocationUpdates!");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(LOG_TAG, "onConnectionSuspended :(");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_TAG, "onConnectionFailed :(");
    }
}
