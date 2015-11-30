package com.leileitest.paperdemo.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by liulei
 * 18:32
 * 2015/11/29
 */
public class ViewUtils {

    /**
     * 转化dip为px
     *
     * @param context
     * @param value
     * @return
     */
    public static float dpToPx(Context context, float value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getScreenMetric(context));
    }

    /**
     * 获取屏幕相关的参数
     *
     * @param context
     * @return
     */
    private static DisplayMetrics getScreenMetric(Context context) {
        return context.getResources().getDisplayMetrics();
    }

    /**
     * 获取屏幕尺寸
     *
     * @param context
     * @return
     */
    public static int[] getScreenSize(Context context) {
        DisplayMetrics metrics = getScreenMetric(context);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    public static int getStatusBarHeight(Context context) {
        int statusHeight = 0;
        try {
            Resources resources = context.getResources();
            int id = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (id > 0)
                statusHeight = context.getResources().getDimensionPixelSize(id);
        } catch (Exception ignored) {
        }
        return statusHeight;
    }
}
