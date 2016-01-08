package com.sansheng.testcenter.location;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import com.sansheng.testcenter.utils.MeterUtilies;

import java.io.File;
import java.util.List;
import java.util.Random;

/**
 * Created by sunshaogang on 12/25/15.
 */
public class LocationUtilies {


    public static final String TAG = "LocationUtilies";
    public static final String BAIDU_APP_KEY = "wFtG3DzxxFRqinI8SRxy9lIy";
    private static Random sRandom = new Random(System.currentTimeMillis());
    public static final int START_CAMERA = 502;
    public static final int RANDOM_FILE_NAME_LENGTH = 16;
    public static final String COMPOSE_LOCATION_LOCATIONINFO = "location_info";
    public final static String SANSHENG_FOLDER = "SanSheng";
    public static File getSanShengPicFolder() {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), SANSHENG_FOLDER);
    }
    public static String getAbsolutePathFromInternalUri(Context context, Uri uri) {
        String finalPath = null;
        if (uri != null) {
            String uriString = uri.toString();
            if (uriString.trim().startsWith("content://") && uriString.trim().endsWith("RAW")) {
                StringBuffer sb = new StringBuffer("file://");
                List<String> segmentsList = uri.getPathSegments();
                int size = segmentsList.size();
                String accountId = segmentsList.get(size - 3);
                String attId = segmentsList.get(size - 2);
                String path = getMappedAppStorageDirectoryPerAccount(
                        context, "" + accountId + ".db_att").getAbsolutePath();
                finalPath = sb.append(path).append(File.separator).append(attId).toString();
            } else {
                finalPath = uri.toString();
            }
        }
        return finalPath;
    }

    public static File getMappedAppStorageDirectoryPerAccount(Context context, String dirName) {
        File directory = context.getDatabasePath(dirName);

        return directory;
    }
    public static String generateRandomString(int len) {
        StringBuffer sb = new StringBuffer();
        sb.append("_sansheng_");
        final String seeds = "0123456789abcdefghijklmnopqrstuv";
        for (int i = 0; i < len; i++) {
            int value = sRandom.nextInt() & 31;
            char c = seeds.charAt(value);
            sb.append(c);
        }
        sb.append("_");
        sb.append(MeterUtilies.getCurrentTimeString(System.currentTimeMillis()));
        sb.append("");
        return sb.toString();
    }

    public static BitmapFactory.Options getBitmMapDecodeOption() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        return options;
    }

    public static String inferMimeType(final String fileName,
                                       final String mimeType) {
        String resultType = null;
        String fileExtension = getFilenameExtension(fileName);
        boolean isTextPlain = "text/plain".equalsIgnoreCase(mimeType);

        if ("eml".equals(fileExtension)) {
            resultType = "message/rfc822";
        } else if ("xlsm".equals(fileExtension)){
            resultType = "application/vnd.ms-excel";
        } else {
            boolean isGenericType = isTextPlain
                    || "application/octet-stream".equalsIgnoreCase(mimeType);
            // If the given mime type is non-empty and non-generic, return it
            if (isGenericType || TextUtils.isEmpty(mimeType)) {
                if (!TextUtils.isEmpty(fileExtension)) {
                    // Otherwise, try to find a mime type based upon the file
                    // extension
                    resultType = MimeTypeMap.getSingleton()
                            .getMimeTypeFromExtension(fileExtension);
                    if (TextUtils.isEmpty(resultType)) {
                        // Finally, if original mimetype is text/plain, use it;
                        // otherwise synthesize
                        resultType = isTextPlain ? mimeType : "application/"
                                + fileExtension;
                    }
                }
            } else {
                resultType = mimeType;
            }
        }

        // No good guess could be made; use an appropriate generic type
        if (TextUtils.isEmpty(resultType)) {
            resultType = isTextPlain ? "text/plain"
                    : "application/octet-stream";
        }
        return resultType.toLowerCase();
    }
    public static String getFilenameExtension(String fileName) {
        String extension = null;
        if (!TextUtils.isEmpty(fileName)) {
            int lastDot = fileName.lastIndexOf('.');
            if ((lastDot > 0) && (lastDot < fileName.length() - 1)) {
                extension = fileName.substring(lastDot + 1).toLowerCase();
            }
        }
        return extension;
    }
}
