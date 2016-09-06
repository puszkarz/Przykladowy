package edu.blooddonor.geoAPI;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

class DistanceMatrixTask extends AsyncTask<LatLng, Void, String> {

    private static final String LOG_TAG = "DistMatrix: ";

    private Context context;
    private ListView distStationsListView;
    private ArrayList<Station> stations;

    // run in Activity as DistanceMatrixTask(targetListView, getApplicationContext());
    public DistanceMatrixTask(ListView targetListView, Context context) {
        distStationsListView = targetListView;
        this.context = context;
    }

    @Override
    protected void onPreExecute () {
        DatabaseHelper db = new DatabaseHelper(context);
        stations = db.getAllStations();
    }

    // Only one address is allowed
    @Override
    protected String doInBackground(LatLng... logLats) {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            // URL url = new URL(urls);
            URL url = GeocodingQuery.genDistanceMatrixQuery(logLats[0]);
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

    @Override
    protected void onPostExecute(String json) {
        Map<Station, Double> distanceMap = GeocodingQuery.getDistanceFromJSON(json);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, statListToString(distanceMap));
        if (distStationsListView != null) {
            distStationsListView.setAdapter(listViewAdapter);
        }
    }

    private static List<String> statListToString(Map<Station, Double> distanceMap) {
        // Sort by the distance
        List<Map.Entry<Station, Double>> listStatDist =
                new LinkedList<Map.Entry<Station, Double>>( distanceMap.entrySet() );
        Collections.sort( listStatDist,
                new Comparator<Map.Entry<Station, Double>>() {
                    public int compare( Map.Entry<Station, Double> o1, Map.Entry<Station, Double> o2 ) {
                        return (o1.getValue()).compareTo( o2.getValue() );
                    }
                } );
        // Make list of strings
        List<String> out = new ArrayList<>();
        for (Map.Entry<Station, Double> entry : listStatDist) {
            out.add(entry.getKey().toString() + " km, " + entry.getValue());
        }
        return out;
    }

}
