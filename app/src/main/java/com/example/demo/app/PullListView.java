package com.example.demo.app;

import android.content.Context;
import android.os.SystemClock;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;

public class PullListView extends ListView implements OnScrollListener {
    private float mInitialMotionY, mLastMotionY, moveY_1, moveY_2;
    private boolean mIsBeingDragged = false;
    private int mTop = 0;
    private Interpolator mScrollAnimationInterpolator;
    private SmoothScrollRunnable mCurrentSmoothScrollRunnable;
    private OnRefreshListener mOnRefreshListener;
    private OnLoadMoreListener mOnLoadMoreListener;
    private boolean isRefreshing = false, isLoadMore = false;
    //lidtView底部加载更多的布局
    private LinearLayout footerView;
    private ProgressBar mLoadMoreProgressBar;
    private TextView mLoadMoreInfoTxt;
    private int footerContentHeight;
    private final static int DONE = 1;
    private final static int LOADING = 2;
    private final static int RELEASE_TO_LOAD_MORE = 3;
    private final static int PULL_TO_LOAD_MORE = 4;
    public final static int LOAD_MORE_STATUE_NO_MORE = 5;
    public final static int LOAD_MORE_STATUE_SYNC_ERROR = 6;
    public final static int LOAD_MORE_STATUE_SUCCESS = 7;
    public final static int LOAD_MORE_STATUS_USELESS = 8;
    public final static int LOAD_MORE_STATUE_HOME_PAGE = 9;
    private int state = DONE;
    private boolean isRecord = false;
    private int startY = 0;
    private int RATIO = 3;
    private boolean isAbleToLoadMore = false;

    private View headerView;
    private int mMaxTop;

    private boolean ROOLBACKHEIGHT_SWITCHER = true;

