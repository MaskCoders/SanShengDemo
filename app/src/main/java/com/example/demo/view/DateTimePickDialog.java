package com.example.demo.view;

/**
 * Created by sunshaogang on 12/11/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;
import com.example.demo.R;

import java.util.Calendar;


public class DateTimePickDialog extends AlertDialog implements DialogInterface.OnClickListener, OnDateChangedListener, OnTimeChangedListener {
    private DatePicker datePicker;
    private TimePicker timePicker;
    private long time;
    private Activity mActivity;
    private final OnDateTimeSetListener mCallBack;
    private final Calendar mCalendar;

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mCallBack != null) {
            mCallBack.onDateTimeSet(mCalendar);
        }
    }

    public interface OnDateTimeSetListener {
        void onDateTimeSet(Calendar calendar);
    }

    public DateTimePickDialog(Activity context, OnDateTimeSetListener callBack, long time) {
        this(context, 0, callBack, time);
    }

    public DateTimePickDialog(Activity context, int theme, OnDateTimeSetListener callBack, long time) {
        super(context, theme);
        this.mActivity = context;
        this.mCallBack = callBack;
        this.time = time;
        mCalendar = Calendar.getInstance();
        Context themeContext = getContext();
        setButton(BUTTON_POSITIVE, themeContext.getText(R.string.date_time_done), this);
        setIcon(0);
        LayoutInflater inflater =
                (LayoutInflater) themeContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.date_time_dialog, null);
        datePicker = (DatePicker) view.findViewById(R.id.date_picker);
        timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        init(datePicker, timePicker);
        setView(view);
    }

    public void init(DatePicker datePicker, TimePicker timePicker) {
        mCalendar.setTimeInMillis(time);
        datePicker.init(mCalendar.get(Calendar.YEAR),
                mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH), this);
        timePicker.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(mCalendar.get(Calendar.MINUTE));
        timePicker.setIs24HourView(true);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        mCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mCalendar.set(Calendar.MINUTE, minute);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, monthOfYear);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }
}
