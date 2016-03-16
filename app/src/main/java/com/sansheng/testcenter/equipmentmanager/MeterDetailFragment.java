package com.sansheng.testcenter.equipmentmanager;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.CustomThreadPoolFactory;
import com.sansheng.testcenter.datamanager.MeterDataListActivity;
import com.sansheng.testcenter.module.Meter;
import com.sansheng.testcenter.utils.MeterUtilies;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by sunshaogang on 12/10/15.
 */
public class MeterDetailFragment extends Fragment implements
        View.OnTouchListener, View.OnClickListener, BaseActivity.ActionBarCallback {
    private Meter mMeter;

    private View mRootView;
    private TextView mMeterType;
    private TextView mCollectId;
    private EditText mEditName;
    private EditText mEditAddress;
    private EditText mEditPassword;
    private TextView mMeterDa;
    private EditText mEditPort;
    private CheckBox mImportant;
    private Spinner mSpinner;
    private ArrayAdapter mChannelAdapter;

    private static final ThreadFactory sThreadFactory = new CustomThreadPoolFactory("MeterThread");
    private ExecutorService sThreadPool = Executors.newSingleThreadExecutor(sThreadFactory);

    public MeterDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mMeter = bundle.getParcelable(MeterUtilies.PARAM_METER);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        ((BaseActivity)getActivity()).setActionBar(BaseActivity.MODIFY_DETAIL_VIEW, this);
        mRootView = inflater.inflate(R.layout.meter_detail_layout, container, false);
        mRootView.setOnTouchListener(this);
        mMeterType = (TextView) mRootView.findViewById(R.id.meter_type);
        mCollectId = (TextView) mRootView.findViewById(R.id.collect_name);
        mMeterDa = (TextView) mRootView.findViewById(R.id.meter_num);
        mEditName = (EditText) mRootView.findViewById(R.id.meter_name);
        mEditAddress = (EditText) mRootView.findViewById(R.id.meter_address);
        mEditPassword = (EditText) mRootView.findViewById(R.id.meter_password);
//        mChannel = (Spinner) mRootView.findViewById(R.id.meter_channel);
        mEditPort = (EditText) mRootView.findViewById(R.id.meter_port);
        mImportant = (CheckBox) mRootView.findViewById(R.id.meter_important);
        mSpinner = (Spinner) mRootView.findViewById(R.id.meter_example);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.collect_channel_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(adapter);
        mMeterType.setOnClickListener(this);
        mEditName.setOnClickListener(this);
        mMeterDa.setOnClickListener(this);
        mEditAddress.setOnClickListener(this);
        mEditPassword.setOnClickListener(this);
        mCollectId.setOnClickListener(this);
        mEditPort.setOnClickListener(this);
        mImportant.setOnClickListener(this);
        if (mMeter != null) {
            refreshView(mMeter);
        }
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
    public boolean onTouch(View v, MotionEvent event) {
        return true;
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
//        DataBaseTask task = new DataBaseTask();
//        task.executeOnExecutor(sThreadPool);
        getActivity().onBackPressed();

    }

    @Override
    public void onCancleClick() {
        getActivity().onBackPressed();
    }


    private void refreshView(Meter meter) {
        mMeterType.setText(String.valueOf(meter.mType));
        mCollectId.setText(String.valueOf(meter.mCollectId));
        mEditName.setText(meter.mMeterName);
        mEditAddress.setText(meter.mMeterAddress);
        mMeterDa.setText(String.valueOf(meter.mDa));
        mEditPassword.setText(meter.mCommPwd);
        mEditPort.setText(meter.mCommPortId);
        mImportant.setChecked(meter.mImportant == 1);
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
            ((MeterDataListActivity)getActivity()).restartLoader(MeterDataListActivity.LOADER_ID_FILTER);
            return null;
        }
    }
}
