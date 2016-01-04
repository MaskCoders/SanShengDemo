package com.sansheng.testcenter.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sunshaogang on 12/17/15.
 */
public class Meter extends Content implements Content.MeterColumns, Parcelable {

    public int mConcentratorId;
    public int mDa;//作用未知
    public String mMeterName;
    public int mMeterNum;
    public String mMeterAddress;
    public int mCommPwd;
    public int mBaudRateId;
    public int mCommPortId;
    public int mProtocolId;
    public int FeiLvId;
    public String mGatherAddress;
    public int mWeiShuId;
    public int mUserSmallTypeId;
    public int mUserTypeId;
    public String mUserNum;
    public String mUserAddress;
    public int mGroupId;
    public String mNote;

    public static final int ID_INDEX = 0;
    public static final int COLLECT_ID_INDEX = ID_INDEX + 1;
    public static final int DA_INDEX = ID_INDEX + 2;
    public static final int METER_NUM_INDEX = ID_INDEX + 3;
    public static final int METER_ADDRESS_INDEX = ID_INDEX + 4;
    public static final int METER_NAME_INDEX = ID_INDEX + 5;
    public static final int COMMON_PASSWORD_INDEX = ID_INDEX + 6;
    public static final int BAUDRATE_ID_INDEX = ID_INDEX + 7;
    public static final int COMMON_PORT_ID_INDEX = ID_INDEX + 8;
    public static final int PROTOCOL_ID_INDEX = ID_INDEX + 9;
    public static final int FEILV_ID_INDEX = ID_INDEX + 10;
    public static final int GATHER_ADDRESS_INDEX = ID_INDEX + 11;
    public static final int WEISHU_ID_INDEX = ID_INDEX + 12;
    public static final int USER_SMALL_TYPE_ID_INDEX = ID_INDEX + 13;
    public static final int USER_TYPE_ID_INDEX = ID_INDEX + 14;
    public static final int USER_NUM_INDEX = ID_INDEX + 15;
    public static final int USER_ADDRESS_INDEX = ID_INDEX + 16;
    public static final int GROUP_ID_INDEX = ID_INDEX + 17;
    public static final int NOTE_INDEX = ID_INDEX + 18;


    public static final String[] CONTENT_PROJECTION = {
            ID, COLLECT_ID, DA, METER_NAME, METER_NUM, METER_ADDRESS,COMMON_PASSWORD, BAUDRATE_ID,COMMON_PORT_ID,
            PROTOCOL_ID, FEILV_ID, GATHER_ADDRESS, WEISHU_ID, USER_SMALL_TYPE_ID, USER_TYPE_ID, USER_NUM,
            USER_ADDRESS, GROUP_ID, NOTE};

    public static final int[] ID_INDEX_PROJECTION = {
            ID_INDEX, COLLECT_ID_INDEX, DA_INDEX, METER_NUM_INDEX, METER_ADDRESS_INDEX, METER_NAME_INDEX,
            COMMON_PASSWORD_INDEX, BAUDRATE_ID_INDEX, COMMON_PORT_ID_INDEX, PROTOCOL_ID_INDEX, FEILV_ID_INDEX,
            GATHER_ADDRESS_INDEX, WEISHU_ID_INDEX, USER_SMALL_TYPE_ID_INDEX, USER_TYPE_ID_INDEX,
            USER_NUM_INDEX, USER_ADDRESS_INDEX, GROUP_ID_INDEX, NOTE_INDEX};


    public static final String TABLE_NAME = "meter";

    public static Uri CONTENT_URI;

    public Meter() {
        mBaseUri = CONTENT_URI;
    }

    public Meter(boolean test, int id) {
        mBaseUri = CONTENT_URI;
        if (test) {
            mConcentratorId = id % 2;
            mMeterName = "电表名称" + id;
            mMeterAddress = "电表地址" + id;
            mMeterNum = id;
            mDa = id % 2;
        }
    }

    public static void init() {
        CONTENT_URI = Uri.parse(Content.BASE_CONTENT_URI + "/meter");
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        // Assign values for each row.
        values.put(COLLECT_ID, mConcentratorId);
        values.put(DA, mDa);
        values.put(METER_NAME, mMeterName);
        values.put(METER_NUM, mMeterNum);
        values.put(METER_ADDRESS, mMeterAddress);
        values.put(COMMON_PASSWORD, mCommPwd);
        values.put(BAUDRATE_ID, mBaudRateId);
        values.put(COMMON_PORT_ID, mCommPortId);
        values.put(PROTOCOL_ID, mProtocolId);
        values.put(FEILV_ID, FeiLvId);
        values.put(GATHER_ADDRESS, mGatherAddress);
        values.put(WEISHU_ID, mWeiShuId);
        values.put(USER_SMALL_TYPE_ID, mUserSmallTypeId);
        values.put(USER_TYPE_ID, mUserTypeId);
        values.put(USER_NUM, mUserNum);
        values.put(USER_ADDRESS, mUserAddress);
        values.put(GROUP_ID, mGroupId);
        values.put(NOTE, mNote);
        return values;
    }

