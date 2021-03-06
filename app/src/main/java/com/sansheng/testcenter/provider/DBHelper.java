package com.sansheng.testcenter.provider;

/**
 * Created by sunshaogang on 12/9/15.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.sansheng.testcenter.module.*;

public class DBHelper extends SQLiteOpenHelper {

    private Context mContext;
    private final static String TAG = "DBHelper";

    //1:初始化
    public static final int DB_VERSION = 2;

    private String createMeterTable = "CREATE TABLE IF NOT EXISTS `" + Meter.TABLE_NAME +"` (\n" +
            "  `"+ Content.MeterColumns.ID + "` integer primary key autoincrement,\n" +
            "  `"+ Content.MeterColumns.COLLECT_ID + "` integer default 0,\n" +
            "  `"+ Content.MeterColumns.DA + "` integer not null default 0,\n" +
            "  `"+ Content.MeterColumns.METER_NAME + "` text,\n" +
            "  `"+ Content.MeterColumns.METER_NUM + "` integer not null default 0,\n" +
            "  `"+ Content.MeterColumns.METER_ADDRESS + "` text,\n" +
            "  `"+ Content.MeterColumns.COMMON_PASSWORD + "` text,\n" +
            "  `"+ Content.MeterColumns.BAUDRATE_ID + "` integer not null default 0,\n" +
            "  `"+ Content.MeterColumns.COMMON_PORT_ID + "` text,\n" +
            "  `"+ Content.MeterColumns.PROTOCOL_ID + "` integer not null default 0,\n" +
            "  `"+ Content.MeterColumns.FEILV_ID + "` integer not null default 0,\n" +
            "  `"+ Content.MeterColumns.GATHER_ADDRESS + "` text,\n" +
            "  `"+ Content.MeterColumns.WEISHU_ID + "` integer not null default 0,\n" +
            "  `"+ Content.MeterColumns.USER_SMALL_TYPE_ID + "` integer not null default 0,\n" +
            "  `"+ Content.MeterColumns.USER_TYPE_ID + "` integer not null default 0,\n" +
            "  `"+ Content.MeterColumns.USER_NUM + "` text,\n" +
            "  `"+ Content.MeterColumns.USER_ADDRESS + "` text,\n" +
            "  `"+ Content.MeterColumns.GROUP_ID + "` integer not null default 0,\n" +
            "  `"+ Content.MeterColumns.NOTE + "` text,\n" +
            "  `"+ Content.MeterColumns.METER_TYPE + "` integer not null default 0,\n" +
            "  `"+ Content.MeterColumns.IMPORTANT + "` integer not null default 0\n" +
            ")";

    private String createMeterDataTable = "CREATE TABLE IF NOT EXISTS `" + MeterData.TABLE_NAME +"` (\n" +
            "  `"+ Content.MeterDataColumns.ID + "` integer primary key autoincrement,\n" +
            "  `"+ Content.MeterDataColumns.METER_ID + "` integer not null default 0,\n" +
            "  `"+ Content.MeterDataColumns.VALUE_TIME + "` bigInteger not null default 0,\n" +
            "  `"+ Content.MeterDataColumns.READ_TIME + "` bigInteger not null default 0,\n" +
            "  `"+ Content.MeterDataColumns.SAVE_TIME + "` bigInteger not null default 0,\n" +
            "  `"+ Content.MeterDataColumns.DATA_TYPE + "` integer not null default 0,\n" +
            "  `"+ Content.MeterDataColumns.DATA_ID + "` integer not null default 0,\n" +
            "  `"+ Content.MeterDataColumns.VALZ + "` text not null default 0,\n" +
            "  `"+ Content.MeterDataColumns.VAL1 + "` text not null default 0,\n" +
            "  `"+ Content.MeterDataColumns.VAL2 + "` text not null default 0,\n" +
            "  `"+ Content.MeterDataColumns.VAL3 + "` text not null default 0,\n" +
            "  `"+ Content.MeterDataColumns.VAL4 + "` text not null default 0,\n" +
            "  `"+ Content.MeterDataColumns.UPDATE_TIME + "` integer not null default 0\n" +
            ")";

    private String createConcentratorTable = "CREATE TABLE IF NOT EXISTS `" + Concentrator.TABLE_NAME +"` (\n" +
            "  `"+ Content.ConcentratorColumns.ID + "` integer primary key autoincrement,\n" +
            "  `"+ Content.ConcentratorColumns.CONCENTRATOR_NAME + "` text,\n" +
            "  `"+ Content.ConcentratorColumns.CONCENTRATOR_ADDRESS + "` text\n" +
            ")";

    private String createCollectTable = "CREATE TABLE IF NOT EXISTS `" + Collect.TABLE_NAME +"` (\n" +
            "  `"+ Content.CollectColumns.ID + "` integer primary key autoincrement,\n" +
            "  `"+ Content.CollectColumns.COMM_ADDRESS + "` text,\n" +
            "  `"+ Content.CollectColumns.COLLECT_NAME + "` text,\n" +
            "  `"+ Content.CollectColumns.PASSWORD + "` text,\n" +
            "  `"+ Content.CollectColumns.CHANNEL_TYPE + "` integer not null default 0,\n" +
            "  `"+ Content.CollectColumns.TERMINAL_IP + "` text,\n" +
            "  `"+ Content.CollectColumns.TERMINAL_PORT + "` text,\n" +
            "  `"+ Content.CollectColumns.BAUDRATE_ID + "` integer not null default 0\n" +
            ")";

    private String createCollectParamTable = "CREATE TABLE IF NOT EXISTS `" + CollectParam.TABLE_NAME +"` (\n" +
            "  `"+ Content.CollectParamColumns.ID + "` integer primary key autoincrement,\n" +
            "  `"+ Content.CollectParamColumns.COLLECT_ID + "` integer not null default 0,\n" +
            "  `"+ Content.CollectParamColumns.AFN + "` integer not null default 0,\n" +
            "  `"+ Content.CollectParamColumns.FN + "` integer not null default 0,\n" +
            "  `"+ Content.CollectParamColumns.PARAM + "` text\n" +
            ")";

    private String createExceptionTable = "CREATE TABLE IF NOT EXISTS `" + Event.TABLE_NAME +"` (\n" +
            "  `"+ Content.EventColumns.ID + "` integer primary key autoincrement,\n" +
            "  `"+ Content.EventColumns.COLLECT_ID + "` integer not null default 0,\n" +
            "  `"+ Content.EventColumns.HEPPEN_TIME + "` text,\n" +
            "  `"+ Content.EventColumns.TYPE + "` integer not null default 0,\n" +
            "  `"+ Content.EventColumns.PM + "` integer not null default 0,\n" +
            "  `"+ Content.EventColumns.FLAG + "` integer not null default 0,\n" +
            "  `"+ Content.EventColumns.NOTE + "` text\n" +
            ")";

    private String createLocationTable = "CREATE TABLE IF NOT EXISTS `" + LocationInfo.TABLE_NAME +"` (\n" +
            "  `"+ Content.LocationInfoColumns.ID + "` integer primary key autoincrement,\n" +
            "  `"+ Content.LocationInfoColumns.LOCTYPE + "` Integer not null default 0,\n" +
            "  `"+ Content.LocationInfoColumns.ADDRESS + "` text,\n" +
            "  `"+ Content.LocationInfoColumns.POI + "` text,\n" +
            "  `"+ Content.LocationInfoColumns.LATITUDE + "` text,\n" +
            "  `"+ Content.LocationInfoColumns.LONTITUDE + "` text,\n" +
            "  `"+ Content.LocationInfoColumns.UPDATE_TIME + "` text,\n" +
            "  `"+ Content.LocationInfoColumns.URI_LIST + "` text\n" +
            ")";

    private static final String UPDATE_TEST_TABLE_NAME = "update_test";
    private String updateTest = "CREATE TABLE IF NOT EXISTS `" + UPDATE_TEST_TABLE_NAME +"` (\n" +
            "  `"+ Content.LocationInfoColumns.ID + "` integer primary key autoincrement,\n" +
            "  `"+ Content.LocationInfoColumns.URI_LIST + "` text\n" +
            ")";

    public DBHelper(Context context, String name) {
        super(context, name, null, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createMeterDataTable);
        db.execSQL(createMeterTable);
        db.execSQL(createConcentratorTable);
        db.execSQL(createCollectTable);
        db.execSQL(createCollectParamTable);
        db.execSQL(createExceptionTable);
        db.execSQL(createLocationTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            Log.e("ssg", " onUpgrade oldVersion = " + oldVersion);
            db.execSQL(updateTest);
        }
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            super.onDowngrade(db, oldVersion, newVersion);
    }

    public static boolean deleteData(SQLiteDatabase db) {
        try {
            db.execSQL("DELETE FROM " + Meter.TABLE_NAME);
            db.execSQL("DELETE FROM " + MeterData.TABLE_NAME);
            db.execSQL("DELETE FROM " + Concentrator.TABLE_NAME);
            db.execSQL("DELETE FROM " + Collect.TABLE_NAME);
            db.execSQL("DELETE FROM " + CollectParam.TABLE_NAME);
            db.execSQL("DELETE FROM " + Event.TABLE_NAME);
            return true;
        } catch (Exception e) {
            Log.e("DbHelper", e.toString());
            return false;
        }
    }
}
