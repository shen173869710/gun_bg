package com.auto.di.guan.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/7/9.
 */

@Entity
public class GroupInfo {
    @Id(autoincrement = true)
    private Long id;
    public int groupId;
    public String groupName;
    public int groupStatus;
    public int groupImage;
    public int groupLevel;
    public int groupTime;
    public int groupRunTime;
    @Generated(hash = 789287909)
    public GroupInfo(Long id, int groupId, String groupName, int groupStatus,
            int groupImage, int groupLevel, int groupTime, int groupRunTime) {
        this.id = id;
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupStatus = groupStatus;
        this.groupImage = groupImage;
        this.groupLevel = groupLevel;
        this.groupTime = groupTime;
        this.groupRunTime = groupRunTime;
    }
    @Generated(hash = 1250265142)
    public GroupInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getGroupId() {
        return this.groupId;
    }
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
    public String getGroupName() {
        return this.groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    public int getGroupStatus() {
        return this.groupStatus;
    }
    public void setGroupStatus(int groupStatus) {
        this.groupStatus = groupStatus;
    }
    public int getGroupImage() {
        return this.groupImage;
    }
    public void setGroupImage(int groupImage) {
        this.groupImage = groupImage;
    }
    public int getGroupLevel() {
        return this.groupLevel;
    }
    public void setGroupLevel(int groupLevel) {
        this.groupLevel = groupLevel;
    }
    public int getGroupTime() {
        return this.groupTime;
    }
    public void setGroupTime(int groupTime) {
        this.groupTime = groupTime;
    }
    public int getGroupRunTime() {
        return this.groupRunTime;
    }
    public void setGroupRunTime(int groupRunTime) {
        this.groupRunTime = groupRunTime;
    }

}
