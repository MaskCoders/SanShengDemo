package com.sansheng.testcenter.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.TestCenterApplication;
import com.telly.groundy.GroundyTask;
import com.telly.groundy.TaskResult;

import java.util.List;

/**
 * Created by sunshaogang on 12/16/15.
 */
public class Utilities {

    public static String TAG = "Utilities";
    
    public class ExampleTask extends GroundyTask {
        @Override
        protected TaskResult doInBackground() {
            // you can send parameters to the task using a Bundle (optional)
            String exampleParam = getStringArg("arg_name");

            // lots of code

            // return a TaskResult depending on the success of your task
            // and optionally pass some results back
            return succeeded().add("the_result", "some result");
        }
    }

    public static boolean checkNetwork() {
        Context context = TestCenterApplication.getInstance().getApplicationContext();
        if (!isNetworkAvailable(context)) {
            Log.w(TAG, "There is no network");
            Utility.showToast(context, R.string.no_available_network);
            return false;
        }
        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (null != ni) {
            if (ni.isAvailable()) {
                return true;
            }
        }
        return false;
    }


    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null || appProcesses.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static boolean equals(CharSequence a, CharSequence b) {
        if (a == b) return true;
        int length;
        if (a != null && b != null && (length = a.length()) == b.length()) {
            if (a instanceof String && b instanceof String) {
                return a.equals(b);
            } else {
                for (int i = 0; i < length; i++) {
                    if (a.charAt(i) != b.charAt(i)) return false;
                }
                return true;
            }
        }
        return false;
    }
}
