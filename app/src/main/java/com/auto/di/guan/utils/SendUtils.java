package com.auto.di.guan.utils;

import android.util.Log;

import com.auto.di.guan.MainActivity;
import com.auto.di.guan.MyApplication;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.entity.CmdStatus;

import org.greenrobot.eventbus.EventBus;

public class SendUtils {

    private static String TAG = SendUtils.class.getSimpleName()+"----";
    public static String LOG_READ_START = "读取状态开始\n";
    public static String LOG_OPEN_START = "开启开始\n";
    public static String LOG_CLOSE_START = "关闭开始\n";
    public static String LOG_SUC = "通信成功\n";
    public static String LOG_READ_SUC = "读取成功\n---";
    public static String LOG_READ_END = "读取结束\n";
    public static String LOG_NAME = "阀门";

    public static int TYPE_CONTENT = 1;
    public static int TYPE_DISCONTENT = -1;
    public static int TYPE_RUN = 0;
    public static int TYPE_NOT_CLOSE = -2;

    public static  void sendopen(String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.controlName = info.nickName;
        cmdStatus.cmd_start =LOG_OPEN_START+desc;
        cmdStatus.control_id = info.controId;
        EventBus.getDefault().post(cmdStatus);
//        Log.e(TAG, cmdStatus.cmd_start);
    }

    public static  void sendClose( String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.cmd_start =LOG_CLOSE_START+desc;
        cmdStatus.controlName =info.nickName;
        cmdStatus.control_id = info.controId;
        EventBus.getDefault().post(cmdStatus);
//        Log.e(TAG, cmdStatus.cmd_start);
    }

    public static  void sendRead( String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.controlName = info.nickName;
        cmdStatus.cmd_read_start =LOG_READ_START+desc;
        cmdStatus.control_id = info.controId;
        EventBus.getDefault().post(cmdStatus);
//        Log.e(TAG, cmdStatus.cmd_read_start);
    }

    public static  void sendMiddle(String desc, int controlId, String name) {
        CmdStatus cmdStatus = new CmdStatus();
        if (desc.contains("zt")) {
            cmdStatus.cmd_read_middle = LOG_READ_SUC+desc;
            Log.e(TAG, cmdStatus.cmd_read_middle);
        }else {
            cmdStatus.cmd_end = LOG_SUC+desc ;
            Log.e(TAG, cmdStatus.cmd_end);
        }
        cmdStatus.controlName = name;
        cmdStatus.control_id = controlId;
        EventBus.getDefault().post(cmdStatus);
    }


    public static  void sendError(String desc, int controlId, String name) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.cmd_end = LOG_READ_END+desc;
        cmdStatus.control_id = controlId;
        cmdStatus.controlName = name;
        EventBus.getDefault().post(cmdStatus);
        Log.e(TAG, cmdStatus.cmd_end);
    }

    public static void  sendEnd(int controlId, int type, String name) {
        CmdStatus cmdStatus = new CmdStatus();
        if (type == TYPE_RUN) {
            cmdStatus.cmd_read_end = LOG_NAME+name+"操作正常";
        }else if (type == TYPE_DISCONTENT) {
            cmdStatus.cmd_read_end = LOG_NAME+name+"通信正常,阀门线断开";
        }else if (type == TYPE_CONTENT) {
            cmdStatus.cmd_read_end = LOG_NAME+name+"通信异常,阀门未打开";
        }else if (type == TYPE_NOT_CLOSE) {
            cmdStatus.cmd_read_end = LOG_NAME+name+"通信异常,阀门未关闭";
        }

        cmdStatus.controlName = name;
        cmdStatus.control_id = controlId;
        EventBus.getDefault().post(cmdStatus);
    }
}
