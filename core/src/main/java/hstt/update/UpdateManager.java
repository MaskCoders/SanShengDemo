//package hstt.update;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import hstt.core.R;
//
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.app.Dialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Handler;
//import android.os.Message;
//import android.view.KeyEvent;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import hstt.core.Config;
//
//public class UpdateManager {
//
//  private Context mContext;
//  // 设置里面调用为true
//  // 开始检测调用为false
//  private boolean notnewshow;
//  private int serverVersion;
//  private String updateMsg = "发现新版本,建议立即更新使用!";
//  // 返回的安装包url
//  private String apkUrl;
//  private Dialog noticeDialog;
//
//  private Dialog downloadDialog;
//    /* 下载包安装路径 */
//  // private static final String savePath = "/sdcard/updatedemo/";
//
//  // private static final String saveFileName = savePath +
//  // "UpdateDemoRelease.apk";
//  private static final String savePath = FileManager.getHiborSaveFilePath(Config.UPDATEPATH, "");
//
//  private static final String saveFileName = FileManager.getHiborSaveFilePath(Config.UPDATEPATH, "Update.apk");
//
//  /* 进度条与通知ui刷新的handler和msg常量 */
//  private ProgressBar mProgress;
//
//  private static final int DOWN_UPDATE = 1;
//
//  private static final int DOWN_OVER = 2;
//
//  private int progress;
//
//  private Thread downLoadThread;
//
//  private boolean interceptFlag = false;
//
//  // 强制更新
//
//  private boolean forceUpdate = false;
//
//  private Handler mHandler = new Handler() {
//    public void handleMessage(Message msg) {
//      switch (msg.what) {
//        case DOWN_UPDATE:
//          mProgress.setProgress(progress);
//          break;
//        case DOWN_OVER:
//
//          installApk(mContext);
//          break;
//        default:
//          break;
//      }
//    }
//
//    ;
//  };
//
//  public UpdateManager(Context context, boolean notnewshow) {
//    this.mContext = context;
//    this.notnewshow = notnewshow;
//  }
//
//  // 外部接口让主Activity调用
//  public void checkUpdateInfo() {
//    System.out.println("savePath=" + savePath);
//
//    new myAsyncTask().execute();
//  }
//
//  private class myAsyncTask extends AsyncTask<Void, Void, Void> {
//    private String s;
//
//    @Override
//    protected Void doInBackground(Void... params) {
//      s = HttpGetUtils.executeHttpGet(mContext, Config.URL_VERSION + Config.Company + "/version.txt");
//
//      return null;
//    }
//
//    @Override
//    protected void onPostExecute(Void result) {
//      // bug fix
//      if (s == null) {
//        Toast.makeText(mContext, "无法连接网络！", Toast.LENGTH_LONG).show();
//        return;
//      }
//      // bug fix end
//
//      DataVersion dv = DataVersion.decodeJson(s);
//
//      if (dv.getStatus() != 0) {
//        Toast.makeText(mContext, "无法连接网络！", Toast.LENGTH_LONG).show();
//        return;
//      }
//      serverVersion = dv.getVersionCode();
//      apkUrl = dv.getDownloadurl();
//      forceUpdate = dv.getMustUpdate().equals("yes");
//      super.onPostExecute(result);
//
//      PackageInfo packageInfo = null;
//      try {
//        packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
//      } catch (NameNotFoundException e) {
//
//        e.printStackTrace();
//      }
//      if (packageInfo != null) {
//        int localVersion = packageInfo.versionCode;
//        System.out.println("localVersion" + localVersion);
//        if (localVersion < serverVersion) {
//          showNoticeDialog();
//        } else {
//
//          if (notnewshow) {
//            showAlreadyNew();
//          }
//          // else {
//          // ((Activity)mContext).finish();
//          // }
//        }
//      }
//    }
//
//  }
//
//  private void showAlreadyNew() {
//
//    String versionName = null;
//    PackageInfo packageInfo = null;
//    try {
//      packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
//    } catch (NameNotFoundException e) {
//
//      e.printStackTrace();
//    }
//    if (packageInfo != null) {
//
//      versionName = packageInfo.versionName;
//    }
//
//    AlertDialog.Builder builder = new Builder(mContext);
//    builder.setTitle("软件版本更新");
//    builder.setMessage(versionName == null ? "" : "当前版本：" + versionName + "\n" + "已经是最新版本！");
//    builder.setPositiveButton("确定", null);
//
//    AlertDialog d = builder.create();
//    d.show();
//  }
//
//  private void showNoticeDialog() {
//    AlertDialog.Builder builder = new Builder(mContext);
//    builder.setTitle("软件版本更新");
//    builder.setMessage(updateMsg);
//    builder.setPositiveButton("下载", new OnClickListener() {
//      @Override
//      public void onClick(DialogInterface dialog, int which) {
//        dialog.dismiss();
//        showDownloadDialog();
//      }
//    });
//
//    // 强制更新，禁止用户跳过更新
//    if (!forceUpdate) {
//      builder.setNegativeButton("以后再说", new OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//          dialog.dismiss();
//
//          // if (!notnewshow) {
//          // ((Activity)mContext).finish();
//          // }
//        }
//      });
//    }
//
//    // add for force update
//    builder.setCancelable(false);
//    // 避免按了搜索键退出而使用现在的版本
//    builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
//      @Override
//      public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//          return true;
//        } else {
//          return false; // 默认返回 false
//        }
//      }
//    });
//    // add for force update end
//    noticeDialog = builder.create();
//    noticeDialog.show();
//  }
//
//  private void showDownloadDialog() {
//    AlertDialog.Builder builder = new Builder(mContext);
//    builder.setTitle("软件版本更新");
//
//    final LayoutInflater inflater = LayoutInflater.from(mContext);
//    View v = inflater.inflate(R.layout.update_progress, null);
//    mProgress = (ProgressBar) v.findViewById(R.id.progress);
//
//    builder.setView(v);
//
//    // add for force update
//    if (!forceUpdate) {
//      // add for force update end
//      builder.setNegativeButton("取消", new OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//          dialog.dismiss();
//          interceptFlag = true;
//        }
//      });
//    }
//
//    // add for force update
//    builder.setCancelable(false);
//    // 避免按了搜索键退出而使用现在的版本
//    builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
//      @Override
//      public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_SEARCH) {
//          return true;
//        } else {
//          return false; // 默认返回 false
//        }
//      }
//    });
//    // add for force update end
//    downloadDialog = builder.create();
//    downloadDialog.show();
//
//    downloadApk();
//  }
//
//  private Runnable mdownApkRunnable = new Runnable() {
//    @Override
//    public void run() {
//      try {
//        URL url = new URL(apkUrl);
//
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.connect();
//        int length = conn.getContentLength();
//        InputStream is = conn.getInputStream();
//
//        File file = new File(savePath);
//        if (!file.exists()) {
//          file.mkdir();
//        }
//        String apkFile = saveFileName;
//        File ApkFile = new File(apkFile);
//        FileOutputStream fos = new FileOutputStream(ApkFile);
//
//        int count = 0;
//        byte buf[] = new byte[1024];
//
//        do {
//          int numread = is.read(buf);
//          count += numread;
//          progress = (int) (((float) count / length) * 100);
//          // 更新进度
//          mHandler.sendEmptyMessage(DOWN_UPDATE);
//          if (numread <= 0) {
//            // 下载完成通知安装
//            mHandler.sendEmptyMessage(DOWN_OVER);
//            break;
//          }
//          fos.write(buf, 0, numread);
//        }
//        while (!interceptFlag);// 点击取消就停止下载.
//
//        fos.close();
//        is.close();
//      } catch (MalformedURLException e) {
//        e.printStackTrace();
//      } catch (IOException e) {
//        e.printStackTrace();
//      }
//
//    }
//  };
//
//  /**
//   * 下载apk
//   *
//   * @param url
//   */
//
//  private void downloadApk() {
//    downLoadThread = new Thread(mdownApkRunnable);
//    downLoadThread.start();
//  }
//
//  /**
//   * 安装apk
//   *
//   * @param url
//   */
//  private void installApk(Context context) {
//    // add for force update
//    GlobalParameters.getInstance(context).put("forceupload", saveFileName);
//    // add for force update end
//
//    File apkfile = new File(saveFileName);
//    if (!apkfile.exists()) {
//      return;
//    }
//    Intent i = new Intent(Intent.ACTION_VIEW);
//    // bug fix @2015-8-27
//    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 以新压入栈
//    i.addCategory("android.intent.category.DEFAULT");
//    // bug fix @2015-8-27 end
//
//    i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
//    mContext.startActivity(i);
//
//  }
//}