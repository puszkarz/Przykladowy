package info.sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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
    private static final String KEY_ID = "_id";
    private static final String KEY_DATE = "_date";
    private static final String KEY_TYPE = "_type";
    private static final String KEY_VOLUME = "_volume";
    private static final String KEY_USER_NICK = "_user_nick";
    private static final String KEY_STATION_NAME = "_station_name";

    // Table Create Statements
    // Station table create statement
    private static final String CREATE_TABLE_STATION = "CREATE TABLE "
            + TABLE_STATION + "(" + KEY_NAME + " TEXT PRIMARY KEY," + KEY_ADDRESS
            + " TEXT," + KEY_COORDINATE_X + " INTEGER," + KEY_COORDINATE_Y + " INTEGER,"
            + ")";

    // Tag table create statement
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER
            + "(" + KEY_NICK + " TEXT PRIMARY KEY," + KEY_BLOODTYPE + " TEXT,"
            +  ")";

    // todo_tag table create statement
    private static final String CREATE_TABLE_DONATION = "CREATE TABLE "
            + TABLE_DONATION + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_DATE + " DATETIME," + KEY_TYPE + " TEXT," + KEY_VOLUME + " INTEGER,"
            + KEY_USER_NICK + "TEXT," + KEY_STATION_NAME + " TEXT" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
