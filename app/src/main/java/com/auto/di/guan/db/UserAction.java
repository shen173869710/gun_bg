package com.auto.di.guan.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/7/16.
 */
@Entity
public class UserAction {
    @Id(autoincrement = true)
    private Long id;

    private String userId;
    /**
     *   执行的命令
     *   开阀
     *   关阀
     */
    private String actionCmd;
    /***
     * 操作的类型
     *  0. 自动轮灌
     *  1. 手动单个操作
     *  2. 手动分组操作
     * **/
    private String actionName;
    /** 设备的id **/
    private int controlId;
    /** 保存的时间 **/
    private long actionTime;
    /** 动作的ID **/
    private int actionId;
    /**执行操作的类型**/
    private int actionType;
    /**执行操作的描述**/
    private String actionTypeName;
    /**执行操作的状态**/
    private boolean actionStatus;
    /**执行操作的状态显示的内容**/
    private String actionStatusName;
    private String userName;
    @Generated(hash = 935423754)
    public UserAction(Long id, String userId, String actionCmd, String actionName,
            int controlId, long actionTime, int actionId, int actionType,
            String actionTypeName, boolean actionStatus, String actionStatusName,
            String userName) {
        this.id = id;
        this.userId = userId;
        this.actionCmd = actionCmd;
        this.actionName = actionName;
        this.controlId = controlId;
        this.actionTime = actionTime;
        this.actionId = actionId;
        this.actionType = actionType;
        this.actionTypeName = actionTypeName;
        this.actionStatus = actionStatus;
        this.actionStatusName = actionStatusName;
        this.userName = userName;
    }
    @Generated(hash = 183515749)
    public UserAction() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getActionCmd() {
        return this.actionCmd;
    }
    public void setActionCmd(String actionCmd) {
        this.actionCmd = actionCmd;
    }
    public String getActionName() {
        return this.actionName;
    }
    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    public int getControlId() {
        return this.controlId;
    }
    public void setControlId(int controlId) {
        this.controlId = controlId;
    }
    public long getActionTime() {
        return this.actionTime;
    }
    public void setActionTime(long actionTime) {
        this.actionTime = actionTime;
    }
    public int getActionId() {
        return this.actionId;
    }
    public void setActionId(int actionId) {
        this.actionId = actionId;
    }
    public int getActionType() {
        return this.actionType;
    }
    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
    public String getActionTypeName() {
        return this.actionTypeName;
    }
    public void setActionTypeName(String actionTypeName) {
        this.actionTypeName = actionTypeName;
    }
    public boolean getActionStatus() {
        return this.actionStatus;
    }
    public void setActionStatus(boolean actionStatus) {
        this.actionStatus = actionStatus;
    }
    public String getActionStatusName() {
        return this.actionStatusName;
    }
    public void setActionStatusName(String actionStatusName) {
        this.actionStatusName = actionStatusName;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }


}
