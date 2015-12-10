package com.example.demo.mode;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import com.example.demo.mode.Content.MeterColumns;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class Meter extends Content implements MeterColumns {

    public int mMeterID;
    public String mMeterName;
    public long mValueTime;
    public long mReadTime;
    public int mDataType;
    public float mValz;
    public boolean isImportant;//1=true|0=false
    public long mUpdateTime;

    public static final int ID_INDEX = 0;
    public static final int METER_ID_INDEX = ID_INDEX + 1;
    public static final int METER_NAME_INDEX = ID_INDEX + 2;
    public static final int VALUE_TIME_INDEX = ID_INDEX + 3;
    public static final int READ_TIME_INDEX = ID_INDEX + 4;
    public static final int DATA_TYPE_INDEX = ID_INDEX + 5;
    public static final int VALZ_INDEX = ID_INDEX + 6;
    public static final int IS_IMPORTANT_INDEX = ID_INDEX + 7;
    public static final int UPDATE_TIME_INDEX = ID_INDEX + 8;

    public static final String[] CONTENT_PROJECTION = {
            ID, METER_ID, METER_NAME, VALUE_TIME, READ_TIME, DATA_TYPE, VALZ, IMPORTANT, UPDATE_TIME};

    public static final int[] ID_INDEX_PROJECTION = {
            ID_INDEX, METER_ID_INDEX, METER_NAME_INDEX, VALUE_TIME_INDEX,
            READ_TIME_INDEX, DATA_TYPE_INDEX, VALZ_INDEX, IS_IMPORTANT_INDEX, UPDATE_TIME_INDEX};

    public static final String TABLE_NAME = "meter";

    public static void init() {
        CONTENT_URI = Uri.parse(Content.CONTENT_URI + "/meter");
        mBaseUri = CONTENT_URI;
    }

    public Meter() {
    }

    public Meter(boolean test, int id) {
        if (test) {
            mMeterID = id;
            mMeterName = "测试" + id;
            mValueTime = System.currentTimeMillis();
            mReadTime = mValueTime + 1;
            mDataType = id % 2 == 0 ? 1 : 2;
            mValz = (float) (1.234 + id);
            isImportant = id % 2 == 0;
            mUpdateTime = System.currentTimeMillis();
        }
    }

    public static Meter restoreMessageWithId(Context context, long id) {
        return Content.restoreContentWithId(context, Meter.class,
                Meter.CONTENT_URI, Meter.CONTENT_PROJECTION, id);
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(MeterColumns.METER_ID, mMeterID);
        values.put(MeterColumns.METER_NAME, mMeterName);
        values.put(MeterColumns.VALUE_TIME, mValueTime);
        values.put(MeterColumns.READ_TIME, mReadTime);
        values.put(MeterColumns.DATA_TYPE, mDataType);
        values.put(MeterColumns.VALZ, mValz);
        values.put(MeterColumns.IMPORTANT, isImportant ? 1 : 0);
        values.put(MeterColumns.UPDATE_TIME, mUpdateTime);
        return values;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void restore(Cursor cursor) {
        mId = cursor.getInt(ID_INDEX);
        mMeterID = cursor.getInt(METER_ID_INDEX);
        mMeterName = cursor.getString(METER_NAME_INDEX);
        mValueTime = cursor.getLong(VALUE_TIME_INDEX);
        mReadTime = cursor.getLong(READ_TIME_INDEX);
        mDataType = cursor.getInt(DATA_TYPE_INDEX);
        mValz = cursor.getFloat(VALZ_INDEX);
        isImportant = cursor.getInt(IS_IMPORTANT_INDEX) == 1;
    }

    @Override
    public Uri save(Context context) {
        return super.save(context);
    }
}