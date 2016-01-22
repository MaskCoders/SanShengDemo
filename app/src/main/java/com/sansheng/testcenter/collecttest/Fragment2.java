package com.sansheng.testcenter.collecttest;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.sansheng.testcenter.R;

/**
 * Created by sunshaogang on 1/20/16.
 */
public class Fragment2 extends BaseTabFragment {
    public static final String TAG = "Fragment2";
    private View mRootView;
    private TextView mCollectName;
    private TextView mCollectAddress;
    private TextView mCollectChannel;
    private TextView mCollectIp;
    private TextView mCollectState;

    public Fragment2() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.collect_test_fragment2_layout, container, false);
        mCollectName = (TextView) mRootView.findViewById(R.id.collect_name);
        mCollectAddress = (TextView) mRootView.findViewById(R.id.collect_address);
        mCollectChannel = (TextView) mRootView.findViewById(R.id.collect_channel);
        mCollectIp = (TextView) mRootView.findViewById(R.id.collect_ip);
        mCollectState = (TextView) mRootView.findViewById(R.id.collect_state);
        mCollectName.setText(mCollect.mCollectName);
        mCollectAddress.setText(mCollect.mCommonAddress);
        mCollectChannel.setText(CollectTestUtils.channelItems(getActivity())[mCollect.mChannelType]);
        mCollectIp.setText(mCollect.mTerminalIp);
        mCollectState.setText("");
        return mRootView;
    }

    @Override
    public String getFragmentTag() {
        return TAG;
    }
}
