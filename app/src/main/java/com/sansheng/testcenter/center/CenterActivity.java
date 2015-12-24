package com.sansheng.testcenter.center;

import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import com.sansheng.testcenter.R;
import com.sansheng.testcenter.base.BaseActivity;
import com.sansheng.testcenter.base.CustomThreadPoolFactory;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by sunshaogang on 12/24/15.
 */
public class CenterActivity extends BaseActivity {

    private static final ThreadFactory sThreadFactory = new CustomThreadPoolFactory("CenterActivity");
    private ExecutorService sThreadPool = Executors.newSingleThreadExecutor(sThreadFactory);

    private ExpandableListView mListView;
    private CenterAdapter mAdapter;
    private List<RootProt> mGroupArray = new ArrayList<RootProt>();//组列表

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center_layout);
        mListView = (ExpandableListView) findViewById(R.id.center_list);
        ParseTask task = new ParseTask();
        task.executeOnExecutor(sThreadPool);
//        mListView.expandGroup( int groupPos):在分组列表视图中 展开一组，
//        mListView.setSelectedGroup( int groupPosition)：设置选择指定的组。
//        mListView.setSelectedChild( int groupPosition, int childPosition, boolean shouldExpandGroup)：设置选择指定的子项。
//        mListView.getPackedPositionGroup( long packedPosition)：返回所选择的组
//        mListView.getPackedPositionForChild( int groupPosition, int childPosition)：返回所选择的子项
//        mListView.getPackedPositionType( long packedPosition)：返回所选择项的类型（Child, Group）
//        mListView.isGroupExpanded( int groupPosition):判断此组是否展开
    }

    private void initView(){
        mAdapter = new CenterAdapter(this, mGroupArray);
        mListView.setAdapter(mAdapter);
        mListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                //TODO:启动检测接口
                return false;
            }
        });
    }

    @Override
    protected void showShortLog(boolean flag) {
        super.showShortLog(flag);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void initButtonList() {

    }

    @Override
    protected void initConnList() {

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    private class ParseTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                return parseXml();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean param) {
            initView();
        }
    }

    private boolean parseXml() throws XmlPullParserException, IOException {
        XmlResourceParser parser = getResources().getXml(R.xml.afn);
        List<Protocol> childArray = new ArrayList<Protocol>();
        RootProt rootProt = null;
        Protocol protocol = null;
        String afn = null;
        String pn = null;
        int pt = -1, po = -1;
        int fn = -1, t = -1, o = -1;
        String n = null;
        int event = parser.getEventType();
        while (event != XmlResourceParser.END_DOCUMENT) {
            switch (event) {
                case XmlResourceParser.START_DOCUMENT://判断当前事件是否是文档开始事件
                    Log.e("ssg", "start parse");
                    break;
                case XmlResourceParser.START_TAG://判断当前事件是否是标签元素开始事件
                    if (TextUtils.equals("afn", parser.getName())) {//判断开始标签元素是否是
                        afn = parser.getAttributeValue(0);
                        int count = parser.getAttributeCount();
                        if (count == 2) {
                            pn = parser.getAttributeValue(1);
                        } else if (count == 4) {
                            pt = Integer.parseInt(parser.getAttributeValue(1));
                            po = Integer.parseInt(parser.getAttributeValue(2));
                            pn = parser.getAttributeValue(3);
                        }
                        rootProt = new RootProt(afn, pn, pt, po);
                        Log.e("ssg", "start afn = " + afn);
                    } else if (TextUtils.equals("fn", parser.getName())) {
                        fn = Integer.parseInt(parser.getAttributeValue(0));
                        t = Integer.parseInt(parser.getAttributeValue(1));
                        o = Integer.parseInt(parser.getAttributeValue(2));
                        n = parser.getAttributeValue(3);
                        Log.e("ssg", "start fn = " + fn);
                    }
                    protocol = new Protocol(afn, pn, fn, t, o, n);
                    break;
                case XmlResourceParser.END_TAG://判断当前事件是否是标签元素结束事件
                    if (TextUtils.equals("afn", parser.getName())) {//判断开始标签元素是否是
                        Log.e("ssg", "end afn");
                        rootProt.setChildArray(childArray);
                        childArray = new ArrayList<Protocol>();
                        mGroupArray.add(rootProt);
                    } else if (TextUtils.equals("fn", parser.getName())) {
                        Log.e("ssg", "end fn");
                        childArray.add(protocol);
                    }
                    break;
                case XmlResourceParser.END_DOCUMENT:
                    Log.e("ssg", "end parse");
                    break;
            }
            event = parser.next();
        }
        if (mGroupArray.size() > 0) {
            return true;
        }
        return false;
    }
}
