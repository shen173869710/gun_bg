package com.auto.di.guan.jobqueue.task;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.event.BindIdEvent;
import com.auto.di.guan.jobqueue.TaskManger;
import com.auto.di.guan.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.OutputStream;

public class BindIdTask extends BaseTask{
    private final String TAG = BASETAG+"BindIdTask";
    private String taskCmd;

    @Override
    public void pushEvnt() {
        LogUtils.e(TAG, "发送事件=======  pushEvnt ==");
        EventBus.getDefault().post(new BindIdEvent(getTaskType()));
    }

    @Override
    public void startTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "写入id 开始=======  cmd =="+getTaskCmd());
        writeCmd(mOutputStream, getTaskCmd());
    }

    @Override
    public void errorTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "写入id 错误 ======="+"errorTask()");
        TaskManger.getInstance().doNextTask(mOutputStream);
    }

    @Override
    public void endTask(String receive, OutputStream mOutputStream) {
        LogUtils.e(TAG, "写入id 结束 ======="+"endTask()"+"====收到信息 =="+receive);
        if (receive.toLowerCase().contains("ok") && receive.length() == 2) {
            if (getTaskType() == TaskEntiy.TASK_TYPE_GID) {
                LogUtils.e(TAG, "写入项目gid 正常 =======");
                ReadIdTask gTask = new ReadIdTask();
                gTask.setTaskType(TaskEntiy.TASK_READ_GID);
                gTask.setTaskCmd("rgid");
                TaskManger.getInstance().addTask(gTask);
                gTask.pushEvnt();

            }else if (getTaskType() == TaskEntiy.TASK_TYPE_BID) {
                LogUtils.e(TAG, "写入项目bid 正常 =======");
                ReadIdTask bTask = new ReadIdTask();
                bTask.setTaskType(TaskEntiy.TASK_READ_BID);
                bTask.setTaskCmd("rbid");
                TaskManger.getInstance().addTask(bTask);
                bTask.pushEvnt();
            }
        }else {
            if (getTaskCount() == 2) {
                setTaskCount(1);
                retryTask(mOutputStream);
            }else {
                errorTask(mOutputStream);
            }
        }
    }

    @Override
    public void retryTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "写入项目gid 重试======="+"retryTask()  cmd =="+getTaskCmd());
        writeCmd(mOutputStream, getTaskCmd());
    }

    public String getTaskCmd() {
        return taskCmd;
    }

    public void setTaskCmd(String taskCmd) {
        this.taskCmd = taskCmd;
    }
}
