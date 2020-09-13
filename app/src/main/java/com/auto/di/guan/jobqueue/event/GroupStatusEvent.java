package com.auto.di.guan.jobqueue.event;

import com.auto.di.guan.db.GroupInfo;

/**
 *        自动轮灌组状态更新
 */
public class GroupStatusEvent {
    private GroupInfo groupInfo;
    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public GroupStatusEvent(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
}
