package com.leileitest.paperdemo.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * sunjie
 * 2015/11/26
 * 16:00
 */
public class NoTouchRecycleView extends RecyclerView {

    public NoTouchRecycleView(Context context) {
        this(context, null);
    }

    public NoTouchRecycleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NoTouchRecycleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    private void init() {
    }

}