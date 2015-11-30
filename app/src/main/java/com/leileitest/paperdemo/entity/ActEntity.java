package com.leileitest.paperdemo.entity;

import com.leileitest.paperdemo.activity.BaseActivity;

/**
 * Created by liulei
 * 18:13
 * 2015/11/29
 */
public class ActEntity {

    private Class<? extends BaseActivity> actClazz;
    private String showName;

    public ActEntity(Class<? extends BaseActivity> actClazz) {
        this.actClazz = actClazz;
        if (actClazz != null)
            this.setShowName(actClazz.getSimpleName());
    }

    public ActEntity(Class<? extends BaseActivity> actClazz, String showName) {
        this.actClazz = actClazz;
        this.showName = showName;
    }

    public Class<? extends BaseActivity> getActClazz() {
        return actClazz;
    }

    public void setActClazz(Class<? extends BaseActivity> actClazz) {
        this.actClazz = actClazz;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public ActEntity() {
    }
}
