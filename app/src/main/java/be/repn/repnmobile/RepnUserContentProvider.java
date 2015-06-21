package be.repn.repnmobile;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RepnUserContentProvider extends ContentProvider {

    private UserDBHelper userDBHelper;

    private static final String AUTHORITY = "be.repn.repnmobile.contentprovider";

    private static final String BASE_PATH = "repnUsers";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/repnUsers";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/repnUser";

    private static final int REPN_USERS = 10;
    private static final int REPN_USER = 20;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, REPN_USERS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", REPN_USER);
    }

    public RepnUserContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)){
            case REPN_USERS : return CONTENT_TYPE;
            case REPN_USER : return CONTENT_ITEM_TYPE;
        }
        throw new IllegalArgumentException("Unknown URI: " + uri);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase writableDatabase = userDBHelper.getWritableDatabase();
        long id = writableDatabase.insertWithOnConflict(RepnContract.User.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public boolean onCreate() {
        this.userDBHelper = new UserDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase readableDatabase = userDBHelper.getReadableDatabase();
        Cursor cursor = readableDatabase.query(RepnContract.User.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public static void insertOrUpdateUser(User user, Context context){
        ContentValues values = new ContentValues();
        values.put(RepnContract.User.COLUMN_NAME_EMAIL, user.getEmail());
        values.put(RepnContract.User.COLUMN_NAME_LAST_NAME, user.getLastName());
        values.put(RepnContract.User.COLUMN_NAME_FIRST_NAME, user.getFirstName());
        values.put(RepnContract.User.COLUMN_NAME_CITY, user.getCity());
        values.put(RepnContract.User.COLUMN_NAME_STREET, user.getStreet());
        values.put(RepnContract.User.COLUMN_NAME_STREET_NUMBER, user.getStreetNumber());
        values.put(RepnContract.User.COLUMN_NAME_MOBILE, user.getMobile());
        if(user.getBirthDate() != null) values.put(RepnContract.User.COLUMN_NAME_BIRTH_DATE, getDateTime(new Date(user.getBirthDate())));

        context.getContentResolver().insert(CONTENT_URI, values);
    }

    private static String getDateTime(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }
}
