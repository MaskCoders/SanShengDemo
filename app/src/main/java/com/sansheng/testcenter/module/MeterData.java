package com.sansheng.testcenter.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.sansheng.testcenter.module.Content.MeterDataColumns;

/**
 * Created by sunshaogang on 12/9/15.
 */
public class MeterData extends Content implements MeterDataColumns, Parcelable {

    public int mMeterID;
    public long mValueTime;
    public long mReadTime;
    public long mSaveTime;
    public int mDataType;
    public int mDataId;
    public float mValz;
    public float mVal1;
    public float mVal2;
    public float mVal3;
    public float mVal4;
    public long mUpdateTime;

    public static final int ID_INDEX = 0;
    public static final int METER_ID_INDEX = ID_INDEX + 1;
    public static final int VALUE_TIME_INDEX = ID_INDEX + 2;
    public static final int READ_TIME_INDEX = ID_INDEX + 3;
    public static final int SAVE_TIME_INDEX = ID_INDEX + 4;
    public static final int DATA_TYPE_INDEX = ID_INDEX + 5;
    public static final int DATA_ID_INDEX = ID_INDEX + 6;
    public static final int VALZ_INDEX = ID_INDEX + 7;
    public static final int VAL1_INDEX = ID_INDEX + 8;
    public static final int VAL2_INDEX = ID_INDEX + 9;
    public static final int VAL3_INDEX = ID_INDEX + 10;
    public static final int VAL4_INDEX = ID_INDEX + 11;
    public static final int UPDATE_TIME_INDEX = ID_INDEX + 12;

    public static final String[] CONTENT_PROJECTION = {
            ID, METER_ID, VALUE_TIME, READ_TIME, SAVE_TIME, DATA_TYPE,
            DATA_ID, VALZ, VAL1, VAL2, VAL3, VAL4, UPDATE_TIME};

    public static final int[] ID_INDEX_PROJECTION = {
            ID_INDEX, METER_ID_INDEX, VALUE_TIME_INDEX, READ_TIME_INDEX, SAVE_TIME_INDEX,
            DATA_TYPE_INDEX, DATA_ID_INDEX, VALZ_INDEX, VAL1_INDEX, VAL2_INDEX, VAL3_INDEX, VAL4_INDEX, UPDATE_TIME_INDEX};

    public static final String TABLE_NAME = "meterdata";

    public static void init() {
        CONTENT_URI = Uri.parse(Content.BASE_CONTENT_URI + "/meterdata");
    }
    public static Uri CONTENT_URI;

    public MeterData() {
        mBaseUri = CONTENT_URI;
    }

    public MeterData(boolean test, int id) {
        mBaseUri = CONTENT_URI;
        if (test) {
            mMeterID = id;
            mValueTime = System.currentTimeMillis();
            mReadTime = mValueTime + 1;
            mDataType = id % 2 == 0 ? 1 : 2;
            mValz = (float) (1.234 + id);
            mUpdateTime = System.currentTimeMillis();
        }
    }

    public static MeterData restoreMessageWithId(Context context, long id) {
        return Content.restoreContentWithId(context, MeterData.class,
                MeterData.CONTENT_URI, MeterData.CONTENT_PROJECTION, id);
    }

    @Override
    public ContentValues toContentValues() {
        // Assign values for each row.
        ContentValues values = new ContentValues();
        values.put(METER_ID, mMeterID);
        values.put(VALUE_TIME, mValueTime);
        values.put(READ_TIME, mReadTime);
        values.put(SAVE_TIME, mSaveTime);
        values.put(DATA_TYPE, mDataType);
        values.put(DATA_ID, mDataId);
        values.put(VALZ, mValz);
        values.put(VAL1, mVal1);
        values.put(VAL2, mVal2);
        values.put(VAL3, mVal3);
        values.put(VAL4, mVal4);
        values.put(UPDATE_TIME, mUpdateTime);
        return values;
    }

    @Override
    public void restore(Cursor cursor) {
        mId = cursor.getInt(ID_INDEX);
        mMeterID = cursor.getInt(METER_ID_INDEX);
        mValueTime = cursor.getLong(VALUE_TIME_INDEX);
        mReadTime = cursor.getLong(READ_TIME_INDEX);
        mSaveTime = cursor.getLong(SAVE_TIME_INDEX);
        mDataType = cursor.getInt(DATA_TYPE_INDEX);
        mDataId = cursor.getInt(DATA_ID_INDEX);
        mValz = cursor.getFloat(VALZ_INDEX);
        mVal1 = cursor.getFloat(VAL1_INDEX);
        mVal2 = cursor.getFloat(VAL2_INDEX);
        mVal3 = cursor.getFloat(VAL3_INDEX);
        mVal4 = cursor.getFloat(VAL4_INDEX);
        mUpdateTime = cursor.getLong(UPDATE_TIME_INDEX);
    }

    @Override
    public Uri save(Context context) {
        return super.save(context);
    }

    public void update(Context context) {
        super.update(context, toContentValues());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeInt(mMeterID);
        dest.writeLong(mValueTime);
        dest.writeLong(mReadTime);
        dest.writeLong(mSaveTime);
        dest.writeInt(mDataType);
        dest.writeInt(mDataId);
        dest.writeFloat(mValz);
        dest.writeFloat(mVal1);
        dest.writeFloat(mVal2);
        dest.writeFloat(mVal3);
        dest.writeFloat(mVal4);
        dest.writeLong(mUpdateTime);
    }

    public static final Parcelable.Creator<MeterData> CREATOR
            = new Parcelable.Creator<MeterData>() {
        @Override
        public MeterData createFromParcel(Parcel in) {
            return new MeterData(in);
        }

        @Override
        public MeterData[] newArray(int size) {
            return new MeterData[size];
        }
    };

    public MeterData(Parcel in) {
        mId = in.readLong();
        mMeterID = in.readInt();
        mValueTime = in.readLong();
        mReadTime = in.readLong();
        mSaveTime = in.readLong();
        mDataType = in.readInt();
        mDataId = in.readInt();
        mValz = in.readFloat();
        mVal1 = in.readFloat();
        mVal2 = in.readFloat();
        mVal3 = in.readFloat();
        mVal4 = in.readFloat();
        mUpdateTime = in.readInt();
    }

    public MeterData copy() {
        MeterData meter = new MeterData();
        meter.mId = mId;
        meter.mMeterID = mMeterID;
        meter.mValueTime = mValueTime;
        meter.mReadTime = mReadTime;
        meter.mSaveTime = mSaveTime;
        meter.mDataType = mDataType;
        meter.mDataId = mDataId;
        meter.mValz = mValz;
        meter.mVal1 = mVal1;
        meter.mVal2 = mVal2;
        meter.mVal3 = mVal3;
        meter.mVal4 = mVal4;
        meter.mUpdateTime = mUpdateTime;
        return meter;
    }

    public String toString() {
        return  "[" + ID + " : " + mId + "]" + "\n" +
                "[" + METER_ID + " : " + mMeterID + "]" + "\n";
    }
}