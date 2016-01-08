package com.sansheng.testcenter.utils;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import com.sansheng.testcenter.base.FormattedDateBuilder;

import java.text.SimpleDateFormat;

/**
 * Created by sunshaogang on 12/10/15.
 */
public class MeterUtilies {
    public static final int NAME_MAX_BYTE_LENGTH = 30;
    public static final String PAMAR_METER_TYPE = "meter_type";
    public static final String PARAM_METER = "meter";
    public static final int METER_TEST_TYPE_SINGLE = 0;
    public static final int METER_TEST_TYPE_TRIPLE = 1;
    public static String getDateShort(Context context, long time) {
        FormattedDateBuilder dateBuilder = new FormattedDateBuilder(context);
        return dateBuilder.formatShortDate(time).toString();
    }

    public static String getDateLong(Context context, long time) {
        FormattedDateBuilder dateBuilder = new FormattedDateBuilder(context);
        return dateBuilder.formatLongDateTime(time).toString();
    }

    public static String getSanShengDate(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(time);
        return dateString;
    }

    public static String getSanShengTime(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(time);
        return dateString;
    }

    public static String getCurrentTimeString(long time) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String dateString = formatter.format(time);
        return dateString;
    }

    public static int showFragment(FragmentManager manager, Fragment old, Fragment fragment, int id, int transition, String tag) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.setTransition(transition);
        if (old != null && old.isVisible()) {
            fragmentTransaction.hide(old);
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        }else {
            fragmentTransaction.add(id, fragment, tag);
        }
        fragmentTransaction.addToBackStack(null);
        final int transactionId = fragmentTransaction.commitAllowingStateLoss();
        return transactionId;
    }

//    public static boolean compareMeter(MeterData a, MeterData b){
//        if (a.mId != b.mId) {
//            return false;
//        } else if (a.mMeterID != b.mMeterID){
//            return false;
//        } else if (TextUtils.equals(a.mMeterName, b.mMeterName)){
//            return false;
//        } else if (a.mValueTime != b.mValueTime){
//            return false;
//        } else if (a.mReadTime != b.mReadTime){
//            return false;
//        } else if (a.mValz != b.mValz){
//            return false;
//        } else if (a.mDataType != b.mDataType){
//            return false;
//        } else if (a.isImportant != b.isImportant){
//            return false;
//        }
//        return true;
//    }
}
