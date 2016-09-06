package edu.blooddonor.geoAPI;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import edu.blooddonor.model.Station;

abstract class GeocodingQuery {

    private static final String LOG_TAG = "GeocodingQuery: ";

    private static final String GEO_URL = "https://maps.googleapis.com/maps/api/geocode/json?";
    private static final String DIST_MATRIX_URL = "https://maps.googleapis.com/maps/api/distancematrix/json?";
    private static final String SERVER_KEY = "AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI";

    public static LatLng getLatLngFromJSON(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject res = jsonObj.getJSONArray("results").getJSONObject(0);
            JSONObject loc = res.getJSONObject("geometry").getJSONObject("location");
            return new LatLng(loc.getDouble("lat"), loc.getDouble("lng"));
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON", e);
            return null;
        }
    }


    //useful links
    //http://stackoverflow.com/questions/29724192/using-json-for-android-maps-api-markers-not-showing-up
    //https://maps.googleapis.com/maps/api/geocode/json?address=Winnetka&key=AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI
    //https://maps.googleapis.com/maps/api/geocode/json?address=Czerwonego+Krzyza+5,+Wroclaw&region=pl&key=AIzaSyAXsltmnu0OEc_UMgthhZ7BDiiajeVD_JI

    /** Generation of query to Google Maps Geocoding API basing on address */
    public static URL genGeocodingQuery(String address) {
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

    public static Map<Station, Double> getDistanceFromJSON(String json) {
        Map<Station, Double> distanceMap = new HashMap<Station, Double>();
        //TODO: Decode JSON response
//        for (Station st : stations) {
//            out.add(st.toString());
//        }
        return distanceMap;
    }

    //        https://maps.googleapis.com/maps/api/distancematrix/json?
    //        origins=41.43206,-81.38992|-33.86748,151.20699
    //        &destinations=40.6905615,-73.9976592|40.6905615,-73.9976592
    //        &key=YOUR_API_KEY
    public static URL genDistanceMatrixQuery(LatLng latLng) {
        //TODO: uwzględnić listę stacji i lokalizację
//        String addressEnc = null;
//        try {
//            Log.d(LOG_TAG, "Query address " + address);
//            addressEnc = URLEncoder.encode(address, "UTF-8"); //Zamiast "UTF-8" mogłoby być java.nio.charset.StandardCharsets.UTF_8.toString()
//            Log.d(LOG_TAG, "Encoded address: " + addressEnc);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace(); //TODO: exception handling
//        }
        URL urlQuery = null;
        try {
            urlQuery = new URL(DIST_MATRIX_URL +
                    "origins=" + latLng +
                    "&destinations=" + latLng +
                    "&key=" + SERVER_KEY);
            Log.d(LOG_TAG, "URL generated: " + urlQuery.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace(); //TODO: exception handling
        }
        return urlQuery; //TODO: null handling
    }

}
