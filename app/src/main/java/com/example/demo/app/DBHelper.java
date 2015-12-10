package com.example.demo.app;

/**
 * Created by sunshaogang on 12/9/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;
    private final static String TAG = "DBHelper";

    public static final int DB_VERSION = 1;

    private String createMsgTable = "CREATE TABLE IF NOT EXISTS `" + Meter.TABLE_NAME +"` (\n" +
            "  `"+ Content.MeterColumns.ID + "` integer primary key autoincrement,\n" +
            "  `"+ Content.MeterColumns.METER_ID + "` integer not null default 0,\n" +
            "  `"+ Content.MeterColumns.METER_NAME + "` text not null,\n" +
            "  `"+ Content.MeterColumns.VALUE_TIME + "` bigInteger not null default 0,\n" +
            "  `"+ Content.MeterColumns.READ_TIME + "` bigInteger not null default 0,\n" +
            "  `"+ Content.MeterColumns.DATA_TYPE + "` integer not null default 1,\n" +
            "  `"+ Content.MeterColumns.VALZ + "` text not null default 0,\n" +
            "  `"+ Content.MeterColumns.IMPORTANT + "` integer not null  default 0,\n" +
            "  `"+ Content.MeterColumns.UPDATE_TIME + "` integer not null default 0\n" +
            ")";

    public DBHelper(Context context, String name) {
        super(context, name, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createMsgTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            super.onDowngrade(db, oldVersion, newVersion);
    }

    public static boolean deleteData(SQLiteDatabase db) {
        try {
            db.execSQL("DELETE FROM " + Meter.TABLE_NAME);
            return true;
        } catch (Exception e) {
            Log.e("DbHelper", e.toString());
            return false;
        }
    }
}
