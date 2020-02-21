package com.auto.di.guan.jobqueue.event;

public class BindIdEvent {
    private int type;

    public BindIdEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
