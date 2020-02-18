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
        //        kf 108 003 0    通讯成功 answer_str  kf 108 003 0 ok
        //        gf 108 003 0    通讯成功 answer_str  gf 108 003 0 ok
        //        rs 108 003      通讯成功 answer_str  zt 108 003 1100 090

        /**开启控制阀**/
        if (msg.contains(KF)) {
//            String[]result = msg.split(" ");
//            if(result != null && result.length == 5) {
//                status.projectId = result[1];
            status.projectId = msg.substring(3, 6);
//                status.deviceId = result[2];
            status.deviceId = msg.substring(7, 10);
//                status.name = result[3];
            status.name = msg.substring(11, 12);
            String ok = msg.substring(13, 15);
            status.type = KF;
            if (OK.toLowerCase().contains(ok)) {
                status.status = Entiy.CONTROL_STATUS＿RUN;
            }

//            }
        }
        //        gf 108 003 1110
        /**关闭控制阀**/
        if (msg.contains(GF)) {
            String[] result = msg.split(" ");
//            if(result != null && result.length == 5) {
//                status.projectId = result[1];
//                status.deviceId = result[2];
//                status.name = result[3];
            status.projectId = msg.substring(3, 6);
            status.deviceId = msg.substring(7, 10);
            status.name = msg.substring(11, 12);
            String ok = msg.substring(13, 15);
            status.type = GF;
            if (OK.toLowerCase().contains(ok)) {
                status.status = Entiy.CONTROL_STATUS＿CONNECT;
            }

//            }
        }

//        zt 108 003 1110  zt 108 003 1100 090
        /**读取控制阀**/
        if (msg.contains(ZT)) {
//            String[]result = msg.split(" ");
//            if(result != null && result.length == 5) {
            status.type = ZT;
            status.projectId = msg.substring(3, 6);
            status.deviceId = msg.substring(7, 10);
            status.code = msg.substring(11, 15);
//                status.projectId = result[1];
//                status.deviceId = result[2];
//                status.code = result[3];
            status.elect = msg.substring(16, 19);
//            }
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
                valueStatus0 = 0;
                valueStatus1 = 0;
            } else if (type.contains("0100")) {
                valueStatus0 = 0;
                valueStatus1 = Entiy.CONTROL_STATUS＿CONNECT;
            } else if (type.contains("0101")) {
                valueStatus0 = 0;
                valueStatus1 = Entiy.CONTROL_STATUS＿RUN;
            } else if (type.contains("1000")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿CONNECT;
                valueStatus1 = 0;
            } else if (type.contains("1010")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿RUN;
                valueStatus1 = 0;
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
                valueStatus0 = 0;
                valueStatus1 = Entiy.CONTROL_STATUS＿NOTCLOSE;
            } else if (type.contains("1030")) {
                valueStatus0 = Entiy.CONTROL_STATUS＿NOTCLOSE;
                valueStatus1 = 0;
            } else {
                return null;
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
