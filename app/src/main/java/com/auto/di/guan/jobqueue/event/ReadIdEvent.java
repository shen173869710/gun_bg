package com.auto.di.guan.jobqueue.event;

public class ReadIdEvent {
    private int type;
    private String cmd;

    public ReadIdEvent(int type, String cmd) {
        this.type = type;
        this.cmd = cmd;
    }

    public int getType() {
        return type;
    }

    public void setTyep(int type) {
        this.type = type;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}
