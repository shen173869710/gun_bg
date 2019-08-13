package com.auto.di.guan.db;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.ArrayList;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by czl on 2017/11/28.
 */
@Entity
public class DeviceInfo implements Serializable{
    static final long serialVersionUID=3L;
    @Id(autoincrement = true)
    public Long id;

    public String deviceName;
    public int groupId;
    public int deviceId;
    public String elect;
    /**
     *   0  未添加
     *   1  已经添加
     */
    public int status;
    /**
     *    0  控制阀
     *    1  管道
     */
    public int pipeType;

    @Convert(columnType =String.class, converter = ControlConvert.class)
    public ArrayList<ControlInfo>controlInfos;

    @Generated(hash = 344141083)
    public DeviceInfo(Long id, String deviceName, int groupId, int deviceId,
            String elect, int status, int pipeType,
            ArrayList<ControlInfo> controlInfos) {
        this.id = id;
        this.deviceName = deviceName;
        this.groupId = groupId;
        this.deviceId = deviceId;
        this.elect = elect;
        this.status = status;
        this.pipeType = pipeType;
        this.controlInfos = controlInfos;
    }

    @Generated(hash = 2125166935)
    public DeviceInfo() {
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

    public int getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getPipeType() {
        return this.pipeType;
    }

    public void setPipeType(int pipeType) {
        this.pipeType = pipeType;
    }

    public ArrayList<ControlInfo> getControlInfos() {
        return this.controlInfos;
    }

    public void setControlInfos(ArrayList<ControlInfo> controlInfos) {
        this.controlInfos = controlInfos;
    }

    public String getControl_1 () {
        String name = controlInfos.get(0).controlName;
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        return controlInfos.get(0).controlName;
    }

    public String getControl_2 () {
        String name = controlInfos.get(1).controlName;
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        return controlInfos.get(1).controlName;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getElect() {
        return this.elect;
    }

    public void setElect(String elect) {
        this.elect = elect;
    }
}
