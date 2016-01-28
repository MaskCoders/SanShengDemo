package com.sansheng.testcenter.datamanager;

import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.CustomThreadPoolFactory;
import com.sansheng.testcenter.base.view.*;
import com.sansheng.testcenter.utils.MeterUtilies;
import com.sansheng.testcenter.module.MeterData;
import com.sansheng.testcenter.utils.Utility;

import java.util.Calendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by sunshaogang on 12/10/15.
 */
public class MeterDataFragment extends Fragment implements View.OnClickListener, BaseActivity.ActionBarCallback {
    private MeterData mMeter;
    private MeterData mChangedMeter;

    private View mRootView;
    private UIUnrevisableView mMeterIdView;
    private UIUnrevisableView mMeterNameView;
    private UIRevisableView mValueTimeView;
    private UIRevisableView mReadTimeView;
    private UIRevisableView mDateTypeView;
    private UIRevisableView mValzView;
    private UIRevisableView mImportantView;
    private String[] mImportantEntries = {"是", "否"};
    private String[] mDataEntries = {"日冻结", "实时数据"};

    private static final ThreadFactory sThreadFactory = new CustomThreadPoolFactory("MeterThread");
    private ExecutorService sThreadPool = Executors.newSingleThreadExecutor(sThreadFactory);

    public MeterDataFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImportantEntries = new String[]{getActivity().getResources().getString(R.string.yes),
                getActivity().getResources().getString(R.string.no)};
        mDataEntries = new String[]{getActivity().getResources().getString(R.string.db_rdj),
                getActivity().getResources().getString(R.string.db_realdata)};
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMeter = bundle.getParcelable(MeterUtilies.PARAM_METER);
        }
        mChangedMeter = mMeter.copy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ((BaseActivity)getActivity()).setActionBar(BaseActivity.MODIFY_DETAIL_VIEW, this);
        mRootView = inflater.inflate(R.layout.meter_data_detail_layout, container, false);
        mMeterIdView = (UIUnrevisableView) mRootView.findViewById(R.id.meter_id);
        mMeterNameView = (UIUnrevisableView) mRootView.findViewById(R.id.meter_name);
        mValueTimeView = (UIRevisableView) mRootView.findViewById(R.id.meter_value_time);
        mReadTimeView = (UIRevisableView) mRootView.findViewById(R.id.meter_read_time);
        mDateTypeView = (UIRevisableView) mRootView.findViewById(R.id.meter_data_type);
        mValzView = (UIRevisableView) mRootView.findViewById(R.id.meter_value);
        mImportantView = (UIRevisableView) mRootView.findViewById(R.id.meter_important);

        mMeterNameView.setOnClickListener(this);
        mValueTimeView.setOnClickListener(this);
        mReadTimeView.setOnClickListener(this);
        mDateTypeView.setOnClickListener(this);
        mValzView.setOnClickListener(this);
        mImportantView.setOnClickListener(this);
        refreshView(mMeter);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        ((BaseActivity)getActivity()).setActionBar(BaseActivity.METER_LIST_VIEW);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.meter_name:
//                modifyMeterNamen();
                break;
            case R.id.meter_value_time:
//                modifyValueTime();
                break;
            case R.id.meter_read_time:
//                modifyReadTime();
                break;
            case R.id.meter_data_type:
//                modifyDataType();
                break;
            case R.id.meter_value:
//                modifyMeterValue();
                break;
            case R.id.meter_important:
//                modifyImportant();
                break;
            default:
                break;
        }
    }

    @Override
    public void onSaveClick() {
        DataBaseTask task = new DataBaseTask();
        task.executeOnExecutor(sThreadPool);
        getActivity().onBackPressed();

    }

    @Override
    public void onCancleClick() {
        getActivity().onBackPressed();
    }


    private void refreshView(MeterData meterData) {
        mMeterIdView.setContent(String.valueOf(meterData.mMeterID));
        mMeterNameView.setContent(String.valueOf(meterData.mMeter.mMeterName));
        mValueTimeView.setContent(MeterUtilies.getSanShengDate(meterData.mValueTime));
        mReadTimeView.setContent(MeterUtilies.getSanShengDate(meterData.mReadTime));
        mDateTypeView.setContent(mDataEntries[meterData.mDataType - 1]);
        mValzView.setContent(String.valueOf(meterData.mValz));
        mImportantView.setContent(mImportantEntries[meterData.mImportant]);
    }

