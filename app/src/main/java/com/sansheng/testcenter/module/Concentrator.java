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
//TODO:需要确认集中器的数据库结构，重写此类
public class Concentrator extends Content implements Content.ConcentratorColumns, Parcelable {

    public String mConcentratorName;
    public String mConcentratorAddress;


    public static final int ID_INDEX = 0;
    public static final int CONCENTRATOR_NUM_INDEX = ID_INDEX + 3;
    public static final int CONCENTRATOR_ADDRESS_INDEX = ID_INDEX + 4;
    public static final int CONCENTRATOR_NAME_INDEX = ID_INDEX + 5;


    public static final String[] CONTENT_PROJECTION = {
            ID, CONCENTRATOR_NAME, CONCENTRATOR_ADDRESS};

    public static final int[] ID_INDEX_PROJECTION = {
            ID_INDEX,CONCENTRATOR_NAME_INDEX, CONCENTRATOR_ADDRESS_INDEX};


    public static final String TABLE_NAME = "concentrator";

    public static Uri CONTENT_URI;

    public Concentrator() {
        mBaseUri = CONTENT_URI;
    }

    public static void init() {
        CONTENT_URI = Uri.parse(Content.BASE_CONTENT_URI + "/concentrator");
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(ID, mId);
        values.put(CONCENTRATOR_NAME, mConcentratorName);
        values.put(CONCENTRATOR_ADDRESS, mConcentratorAddress);
        return values;
    }

    @Override
    public void restore(Cursor cursor) {
        mId = cursor.getLong(ID_INDEX);
        mConcentratorName = cursor.getString(CONCENTRATOR_NAME_INDEX);
        mConcentratorAddress = cursor.getString(CONCENTRATOR_ADDRESS_INDEX);
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
        dest.writeString(mConcentratorName);
        dest.writeString(mConcentratorAddress);
    }

    public static final Creator<Concentrator> CREATOR
            = new Creator<Concentrator>() {
        @Override
        public Concentrator createFromParcel(Parcel in) {
            return new Concentrator(in);
        }

        @Override
        public Concentrator[] newArray(int size) {
            return new Concentrator[size];
        }
    };

    public Concentrator(Parcel in) {
        mId = in.readLong();
        mConcentratorName = in.readString();
        mConcentratorAddress = in.readString();
    }

    public String toString() {
        return "[" + ID + " : " + mId + "]" + "\n" +
                "[" + CONCENTRATOR_NAME + " : " + mConcentratorName + "]" + "\n";
    }
}
