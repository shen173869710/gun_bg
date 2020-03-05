package com.auto.di.guan.jobqueue.task;
import com.auto.di.guan.BaseApp;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

/**
 *   创建操作任务
 */
public class TaskFactory {

    public static final String TAG = "TaskFactory";
    /**
     *  创建写入和读取gid  task
     */
    public static void createGidTak(){
        TaskManager.getInstance().addTask(new BindIdTask(TaskEntiy.TASK_TYPE_GID,Entiy.writeGid(BaseApp.getProjectId())));
        TaskManager.getInstance().addTask(new ReadIdTask(TaskEntiy.TASK_READ_GID,"rgid"));
        TaskManager.getInstance().startTask();
    }

    /**
     *  创建写入和读取gid  task
     */
    public static void createBidTak(String bid){
        TaskManager.getInstance().addTask(new BindIdTask(TaskEntiy.TASK_TYPE_BID,Entiy.writeBid(bid)));
        TaskManager.getInstance().addTask(new ReadIdTask(TaskEntiy.TASK_READ_BID,"rbid"));
        TaskManager.getInstance().startTask();
    }

    /**
     *        创建开阀操作
     * @param info
     */
    public static void createOpenTask(ControlInfo info) {
        final String cmd = Entiy.cmdOpen(BaseApp.getProjectId(), info.getDeviceProtocalId(), info.getProtocalId());
        TaskManager.getInstance().addTask(new OpenTask(TaskEntiy.TASK_OPTION_OPEN, cmd,info));
    }

    /**
     *        创建关阀操作
     * @param info
     */
    public static void createCloseTask(ControlInfo info) {
        final String cmd = Entiy.cmdClose(BaseApp.getProjectId(), info.getDeviceProtocalId(), info.getProtocalId());
        TaskManager.getInstance().addTask(new CloseTask(TaskEntiy.TASK_OPTION_ClOSE, cmd,info));
    }

    /**
     *        创建开阀读取状态操作
     * @param info
     */
    public static void createReadTask(ControlInfo info,int type) {
        final String cmd = Entiy.cmdRead(BaseApp.getProjectId(), info.getDeviceProtocalId());
        TaskManager.getInstance().addTask(new ReadTask(type, cmd,info));
    }

    /**
     *        创建读取结束标志位
     */
    public static void createReadEndTask() {
        TaskManager.getInstance().addTask(new SingleEndTask(0, ""));
    }

    /**
     *
     * @param groupInfo 组信息
     * @param type      类型
     */
    public static void createGroupReadEndTask(int type,GroupInfo groupInfo) {
        TaskManager.getInstance().addTask(new GroupEndTask(type, "",groupInfo));
    }


    /**
     *   创建手动分组轮灌
     *   1. 获取所有组的设备
     *   2. 添加开启taks
     *   3. 添加读取taks
     *   4. 如果有别的组开启
     *   5. 添加关闭task
     *   6. 添加读取task
     */

    public static void createGroupOpenTask(GroupInfo groupInfo) {
        // 获取正在开启的组，如果有多组就不能执行
        List<GroupInfo> groupInfos = GroupInfoSql.queryOpenGrouplList();
        GroupInfo closeGroupInfo = null;
        int closeGroupId = -1;
        if (groupInfos != null) {
            if(groupInfos.size() > 1) {
                LogUtils.e(TAG,"当前有多组处于运行状态 异常");
                return;
            }
            if(groupInfos.size() == 1) {
                closeGroupInfo = groupInfos.get(0);
                closeGroupId = closeGroupInfo.getGroupId();
            }
        }

        LogUtils.e(TAG, "********************单组操作开始****************");
        //  1. 获取所有组的设备
        ArrayList<ControlInfo> openList = new ArrayList<>();
        ArrayList<ControlInfo> closeList = new ArrayList<>();
        List<DeviceInfo> deveiceInfos = DeviceInfoSql.queryDeviceList();
        int openGroupId = groupInfo.groupId;
        int size = deveiceInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo controlInfo0 = deveiceInfos.get(i).getValveDeviceSwitchList().get(0);
            ControlInfo controlInfo1 = deveiceInfos.get(i).getValveDeviceSwitchList().get(1);
            if (controlInfo0.getValve_group_id() == openGroupId) {
                openList.add(controlInfo0);
            }
            if (controlInfo1.getValve_group_id() == openGroupId) {
                openList.add(controlInfo1);
            }

            if (closeGroupId > 0) {
                if (controlInfo0.getValve_group_id() == closeGroupId) {
                    closeList.add(controlInfo0);
                }
                if (controlInfo1.getValve_group_id() == closeGroupId) {
                    closeList.add(controlInfo1);
                }
            }
        }

