package com.example.bartek.przykladowy;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import info.sqlite.helper.DatabaseHelper;
import info.sqlite.model.Station;

public class DistanceMatrixTask extends AsyncTask<String, Void, String> {

    private static final String LOG_TAG = "DistMatrix: ";
    private static final String DIST_MATRIX_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    private static final String SERVER_KEY = "AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI";

    private Context context;
    private ListView distStationsListView;
    private List<Station> stations;

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

    @Override
    protected String doInBackground(String... addresses) {
        HttpURLConnection conn = null;
        final StringBuilder json = new StringBuilder();
        try {
            // Connect to the web service
            // URL url = new URL(urls);
            URL url = genDistanceMatrixQuery(addresses[0]);
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
        ArrayList<String> distances = getDistanceFromJSON(json);
        ArrayAdapter<String> listViewAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, statListToString(stations, distances));
        if (distStationsListView != null) {
            distStationsListView.setAdapter(listViewAdapter);
        }
    }

    private static ArrayList<String> getDistanceFromJSON(String json) {
        ArrayList<String> out = new ArrayList<>();
        //TODO: Decode JSON response
//        for (Station st : stations) {
//            out.add(st.toString());
//        }
        return out;
    }

    private static ArrayList<String> statListToString(List<Station> stations, ArrayList<String> distance) {
        //TODO: zrobić jako mapę!
        ArrayList<String> out = new ArrayList<>();
        int idx = 0;
        for (Station st : stations) {
            out.add(st.toString() + distance.get(idx));
            idx++;
        }
        return out;
    }

    //        https://maps.googleapis.com/maps/api/distancematrix/json?
    //        origins=41.43206,-81.38992|-33.86748,151.20699
    //        &destinations=40.6905615,-73.9976592|40.6905615,-73.9976592
    //        &key=YOUR_API_KEY

    private static URL genDistanceMatrixQuery(String address) {
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
            urlQuery = new URL(DIST_MATRIX_URL +
                    "origins=" + addressEnc +
                    "&destinations=" + addressEnc +
                    "&key=" + SERVER_KEY);
        } catch (MalformedURLException e) {
            e.printStackTrace(); //TODO: exception handling
        }
        return urlQuery; //TODO: null handling
    }
}
