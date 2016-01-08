package com.sansheng.testcenter.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.baidu.location.BDLocation;

import java.util.ArrayList;

/**
 * Created by sunshaogang on 1/7/16.
 */
public class LocationInfo extends Content implements Content.LocationInfoColumns, Parcelable {

    public int mLocType;
    public String mAddress;
    public String mPoi;
    public String mLatitude;
    public String mLontitude;
    public String mUpdateTime;
    public ArrayList<String> mUriList = new ArrayList<String>();

    public static final int ID_INDEX = 0;
    public static final int LOCTYPE_INDEX = ID_INDEX + 1;
    public static final int ADDRESS_INDEX = ID_INDEX + 2;
    public static final int POI_INDEX = ID_INDEX + 3;
    public static final int LATITUDE_INDEX = ID_INDEX + 4;
    public static final int LONTITUDE_INDEX = ID_INDEX + 5;
    public static final int UPDATE_TIME_INDEX = ID_INDEX + 6;
    public static final int URI_LIST_INDEX = ID_INDEX + 7;

    public static final String[] CONTENT_PROJECTION = {
            ID, LOCTYPE, ADDRESS, POI, LATITUDE, LONTITUDE, UPDATE_TIME, URI_LIST};

    public static final int[] ID_INDEX_PROJECTION = {
            ID_INDEX, LOCTYPE_INDEX, ADDRESS_INDEX, POI_INDEX, LATITUDE_INDEX, LONTITUDE_INDEX,
            UPDATE_TIME_INDEX, URI_LIST_INDEX};


    public static final String TABLE_NAME = "location";

    public static Uri CONTENT_URI;

    public LocationInfo() {
        mBaseUri = CONTENT_URI;
    }

    public void restoreFromLocation(BDLocation location) {
        mLocType = location.getLocType();
        mAddress = location.getAddrStr();
        mLatitude = String.valueOf(location.getLatitude());
        mLontitude = String.valueOf(location.getLongitude());
        mUpdateTime = location.getTime();
    }

    public static void init() {
        CONTENT_URI = Uri.parse(Content.BASE_CONTENT_URI + "/location");
    }

    @Override
    public ContentValues toContentValues() {
        ContentValues values = new ContentValues();
        values.put(LOCTYPE, mLocType);
        values.put(ADDRESS, mAddress);
        values.put(POI, mPoi);
        values.put(LATITUDE, mLatitude);
        values.put(LONTITUDE, mLontitude);
        values.put(UPDATE_TIME, mUpdateTime);
        values.put(URI_LIST, ModuleUtilites.listToJson(mUriList));
        return values;
    }

    @Override
    public void restore(Cursor cursor) {
        mId = cursor.getLong(ID_INDEX);
        mLocType = cursor.getInt(LOCTYPE_INDEX);
        mAddress = cursor.getString(ADDRESS_INDEX);
        mPoi = cursor.getString(POI_INDEX);
        mLatitude = cursor.getString(LATITUDE_INDEX);
        mLontitude = cursor.getString(LONTITUDE_INDEX);
        mUpdateTime = cursor.getString(UPDATE_TIME_INDEX);
        mUriList = ModuleUtilites.jsonToList(cursor.getString(URI_LIST_INDEX));
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
        dest.writeString(mBaseUri.toString());
        dest.writeLong(mId);
        dest.writeInt(mLocType);
        dest.writeString(mAddress);
        dest.writeString(mPoi);
        dest.writeString(mLatitude);
        dest.writeString(mLontitude);
        dest.writeString(mUpdateTime);
        dest.writeString(ModuleUtilites.listToJson(mUriList));
    }

    public static final Creator<LocationInfo> CREATOR
            = new Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel in) {
            return new LocationInfo(in);
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    public LocationInfo(Parcel in) {
        mBaseUri = Uri.parse(in.readString());
        mId = in.readLong();
        mLocType = in.readInt();
        mAddress = in.readString();
        mPoi = in.readString();
        mLatitude = in.readString();
        mLontitude = in.readString();
        mUpdateTime = in.readString();
        mUriList = ModuleUtilites.jsonToList(in.readString());
    }

    public String toString() {
        String result = "[" + ID + " : " + mId + "]" + "\n" +
                "[" + LOCTYPE + " : " + mLocType + "]" + "\n" +
                "[" + ADDRESS + " : " + mAddress + "]" + "\n" +
                "[" + POI + " : " + mPoi + "]" + "\n" +
                "[" + LATITUDE + " : " + mLatitude + "]" + "\n" +
                "[" + LONTITUDE + " : " + mLontitude + "]" + "\n" +
                "[" + UPDATE_TIME + " : " + mUpdateTime + "]" + "\n";
        if (mUriList.size() != 0) {
            result += "[" + URI_LIST + " : " + mUriList.toString() + "]" + "\n";
        } else {
            result += "[" + URI_LIST + " : " + null + "]" + "\n";
        }
        return result;
    }

}
