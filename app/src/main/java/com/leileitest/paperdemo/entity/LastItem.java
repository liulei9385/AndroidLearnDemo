package com.leileitest.paperdemo.entity;

/**
 * Created by sunjie on 2015/11/26 0026.
 */
public class LastItem {
    private Class classz;
    private String notice;

    public LastItem(Class classz, String notice) {
        this.classz = classz;
        this.notice = notice;
    }

    public Class getClassz() {
        return classz;
    }

    public String getNotice() {
        return notice;
    }

}
