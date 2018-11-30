package com.auto.di.guan.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/28.
 */
@Entity
public class User implements Serializable {
    static final long serialVersionUID=1L;
    @Id(autoincrement = true)
    private Long id;

    private String userId;
    private String account;
    private String password;
    private String name;
    private int level;
    @Generated(hash = 212737623)
    public User(Long id, String userId, String account, String password,
            String name, int level) {
        this.id = id;
        this.userId = userId;
        this.account = account;
        this.password = password;
        this.name = name;
        this.level = level;
    }
    @Generated(hash = 586692638)
    public User() {
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
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getLevel() {
        return this.level;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
