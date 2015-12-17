package com.sansheng.testcenter.upgrade;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.TestCenterApplication;
import com.sansheng.testcenter.base.DialogFragmentFactory;
import com.sansheng.testcenter.base.DialogFragmentFactory.CheckVersionDialogFragment;
import com.sansheng.testcenter.utils.Utilities;
import com.sansheng.testcenter.utils.Utility;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hanyong on 14-8-20.
 */
public class AppUpgrade {

	private static final String TAG = "Upgrade";
	private static boolean DEBUG = false;

    public static final String PARAMETER_VERSION_NAME = "versionName";
    public static final String PARAMETER_VERSION_CODE = "versionCode";
    public static final String PARAMETER_CHANNEL_NAME = "channel";
    public static final String PARAMETER_PACKAGE_NAME = "packageName";
    public static final String PARAMETER_SYSTEN_VERSION = "sdkVersion";
    public static final String PARAMETER_DEVICE_ID = "deviceId";
    public static final String PARAMETER_LANGUAGE = "language";

    private static final String VERSION_INFO_APKSIZE = "size";
    private static final String VERSION_INFO_APK_ADDRESS = "url";
    private static final String VERSION_INFO_RELEASENOTE = "releaseNote";
    private static final String VERSION_INFO_STATE = "mandatory";

    public static final int MESSAGE_CHECK_RESULT_OK = 0;
    public static final int MESSAGE_NETWORK_UNCONNECT = 1;
    public static final int MESSAGE_CHECK_RESULT_NO_UPDATE = 2;
    public static final int MESSAGE_NETWORK_ERROR = 3;

    private Activity mActivity;

    private boolean isAuto = true;
    private AppInfo mAppInfo;
    private UpdateTask mUpdateTask;
    DownloadManager dm;

    SharedPreferences prefs;

    public static final String DOWNLOAD_ID ="downloadid";
    public static final String CHECK_STATE ="state";
    public static final String UPGRADE_IGNORE_THE_VERSION = "ignoreTheVersion";

    private DialogFragment mInstallAlertDialog;
    private CheckVersionDialogFragment mDialog;
    private DialogFragment mUpdateDialog;

    public Activity getActivity() {
        return mActivity;
    }

    public enum State {
		INITIAL(1),  DOWNLOADING(2), DOWNLOADCOMPLETE(3);

		private int value;

		State(int arg1) {
			value = arg1;
		}

		public int getValue() {
			return value;
		}
	}

    public void check(boolean auto){
        isAuto = auto;
        if(mUpdateTask != null && !mUpdateTask.isCancelled()){
            mUpdateTask.cancel(true);
        }
        int state = getStatus();
        if(state == State.DOWNLOADING.getValue()){
            if(queryDownloadStatus(prefs.getLong(DOWNLOAD_ID, 0))){
                return;
            }
        }

        mUpdateTask = new UpdateTask();
        mUpdateTask.execute();
    }

    public boolean isAuto() {
        return isAuto;
    }

    public AppUpgrade(Activity act){
        mActivity = act;
        mAppInfo = AppInfo.getTheAppInfo(mActivity);
        prefs = mActivity.getSharedPreferences(AppInfo.SP_STATISTICS,
				Context.MODE_PRIVATE);
        dm = (DownloadManager) mActivity
                .getSystemService(Context.DOWNLOAD_SERVICE);
        if(mAppInfo.getAppVersionCode() == prefs.getInt(PARAMETER_VERSION_CODE,0)){
            setInitial();
        }
    }
    
    class UpdateTask extends AsyncTask<Void,String,UpgradeInfo> {


        protected void onPreExecute() {
            if(isAuto) return;
            mDialog = CheckVersionDialogFragment.newInstance();
            mDialog.show(mActivity.getFragmentManager(),CheckVersionDialogFragment.TAG);
        }