        if (openList.size() == 0) {
            LogUtils.e(TAG,"当前分组没有设备");
            return;
        }
        // 2. 添加开启task
        addOpenGroupTask(openList,true);
        // 3. 添加开启状态查询task
        addReadGroupTask(openList,TaskEntiy.TASK_OPTION_OPEN_READ);
        // 4. 添加开启结束标志位
        createGroupReadEndTask(TaskEntiy.TASK_OPTION_GROUP_OPEN_READ_END,groupInfo);

        if (closeGroupId > 0 && closeList.size() > 0) {

            LogUtils.e(TAG, "********************单组操作有需要关闭的组****************");
            // 1 添加关闭其他组task
            addOpenGroupTask(closeList,false);
            // 1 添加关闭状态查询task
            addReadGroupTask(closeList,TaskEntiy.TASK_OPTION_CLOSE_READ);
            // 4. 添加关闭结束标志位
            createGroupReadEndTask(TaskEntiy.TASK_OPTION_GROUP_CLOSE_READ_END,closeGroupInfo);
        }
        // 添加完成之后启动任务
        TaskManager.getInstance().startTask();
    }

    /**
     *        添加开启的设备task
     * @param controlInfos
     */
    private static void addOpenGroupTask(List<ControlInfo> controlInfos, boolean isOpen) {
        int size = controlInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo info = controlInfos.get(i);
            if (isOpen) {
                createOpenTask(info);
            }else {
                createCloseTask(info);
            }
        }
    }

    /**
     *        添加读取状态task
     * @param controlInfos
     */
    private static void addReadGroupTask(List<ControlInfo> controlInfos,int type) {
        int size = controlInfos.size();
        for (int i = 0; i < size; i++) {
            createReadTask(controlInfos.get(i),type);
        }
    }

    /**
     * . 1.添加关闭task
     *   2.添加读取task
     *   3.添加关闭标志位
     * @param groupInfo
     */
    public static void createGroupCloseTask(GroupInfo groupInfo){
        ArrayList<ControlInfo> closeList = new ArrayList<>();
        List<DeviceInfo> deveiceInfos = DeviceInfoSql.queryDeviceList();
        int goupId = groupInfo.groupId;
        int size = deveiceInfos.size();
        for (int i = 0; i < size; i++) {
            ControlInfo controlInfo0 = deveiceInfos.get(i).getValveDeviceSwitchList().get(0);
            ControlInfo controlInfo1 = deveiceInfos.get(i).getValveDeviceSwitchList().get(1);
            if (controlInfo0.getValve_group_id() == goupId) {
                closeList.add(controlInfo0);
            }
            if (controlInfo1.getValve_group_id() == goupId) {
                closeList.add(controlInfo1);
            }
        }
        addOpenGroupTask(closeList,false);
        // 1 添加关闭状态查询task
        addReadGroupTask(closeList,TaskEntiy.TASK_OPTION_CLOSE_READ);
        // 4. 添加关闭结束标志位
        createGroupReadEndTask(TaskEntiy.TASK_OPTION_GROUP_CLOSE_READ_END,groupInfo);
        // 添加完成之后启动任务
        TaskManager.getInstance().startTask();
    }

    /**
     *   自动轮灌开启
     *
     *
     */
    public static  void createAutoGroupOpenTask() {
        List<GroupInfo> groupInfos = GroupInfoSql.queryGrouplList();
    }


    /**
     *   自动轮灌关闭
     *
     *
     */
    public static  void createAutoGroupCloseTask() {
        List<GroupInfo> groupInfos = GroupInfoSql.queryGrouplList();
    }

    /**
     *   自动轮灌暂停
     */
    public static  void createAutoGroupPauseTask(GroupInfo groupInfo) {
        List<GroupInfo> groupInfos = GroupInfoSql.queryGrouplList();
    }

    /**
     *   自动轮灌开启下一组
     */
    public static  void createAutoGroupNextTask(GroupInfo groupInfo) {
        List<GroupInfo> groupInfos = GroupInfoSql.queryGrouplList();
    }
}
