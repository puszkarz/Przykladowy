package info.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.sqlite.model.Donation;
import info.sqlite.model.Station;
import info.sqlite.model.User;

/**
 * Created by magda on 31.08.16.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Donations_List";

    // Common column names
    private static final String KEY_ID = "id";

    // Table Names
    private static final String TABLE_STATION = "stations";
    private static final String TABLE_USER = "users";
    private static final String TABLE_DONATION = "donations";

    // STATION Table - column names
    private static final String KEY_NAME = "_name";
    private static final String KEY_ADDRESS = "_address";
    private static final String KEY_COORDINATE_X = "_coordinate_x";
    private static final String KEY_COORDINATE_Y = "_coordinate_y";


    // USER Table - column names
    private static final String KEY_NICK = "_nick";
    private static final String KEY_BLOODTYPE = "_bloodtype";

    // DONATION_TAGS Table - column names
    private static final String KEY_DATE = "_date";
    private static final String KEY_TYPE = "_type";
    private static final String KEY_VOLUME = "_volume";
    private static final String KEY_USER_ID = "_user_id";
    private static final String KEY_STATION_ID = "_station_id";

    // Table Create Statements
    // Station table create statement
    private static final String CREATE_TABLE_STATION = "CREATE TABLE " + TABLE_STATION
            + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NAME + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_COORDINATE_X + " INTEGER,"
            + KEY_COORDINATE_Y + " INTEGER"
            + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_NICK + " TEXT,"
            + KEY_BLOODTYPE + " TEXT"
            +  ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_DONATION = "CREATE TABLE " + TABLE_DONATION
            + "("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_DATE + " TEXT,"
            + KEY_TYPE + " TEXT,"
            + KEY_VOLUME + " INTEGER,"
            + KEY_USER_ID + " INTEGER,"
            + KEY_STATION_ID + " INTEGER"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_STATION);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_DONATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DONATION);

        // create new tables
        onCreate(db);
    }

    // ------------------------ "station" table methods ----------------//

    /**
     * Creating a station
     */
    public long createStation(Station station) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, station.get_name());
        values.put(KEY_ADDRESS, station.get_address());
        values.put(KEY_COORDINATE_X, station.get_coordinate_x());
        values.put(KEY_COORDINATE_Y, station.get_coordinate_y());

        // insert row
        long station_id = db.insert(TABLE_STATION, null, values);
        db.close();
        return station_id;
    }

    /**
     * get a single station
     */
    public Station getStation(long station_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_STATION + " WHERE "
                + KEY_ID + " = " + station_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Station station = new Station();
        station.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
        station.set_name((c.getString(c.getColumnIndex(KEY_NAME))));
        station.set_address(c.getString(c.getColumnIndex(KEY_ADDRESS)));
        station.set_coordinate_x(c.getInt(c.getColumnIndex(KEY_COORDINATE_X)));
        station.set_coordinate_y(c.getInt(c.getColumnIndex(KEY_COORDINATE_Y)));
        db.close();
        return station;
    }


    /**
     * getting all the stations
     */
    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList<Station>();
        String selectQuery = "SELECT  * FROM " + TABLE_STATION;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Station station = new Station();
                station.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
                station.set_name((c.getString(c.getColumnIndex(KEY_NAME))));
                station.set_address(c.getString(c.getColumnIndex(KEY_ADDRESS)));
                station.set_coordinate_x(c.getInt(c.getColumnIndex(KEY_COORDINATE_X)));
                station.set_coordinate_y(c.getInt(c.getColumnIndex(KEY_COORDINATE_Y)));

                // adding to stations list
                stations.add(station);
            } while (c.moveToNext());
        }
        db.close();
        return stations;
    }

    /**
     * Updating a station
     */
    public int updateStation(Station station) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, station.get_name());
        values.put(KEY_ADDRESS, station.get_address());
        values.put(KEY_COORDINATE_X, station.get_coordinate_x());
        values.put(KEY_COORDINATE_Y, station.get_coordinate_y());

        // updating row
        int ret = db.update(TABLE_STATION, values, KEY_ID + " = ?",
                new String[] { String.valueOf(station.get_id()) });
        db.close();
        return ret;
    }

    /**
     * Deleting a station
     */
    public void deleteStation(long station_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STATION, KEY_ID + " = ?",
                new String[] { String.valueOf(station_id) });
        db.close();
    }

    // ------------------------ "user" table methods ----------------//

    /**
     * Creating a user
     */
    public long createUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NICK, user.get_nick());
        values.put(KEY_BLOODTYPE, String.valueOf(user.get_bloodtype()));

        // insert row
        long user_id = db.insert(TABLE_USER, null, values);
        db.close();
        return user_id;
    }

    /**
     * get a single user
     */
    public User getUser(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_ID + " = " + user_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        User user = new User();
        user.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
        user.set_nick((c.getString(c.getColumnIndex(KEY_NICK))));
        user.set_bloodtype(c.getString(c.getColumnIndex(KEY_BLOODTYPE)));
        db.close();
        return user;
    }


    /**
     * getting all the users
     */
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                User user = new User();
                user.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
                user.set_nick((c.getString(c.getColumnIndex(KEY_NICK))));
                user.set_bloodtype(c.getString(c.getColumnIndex(KEY_BLOODTYPE)));

                // adding to stations list
                users.add(user);
            } while (c.moveToNext());
        }
        db.close();
        return users;
    }

    /**
     * Updating a user
     */
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, user.get_nick());
        values.put(KEY_TYPE, String.valueOf(user.get_bloodtype()));

        // updating row
        int ret = db.update(TABLE_USER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(user.get_id()) });
        db.close();
        return ret;
    }

    /**
     * Deleting a user
     */
    public void deleteUser(long user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER, KEY_ID + " = ?",
                new String[] { String.valueOf(user_id) });
        db.close();
    }

    // ------------------------ "donation" table methods ----------------//

    /**
     * Creating a donation
     */
    public long createDonation(Donation donation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, String.valueOf(donation.get_date()));
        values.put(KEY_TYPE, String.valueOf(donation.get_type()));
        values.put(KEY_VOLUME, donation.get_volume());
        values.put(KEY_USER_ID, donation.get_user_id());
        values.put(KEY_STATION_ID, donation.get_station_id());

        // insert row
        long donation_id = db.insert(TABLE_DONATION, null, values);
        db.close();
        return donation_id;
    }

    /**
     * get a single donation
     */
    public Donation getDonation(long donation_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_DONATION + " WHERE "
                + KEY_ID + " = " + donation_id;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Donation donation = new Donation();
        donation.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
        donation.set_date((c.getString(c.getColumnIndex(KEY_DATE))));
        donation.set_type((c.getString(c.getColumnIndex(KEY_TYPE))));
        donation.set_volume(c.getInt(c.getColumnIndex(KEY_VOLUME)));
        donation.set_user_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
        donation.set_station_id(c.getInt(c.getColumnIndex(KEY_STATION_ID)));
        db.close();
        return donation;
    }


    /**
     * getting all the donations
     */
    public List<Donation> getAllDonations() {
        List<Donation> donations = new ArrayList<Donation>();
        String selectQuery = "SELECT  * FROM " + TABLE_DONATION;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Donation donation = new Donation();
                donation.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
                donation.set_date((c.getString(c.getColumnIndex(KEY_DATE))));
                donation.set_type((c.getString(c.getColumnIndex(KEY_TYPE))));
                donation.set_volume(c.getInt(c.getColumnIndex(KEY_VOLUME)));
                donation.set_user_id(c.getInt(c.getColumnIndex(KEY_USER_ID)));
                donation.set_station_id(c.getInt(c.getColumnIndex(KEY_STATION_ID)));

                // adding to stations list
                donations.add(donation);
            } while (c.moveToNext());
        }
        db.close();
        return donations;
    }

    /**
     * Updating a donation
     */
    public int updateDonation(Donation donation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, String.valueOf(donation.get_date()));
        values.put(KEY_TYPE, String.valueOf(donation.get_type()));
        values.put(KEY_VOLUME, donation.get_volume());
        values.put(KEY_USER_ID, donation.get_user_id());
        values.put(KEY_STATION_ID, donation.get_station_id());

        // updating row
        int ret = db.update(TABLE_DONATION, values, KEY_ID + " = ?",
                new String[] { String.valueOf(donation.get_id()) });
        db.close();
        return ret;
    }

    /**
     * Deleting a donation
     */
    public void deleteDonation(long donation_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DONATION, KEY_ID + " = ?",
                new String[] { String.valueOf(donation_id) });
    }

    // Getting donations Count
    public int getDonationsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_DONATION;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int ret = cursor.getCount();
        cursor.close();
        return ret;
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
