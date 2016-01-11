package com.sansheng.testcenter.module;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.sansheng.testcenter.utils.Utility;
import com.sansheng.testcenter.provider.ProviderUnavailableException;

/**
 * Created by sunshaogang on 12/9/15.
 */
public abstract class Content {

    // Newly created objects get this id
    public static final int NOT_SAVED = -1;
    public long mId = NOT_SAVED;
    public Uri mBaseUri;
    private Uri mUri;
    public static String AUTHORITY;
    public static Uri BASE_CONTENT_URI;
    private static String EMAIL_PACKAGE_NAME;
    public static final String[] COUNT_COLUMNS = new String[]{"count(*)"};
    public static String PARAMETER_LIMIT = "limit";
    public static String PARAMETER_IDS = "ids";
    public static String DESC = " desc ";
    public static String ASC = " asc ";

    public abstract ContentValues toContentValues();

    public abstract void restore(Cursor cursor);

    public boolean isSaved() {
        return mId != NOT_SAVED;
    }

    public Uri save(Context context) {
        if (isSaved()) {
            throw new UnsupportedOperationException();
        }
        Uri res = context.getContentResolver().insert(mBaseUri, toContentValues());
        mId = Long.parseLong(res.getPathSegments().get(1));
        return res;
    }

    public void saveOrUpdate(Context context, ContentValues contentValues) {
        if (isSaved()) {
            context.getContentResolver().update(getUri(), contentValues, null, null);
        } else {
            context.getContentResolver().insert(mBaseUri, toContentValues());
        }
    }

    public int update(Context context, ContentValues contentValues) {
        if (!isSaved()) {
            throw new UnsupportedOperationException();
        }
        return context.getContentResolver().update(getUri(), contentValues, null, null);
    }

    static public int update(Context context, Uri baseUri, long id, ContentValues contentValues) {
        return context.getContentResolver()
                .update(ContentUris.withAppendedId(baseUri, id), contentValues, null, null);
    }

    static public int delete(Context context, Uri baseUri, long id) {
        return context.getContentResolver()
                .delete(ContentUris.withAppendedId(baseUri, id), null, null);
    }

    static public int count(Context context, Uri uri, String selection, String[] selectionArgs) {
        return Utility.getFirstRowLong(context,
                uri, COUNT_COLUMNS, selection, selectionArgs, null, 0, Long.valueOf(0)).intValue();
    }

    /**
     * Same as {@link #count(Context, Uri, String, String[])} without selection.
     */
    static public int count(Context context, Uri uri) {
        return count(context, uri, null, null);
    }

    public Uri getUri() {
        if (mUri == null) {
            mUri = ContentUris.withAppendedId(mBaseUri, mId);
        }
        return mUri;
    }

    /**
     * Restore a subclass of Content from the database
     *
     * @param context           the caller's context
     * @param klass             the class to restore
     * @param contentUri        the content uri of the EmailContent subclass
     * @param contentProjection the content projection for the EmailContent subclass
     * @param id                the unique id of the object
     * @return the instantiated object
     */
    public static <T extends Content> T restoreContentWithId(Context context,
                                                             Class<T> klass, Uri contentUri, String[] contentProjection, long id) {

        Uri u = ContentUris.withAppendedId(contentUri, id);
        Cursor c = context.getContentResolver().query(u, contentProjection, null, null, null);
        if (c == null) throw new ProviderUnavailableException();
        try {
            if (c.moveToFirst()) {
                return getContent(c, klass);
            } else {
                return null;
            }
        } finally {
            c.close();
        }
    }

    static public <T extends Content> T getContent(Cursor cursor, Class<T> klass) {
        try {
            T content = klass.newInstance();
            content.mId = cursor.getLong(0);
            content.restore(cursor);
            return content;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized void init(Context context) {
        EMAIL_PACKAGE_NAME = context.getPackageName();
        AUTHORITY = EMAIL_PACKAGE_NAME + ".provider.Equipment";
        BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
        Meter.init();
        MeterData.init();
        Collect.init();
        CollectParam.init();
        EquipmentException.init();
        Concentrator.init();
        LocationInfo.init();
    }

    static public Uri uriWithLimit(Uri uri, int limit) {
        return uri.buildUpon().appendQueryParameter(Content.PARAMETER_LIMIT,
                Integer.toString(limit)).build();
    }

    public static interface MeterColumns {
        public static final String ID = "_id";
        public static final String COLLECT_ID = "collectId";
        public static final String DA = "da";
        public static final String METER_NAME = "meterName";
        public static final String METER_NUM = "meterNum";
        public static final String METER_ADDRESS = "meterAddress";
        public static final String COMMON_PASSWORD = "commPwd";
        public static final String BAUDRATE_ID = "baudrateID";
        public static final String COMMON_PORT_ID = "commPortId";
        public static final String PROTOCOL_ID = "protocolId";
        public static final String FEILV_ID = "feilvId";
        public static final String GATHER_ADDRESS = "gatherAddress";
        public static final String WEISHU_ID = "weishuId";
        public static final String USER_SMALL_TYPE_ID = "userSmallType";
        public static final String USER_TYPE_ID = "userType";
        public static final String USER_NUM = "userNum";
        public static final String USER_ADDRESS = "userAddress";
        public static final String GROUP_ID = "groupId";
        public static final String NOTE = "note";
        public static final String METER_TYPE = "type";
    }

    public static interface MeterDataColumns {
        public static final String ID = "_id";
        public static final String METER_ID = "meterId";
        public static final String VALUE_TIME = "valueTime";
        public static final String READ_TIME = "readTime";
        public static final String SAVE_TIME = "saveTime";
        public static final String DATA_TYPE = "dataType";
        public static final String DATA_ID = "dataId";
        public static final String VALZ = "valz";
        public static final String VAL1 = "val1";
        public static final String VAL2 = "val2";
        public static final String VAL3 = "val3";
        public static final String VAL4 = "val4";
        public static final String IMPORTANT = "important";
        public static final String UPDATE_TIME = "updateTime";
    }
    public static interface ConcentratorColumns {
        public static final String ID = "_id";
        public static final String CONCENTRATOR_NAME = "concentratorNmae";
        public static final String CONCENTRATOR_ADDRESS = "concentratorAddress";
    }

    public static interface CollectColumns {
        public static final String ID = "_id";
        public static final String COMM_ADDRESS = "commAddress";
        public static final String COLLECT_NAME = "collectName";
        public static final String PASSWORD = "password";
        public static final String CHANNEL_TYPE = "channelType";
        public static final String TERMINAL_IP = "terminalIp";
        public static final String TERMINAL_PORT = "terminalPort";
        public static final String BAUDRATE_ID = "baudrateId";
    }

    public static interface CollectParamColumns {
        public static final String ID = "_id";
        public static final String COLLECT_ID = "collectId";
        public static final String AFN = "afn";
        public static final String FN = "fn";
        public static final String PARAM = "param";
    }


    public static interface EquipmentExceptionColumns {
        public static final String ID = "_id";
        public static final String COLLECT_ID = "collectId";
        public static final String HEPPEN_TIME = "heppenTime";
        public static final String TYPE = "type";
        public static final String PM = "pm";
        public static final String FLAG = "flag";
        public static final String NOTE = "note";
    }



    public static interface LocationInfoColumns {
        public static final String ID = "_id";
        public static final String LOCTYPE = "loctype";
        public static final String ADDRESS = "address";
        public static final String POI = "poi";
        public static final String LATITUDE = "latitude";
        public static final String LONTITUDE = "lontitude";
        public static final String UPDATE_TIME = "upate_time";
        public static final String URI_LIST = "uri_list";
    }

}
