package com.sansheng.testcenter.location;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.baidu.location.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.TestCenterApplication;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.CustomThreadPoolFactory;
import com.sansheng.testcenter.base.view.ComposeDialogFragmentFactory;
import com.sansheng.testcenter.base.view.DialogFragmentCallback;
import com.sansheng.testcenter.base.view.PoiSelectDialog;
import com.sansheng.testcenter.module.LocationInfo;
import com.sansheng.testcenter.utils.Utilities;
import com.sansheng.testcenter.utils.Utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by sunshaogang on 12/25/15.
 * collect scene information
 */
public class ComposeLocationActivity extends BaseActivity implements PoiSelectDialog.PoiSelectCallback {

    public static String TAG = "ComposeLocationActivity";
    private Uri photoUri;
    private File photoImage;
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 201;
    public static final int SELECT_IMAGE_FROM_GALLERY = 202;

    private static final ThreadFactory sThreadFactory = new CustomThreadPoolFactory("LocationInfoAdapter");
    private ExecutorService sThreadPool = Executors.newSingleThreadExecutor(sThreadFactory);
    private int mThumbnailWidth;
    private int mThumbnailHeight;

    //location
    public BDLocationListener mListener = new LocationListener();
    private LocationClient mLocationClient;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private static final String tempcoor = "gcj02";
    private static final int Loacation_frequence = 3000;
    private static List<Poi> mPoiList = new ArrayList<Poi>();
    //location
    private RadioGroup mSelectMode;
    private TextView mLocationBtn;
    private TextView mPhotoBtn;
    private TextView mAddBtn;
    private LinearLayout mPicsLayout;
    private TextView mAddressView;
    private TextView mPoiView;
    private TextView mUpdateTimesView;

    private LocationInfo mLocationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBar(LOCATION_INFO);
        hideBottomLog();
//        getActionBar().hide();
        mThumbnailWidth = getResources().getDimensionPixelSize(R.dimen.compose_thumbnail_width);
        mThumbnailHeight = getResources().getDimensionPixelSize(R.dimen.compose_thumbnail_height);
        initData();
        setTitle("现场采集");
        mLocationClient = new LocationClient(TestCenterApplication.getInstance().getApplicationContext());  //声明LocationClient类
        mLocationClient.registerLocationListener(mListener);  //注册监听函数
        mLocationInfo = new LocationInfo();
    }

    @Override
    protected void initButtonList() {
        View inflate = getLayoutInflater().inflate(R.layout.compose_location_control_layout, null);
        mSelectMode = (RadioGroup) inflate.findViewById(R.id.selectMode);
        mLocationBtn = (TextView) inflate.findViewById(R.id.compose_location);
        mPhotoBtn = (TextView) inflate.findViewById(R.id.compose_take_photo);
        mAddBtn = (TextView) inflate.findViewById(R.id.compose_add_to_db);
        mPhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPicture();
            }
        });
        mLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initLocation();
                if (mLocationBtn.getText().equals(getString(R.string.start_location))) {
                    mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
                    mLocationBtn.setText(getString(R.string.stop_location));
                } else {
                    mLocationClient.stop();
                    mLocationBtn.setText(getString(R.string.start_location));
                }
            }
        });
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLocationInfo.mUriList.size() == 0 || TextUtils.isEmpty(mLocationInfo.mAddress)) {
                    Utility.showToast(ComposeLocationActivity.this, "信息不完整，无法添加");
                    return;
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(LocationUtilies.COMPOSE_LOCATION_LOCATIONINFO, mLocationInfo);
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                ComposeLocationActivity.this.finish();
            }
        });

        mSelectMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                String ModeInformation = null;
                switch (checkedId) {
                    case R.id.radio_hight:
                        tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
                        ModeInformation = getString(R.string.hight_accuracy_desc);
                        break;
                    case R.id.radio_low:
                        tempMode = LocationClientOption.LocationMode.Battery_Saving;
                        ModeInformation = getString(R.string.saving_battery_desc);
                        break;
                    case R.id.radio_device:
                        tempMode = LocationClientOption.LocationMode.Device_Sensors;
                        ModeInformation = getString(R.string.device_sensor_desc);
                        break;
                    default:
                        break;
                }
                Utility.showToast(ComposeLocationActivity.this, ModeInformation);
