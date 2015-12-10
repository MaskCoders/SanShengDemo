package com.example.demo.mode;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.example.demo.util.Utility;
import com.example.demo.provider.ProviderUnavailableException;

/**
 * Created by sunshaogang on 12/9/15.
 */
public abstract class Content {

    // Newly created objects get this id
    public static final int NOT_SAVED = -1;
    public long mId = NOT_SAVED;
    public static Uri mBaseUri;
    private Uri mUri;
    public static String AUTHORITY;
    public static Uri CONTENT_URI;
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

    /**
     * Generic count method that can be used for any ContentProvider
     *
     * @param context       the calling Context
     * @param uri           the Uri for the provider query
     * @param selection     as with a query call
     * @param selectionArgs as with a query call
     * @return the number of items matching the query (or zero)
     */
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
        CONTENT_URI = Uri.parse("content://" + AUTHORITY);
        Meter.init();
    }

    public static interface MeterColumns {
        public static final String ID = "_id";
        public static final String METER_ID = "meterId";
        public static final String METER_NAME = "name";
        public static final String VALUE_TIME = "valueTime";
        public static final String READ_TIME = "readTime";
        public static final String DATA_TYPE = "dataType";
        public static final String VALZ = "valz";
        public static final String IMPORTANT = "important";
        public static final String UPDATE_TIME = "updateTime";
    }

    static public Uri uriWithLimit(Uri uri, int limit) {
        return uri.buildUpon().appendQueryParameter(Content.PARAMETER_LIMIT,
                Integer.toString(limit)).build();
    }
}
