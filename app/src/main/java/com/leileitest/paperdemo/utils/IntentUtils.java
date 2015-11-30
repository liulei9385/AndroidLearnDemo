package com.leileitest.paperdemo.utils;

import android.content.Context;
import android.content.Intent;

import com.leileitest.paperdemo.activity.BaseActivity;

/**
 * USER: liulei
 * DATE: 2015/4/24.
 * TIME: 10:29
 */
public class IntentUtils {

    public static final int FLAG_SINGLE_TOP = Intent.FLAG_ACTIVITY_CLEAR_TOP
            | Intent.FLAG_ACTIVITY_SINGLE_TOP;

    public static Intent setUpIntent(Context context, Class<? extends BaseActivity> clazz) {
        return new Intent(context, clazz);
    }

    public static Intent setUpIntent(String action) {
        return new Intent(action);
    }

    public static Intent setSingleTop(Context context, Class<? extends BaseActivity> clazz) {
        Intent intent = setUpIntent(context, clazz);
        return intent.setFlags(FLAG_SINGLE_TOP);
    }

    public static Intent setSingleTop(String action) {
        Intent intent = setUpIntent(action);
        return intent.setFlags(FLAG_SINGLE_TOP);
    }

    public static Object getParcelableData(Intent intent, String key) {
        Object result = null;
        if (intent != null) {
            boolean hasExtras = intent.hasExtra(key);
            if (hasExtras) {
                result = intent.getParcelableExtra(key);
            }
        }
        return result;
    }

}
