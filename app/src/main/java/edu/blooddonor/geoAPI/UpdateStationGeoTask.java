package edu.blooddonor.geoAPI;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.blooddonor.model.Station;
import edu.blooddonor.sqliteDB.DatabaseHelper;

/**
 * Async Task updating database Stations entries in case of geographical coordinates missing (using queries to Google Maps Geocoding API).
 *
 * This asynchronous task checks database looking for not well defined Stations table entries.
 * Then gets latitude and longitude of stations basing on addresses using queries to Google Maps Geocoding API.
 * Finally updates database.
 *
 * @author puszkarz
 *
 */

class UpdateStationGeoTask extends AsyncTask<Void, Void, String> {

    private static String LOG_TAG = "UpdateStatGeo: ";

    private Station station;
    private DatabaseHelper db;
    private URL url;
    private GoogleMap mMap;

    public UpdateStationGeoTask(Station station, DatabaseHelper db) {
        this.station = station;
        this.db = db;
        this.url = null;
        this.mMap = null;

    }

    public UpdateStationGeoTask(GoogleMap mMap, Station station, DatabaseHelper db) {
        this(station, db);
        this.mMap = mMap;
    }

    @Override
    protected void onPreExecute () {
        url = GeocodingQuery.genGeocodingQuery(station.get_address());
    }

    @Override
    protected String doInBackground(Void... voids) {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        if (url != null) {
            try {
                conn = (HttpURLConnection) url.openConnection();
                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                // Read the JSON data into the StringBuilder
                int read;
                char[] buff = new char[1024];
                while ((read = in.read(buff)) != -1) {
                    json.append(buff, 0, read);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        return json.toString();
    }

    // Executed after the complete execution of doInBackground() method
    @Override
    protected void onPostExecute(String json) {
        if (json != null) {
            if (!json.equals("")) {
                LatLng latLng = GeocodingQuery.getLatLngFromJSON(json);
                if (latLng != null) {
                    station.set_latitude(latLng.latitude);
                    station.set_longitude(latLng.longitude);
                    db.updateStation(station);
                    Log.d(LOG_TAG, "Station updated: " + station.toString());

                    if (mMap!=null) {
                        mMap.addMarker(new MarkerOptions()
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                                .title(station.get_name())
                                .position(latLng));
                        Log.d(LOG_TAG, "Marker added: " + station.get_name());
                    }

                } else {
                    Log.e(LOG_TAG, "Wrong JSON output for Station " + station.toString());
                }
            }
        }

    }
}
