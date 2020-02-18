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
        int size =  deviceInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo controlInfo_0 = deviceInfos.get(i).getValveDeviceSwitchList().get(0);
            ControlInfo controlInfo_1 = deviceInfos.get(i).getValveDeviceSwitchList().get(1);
            if (info.getValve_id() == controlInfo_1.getValve_id()) {
                controlInfo_0.setValve_group_id(0);
            }
            if (info.getValve_id() == controlInfo_1.getValve_id()) {
                controlInfo_1.setValve_group_id(0);
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
        int size =  deviceInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo controlInfo_0 = deviceInfos.get(i).getValveDeviceSwitchList().get(0);
            ControlInfo controlInfo_1 = deviceInfos.get(i).getValveDeviceSwitchList().get(1);
            if (groupId == controlInfo_0.getValve_group_id()) {
                controlInfos.add(controlInfo_0);
            }
            if (groupId == controlInfo_1.getValve_group_id()) {
                controlInfos.add(controlInfo_1);
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
        int size =  deviceInfos.size();
        for (int i = 0; i < size; i++) {
            controlInfos.add(deviceInfos.get(i).getValveDeviceSwitchList().get(0));
            controlInfos.add(deviceInfos.get(i).getValveDeviceSwitchList().get(1));

        }
        return controlInfos;
    }


    /**
     * 查询分组列表
     */
    public static List<ControlInfo> queryBindControlList() {
        ArrayList<ControlInfo>controlInfos = new ArrayList<>();
        List<DeviceInfo>deviceInfos = DeviceInfoSql.queryDeviceList();
        int size =  deviceInfos.size();
        for (int i = 0; i < size; i++) {
            DeviceInfo deviceInfo = deviceInfos.get(i);
            if (deviceInfo.getDeviceStatus()== Entiy.DEVEICE_BIND) {
                controlInfos.add(deviceInfo.getValveDeviceSwitchList().get(0));
                controlInfos.add(deviceInfo.getValveDeviceSwitchList().get(1));
            }
        }
        return controlInfos;
    }
}
