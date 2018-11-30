package com.auto.di.guan.entity;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class ReadEvent {
    public String cmd;

    public ReadEvent(String cmd, int isGid) {
        this.cmd = cmd;
        this.isGid = isGid;
    }

    public int isGid;

    public ReadEvent(String cmd) {
        this.cmd = cmd;
    }
}
