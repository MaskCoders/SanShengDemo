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

    public int mCollectId;
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
    public int mImportant;
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
    public static final int IMPORTANT_INDEX = ID_INDEX + 18;
    public static final int NOTE_INDEX = ID_INDEX + 19;


    public static final String[] CONTENT_PROJECTION = {
            ID, COLLECT_ID, DA, METER_NAME, METER_NUM, METER_ADDRESS,COMMON_PASSWORD, BAUDRATE_ID,COMMON_PORT_ID,
            PROTOCOL_ID, FEILV_ID, GATHER_ADDRESS, WEISHU_ID, USER_SMALL_TYPE_ID, USER_TYPE_ID, USER_NUM,
            USER_ADDRESS, GROUP_ID, IMPORTANT, NOTE};

    public static final int[] ID_INDEX_PROJECTION = {
            ID_INDEX, COLLECT_ID_INDEX, DA_INDEX, METER_NUM_INDEX, METER_ADDRESS_INDEX, METER_NAME_INDEX,
            COMMON_PASSWORD_INDEX, BAUDRATE_ID_INDEX, COMMON_PORT_ID_INDEX, PROTOCOL_ID_INDEX, FEILV_ID_INDEX,
            GATHER_ADDRESS_INDEX, WEISHU_ID_INDEX, USER_SMALL_TYPE_ID_INDEX, USER_TYPE_ID_INDEX,
            USER_NUM_INDEX, USER_ADDRESS_INDEX, GROUP_ID_INDEX, IMPORTANT_INDEX, NOTE_INDEX};


    public static final String TABLE_NAME = "meter";

    public static void init() {
        CONTENT_URI = Uri.parse(Content.CONTENT_URI + "/meter");
        mBaseUri = CONTENT_URI;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ID, mCollectId);
        values.put(COLLECT_ID, mDa);
        values.put(DA, mMeterName);
        values.put(METER_NAME, mMeterNum);
        values.put(METER_NUM, mMeterAddress);
        values.put(METER_ADDRESS, mCommPwd);
        values.put(COMMON_PASSWORD, mBaudRateId);
        values.put(BAUDRATE_ID, mCommPortId);
        values.put(COMMON_PORT_ID, mProtocolId);
        values.put(PROTOCOL_ID, FeiLvId);
        values.put(FEILV_ID, mGatherAddress);
        values.put(GATHER_ADDRESS, mWeiShuId);
        values.put(WEISHU_ID, mUserSmallTypeId);
        values.put(USER_SMALL_TYPE_ID, mUserTypeId);
        values.put(USER_TYPE_ID, mUserNum);
        values.put(USER_NUM, mUserAddress);
        values.put(USER_ADDRESS, mGroupId);
        values.put(GROUP_ID, mImportant);
        values.put(IMPORTANT, mNote);
        values.put(NOTE, mNote);
        return values;
    }

    @Override
    public void restore(Cursor cursor) {
        mId = cursor.getLong(ID_INDEX);
        mCollectId = cursor.getInt(COLLECT_ID_INDEX);
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
        mImportant = cursor.getInt(IMPORTANT_INDEX);
        mNote = cursor.getString(NOTE_INDEX);
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
        dest.writeInt(mCollectId);
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
        dest.writeInt(mImportant);
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
        mCollectId = in.readInt();
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
        mImportant = in.readInt();
        mNote = in.readString();
    }

    public String toString() {
        return  "[" + ID + " : " + mId + "]" + "\n" +
                "[" + METER_NAME + " : " + mMeterName + "]" + "\n";
    }
}
