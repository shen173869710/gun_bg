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
    private String userAccount;
    private int controlId;
    private long time;
    private int actionId;
    private String actionDesc;
    private String actionEnd;
    private String userName;
    @Generated(hash = 68402018)
    public UserAction(Long id, String userId, String userAccount, int controlId,
            long time, int actionId, String actionDesc, String actionEnd,
            String userName) {
        this.id = id;
        this.userId = userId;
        this.userAccount = userAccount;
        this.controlId = controlId;
        this.time = time;
        this.actionId = actionId;
        this.actionDesc = actionDesc;
        this.actionEnd = actionEnd;
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
    public String getUserAccount() {
        return this.userAccount;
    }
    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }
    public int getControlId() {
        return this.controlId;
    }
    public void setControlId(int controlId) {
        this.controlId = controlId;
    }
    public long getTime() {
        return this.time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    public int getActionId() {
        return this.actionId;
    }
    public void setActionId(int actionId) {
        this.actionId = actionId;
    }
    public String getActionDesc() {
        return this.actionDesc;
    }
    public void setActionDesc(String actionDesc) {
        this.actionDesc = actionDesc;
    }
    public String getActionEnd() {
        return this.actionEnd;
    }
    public void setActionEnd(String actionEnd) {
        this.actionEnd = actionEnd;
    }
    public String getUserName() {
        return this.userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }


}
