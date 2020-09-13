package com.auto.di.guan.entity;

import com.auto.di.guan.db.GroupInfo;

/**
 * Created by Administrator on 2017/12/22 0022.
 */

public class MessageEvent {

    public int flag;
    public GroupInfo groupInfo;

    public MessageEvent(int flag) {
        this.flag = flag;
    }

    public MessageEvent(int flag, GroupInfo groupInfo) {
        this.flag = flag;
        this.groupInfo = groupInfo;
    }
}
