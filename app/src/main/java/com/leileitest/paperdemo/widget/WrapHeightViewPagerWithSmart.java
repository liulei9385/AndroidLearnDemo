package com.leileitest.paperdemo.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * think
 * 2015/12/4
 * 8:45
 */
public class WrapHeightViewPagerWithSmart extends ViewPager {

    public WrapHeightViewPagerWithSmart(Context context) {
        this(context, null);
    }

    public WrapHeightViewPagerWithSmart(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureHeight = 0;
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);

        switch (mode) {

            case MeasureSpec.EXACTLY:
            case MeasureSpec.UNSPECIFIED:
                measureHeight = size;
                break;

            case MeasureSpec.AT_MOST:
                measureHeight = Math.min(size, measureHeight);
                break;
        }

        if (getChildCount() > 0) {
            View view = getChildAt(0);
            view.measure(0, 0);
            if (mode == MeasureSpec.AT_MOST) {
                measureHeight = Math.max(measureHeight, view.getMeasuredHeight());
            } else {
                //已子view的高度为准
                measureHeight = Math.max(measureHeight, view.getMeasuredHeight());
            }
        }
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), measureHeight);
        ViewGroup.LayoutParams params = getLayoutParams();
        //跟改layoutparams到一个合适的高度
        if (params.height != measureHeight) {
            params.height = measureHeight;
            setLayoutParams(params);
        }
    }

}