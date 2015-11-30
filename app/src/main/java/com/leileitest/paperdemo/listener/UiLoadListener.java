package com.leileitest.paperdemo.listener;

import android.view.View;

/**
 * Created by liulei
 * 17:17
 * 2015/11/29
 */
public interface UiLoadListener {

    /**
     * 初始化View
     */
    void initView();

    /**
     * @param itemView 初始化View
     */
    void initFramgentView(View itemView);

    /**
     * 获取intent传递的数据
     */
    void prepareData();

    /**
     * 加载数据
     */
    void obtainData();

    /**
     * 配置界面
     */
    void configUi();
}
