package com.sansheng.testcenter.demo.view;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.baidu.location.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.TestCenterApplication;

import java.util.List;

/**
 * Created by sunshaogang on 12/25/15.
 * get current location
 */
public class LocationActivity extends Activity {
    public BDLocationListener mListener = new CollectLocationListener();
    private LocationClient mLocationClient;
    private TextView LocationResult, ModeInfor;
    private Button startLocation;
    private RadioGroup selectMode;
    private LocationClientOption.LocationMode tempMode = LocationClientOption.LocationMode.Hight_Accuracy;
    private static final String tempcoor = "gcj02";
    private static final int Loacation_frequence = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_layout);
        mLocationClient = new LocationClient(TestCenterApplication.getInstance().getApplicationContext());  //声明LocationClient类
        mLocationClient.registerLocationListener(mListener);  //注册监听函数
        LocationResult = (TextView) findViewById(R.id.textView1);
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        ModeInfor = (TextView) findViewById(R.id.modeinfor);
        ModeInfor.setText(getString(R.string.hight_accuracy_desc));
        startLocation = (Button) findViewById(R.id.addfence);
        startLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initLocation();

                if (startLocation.getText().equals(getString(R.string.start_location))) {
                    mLocationClient.start();//定位SDK start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
                    startLocation.setText(getString(R.string.stop_location));
                } else {
                    mLocationClient.stop();
                    startLocation.setText(getString(R.string.start_location));
                }
            }
        });
        selectMode = (RadioGroup) findViewById(R.id.selectMode);
        selectMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

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
                ModeInfor.setText(ModeInformation);
            }
        });
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        mLocationClient.stop();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null)
            mLocationClient.unRegisterLocationListener(mListener);  //注册监听函数
    }

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

    /**
     * 实现BDLocationListener接口
     * BDLocationListener接口有2个方法需要实现： 1.接收异步返回的定位结果，参数是BDLocation类型参数。
     */
    public class CollectLocationListener implements BDLocationListener {

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
            List<Poi> list = location.getPoiList();// POI信息
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            logMsg(sb.toString());
            Log.e("ssg", sb.toString());
        }
    }

    public void logMsg(String str) {
        try {
            if (LocationResult != null)
                LocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
