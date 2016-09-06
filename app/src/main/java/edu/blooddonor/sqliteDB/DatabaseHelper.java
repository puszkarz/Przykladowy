package edu.blooddonor.sqliteDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.blooddonor.model.Donation;
import edu.blooddonor.model.Station;
import edu.blooddonor.model.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 22;

    // Database Name
    private static final String DATABASE_NAME = "Donations_List";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(StationSQL.createTable());
        db.execSQL(UserSQL.createTable());
        db.execSQL(DonationSQL.createTable());

        InitialContent.stationsInit(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + StationSQL.getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + UserSQL.getTableName());
        db.execSQL("DROP TABLE IF EXISTS " + DonationSQL.getTableName());
        // create new tables
        onCreate(db);
    }


    // ------------------------ "station" table methods ----------------//
    /** Inserting a station */
    public long insertStation(Station station) {
        SQLiteDatabase db = this.getWritableDatabase();
        long out = insertStation(db, station);
        db.close();
        return out;
    }

    /** Inserting a station (static) */
    public static long insertStation(SQLiteDatabase db, Station station) {
        return db.insert(StationSQL.getTableName(), null, StationSQL.toContentValue(station));
    }

    /** Get a single station */
    public Station getStation(int station_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = StationSQL.getSelectSingleQuery(station_id);
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        Station station = null;
        if (c != null) {
            c.moveToFirst();
            station = StationSQL.getStation(c);
            c.close();
        }
        db.close();
        return station;
    }

    /** getting all the stations */
    public ArrayList<Station> getAllStations() {
        ArrayList<Station> stations = new ArrayList<>();
        String selectQuery = StationSQL.getSelectAllQuery();
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                stations.add(StationSQL.getStation(c));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return stations;
    }

    /** Updating a station */
    public int updateStation(Station station) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = StationSQL.toContentValue(station);
        // updating row
        int ret = db.update(StationSQL.getTableName(), values, UserSQL.getKeyId() + " = ?",
                new String[] { String.valueOf(station.get_id()) });
        db.close();
        return ret;
    }

    /** Deleting a station */
    public void deleteStation(long station_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(StationSQL.getTableName(),
                StationSQL.getKeyId() + " = " + String.valueOf(station_id), null);
        db.close();
    }


    // ------------------------ "user" table methods ----------------//
    /** Creating a user */
    public long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = UserSQL.toContentValue(user);
        long user_id = db.insert(UserSQL.getTableName(), null, values);
        db.close();
        return user_id;
    }

    /** Get a single user */
    public User getUser(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = UserSQL.getSelectSingleQuery(user_id);
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        User user = null;
        if (c != null) {
            c.moveToFirst();
            user = UserSQL.getUser(c);
            c.close();
        }
        db.close();
        return user;
    }

    /** Getting all the users */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String selectQuery = UserSQL.getSelectAllQuery();
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                User user = UserSQL.getUser(c);
                users.add(user);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return users;
    }

    /** Updating a user */
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = UserSQL.toContentValue(user);
        // updating row
        int ret = db.update(UserSQL.getTableName(), values, UserSQL.getKeyId() + " = ?",
                new String[] { String.valueOf(user.get_id()) });
        db.close();
        return ret;
    }

    /** Deleting a user */
    public void deleteUser(long user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(UserSQL.getTableName(), UserSQL.getKeyId() + " = " + String.valueOf(user_id), null);
        db.close();
    }

    /** Getting users Count */
    public int getUsersCount() {
        String countQuery = UserSQL.getSelectAllQuery();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int ret = cursor.getCount();
        cursor.close();
        return ret;
    }


    // ------------------------ "donation" table methods ----------------//
    /** Inserting a donation */
    public long insertDonation(Donation donation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = DonationSQL.toContentValue(donation);
        long donation_id = db.insert(DonationSQL.getTableName(), null, values);
        db.close();
        return donation_id;
    }

    /** Get a single donation */
    public Donation getDonation(long donation_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = DonationSQL.getSelectSingleQuery(donation_id);
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        Donation donation = null;
        if (c != null) {
            c.moveToFirst();
            donation = DonationSQL.getDonation(c);
            c.close();
        }
        db.close();
        return donation;
    }

    /** getting all the donations */
    public List<Donation> getAllDonations() {
        List<Donation> donations = new ArrayList<>();
        String selectQuery = DonationSQL.getSelectAllQuery();
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Donation donation = DonationSQL.getDonation(c);
                donations.add(donation);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return donations;
    }

    /** Updating a donation */
    public int updateDonation(Donation donation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = DonationSQL.toContentValue(donation);
        // updating row
        int ret = db.update(DonationSQL.getTableName(), values,
                DonationSQL.getKeyId() + " = " + String.valueOf(donation.get_id()), null);
        db.close();
        return ret;
    }

    /** Deleting a donation */
    public void deleteDonation(long donation_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DonationSQL.getTableName(), DonationSQL.getKeyId() + " = " +
                String.valueOf(donation_id), null);
    }

    /** Getting donations Count */
    public int getDonationsCount() {
        String countQuery = DonationSQL.getSelectAllQuery();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int ret = cursor.getCount();
        cursor.close();
        return ret;
    }

    /** closing database */
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
