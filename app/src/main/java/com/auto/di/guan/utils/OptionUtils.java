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
        /**开启控制阀**/
        if (msg.contains(KF)) {
            String[]result = msg.split(" ");
            if(result != null && result.length == 5) {
                status.projectId = result[1];
                status.deviceId = result[2];
                status.name = result[3];
                status.type = KF;
                if (OK.contains(result[4])) {
                    status.status = Entiy.CONTROL_STATUS＿RUN;
                }

            }
        }
        /**关闭控制阀**/
        if (msg.contains(GF)) {
            String[]result = msg.split(" ");
            if(result != null && result.length == 5) {
                status.type = GF;
                status.projectId = result[1];
                status.deviceId = result[2];
                status.name = result[3];
                if (OK.contains(result[4])) {
                    status.status = Entiy.CONTROL_STATUS＿CONNECT;
                }

            }
        }

//        zt 108 003 1110
        /**读取控制阀**/
        if (msg.contains(ZT)) {
            String[]result = msg.split(" ");
            if(result != null && result.length == 5) {
                status.type = ZT;
                status.projectId = result[1];
                status.deviceId = result[2];
                status.code = result[3];
                status.elect =result[4];
            }
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
        if (status != null && !TextUtils.isEmpty(status.code)) {
            DeviceInfo info = new DeviceInfo();
            info.controlInfos = new ArrayList<>();
            info.controlInfos.add(new ControlInfo());
            info.controlInfos.add(new ControlInfo());
            String type = status.code;
            if (type.contains("0000")) {
                info.controlInfos.get(0).status = 0;
                info.controlInfos.get(1).status = 0;
            }else if (type.contains("0100")) {
                info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿CONNECT;
                info.controlInfos.get(1).status = 0;
            }else if (type.contains("0101")) {
                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿RUN;
                    info.controlInfos.get(1).status = 0;
            }else if (type.contains("1000")) {
                    info.controlInfos.get(0).status = 0;
                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿CONNECT;
            }else if (type.contains("1010")) {
                    info.controlInfos.get(0).status = 0;
                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿RUN;
            }else if (type.contains("1100")) {
                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿CONNECT;
                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿CONNECT;
            }else if (type.contains("1110")) {
                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿RUN;
                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿CONNECT;
            }else if (type.contains("1101")) {
                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿CONNECT;
                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿RUN;
            }else if (type.contains("1111")) {
                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿RUN;
                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿RUN;
            }else {
                return null;
            }

//            switch (Integer.valueOf(status.code)) {
//                case 0:
//                    LogUtils.e("0000---------------", " "+status.code);
//                    info.controlInfos.get(0).status = 0;
//                    info.controlInfos.get(1).status = 0;
//                    break;
//                case 100:
//                    LogUtils.e("0100---------------", " "+status.code);
//                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿CONNECT;
//                    info.controlInfos.get(1).status = 0;
//                    break;
//                case 101:
//                    LogUtils.e("0101---------------", " "+status.code);
//                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿RUN;
//                    info.controlInfos.get(1).status = 0;
//                    break;
//                case 1000:
//                    LogUtils.e("1000---------------", " "+status.code);
//                    info.controlInfos.get(0).status = 0;
//                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿CONNECT;
//                    break;
//                case 1010:
//                    LogUtils.e("1010---------------", " "+status.code);
//                    info.controlInfos.get(0).status = 0;
//                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿RUN;
//                    break;
//                case 1100:
//                    LogUtils.e("1100---------------", " "+status.code);
//                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿CONNECT;
//                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿CONNECT;
//                    break;
//                case 1110:
//                    LogUtils.e("1110---------------", " 第一个链接 正常工作， 第二个链接 没有开启");
//                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿RUN;
//                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿CONNECT;
//                    break;
//                case 1101:
//                    LogUtils.e("1101---------------", " "+status.code);
//                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿CONNECT;
//                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿RUN;
//                    break;
//                case 1111:
//                    LogUtils.e("1111---------------", " "+status.code);
//                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿RUN;
//                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿RUN;
//                    break;
//            }
            return info;
        }else {
            return null;
        }
    }
}