        @Override
        protected UpgradeInfo doInBackground(Void... voids) {
            UpgradeInfo info = new UpgradeInfo();
            if(!Utilities.checkNetwork()){
                info.status = MESSAGE_NETWORK_UNCONNECT;
                return info;
            }
            //TODO:从网络获取最新版本的信息
//            String url = URLMapController.getUpgradeURL(mActivity);
//            if (TextUtils.isEmpty(url)) {
//                LogUtils.d(TAG, "url fetch error");
//                return info;
//            }
//            String res = KingsoftHttpUtils.getInstance(EmailApplication.getInstance().getApplicationContext()).sentHttpPostRequest(url, getCheckParams());
//            if(KingsoftHttpUtils.isErrorResult(res)){
//                if(KingsoftHttpUtils.isNetErrorResult(res)){
//                    info.status = MESSAGE_NETWORK_ERROR;
//                }else{
//                    info.status = MESSAGE_CHECK_RESULT_NO_UPDATE;
//                }
//                return info;
//            }
//            info = paraseVersionInfo(res);
            return info;
        }

        @Override
        protected void onPostExecute(UpgradeInfo info) {
        	if(DEBUG)
        		Log.d(TAG, info.toString());
            if(mDialog != null
                    && mDialog.getDialog() != null
                    && mDialog.getDialog().isShowing()){
                mDialog.dismissAllowingStateLoss();
            }
            if(mActivity.isFinishing()){
                return;
            }
            if(info == null){
                return;
            }
            if(info.status == MESSAGE_CHECK_RESULT_OK){
                checkAndAlertUpgradeDialog(info);
                return;
            }
            AppInfo.HAS_NEW_VERSION = false;
            if(isAuto){
                return;
            }
            int message = R.string.no_update;
            switch(info.status){
                case MESSAGE_NETWORK_UNCONNECT:
                    message = R.string.upgrade_network_unconnect;
                    break;
                case MESSAGE_NETWORK_ERROR:
                    message = R.string.upgrade_network_error;
                    break;
                case MESSAGE_CHECK_RESULT_NO_UPDATE:
                    message = R.string.no_update;
                    break;
            }
            Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
        }
    }

    public void checkAndAlertUpgradeDialog(UpgradeInfo info){
        AppInfo.HAS_NEW_VERSION = true;

        if(isAuto && info.isOrdinary()){
            return;
        }

        int state = prefs.getInt(CHECK_STATE, State.INITIAL.getValue());

        int versionCode = prefs.getInt(PARAMETER_VERSION_CODE,0);

        if(versionCode == info.versionCode){
            if(isAuto && !isNotify()
                    && !info.isForce()
                    && state != State.DOWNLOADCOMPLETE.getValue()){
                return;
            }
            if(state == State.DOWNLOADCOMPLETE.getValue()){
                File f = new File(getDownLoadPath());
                if(f.exists() && f.isFile()){
                    showInstallAlertDialog(info);
                    return;
                }
                setInitial();
            }
        }
        mUpdateDialog = DialogFragmentFactory.createDialogFragment(DialogFragmentFactory.FRAGEMENT_TYPE_UPDATE,this, info);
        try {
	        mUpdateDialog.show(mActivity.getFragmentManager(),
	                DialogFragmentFactory.UpgradeDialogFragment.TAG);
		} catch (Exception e) {
			// TODO: handle exception
		}
        setNotify(true);
        prefs.edit().putInt(PARAMETER_VERSION_CODE, info.versionCode).commit();
    }

    public void showInstallAlertDialog(UpgradeInfo info){
        if(mActivity.isFinishing()){
            return;
        }
        mInstallAlertDialog
                = DialogFragmentFactory.createDialogFragment(DialogFragmentFactory.FRAGEMNT_TYPE_INSTALL, this,info);
		mInstallAlertDialog.show(mActivity.getFragmentManager(),
                DialogFragmentFactory.InstallAlertDialogFragment.TAG);
    }

