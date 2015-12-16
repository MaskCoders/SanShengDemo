package com.sansheng.testcenter.provider;

import android.content.*;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import com.sansheng.testcenter.demo.mode.Content;
import com.sansheng.testcenter.demo.mode.Meter;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class EquipmentProvider extends ContentProvider {

    private static boolean DEBUG = true;
    private static final String TAG = "EquipmentProvider";
    private static Context mContext;
    private static String DATABASE_NAME = "Equipment.db";
    private static final Object sDatabaseLock = new Object();
    private static SQLiteDatabase mDatabase;
    private static UriMatcher mURIMatcher;
    private static final int BASE_SHIFT = 12;

    private static final int METER_BASE = 0;
    private static final int METER_ID = METER_BASE + 1;

    private static final SparseArray<String> TABLE_NAMES;
    private static final Uri LIST_NOTIFY_URI = Meter.CONTENT_URI;


    static {
        SparseArray<String> array = new SparseArray<String>(4);
        array.put(METER_BASE >> BASE_SHIFT, Meter.TABLE_NAME);
        TABLE_NAMES = array;
    }

    private static SQLiteDatabase getDatabase(Context context) {
        synchronized (sDatabaseLock) {
            // Always return the cached database, if we've got one
            if (mDatabase != null) {
                return mDatabase;
            }
            DBHelper helper = new DBHelper(context, DATABASE_NAME);
            mDatabase = helper.getWritableDatabase();
            return mDatabase;
        }
    }

    @Override
    public boolean onCreate() {
        //init database
        mContext = getContext();
        Meter.init(mContext);
        getDatabase(mContext);
        init();
        return false;
    }

    private void init() {
        mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        UriMatchInit();
    }

    private void UriMatchInit() {
        mURIMatcher.addURI(Content.AUTHORITY, "meter", METER_BASE);
        mURIMatcher.addURI(Content.AUTHORITY, "meter/#", METER_ID);
    }

    private static int findMatch(Uri uri, String methodName) {
        int match = mURIMatcher.match(uri);
        if (match < 0) {
            throw new IllegalArgumentException("Unknown uri: " + uri);
        } else if (DEBUG) {
            Log.v(TAG, methodName + ": uri=" + uri + ", match is " + match);
        }
        return match;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        int match = findMatch(uri, "query");
        Context context = getContext();
        SQLiteDatabase db = getDatabase(context);
        int table = match >> BASE_SHIFT;
        String tableName = TABLE_NAMES.valueAt(table);
        String limit = uri.getQueryParameter(Content.PARAMETER_LIMIT);
        String id = uri.getQueryParameter(Content.PARAMETER_IDS);
        if (id != null) {
            selection = whereWithId(id, selection);
        }
        Cursor cursor = null;
        try {
            switch (match) {
                case METER_BASE:
                case METER_ID:
                    String sql = buildMeterQuery(projection, selection, sortOrder);
                    cursor = db.rawQuery(sql, selectionArgs);
                    break;
                default:
                    cursor = db.query(tableName, projection, selection, selectionArgs, null, null, sortOrder, limit);
                    break;
            }
        } catch (Exception e) {
            return null;
        }
        if ((cursor != null) && !isTemporary()) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int match = findMatch(uri, "insert");
        Context context = getContext();
        SQLiteDatabase db = getDatabase(context);
        int table = match >> BASE_SHIFT;
        String tableName = TABLE_NAMES.valueAt(table);
        long id = -1;
        Uri result = null;
        Uri contentUri = Meter.CONTENT_URI;
        try {
            switch (match) {
                case METER_BASE:
                case METER_ID:
                    contentUri = Meter.CONTENT_URI;
                    break;
            }
            switch (match) {
                case METER_BASE:
                case METER_ID:
                    id = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    break;
                default:
                    id = db.insert(tableName, null, values);
            }
            result = ContentUris.withAppendedId(contentUri, id);
        } catch (Exception e) {
            return null;
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = findMatch(uri, "delete");
        Context context = getContext();
        // See the comment at delete(), above
        SQLiteDatabase db = getDatabase(context);
        int table = match >> BASE_SHIFT;
        String tableName = TABLE_NAMES.valueAt(table);
        String id = uri.getQueryParameter(Content.PARAMETER_IDS);
        int result = 0;
        if (id != null) {
            selection = whereWithId(id, selection);
        }
        try {
            switch (match) {
                case METER_BASE:
                case METER_ID:
                    break;
            }
            result = db.delete(tableName, selection, selectionArgs);
        } catch (Exception e) {
            Log.e(TAG, "delete error", e);
            return 0;
        }
        return result;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int match = findMatch(uri, "update");
        Context context = getContext();
        // See the comment at delete(), above
        SQLiteDatabase db = getDatabase(context);
        int table = match >> BASE_SHIFT;
        String tableName = TABLE_NAMES.valueAt(table);
        int result = 0;
        try {
            switch (match) {
                case METER_BASE:
                    break;
                case METER_ID:
                    String id = uri.getLastPathSegment();
                    selection = whereWithId(id, selection);
                    break;
            }
            result = db.update(tableName, values, selection, selectionArgs);
        } catch (Exception e) {
            return 0;
        }
        return result;
    }

    @Override
    public String getType(Uri uri) {
        int match = findMatch(uri, "getType");
        switch (match) {
            case METER_BASE:
                return "vnd.android.cursor.dir/meter";
            case METER_ID:
                return "vnd.android.cursor.dir/meter/#";
            default:
                return null;
        }
    }

    //query
    private String buildMeterQuery(String[] projection, String selection, String sortOrder) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ");
        builder.append(Meter.TABLE_NAME).append(" WHERE ").append(selection);
        if (!TextUtils.isEmpty(sortOrder)) {
            builder.append(" ORDER BY ");
            builder.append(sortOrder);
        }
        return builder.toString();
    }

    private Cursor reverseCursor(Cursor data) {
        if (data == null) {
            return null;
        }
        MatrixCursor cursor = new MatrixCursor(Meter.CONTENT_PROJECTION);
        data.moveToLast();
        for (int i = data.getCount(); i > 0; i--) {
            cursor.addRow(getCicleContent(data));
            data.moveToPrevious();
        }
        return cursor;
    }

    private Object[] getCicleContent(Cursor row) {
        return new Object[]{
                row.getLong(Meter.ID_INDEX),
                row.getInt(Meter.METER_ID_INDEX),
                row.getString(Meter.METER_NAME_INDEX),
                row.getLong(Meter.VALUE_TIME_INDEX),
                row.getLong(Meter.READ_TIME_INDEX),
                row.getInt(Meter.DATA_TYPE_INDEX),
                row.getString(Meter.VALZ_INDEX),
                row.getInt(Meter.IS_IMPORTANT_INDEX),
        };
    }

    private static String whereWithId(String id, String selection) {
        StringBuilder sb = new StringBuilder(256);
        sb.append("_id in(");
        sb.append(id);
        sb.append(") ");
        if (selection != null) {
            sb.append(" AND (");
            sb.append(selection);
            sb.append(')');
        }
        return sb.toString();
    }

    /**
     * called when all accounts deleted
     */
    public static void deleteEquipmentData() {
        EquipmentPreference.getPreferences(mContext).deleteCirclePreference();
        DBHelper.deleteData(getDatabase(mContext));
    }
}
