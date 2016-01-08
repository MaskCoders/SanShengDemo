package com.sansheng.testcenter.base;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SquareImageView extends ImageView {

    public SquareImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // TODO Auto-generated constructor stub
    }

    public SquareImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public SquareImageView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    private Bitmap mBitmap;

    @Override
    public void setImageBitmap(Bitmap bm)
    {
        super.setImageBitmap(bm);
        if (mBitmap != null && !mBitmap.isRecycled()) {
            mBitmap.recycle();
        }
        this.mBitmap = bm;
    }
}
