package info.sqlite.intermediary;

import android.content.ContentValues;
import android.database.Cursor;

import info.sqlite.model.User;

/**
 * Set of methods supporting SQLite access for User model
 */
public abstract class UserSQL {

    private static final String TABLE_USER = "users";

    // USER Table - column names
    private static final String KEY_ID = "id";
    private static final String KEY_NICK = "_nick";
    private static final String KEY_BLOOD_TYPE_ID = "_bloodTypeID";

    // User table create statement
    public static String createTable() {
        return "CREATE TABLE " + TABLE_USER
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NICK + " TEXT NOT NULL,"
                + KEY_BLOOD_TYPE_ID + " TEXT NOT NULL"
                +  ")";
    }

    public static ContentValues toContentValue(User user) {
        ContentValues values = new ContentValues();
        values.put(KEY_NICK, user.get_nick());
        values.put(KEY_BLOOD_TYPE_ID, String.valueOf(user.get_bloodTypeID()));
        return values;
    }

    public static User getUser(Cursor c) {
        User user = new User();
        user.set_id(c.getInt(c.getColumnIndex(KEY_ID)));
        user.set_nick((c.getString(c.getColumnIndex(KEY_NICK))));
        user.set_bloodTypeID(c.getString(c.getColumnIndex(KEY_BLOOD_TYPE_ID)));
        return user;
    }

    public static String getSelectAllQuery() {
        return "SELECT  * FROM " + TABLE_USER;
    }

    public static String getSelectSingleQuery(long user_id) {
        return "SELECT  * FROM " + TABLE_USER + " WHERE "
                + KEY_ID + " = " + user_id;
    }

    public static String getTableName() {
        return TABLE_USER;
    }

    public static String getKeyId() {
        return KEY_ID;
    }
}
