package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.entity.Entiy;

import java.util.ArrayList;
import java.util.List;

import static com.auto.di.guan.db.sql.DeviceInfoSql.updateDeviceList;

public class ControlInfoSql extends BaseSql {


    public static void updateControl(ControlInfo info) {
        List<DeviceInfo> deviceInfos = DeviceInfoSql.queryDeviceList();
        for (int i = 0; i < deviceInfos.size(); i++) {
            if (info.controId == deviceInfos.get(i).getControlInfos().get(0).controId) {
                deviceInfos.get(i).getControlInfos().get(0).groupId = 0;
            }
            if (info.controId == deviceInfos.get(i).getControlInfos().get(1).controId) {
                deviceInfos.get(i).getControlInfos().get(1).groupId = 0;
            }
        }
        updateDeviceList(deviceInfos);
    }



    /**
     * 查询分组列表
     */
    public static List<ControlInfo> queryControlList(int groupId) {
        ArrayList<ControlInfo> controlInfos = new ArrayList<>();
        List<DeviceInfo>deviceInfos = DeviceInfoSql.queryDeviceList();
        for (int i = 0; i < deviceInfos.size(); i++) {
            if (groupId == deviceInfos.get(i).getControlInfos().get(0).groupId) {
                controlInfos.add(deviceInfos.get(i).controlInfos.get(0));
            }
            if (groupId == deviceInfos.get(i).getControlInfos().get(1).groupId)
            {
                controlInfos.add(deviceInfos.get(i).controlInfos.get(1));
            }
        }
        return controlInfos;
    }

    /**
     * 查询分组列表
     */
    public static List<ControlInfo> queryControlList() {
        ArrayList<ControlInfo>controlInfos = new ArrayList<>();
        List<DeviceInfo>deviceInfos = DeviceInfoSql.queryDeviceList();
        for (int i = 0; i < deviceInfos.size(); i++) {
            controlInfos.add(deviceInfos.get(i).controlInfos.get(0));
            controlInfos.add(deviceInfos.get(i).controlInfos.get(1));

        }
        return controlInfos;
    }


    /**
     * 查询分组列表
     */
    public static List<ControlInfo> queryBindControlList() {
        ArrayList<ControlInfo>controlInfos = new ArrayList<>();
        List<DeviceInfo>deviceInfos = DeviceInfoSql.queryDeviceList();
        for (int i = 0; i < deviceInfos.size(); i++) {
            if (deviceInfos.get(i).status == Entiy.DEVEICE_BIND) {
                controlInfos.add(deviceInfos.get(i).controlInfos.get(0));
                controlInfos.add(deviceInfos.get(i).controlInfos.get(1));
            }
        }
        return controlInfos;
    }
}
