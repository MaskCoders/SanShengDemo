package com.sansheng.testcenter.demo.view;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.CustomThreadPoolFactory;
import com.sansheng.testcenter.base.view.UIRevisableView;
import com.sansheng.testcenter.base.view.UIUnrevisableView;
import com.sansheng.testcenter.demo.util.MeterUtilies;
import com.sansheng.testcenter.module.Meter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by sunshaogang on 12/10/15.
 */
public class MeterFragment extends Fragment implements View.OnClickListener, BaseActivity.ActionBarCallback {
    private Meter mMeter;
    private Meter mChangedMeter;

    private View mRootView;
    private UIUnrevisableView mMeterIdView;
    private UIRevisableView mMeterNameView;
    private UIRevisableView mValueTimeView;
    private UIRevisableView mReadTimeView;
    private UIRevisableView mDateTypeView;
    private UIRevisableView mValzView;
    private UIRevisableView mImportantView;
    private String[] mImportantEntries = {"是", "否"};
    private String[] mDataEntries = {"日冻结", "实时数据"};

    private static final ThreadFactory sThreadFactory = new CustomThreadPoolFactory("MeterThread");
    private ExecutorService sThreadPool = Executors.newSingleThreadExecutor(sThreadFactory);

    public MeterFragment() {
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
        mRootView = inflater.inflate(R.layout.meter_detail_layout, container, false);
        mMeterIdView = (UIUnrevisableView) mRootView.findViewById(R.id.meter_id);
        mMeterNameView = (UIRevisableView) mRootView.findViewById(R.id.meter_name);
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


    private void refreshView(Meter meter) {
//        mMeterIdView.setContent(String.valueOf(meter.mMeterID));
//        mMeterNameView.setContent(String.valueOf(meter.mMeterName));
//        mValueTimeView.setContent(MeterUtilies.getSanShengDate(meter.mValueTime));
//        mReadTimeView.setContent(MeterUtilies.getSanShengDate(meter.mReadTime));
//        mDateTypeView.setContent(mDataEntries[meter.mDataType - 1]);
//        mValzView.setContent(String.valueOf(meter.mValz));
//        mImportantView.setContent(mImportantEntries[meter.isImportant]);
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

//    private void modifyValueTime() {
//        DateTimePickDialog.OnDateTimeSetListener listener = new DateTimePickDialog.OnDateTimeSetListener() {
//            @Override
//            public void onDateTimeSet(Calendar calendar) {
//                mChangedMeter.mValueTime = calendar.getTimeInMillis();
//                refreshView(mChangedMeter);
//            }
//        };
//        final DateTimePickDialog dateTimePickDialog = new DateTimePickDialog(getActivity(), listener, mChangedMeter.mValueTime);
//        dateTimePickDialog.show();
//    }

//    private void modifyReadTime() {
//        DateTimePickDialog.OnDateTimeSetListener listener = new DateTimePickDialog.OnDateTimeSetListener() {
//            @Override
//            public void onDateTimeSet(Calendar calendar) {
//                mChangedMeter.mReadTime = calendar.getTimeInMillis();
//                refreshView(mChangedMeter);
//            }
//        };
//        final DateTimePickDialog dateTimePickDialog = new DateTimePickDialog(getActivity(), listener, mChangedMeter.mReadTime);
//        dateTimePickDialog.show();
//    }

//    private void modifyMeterValue() {
//        final BaseDialog dialog = new BaseDialog(getActivity(), R.style.CustomDialog);
//        dialog.setTitleText(R.string.meter_detail_value_title);
//        dialog.setEditText(String.valueOf(mChangedMeter.mValz));
//        dialog.setOnPositiveBtnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (TextUtils.equals(mChangedMeter.mMeterName, dialog.getEditText().getText().toString())) {
//                    //未修改，无需更新
//                    dialog.dismiss();
//                } else {//修改
//                    try {
//                        mChangedMeter.mValz = Float.valueOf(dialog.getEditText().getText().toString());
//                    } catch (NumberFormatException e) {
//                        Utility.showToast(getActivity(), getActivity().getResources().getString(R.string.illegal_input));
//                    }
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

//    private void modifyDataType() {
//        final SettingsRadioDialog dialog = new SettingsRadioDialog(getActivity(), R.style.CustomDialog);
//        dialog.setTitleText(R.string.meter_detail_data_type_title);
//
//        dialog.setSingleChoiceItems(mDataEntries, mChangedMeter.mDataType - 1, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                if (which + 1 != mChangedMeter.mDataType) {
//                    mChangedMeter.mDataType = which + 1;
//                    refreshView(mChangedMeter);
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

//    private void modifyImportant() {
//        final SettingsRadioDialog dialog = new SettingsRadioDialog(getActivity(), R.style.CustomDialog);
//        dialog.setTitleText(R.string.meter_detail_important_title);
//
//        dialog.setSingleChoiceItems(mImportantEntries, mChangedMeter.isImportant, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int which) {
//                if (which != mChangedMeter.isImportant) {
//                    mChangedMeter.isImportant = which;
//                    refreshView(mChangedMeter);
//                }
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//    }

    private class DataBaseTask extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            mChangedMeter.update(getActivity());
            ((MeterDataListActivity)getActivity()).restartLoader();
            return null;
        }
    }
}
