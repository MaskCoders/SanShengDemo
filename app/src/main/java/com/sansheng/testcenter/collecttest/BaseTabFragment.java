package com.sansheng.testcenter.collecttest;

import android.app.Fragment;
import android.os.Bundle;
import com.sansheng.testcenter.module.Collect;

/**
 * Created by sunshaogang on 1/21/16.
 */
abstract class BaseTabFragment extends Fragment{
    public abstract  String getFragmentTag();
    protected Collect mCollect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            mCollect = bundle.getParcelable(CollectTestUtils.PARAM_COLLECT);
//        }
        mCollect = ((CollectTestActivity)getActivity()).getCollect();
    }
}
