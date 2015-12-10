/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.demo.app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.*;
import android.provider.OpenableColumns;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Base64;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Pattern;

public class Utility {
    private static final String TAG = "Utility";
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Charset ASCII = Charset.forName("US-ASCII");
	public static final Charset GB2312 = Charset.forName("GB2312");
	public static final Charset GB18030 = Charset.forName("GB18030");

    private static Toast mToast;

    // "GMT" + "+" or "-" + 4 digits
    private static final Pattern DATE_CLEANUP_PATTERN_WRONG_TIMEZONE =
            Pattern.compile("GMT([-+]\\d{4})$");

    private static Handler sMainThreadHandler;
    
    /**
     * @return a {@link Handler} tied to the home_layout thread.
     */
    public static Handler getMainThreadHandler() {
        if (sMainThreadHandler == null) {
            // No need to synchronize -- it's okay to create an extra Handler, which will be used
            // only once and then thrown away.
            sMainThreadHandler = new Handler(Looper.getMainLooper());
        }
        return sMainThreadHandler;
    }

    public final static String readInputStream(InputStream in, String encoding) throws IOException {
        InputStreamReader reader = new InputStreamReader(in, encoding);
        StringBuffer sb = new StringBuffer();
        int count;
        char[] buf = new char[512];
        while ((count = reader.read(buf)) != -1) {
            sb.append(buf, 0, count);
        }
        return sb.toString();
    }

    public final static boolean arrayContains(Object[] a, Object o) {
        int index = arrayIndex(a, o);
        return (index >= 0);
    }

