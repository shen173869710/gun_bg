package com.auto.di.guan.entity;

import com.auto.di.guan.db.GroupInfo;


public class GroupOptionEvent {


    public GroupOptionEvent(GroupInfo groupInfo, boolean isStart) {
        this.groupInfo = groupInfo;
        this.isStart = isStart;
    }

    public GroupInfo groupInfo;

    public GroupOptionEvent(GroupInfo groupInfo, GroupInfo closeInfo, boolean isStart) {
        this.groupInfo = groupInfo;
        this.closeInfo = closeInfo;
        this.isStart = isStart;
    }

    public GroupInfo closeInfo;
    public boolean isStart;

}
