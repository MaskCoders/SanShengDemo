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
public class Collect extends Content implements Content.CollectColumns, Parcelable {

    public String mCommonAddress;
    public String mCollectName;
    public String mPassword;
    public int mChannelType;
    public String mTerminalIp;
    public int mTerminalPort;
    public int mBaudRateId;

    public static final int ID_INDEX = 0;
    public static final int COMM_ADDRESS_INDEX = ID_INDEX + 1;
    public static final int COLLECT_NAME_INDEX = ID_INDEX + 2;
    public static final int PASSWORD_INDEX = ID_INDEX + 3;
    public static final int CHANNEL_TYPE_INDEX = ID_INDEX + 4;
    public static final int TERMINAL_IP_INDEX = ID_INDEX + 5;
    public static final int TERMINAL_PORT_INDEX = ID_INDEX + 6;
    public static final int BAUDRATE_ID_INDEX = ID_INDEX + 7;


    public static final String[] CONTENT_PROJECTION = {
            ID, COMM_ADDRESS, COLLECT_NAME, PASSWORD, CHANNEL_TYPE, TERMINAL_IP,TERMINAL_PORT, BAUDRATE_ID};

    public static final int[] ID_INDEX_PROJECTION = {
            ID_INDEX, COMM_ADDRESS_INDEX, COLLECT_NAME_INDEX, PASSWORD_INDEX, CHANNEL_TYPE_INDEX, TERMINAL_IP_INDEX,
            TERMINAL_PORT_INDEX, BAUDRATE_ID_INDEX};


    public static final String TABLE_NAME = "collect";

    public static void init() {
        CONTENT_URI = Uri.parse(Content.CONTENT_URI + "/collect");
        mBaseUri = CONTENT_URI;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(COMM_ADDRESS, mCommonAddress);
        values.put(COLLECT_NAME, mCollectName);
        values.put(PASSWORD, mPassword);
        values.put(CHANNEL_TYPE, mChannelType);
        values.put(TERMINAL_IP, mTerminalIp);
        values.put(TERMINAL_PORT, mTerminalPort);
        values.put(BAUDRATE_ID, mBaudRateId);
        return values;
    }

    @Override
    public void restore(Cursor cursor) {
        mId = cursor.getLong(ID_INDEX);
        mCommonAddress = cursor.getString(COMM_ADDRESS_INDEX);
        mCollectName = cursor.getString(COLLECT_NAME_INDEX);
        mPassword = cursor.getString(PASSWORD_INDEX);
        mChannelType = cursor.getInt(CHANNEL_TYPE_INDEX);
        mTerminalIp = cursor.getString(TERMINAL_IP_INDEX);
        mTerminalPort = cursor.getInt(TERMINAL_PORT_INDEX);
        mBaudRateId = cursor.getInt(BAUDRATE_ID_INDEX);
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
        dest.writeString(mCommonAddress);
        dest.writeString(mCollectName);
        dest.writeString(mPassword);
        dest.writeInt(mChannelType);
        dest.writeString(mTerminalIp);
        dest.writeInt(mTerminalPort);
        dest.writeInt(mBaudRateId);
    }

    public static final Creator<Collect> CREATOR
            = new Creator<Collect>() {
        @Override
        public Collect createFromParcel(Parcel in) {
            return new Collect(in);
        }

        @Override
        public Collect[] newArray(int size) {
            return new Collect[size];
        }
    };

    public Collect(Parcel in) {
        mId = in.readLong();
        mCommonAddress = in.readString();
        mCollectName = in.readString();
        mPassword = in.readString();
        mChannelType = in.readInt();
        mTerminalIp = in.readString();
        mTerminalPort = in.readInt();
        mBaudRateId = in.readInt();
    }

    public String toString() {
        return  "[" + ID + " : " + mId + "]" + "\n" +
                "[" + COLLECT_NAME + " : " + mCollectName + "]" + "\n";
    }
}
