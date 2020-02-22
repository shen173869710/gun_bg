package com.auto.di.guan.jobqueue.task;

import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.TaskManger;
import com.auto.di.guan.utils.LogUtils;

import java.io.OutputStream;

/**
 *   绑定ID的任务
 */
public class BindIdTask extends BaseTask{
    private final String TAG = BASETAG+"BindIdTask";
    private String taskCmd;

    @Override
    public void startTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "写入id 开始=======  cmd =="+getTaskCmd());
        writeCmd(mOutputStream, getTaskCmd());
    }

    @Override
    public void errorTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "写入id 错误 ======="+"errorTask()");

    }

    @Override
    public void endTask(String receive, OutputStream mOutputStream) {
        LogUtils.e(TAG, "写入id 结束 ======="+"endTask()"+"====收到信息 =="+receive+ " receive.length() = "+receive.length());
        if (receive.toLowerCase().contains("ok") && receive.trim().length() == 2) {
            if (getTaskType() == TaskEntiy.TASK_TYPE_GID) {
                LogUtils.e(TAG, "写入项目gid 正常 =======");
                TaskManger.getInstance().doNextTask();
            }else if (getTaskType() == TaskEntiy.TASK_TYPE_BID) {
                LogUtils.e(TAG, "写入项目bid 正常 =======");
                TaskManger.getInstance().doNextTask();
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
