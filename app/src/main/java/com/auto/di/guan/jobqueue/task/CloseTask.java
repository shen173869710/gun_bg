package com.auto.di.guan.jobqueue.task;

import android.os.Handler;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.SendUtils;

/**
 *   开启阀门的任务
 */
public class CloseTask extends BaseTask{
    private final String TAG = BASETAG+"CloseTask";

    public CloseTask(int taskType, String taskCmd) {
        super(taskType, taskCmd);
    }

    public CloseTask(int taskType, String taskCmd, ControlInfo taskInfo) {
        super(taskType, taskCmd, taskInfo);
    }

    @Override
    public void startTask() {
        LogUtils.e(TAG, "关阀 开始==================================================  cmd =="+getTaskCmd());
        SendUtils.sendClose(getTaskCmd(), getTaskInfo());
        writeCmd(getTaskCmd());
    }

    @Override
    public void errorTask() {
        LogUtils.e(TAG, "关阀 异常 ======="+"errorTask()");
        SendUtils.sendCloseError(getReceive(), getTaskInfo());
        finishTask();
    }

    /**
     *  kf 012 004 0 01
     *  kf 012 004 0 ok
     * @param receive
     */
    @Override
    public void endTask(String receive) {
        LogUtils.e(TAG, "关阀 通信成功====收到信息 =="+receive+ " receive.length() = "+receive.length());
        setReceive(receive);
        /**
         *   如果是未知的命令 如果count == 2 重试一次
         *                   如果count == 1 进入错误
         */
        if (!receive.toLowerCase().contains("ok")) {
            /**
             *
             */
            retryTask();
        }else {
            /**g
             *   如果数据包含kf 说明阀成功  执行下一个任务
             */
            if(receive.toLowerCase().contains("gf") && receive.toLowerCase().contains("ok")) {
                SendUtils.sendCloseEnd(receive, getTaskInfo());

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finishTask();
                    }
                },3000);
            }else {
                errorTask();
            }
        }
    }

    @Override
    public void retryTask() {
        LogUtils.e(TAG, "关阀  重试======="+"retryTask()  cmd =="+getTaskCmd());
        String receive = getReceive();
        if(getTaskCount() == 2) {
            setTaskCount(1);
            SendUtils.sendCloseRet(receive, getTaskInfo());
            writeCmd(getTaskCmd());
        }else {
            errorTask();
        }
    }

}
