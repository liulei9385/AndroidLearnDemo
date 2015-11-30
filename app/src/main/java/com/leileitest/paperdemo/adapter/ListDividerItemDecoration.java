package com.leileitest.paperdemo.adapter;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * USER: liulei
 * DATE: 2015/4/15.
 * TIME: 16:22
 */

public class ListDividerItemDecoration extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int drawDividerHeight;
    private float marginLeft;
    private float marginRight;

    // list底部是否画divider
    private boolean isDrawDividreForBottom = false;

    public ListDividerItemDecoration(Drawable drawable) {
        this(1, drawable);
    }

    public ListDividerItemDecoration(int drawDividerHeight, Drawable mDivider) {
        if (drawDividerHeight < 0)
            drawDividerHeight = 0;
        this.drawDividerHeight = drawDividerHeight;
        this.mDivider = mDivider;
    }

    public void setMarginLeft(float marginLeft) {
        this.marginLeft = marginLeft;
    }

    public void setMarginRight(float marginRight) {
        this.marginRight = marginRight;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = (int) (parent.getPaddingLeft() + marginLeft);
        int right = (int) (parent.getWidth()
                - parent.getPaddingRight() - marginRight);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + drawDividerHeight;
            //解决 android 9 Drawable.draw() 绘制的问题
            if (mDivider != null) {
                if (i + 1 >= childCount && !isDrawDividreForBottom)
                    return;
                final Rect bounds = new Rect(left, top, right, bottom);
                c.save();
                c.clipRect(bounds);
                mDivider.setBounds(bounds);
                mDivider.draw(c);
                c.restore();
            }
        }
    }

    public void setIsDrawDividreForBottom(boolean isDrawDividreForBottom) {
        this.isDrawDividreForBottom = isDrawDividreForBottom;
    }
}
