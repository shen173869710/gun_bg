package com.auto.di.guan.jobqueue.event;

/**
 *  绑定设备成功
 */
public class BindSucessEvent {
    private boolean isOk;
    private int type;

    public BindSucessEvent(boolean isOk, int type) {
        this.isOk = isOk;
        this.type = type;
    }

    public boolean isOk() {
        return isOk;
    }

    public void setOk(boolean ok) {
        isOk = ok;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
