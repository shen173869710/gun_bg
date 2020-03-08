package com.auto.di.guan.entity;

import com.auto.di.guan.BaseApp;

/**
 * Created by Administrator on 2017/7/16.
 */

public class Entiy {


    /**
     * 操作的页面
     * <p>
     * 400  单个手动操作
     * 310  单个分组操作
     * 320  自动分组操作
     */
    public static int FRAGMENT_4 = 400;
    public static int FRAGMENT_31 = 310;
    public static int FRAGMENT_32 = 320;
    public static int FRAGMENT_0 = 1000;
    /**
     *   设置轮灌时间比例时间
     */
    public static final int RUN_TIME = 60;
    public static final int RUN_TIME_COUNT = 1000;

    public static final int RUN_DO_NEXT = 1;
    /**
     *  报警信息
     */
    //  开阀异常
    public static final int VIDEO_OPEN_ERROR = 1;
    //  关阀异常
    public static final int VIDEO_CLOSE_ERROR =2;
    //  查询异常
    public static final int VIDEO_READ_ERROR = 5;

    public static String ALERM_ACTION = "com.auto.di.guan.utils.LongRunningService";
    public static int GROUP_START = 100000;
    public static int GROUP_STOP = 100001;
    public static int  GROUP_NEXT = 100002;
    public static int GEID_ALL_ITEM = BaseApp.getUser().getPileOutNum() * BaseApp.getUser().getTrunkPipeNum();
    public static int GRID_COLUMNS = BaseApp.getUser().getPileOutNum();
    public static int GRID_ROW = BaseApp.getUser().getTrunkPipeNum();
    public static String []TAB_TITLE = {"增减阀控器",
            "绑定阀门",
            "轮灌分组",
            "轮灌操作",
            "手动操作",
            "查询用户",
            "用户管理",
            "系统维护",
            "读取参数",
            "视频监控",
            "农田管理",
            "退出登录"
    };
    /**
     *    可以分组
     */
    public static final int LEVEL_2 = 200;
    /**
     *    可以轮灌
     */
    public static final int LEVEL_3 = 300;
    /**
     *    可以手动
     */
    public static final int LEVEL_4 = 400;
    /**
     *    可以分组 可以轮灌
     */
    public static final int LEVEL_2_3 = 500;
    /**
     *    可以分组 可以手动
     */
    public static final int LEVEL_2_4 = 700;
    /**
     *    可以轮灌 可以手动
     */
    public static final int LEVEL_3_4 = 800;
    /**
     *    可以分组 可以手动
     */
    public static final int LEVEL_2_3_4 = 900;

    /**
     *    群组状态 关闭
     */
    public static final int GROUP_STATUS_COLSE = 0;
    /**
     *    群组状态 开启
     */
    public static final int GROUP_STATUS_OPEN = 1;


    /**
     *    设备是否添加
     */
    public static final int DEVEICE_UNBIND = 0;
    /**
     *    设备已经添加
     */
    public static final int DEVEICE_BIND = 1;
    /**控制阀已经链接成功**/
    public static final int CONTROL_STATUS＿CONNECT = 100;
    /**设备已经处于运行状态**/
    public static final int CONTROL_STATUS＿RUN = 200;
    /**设备已经处于错误状态**/
    public static final int CONTROL_STATUS＿ERROR = 300;
    /**设备无法关闭**/
    public static final int CONTROL_STATUS＿NOTCLOSE = 400;
    /**设备断开链接**/
    public static final int CONTROL_STATUS＿DISCONNECT = 0;


    public static  String writeBid(String bid) {
        if (bid != null) {
            if (bid.length() == 1) {
                bid = "00"+bid;
            }else if (bid.length() ==2) {
                bid = "0"+bid;
            }
        }

        return "sbid"+bid;
    }

    public static  String getBid(String bid) {
        if (bid != null) {
            if (bid.length() == 1) {
                bid = "00"+bid;
            }else if (bid.length() ==2) {
                bid = "0"+bid;
            }
        }

        return bid;
    }

