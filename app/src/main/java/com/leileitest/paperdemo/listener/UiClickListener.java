package com.leileitest.paperdemo.listener;

import android.view.View;

/**
 * Created by liulei
 * 17:51
 * 2015/11/29
 */
public abstract class UiClickListener implements View.OnClickListener {

    @Override
    public void onClick(View v) {
        onViewClicked(v.getId(), v);
    }

    public abstract void onViewClicked(int vId, View v);

}