//                ModeInfor.setText(ModeInformation);
            }
        });
        main_button_list.addView(inflate);
    }

    @Override
    protected void initConnList() {

    }

    @Override
    protected void initCenter() {
        View inflate = getLayoutInflater().inflate(R.layout.compose_location_center_layout, null);
        mPicsLayout = (LinearLayout) inflate.findViewById(R.id.compose_pics);
        mAddressView = (TextView) inflate.findViewById(R.id.compose_address);
        mPoiView = (TextView) inflate.findViewById(R.id.compose_poi);
        mUpdateTimesView = (TextView) inflate.findViewById(R.id.compose_update_time);
        main_info.addView(inflate);
    }

    private void initData() {
//        main_status_info.setText("has conn the ser : ip-192.168,134,77 :  port-8001");
//        main_sort_log.setText("show the sort log");
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLocationClient.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null)
            mLocationClient.unRegisterLocationListener(mListener);  //注册监听函数
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = null;
            if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
                uri = photoUri;
//                try {
//                    String a = MediaStore.Images.Media.insertImage(getContentResolver(), photoImage.getAbsolutePath(),uri.getLastPathSegment(),"");
//                    Log.e("ssg", "a = " + a);
//                    uri = Uri.parse(a);
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                msc.connect();


//                String[] proj = {MediaStore.Images.Media.DATA};
//                Cursor cursor = managedQuery(uri, proj, null, null, null);
//                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//                cursor.moveToFirst();
//                String path = cursor.getString(column_index);
//                Log.e("ssg", "db path = " + path);
            } else if (requestCode == SELECT_IMAGE_FROM_GALLERY) {
                uri = data.getData();
            }
            //TODO:已经得到想要的URI和path，(ˇˍˇ） 想怎么处理都可以。
            if (uri != null && !TextUtils.isEmpty(uri.toString())) {
                String path = Utilities.getPath(this, uri);
                mLocationInfo.mUriList.add(uri.toString());
//            ContentValues values = new ContentValues();
//            values.put(LocationInfo.URI_LIST, ModuleUtilites.listToJson(mLocationInfo.mUriList));
//            mLocationInfo.saveOrUpdate(this, values);
//                mUrisView.setText(mLocationInfo.mUriList.toString());
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mThumbnailWidth, mThumbnailHeight);
                int margin = getResources().getDimensionPixelSize(R.dimen.compose_thumbnail_margin);
                params.setMargins(margin, margin, margin, margin);
                imageView.setLayoutParams(params);
                ThumbnailUtility.loadBitmap(sThreadPool, uri.toString(),
                        imageView, mThumbnailWidth, mThumbnailHeight, R.drawable.circle_message_photo_unload, this);
                mPicsLayout.addView(imageView);
                Log.e("ssg", "uri = " + uri);
                Log.e("ssg", "the path is :" + path);
                Log.e("ssg", "the photoImage is :" + photoImage);
                Log.e("ssg", "the photoUri is :" + photoUri);
            }
