package com.auto.di.guan.db;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/29.
 */
public class ControlInfo implements Serializable {
    static final long serialVersionUID=2L;
    @Id(autoincrement = true)
    public Long id;

    public int groupId;
    public String deviceId;
    public int controId;
    public int imageId;
    public String name;
    public String controlName;
    /**
     *   0  未添加
     *   1  已经添加
     *   2  已经连接
     *   3  工作当中
     *   4  异常当中
     */
    public int status;
    /**
     *  管道类型
     */
    public int type;

    public boolean isSelect;
    public int time;
    public ControlInfo() {

    }
    public ControlInfo(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;
    }
}
