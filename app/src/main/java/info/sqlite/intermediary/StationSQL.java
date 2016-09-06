package info.sqlite.intermediary;

import android.content.ContentValues;
import android.database.Cursor;

import info.sqlite.model.Station;

/**
 * Set of methods supporting SQLite access for Station model
 */
public class StationSQL {

    private static final String TABLE_STATION = "stations";

    // STATION Table - column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "_name";
    private static final String KEY_ADDRESS = "_address";
    private static final String KEY_LATITUDE = "_latitude";
    private static final String KEY_LONGITUDE = "_longitude";

    // Station table create statement
    public static String createTable() {
        return "CREATE TABLE " + TABLE_STATION
                + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT NOT NULL ,"
                + KEY_ADDRESS + " TEXT NOT NULL,"
                + KEY_LATITUDE + " REAL NOT NULL,"
                + KEY_LONGITUDE + " REAL NOT NULL"
                + ")";
    }

    public static ContentValues toContentValue(Station station) {
        ContentValues values = new ContentValues();
        // Assumption that it is used only at initialization of database, so KEY_ID is omitted
        values.put(KEY_NAME, station.get_name());
        values.put(KEY_ADDRESS, station.get_address());
        values.put(KEY_LATITUDE, station.get_latitude());
        values.put(KEY_LONGITUDE, station.get_longitude());
        return values;
    }

    public static Station getStation(Cursor c) {
        return new Station(
                c.getInt(c.getColumnIndex(KEY_ID)),
                c.getString(c.getColumnIndex(KEY_NAME)),
                c.getString(c.getColumnIndex(KEY_ADDRESS)),
                c.getDouble(c.getColumnIndex(KEY_LATITUDE)),
                c.getDouble(c.getColumnIndex(KEY_LONGITUDE)));
    }

    public static String getSelectAllQuery() {
        return "SELECT  * FROM " + TABLE_STATION;
    }

    public static String getSelectSingleQuery(int station_id) {
        return "SELECT  * FROM " + TABLE_STATION + " WHERE "
                + KEY_ID + " = " + station_id;
    }

    public static String getTableName() {
        return TABLE_STATION;
    }

    public static String getKeyId() {
        return KEY_ID;
    }
}
