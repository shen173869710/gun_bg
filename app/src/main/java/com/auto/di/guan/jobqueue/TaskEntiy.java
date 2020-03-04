package com.auto.di.guan.jobqueue;

public class TaskEntiy {
    /**
     *   绑定项目gid
     */
    public static final int TASK_TYPE_GID = 1;
    /**
     *   绑定gid
     */
    public static final int TASK_TYPE_BID = 2;
    /**
     *   读取gid
     */
    public static final int TASK_READ_GID = 3;
    /**
     *   读取bid
     */
    public static final int TASK_READ_BID = 4;
    /**
     *   单个的开启task
     */
    public static final int TASK_OPTION_OPEN = 5;
    /**
     *   单个的关闭task
     */
    public static final int TASK_OPTION_ClOSE = 6;
    /**
     *   读取开状态task
     */
    public static final int TASK_OPTION_OPEN_READ = 7;
    /**
     *   读取关状态task
     */
    public static final int TASK_OPTION_CLOSE_READ = 8;
    /**
     *   读取状态task
     */
    public static final int TASK_OPTION_READ = 9;

    /**
     *   手动分组开启查结束标志位
     */
    public static final int TASK_OPTION_GROUP_OPEN_READ_END = 10;
    /**
     *   手动分组关闭查询束标志位
     */
    public static final int TASK_OPTION_GROUP_CLOSE_READ_END = 11;



    /**
     *        是否绑定设备成功
     * @param receive  ok
     * @return
     */
    public static boolean isBindGidSuccess(String receive) {
        if(receive.toLowerCase().contains("ok") && receive.length() == 2) {
            return true;
        }
        return false;
    }
}
