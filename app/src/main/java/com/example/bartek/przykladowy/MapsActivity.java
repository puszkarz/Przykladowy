package com.example.bartek.przykladowy;

import android.os.AsyncTask;
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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    static String geoUrl = "https://maps.googleapis.com/maps/api/geocode/json?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        String serverKey = "AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI";
//        String address = "Czerwonego Krzyża 5/9, 50-345 Wrocław";
        String address = "Czerwonego Krzyza 5/9, 50-345 Wroclaw";
        String addressEnc = null;
        try {
            Log.d("Trying 1: ", address);
            Log.d("Trying 2: ", java.nio.charset.StandardCharsets.UTF_8.toString());
            addressEnc = URLEncoder.encode(address, "UTF-8" );
            Log.d("Trying 3: ", "Reading all donations..");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String geoQuery = geoUrl + "address=" +  addressEnc + "&key=" + serverKey;
//      URL  urlQuery = null;
        try {
            Log.d("Trying URL: ", geoQuery);
            URL urlQuery = new URL(geoQuery);
            new MarkerTask().execute(urlQuery);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


//        System.out.println( geoQuery  );
//        http://stackoverflow.com/questions/29724192/using-json-for-android-maps-api-markers-not-showing-up
//        https://maps.googleapis.com/maps/api/geocode/json?address=Winnetka&key=AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI
//        https://maps.googleapis.com/maps/api/geocode/json?address=Czerwonego+Krzyza+5,+Wroclaw&region=pl&key=AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }


    private class MarkerTask extends AsyncTask<URL, Void, String> {

        private static final String LOG_TAG = "ExampleApp";

//    private static final String SERVICE_URL = "https://api.myjson.com/bins/4jb09";

        // Invoked by execute() method of this object
        @Override
        protected String doInBackground(URL... urls) {

            HttpURLConnection conn = null;
            final StringBuilder json = new StringBuilder();
            try {
                // Connect to the web service
//            URL url = new URL(urls);
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
                //throw new IOException("Error connecting to service", e); //uncaught
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
                // De-serialize the JSON string into an array of city objects
                JSONObject jsonObj = new JSONObject(json);
//                JSONArray jsonArray = new JSONArray(json);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObj = jsonArray.getJSONObject(i);

//                // get the first result
                JSONObject res = jsonObj.getJSONArray("results").getJSONObject(0);
//                System.out.println(res.getString("formatted_address"));
                JSONObject loc = res.getJSONObject("geometry").getJSONObject("location");
                Log.d("GEOCODED:", "lat: " + loc.getDouble("lat") +
                        ", lng: " + loc.getDouble("lng"));
//                JSONArray jAr1 = jsonObj.getJSONArray("results");
//                JSONObject jAr2 = jAr1.getJSONObject(0).get("geometry")
//                .getString("pageName");
//                    LatLng latLng = new LatLng(jsonObj.getJSONArray("latlng").getDouble(0),
//                            jsonObj.getJSONArray("latlng").getDouble(1));
                LatLng latLng = new LatLng(loc.getDouble("lat"), loc.getDouble("lng"));

                String formattedAddress = res.getString("formatted_address");

                    //move CameraPosition on first result
//                    if (i == 0) {
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng).zoom(13).build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//                    }

                    // Create a marker for each city in the JSON data.
//                        .snippet(Integer.toString(jsonObj.getInt("population")))
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                        .title(formattedAddress)
                        .position(latLng));
//                }

            } catch (JSONException e) {
                Log.e(LOG_TAG, "Error processing JSON", e);
            }

        }
    }


}
