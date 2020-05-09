package com.auto.di.guan.utils;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.entity.CmdStatus;

import org.greenrobot.eventbus.EventBus;

public class SendUtils {

    public static final String HTML_GREEN_START ="<b><font color=\"#37b660\">";
    public static final String HTML_BLACK_START ="<b><font color=\"#000000\">";
    public static final String HTML_END="</font></b>";

    private static String TAG = SendUtils.class.getSimpleName()+"----";
    public static String LOG_READ_START = "读取开始\n";
    public static String LOG_OPEN_START = "开启开始\n";
    public static String LOG_CLOSE_START = "关闭开始\n";

    public static String LOG_OPEN_SUC = "开阀通信成功\n";
    public static String LOG_OPEN_RET = "开阀通信重试\n";
    public static String LOG_CLOSE_SUC = "关阀通信成功\n";
    public static String LOG_CLOSE_RET = "关阀通信重试\n";

    public static String LOG_OPEN_FALIE = "开阀失败\n";
    public static String LOG_CLOSE_FALIE  = "关阀失败\n";

    public static String LOG_READ_RET = "读取重试\n";
    public static String LOG_READ_SUC = "读取成功\n";
    public static String LOG_READ_END = "读取结束\n";
    public static String LOG_NAME = "阀门";
    /***
     *    开阀的相关状态
     */
    //  开阀正常
    public static final int OPTION_OPEN_SUCESS = 10;
    public static final String OPTION_OPEN_SUCESS_VALUE = "开启操作正常";
    //  开阀失败
    public static final int OPTION_OPEN_FAILE = 20;
    public static final String OPTION_OPEN_FAILE_VALUE = "通信正常,阀门开启失败";
    //  开阀异常 阀门无法打开
    public static final int OPTION_OPEN_ERROR = 30;
    public static final String OPTION_OPEN_ERROR_VALUE = "通信正常,阀门无法打开";
    //  开阀失败 阀门线断开
    public static final int OPTION_OPEN_DIS = 40;
    public static final String OPTION_OPEN_DIS_VALUE = "开启失败,阀门线未连接";
    //  开阀失败 其他异常
    public static final int OPTION_OPEN_OTHER = 50;
    public static final String OPTION_OPEN_OTHER_VALUE = "开启失败,未知异常";

    //  关阀正常
    public static final int OPTION_CLOSE_SUCESS = 100;
    public static final String OPTION_CLOSE_SUCESS_VALUE = "关闭操作正常";
    //  关阀失败
    public static final int OPTION_CLOSE_FAILE = 200;
    public static final String OPTION_CLOSE_FAILE_VALUE = "通信正常,阀门关闭失败";
    //  关阀失败 阀门无法关闭
    public static final int OPTION_CLOSE_ERROR = 300;
    public static final String OPTION_CLOSE_ERROR_VALUE = "通信正常,阀门无法关闭";
    //  关阀失败 阀门线断开
    public static final int OPTION_CLOSE_DIS = 400;
    public static final String OPTION_CLOSE_DIS_VALUE = "关闭失败,阀门线未连接";
    //  关阀失败 其他异常
    public static final int OPTION_CLOSE_OTHER = 500;
    public static final String OPTION_CLOSE_OTHER_VALUE = "通信正常,未知异常";

    //  读取失败
    public static final int OPTION_READ_CONNECT = 1000;
    public static final String OPTION_READ_CONNECT_VALUE = "链接正常,关阀状态";
    //  读取失败
    public static final int OPTION_READ_RUN = 2000;
    public static final String OPTION_READ_RUN_VALUE = "链接正常,开阀状态";
    //  读取失败
    public static final int OPTION_READ_FAILE = 3000;
    public static final String OPTION_READ_FAILE_VALUE = "读取失败,未知异常";
    //  读取成功 状态不对
    public static final int OPTION_READ_ERROR = 4000;
    public static final String OPTION_READ_ERROR_VALUE = "读取成功,阀门无法关闭";
    //  读取成功 状态不对
    public static final int OPTION_READ_DIS = 5000;
    public static final String OPTION_READ_DIS_VALUE = "读取成功,阀门线未连接";

    /**
     *        开阀发送信息
     * @param desc
     * @param info
     */
    public static  void sendopen(String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.setControlName(info.getValve_alias());
        cmdStatus.setCmd_start(LOG_OPEN_START+desc);
        cmdStatus.setControl_id(info.getValve_id());
        EventBus.getDefault().post(cmdStatus);
    }

    /**
     *       关阀门发送信息
     * @param desc
     * @param info
     */
    public static  void sendClose( String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.setCmd_start(LOG_CLOSE_START+desc);
        cmdStatus.setControlName(info.getValve_alias());
        cmdStatus.setControl_id(info.getValve_id());
        EventBus.getDefault().post(cmdStatus);
    }

    /**
     *      开阀结束发送信息
     * @param desc
     * @param info
     */
    public static  void sendOpenEnd(String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.setCmd_end(LOG_OPEN_SUC+desc);
        cmdStatus.setControlName(info.getValve_alias());
        cmdStatus.setControl_id(info.getValve_id());
        EventBus.getDefault().post(cmdStatus);
    }

