package com.auto.di.guan.jobqueue.task;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.event.Fragment4Event;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.PollingUtils;

import org.greenrobot.eventbus.EventBus;

/**
 *    单个操作完成的标志位
 */
public class SingleEndTask extends BaseTask{
    private final String TAG = BASETAG+"SingleEndTask";
    public SingleEndTask(int taskType, String taskCmd) {
        super(taskType, taskCmd);
    }

    public SingleEndTask(int taskType, String taskCmd, ControlInfo taskInfo) {
        super(taskType, taskCmd, taskInfo);
    }

    @Override
    public void startTask() {
        LogUtils.e(TAG, "读取结束标志位===================更新fragment4");
        EventBus.getDefault().post(new Fragment4Event());
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (getTaskType() == TaskEntiy.TASK_POLL_END) {
            PollingUtils.isRun = false;
        }
        finishTask();
    }

    @Override
    public void errorTask() {
        finishTask();
    }

    @Override
    public void endTask(String receive) {

    }

    @Override
    public void retryTask() {

    }

}
