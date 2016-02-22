package com.sansheng.testcenter.center;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.module.CollectParam;
import com.sansheng.testcenter.utils.MeterUtilies;

import java.util.Calendar;

/**
 * Created by sunshaogang on 2/19/16.
 * 用于向用户提供 修改时标和曲线间隔的对话框
 */
public class DataReadDialog extends BaseDialog{

    private TextView mDateSpinner;
    private Spinner mTimeSpinner;

    public DataReadDialog(DialogCallback callback, CollectParam param) {
        super(callback, param);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        mDialog.setTitleText("设置时标和曲线间隔");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mRootView = inflater.inflate(R.layout.data_read_dialog_layout, null);
        mDateSpinner = (TextView) mRootView.findViewById(R.id.date_flag);
        mTimeSpinner = (Spinner) mRootView.findViewById(R.id.time_range);
        mDateSpinner.setText(MeterUtilies.getSanShengDate(System.currentTimeMillis()));
        mDateSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyDate();
            }
        });
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.center_time_range, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSpinner.setAdapter(adapter);
        mTimeSpinner.setSelection(2, true);
        mDialog.setCustomView(mRootView);
        return mDialog;
    }

    @Override
    protected void resetParam() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    private void modifyDate() {
        DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                mDateSpinner.setText(MeterUtilies.getSanShengDate(calendar.getTimeInMillis()));
            }
        };
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        final DatePickerDialog datePickDialog = new DatePickerDialog(getActivity(), listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickDialog.show();
    }
}
