package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;

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
            if (info.getValve_id() == controlInfo_0.getValve_id()) {
                controlInfo_0.setValve_group_id(0);
                controlInfo_0.setSelect(info.isSelect());
            }
            if (info.getValve_id() == controlInfo_1.getValve_id()) {
                controlInfo_1.setValve_group_id(0);
                controlInfo_1.setSelect(info.isSelect());
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
     * 查询已经开启的设备
     */
    public static List<ControlInfo> queryControlRunList() {
        ArrayList<ControlInfo>controlInfos = new ArrayList<>();
        List<DeviceInfo>deviceInfos = DeviceInfoSql.queryDeviceList();
        int size =  deviceInfos.size();
        for (int i = 0; i < size; i++) {



        }
        return controlInfos;
    }
}
