package com.sansheng.testcenter.module;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.google.gson.Gson;
import com.sansheng.testcenter.center.ProtoParam;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunshaogang on 12/17/15.
 */
public class CollectParam extends Content implements Content.CollectParamColumns, Parcelable {


    public int mCollectId;
    public int mAFn;
    public int mFn;
    public String mParam = "\n" +
            "{\n" +
            "  \"Fn\":3,\n" +
            "  \"Paras\":[\n" +
            "  {\"n\":\"主站IP地址\",\"v\":\"10.130.124.219\",\"f\":0,\"u\":1},\n" +
            "  {\"n\":\"主站IP地址1段\",\"v\":\"10\",\"f\":101,\"u\":0},\n" +
            "  {\n" +
            "    \"n\":\"主站IP地址2段\",\n" +
            "    \"v\":\"130\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"主站IP地址3段\",\n" +
            "    \"v\":\"124\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"主站IP地址4段\",\n" +
            "    \"v\":\"219\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"主站端口\",\n" +
            "    \"v\":\"6006\",\n" +
            "    \"f\":102,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用IP地址\",\n" +
            "    \"v\":\"192.169.0.3\",\n" +
            "    \"f\":0,\n" +
            "    \"u\":1\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用IP地址1段\",\n" +
            "    \"v\":\"192\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用IP地址2段\",\n" +
            "    \"v\":\"169\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用IP地址3段\",\n" +
            "    \"v\":\"0\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用IP地址4段\",\n" +
            "    \"v\":\"3\",\n" +
            "    \"f\":101,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"备用端口\",\n" +
            "    \"v\":\"8001\",\n" +
            "    \"f\":102,\n" +
            "    \"u\":0\n" +
            "  },\n" +
            "  {\n" +
            "    \"n\":\"APN\",\n" +
            "    \"v\":\"BNDQ-DDN.BJ\",\n" +
            "    \"f\":50,\n" +
            "    \"u\":0\n" +
            "  }]\n" +
            "}";//json
    private List<ProtoParam> paramList = new ArrayList<ProtoParam>();

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

    public static Uri CONTENT_URI;

    public CollectParam() {
        mBaseUri = CONTENT_URI;
    }

    public CollectParam(int collectId, int afn, int fn, String param) {
        mBaseUri = CONTENT_URI;
        mCollectId = collectId;
        mAFn = afn;
        mFn = fn;
        mParam = param;
    }

    public static void init() {
        CONTENT_URI = Uri.parse(Content.BASE_CONTENT_URI + "/collectparam");
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

    public List<ProtoParam> getParamList() {
        if (paramList.size() == 0) {
            try {
                JSONObject root = new JSONObject(mParam);
                JSONArray array = root.getJSONArray("Paras");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    ProtoParam param = new ProtoParam(object);
                    paramList.add(param);
                    Log.e("ssg", param.toString());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        Log.e("ssg", paramList.toString());
        return paramList;
    }

    public void resetParamList(String[] keys, String[] values, int[] types) {
        paramList.clear();
        for (int i = 0; i < keys.length; i++) {
            ProtoParam param = new ProtoParam(keys[i], values[i], types[i]);
            paramList.add(param);
        }
        mParam = paramList.toString();
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

    public void saveOrUpdate(Context context) {
        super.saveOrUpdate(context, toContentValues());
    }

    @Override
    public String toString() {
        return toJson();
    }

    public String toJson() {
        JSONObject object = new JSONObject();
        Gson gson = new Gson();
        try {
            object.put("aFn", mAFn);
            object.put("Fn", mFn);
            object.put("Paras", gson.toJson(getParamList()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }
}
