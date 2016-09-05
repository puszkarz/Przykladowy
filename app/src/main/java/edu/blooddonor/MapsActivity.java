package edu.blooddonor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.URL;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

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
        setContentView(edu.blooddonor.R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(edu.blooddonor.R.id.map);
        mapFragment.getMapAsync(this);

    }

    /** Marking several addresses on map */
    void markAddressOnMap(String address) {
        URL urlQuery = GeocodingTask.genGeocodingQuery(address);
        Log.d("Geocoding API: ", "Trying URL: " + urlQuery.toString());
        new GeocodingTask(mMap).execute(urlQuery);
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
        Log.d("Location: ", "Polaczylem sie, ale pytam o permission");
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Location 0a: ", "Pragne zgody!");
            // Ostatni argument jest intem i jest dziwny, ale musi być, w przykładzie stała o nazwie MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 5);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 7);
        }

        startLocationUpdates();

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        Log.d("Location: ", "On connect: Uzyskalem lokalizację.");
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

}
