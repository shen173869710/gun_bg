package com.auto.di.guan.utils;

import android.text.TextUtils;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.OptionStatus;

import java.util.ArrayList;

public class OptionUtils {


    public final static String KF = "kf";
    public final static String GF = "gf";
    public final static String ZT = "zt";

    public final static String OK = "ok";

    public static OptionStatus receive(String msg) {
        OptionStatus status = new OptionStatus();
        status.allCmd = msg;
        if (TextUtils.isEmpty(msg)) {
            return null;
        }
        //                                             0123456789abcde
        //        kf 00108 003 0    通讯成功 answer_str  kf 00108 003 0 ok
        //        gf 00108 003 0    通讯成功 answer_str  gf 00108 003 0 ok
        //        rs 00108 003      通讯成功 answer_str  zt 00108 003 1100 090

        /**开启控制阀**/
        if (msg.contains(KF)) {
            status.projectId = msg.substring(3, 8);
            status.deviceId = msg.substring(9, 12);
            status.name = msg.substring(13, 14);
            String ok = msg.substring(15, 17);
            status.type = KF;
            if (OK.toLowerCase().contains(ok)) {
                status.status = Entiy.CONTROL_STATUS＿RUN;
            }
        }
        //        gf 108 003 1110
        /**关闭控制阀**/
        if (msg.contains(GF)) {
            status.projectId = msg.substring(3, 8);
            status.deviceId = msg.substring(9, 12);
            status.name = msg.substring(13, 14);
            String ok = msg.substring(15, 17);
            status.type = GF;
            if (OK.toLowerCase().contains(ok)) {
                status.status = Entiy.CONTROL_STATUS＿CONNECT;
            }
        }

//        zt 108 003 1110  zt 108 003 1100 090
        /**读取控制阀**/
        if (msg.contains(ZT)) {
            status.type = ZT;
            status.projectId = msg.substring(3, 8);
            status.deviceId = msg.substring(9, 12);
            status.code = msg.substring(13, 17);
            status.elect = msg.substring(18, 21);
        }

        if (TextUtils.isEmpty(status.type)) {
            return null;
        }
        return status;
    }

    // kf 012 004 0 ok
    // gf 012 004 0 ok
    //zt 012 004 xxxx

    public static DeviceInfo changeStatus(OptionStatus status) {
        //status = {"allCmd":"zt 102 002 1100 090\n\u0000","code":"1100","deviceId":"002","elect":"090","projectId":"102","type":"zt","status":0}
        if (status != null && !TextUtils.isEmpty(status.code)) {
            DeviceInfo info = new DeviceInfo();
            ArrayList<ControlInfo> controlInfos = new ArrayList<>();
            ControlInfo controlInfo0 = new ControlInfo();
            ControlInfo controlInfo1 = new ControlInfo();
            String type = status.code;
            int valueStatus0 = 0;
            int valueStatus1 = 0;
            if (type.contains("0000")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿DISCONNECT;
                valueStatus1 = Entiy.CONTROL_STATUS＿DISCONNECT;
            } else if (type.contains("0100")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿DISCONNECT;
                valueStatus1 = Entiy.CONTROL_STATUS＿CONNECT;
            } else if (type.contains("0101")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿DISCONNECT;
                valueStatus1 = Entiy.CONTROL_STATUS＿RUN;
            } else if (type.contains("1000")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿CONNECT;
                valueStatus1 = Entiy.CONTROL_STATUS＿DISCONNECT;
            } else if (type.contains("1010")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿RUN;
                valueStatus1 = Entiy.CONTROL_STATUS＿DISCONNECT;
            } else if (type.contains("1100")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿CONNECT;
                valueStatus1 = Entiy.CONTROL_STATUS＿CONNECT;
            } else if (type.contains("1110")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿RUN;
                valueStatus1 = Entiy.CONTROL_STATUS＿CONNECT;
            } else if (type.contains("1101")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿CONNECT;
                valueStatus1 = Entiy.CONTROL_STATUS＿RUN;
            } else if (type.contains("1111")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿RUN;
                valueStatus1 = Entiy.CONTROL_STATUS＿RUN;
            } else if (type.contains("1103")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿CONNECT;
                valueStatus1 = Entiy.CONTROL_STATUS＿NOTCLOSE;
            } else if (type.contains("1113")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿RUN;
                valueStatus1 = Entiy.CONTROL_STATUS＿NOTCLOSE;
            } else if (type.contains("1130")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿NOTCLOSE;
                valueStatus1 = Entiy.CONTROL_STATUS＿CONNECT;
            } else if (type.contains("1131")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿NOTCLOSE;
                valueStatus1 = Entiy.CONTROL_STATUS＿RUN;
            } else if (type.contains("1133")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿NOTCLOSE;
                valueStatus1 = Entiy.CONTROL_STATUS＿NOTCLOSE;
            } else if (type.contains("0103")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿DISCONNECT;
                valueStatus1 = Entiy.CONTROL_STATUS＿NOTCLOSE;
            } else if (type.contains("1030")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿NOTCLOSE;
                valueStatus1 = Entiy.CONTROL_STATUS＿DISCONNECT;
            } else{
                String index0 = type.substring(2,3);
                if ("0".equals(index0)) {
                    valueStatus0 = Entiy.CONTROL_STATUS＿CONNECT;
                }else if ("1".equals(index0)) {
                    valueStatus0 = Entiy.CONTROL_STATUS＿RUN;
                }else {
                    valueStatus0 = Entiy.CONTROL_STATUS＿ERROR;
                }
                String index1 = type.substring(3,4);
                if ("0".equals(index1)) {
                    valueStatus1 = Entiy.CONTROL_STATUS＿CONNECT;
                }else if ("1".equals(index1)) {
                    valueStatus1 = Entiy.CONTROL_STATUS＿RUN;
                }else {
                    valueStatus1 = Entiy.CONTROL_STATUS＿ERROR;
                }
            }
            controlInfo0.setValve_status(valueStatus0);
            controlInfo1.setValve_status(valueStatus1);
            controlInfos.add(controlInfo0);
            controlInfos.add(controlInfo1);
            info.setValveDeviceSwitchList(controlInfos);
            return info;
        } else {
            return null;
        }
    }
}