    @Override
    public void restore(Cursor cursor) {
        mId = cursor.getLong(ID_INDEX);
        mConcentratorId = cursor.getInt(COLLECT_ID_INDEX);
        mDa = cursor.getInt(DA_INDEX);
        mMeterName = cursor.getString(METER_NUM_INDEX);
        mMeterNum = cursor.getInt(METER_ADDRESS_INDEX);
        mMeterAddress = cursor.getString(METER_NAME_INDEX);
        mCommPwd = cursor.getInt(COMMON_PASSWORD_INDEX);
        mBaudRateId = cursor.getInt(BAUDRATE_ID_INDEX);
        mCommPortId = cursor.getInt(COMMON_PORT_ID_INDEX);
        mProtocolId = cursor.getInt(PROTOCOL_ID_INDEX);
        FeiLvId = cursor.getInt(FEILV_ID_INDEX);
        mGatherAddress = cursor.getString(GATHER_ADDRESS_INDEX);
        mWeiShuId = cursor.getInt(WEISHU_ID_INDEX);
        mUserSmallTypeId = cursor.getInt(USER_SMALL_TYPE_ID_INDEX);
        mUserTypeId = cursor.getInt(USER_TYPE_ID_INDEX);
        mUserNum = cursor.getString(USER_NUM_INDEX);
        mUserAddress = cursor.getString(USER_ADDRESS_INDEX);
        mGroupId = cursor.getInt(GROUP_ID_INDEX);
        mNote = cursor.getString(NOTE_INDEX);
    }

    public void restoreWithMeterDataCursor(Cursor cursor){
        mId = cursor.getLong(MeterData.METER_ID_INDEX);
        mConcentratorId = cursor.getInt(cursor.getColumnIndex(COLLECT_ID));
        mDa = cursor.getInt(cursor.getColumnIndex(DA));
        mMeterName = cursor.getString(cursor.getColumnIndex(METER_NAME));
        mMeterNum = cursor.getInt(cursor.getColumnIndex(METER_NUM));
        mMeterAddress = cursor.getString(cursor.getColumnIndex(METER_ADDRESS));
        mCommPwd = cursor.getInt(cursor.getColumnIndex(COMMON_PASSWORD));
        mBaudRateId = cursor.getInt(cursor.getColumnIndex(BAUDRATE_ID));
        mCommPortId = cursor.getInt(cursor.getColumnIndex(COMMON_PORT_ID));
        mProtocolId = cursor.getInt(cursor.getColumnIndex(PROTOCOL_ID));
        FeiLvId = cursor.getInt(cursor.getColumnIndex(FEILV_ID));
        mGatherAddress = cursor.getString(cursor.getColumnIndex(GATHER_ADDRESS));
        mWeiShuId = cursor.getInt(cursor.getColumnIndex(WEISHU_ID));
        mUserSmallTypeId = cursor.getInt(cursor.getColumnIndex(USER_SMALL_TYPE_ID));
        mUserTypeId = cursor.getInt(cursor.getColumnIndex(USER_TYPE_ID));
        mUserNum = cursor.getString(cursor.getColumnIndex(USER_NUM));
        mUserAddress = cursor.getString(cursor.getColumnIndex(USER_ADDRESS));
        mGroupId = cursor.getInt(cursor.getColumnIndex(GROUP_ID));
        mNote = cursor.getString(cursor.getColumnIndex(NOTE));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public Uri save(Context context) {
        return super.save(context);
    }

    public void update(Context context) {
        super.update(context, toContentValues());
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeInt(mConcentratorId);
        dest.writeInt(mDa);
        dest.writeString(mMeterName);
        dest.writeInt(mMeterNum);
        dest.writeString(mMeterAddress);
        dest.writeInt(mCommPwd);
        dest.writeInt(mBaudRateId);
        dest.writeInt(mCommPortId);
        dest.writeInt(mProtocolId);
        dest.writeInt(FeiLvId);
        dest.writeString(mGatherAddress);
        dest.writeInt(mWeiShuId);
        dest.writeInt(mUserSmallTypeId);
        dest.writeInt(mUserTypeId);
        dest.writeString(mUserNum);
        dest.writeString(mUserAddress);
        dest.writeInt(mGroupId);
        dest.writeString(mNote);
    }

    public static final Parcelable.Creator<Meter> CREATOR
            = new Parcelable.Creator<Meter>() {
        @Override
        public Meter createFromParcel(Parcel in) {
            return new Meter(in);
        }

        @Override
        public Meter[] newArray(int size) {
            return new Meter[size];
        }
    };

    public Meter(Parcel in) {
        mId = in.readLong();
        mConcentratorId = in.readInt();
        mDa = in.readInt();
        mMeterName = in.readString();
        mMeterNum = in.readInt();
        mMeterAddress = in.readString();
        mCommPwd = in.readInt();
        mBaudRateId = in.readInt();
        mCommPortId = in.readInt();
        mProtocolId = in.readInt();
        FeiLvId = in.readInt();
        mGatherAddress = in.readString();
        mWeiShuId = in.readInt();
        mUserSmallTypeId = in.readInt();
        mUserTypeId = in.readInt();
        mUserNum = in.readString();
        mUserAddress = in.readString();
        mGroupId = in.readInt();
        mNote = in.readString();
    }

    public Meter copy() {
        Meter meter = new Meter();
        meter.mId = mId;
        meter.mConcentratorId = mConcentratorId;
        meter.mDa = mDa;
        meter.mMeterName = mMeterName;
        meter.mMeterNum = mMeterNum;
        meter.mMeterAddress = mMeterAddress;
        meter.mCommPwd = mCommPwd;
        meter.mBaudRateId = mBaudRateId;
        meter.mCommPortId = mCommPortId;
        meter.mProtocolId = mProtocolId;
        meter.FeiLvId = FeiLvId;
        meter.mGatherAddress = mGatherAddress;
        meter.mWeiShuId = mWeiShuId;
        meter.mUserSmallTypeId = mUserSmallTypeId;
        meter.mUserTypeId = mUserTypeId;
        meter.mUserNum = mUserNum;
        meter.mUserAddress = mUserAddress;
        meter.mGroupId = mGroupId;
        meter.mNote = mNote;
        return meter;
    }
    public String toString() {
        return  "[" + ID + " : " + mId + "]" + "\n" +
                "[" + METER_NAME + " : " + mMeterName + "]" + "\n";
    }
}
