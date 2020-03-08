package com.auto.di.guan.jobqueue.event;

import com.auto.di.guan.db.GroupInfo;
/**
 *     自动轮灌事件
 */
public class AutoTaskEvent {
    private int type;
    private GroupInfo groupInfo;

    public AutoTaskEvent(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public AutoTaskEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
}
