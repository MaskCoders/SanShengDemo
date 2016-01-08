package com.sansheng.testcenter.location;

import java.lang.ref.WeakReference;

public class AsyncHolder {
    private final WeakReference<BitmapWorkerTask> mBitmapWorkerTaskReference;

    public AsyncHolder(BitmapWorkerTask bitmapWorkerTask) {
        mBitmapWorkerTaskReference =
            new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);
    }

    public BitmapWorkerTask getBitmapWorkerTask() {
        return mBitmapWorkerTaskReference.get();
    }
}