package com.sansheng.testcenter.upgrade;

/**
 * Created by sunshaogang on 12/17/15.
 */

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

public class AppInfo {
    protected static AppInfo mInstance;

    public static boolean HAS_NEW_VERSION = false;

    public static final String ST_APP_ID = "appId";
    public static final String SP_STATISTICS = "xieyi";

    private String appVersionName;
    private String appChannelName;
    private String appCompanyName;
    private String appID;
    private int appVersionCode;

    private static final String CHANNEL_NAME = "channel";
    private static final String PRODUCT_NAME = "product";
    private static final String UNKNOW_PRODUCT_NAME = "unknownProduct";
    protected String DeviceID;
    protected Context mAppContext;

    private AppInfo(Context con) {
        this.mAppContext = con.getApplicationContext();
    }

    public static AppInfo getTheAppInfo(Context con) {
        if(mInstance == null) {
            Class var1 = AppInfo.class;
            synchronized(AppInfo.class) {
                if(mInstance == null) {
                    mInstance = new AppInfo(con);
                }
            }
        }

        return mInstance;
    }


    public String getAppVersionName() {
        if(this.appVersionName == null) {
            PackageInfo pi = null;

            try {
                pi = this.mAppContext.getPackageManager().getPackageInfo(this.mAppContext.getPackageName(), 0);
                this.appVersionName = pi.versionName;
            } catch (PackageManager.NameNotFoundException var3) {
                var3.printStackTrace();
            }
        }

        return this.appVersionName;
    }

    public int getAppVersionCode() {
        if(this.appVersionCode == 0) {
            PackageInfo pi = null;

            try {
                pi = this.mAppContext.getPackageManager().getPackageInfo(this.mAppContext.getPackageName(), 0);
                this.appVersionCode = pi.versionCode;
            } catch (PackageManager.NameNotFoundException var3) {
                var3.printStackTrace();
            }
        }

        return this.appVersionCode;
    }

    public String getAppChannelName() {
        if(this.appChannelName == null) {
            try {
                ApplicationInfo e = this.mAppContext.getPackageManager().getApplicationInfo(this.mAppContext.getPackageName(), 128);
                this.appChannelName = e.metaData.getString("channel");
            } catch (Exception var2) {
                this.appChannelName = "";
            }
        }

        return this.appChannelName;
    }

    public String getAppProductName() {
        if(TextUtils.isEmpty(this.appCompanyName)) {
            try {
                PackageManager e = this.mAppContext.getPackageManager();
                String packageName = this.mAppContext.getPackageName();
                ApplicationInfo applicationInfo = e.getApplicationInfo(packageName, 128);
                this.appCompanyName = applicationInfo.metaData.getString("product");
            } catch (Exception var4) {
                this.appCompanyName = "unknownProduct";
            }
        }

        return this.appCompanyName;
    }

    public String getAppID() {
        if(this.appID == null) {
            try {
                ApplicationInfo e = this.mAppContext.getPackageManager().getApplicationInfo(this.mAppContext.getPackageName(), 128);
                this.appID = e.metaData.getString("appId");
            } catch (Exception var2) {
                this.appID = "";
            }
        }

        return this.appID;
    }

    public String getAppPackageName() {
        return this.mAppContext.getPackageName();
    }
}