    public PullListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public PullListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init(context);
    }

    public PullListView getInstance() {
        return this;
    }

    void init(Context context) {
        getInstance().setOnScrollListener(this);
        LayoutInflater inflater = LayoutInflater.from(context);
        footerView = (LinearLayout) inflater.inflate(R.layout.circle_refresh_list_footer, null);
        mLoadMoreProgressBar = (ProgressBar) footerView.findViewById(R.id.lvFooterProgressBar);
        mLoadMoreInfoTxt = (TextView)footerView.findViewById(R.id.lvFooterInfoTxt);
        measureView(footerView);
        footerContentHeight = footerView.getMeasuredHeight();
        footerView.setPadding(0, 0, 0, -1 * footerContentHeight);
        footerView.invalidate();
        addFooterView(footerView);

        if (null == mScrollAnimationInterpolator) {
            mScrollAnimationInterpolator = new DecelerateInterpolator();
        }

        mMaxTop = Utility.dip2Px(context,100);
    }

    public void setPullHeaderView(View v) {
        headerView = v;
        addHeaderView(v);
        if (mTop == 0 && !ROOLBACKHEIGHT_SWITCHER) {
            mTop = 1080 / 5;
        }
        pullEvent(0);
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams params = child.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.FILL_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0,
                params.width);
        int lpHeight = params.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
                    MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0,
                    MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    public void setFooterViewStatic() {
        state = LOADING;
        changeFooterViewByState();
    }
    private void changeFooterViewByState() {
        switch (state) {
            case DONE:
                mLoadMoreInfoTxt.setText(R.string.load_more);
                mLoadMoreProgressBar.setVisibility(VISIBLE);
                footerView.setPadding(0, 0, 0, -1 * footerContentHeight);
                break;
            case PULL_TO_LOAD_MORE:
                //footerView.setPadding(0, 0, 0, -1 * footerContentHeight);
                break;
            case RELEASE_TO_LOAD_MORE:
                //footerView.setPadding(0, 0, 0, 0);
                break;
            case LOADING:
                mLoadMoreInfoTxt.setText(R.string.load_more);
                mLoadMoreProgressBar.setVisibility(VISIBLE);
                footerView.setPadding(0, 0, 0, 0);
                break;
            case LOAD_MORE_STATUE_NO_MORE:
                mLoadMoreInfoTxt.setText(R.string.circle_no_more_message);
                mLoadMoreProgressBar.setVisibility(GONE);
                footerView.setPadding(0, 0, 0, 0);
                break;
            case LOAD_MORE_STATUE_HOME_PAGE:
                mLoadMoreInfoTxt.setText(R.string.circle_click_head_more_content);
                mLoadMoreProgressBar.setVisibility(GONE);
                footerView.setPadding(0, 0, 0, 0);
                break;
            case LOAD_MORE_STATUE_SYNC_ERROR:
                mLoadMoreInfoTxt.setText(R.string.circle_sync_error);
                mLoadMoreProgressBar.setVisibility(GONE);
                footerView.setPadding(0, 0, 0, 0);
                break;
            case LOAD_MORE_STATUS_USELESS:
                footerView.setPadding(0, 0, 0, -1 * footerContentHeight);
                break;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN && state != LOAD_MORE_STATUE_NO_MORE && state != LOAD_MORE_STATUS_USELESS) {
            int childCount = getChildCount();
            int lastBottom = getChildAt(childCount - 1).getBottom();
            int lastVisiblePosition = getLastVisiblePosition();
            int itemCount = getAdapter().getCount() - 1;
            int end = getHeight() - getPaddingBottom();
            if(lastVisiblePosition >= itemCount && lastBottom <= end && state != LOADING) {//getChildAt(1).getBottom() + 1 >= this.getBottom() && state != LOADING
                isAbleToLoadMore = true;
                state = PULL_TO_LOAD_MORE;
                if (!isRecord) {
                    isRecord = true;
                    startY = (int) ev.getY();// 手指按下时记录当前位置
                }
            } else {
                isAbleToLoadMore = false;
                state = DONE;
            }
            if (isFirstItemVisible()) {
                mLastMotionY = mInitialMotionY = ev.getY();
            }

        }
        return super.onInterceptTouchEvent(ev);
    }

    private void onLoadMore() {
        if (mOnLoadMoreListener != null) {
            isLoadMore = true;
            mOnLoadMoreListener.onLoadMore(getInstance());
        }
    }

    public View getPullHeaderView() {
        return headerView;
    }

    public void setPullHeaderViewHeight(int height) {
        if(!ROOLBACKHEIGHT_SWITCHER)
            mTop = height;
        if (headerView != null) {
            pullEvent(0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub

        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE: {
                if (isRefreshing || isLoadMore) {
                    return super.onTouchEvent(event);
                }

//                if (!isAbleToLoadMore) {
//                    break;
//                }
                int tempY = (int) event.getY();
                if (!isRecord && isAbleToLoadMore) {
                    isRecord = true;
                    startY = tempY;
                }
                int distance = startY - tempY;
                if (state != LOADING && isAbleToLoadMore && distance > 0) {
                    int paddingBottom = distance - footerContentHeight;
                    if (distance >= footerContentHeight) {
                        footerView.setPadding(0, 0, 0,  paddingBottom / RATIO);
                        state = RELEASE_TO_LOAD_MORE;
                    } else if (distance > 0 && distance < footerContentHeight) {
                        footerView.setPadding(0, 0, 0, paddingBottom);
                        state = PULL_TO_LOAD_MORE;
                    }
                    break;
                }

                if (isFirstItemVisible()) {
                    mLastMotionY = event.getY();
                    moveY_1 = event.getY();
                    if (moveY_1 != moveY_2) {
                        float rotate = moveY_2 - moveY_1;
                        moveY_2 = moveY_1;
                        if (mLastMotionY - mInitialMotionY > 0) {
                            mIsBeingDragged = true;
                            pullEvent(mLastMotionY - mInitialMotionY);
                            return super.onTouchEvent(newMotionEvent(event));
                        }
                    }
                    System.out.println("mLastMotionY:" + mLastMotionY);
                }
                break;
            }
            case MotionEvent.ACTION_DOWN: {
                if (isRefreshing || isLoadMore) {
                    return super.onTouchEvent(event);
                }
                if(state != LOAD_MORE_STATUE_NO_MORE && state != LOAD_MORE_STATUS_USELESS && state != LOAD_MORE_STATUE_HOME_PAGE) {
                    int childCount = getChildCount();
                    int lastBottom = getChildAt(childCount - 1).getBottom();
                    int lastVisiblePosition = getLastVisiblePosition();
                    int itemCount = getAdapter().getCount() - 1;
                    int end = getHeight() - getPaddingBottom();
                    if(lastVisiblePosition >= itemCount && lastBottom <= end && state != LOADING/*getChildAt(1).getBottom() <= this.getBottom() && state != LOADING*/) {
                        isAbleToLoadMore = true;
                        state = PULL_TO_LOAD_MORE;
                        if (!isRecord && isAbleToLoadMore) {
                            isRecord = true;
                            startY = (int) event.getY();// 手指按下时记录当前位置
                        }
                        break;
                    } else {
                        isAbleToLoadMore = false;
                        state = DONE;
                    }
                }

                if (isFirstItemVisible()) {
                    mLastMotionY = mInitialMotionY = event.getY();
                }
                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
//                if (!isAbleToLoadMore) {
//                    break;
//                }
                if (state != LOADING && isAbleToLoadMore&&!mIsBeingDragged) {
                    if (state == PULL_TO_LOAD_MORE) {
                        state = DONE;
                        changeFooterViewByState();
                    } else if(state == RELEASE_TO_LOAD_MORE) {
                        state = LOADING;
                        changeFooterViewByState();
                        onLoadMore();
                    }
                    isRecord = false;
                    isAbleToLoadMore = false;
                    break;
                }

                if (mIsBeingDragged) {
                    mIsBeingDragged = false;
                    mLastMotionY = event.getY();
                    if (mLastMotionY - mInitialMotionY > mTop) {
                        if (null != mOnRefreshListener) {
                            mOnRefreshListener.onRefresh(PullListView.this);
                        }
                    } else {
                        isRefreshing = true;
                    }

                }
                // rotateLayout.toup();
                break;
            }
        }
        return super.onTouchEvent(event);
    }

    private MotionEvent newMotionEvent(MotionEvent ev) {
        return MotionEvent.obtain(ev.getDownTime(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_CANCEL, ev.getX(), ev.getY(), 0);

    }

    public final void setOnRefreshListener(OnRefreshListener listener) {
        mOnRefreshListener = listener;
    }

    public final void setOnLoadMoreListener(OnLoadMoreListener listener) {
        mOnLoadMoreListener = listener;
    }

    public void listviewScrollBy(int y) {
        this.smoothScrollBy(y,500);
    }

    /**
     * Simple Listener to listen for any callbacks to Refresh.
     *
     * @author Chris Banes
     */
    public static interface OnRefreshListener<V extends View> {

        /**
         * onRefresh will be called for both a Pull from start, and Pull from
         * end
         */
        public void onRefresh(PullListView refreshView);

    }

    /**
     * Simple Listener to listen for any callbacks to Refresh.
     *
     * @author Chris Banes
     */
    public static interface OnLoadMoreListener<V extends View> {

        /**
         * onRefresh will be called for both a Pull from start, and Pull from
         * end
         */
        public void onLoadMore(PullListView refreshView);

    }


    private void animateRoup()
    {
        if(mCurrentSmoothScrollRunnable==null) {

            int start = mTop;
            if(headerView!=null)
            {
                headerView.clearAnimation();
                start += headerView.getPaddingTop();
            }
            mCurrentSmoothScrollRunnable = new SmoothScrollRunnable(
                    start, 0, 100,
                    new OnSmoothScrollFinishedListener() {
                        @Override
                        public void onSmoothScrollFinished() {
                            mCurrentSmoothScrollRunnable = null;
                        }
                    });
            postDelayed(mCurrentSmoothScrollRunnable, 10);
        }
    }

    public void onCompleteLoadMore(int status) {
        if (isLoadMore) {
            if(status == LOAD_MORE_STATUE_NO_MORE){
                state = LOAD_MORE_STATUE_NO_MORE;
            } else if (status == LOAD_MORE_STATUE_SYNC_ERROR){
                state = LOAD_MORE_STATUE_SYNC_ERROR;
            } else if (status == LOAD_MORE_STATUE_HOME_PAGE){
                state = LOAD_MORE_STATUE_HOME_PAGE;
            } else {
                state = DONE;
            }
            isLoadMore = false;
            isAbleToLoadMore = false;
            changeFooterViewByState();
        }
    }

    public void setNoMoreData() {
        state = LOAD_MORE_STATUE_NO_MORE;
        isLoadMore = false;
        isAbleToLoadMore = false;
        changeFooterViewByState();
    }


    final class SmoothScrollRunnable implements Runnable {
        private final Interpolator mInterpolator;
        private final int mScrollToY;
        private final int mScrollFromY;
        private final long mDuration;
        private OnSmoothScrollFinishedListener mListener;

        private boolean mContinueRunning = true;
        private long mStartTime = -1;
        private int mCurrentY = -1;

        public SmoothScrollRunnable(int fromY, int toY, long duration,
                                    OnSmoothScrollFinishedListener listener) {
            mScrollFromY = fromY;
            mScrollToY = toY;
            mInterpolator = mScrollAnimationInterpolator;
            mDuration = duration;
            mListener = listener;
        }

        @Override
        public void run() {

            /**
             * Only set mStartTime if this is the first time we're starting,
             * else actually calculate the Y delta
             */
            if (mStartTime == -1) {
                mStartTime = System.currentTimeMillis();
            } else {

                /**
                 * We do do all calculations in long to reduce software float
                 * calculations. We use 1000 as it gives us good accuracy and
                 * small rounding errors
                 */
                long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime))
                        / mDuration;
                normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);

                final int deltaY = Math.round((mScrollFromY - mScrollToY)
                        * mInterpolator
                        .getInterpolation(normalizedTime / 1000f));
                mCurrentY = mScrollFromY - deltaY;
                pullEvent(mCurrentY);
            }

            // If we're not at the target Y, keep going...
            if (mContinueRunning && mScrollToY != mCurrentY) {
                ViewCompat.postOnAnimation(PullListView.this, this);
            } else {
                if (null != mListener) {
                    mListener.onSmoothScrollFinished();
                }
            }
        }

        public void stop() {
            mContinueRunning = false;
            removeCallbacks(this);
        }
    }

    static interface OnSmoothScrollFinishedListener {
        void onSmoothScrollFinished();
    }

    private void pullEvent(float newScrollValue) {
        // scrollTo(0, (int) newScrollValue + mTop);d
        if(headerView!=null) {
            headerView.setPadding(0, Math.min((int) newScrollValue - mTop, mMaxTop), 0, 0);
        }
    }

    private boolean isFirstItemVisible() {
        final Adapter adapter = getAdapter();
        if (null == adapter || adapter.isEmpty()) {
            return true;
        } else {
            if (getFirstVisiblePosition() <= 1) {
                final View firstVisibleChild = getChildAt(0);
                if (firstVisibleChild != null) {
                    int listLeftTop[] = new int[2];
                    getLocationInWindow(listLeftTop);
                    int childeLeftTop[] = new int[2];
                    firstVisibleChild.getLocationInWindow(childeLeftTop);
                    return childeLeftTop[1] >= listLeftTop[1];
                }
            }
        }
        return false;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (firstVisibleItem + visibleItemCount == totalItemCount
                && totalItemCount > 0 && !isRefreshing && !isLoadMore) {

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // TODO Auto-generated method stub
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            if (view.getLastVisiblePosition() == view.getCount() - 1
                    && !isRefreshing && !isLoadMore) {
//                if (mOnLoadMoreListener != null) {
//                    isLoadMore = true;
//                    mOnLoadMoreListener.onLoadMore(getInstance());
//                    Toast.makeText(getContext(), "load more", Toast.LENGTH_LONG)
//                            .show();
//                }
            }
        }
    }

    public void setLoadMoreUseless() {
        state = LOAD_MORE_STATUS_USELESS;
    }
}
