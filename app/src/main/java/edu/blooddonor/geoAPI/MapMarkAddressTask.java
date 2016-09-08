package edu.blooddonor.geoAPI;

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
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Async Task marks on a map positions of stations basing on addresses (using Google Maps Geocoding API).
 *
 * This asynchronous task gets latitude and longitude of stations basing on addresses.
 * Uses queries to Google Maps Geocoding API. Sets markers on a map using obtained coordinates.
 *
 * @author puszkarz
 *
 */

class MapMarkAddressTask extends AsyncTask<String, Void, String> {

    private static final String LOG_TAG = "GeoLoc: ";

    private GoogleMap mMap;

    public MapMarkAddressTask(GoogleMap mMap)
    {
        this.mMap = mMap;
    }

    // Invoked by execute() method of this object
    // Only one address is allowed
    @Override
    protected String doInBackground(String... addresses) {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            URL url = GeocodingQuery.genGeocodingQuery(addresses[0]);

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

            MapsActivity.moveCamera(mMap, latLng);
            mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title("Blood Donation Center")
                    .position(latLng));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON", e);
        }
    }

}