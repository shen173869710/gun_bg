package com.auto.di.guan.jobqueue.task;

import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.event.AutoCountEvent;
import com.auto.di.guan.jobqueue.event.AutoTaskEvent;
import com.auto.di.guan.jobqueue.event.GroupStatusEvent;
import com.auto.di.guan.utils.LogUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

/**
 *    分组操作完成的标志位
 */
public class GroupAutoEndTask extends BaseTask{
    private final String TAG = BASETAG+"GroupAutoEndTask";

    private GroupInfo groupInfo;

    public GroupAutoEndTask(int taskType,GroupInfo groupInfo) {
        super(taskType, "");
        this.groupInfo = groupInfo;
    }


    @Override
    public void startTask() {
        // 如果是这个任务说明开启任务已经全部完成
        if (getTaskType() == TaskEntiy.TASK_OPTION_AUTO_OPEN) {
            LogUtils.e(TAG, "**********************************自动轮灌  开启一组完成****************************\n"+(new Gson().toJson(getGroupInfo())));

            EventBus.getDefault().post(new AutoTaskEvent(getGroupInfo()));
            EventBus.getDefault().post(new GroupStatusEvent(getGroupInfo()));
        }else if (getTaskType() == TaskEntiy.TASK_OPTION_AUTO_CLOSE) {
            LogUtils.e(TAG, "**********************************自动轮灌  关闭一组完成****************************\n"+(new Gson().toJson(getGroupInfo())));
            EventBus.getDefault().post(new AutoCountEvent(getGroupInfo()));
            EventBus.getDefault().post(new GroupStatusEvent(getGroupInfo()));
        }else if (getTaskType() == TaskEntiy.TASK_OPTION_AUTO_NEXT) {
            LogUtils.e(TAG, "**********************************自动轮灌  开启一组完成  开始执行下一组轮灌****************************\n"+(new Gson().toJson(getGroupInfo())));
            GroupInfo nextInfo = GroupInfoSql.queryNextGroup();
            if (nextInfo != null) {
                EventBus.getDefault().post(new AutoTaskEvent(nextInfo));
            }
            EventBus.getDefault().post(new GroupStatusEvent(getGroupInfo()));
            finishTask();
        }

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

    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
}
