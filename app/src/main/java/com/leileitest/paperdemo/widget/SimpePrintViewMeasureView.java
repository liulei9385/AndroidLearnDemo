package com.leileitest.paperdemo.widget;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import com.leileitest.paperdemo.R;
import com.leileitest.paperdemo.utils.ViewUtils;

/**
 * think
 * 2015/12/9
 * 22:03
 */
public class SimpePrintViewMeasureView extends View {

    private Paint mPaint;
    private RectF drawRectF;

    public SimpePrintViewMeasureView(Context context) {
        this(context, null);
    }

    public SimpePrintViewMeasureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpePrintViewMeasureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG
                | Paint.FILTER_BITMAP_FLAG);
        drawRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        Context context = getContext();


        Drawable draIcLauncher = ContextCompat.getDrawable(context, R.mipmap.ic_launcher);
        Drawable hasWrapDr = DrawableCompat.wrap(draIcLauncher);
        int tintColor = ContextCompat.getColor(context, R.color.colorPrimaryDark);
        DrawableCompat.setTint(hasWrapDr, tintColor);
        int width = hasWrapDr.getIntrinsicWidth();
        int height = hasWrapDr.getIntrinsicHeight();
        int cWidth = canvas.getWidth();
        int cheight = canvas.getHeight();
        hasWrapDr.setBounds(cWidth / 2 - width / 2, cheight / 2 - height / 2,
                cWidth - (cWidth - width) / 2, cheight - (cheight - height) / 2);

        int accentColor = ContextCompat.getColor(context, R.color.colorAccent);


        int right = Math.min(cWidth, cheight);
        int bottom = Math.min(cWidth, cheight);
        int left = 0;
        int top = (cheight - bottom) / 2;
        int padding = (int) ViewUtils.dpToPx(context, 5.0f);
        drawRectF.set(left + padding, top + padding,
                left + right - padding, top + bottom - padding);

        mPaint.setColor(0x8000ff00);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawRect(drawRectF, mPaint);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(accentColor);
        canvas.drawRoundRect(drawRectF, cWidth / 2, cheight / 2, mPaint);

        mPaint.setStrokeWidth(ViewUtils.dpToPx(context, 2.0f));
        hasWrapDr.draw(canvas);

        canvas.restore();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int withMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int xSize = MeasureSpec.getSize(widthMeasureSpec);
        int ySize = MeasureSpec.getSize(heightMeasureSpec);
        simplePrint(withMode, xSize);
        simplePrint(heightMode, ySize);
    }

    public void simplePrint(int mode, int size) {
        switch (mode) {
            //EXACTLY --> MATCH_PARENT,56DP
            //AT_MOST --> WRAP_CONTENT
            case MeasureSpec.EXACTLY:
                System.out.println("##<<-" + "Exactly" + "->>##" + size);
                break;
            case MeasureSpec.UNSPECIFIED:
                System.out.println("##<<-" + "Unsecified" + "->>##" + size);
                break;
            case MeasureSpec.AT_MOST:
                System.out.println("##<<-" + "Atmost" + "->>##" + size);

        }
    }

}
