package com.sansheng.testcenter.datamanager;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.metertest.CollectSelectDialog;
import com.sansheng.testcenter.base.CustomThreadPoolFactory;
import com.sansheng.testcenter.base.MeterSelectDialog;
import com.sansheng.testcenter.base.view.SettingsRadioDialog;
import com.sansheng.testcenter.base.view.UIRevisableView;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.utils.MeterUtilies;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by sunshaogang on 1/14/16.
 */
public class MeterDataFilterFragment extends Fragment implements View.OnClickListener, CollectSelectDialog.CollectCallback, MeterSelectDialog.MeterCallback {

    //view
    private View mRootView;
    private UIRevisableView mStartTimeView;
    private UIRevisableView mEndTimeView;
    //    private UIRevisableView mSelectCollectView;
    private UIRevisableView mSelectMeterView;
    private UIRevisableView mDataTypeView;
    private UIRevisableView mDataContentView;

    //value
    private long startTime;
    private long endTime;
    private ArrayList<Collect> mCollects;
    private ArrayList<Meter> mMeters;
    private int dataType = 0;
    private int dataContent = 0;


    private int[] mDataContentEntries = {0, 1, 2, 3};
    private String[] mDataContentValues = {"全选", "正向有功", "正向无功", "一象限无功示值"};
    private int[] mDataTypeEntries = {0, 1, 2, 3};
    private String[] mDataTypeValues = {"全选", "日冻结", "曲线", "实时数据"};

    public MeterDataFilterFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            startTime = bundle.getLong(MeterUtilies.PARAM_START_TIME);
            endTime = bundle.getLong(MeterUtilies.PARAM_END_TIME);
            dataType = bundle.getInt(MeterUtilies.PARAM_DATA_TYPE);
            dataContent = bundle.getInt(MeterUtilies.PARAM_DATA_CONTENT);
        } else {
            startTime = 0;
            endTime = System.currentTimeMillis();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.meter_data_filter_layout, container, false);
        mStartTimeView = (UIRevisableView) mRootView.findViewById(R.id.start_time);
        mEndTimeView = (UIRevisableView) mRootView.findViewById(R.id.end_time);
//        mSelectCollectView = (UIRevisableView) mRootView.findViewById(R.id.select_collect);
        mSelectMeterView = (UIRevisableView) mRootView.findViewById(R.id.select_meter);
        mDataTypeView = (UIRevisableView) mRootView.findViewById(R.id.data_type);
        mDataContentView = (UIRevisableView) mRootView.findViewById(R.id.data_content);
        //初始化
        mStartTimeView.setContent(MeterUtilies.getSanShengDate(startTime));
        mEndTimeView.setContent(MeterUtilies.getSanShengDate(endTime));
//        mSelectCollectView.setContent("全选");
        mSelectMeterView.setContent("全选");
        if (dataType == 0) {
            mDataTypeView.setContent("全选");
        } else {
            mDataTypeView.setContent(mDataTypeValues[dataType]);
        }

        if (dataContent == 0) {
            mDataContentView.setContent("全选");
        } else {
            mDataContentView.setContent(mDataContentValues[dataContent]);
        }

        mStartTimeView.setOnClickListener(this);
        mEndTimeView.setOnClickListener(this);
//        mSelectCollectView.setOnClickListener(this);
        mSelectMeterView.setOnClickListener(this);
        mDataTypeView.setOnClickListener(this);
        mDataContentView.setOnClickListener(this);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.start_time:
                modifyStartTime();
                break;
            case R.id.end_time:
                modifyEndTime();
                break;
