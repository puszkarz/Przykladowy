package edu.blooddonor.geoAPI;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import edu.blooddonor.model.Station;

/**
 * Collection of static methods used to communicate with Google Maps APIs, parsing JSON responses.
 *
 * Collection of static methods used to communicate with Google Maps APIs:
 * There are included here methods generating queries to Google Maps Distance Matrix API
 * and Google Maps Geocoding API. Moreover JSON responses from those APIs are parsed here.
 *
 * @author puszkarz
 *
 */

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

    public static Integer getDistanceFromJSON(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject rows = jsonObj.getJSONArray("rows").getJSONObject(0);
            JSONObject elements = rows.getJSONArray("elements").getJSONObject(0);
            return elements.getJSONObject("distance").getInt("value");
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON", e);
            return null;
        }
    }

    public static String getDistanceTxtFromJSON(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject rows = jsonObj.getJSONArray("rows").getJSONObject(0);
            JSONObject elements = rows.getJSONArray("elements").getJSONObject(0);
            return elements.getJSONObject("distance").getString("text");
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON", e);
            return null;
        }
    }

    public static String getDurationFromJSON(String json) {
        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject rows = jsonObj.getJSONArray("rows").getJSONObject(0);
            JSONObject elements = rows.getJSONArray("elements").getJSONObject(0);
            return elements.getJSONObject("duration").getString("text");
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Error processing JSON", e);
            return null;
        }
    }

    /** Generation of query to Google Maps Geocoding API basing on address */
    public static URL genGeocodingQuery(String address) {
        String addressEnc = null;
        try {
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
        return urlQuery;
    }

    public static URL genDistanceMatrixQuery(Station station, LatLng userLatLng) {
        if (!station.isWellDefined()) {
            return null;
        }
        String stLatLngString =
                Double.toString(station.get_latitude()) + "," + Double.toString(station.get_longitude());
        String userLatLngString =
                Double.toString(userLatLng.latitude) + "," + Double.toString(userLatLng.longitude);
        try {
            stLatLngString = URLEncoder.encode(stLatLngString, "UTF-8");
            userLatLngString = URLEncoder.encode(userLatLngString, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace(); //TODO: exception handling
        }
        URL urlQuery = null;
        try {
            urlQuery = new URL(DIST_MATRIX_URL +
                    "origins=" + userLatLngString +
                    "&destinations=" + stLatLngString +
                    "&key=" + SERVER_KEY);
            Log.d(LOG_TAG, "URL generated: " + urlQuery.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace(); //TODO: exception handling
        }
        return urlQuery;
    }

}
