package edu.blooddonor.sqliteDB;

import android.content.ContentValues;
import android.database.Cursor;

import edu.blooddonor.model.Donation;

/**
 * Set of methods supporting SQLite access for Donation model
 */
abstract class DonationSQL {

    // Table Name
    private static final String TABLE_DONATION = "donations";

    // DONATION Table - column names
    private static final String KEY_ID = "id";
    private static final String KEY_DATE = "_date";
    private static final String KEY_TYPE = "_type";
    private static final String KEY_VOLUME = "_volume";
    private static final String KEY_BLOOD_VOLUME = "_blood_volume";
    private static final String KEY_USER_ID = "_user_id";
    private static final String KEY_STATION_ID = "_station_id";

    // Donation table create statement
    public static String createTable() {
        return "CREATE TABLE " + TABLE_DONATION
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_DATE + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_VOLUME + " INTEGER,"
                + KEY_BLOOD_VOLUME + " REAL,"
                + KEY_USER_ID + " INTEGER NOT NULL,"
                + KEY_STATION_ID + " INTEGER NOT NULL,"
                + " FOREIGN KEY (" + KEY_STATION_ID + ") REFERENCES " + StationSQL.getTableName() + "(" + StationSQL.getKeyId() + "),"
                + " FOREIGN KEY (" + KEY_USER_ID + ") REFERENCES " + UserSQL.getTableName() + "(" + UserSQL.getKeyId() + ")"
                + ")";
    }

    public static ContentValues toContentValue(Donation donation) {
        ContentValues values = new ContentValues();
        values.put(KEY_DATE, String.valueOf(donation.get_date()));
        values.put(KEY_TYPE, String.valueOf(donation.get_type()));
        values.put(KEY_VOLUME, donation.get_volume());
        values.put(KEY_BLOOD_VOLUME, donation.get_blood_volume());
        values.put(KEY_USER_ID, donation.get_user_id());
        values.put(KEY_STATION_ID, donation.get_station_id());
        return values;
    }

    public static Donation getDonation(Cursor c) {
        Donation donation = new Donation();
        donation.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
        donation.set_date((c.getString(c.getColumnIndex(KEY_DATE))));
        donation.set_type((c.getString(c.getColumnIndex(KEY_TYPE))));
        donation.set_volume(c.getInt(c.getColumnIndex(KEY_VOLUME)));
        donation.set_blood_volume(c.getDouble(c.getColumnIndex(KEY_BLOOD_VOLUME)));
        donation.set_user_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
        donation.set_station_id(c.getInt(c.getColumnIndex(KEY_STATION_ID)));
        return donation;
    }

    public static String getSelectAllQuery() {
        return "SELECT  * FROM " + TABLE_DONATION;
    }

    public static String getSelectSingleQuery(long donation_id) {
        return "SELECT  * FROM " + TABLE_DONATION + " WHERE "
                + KEY_ID + " = " + donation_id;
    }

    public static String getTableName() {
        return TABLE_DONATION;
    }

    public static String getKeyId() {
        return KEY_ID;
    }

}
