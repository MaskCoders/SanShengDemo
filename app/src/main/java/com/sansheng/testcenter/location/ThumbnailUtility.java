package com.sansheng.testcenter.location;

import android.content.Context;
import android.graphics.*;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import com.sansheng.testcenter.utils.Utilities;

import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;

public class ThumbnailUtility {
    private static final String TAG = "ThumbnailUtility";

    private ThumbnailUtility() {

    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap decodeSampledBitmapFromResource(String filePath,
                                                         int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            Log.w(TAG, "image width or height should not be 0");
            return null;
        }
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = 1;
        options.inJustDecodeBounds = false;

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        FileInputStream fis = null;
        try {
            fis = new FileInputStream(filePath);
            if (filePath.endsWith(".bmp") || filePath.endsWith(".wbmp")) {
                return Bitmap.createScaledBitmap(BitmapFactory.decodeFileDescriptor(fis.getFD(), null, null), reqWidth, reqHeight, false);
            } else {
                return BitmapFactory.decodeFileDescriptor(fis.getFD(), null, options);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Decode Bitmap", e.getMessage());
            return null;
        } catch (OutOfMemoryError outOfMemoryError) {
            Log.e("Decode Bitmap", outOfMemoryError.getMessage());
            return null;
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

		/* Calculate size according to reqWidth and reqHeight.
        final int wDivider = Math.max(options.outWidth / reqWidth, 1);
        final int hDivider = Math.max(options.outHeight / reqHeight, 1);
      
        inSampleSize = Math.min(wDivider, hDivider);
		*/

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static boolean cancelPotentialWork(String fileName, ImageView imageView) {
        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
        Log.d(TAG, "fileName: " + fileName);

        if (bitmapWorkerTask != null) {
            final String filePath = bitmapWorkerTask.getFilePath();
            Log.d(TAG, "filePath:" + filePath);
            // If bitmapData is not yet set or it differs from the new data
            if (TextUtils.isEmpty(filePath) || imageView.getTag() == null ||
                    !filePath.equals(fileName)) {
                // Cancel previous task
                bitmapWorkerTask.cancel(true);
            } else {
                // The same work is already in progress
                Log.d(TAG, "loadBitmapWorker is in progress");
                return false;
            }
        }
        // No task associated with the ImageView, or an existing task was cancelled
        Log.d(TAG, "start a new BitmapWorkerTask");
        return true;
    }

    /*package*/
    static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
        if (imageView != null) {
            Object o = imageView.getTag();
            if (o instanceof AsyncHolder) {
                final AsyncHolder asyncDrawable = (AsyncHolder) o;
                return asyncDrawable.getBitmapWorkerTask();
            }
        }
        return null;
    }

    public static void loadBitmap(ExecutorService threadpool, String filePath, ImageView imageView, int width, int height, int defaultIcon, Context context) {
        Log.d(TAG, "loadBitmap filePath: " + filePath);
        if (TextUtils.isEmpty(filePath)) {
            imageView.setImageResource(defaultIcon);
            imageView.setTag(null);
            return;
        }
        if (filePath.startsWith("content://")) {
            filePath = Utilities.getPath(context, Uri.parse(filePath));
        } else if (filePath.startsWith("file://")) {
            try {
                filePath = URLDecoder.decode(filePath, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            filePath = filePath.substring(7);
        }

//        if (ThumbnailUtility.cancelPotentialWork(filePath, imageView)) {
        imageView.setImageResource(defaultIcon);
        final BitmapWorkerTask task = new BitmapWorkerTask(imageView);
        final AsyncHolder asyncDrawable = new AsyncHolder(task);
        imageView.setTag(asyncDrawable);
        if (!TextUtils.isEmpty(filePath) && context != null) {
//                filePath = LocationUtilies.getSanShengPicFolder(context, Uri.parse(filePath));
            if (filePath.startsWith("file://")) {
                filePath = filePath.substring(7);
            }
        }
        if (threadpool == null || threadpool.isShutdown()) {
            task.execute(filePath, width, height, defaultIcon, false);
        } else {
            task.executeOnExecutor(threadpool, filePath, width, height, defaultIcon);
        }
//        }
    }

    public static void loadGalleryThumbnail(ExecutorService threadpool, String filePath, ImageView imageView, int width, int height, int defaultIcon, Context context) {
        if (ThumbnailUtility.cancelPotentialWork(filePath, imageView)) {
            imageView.setImageResource(defaultIcon);
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            AsyncHolder asyncDrawable = new AsyncHolder(task);
            imageView.setTag(asyncDrawable);
            if (threadpool == null || threadpool.isShutdown()) {
                return;
            }
            task.executeOnExecutor(threadpool, filePath, width, height, 0);
        }
    }

    public static void loadFolderBitmap(ExecutorService threadpool, String filePath, ImageView imageView, int width, int height, int defaultIcon, Context context) {
        if (ThumbnailUtility.cancelPotentialWork(filePath, imageView)) {
            imageView.setImageResource(defaultIcon);
            BitmapWorkerTask task = new BitmapWorkerTask(imageView);
            AsyncHolder asyncDrawable = new AsyncHolder(task);
            imageView.setTag(asyncDrawable);
            if (threadpool == null || threadpool.isShutdown()) {
                return;
            }
            task.executeOnExecutor(threadpool, filePath, width, height, defaultIcon, true);
        }
    }

}
