package com.sansheng.testcenter.provider;

import android.content.*;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import com.sansheng.testcenter.module.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class EquipmentProvider extends ContentProvider {

    private static boolean DEBUG = true;
    private static final String TAG = "EquipmentProvider";

    private static final String NOTIFICATION_OP_DELETE = "delete";
    private static final String NOTIFICATION_OP_INSERT = "insert";
    private static final String NOTIFICATION_OP_UPDATE = "update";

    private static Context mContext;
    private static String DATABASE_NAME = "Equipment.db";
    private static final Object sDatabaseLock = new Object();
    private static SQLiteDatabase mDatabase;
    private static UriMatcher mURIMatcher;
    private static final int BASE_SHIFT = 12;// 12 bits to the base type: 0, 0x1000, 0x2000, etc.
    //电表
    private static final int METER_BASE = 0;
    private static final int METER = METER_BASE;
    private static final int METER_ID = METER_BASE + 1;
    //抄表
    private static final int METERDATA_BASE = 0x1000;
    private static final int METERDATA = METERDATA_BASE;
    private static final int METERDATA_ID = METERDATA_BASE + 1;
    //concentrator 集中器
    private static final int CONCENTRATOR_BASE = 0x2000;
    private static final int CONCENTRATOR = CONCENTRATOR_BASE;
    private static final int CONCENTRATOR_ID = CONCENTRATOR_BASE + 1;
    //collect
    private static final int COLLECT_BASE = 0x3000;
    private static final int COLLECT = COLLECT_BASE;
    private static final int COLLECT_ID = COLLECT_BASE + 2;
    //collect param
    private static final int COLLECT_PARAM_BASE = 0x4000;
    private static final int COLLECT_PARAM = COLLECT_PARAM_BASE;
    private static final int COLLECT_PARAM_ID = COLLECT_PARAM_BASE + 2;
    //exception
    private static final int EXCEPTION_BASE = 0x5000;
    private static final int EXCEPTION = EXCEPTION_BASE;
    private static final int EXCEPTION_ID = EXCEPTION_BASE + 2;

    private static ExecutorService mNotifyThreadPool = Executors.newSingleThreadExecutor();

    private static final SparseArray<String> TABLE_NAMES;

    static {
        SparseArray<String> array = new SparseArray<String>(6);
        array.put(METER_BASE >> BASE_SHIFT, Meter.TABLE_NAME);
        array.put(METERDATA_BASE >> BASE_SHIFT, MeterData.TABLE_NAME);
        array.put(CONCENTRATOR_BASE >> BASE_SHIFT, Concentrator.TABLE_NAME);
        array.put(COLLECT_BASE >> BASE_SHIFT, Collect.TABLE_NAME);
        array.put(COLLECT_PARAM_BASE >> BASE_SHIFT, CollectParam.TABLE_NAME);
        array.put(EXCEPTION_BASE >> BASE_SHIFT, EquipmentException.TABLE_NAME);
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
        Content.init(mContext);
        getDatabase(mContext);
        init();
        return false;
    }

    private void init() {
        mURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        initUriMatch();
    }

    private void initUriMatch() {
        mURIMatcher.addURI(Content.AUTHORITY, "meter", METER);
        mURIMatcher.addURI(Content.AUTHORITY, "meter/#", METER_ID);
        mURIMatcher.addURI(Content.AUTHORITY, "meterdata", METERDATA);
        mURIMatcher.addURI(Content.AUTHORITY, "meterdata/#", METERDATA_ID);
        mURIMatcher.addURI(Content.AUTHORITY, "concentrator", CONCENTRATOR);
        mURIMatcher.addURI(Content.AUTHORITY, "concentrator/#", CONCENTRATOR_ID);
        mURIMatcher.addURI(Content.AUTHORITY, "collect", COLLECT);
        mURIMatcher.addURI(Content.AUTHORITY, "collect/#", COLLECT_ID);
        mURIMatcher.addURI(Content.AUTHORITY, "collectparam", COLLECT_PARAM);
        mURIMatcher.addURI(Content.AUTHORITY, "collectparam/#", COLLECT_PARAM_ID);
        mURIMatcher.addURI(Content.AUTHORITY, "except", EXCEPTION);
        mURIMatcher.addURI(Content.AUTHORITY, "except/#", EXCEPTION_ID);
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
        Log.e("ssg", "query uri = " + uri);
        int match = findMatch(uri, "query");
        Context context = getContext();
        SQLiteDatabase db = getDatabase(context);
        Log.e("ssg", "query match = " + match);
        int table = match >> BASE_SHIFT;
        Log.e("ssg", "query table = " + table);
        String tableName = TABLE_NAMES.valueAt(table);
        Log.e("ssg", "query tableName = " + tableName);
        String limit = uri.getQueryParameter(Content.PARAMETER_LIMIT);
        String id = uri.getQueryParameter(Content.PARAMETER_IDS);
        if (id != null) {
            selection = whereWithId(id, selection);
        }
        Cursor cursor = null;
        try {
            String sql;
            switch (match) {
                case METER:
                    sql = buildMeterQuery(tableName, projection, selection, sortOrder);
                    cursor = db.rawQuery(sql, selectionArgs);
                    break;
                case METER_ID:
                    break;
                case METERDATA:
                    sql = buildMeterQuery(tableName, projection, selection, sortOrder);
                    cursor = db.rawQuery(sql, selectionArgs);
                    break;
                case METERDATA_ID:
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
        Log.e("ssg", "insert uri = " + uri);
        int match = findMatch(uri, "insert");
        Context context = getContext();
        SQLiteDatabase db = getDatabase(context);
        int table = match >> BASE_SHIFT;
        String tableName = TABLE_NAMES.valueAt(table);
        long id = -1;
        Uri result = null;
        try {
            switch (match) {
                case METER:
                case METER_ID:
                    id = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    break;
                case METERDATA:
                case METERDATA_ID:
                    id = db.insertWithOnConflict(tableName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                    break;
                default:
                    id = db.insert(tableName, null, values);
            }
            result = ContentUris.withAppendedId(uri, id);
        } catch (Exception e) {
            return null;
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.e("ssg", "delete uri = " + uri);
        int match = findMatch(uri, "delete");
        Context context = getContext();
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
                case METER:
                case METER_ID:
                case METERDATA:
                case METERDATA_ID:
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
        Log.e("ssg", "update uri = " + uri);
        int match = findMatch(uri, "update");
        Context context = getContext();
        // See the comment at delete(), above
        SQLiteDatabase db = getDatabase(context);
        int table = match >> BASE_SHIFT;
        String tableName = TABLE_NAMES.valueAt(table);
        int result = 0;
        try {
            switch (match) {
                case METER:
                    break;
                case METERDATA:
                    break;
                case METER_ID:
                case METERDATA_ID:
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
            case METER:
                return "vnd.android.cursor.dir/meter";
            case METER_ID:
                return "vnd.android.cursor.dir/meter/#";
            case METERDATA:
                return "vnd.android.cursor.dir/meterdata";
            case METERDATA_ID:
                return "vnd.android.cursor.dir/meterdata/#";
            case CONCENTRATOR:
                return "vnd.android.cursor.dir/concentrator/";
            case CONCENTRATOR_ID:
                return "vnd.android.cursor.dir/concentrator/#";
            case COLLECT:
                return "vnd.android.cursor.dir/collect/";
            case COLLECT_ID:
                return "vnd.android.cursor.dir/collect/#";
            case COLLECT_PARAM:
                return "vnd.android.cursor.dir/collectparam/";
            case COLLECT_PARAM_ID:
                return "vnd.android.cursor.dir/collectparam/#";
            case EXCEPTION:
                return "vnd.android.cursor.dir/except/";
            case EXCEPTION_ID:
                return "vnd.android.cursor.dir/except/#";
            default:
                return null;
        }
    }

    //query
    private String buildMeterQuery(String table, String[] projection, String selection, String sortOrder) {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT * FROM ");
        builder.append(table).append(" WHERE ").append(selection);
        if (!TextUtils.isEmpty(sortOrder)) {
            builder.append(" ORDER BY ");
            builder.append(sortOrder);
        }
        return builder.toString();
    }

//    private Cursor reverseCursor(Cursor data) {
//        if (data == null) {
//            return null;
//        }
//        MatrixCursor cursor = new MatrixCursor(MeterData.CONTENT_PROJECTION);
//        data.moveToLast();
//        for (int i = data.getCount(); i > 0; i--) {
//            cursor.addRow(getContent(data));
//            data.moveToPrevious();
//        }
//        return cursor;
//    }
//
//    private Object[] getContent(Cursor row) {
//        return new Object[]{
//                row.getLong(MeterData.ID_INDEX),
//                row.getInt(MeterData.METER_ID_INDEX),
//                row.getString(MeterData.METER_NAME_INDEX),
//                row.getLong(MeterData.VALUE_TIME_INDEX),
//                row.getLong(MeterData.READ_TIME_INDEX),
//                row.getInt(MeterData.DATA_TYPE_INDEX),
//                row.getString(MeterData.VALZ_INDEX),
//                row.getInt(MeterData.IS_IMPORTANT_INDEX),
//        };
//    }

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
        EquipmentPreference.getPreferences(mContext).deletePreference();
        DBHelper.deleteData(getDatabase(mContext));
    }
}
