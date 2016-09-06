package edu.blooddonor.geoAPI;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.blooddonor.model.Station;
import edu.blooddonor.sqliteDB.DatabaseHelper;

class UpdateStationGeoTask extends AsyncTask<Void, Void, String> {

    private static String LOG_TAG = "UpdateStatGeo: ";

    private Station station;
    private DatabaseHelper db;
    private URL url;

    public UpdateStationGeoTask(Station station, DatabaseHelper db) {
        this.station = station;
        this.db = db;
        this.url = null;
    }

    protected void onPreExecute () {
        Log.e(LOG_TAG, "URLAsync preExec: " + station.toString());
        if (!station.isWellDefined()) {
            Log.e(LOG_TAG, "Station not well defined. Updating. Stat: " + station.toString());
            url = GeocodingQuery.genGeocodingQuery(station.get_address());
        }
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
        LatLng latLng = GeocodingQuery.getLatLngFromJSON(json);
        if (latLng != null) {
            station.set_latitude(latLng.latitude);
            station.set_longitude(latLng.longitude);
            db.updateStation(station);
            Log.e(LOG_TAG, "Station updated: " + station.toString());
        }
    }
}
