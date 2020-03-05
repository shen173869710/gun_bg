package com.auto.di.guan.jobqueue.task;

import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.utils.LogUtils;

/**
 *   绑定ID的任务
 */
public class BindIdTask extends BaseTask{
    private final String TAG = BASETAG+"BindIdTask";

    public BindIdTask(int taskType, String taskCmd) {
        super(taskType, taskCmd);
    }

    @Override
    public void startTask() {
        LogUtils.e(TAG, "写入id 开始=======  cmd =="+getTaskCmd());
        writeCmd(getTaskCmd());
    }

    @Override
    public void errorTask() {
        LogUtils.e(TAG, "写入id 错误 ======="+"errorTask()");

    }

    @Override
    public void endTask(String receive) {
        setReceive(receive);
        LogUtils.e(TAG, "写入id 结束 ======="+"endTask()"+"====收到信息 =="+receive+ " receive.length() = "+receive.length());
        if (receive.toLowerCase().contains("ok") && receive.trim().length() == 2) {
            if (getTaskType() == TaskEntiy.TASK_TYPE_GID) {
                LogUtils.e(TAG, "写入项目gid 正常 =======");
                finishTask();
            }else if (getTaskType() == TaskEntiy.TASK_TYPE_BID) {
                LogUtils.e(TAG, "写入项目bid 正常 =======");
                finishTask();
            }
        }else {
            if (getTaskCount() == 2) {
                setTaskCount(1);
                retryTask();
            }else {
                errorTask();
            }
        }
    }

    @Override
    public void retryTask() {
        LogUtils.e(TAG, "写入项目gid 重试======="+"retryTask()  cmd =="+getTaskCmd());
        writeCmd( getTaskCmd());
    }

}
