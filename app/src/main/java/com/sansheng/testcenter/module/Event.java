package com.sansheng.testcenter.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.sansheng.testcenter.utils.MeterUtilies;

/**
 * Created by sunshaogang on 12/17/15.
 */
public class Event extends Content implements Content.EventColumns, Parcelable {

    public int mCollectId;
    public String mHappenTime;
    public int mType;
    public int mPm;
    public int mFlag;
    public String mNote;
    public int mCount;

    public static final int ID_INDEX = 0;
    public static final int COLLECT_ID_INDEX = ID_INDEX + 1;
    public static final int HEPPEN_TIME_INDEX = ID_INDEX + 2;
    public static final int TYPE_INDEX = ID_INDEX + 3;
    public static final int PM_INDEX = ID_INDEX + 4;
    public static final int FLAG_INDEX = ID_INDEX + 5;
    public static final int NOTE_INDEX = ID_INDEX + 6;

    public static final String[] CONTENT_PROJECTION = {
            ID, COLLECT_ID, HEPPEN_TIME, TYPE, PM, FLAG, NOTE};

    public static final int[] ID_INDEX_PROJECTION = {
            ID_INDEX, COLLECT_ID_INDEX, HEPPEN_TIME_INDEX, TYPE_INDEX, PM_INDEX, FLAG_INDEX, NOTE_INDEX};


    public static final String TABLE_NAME = "event";
    public static Uri CONTENT_URI;

    public Event() {
        mBaseUri = CONTENT_URI;
    }

    public static void init() {
        CONTENT_URI = Uri.parse(Content.BASE_CONTENT_URI + "/event");
    }

    public Event(boolean test, int id) {
        mBaseUri = CONTENT_URI;
        if (test) {
            mCollectId = id % 10;
            mHappenTime = MeterUtilies.getSanShengTime(System.currentTimeMillis());
            mType = id % 3;
            mPm = id;
            mNote = " this is an example == " + id;
            Log.e("ssg", "mCollectId = " + mCollectId);
        }
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(COLLECT_ID, mCollectId);
        values.put(HEPPEN_TIME, mHappenTime);
        values.put(TYPE, mType);
        values.put(PM, mPm);
        values.put(FLAG, mFlag);
        values.put(NOTE, mNote);
        return values;
    }

    @Override
    public void restore(Cursor cursor) {
        mId = cursor.getLong(ID_INDEX);
        mCollectId = cursor.getInt(COLLECT_ID_INDEX);
        mHappenTime = cursor.getString(HEPPEN_TIME_INDEX);
        mType = cursor.getInt(TYPE_INDEX);
        mPm = cursor.getInt(PM_INDEX);
        mFlag = cursor.getInt(FLAG_INDEX);
        mNote = cursor.getString(NOTE_INDEX);
        if (cursor.getColumnIndex(EventColumns.COUNT) > -1) {
            mCount = cursor.getInt(cursor.getColumnIndex(EventColumns.COUNT));
        }
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
        dest.writeString(mHappenTime);
        dest.writeInt(mType);
        dest.writeInt(mPm);
        dest.writeInt(mFlag);
        dest.writeString(mNote);
    }

    public static final Creator<Event> CREATOR
            = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public Event(Parcel in) {
        mId = in.readLong();
        mCollectId = in.readInt();
        mHappenTime = in.readString();
        mType = in.readInt();
        mPm = in.readInt();
        mFlag = in.readInt();
        mNote = in.readString();
    }

    public String toString() {
        return "[" + ID + " : " + mId + "]" + "\n" +
                "[" + NOTE + " : " + mNote + "]" + "\n";
    }
}