    public final static int arrayIndex(Object[] a, Object o) {
        for (int i = 0, count = a.length; i < count; i++) {
            if (a[i].equals(o)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns a concatenated string containing the output of every Object's
     * toString() method, each separated by the given separator character.
     */
    public static String combine(Object[] parts, char separator) {
        if (parts == null) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < parts.length; i++) {
            sb.append(parts[i].toString());
            if (i < parts.length - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    public static boolean isPortFieldValid(TextView view) {
        CharSequence chars = view.getText();
        if (TextUtils.isEmpty(chars)) return false;
        Integer port;
        // In theory, we can't get an illegal value here, since the field is monitored for valid
        // numeric input. But this might be used elsewhere without such a check.
        try {
            port = Integer.parseInt(chars.toString());
        } catch (NumberFormatException e) {
            return false;
        }
        return port > 0 && port < 65536;
    }

    /**
     * Validate a hostname name field.
     *
     * Because we just use the {@link URI} class for validation, it'll accept some invalid
     * host names, but it works well enough...
     */
    public static boolean isServerNameValid(TextView view) {
        return isServerNameValid(view.getText().toString());
    }

    public static boolean isServerNameValid(String serverName) {
        serverName = serverName.trim();
        if (TextUtils.isEmpty(serverName)) {
            return false;
        }
        try {
            URI uri = new URI(
                    "http",
                    null,
                    serverName,
                    -1,
                    null, // path
                    null, // query
                    null);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * A fast version of  URLDecoder.decode() that works only with UTF-8 and does only two
     * allocations. This version is around 3x as fast as the standard one and I'm using it
     * hundreds of times in places that slow down the UI, so it helps.
     */
    public static String fastUrlDecode(String s) {
        try {
            byte[] bytes = s.getBytes("UTF-8");
            byte ch;
            int length = 0;
            for (int i = 0, count = bytes.length; i < count; i++) {
                ch = bytes[i];
                if (ch == '%') {
                    int h = (bytes[i + 1] - '0');
                    int l = (bytes[i + 2] - '0');
                    if (h > 9) {
                        h -= 7;
                    }
                    if (l > 9) {
                        l -= 7;
                    }
                    bytes[length] = (byte) ((h << 4) | l);
                    i += 2;
                }
                else if (ch == '+') {
                    bytes[length] = ' ';
                }
                else {
                    bytes[length] = bytes[i];
                }
                length++;
            }
            return new String(bytes, 0, length, "UTF-8");
        }
        catch (UnsupportedEncodingException uee) {
            return null;
        }
    }

    /**
     * Generate a time in milliseconds from a date string that represents a date/time in GMT
     * @param date string in format 20090211T180303Z (rfc2445, iCalendar).
     * @return the time in milliseconds (since Jan 1, 1970)
     */
    public static long parseDateTimeToMillis(String date) {
        GregorianCalendar cal = parseDateTimeToCalendar(date);
        return cal.getTimeInMillis();
    }

    /**
     * Generate a GregorianCalendar from a date string that represents a date/time in GMT
     * @param date string in format 20090211T180303Z (rfc2445, iCalendar).
     * @return the GregorianCalendar
     */
    public static GregorianCalendar parseDateTimeToCalendar(String date) {
        GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
                Integer.parseInt(date.substring(4, 6)) - 1, Integer.parseInt(date.substring(6, 8)),
                Integer.parseInt(date.substring(9, 11)), Integer.parseInt(date.substring(11, 13)),
                Integer.parseInt(date.substring(13, 15)));
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return cal;
    }

    /**
     * Generate a time in milliseconds from an email date string that represents a date/time in GMT
     * @param date string in format 2010-02-23T16:00:00.000Z (ISO 8601, rfc3339)
     * @return the time in milliseconds (since Jan 1, 1970)
     */
    public static long parseEmailDateTimeToMillis(String date) {
        GregorianCalendar cal = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
                Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)),
                Integer.parseInt(date.substring(11, 13)), Integer.parseInt(date.substring(14, 16)),
                Integer.parseInt(date.substring(17, 19)));
        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        return cal.getTimeInMillis();
    }

    private static byte[] encode(Charset charset, String s) {
        if (s == null) {
            return null;
        }
        final ByteBuffer buffer = charset.encode(CharBuffer.wrap(s));
        final byte[] bytes = new byte[buffer.limit()];
        buffer.get(bytes);
        return bytes;
    }

    private static String decode(Charset charset, byte[] b) {
        if (b == null) {
            return null;
        }
        final CharBuffer cb = charset.decode(ByteBuffer.wrap(b));
        return new String(cb.array(), 0, cb.length());
    }

    /** Converts a String to UTF-8 */
    public static byte[] toUtf8(String s) {
        return encode(UTF_8, s);
    }

    /** Builds a String from UTF-8 bytes */
    public static String fromUtf8(byte[] b) {
        return decode(UTF_8, b);
    }

    /** Converts a String to ASCII bytes */
    public static byte[] toAscii(String s) {
        return encode(ASCII, s);
    }

    /** Builds a String from ASCII bytes */
    public static String fromAscii(byte[] b) {
        return decode(ASCII, b);
    }

	/** Builds a String from GB2312 bytes */
	public static String fromGB2312(byte[] b) {
		return decode(GB2312, b);
	}

	/** Converts a String to GB2312 bytes */
	public static byte[] toGB2312(String s) {
		return encode(GB2312, s);
	}

	/** Builds a String from GB18030 bytes */
	public static String fromGB18030(byte[] b) {
		return decode(GB18030, b);
	}

	/** Converts a String to GB18030 bytes */
	public static byte[] toGB18030(String s) {
		return encode(GB18030, s);
	}
	
    /**
     * @return true if the input is the first (or only) byte in a UTF-8 character
     */
    public static boolean isFirstUtf8Byte(byte b) {
        // If the top 2 bits is '10', it's not a first byte.
        return (b & 0xc0) != 0x80;
    }

    public static String byteToHex(int b) {
        return byteToHex(new StringBuilder(), b).toString();
    }

    public static StringBuilder byteToHex(StringBuilder sb, int b) {
        b &= 0xFF;
        sb.append("0123456789ABCDEF".charAt(b >> 4));
        sb.append("0123456789ABCDEF".charAt(b & 0xF));
        return sb;
    }

    public static String replaceBareLfWithCrlf(String str) {
        return str.replace("\r", "").replace("\n", "\r\n");
    }

    /**
     * Cancel an {@link AsyncTask}.  If it's already running, it'll be interrupted.
     */
    public static void cancelTaskInterrupt(AsyncTask<?, ?, ?> task) {
        cancelTask(task, true);
    }



    /**
     * Cancel an {@link AsyncTask}.
     *
     * @param mayInterruptIfRunning <tt>true</tt> if the thread executing this
     *        task should be interrupted; otherwise, in-progress tasks are allowed
     *        to complete.
     */
    public static void cancelTask(AsyncTask<?, ?, ?> task, boolean mayInterruptIfRunning) {
        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
            task.cancel(mayInterruptIfRunning);
        }
    }

    public static String getSmallHash(final String value) {
        final MessageDigest sha;
        try {
            sha = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException impossible) {
            return null;
        }
        sha.update(Utility.toUtf8(value));
        final int hash = getSmallHashFromSha1(sha.digest());
        return Integer.toString(hash);
    }

    /**
     * @return a non-negative integer generated from 20 byte SHA-1 hash.
     */
    /* package for testing */ static int getSmallHashFromSha1(byte[] sha1) {
        final int offset = sha1[19] & 0xf; // SHA1 is 20 bytes.
        return ((sha1[offset]  & 0x7f) << 24)
                | ((sha1[offset + 1] & 0xff) << 16)
                | ((sha1[offset + 2] & 0xff) << 8)
                | ((sha1[offset + 3] & 0xff));
    }

    /**
     * Try to make a date MIME(RFC 2822/5322)-compliant.
     *
     * It fixes:
     * - "Thu, 10 Dec 09 15:08:08 GMT-0700" to "Thu, 10 Dec 09 15:08:08 -0700"
     *   (4 digit zone value can't be preceded by "GMT")
     *   We got a report saying eBay sends a date in this format
     */
    public static String cleanUpMimeDate(String date) {
        if (TextUtils.isEmpty(date)) {
            return date;
        }
        date = DATE_CLEANUP_PATTERN_WRONG_TIMEZONE.matcher(date).replaceFirst("$1");
        return date;
    }

    public static ByteArrayInputStream streamFromAsciiString(String ascii) {
        return new ByteArrayInputStream(toAscii(ascii));
    }
    
	public static ByteArrayInputStream streamFromGB2312String(String gbString) {
		return new ByteArrayInputStream(toGB2312(gbString));
	}

	public static ByteArrayInputStream streamFromGB18030String(String gbString) {
		return new ByteArrayInputStream(toGB18030(gbString));
	}

    /**
     * @param context
     * @param syncResult sync error type :@UIProvider.lastSyncResult.
     * toast a error info in the string array
     * add by sunshaogang 2014-09-04
     */
//    public static void showErrorToast(Context context, int syncResult) {
//        CharSequence toastStr = Utils.getSyncStatusText4AbastractControl(context, syncResult);
//        showErrorToast(context, toastStr.toString());
//    }

    /**
     * A thread safe way to show a Toast.  Can be called from any thread.
     *
     * @param context context
     * @param resId Resource ID of the message string.
     */
    public static void showToast(Context context, int resId) {
        showToast(context, context.getResources().getString(resId));
    }
    
    public static void showToast(Context context, int resId, final int duration ) {
        showToast(context, context.getResources().getString(resId), duration);
    }

    /**
     * A thread safe way to show a Toast.  Can be called from any thread.
     *
     * @param context context
     * @param message Message to show.
     */
    public static void showToast(final Context context, final String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }
    
    public static void showToast(final Context context, final String message, final int duration) {
        if (mToast != null) {
            mToast.cancel();
        }

        getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                mToast = Toast.makeText(context.getApplicationContext(), message, duration);
                mToast.show();
            }
        });
    }

    /**
     * Run {@code r} on a worker thread, returning the AsyncTask
     * @return the AsyncTask; this is primarily for use by unit tests, which require the
     * result of the task
     *
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Deprecated
    public static AsyncTask<Void, Void, Void> runAsync(final Runnable r) {
        return new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                r.run();
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /* package */ interface NewFileCreator {
        public static final NewFileCreator DEFAULT = new NewFileCreator() {
                    @Override
                    public boolean createNewFile(File f) throws IOException {
                        return f.createNewFile();
                    }
        };
        public boolean createNewFile(File f) throws IOException;
    }

    public interface CursorGetter<T> {
        T get(Cursor cursor, int column);
    }

    private static final CursorGetter<Long> LONG_GETTER = new CursorGetter<Long>() {
        @Override
        public Long get(Cursor cursor, int column) {
            return cursor.getLong(column);
        }
    };

    private static final CursorGetter<Integer> INT_GETTER = new CursorGetter<Integer>() {
        @Override
        public Integer get(Cursor cursor, int column) {
            return cursor.getInt(column);
        }
    };

    private static final CursorGetter<String> STRING_GETTER = new CursorGetter<String>() {
        @Override
        public String get(Cursor cursor, int column) {
            return cursor.getString(column);
        }
    };

    private static final CursorGetter<byte[]> BLOB_GETTER = new CursorGetter<byte[]>() {
        @Override
        public byte[] get(Cursor cursor, int column) {
            return cursor.getBlob(column);
        }
    };

    /**
     * @return if {@code original} is to the EmailProvider, add "?limit=1".  Otherwise just returns
     * {@code original}.
     *
     * Other providers don't support the limit param.  Also, changing URI passed from other apps
     * can cause permission errors.
     */
    /* package */ static Uri buildLimitOneUri(Uri original) {
        if ("content".equals(original.getScheme()) &&
                Content.AUTHORITY.equals(original.getAuthority())) {
            return Content.uriWithLimit(original, 1);
        }
        return original;
    }

    /**
     * @return a generic in column {@code column} of the first result row, if the query returns at
     * least 1 row.  Otherwise returns {@code defaultValue}.
     */
    public static <T extends Object> T getFirstRowColumn(Context context, Uri uri,
            String[] projection, String selection, String[] selectionArgs, String sortOrder,
            int column, T defaultValue, CursorGetter<T> getter) {
        // Use PARAMETER_LIMIT to restrict the query to the single row we need
        uri = buildLimitOneUri(uri);
        Cursor c = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                sortOrder);
        if (c != null) {
            try {
                if (c.moveToFirst()) {
                    return getter.get(c, column);
                }
            } finally {
                c.close();
            }
        }
        return defaultValue;
    }

    /**
     * {@link #getFirstRowColumn} for a Long with null as a default value.
     */
    public static Long getFirstRowLong(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs,
                sortOrder, column, null, LONG_GETTER);
    }

    /**
     * {@link #getFirstRowColumn} for a Long with a provided default value.
     */
    public static Long getFirstRowLong(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column,
            Long defaultValue) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs,
                sortOrder, column, defaultValue, LONG_GETTER);
    }

    /**
     * {@link #getFirstRowColumn} for an Integer with null as a default value.
     */
    public static Integer getFirstRowInt(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs,
                sortOrder, column, null, INT_GETTER);
    }

    /**
     * {@link #getFirstRowColumn} for an Integer with a provided default value.
     */
    public static Integer getFirstRowInt(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column,
            Integer defaultValue) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs,
                sortOrder, column, defaultValue, INT_GETTER);
    }

    /**
     * {@link #getFirstRowColumn} for a String with null as a default value.
     */
    public static String getFirstRowString(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column) {
        return getFirstRowString(context, uri, projection, selection, selectionArgs, sortOrder,
                column, null);
    }

    /**
     * {@link #getFirstRowColumn} for a String with a provided default value.
     */
    public static String getFirstRowString(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column,
            String defaultValue) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs,
                sortOrder, column, defaultValue, STRING_GETTER);
    }

    /**
     * {@link #getFirstRowColumn} for a byte array with a provided default value.
     */
    public static byte[] getFirstRowBlob(Context context, Uri uri, String[] projection,
            String selection, String[] selectionArgs, String sortOrder, int column,
            byte[] defaultValue) {
        return getFirstRowColumn(context, uri, projection, selection, selectionArgs, sortOrder,
                column, defaultValue, BLOB_GETTER);
    }

    /**
     * Convenience method wrapping calls to retrieve columns from a single row, via EmailProvider.
     * The arguments are exactly the same as to contentResolver.query().  Results are returned in
     * an array of Strings corresponding to the columns in the projection.  If the cursor has no
     * rows, null is returned.
     */
    public static String[] getRowColumns(Context context, Uri contentUri, String[] projection,
            String selection, String[] selectionArgs) {
        String[] values = new String[projection.length];
        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(contentUri, projection, selection, selectionArgs, null);
        try {
            if (c != null && c.moveToFirst()) {
                for (int i = 0; i < projection.length; i++) {
                    values[i] = c.getString(i);
                }
            } else {
                return null;
            }
        } finally {
        	if (c != null) {
        		c.close();
        	}
        }
        return values;
    }

    /**
     * Convenience method for retrieving columns from a particular row in EmailProvider.
     * Passed in here are a base uri (e.g. Message.CONTENT_URI), the unique id of a row, and
     * a projection.  This method calls the previous one with the appropriate URI.
     */
    public static String[] getRowColumns(Context context, Uri baseUri, long id,
            String... projection) {
        return getRowColumns(context, ContentUris.withAppendedId(baseUri, id), projection, null,
                null);
    }

    public static boolean isExternalStorageMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static long[] toPrimitiveLongArray(Collection<Long> collection) {
        // Need to do this manually because we're converting to a primitive long array, not
        // a Long array.
        final int size = collection.size();
        final long[] ret = new long[size];
        // Collection doesn't have get(i).  (Iterable doesn't have size())
        int i = 0;
        for (Long value : collection) {
            ret[i++] = value;
        }
        return ret;
    }

    public static Set<Long> toLongSet(long[] array) {
        // Need to do this manually because we're converting from a primitive long array, not
        // a Long array.
        final int size = array.length;
        HashSet<Long> ret = new HashSet<Long>(size);
        for (int i = 0; i < size; i++) {
            ret.add(array[i]);
        }
        return ret;
    }

    /**
     * Workaround for the {@link ListView#smoothScrollToPosition} randomly scroll the view bug
     * if it's called right after {@link ListView#setAdapter}.
     */
    public static void listViewSmoothScrollToPosition(final Activity activity,
            final ListView listView, final int position) {
        // Workarond: delay-call smoothScrollToPosition()
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                if (activity.isFinishing()) {
                    return; // Activity being destroyed
                }
                listView.smoothScrollToPosition(position);
            }
        });
    }

    private static final String[] ATTACHMENT_META_NAME_PROJECTION = {
        OpenableColumns.DISPLAY_NAME
    };
    private static final int ATTACHMENT_META_NAME_COLUMN_DISPLAY_NAME = 0;

    /**
     * @return Filename of a content of {@code contentUri}.  If the provider doesn't provide the
     * filename, returns the last path segment of the URI.
     */
    public static String getContentFileName(Context context, Uri contentUri) {
        String name = getFirstRowString(context, contentUri, ATTACHMENT_META_NAME_PROJECTION, null,
                null, null, ATTACHMENT_META_NAME_COLUMN_DISPLAY_NAME);
        if (name == null) {
            name = contentUri.getLastPathSegment();
        }
        return name;
    }

    /**
     * Append a bold span to a {@link SpannableStringBuilder}.
     */
    public static SpannableStringBuilder appendBold(SpannableStringBuilder ssb, String text) {
        if (!TextUtils.isEmpty(text)) {
            SpannableString ss = new SpannableString(text);
            ss.setSpan(new StyleSpan(Typeface.BOLD), 0, ss.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssb.append(ss);
        }

        return ssb;
    }


    /**
     * Cursor wrapper that remembers where it was closed.
     *
     * Use {@link #get} to create a wrapped cursor.
     * USe {@link #getTraceIfAvailable} to get the stack trace.
     * Use {@link #log} to log if/where it was closed.
     */
    public static class CloseTraceCursorWrapper extends CursorWrapper {
        private static final boolean TRACE_ENABLED = false;

        private Exception mTrace;

        private CloseTraceCursorWrapper(Cursor cursor) {
            super(cursor);
        }

        @Override
        public void close() {
            mTrace = new Exception("STACK TRACE");
            super.close();
        }

        public static Exception getTraceIfAvailable(Cursor c) {
            if (c instanceof CloseTraceCursorWrapper) {
                return ((CloseTraceCursorWrapper) c).mTrace;
            } else {
                return null;
            }
        }

        public static void log(Cursor c) {
            if (c == null) {
                return;
            }
            if (c.isClosed()) {
                Log.w(TAG, "Cursor was closed here: Cursor=" + c,
                        getTraceIfAvailable(c));
            } else {
                Log.w(TAG, "Cursor not closed.  Cursor=" + c);
            }
        }

        public static Cursor get(Cursor original) {
            return TRACE_ENABLED ? new CloseTraceCursorWrapper(original) : original;
        }

        /* package */ static CloseTraceCursorWrapper alwaysCreateForTest(Cursor original) {
            return new CloseTraceCursorWrapper(original);
        }
    }

    /**
     * Test that the given strings are equal in a null-pointer safe fashion.
     */
    public static boolean areStringsEqual(String s1, String s2) {
        return (s1 != null && s1.equals(s2)) || (s1 == null && s2 == null);
    }

    public static void enableStrictMode(boolean enabled) {
        StrictMode.setThreadPolicy(enabled
                ? new StrictMode.ThreadPolicy.Builder().detectAll().build()
                : StrictMode.ThreadPolicy.LAX);
        StrictMode.setVmPolicy(enabled
                ? new StrictMode.VmPolicy.Builder().detectAll().build()
                : StrictMode.VmPolicy.LAX);
    }

    /**
     * Builds an "in" expression for SQLite.
     *
     * e.g. "ID" + 1,2,3 -> "ID in (1,2,3)".  If {@code values} is empty or null, it returns an
     * empty string.
     */
    public static String buildInSelection(String columnName, Collection<? extends Number> values) {
        if ((values == null) || (values.size() == 0)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(columnName);
        sb.append(" in (");
        String sep = "";
        for (Number n : values) {
            sb.append(sep);
            sb.append(n.toString());
            sep = ",";
        }
        sb.append(')');
        return sb.toString();
    }
    
    public static int dip2Px(Context context, float dip) {
        return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
    }
    
    public static int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(pxValue / scale + 0.5f); 
    }

   /**
     * @author HouMingtao
     */
    public static String preProcessSql(String sql) {
        if (sql == null || sql.isEmpty()) {
            return "";
        }
        sql = sql.replaceAll("/", "//");
        sql = sql.replaceAll("\\\\", "/\\\\");
        sql = sql.replaceAll("_", "/_");
        sql = sql.replaceAll("%", "/%");
        sql = sql.replaceAll("'", "''");
        sql = sql.replaceAll("\\[", "/\\[");
        sql = sql.replaceAll("-", "/-");
        sql = sql.replaceAll("&", "/&");
        sql = sql.replaceAll("\\|", "/\\|");
        sql = sql.replaceAll("\"", "/\"");
        return sql;
    }

    private static String toHexString(byte[] bytes) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        char str[] = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            byte byte0 = bytes[i];
            str[2 * i] = hexDigits[byte0 >>> 4 & 0xf];
            str[2 * i + 1] = hexDigits[byte0 & 0xf];
        }

        return new String(str);
    }

    public static String digest(String algorithm, String... strings) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        for (String s : strings) {
            digest.update(s.getBytes());
        }

        byte[] bytes = digest.digest();
        return toHexString(bytes);
    }

    private static byte[] digestInternal(String algorithm, File file) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        FileInputStream stream = null;

        try {
            stream = new FileInputStream(file);
            byte[] bytes = new byte[1024 * 128];
            int len;

            while ((len = stream.read(bytes)) > 0) {
                digest.update(bytes, 0, len);
            }

            bytes = digest.digest();

            return bytes;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static String digest(String algorithm,File file)throws NoSuchAlgorithmException, IOException {
        return toHexString(digestInternal(algorithm, file));
    }


    public static String digestToBase64(String algorithm, File file)throws NoSuchAlgorithmException,IOException {

        return Base64.encodeToString(digestInternal(algorithm, file), Base64.NO_WRAP);
    }

    public static String hmac(String algorithm, String secretKey, String... strings) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(keySpec);

        for (String string : strings) {
        mac.update(string.getBytes("UTF-8"));
        }

        return Base64.encodeToString(mac.doFinal(), Base64.NO_WRAP);
    }


}