	public void startDowndLoad(Uri uri) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS).mkdirs();
			String fileName = getDownLoadPath();
            dm.remove(prefs.getLong(DOWNLOAD_ID, 0));
            File f = new File(fileName);
            if (f.exists()) {
                f.delete();
            }
			DownloadManager.Request dwreq = new DownloadManager.Request(uri);
			dwreq.setDescription(mActivity.getResources().getString(
					R.string.upgrade_title));
            try {
                dwreq.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS, "wpsMail.apk");
            } catch (IllegalStateException e) {
                Utility.showToast(TestCenterApplication.getInstance().getApplicationContext(),
                        R.string.update_failed);
                e.printStackTrace();
                return;
            }
			//dwreq.setNotificationVisibility(0);
			dwreq.setVisibleInDownloadsUi(false);
			long lastDownload = dm.enqueue(dwreq);
            Toast.makeText(mActivity, R.string.start_download, Toast.LENGTH_LONG).show();
            prefs.edit().putLong(DOWNLOAD_ID, lastDownload).commit();
			setStatus(State.DOWNLOADING);
		} else {
            Toast.makeText(mActivity, R.string.no_sdcard, Toast.LENGTH_LONG)
					.show();
		}
	}

	public void installNow() {
		String rootpath = Environment.getExternalStorageDirectory().getPath()
				+ "/" + Environment.DIRECTORY_DOWNLOADS + "/";
		File file = new File(rootpath + "wpsMail.apk");
		if (!file.exists()) {
			setInitial();
			return;
		}
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		mActivity.startActivity(intent);
	}

    public String getDownLoadPath() {
		String fileName = Environment.getExternalStorageDirectory().getPath()
				+ "/" + Environment.DIRECTORY_DOWNLOADS + "/" + "wpsMail.apk";
		return fileName;
	}

    private void setInitial() {
        prefs.edit().putInt(PARAMETER_VERSION_CODE,0);
        prefs.edit().putInt(DOWNLOAD_ID,0);
		setStatus(State.INITIAL);
	}

    public Boolean isNotify() {
		return prefs.getBoolean(UPGRADE_IGNORE_THE_VERSION, true);
	}

	public void setNotify(Boolean bNotify) {
		prefs.edit().putBoolean(UPGRADE_IGNORE_THE_VERSION, bNotify).commit();
	}

    private int getStatus() {
		return prefs.getInt(CHECK_STATE, State.INITIAL.getValue());
	}

	private void setStatus(State state) {
		prefs.edit().putInt(CHECK_STATE, state.getValue()).commit();
	}

	private boolean queryDownloadStatus(long id) {
		boolean retVal = false;
		Cursor c = null;
		try {
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(id);
			c = dm.query(query);
			if (c != null && c.moveToFirst()) {
				int status = c.getInt(c
						.getColumnIndex(DownloadManager.COLUMN_STATUS));
				switch (status) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    setStatus(State.DOWNLOADCOMPLETE);
                    break;
				case DownloadManager.STATUS_RUNNING:
                case DownloadManager.STATUS_PAUSED:
                case DownloadManager.STATUS_PENDING:
                    if(!isAuto)
                        Toast.makeText(mActivity, R.string.downloading, Toast.LENGTH_LONG).show();
                    retVal = true;
					break;
                default:
					dm.remove(id);
                    setInitial();
					break;
				}
			}else{
                setInitial();
            }
		} catch (Exception e) {
			setInitial();
			e.printStackTrace();
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return retVal;
	}

    public UpgradeInfo paraseVersionInfo(String json) {
        UpgradeInfo info = new UpgradeInfo();
        if(json == null || json.equals("{}")){
            return info;
        }
        try {
            JSONObject jsonObject = new JSONObject(json);
            info.url = jsonObject.optString(VERSION_INFO_APK_ADDRESS);
            if(TextUtils.isEmpty(info.url)){
                return info;
            }
            info.status = MESSAGE_CHECK_RESULT_OK;
            info.apkSize = (float) jsonObject.optDouble(VERSION_INFO_APKSIZE);
            info.isForce = jsonObject.optInt(VERSION_INFO_STATE);
            info.releaseNote = jsonObject.optString(VERSION_INFO_RELEASENOTE);
            info.versionName = jsonObject.optString(PARAMETER_VERSION_NAME);
            info.versionCode = jsonObject.optInt(PARAMETER_VERSION_CODE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return info;
    }

    protected List<NameValuePair> getCheckParams() {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair(PARAMETER_VERSION_NAME, mAppInfo.getAppVersionName()));
        params.add(new BasicNameValuePair(PARAMETER_VERSION_CODE, String.valueOf(mAppInfo.getAppVersionCode())));
        params.add(new BasicNameValuePair(PARAMETER_CHANNEL_NAME, mAppInfo.getAppChannelName()));
        params.add(new BasicNameValuePair(PARAMETER_PACKAGE_NAME, mAppInfo.getAppPackageName()));
        params.add(new BasicNameValuePair(AppInfo.ST_APP_ID, mAppInfo.getAppID()));
        return params;
    }

    public static class UpgradeInfo {

        public int status = MESSAGE_CHECK_RESULT_NO_UPDATE;

        public float apkSize;
        public String versionName;
        public int versionCode;
        public String releaseNote;
        public String url;
        public int isForce = 0;

        public String getSizeUseMB() {
            DecimalFormat df = new DecimalFormat("0.00");
            String fileSize = df.format(apkSize);
            return fileSize;
        }
        public boolean isForce(){
            return isForce == 1;
        }

        public boolean isOrdinary(){
            return isForce == 0;
        }

        public boolean isPush(){
            return isForce == 2;
        }

        public void write(SharedPreferences prefs) {
            if (TextUtils.isEmpty(url)) {
                return;
            }
        }

        @Override
        public String toString() {
        	// TODO Auto-generated method stub
        	StringBuilder sb = new StringBuilder();
        	sb.append("[");
        	sb.append("status=");
        	switch (status) {
			case MESSAGE_CHECK_RESULT_OK:
				sb.append("MESSAGE_CHECK_RESULT_OK");
				break;
			case MESSAGE_NETWORK_UNCONNECT:
				sb.append("MESSAGE_NETWORK_UNCONNECT");
				break;
			case MESSAGE_CHECK_RESULT_NO_UPDATE:
				sb.append("MESSAGE_CHECK_RESULT_NO_UPDATE");
				break;
			case MESSAGE_NETWORK_ERROR:
				sb.append("MESSAGE_NETWORK_ERROR");
				break;
			default:
				break;
			}
        	sb.append(",");
        	sb.append("versionName=");
        	sb.append(versionName);
        	sb.append(",");
        	sb.append("versionCode=");
        	sb.append(versionCode);
        	sb.append(",");
        	sb.append("releaseNote=");
        	sb.append(releaseNote);
        	sb.append(",");
        	sb.append("url=");
        	sb.append(url);
        	sb.append(",");
        	sb.append("isForce=");
        	sb.append(isForce);
        	sb.append("]");
        	return sb.toString();
        }
    }
    
    public void onDestory(){
    	cancelTasks();
    	// 当dialog已经弹出，切换activity的时候，dialog会出现null point.
    	// 但是该函数已经做了 null point处理，不清楚原因，所以做try catch处理
    	try {
        	dismissDialogs();
		} catch (Exception e) {
			// TODO: handle exception
		}

    	mActivity = null;
    }
    
    private void dismissDialogs(){
        if(mDialog != null){
        	mDialog.dismiss();
        	mDialog = null;
        }
        if(mUpdateDialog !=null){
        	mUpdateDialog.dismiss();
        	mUpdateDialog = null;
        }
        
        if(mInstallAlertDialog!=null){
        	mInstallAlertDialog.dismiss();
        	mInstallAlertDialog = null;
        }
    }
    
    private void cancelTasks(){
    	if(mUpdateTask!=null){
    		mUpdateTask.cancel(true);
    		mUpdateTask = null;
    	}
    }
}
