package com.sansheng.testcenter.equipmentmanager;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.collecttest.CollectTestUtils;
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
    private EditText mEditIp;

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
        mRootView = inflater.inflate(R.layout.collect_detail_layout, container, false);
        mEditName = (EditText) mRootView.findViewById(R.id.collect_name);
        mEditAddress = (EditText) mRootView.findViewById(R.id.collect_address);
        mEditPassword = (EditText) mRootView.findViewById(R.id.collect_password);
        mChannel = (Spinner) mRootView.findViewById(R.id.collect_channel);
        mEditPort = (EditText) mRootView.findViewById(R.id.collect_port);
        mEditIp = (EditText) mRootView.findViewById(R.id.collect_ip);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.collect_channel_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mChannel.setAdapter(adapter);
        mChannel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {//客户端
                    mRootView.findViewById(R.id.collect_port_layout).setVisibility(View.GONE);
                    mRootView.findViewById(R.id.collect_ip_layout).setVisibility(View.VISIBLE);
                    mEditIp.setText("192.168.0.1");
                } else if (position == 1) {//服务端
                    mRootView.findViewById(R.id.collect_port_layout).setVisibility(View.VISIBLE);
                    mRootView.findViewById(R.id.collect_ip_layout).setVisibility(View.GONE);
                    mEditPort.setText(mCollect.mTerminalPort);
                } else {
                    mRootView.findViewById(R.id.collect_port_layout).setVisibility(View.GONE);
                    mRootView.findViewById(R.id.collect_ip_layout).setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mRootView.findViewById(R.id.collect_port_layout).setVisibility(View.GONE);
                mRootView.findViewById(R.id.collect_ip_layout).setVisibility(View.GONE);
            }

        });
        if (mCollect != null) {
            mEditName.setText(mCollect.mCollectName);
            mEditAddress.setText(mCollect.mTerminalIp);
            mEditPassword.setText(mCollect.mPassword);
            mChannel.setSelection(mCollect.mChannelType, true);
            if (mCollect.mChannelType == 1) {
                mRootView.findViewById(R.id.collect_port_layout).setVisibility(View.VISIBLE);
                mEditPort.setText(mCollect.mTerminalPort);
            } else if (mCollect.mChannelType == 0) {
                mRootView.findViewById(R.id.collect_ip_layout).setVisibility(View.VISIBLE);
                mEditIp.setText("192.168.0.1");
            }
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
