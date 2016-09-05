package edu.blooddonor;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

//useful links
//http://stackoverflow.com/questions/29724192/using-json-for-android-maps-api-markers-not-showing-up
//https://maps.googleapis.com/maps/api/geocode/json?address=Winnetka&key=AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI
//https://maps.googleapis.com/maps/api/geocode/json?address=Czerwonego+Krzyza+5,+Wroclaw&region=pl&key=AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI

/**
 * Geocoding task sends queries to Google Maps Geocoding API and retrives points coordinates basing on addresses.
 */

public class GeocodingTask extends AsyncTask<URL, Void, String> {

    private static final String LOG_TAG = "GeoLoc: ";
    private static final String GEO_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
    private static final String SERVER_KEY = "AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI";

    private GoogleMap mMap;

    public GeocodingTask(GoogleMap mMap)
    {
        this.mMap = mMap;
    }

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

    /** Generation of query to Google Maps Geocoding API basing on address */
    static URL genGeocodingQuery(String address) {
        String addressEnc = null;
        try {
            Log.d(LOG_TAG, "Query address " + address);
            addressEnc = URLEncoder.encode(address, "UTF-8"); //Zamiast "UTF-8" mogłoby być java.nio.charset.StandardCharsets.UTF_8.toString()
            Log.d(LOG_TAG, "Encoded address: " + addressEnc);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); //TODO: exception handling
        }
        URL urlQuery = null;
        try {
            urlQuery = new URL(GEO_URL + "address=" + addressEnc + "&key=" + SERVER_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace(); //TODO: exception handling
        }
        return urlQuery; //TODO: null handling
    }

}