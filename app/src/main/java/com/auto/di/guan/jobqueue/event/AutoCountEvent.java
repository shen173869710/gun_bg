package com.auto.di.guan.jobqueue.event;

import com.auto.di.guan.db.GroupInfo;

/**
 *   自动轮灌计时器
 */
public class AutoCountEvent {
    private GroupInfo groupInfo;


    public AutoCountEvent(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
}
