package com.sansheng.testcenter.center;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.view.AnswerDialog;
import com.sansheng.testcenter.datamanager.MeterDataFragment;
import com.sansheng.testcenter.module.Collect;
import com.sansheng.testcenter.utils.MeterUtilies;

/**
 * Created by sunshaogang on 2/16/16.
 */
public class Dialog14to3 extends DialogFragment {
    private View mRootView;
    //    private View mEmptyView;
    private EditText startContant;
    private EditText endContant;
    private AnswerDialog mDialog;
    private Dialog14to3Callback callback;
    private String collectIds;

    public Dialog14to3(Dialog14to3Callback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new AnswerDialog(getActivity());
        mDialog.show();
        mDialog.setTitleText("14-3");
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mRootView = inflater.inflate(R.layout.fourteen_to_three, null);
        startContant = (EditText) mRootView.findViewById(R.id.start_point_contant);
        endContant = (EditText) mRootView.findViewById(R.id.end_point_contant);

        mDialog.setCustomView(mRootView);
        mDialog.setNegativeButton(R.string.cancel, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.setPositiveButton(R.string.confirm, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putInt(CenterActivity.PARAM_START_POINT, Integer.valueOf(startContant.getText().toString()));
                bundle.putInt(CenterActivity.PARAM_END_POINT, Integer.valueOf(endContant.getText().toString()));
                callback.on14to3PositiveClick(bundle);
            }
        });
        return mDialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public void showDetailFragment(Collect collect) {
        MeterDataFragment fragment = new MeterDataFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MeterUtilies.PARAM_METER, collect);
        fragment.setArguments(bundle);
        MeterUtilies.showFragment(getFragmentManager(), null, fragment, R.id.meter_content, FragmentTransaction.TRANSIT_FRAGMENT_OPEN, String.valueOf(collect.mId));
    }

    public interface Dialog14to3Callback {
        void on14to3PositiveClick(Bundle bundle);
    }
}

