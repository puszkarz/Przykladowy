package edu.blooddonor.geoAPI;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.blooddonor.sqliteDB.DatabaseHelper;
import edu.blooddonor.model.Station;

import com.google.android.gms.maps.model.LatLng;

/**
 * Async Task getting distances and travel times between Stations and given location of user (using Google Maps Distance Matrix API).
 *
 * This asynchronous task gets distances between Stations and given location of user.
 * Uses queries to Google Maps Distance Matrix API. Updates a list view with concatenated
 * names of station, distances (in km) and travel time [sorted by distance].
 *
 * @author puszkarz
 *
 */

class DistanceMatrixTask extends AsyncTask<LatLng, Void, Map<Station, String>> {

    private static final String LOG_TAG = "DistMatrix: ";

    private Context context;
    private ListView distStationsListView;
    ArrayList<Station> stations;


    public DistanceMatrixTask(ListView _targetListView, Context _context) {
        distStationsListView = _targetListView;
        this.context = _context;
    }

    @Override
    protected void onPreExecute () {
        DatabaseHelper db = new DatabaseHelper(context);
        stations = db.getAllStations();
    }

    // Only one LatLng is allowed
    @Override
    protected Map<Station, String> doInBackground(LatLng... userLatLngs) {

        Map<Station, URL> urlMap = new HashMap<>();

        for (Station st : stations) {
            URL url = GeocodingQuery.genDistanceMatrixQuery(st, userLatLngs[0]);
            if (url != null) {
                urlMap.put(st, url);
                Log.d(LOG_TAG, "urlMap added: " + url.toString());
            }
        }

        Map<Station, String> jsonMap = new HashMap<>();

        for (Map.Entry<Station, URL> entry : urlMap.entrySet()) {
            HttpURLConnection conn = null;
            final StringBuilder json = new StringBuilder();
            try {
                // Connect to the web service
                Log.d(LOG_TAG, "Processing: " + entry.getValue().toString());
                conn = (HttpURLConnection) entry.getValue().openConnection();
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
            jsonMap.put(entry.getKey(), json.toString());
        }
        return jsonMap;
    }

    @Override
    protected void onPostExecute(Map<Station, String> jsonMap) {
        Map<Station, DistancePack> distanceMap = new HashMap<>();
        for (Map.Entry<Station, String> entry : jsonMap.entrySet()) {
            if (entry.getValue() != null) {
                DistancePack distance = new DistancePack(entry.getValue());
                distanceMap.put(entry.getKey(), distance);
            }
        }
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, statListToString(distanceMap));
        if (distStationsListView != null) {
            distStationsListView.setAdapter(listViewAdapter);
        }
    }

    private static List<String> statListToString(Map<Station, DistancePack> distanceMap) {
        // Sort by the distance
        List<Map.Entry<Station, DistancePack>> listStatDist =
                new LinkedList<>( distanceMap.entrySet() );
        Collections.sort( listStatDist,
                new Comparator<Map.Entry<Station, DistancePack>>() {
                    public int compare( Map.Entry<Station, DistancePack> o1, Map.Entry<Station, DistancePack> o2 ) {
                        return (o1.getValue().getDistance()).compareTo(o2.getValue().getDistance());
                    }
                } );
        // Make list of strings
        List<String> out = new ArrayList<>();
        for (Map.Entry<Station, DistancePack> entry : listStatDist) {
            out.add(entry.getKey().toString() + ",\n" + entry.getValue().toString());
        }
        return out;
    }

    private class DistancePack  {

        private Integer distance;
        private String distTxt;
        private String timeTxt;

        public DistancePack(String json) {
            this.distance = GeocodingQuery.getDistanceFromJSON(json);
            this.distTxt = GeocodingQuery.getDistanceTxtFromJSON(json);
            this.timeTxt = GeocodingQuery.getDurationFromJSON(json);
        }

        public Integer getDistance() {
            return this.distance;
        }

        public String toString () {
            return distTxt + ", " + timeTxt;
        }

    }

}