//    private void modifyMeterNamen() {
//        final BaseDialog dialog = new BaseDialog(getActivity(), R.style.CustomDialog);
//        dialog.setTitleText(R.string.meter_detail_name_title);
//        dialog.setEditText(mChangedMeter.mMeterName);
//        dialog.setOnPositiveBtnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (dialog.getEditText().getText().toString().getBytes().length > MeterUtilies.NAME_MAX_BYTE_LENGTH) {
//                    Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.too_long));
//                } else if (TextUtils.equals(mChangedMeter.mMeterName, dialog.getEditText().getText().toString())) {
//                    //未修改，无需更新
//                    dialog.dismiss();
//                } else {//修改
//                    mChangedMeter.mMeterName = dialog.getEditText().getText().toString();
//                    refreshView(mChangedMeter);
//                    dialog.dismiss();
//                }
//            }
//        });
//        dialog.setOnNegativeBtnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

    private void modifyValueTime() {
        DateTimePickDialog.OnDateTimeSetListener listener = new DateTimePickDialog.OnDateTimeSetListener() {
            @Override
            public void onDateTimeSet(Calendar calendar) {
                mChangedMeter.mValueTime = calendar.getTimeInMillis();
                refreshView(mChangedMeter);
            }
        };
        final DateTimePickDialog dateTimePickDialog = new DateTimePickDialog(getActivity(), listener, mChangedMeter.mValueTime);
        dateTimePickDialog.show();
    }

    private void modifyReadTime() {
        DateTimePickDialog.OnDateTimeSetListener listener = new DateTimePickDialog.OnDateTimeSetListener() {
            @Override
            public void onDateTimeSet(Calendar calendar) {
                mChangedMeter.mReadTime = calendar.getTimeInMillis();
                refreshView(mChangedMeter);
            }
        };
        final DateTimePickDialog dateTimePickDialog = new DateTimePickDialog(getActivity(), listener, mChangedMeter.mReadTime);
        dateTimePickDialog.show();
    }

    private void modifyMeterValue() {
        final BaseDialog dialog = new BaseDialog(getActivity(), R.style.CustomDialog);
        dialog.setTitleText(R.string.meter_detail_value_title);
        dialog.setEditText(String.valueOf(mChangedMeter.mValz));
        dialog.setOnPositiveBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.equals(mChangedMeter.mMeter.mMeterName, dialog.getEditText().getText().toString())) {
                    //未修改，无需更新
                    dialog.dismiss();
                } else {//修改
                    try {
                        mChangedMeter.mValz = Float.valueOf(dialog.getEditText().getText().toString());
                    } catch (NumberFormatException e) {
                        Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.illegal_input));
                    }
                    refreshView(mChangedMeter);
                    dialog.dismiss();
                }
            }
        });
        dialog.setOnNegativeBtnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void modifyDataType() {
        final SettingsRadioDialog dialog = new SettingsRadioDialog(getActivity(), R.style.CustomDialog);
        dialog.setTitleText(R.string.meter_detail_data_type_title);

        dialog.setSingleChoiceItems(mDataEntries, mChangedMeter.mDataType - 1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which + 1 != mChangedMeter.mDataType) {
                    mChangedMeter.mDataType = which + 1;
                    refreshView(mChangedMeter);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void modifyImportant() {
        final SettingsRadioDialog dialog = new SettingsRadioDialog(getActivity(), R.style.CustomDialog);
        dialog.setTitleText(R.string.meter_detail_important_title);

        dialog.setSingleChoiceItems(mImportantEntries, mChangedMeter.mImportant, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which != mChangedMeter.mImportant) {
                    mChangedMeter.mImportant = which;
                    refreshView(mChangedMeter);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private class DataBaseTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            mChangedMeter.update(getActivity());
            ((MeterDataListActivity)getActivity()).restartLoader(MeterDataListActivity.LOADER_ID_FILTER);
            return null;
        }
    }
}
