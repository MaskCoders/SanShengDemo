package com.sansheng.testcenter.base.view;

import android.content.Context;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;

import java.util.Calendar;

/**
 * Created by sunshaogang on 1/15/16.
 */
public class DigitalClock extends android.widget.DigitalClock {
    Calendar mCalendar;
//    private final static String mFormat = "EEEE,MMMM-dd-yyyy hh:mm aa";//h:mm:ss aa
    private final static String mFormat = "yyyy-MM-dd EEEE aa hh:mm ";//h:mm:ss aa
    private FormatChangeObserver mFormatChangeObserver;

    private Runnable mTicker;
    private Handler mHandler;

    private boolean mTickerStopped = false;

    public DigitalClock(Context context) {
        super(context);
        initClock(context);
    }

    public DigitalClock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {
        Resources r = context.getResources();
        if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
        mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);

    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();

        mHandler = new Handler();

        mTicker = new Runnable() {
            @Override
            public void run() {
                if (mTickerStopped) {
                    return;
                }
                mCalendar.setTimeInMillis(System.currentTimeMillis());
                setText(DateFormat.format(mFormat, mCalendar));
                invalidate();
                long now = SystemClock.uptimeMillis();
                long next = now + (1000 - now % 1000);
                mHandler.postAtTime(mTicker, next);
            }
        };
        mTicker.run();
    }


    private class FormatChangeObserver extends ContentObserver {

        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {

        }
    }
}

