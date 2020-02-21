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
