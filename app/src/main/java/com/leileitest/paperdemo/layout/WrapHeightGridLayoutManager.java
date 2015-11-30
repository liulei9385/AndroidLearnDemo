package com.leileitest.paperdemo.layout;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by liulei
 * 21:56
 * 2015/11/29
 */
public class WrapHeightGridLayoutManager extends GridLayoutManager {

    private int spanRows = 1;
    private int measuredWidth = 0;
    private int measuredHeight = 0;

    private int verticalSpace;

    public void setVerticalSpace(int verticalSpace) {
        this.verticalSpace = verticalSpace;
    }

    private RecyclerView mRecyclerView;

    public void setSpanRows(int spanRows) {
        if (this.spanRows != spanRows) {
            this.spanRows = spanRows;
        }
    }

    public WrapHeightGridLayoutManager(Context context, AttributeSet attrs,
                                       int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public WrapHeightGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public WrapHeightGridLayoutManager(Context context, int spanCount,
                                       int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    @Override
    public void onItemsChanged(RecyclerView recyclerView) {
        super.onItemsChanged(recyclerView);
    }

    @Override
    public void onAttachedToWindow(RecyclerView view) {
        super.onAttachedToWindow(view);
        mRecyclerView = view;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state,
                          int widthSpec, int heightSpec) {
        //super.onMeasure(recycler, state, widthSpec, heightSpec);
        if (measuredHeight <= 0) {
            if (getItemCount() > 0) {
                View firstView = recycler.getViewForPosition(0);
                if (firstView != null) {
                    measureChild(firstView, widthSpec, heightSpec);
                    measuredWidth = View.MeasureSpec.getSize(widthSpec);
                    int exrasHeight = mRecyclerView.getPaddingTop() + mRecyclerView.getPaddingBottom();
                    exrasHeight += verticalSpace * (spanRows - 1);
                    measuredHeight = firstView.getMeasuredHeight() * spanRows + exrasHeight;
                }
            }
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }
}