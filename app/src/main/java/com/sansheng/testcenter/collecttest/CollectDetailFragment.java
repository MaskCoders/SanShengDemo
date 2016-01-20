package com.sansheng.testcenter.collecttest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.module.Collect;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class CollectDetailFragment extends Fragment {

    private Collect mCollect;

    private View mRootView;
    private EditText mEditName;
    private EditText mEditAddress;
    private EditText mEditPassword;
    private TextView mEditChannel;
    private EditText mEditPort;

    public CollectDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mCollect = bundle.getParcelable(CollectTestUtils.PARAM_COLLECT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.collect_test_detail_layout, container, false);
        mEditName = (EditText) mRootView.findViewById(R.id.collect_name);
        mEditAddress = (EditText) mRootView.findViewById(R.id.collect_name);
        mEditPassword = (EditText) mRootView.findViewById(R.id.collect_name);
        mEditChannel = (TextView) mRootView.findViewById(R.id.collect_name);
        mEditPort = (EditText) mRootView.findViewById(R.id.collect_name);
        if (mCollect != null) {
            mEditName.setText(mCollect.mCollectName);
            mEditAddress.setText(mCollect.mCommonAddress);
            mEditPassword.setText(mCollect.mPassword);
            mEditChannel.setText(String.valueOf(mCollect.mChannelType));
            mEditPort.setText(mCollect.mTerminalPort);
        } else {
            mCollect = new Collect();
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
    }

    private void save(){

    }
}
