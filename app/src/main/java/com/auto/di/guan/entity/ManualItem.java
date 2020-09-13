package com.auto.di.guan.entity;

/**
 * Created by Administrator on 2017/7/8.
 */

public class ManualItem {
    private String name;
    private boolean isSelect;

    public void setName(String name) {
        this.name = name;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getName() {
        return name;
    }

    public boolean isSelect() {
        return isSelect;
    }
}