//            case R.id.select_collect:
//                selectCollect();
//                break;
            case R.id.select_meter:
                selectMeter();
                break;
            case R.id.data_type:
                changeDataType();
                break;
            case R.id.data_content:
                changeDataContent();
                break;
            default:
                break;
        }
    }


    private void modifyStartTime() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                startTime = calendar.getTimeInMillis();
                mStartTimeView.setContent(MeterUtilies.getSanShengDate(startTime));
            }
        };
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(startTime);
        final DatePickerDialog datePickDialog = new DatePickerDialog(getActivity(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickDialog.show();
    }

    private void modifyEndTime() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                endTime = calendar.getTimeInMillis();
                mEndTimeView.setContent(MeterUtilies.getSanShengDate(endTime));
            }
        };
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(endTime);
        final DatePickerDialog datePickDialog = new DatePickerDialog(getActivity(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickDialog.show();
    }

    private void selectCollect() {
        CollectSelectDialog collectDialog = new CollectSelectDialog(this);
        collectDialog.show(getFragmentManager(), "select_collects");
    }

    private void selectMeter() {
        MeterSelectDialog meterSelectDialog = new MeterSelectDialog(this);
        meterSelectDialog.show(getFragmentManager(), "select_meter");
    }

    private void changeDataType() {
        final SettingsRadioDialog dialog = new SettingsRadioDialog(getActivity(), R.style.CustomDialog);
        dialog.setTitleText("选择数据类型");
        dialog.setSingleChoiceItems(mDataTypeValues, dataType, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which != dataType) {
                    dataType = which;
                    mDataTypeView.setContent(mDataTypeValues[dataType]);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void changeDataContent() {
        final SettingsRadioDialog dialog = new SettingsRadioDialog(getActivity(), R.style.CustomDialog);
        dialog.setTitleText("选择数据类型");
        dialog.setSingleChoiceItems(mDataContentValues, dataContent, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which != dataContent) {
                    dataContent = which;
                    mDataContentView.setContent(mDataContentValues[dataContent]);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onCollectNegativeClick() {

    }

    @Override
    public void onCollectPositiveClick(HashMap<String, Collect> collects) {
        if (collects == null || collects.size() == 0) {
            return;
        }
        this.mCollects = new ArrayList<Collect>(collects.values());
        if (collects.size() == 1) {
//            mSelectCollectView.setContent(mCollects.get(0).mCollectName);
        } else {
//            mSelectCollectView.setContent(mCollects.get(0).mCollectName + "[" + mCollects.size() + "]");
        }
    }

    @Override
    public void onMeterPositiveClick(HashMap<String, Meter> meters) {
        if (meters == null || meters.size() == 0) {
            return;
        }
        this.mMeters = new ArrayList<Meter>(meters.values());
        if (meters.size() == 1) {
            mSelectMeterView.setContent(mMeters.get(0).mMeterName);
        } else {
            mSelectMeterView.setContent(mMeters.get(0).mMeterName + "[" + mMeters.size() + "]");
        }
    }

    public Bundle getFilter() {
        Bundle bundle = new Bundle();
        if (mMeters != null && mMeters.size() > 0) {
            StringBuilder ids = new StringBuilder();
            for (Meter meter : mMeters) {
                if (!TextUtils.isEmpty(ids)) {
                    ids.append(",");
                }
                ids.append(meter.mId);
            }
            bundle.putString(MeterUtilies.PARAM_METER_ID, ids.toString());
        }
        bundle.putLong(MeterUtilies.PARAM_START_TIME, startTime);
        bundle.putLong(MeterUtilies.PARAM_END_TIME, endTime);
        bundle.putInt(MeterUtilies.PARAM_DATA_TYPE, dataType);
        bundle.putInt(MeterUtilies.PARAM_DATA_CONTENT, dataContent);
        return bundle;
    }

//    private void modifyImportant() {
//        final SettingsRadioDialog dialog = new SettingsRadioDialog(getActivity(), R.style.CustomDialog);
//        dialog.setTitleText(R.string.meter_detail_important_title);
//
//        dialog.setSingleChoiceItems(mImportantEntries, mChangedMeter.mImportant, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                if (which != mChangedMeter.mImportant) {
//                    mChangedMeter.mImportant = which;
////                    refreshView(mChangedMeter);
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

//    private class DataBaseTask extends AsyncTask<Void, Void, Integer> {
//
//        @Override
//        protected Integer doInBackground(Void... params) {
//            mChangedMeter.update(getActivity());
//            ((MeterDataListActivity) getActivity()).restartLoader();
//            return null;
//        }
//    }
}
