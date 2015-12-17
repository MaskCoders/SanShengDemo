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
public class CollectParam extends Content implements Content.CollectParamColumns, Parcelable {


    public int mCollectId;
    public int mAFn;
    public int mFn;
    public String mParam;

    public static final int ID_INDEX = 0;
    public static final int COLLECT_ID_INDEX = ID_INDEX + 1;
    public static final int AFN_INDEX = ID_INDEX + 2;
    public static final int FN_INDEX = ID_INDEX + 3;
    public static final int PARAM_INDEX = ID_INDEX + 4;

    public static final String ID = "_id";
    public static final String COLLECT_ID = "collectId";
    public static final String AFN = "afn";
    public static final String FN = "fn";
    public static final String PARAM = "param";
    public static final String[] CONTENT_PROJECTION = {
            ID, COLLECT_ID, AFN, FN, PARAM};

    public static final int[] ID_INDEX_PROJECTION = {
            ID_INDEX, COLLECT_ID_INDEX, AFN_INDEX, FN_INDEX, PARAM_INDEX};


    public static final String TABLE_NAME = "collectparam";

    public static void init() {
        CONTENT_URI = Uri.parse(Content.CONTENT_URI + "/collectparam");
        mBaseUri = CONTENT_URI;
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(COLLECT_ID, mCollectId);
        values.put(AFN, mAFn);
        values.put(FN, mFn);
        values.put(PARAM, mParam);
        return values;
    }

    @Override
    public void restore(Cursor cursor) {
        mId = cursor.getLong(ID_INDEX);
        mCollectId = cursor.getInt(COLLECT_ID_INDEX);
        mAFn = cursor.getInt(AFN_INDEX);
        mFn = cursor.getInt(FN_INDEX);
        mParam = cursor.getString(PARAM_INDEX);
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
        dest.writeInt(mAFn);
        dest.writeInt(mFn);
        dest.writeString(mParam);
    }

    public static final Creator<CollectParam> CREATOR
            = new Creator<CollectParam>() {
        @Override
        public CollectParam createFromParcel(Parcel in) {
            return new CollectParam(in);
        }

        @Override
        public CollectParam[] newArray(int size) {
            return new CollectParam[size];
        }
    };

    public CollectParam(Parcel in) {
        mId = in.readLong();
        mCollectId = in.readInt();
        mAFn = in.readInt();
        mFn = in.readInt();
        mParam = in.readString();
    }

    public String toString() {
        return  "[" + ID + " : " + mId + "]" + "\n" +
                "[" + PARAM + " : " + mParam + "]" + "\n";
    }
}
