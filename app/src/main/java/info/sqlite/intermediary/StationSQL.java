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
    private static final String KEY_COORDINATE_X = "_coordinate_x";
    private static final String KEY_COORDINATE_Y = "_coordinate_y";

    // Station table create statement
    public static String createTable() {
        return "CREATE TABLE " + TABLE_STATION
                + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT NOT NULL ,"
                + KEY_ADDRESS + " TEXT NOT NULL,"
                + KEY_COORDINATE_X + " INTEGER,"
                + KEY_COORDINATE_Y + " INTEGER"
                + ")";
    }

    public static ContentValues toContentValue(Station station) {
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, station.get_name());
        values.put(KEY_ADDRESS, station.get_address());
        values.put(KEY_COORDINATE_X, station.get_coordinate_x());
        values.put(KEY_COORDINATE_Y, station.get_coordinate_y());
        return values;
    }

    public static Station getStation(Cursor c) {
        Station station = new Station();
        station.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
        station.set_name((c.getString(c.getColumnIndex(KEY_NAME))));
        station.set_address(c.getString(c.getColumnIndex(KEY_ADDRESS)));
        station.set_coordinate_x(c.getInt(c.getColumnIndex(KEY_COORDINATE_X)));
        station.set_coordinate_y(c.getInt(c.getColumnIndex(KEY_COORDINATE_Y)));
        return station;
    }

    public static String getSelectAllQuery() {
        return "SELECT  * FROM " + TABLE_STATION;
    }

    public static String getSelectSingleQuery(long station_id) {
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
