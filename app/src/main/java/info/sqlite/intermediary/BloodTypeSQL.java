package info.sqlite.intermediary;

/**
 * Set of methods supporting SQLite access for Blood Type Table
 */
public class BloodTypeSQL {

    private static final String TABLE_BLOOD_TYPE = "bloodType";

    // BLOOD_TYPE Table - column names
    private static final String KEY_ID = "id";
    private static final String KEY_BLOOD_TYPE = "_bloodType";

    public static String createTable() {
        return "CREATE TABLE " + TABLE_BLOOD_TYPE
                + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_BLOOD_TYPE + " TEXT"
                +  ")";
    }

    public static String getTableName() {
        return TABLE_BLOOD_TYPE;
    }

    public static String getKeyId() {
        return KEY_ID;
    }
}