    /**
     *      开阀结束发送信息
     * @param desc
     * @param info
     */
    public static  void sendOpenRet(String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.setCmd_end(LOG_OPEN_RET+desc);
        cmdStatus.setControlName(info.getValve_alias());
        cmdStatus.setControl_id(info.getValve_id());
        EventBus.getDefault().post(cmdStatus);
    }

    /**
     *      关闭阀结束发送信息
     * @param desc
     * @param info
     */
    public static  void sendCloseEnd(String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.setCmd_end(LOG_CLOSE_SUC+desc);
        cmdStatus.setControlName(info.getValve_alias());
        cmdStatus.setControl_id(info.getValve_id());
        EventBus.getDefault().post(cmdStatus);
    }

    /**
     *      关闭阀结通信重试
     * @param desc
     * @param info
     */
    public static  void sendCloseRet(String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.setCmd_end(LOG_CLOSE_RET+desc );
        cmdStatus.setControlName(info.getValve_alias());
        cmdStatus.setControl_id(info.getValve_id());
        EventBus.getDefault().post(cmdStatus);
    }

    public static  void sendOpenError(String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.setCmd_end(LOG_OPEN_FALIE+desc);
        cmdStatus.setControl_id(info.getValve_id());
        cmdStatus.setControlName(info.getValve_alias());
        EventBus.getDefault().post(cmdStatus);
    }

    public static  void sendCloseError(String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.setCmd_end(LOG_CLOSE_FALIE+desc);
        cmdStatus.setControl_id(info.getValve_id());
        cmdStatus.setControlName(info.getValve_alias());
        EventBus.getDefault().post(cmdStatus);
    }


    /**
     *        读取开始
     * @param desc
     * @param info
     */
    public static  void sendReadStart( String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.setControlName(info.getValve_alias());
        cmdStatus.setCmd_read_start(LOG_READ_START+desc);
        cmdStatus.setControl_id(info.getValve_id());
        EventBus.getDefault().post(cmdStatus);
    }
    /**
     *        读取获取数据
     * @param desc
     * @param info
     */
    public static  void sendReadMiddle( String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.setControlName(info.getValve_alias());
        cmdStatus.setCmd_read_middle(LOG_READ_SUC+desc);
        cmdStatus.setControl_id(info.getValve_id());
        EventBus.getDefault().post(cmdStatus);
    }
    /**
     *        读取获取数据
     * @param desc
     * @param info
     */
    public static  void sendReadTryMiddle( String desc, ControlInfo info) {
        CmdStatus cmdStatus = new CmdStatus();
        cmdStatus.setControlName(info.getValve_alias());
        cmdStatus.setCmd_read_middle(LOG_READ_RET+desc);
        cmdStatus.setControl_id(info.getValve_id());
        EventBus.getDefault().post(cmdStatus);
    }

    /**
     *        读取结束
     * @param type
     * @param info
     */
    public static void  sendReadEnd( ControlInfo info, int cmd_type, int optionType, int type, boolean isSaveDb) {
        CmdStatus cmdStatus = new CmdStatus();
        String name = info.getValve_alias();
        int controlId = info.getValve_id();
        String desc = "";
        boolean isNormal = false;
        switch (type) {
            case OPTION_OPEN_SUCESS:
                isNormal = true;
                desc = OPTION_OPEN_SUCESS_VALUE;
                break;
            case OPTION_OPEN_FAILE:
                desc = OPTION_OPEN_FAILE_VALUE;
                break;
            case OPTION_OPEN_ERROR:
                desc = OPTION_OPEN_ERROR_VALUE;
                break;
            case OPTION_OPEN_DIS:
                desc = OPTION_OPEN_DIS_VALUE;
                break;
            case OPTION_OPEN_OTHER:
                desc = OPTION_OPEN_OTHER_VALUE;
                break;
            case OPTION_CLOSE_SUCESS:
                isNormal = true;
                desc = OPTION_CLOSE_SUCESS_VALUE;
                break;
            case OPTION_CLOSE_FAILE:
                desc = OPTION_CLOSE_FAILE_VALUE;
                break;
            case OPTION_CLOSE_ERROR:
                desc = OPTION_CLOSE_ERROR_VALUE;
                break;
            case OPTION_CLOSE_DIS:
                desc = OPTION_CLOSE_DIS_VALUE;
                break;
            case OPTION_CLOSE_OTHER:
                desc = OPTION_CLOSE_OTHER_VALUE;
                break;
            case OPTION_READ_FAILE:
                desc = OPTION_READ_FAILE_VALUE;
                break;
            case OPTION_READ_ERROR:
                desc = OPTION_READ_ERROR_VALUE;
                break;
            case OPTION_READ_CONNECT:
                desc = OPTION_READ_CONNECT_VALUE;
                break;
            case OPTION_READ_RUN:
                desc = OPTION_READ_RUN_VALUE;
                break;
            case OPTION_READ_DIS:
                desc = OPTION_READ_DIS_VALUE;
                break;
        }
        cmdStatus.setCmd_read_end(LOG_NAME + name + desc);
        cmdStatus.setControlName(name);
        cmdStatus.setControl_id(controlId);
        EventBus.getDefault().post(cmdStatus);

        if (isSaveDb) {
            ActionUtil.saveAction(info,cmd_type,optionType, desc, isNormal);
        }
    }
}
