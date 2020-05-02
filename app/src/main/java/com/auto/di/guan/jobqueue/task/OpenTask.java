package com.auto.di.guan.jobqueue.task;

import android.os.Handler;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.SendUtils;

/**
 *   开启阀门的任务
 *
 *   kf 012 004 0 01
 *   kf 012 004 0 ok
 */
public class OpenTask extends BaseTask{
    private final String TAG = BASETAG+"OpenTask";

    public OpenTask(int taskType, String taskCmd) {
        super(taskType, taskCmd);
    }

    public OpenTask(int taskType, String taskCmd, ControlInfo taskInfo) {
        super(taskType, taskCmd, taskInfo);
    }


    @Override
    public void startTask() {
        LogUtils.e(TAG, "开阀 开始======================================= cmd =="+getTaskCmd());
        SendUtils.sendopen(getTaskCmd(), getTaskInfo());
        writeCmd(getTaskCmd());
    }

    @Override
    public void errorTask() {
        LogUtils.e(TAG, "开阀 异常 ======="+"errorTask()");
        SendUtils.sendOpenError(getReceive(), getTaskInfo());
        finishTask();
    }

    /**
     *  kf 012 004 0 01
     *  kf 012 004 0 ok
     * @param receive
     */
    @Override
    public void endTask(String receive) {
        setReceive(receive);
        LogUtils.e(TAG, "开阀 通信成功====收到信息 =="+receive+ " receive.length() = "+receive.length());
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
            /**
             *   如果数据包含kf 说明阀成功  执行下一个任务
             */
            if(receive.toLowerCase().contains("kf") && receive.toLowerCase().contains("ok")) {
                SendUtils.sendOpenEnd(receive, getTaskInfo());
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
        LogUtils.e(TAG, "开阀 开启 重试======="+"retryTask()  cmd =="+getTaskCmd()+ " 当前重试次数 = "+getTaskCount());
        if(getTaskCount() == 2) {
            setTaskCount(1);
            SendUtils.sendOpenRet(getReceive(), getTaskInfo());
            writeCmd(getTaskCmd());
        }else {
            errorTask();
        }
    }

}
