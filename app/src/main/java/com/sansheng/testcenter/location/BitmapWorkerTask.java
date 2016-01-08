package com.sansheng.testcenter.location;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import com.sansheng.testcenter.base.SquareImageView;

import java.io.File;
import java.lang.ref.WeakReference;

public class BitmapWorkerTask extends AsyncTask<Object, Void, Bitmap> {
    private final String TAG = "BitmapWorkerTask";
    private final WeakReference<ImageView> mImageViewReference;
    private String mFilePath;
    private int mDefaultResId = 0;
    private long mAttId;
    private Context mContext;

    public BitmapWorkerTask(ImageView imageView) {
        mImageViewReference = new WeakReference<ImageView>(imageView);
    }

    public BitmapWorkerTask(long attId) {
        mImageViewReference = null;
        mAttId = attId;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            Log.d(TAG, "isCanceled");
            bitmap = null;
        }

        if (mImageViewReference != null) {
            final ImageView imageView = mImageViewReference.get();
            final BitmapWorkerTask bitmapWorkerTask =
                    ThumbnailUtility.getBitmapWorkerTask(imageView);
            if (this == bitmapWorkerTask && imageView != null) {
                if (bitmap != null) {
                    Log.d(TAG, "set to thumbnail image");
                    if (imageView instanceof SquareImageView) {
                        ((SquareImageView) imageView).setImageBitmap(bitmap);
                    } else {
                        imageView.setImageBitmap(bitmap);
                    }
                } else {
                    Log.d(TAG, "set to default format icon");
                    imageView.setImageResource(mDefaultResId);
                    imageView.setTag(null);
                }
            } else {
                Log.d(TAG, "bitmapWorker is this");
            }
        }
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        if (params.length != 5 && params.length != 4) {
            return null;
        }

        mFilePath = (String) params[0];
        int width = (Integer) params[1];
        int height = (Integer) params[2];
        mDefaultResId = (Integer) params[3];

            if (params.length == 5) {
                boolean isFolder = (Boolean) params[4];
                if (isFolder) {
                    File f = new File(mFilePath);
                    String thumbnailPath = null;
                    File[] files = f.listFiles();
                    if (files != null && files.length > 0) {
                        File latestImage = null;
                        for (File file : files) {
                            String path = file.getAbsolutePath();
                            String mime = LocationUtilies.inferMimeType(path, null);
                            if (mime.startsWith("image")) {
                                if (latestImage == null) latestImage = file;
                                else if (file.lastModified() > latestImage.lastModified()) {
                                    latestImage = file;
                                }

                            }
                        }
                        if (latestImage == null) {
                            return null;
                        }
                        thumbnailPath = latestImage.getAbsolutePath();
                    }
                    return ThumbnailUtility.decodeSampledBitmapFromResource(thumbnailPath, width, height);
                } else {
                    return ThumbnailUtility.decodeSampledBitmapFromResource(mFilePath, width, height);
                }
            } else {
                return ThumbnailUtility.decodeSampledBitmapFromResource(mFilePath, width, height);
            }
    }


    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String data) {
        this.mFilePath = data;
    }


}