//            String[] proj = {MediaStore.Images.Media.DATA};
//            Cursor cursor = managedQuery(uri, proj, null, null, null);
//            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            cursor.moveToFirst();
//            String path = cursor.getString(column_index);
////            Bitmap bmp = BitmapFactory.decodeFile(path);
//            Log.e("ssg", "the query path is :" + path);
        } else {
            Utility.showToast(this, "请重新选择图片");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPoiSelectClick(int position) {
        if (mLocationInfo != null && mPoiList.size() > position) {
            mLocationInfo.mPoi = mPoiList.get(position).getName();
            //停止定位
            mLocationClient.stop();
            mLocationBtn.setText(getString(R.string.start_location));
            mAddressView.setText(mLocationInfo.mAddress);
            mPoiView.setText(mLocationInfo.mPoi);
            mUpdateTimesView.setText(mLocationInfo.mUpdateTime);
        }
    }

    /**
     * 自定义文件名
     *
     * @param context
     */
    private void takePhoto(Context context) {
        String fileName = LocationUtilies.generateRandomString(LocationUtilies.RANDOM_FILE_NAME_LENGTH) + ".jpg";
        File dir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM).getPath());
        if (dir != null) {
            File imageFile = new File(dir.getAbsolutePath(), fileName);
            Uri imageFileUri = Uri.fromFile(imageFile);
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, imageFileUri);
            photoUri = imageFileUri;
            startActivityForResult(intent, LocationUtilies.START_CAMERA);
        }
    }

    private void selectPicture() {
        if (mLocationInfo != null && mLocationInfo.mUriList.size() == 4) {
            Utility.showToast(this, "最多添加四张图片");
            return;
        }
        DialogFragmentCallback callback = new DialogFragmentCallback(this) {

            @Override
            public void onItemSelected(int pos) {
                Intent intent = null;
                if (pos == 0) {
                    //系统接口
                    Log.e("ssg", "版本 =  " + Build.VERSION.SDK_INT);
//                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setType("image/*");
//                    } else {
//                        intent = new Intent(Intent.ACTION_PICK,
//                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    }
                    startActivityForResult(intent, SELECT_IMAGE_FROM_GALLERY);

                } else if (pos == 1) {
                    intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    Uri uri = getImageFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                } else {
                    Log.w(TAG, "unknown position: " + pos);
                }
            }
        };
        Dialog df = ComposeDialogFragmentFactory.getInstance().createSelectImageDialog(this, callback);
        df.show();
    }

    private Uri getImageFileUri() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File mediaStorageDir = LocationUtilies.getSanShengPicFolder();
            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d(TAG, "failed to create directory");
                    return null;
                }
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            photoImage = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
            photoUri = Uri.fromFile(photoImage);
            return photoUri;
        }
        return null;
    }

    final MediaScannerConnection msc = new MediaScannerConnection(this, new MediaScannerConnection.MediaScannerConnectionClient() {
        public void onMediaScannerConnected() {
            msc.scanFile(LocationUtilies.getSanShengPicFolder().getAbsolutePath(), "image/*");
        }

        public void onScanCompleted(String path, Uri uri) {

            msc.disconnect();
        }
    });

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(tempMode);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType(tempcoor);//可选，默认gcj02，设置返回的定位结果坐标系，
        option.setScanSpan(Loacation_frequence);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        mLocationClient.setLocOption(option);
    }

    @Override
    public void setValue(double[] values) {

    }

    public class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");// 位置语义化信息
            sb.append(location.getLocationDescribe());
            mPoiList = location.getPoiList();// POI信息
            if (mPoiList != null) {
                sb.append("\npoilist size = : ");
                sb.append(mPoiList.size());
                for (Poi p : mPoiList) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            if (mLocationInfo != null &&
                    (mLocationInfo.mLocType == BDLocation.TypeGpsLocation ||
                            mLocationInfo.mLocType == BDLocation.TypeNetWorkLocation)) {
                return;
            }
            mLocationInfo.restoreFromLocation(location);
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                showPoiDialog(location.getPoiList());
            }

        }
    }

    private void showPoiDialog(List<Poi> list) {
        if (list == null || list.size() == 0) {
            return;
        }
        PoiSelectDialog poiSelectDialog = new PoiSelectDialog(this, list);
        poiSelectDialog.show(getFragmentManager(), "select_poi");
    }
}
