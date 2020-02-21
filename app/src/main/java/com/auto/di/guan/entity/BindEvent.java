package com.auto.di.guan.entity;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class BindEvent {
    /*
     *  绑定设备的类型
     */
    public int type;
    public String status;


    public BindEvent(String status) {
        this.status = status;
    }

    public BindEvent(int type, String status) {
        this.type = type;
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