    public static  String writeGid(String gid) {
        if (gid != null) {
            if (gid.length() == 1) {
                gid = "00"+gid;
            }else if (gid.length() ==2) {
                gid = "0"+gid;
            }
        }
        return "sgid"+gid;
    }

    /**
     *        rs 012 004 0 01
     *        zt 012 004 xxxx
     *
     *        串命令“rs xxx xxx 0 01↙”(最后一个字符是”回车”)。其中第三个字符是空格符，第
             4、5、6字符是三位ASCII字符表示的一个0-255的数，该数是无线阀控器的项目
             ID, 第7个字符是空格，第8、9、10字符是三位ASCII字符表示的一个0-255的数，
             该数是无线阀控器的板ID, 第11个字符是空格，第12个字符是ASCII字符“0”,第
             13个字符是空格，第14和15字符是ASCII字符“0”和“1”。例如要查询项目ID为
             12，板ID为4的无线阀控器连接的2个电磁阀的状态可以使用以下命令
             “rs 012 004 0 01↙”。

     *         控制项目ID为12，板ID为4的无线阀控器收到“rs 012 004 0 01↙”命令，除
                 了查询2个连接的电磁阀的状态，还会向控制终端返馈以下字符串
                 “zt 012 004 xxxx↙”(最后一个字符是”回车”)，第12个字符是ASCII字符“0”或“1”，
                 如果是“0”，表明本阀控器第一个端口没有连接电磁阀，如果是“1”， 表明本阀控器
                 第一个端口有连接电磁阀，第13个字符是ASCII字符“0”或“1”， 如果是“0”，表明
                 本阀控器第2个端口没有连接电磁阀，如果是“1”， 表明本阀控器第2个端口有连
                 接电磁阀，第14个字符是ASCII字符“0”或“1”， 如果是“1”，表明本阀控器第一个
                 端口连接的电磁阀已打开，如果是“0”， 表明本阀控器第一个端口连接的电磁阀
                 已关闭。第15个字符是ASCII字符“0”或“1”， 如果是“1”，表明本阀控器第2个端
                 口连接的电磁阀已打开，如果是“0”， 表明本阀控器第2个端口连接的电磁阀已关
                 闭。

     * @param groupId
     * @param deviceId
     * @return
     */
    public static  String cmdRead(String groupId, String deviceId) {
        return "rs"+" "+groupId+" "+deviceId;
    }

    /**
     *       kf 012 004 0 01
     *       kf 012 004 0 ok
     * @param groupId
     * @param deviceId
     * @param deveiceName
     * @return
     */
    public static  String cmdOpen(String groupId, String deviceId, String deveiceName) {
        return "kf"+" "+groupId+" "+deviceId+" "+deveiceName;
    }

    /**
     *        gf 012 004 0 01
     *        gf 012 004 0 ok
     * @param groupId
     * @param deviceId
     * @param deveiceName
     * @return
     */
    public static  String cmdClose(String groupId, String deviceId, String deveiceName) {
        return "gf"+" "+groupId+" "+deviceId+" "+deveiceName;
    }

    public static String LOG_OPEN_START = "开启---";
    public static String LOG_CLOSE_START = "关闭---";


    public static int ACTION_TYPE_4 = 100;
    public static int ACTION_TYPE_31 = 31;
    public static int ACTION_TYPE_32 = 32;
    public static int ACTION_TYPE_ERROR = -1;


    /**
     *  根据项目位置生成通信ID
     * @return
     */
    public static  String createProtocalId(int id) {
        return String.format("%03d",id);
    }

    public static  String createGid(String gid) {
        if (gid != null) {
            if (gid.length() == 1) {
                gid = "00"+gid;
            }else if (gid.length() ==2) {
                gid = "0"+gid;
            }
        }
        return "sgid"+gid;
    }
}
