package com.auto.di.guan.jobqueue.task;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.jobqueue.event.Fragment31Event;
import com.auto.di.guan.jobqueue.event.Fragment4Event;
import com.auto.di.guan.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 *    分组操作完成的标志位
 */
public class GroupEndTask extends BaseTask{
    private final String TAG = BASETAG+"ReadGroupTask";

   private GroupInfo groupInfo;

    public GroupEndTask(int taskType, String taskCmd) {
        super(taskType, taskCmd);
    }

    public GroupEndTask(int taskType, String taskCmd, ControlInfo taskInfo) {
        super(taskType, taskCmd, taskInfo);
    }

    public GroupEndTask(int taskType, String taskCmd, GroupInfo groupInfo) {
        super(taskType, taskCmd);
        this.groupInfo = groupInfo;
    }

    @Override
    public void startTask() {
        LogUtils.e(TAG, "读取状态 开始=======  cmd =="+getTaskCmd());
        // 如果是这个任务说明开启任务已经全部完成
        if (getTaskType() == TaskEntiy.TASK_OPTION_GROUP_OPEN_READ_END) {
            LogUtils.e(TAG, "分组开启操作结束==========================  cmd =="+getTaskCmd());
            openGroupStatus();

        }else if (getTaskType() == TaskEntiy.TASK_OPTION_GROUP_CLOSE_READ_END) {
            LogUtils.e(TAG, "分组关闭操作结束==========================  cmd =="+getTaskCmd());
            closeGroupStatus();
        }
    }

    /**
     *   执行完操作更新组的状态
     */
    public void openGroupStatus(){
       GroupInfo groupInfo = getGroupInfo();
       if (groupInfo != null) {
           groupInfo.setGroupTime(0);
           groupInfo.setGroupRunTime(0);
           groupInfo.setGroupStatus(Entiy.GROUP_STATUS_OPEN);
           GroupInfoSql.updateGroup(groupInfo);
           LogUtils.e(TAG, "====================更新开启组的状态成功  openGroupStatus");
       }else {
           LogUtils.e(TAG, "====================更新开启组的状态失败");
       }
        LogUtils.e(TAG, "===================更新Fragment31");
        EventBus.getDefault().post(new Fragment31Event());
        finishTask();
    }

    /**
     *   执行完操作更新组的状态
     */
    public void closeGroupStatus(){
        GroupInfo groupInfo = getGroupInfo();
        if (groupInfo != null) {
            groupInfo.setGroupTime(0);
            groupInfo.setGroupRunTime(0);
            groupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
            GroupInfoSql.updateGroup(groupInfo);
            LogUtils.e(TAG, "===================更新开启组的状态成功   CloseGroupStatus()");
        }else {
            LogUtils.e(TAG, "===================更新开启组的状态失败");
        }
        LogUtils.e(TAG, "===================更新Fragment31");
        EventBus.getDefault().post(new Fragment31Event());
        finishTask();
    }

    @Override
    public void errorTask() {

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
