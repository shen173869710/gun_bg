package com.auto.di.guan.jobqueue.task;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.event.Fragment31Event;
import com.auto.di.guan.jobqueue.event.Fragment32Event;
import com.auto.di.guan.utils.LogUtils;
import org.greenrobot.eventbus.EventBus;

/**
 *    分组操作完成的标志位
 */
public class GroupEndTask extends BaseTask{
    private final String TAG = BASETAG+"GroupEndTask";
    private GroupInfo mGroupInfo;

    public GroupEndTask(int taskType, String taskCmd, GroupInfo groupInfo) {
        super(taskType, taskCmd);
        mGroupInfo = groupInfo;
    }

    @Override
    public void startTask() {
        LogUtils.e(TAG, "手动操作开始任务 ===============================  cmd =="+getTaskCmd());
        // 如果是这个任务说明开启任务已经全部完成
        if (getTaskType() == TaskEntiy.TASK_OPTION_GROUP_OPEN_READ_END) {
            if (mGroupInfo !=  null) {
                mGroupInfo.setGroupStatus(1);
                GroupInfoSql.updateGroup(mGroupInfo);
            }
            LogUtils.e(TAG, "分组手动开启     操作结束==========================  cmd =="+getTaskCmd());
        }else if (getTaskType() == TaskEntiy.TASK_OPTION_GROUP_CLOSE_READ_END) {
            if (mGroupInfo != null) {
                mGroupInfo.setGroupStatus(0);
                mGroupInfo.setGroupTime(0);
                mGroupInfo.setGroupRunTime(0);
                GroupInfoSql.updateGroup(mGroupInfo);
            }
            LogUtils.e(TAG, "分组手动关闭     操作结束==========================  cmd =="+getTaskCmd());
        }
        EventBus.getDefault().post(new Fragment31Event());
        EventBus.getDefault().post(new Fragment32Event());
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
