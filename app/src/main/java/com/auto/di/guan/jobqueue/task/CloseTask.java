package com.auto.di.guan.jobqueue.task;

import com.auto.di.guan.jobqueue.TaskManger;
import com.auto.di.guan.utils.LogUtils;

import java.io.OutputStream;

/**
 *   开启阀门的任务
 */
public class CloseTask extends BaseTask{
    private final String TAG = BASETAG+"CloseTask";
    private String taskCmd;

    @Override
    public void startTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "关阀 开始=======  cmd =="+getTaskCmd());
        writeCmd(mOutputStream, getTaskCmd());
    }

    @Override
    public void errorTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "关阀 异常 ======="+"errorTask()");
        TaskManger.getInstance().doNextTask();
    }

    /**
     *  kf 012 004 0 01
     *  kf 012 004 0 ok
     * @param receive
     * @param mOutputStream
     */
    @Override
    public void endTask(String receive, OutputStream mOutputStream) {
        LogUtils.e(TAG, "关阀 通信成功====收到信息 =="+receive+ " receive.length() = "+receive.length());

        /**
         *   如果是未知的命令 如果count == 2 重试一次
         *                   如果count == 1 进入错误
         */
        if (!receive.toLowerCase().contains("zt") &&
            !receive.toLowerCase().contains("rs") &&
                !receive.toLowerCase().contains("gf")
                && !receive.toLowerCase().contains("kf")) {
            /**
             *
             */
            if (getTaskCount() == 2) {
                setTaskCount(1);
                retryTask(mOutputStream);
            }else {
                errorTask(mOutputStream);
            }
        }else {
            /**g
             *   如果数据包含kf 说明阀成功  执行下一个任务
             */
            if(receive.toLowerCase().contains("gf") && receive.toLowerCase().contains("ok")) {
                TaskManger.getInstance().doNextTask();
            }
        }
    }

    @Override
    public void retryTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "开阀 开启 重试======="+"retryTask()  cmd =="+getTaskCmd());
        writeCmd(mOutputStream, getTaskCmd());
    }

    public String getTaskCmd() {
        return taskCmd;
    }

    public void setTaskCmd(String taskCmd) {
        this.taskCmd = taskCmd;
    }
}
