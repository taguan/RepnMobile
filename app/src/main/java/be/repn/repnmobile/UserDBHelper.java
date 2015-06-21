package be.repn.repnmobile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDBHelper extends SQLiteOpenHelper {

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RepnContract.User.TABLE_NAME + " (" +
                    RepnContract.User._ID + " INTEGER PRIMARY KEY," +
                    RepnContract.User.COLUMN_NAME_LAST_NAME + " VARCHAR(255)," +
                    RepnContract.User.COLUMN_NAME_FIRST_NAME + " VARCHAR(255)," +
                    RepnContract.User.COLUMN_NAME_CITY + " VARCHAR(255)," +
                    RepnContract.User.COLUMN_NAME_STREET + " VARCHAR(255)," +
                    RepnContract.User.COLUMN_NAME_STREET_NUMBER + " VARCHAR(64)," +
                    RepnContract.User.COLUMN_NAME_PHONE + " VARCHAR(64)," +
                    RepnContract.User.COLUMN_NAME_MOBILE + " VARCHAR(64)," +
                    RepnContract.User.COLUMN_NAME_BIRTH_DATE + " DATE," +
                    RepnContract.User.COLUMN_NAME_EMAIL + " VARCHAR(255)," +
                    "UNIQUE (" + RepnContract.User.COLUMN_NAME_EMAIL + ")" +
            " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RepnContract.User.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Repn.db";

    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
