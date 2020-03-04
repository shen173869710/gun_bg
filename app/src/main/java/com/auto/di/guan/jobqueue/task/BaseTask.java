package com.auto.di.guan.jobqueue.task;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.jobqueue.TaskManger;
import com.auto.di.guan.jobqueue.event.SendCmdEvent;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseTask {

    public final String BASETAG = "BaseTask == ";
    public final int RETRY_COUNT = 2;

    private int taskCount = RETRY_COUNT;
    private int taskType;
    private String taskCmd;
    private ControlInfo taskInfo;
    /**
     *   接收的数据
     */
    private String receive;

    public BaseTask(int taskType, String taskCmd) {
        this.taskType = taskType;
        this.taskCmd = taskCmd;
    }

    public BaseTask(int taskType, String taskCmd, ControlInfo taskInfo) {
        this.taskType = taskType;
        this.taskCmd = taskCmd;
        this.taskInfo = taskInfo;
    }

    /**
     *   开始执行命令
     */
    public abstract  void startTask();
    /**
     *   命令异常
     */
    public abstract void errorTask();
    /**
     *   任务执行结束
     */
    public abstract void endTask(String receive);
    /**
     *   重试执行任务
     */
    public abstract void retryTask();

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

    public void writeCmd(String cmd) {
        EventBus.getDefault().post(new SendCmdEvent(cmd));
//        try {
//            mOutputStream.write(new String(cmd).getBytes());
//            mOutputStream.write('\n');
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        FloatWindowUtil.getInstance().show();
//        new Handler().postDelayed(new Runnable() {
//        @Override
//        public void run() {
//
//            ControlInfo info = getTaskInfo();
//            String rec = "";
//            if (cmd.contains("rgid")) {
//                rec = "gid=001";
//            }else if (cmd.contains("rbid")) {
//                rec = "bid=001";
//            }else if(cmd.contains("kf")){
//                rec  = "kf"+" "+ BaseApp.getProjectId()+" "+ info.getDeviceProtocalId()+" "+ info.getProtocalId()+" "+"ok";
//            }else if (cmd.contains("rs")) {
//                rec  = "zt"+" "+ BaseApp.getProjectId()+" "+ info.getDeviceProtocalId()+" "+"1100 090";
//            }else if (cmd.contains("gf")) {
//                rec  = "gf"+" "+ BaseApp.getProjectId()+" "+ info.getDeviceProtocalId()+" "+ info.getProtocalId()+" "+"ok";
//            }else {
//                EventBus.getDefault().post(new TestEvent("err"));
//            }
//            EventBus.getDefault().post(new TestEvent(rec));
//        }
//    },2000);
}

    public int getRETRY_COUNT() {
        return RETRY_COUNT;
    }

    public String getTaskCmd() {
        return taskCmd;
    }

    public void setTaskCmd(String taskCmd) {
        this.taskCmd = taskCmd;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    /***
     *   任务完成
     */
    public void finishTask() {
        TaskManger.getInstance().doNextTask();
    }
}
