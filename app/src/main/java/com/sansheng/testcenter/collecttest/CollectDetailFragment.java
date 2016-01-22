package com.sansheng.testcenter.collecttest;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
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
    private Spinner mChannel;
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
        mRootView = inflater.inflate(R.layout.collect_select_detail_layout, container, false);
        mEditName = (EditText) mRootView.findViewById(R.id.collect_name);
        mEditAddress = (EditText) mRootView.findViewById(R.id.collect_address);
        mEditPassword = (EditText) mRootView.findViewById(R.id.collect_password);
        mChannel = (Spinner) mRootView.findViewById(R.id.collect_channel);
        mEditPort = (EditText) mRootView.findViewById(R.id.collect_port);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.collect_channel_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChannel.setAdapter(adapter);
        if (mCollect != null) {
            mEditName.setText(mCollect.mCollectName);
            mEditAddress.setText(mCollect.mTerminalIp);
            mEditPassword.setText(mCollect.mPassword);
            mChannel.setSelection(mCollect.mChannelType, true);
            mEditPort.setText(mCollect.mTerminalPort);
        } else {
            mCollect = new Collect();
        }
        mRootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                return;
            }
        });
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

    //保存当前的终端
    public Collect getCollect(){
        if (mCollect == null) {
            mCollect = new Collect();
        }
        mCollect.mCollectName = mEditName.getText().toString();
        mCollect.mTerminalIp = mEditAddress.getText().toString();
        mCollect.mPassword = mEditPassword.getText().toString();
        mCollect.mChannelType = mChannel.getSelectedItemPosition();
        mCollect.mTerminalPort = mEditPort.getText().toString();
        return mCollect;
    }
}
