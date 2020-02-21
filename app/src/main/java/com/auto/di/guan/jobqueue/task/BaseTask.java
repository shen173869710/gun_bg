package com.auto.di.guan.jobqueue.task;

import android.os.Handler;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.jobqueue.TestEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.OutputStream;

public abstract class BaseTask {

    public final String BASETAG = "BaseTask == ";
    public final int RETRY_COUNT = 2;

    private int taskCount = RETRY_COUNT;
    private int taskType;
    private ControlInfo taskInfo;


    /**
     *   开始执行命令Eventbus
     */
    public abstract  void pushEvnt();

    /**
     *   开始执行命令
     */
    public abstract  void startTask(OutputStream mOutputStream);
    /**
     *   命令异常
     */
    public abstract void errorTask(OutputStream mOutputStream);
    /**
     *   任务执行结束
     */
    public abstract void endTask(String receive,OutputStream mOutputStream);
    /**
     *   重试执行任务
     */
    public abstract void retryTask(OutputStream mOutputStream);

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public ControlInfo getTaskInfo() {
        return taskInfo;
    }

    public void setTaskInfo(ControlInfo taskInfo) {
        this.taskInfo = taskInfo;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public void writeCmd(OutputStream mOutputStream, String cmd) {
//        try {
//            mOutputStream.write(new String(cmd).getBytes());
//            mOutputStream.write('\n');
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (cmd.contains("rgid")) {
                    EventBus.getDefault().post(new TestEvent("gid=001"));
                }else if (cmd.contains("rbid")) {
                    EventBus.getDefault().post(new TestEvent("bid=001"));
                }else{
                    EventBus.getDefault().post(new TestEvent("err"));
                }

            }
        },2000);
    }
}
