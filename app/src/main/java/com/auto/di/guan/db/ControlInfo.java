package com.auto.di.guan.db;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/29.
 */
public class ControlInfo implements Serializable {
    static final long serialVersionUID=2L;
    @Id(autoincrement = true)
    private Long id;
    private int valve_group_id;
    //设备 组 id
    private int device_id;
    //设备id
    private int valve_id;
    // 阀门图片地址
    private String valve_imgage_path;
    // 阀门图片id地址
    private int valve_imgage_id;
    // 阀门的通信id
    private String valve_name;
    /**
     * 绑定设备的ID
     *  显示的名字
     */
    private String valve_alias;
    /**
     *   0  未添加
     *   1  已经添加
     *   2  已经连接
     *   3  工作当中
     *   4  异常当中
     */
    private int valve_status;
    //创建者
    private String create_by;
    //创建时间
    private String create_time;
    // 更新者
    private String update_by;
    //更新时间
    private String update_time;
    //通信协议项目ID
    private String protocalId;
    //通信协议ID
    private String deviceProtocalId;
    @Transient
    private boolean isSelect;

    public ControlInfo() {

    }
    public ControlInfo(int imageId, String name) {
        this.valve_imgage_id = imageId;
        this.valve_name = name;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getValve_group_id() {
        return valve_group_id;
    }

    public void setValve_group_id(int valve_group_id) {
        this.valve_group_id = valve_group_id;
    }

    public int getDevice_id() {
        return device_id;
    }

    public void setDevice_id(int device_id) {
        this.device_id = device_id;
    }

    public int getValve_id() {
        return valve_id;
    }

    public void setValve_id(int valve_id) {
        this.valve_id = valve_id;
    }

    public String getValve_imgage_path() {
        return valve_imgage_path;
    }

    public void setValve_imgage_path(String valve_imgage_path) {
        this.valve_imgage_path = valve_imgage_path;
    }

    public int getValve_imgage_id() {
        return valve_imgage_id;
    }

    public void setValve_imgage_id(int valve_imgage_id) {
        this.valve_imgage_id = valve_imgage_id;
    }

    public String getValve_name() {
        return valve_name;
    }

    public void setValve_name(String valve_name) {
        this.valve_name = valve_name;
    }

    public String getValve_alias() {
        return valve_alias;
    }

    public void setValve_alias(String valve_alias) {
        this.valve_alias = valve_alias;
    }

    public int getValve_status() {
        return valve_status;
    }

    public void setValve_status(int valve_status) {
        this.valve_status = valve_status;
    }

    public String getCreate_by() {
        return create_by;
    }

    public void setCreate_by(String create_by) {
        this.create_by = create_by;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getProtocalId() {
        return protocalId;
    }

    public void setProtocalId(String protocalId) {
        this.protocalId = protocalId;
    }

    public String getDeviceProtocalId() {
        return deviceProtocalId;
    }

    public void setDeviceProtocalId(String deviceProtocalId) {
        this.deviceProtocalId = deviceProtocalId;
    }
}
