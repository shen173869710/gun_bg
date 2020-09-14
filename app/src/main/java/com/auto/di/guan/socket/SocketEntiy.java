package com.auto.di.guan.socket;

import android.text.TextUtils;

import org.greenrobot.eventbus.EventBus;

public class SocketEntiy {
    /**
     *    水泵控制相关 ip 端口号
     */
    public static final String IP = "192.168.3.100";
    public static final int PORT = 5000;

    /**
     *    如果状态是2-3 就60 000秒查询一次
     */
    public static final long STATUS_1_3 = 60000;
    /**
     *    如果状态是2-3 就180000毫秒查询一次 handler what
     */
    public static final int STATUS_1_3_HANDLER = 1;
    /**
     *    如果状态是1 就120 000毫秒查询一次
     */
    public static final long STATUS_2 = 120000;

    public static final int STATUS_2_HANDLER = 2;

    public static String []SOCKET_PARM = {
            "电流",
            "电压",
            "状态",
            "错误"
    };

    public static String getSocketStatus(String status) {
        String value = "";
        if (TextUtils.isEmpty(status)) {
            return "";
        }
        if ("0".equals(status)){
            value = "待机";
        }else if ("1".equals(status)){
            value = "启动";
        }else if ("2".equals(status)){
            value = "运行";
        }else if ("3".equals(status)){
            value = "停车";
        }else if ("4".equals(status)){
            value = "故障";
        }
        return value;
    }

    public static String getSocketError(String error) {
        String value = "";
        if (TextUtils.isEmpty(error)) {
            return "";
        }
        if ("0".equals(error)){
            value = "操作正常";
        }else if ("1".equals(error)){
            value = "无法连接";
        }else if ("2".equals(error)){
            value = "过流保护";
        }else {
            value = "其他错误";
        }
        return value;
    }
    /**
     *   获取开启
     */
    public static byte[] getSockenOpen(String nameCode) {
        String cmd = "kb 20000 "+nameCode+"               ";
        return cmd.getBytes();
    }
    /**
     *   关
     */
    public static byte[] getSockenClose(String nameCode) {
        String cmd = "gb 20000 "+nameCode+"               ";
        return cmd.getBytes();
    }

    /**
     *  查询
     * @param nameCode
     * @return
     */
    public static byte[] getSockenRead(String nameCode) {
        String cmd = "cb 20000 "+nameCode+"               ";
        return cmd.getBytes();
    }

    /**
     *     解析
     * @param data
     */
    public static void prare(String data) {
        SocketResult event = new SocketResult();
        String[]result = data.split(" ");
        if (result != null) {
            int len = result.length;

            if (data.toLowerCase().contains("kb") && data.toLowerCase().contains("ok")) {
                event.setType(result[0]);
                event.setProjectId(result[1]);
                event.setNameCode(result[2]);
                new UdpSendThread(SocketEntiy.getSockenRead(event.getNameCode())).start();
            } else if (data.toLowerCase().contains("gb") && data.toLowerCase().contains("ok")){
                event.setType(result[0]);
                event.setProjectId(result[1]);
                event.setNameCode(result[2]);
                new UdpSendThread(SocketEntiy.getSockenRead(event.getNameCode())).start();
            }else if ("bzt".equals(result[0]) && len >= 7){
                event.setType(result[0]);
                event.setProjectId(result[1]);
                event.setNameCode(result[2]);
                event.setStatusValue(result[3]);
                event.setVoltageValue(result[4]+"V");
                event.setElectricityValue(result[5]+"A");
                event.setErrorCodeValue(getSocketError(result[6]));
                EventBus.getDefault().post(event);
            }
        }
    }
}
