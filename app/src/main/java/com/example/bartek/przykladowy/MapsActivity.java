package com.example.bartek.przykladowy;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    static String geoUrl = "https://maps.googleapis.com/maps/api/geocode/json?";
    static String serverKey = "AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI";
//        pomocne linki
//        http://stackoverflow.com/questions/29724192/using-json-for-android-maps-api-markers-not-showing-up
//        https://maps.googleapis.com/maps/api/geocode/json?address=Winnetka&key=AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI
//        https://maps.googleapis.com/maps/api/geocode/json?address=Czerwonego+Krzyza+5,+Wroclaw&region=pl&key=AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI

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
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    /** Marking several addresses on map */
    void markAddressOnMap(String address) {
        String geoQuery = genGeoLocQuery(address);
        try {
            Log.d("Trying URL: ", geoQuery);
            URL urlQuery = new URL(geoQuery);
            new MarkerTask().execute(urlQuery);
        } catch (MalformedURLException e) {
            e.printStackTrace(); //TODO: exception handling
        }
    }

    /** Generation of geoloc query to Google API basing on address */
    String genGeoLocQuery(String address) {
        String addressEnc = null;
        try {
            Log.d("GeoLoc: ", "Query address " + address);
            addressEnc = URLEncoder.encode(address, "UTF-8"); //Zamiast "UTF-8" mogłoby być java.nio.charset.StandardCharsets.UTF_8.toString()
            Log.d("GeoLoc: ", "Encoded address: " + addressEnc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); //TODO: exception handling
        }
        return geoUrl + "address=" + addressEnc + "&key=" + serverKey;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Marking one address on map
        String address = "Czerwonego Krzyza 5/9, 50-345 Wroclaw";
        markAddressOnMap(address);
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
        Log.d("Location 0: ", "Polaczylem sie, ale pytam o permission");
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Location 0a: ", "Pragne zgody!");
            // Ostatni argument jest intem i jest dziwna, ale musi być MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 7);
        }
//        if (mRequestingLocationUpdates) {
        startLocationUpdates();
//        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        Log.d("Location 1: ", "On connect: Uzyskalem lokalizację.");
        addPosMarker();
    }

    protected void startLocationUpdates() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Location 0a: ", "Pragne zgody w startLocationUpdates!");
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation  = location;
        Log.d("Location:", "onLocationChanged.");
        addPosMarker();
    }

    public void addPosMarker() {
        if (mLastLocation != null) {
            Log.d("Location: ", "Nie jest nullem! :)");
            LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng).zoom(13).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                    .title("You are here!")
                    .position(latLng));
        } else {
            Log.d("Location: ", "Location jest nullem :(");
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Location: ", "onConnectionSuspended :(");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Location: ", "onConnectionFailed :(");
    }

    private class MarkerTask extends AsyncTask<URL, Void, String> {

        private static final String LOG_TAG = "Log MarkerTask";

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(URL... urls) {
            HttpURLConnection conn = null;
            final StringBuilder json = new StringBuilder();
            try {
                // Connect to the web service
                // URL url = new URL(urls);
                URL url = urls[0];
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                // Read the JSON data into the StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    json.append(buff, 0, read);
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error connecting to service", e);
                // throw new IOException("Error connecting to service", e); //uncaught
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
            return json.toString();
        }

        // Executed after the complete execution of doInBackground() method
        @Override
        protected void onPostExecute(String json) {
            try {
                JSONObject jsonObj = new JSONObject(json);
                JSONObject res = jsonObj.getJSONArray("results").getJSONObject(0);
                JSONObject loc = res.getJSONObject("geometry").getJSONObject("location");
                LatLng latLng = new LatLng(loc.getDouble("lat"), loc.getDouble("lng"));
                String formattedAddress = res.getString("formatted_address");
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(13).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title(formattedAddress)
                        .position(latLng));
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error processing JSON", e);
            }
        }
    }
}
