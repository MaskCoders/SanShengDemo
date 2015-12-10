package com.example.demo.app;

import android.content.Context;

import java.text.SimpleDateFormat;

/**
 * Created by sunshaogang on 12/10/15.
 */
public class MeterUtilies {
    public static String getDateShort(Context context, long time) {
        FormattedDateBuilder dateBuilder = new FormattedDateBuilder(context);
        return dateBuilder.formatShortDate(time).toString();
    }

    public static String getDateLong(Context context, long time) {
        FormattedDateBuilder dateBuilder = new FormattedDateBuilder(context);
        return dateBuilder.formatLongDateTime(time).toString();
    }
    public static String getSanShengDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(time);
        return dateString;
    }
}
