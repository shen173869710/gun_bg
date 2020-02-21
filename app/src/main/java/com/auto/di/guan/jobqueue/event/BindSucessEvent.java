package com.auto.di.guan.jobqueue.event;

/**
 *  绑定设备成功
 */
public class BindSucessEvent {
    private int type;

    public BindSucessEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
