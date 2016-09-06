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
        Map<Station, Integer> distanceMap = new HashMap<>();
        for (Map.Entry<Station, String> entry : jsonMap.entrySet()) {
            Integer distance = GeocodingQuery.getDistanceFromJSON(entry.getValue());
            if (distance!=null) {
                distanceMap.put(entry.getKey(), distance);
            }
        }
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, statListToString(distanceMap));
        if (distStationsListView != null) {
            distStationsListView.setAdapter(listViewAdapter);
        }
    }

    private static List<String> statListToString(Map<Station, Integer> distanceMap) {
        // Sort by the distance
        List<Map.Entry<Station, Integer>> listStatDist =
                new LinkedList<>( distanceMap.entrySet() );
        Collections.sort( listStatDist,
                new Comparator<Map.Entry<Station, Integer>>() {
                    public int compare( Map.Entry<Station, Integer> o1, Map.Entry<Station, Integer> o2 ) {
                        return (o1.getValue()).compareTo( o2.getValue() );
                    }
                } );
        // Make list of strings
        List<String> out = new ArrayList<>();
        for (Map.Entry<Station, Integer> entry : listStatDist) {
            out.add(entry.getKey().toString() + " m, " + entry.getValue().toString());
        }
        return out;
    }

}
