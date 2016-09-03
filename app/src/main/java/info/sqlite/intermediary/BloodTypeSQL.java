package info.sqlite.intermediary;

import android.content.ContentValues;
import android.database.Cursor;

import info.sqlite.model.BloodType;

/**
 * Set of methods supporting SQLite access for Blood Type Table
 */
public class BloodTypeSQL {

    private static final String TABLE_BLOOD_TYPE = "bloodType";

    // BLOOD_TYPE Table - column names
    private static final String KEY_ID = "id";
    private static final String KEY_RH_PLUS = "_rhPlus";
    private static final String KEY_ANTIGEN_A = "_antigenA";
    private static final String KEY_ANTIGEN_B = "_antigenB";

    public static String createTable() {
        return "CREATE TABLE " + TABLE_BLOOD_TYPE
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_RH_PLUS + " INTEGER NOT NULL,"
                + KEY_ANTIGEN_A + " INTEGER NOT NULL,"
                + KEY_ANTIGEN_B + " INTEGER NOT NULL"
                +  ")";
    }

    public static ContentValues toContentValue(BloodType bloodType) {
        ContentValues values = new ContentValues();
        values.put(KEY_RH_PLUS, bloodType.getRHplus());
        values.put(KEY_ANTIGEN_A, bloodType.getAntigenA());
        values.put(KEY_ANTIGEN_B, bloodType.getAntigenB());
        return values;
    }

    public static String getSelectAllQuery() {
        return "SELECT  * FROM " + TABLE_BLOOD_TYPE;
    }

    public static BloodType getBloodType(Cursor c) {
        return new BloodType(
                c.getInt(c.getColumnIndex(KEY_RH_PLUS)),
                c.getInt(c.getColumnIndex(KEY_ANTIGEN_A)),
                c.getInt(c.getColumnIndex(KEY_ANTIGEN_B)) );
    }

    public static String getTableName() {
        return TABLE_BLOOD_TYPE;
    }

    public static String getKeyId() {
        return KEY_ID;
    }

}
