package com.auto.di.guan.jobqueue.task;

import android.text.TextUtils;

import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.OptionStatus;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.TaskManger;
import com.auto.di.guan.jobqueue.event.VideoPlayEcent;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.OptionUtils;
import com.auto.di.guan.utils.SendUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.OutputStream;

/**
 *   读取设备的状态
 *   rs 012 002 0 01
 *   zt 102 002 1100 090
 */
public class ReadGroupTask extends BaseTask{
    private final String TAG = BASETAG+"ReadGroupTask";

   private GroupInfo groupInfo;

    public ReadGroupTask(int taskType, String taskCmd) {
        super(taskType, taskCmd);
    }

    public ReadGroupTask(int taskType, String taskCmd, ControlInfo taskInfo) {
        super(taskType, taskCmd, taskInfo);
    }

    public ReadGroupTask(int taskType, String taskCmd, GroupInfo groupInfo) {
        super(taskType, taskCmd);
        this.groupInfo = groupInfo;
    }

    @Override
    public void startTask() {
        LogUtils.e(TAG, "读取状态 开始=======  cmd =="+getTaskCmd());
        // 如果是这个任务说明开启任务已经全部完成
        if (getTaskType() == TaskEntiy.TASK_OPTION_GROUP_OPEN_READ_END) {
            LogUtils.e(TAG, "分组开启操作结束=======  cmd =="+getTaskCmd());
            openGroupStatus();
            finishTask();
        }else if (getTaskType() == TaskEntiy.TASK_OPTION_GROUP_CLOSE_READ_END) {
            LogUtils.e(TAG, "分组关闭操作结束=======  cmd =="+getTaskCmd());
            closeGroupStatus();
            TaskManger.getInstance().doNextTask();
            finishTask();
        }else {
            SendUtils.sendReadStart(getTaskCmd(), getTaskInfo());
            writeCmd(getTaskCmd());
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
           LogUtils.e(TAG, "更新开启组的状态成功  openGroupStatus");
       }else {
           LogUtils.e(TAG, "更新开启组的状态失败");
       }

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
            LogUtils.e(TAG, "更新开启组的状态成功   CloseGroupStatus()");
        }else {
            LogUtils.e(TAG, "更新开启组的状态失败");
        }
    }

    @Override
    public void errorTask() {
        LogUtils.e(TAG, "读取状态 错误 ======="+"errorTask()");
        SendUtils.sendReadEnd(SendUtils.OPTION_READ_FAILE, getTaskInfo());
        finishTask();
    }

    /**
     *  rs 012 004 0 01
     *  zt 012 004 xxxx
     * @param receive
     */
    @Override
    public void endTask(String receive) {
        LogUtils.e(TAG, "读取状态 通信成功 ======="+"endTask()"+"====收到信息 =="+receive);
        /**
         *  未知的命令 如果count == 2 重试一次
         *                   如果count == 1 进入错误
         */
        if (!receive.toLowerCase().contains("zt") &&
                !receive.toLowerCase().contains("rs") &&
                !receive.toLowerCase().contains("gf")
                && !receive.toLowerCase().contains("kf")) {
            /**
             *  未知命令重试
             */
            retryTask();
        }else {
            /**
             *    解析返回的数据
             */
            OptionStatus status = OptionUtils.receive(receive);
            // 解析失败
            if(status == null) {
                retryTask();
            }else {
                //  发送通信成功
                SendUtils.sendReadMiddle(receive, getTaskInfo());
                // 解析通信成功的状态
                doReadStatus(receive,status);
                // 执行下一个任务
                finishTask();
            }
        }
    }

    @Override
    public void retryTask() {
        LogUtils.e(TAG, "读取状态 重试======="+"retryTask()  cmd =="+getTaskCmd()+ " 当前重试次数 = "+getTaskCount());
        if(getTaskCount() == 2) {
            setTaskCount(1);
            SendUtils.sendReadMiddle("读取异常\n "+getReceive(), getTaskInfo());
            writeCmd(getTaskCmd());
        }else {
            errorTask();
        }
    }


    public void doReadStatus(String receive,OptionStatus status) {
        //status = {"allCmd":"zt 102 002 1100 090\n\u0000","code":"1100","deviceId":"002","elect":"090","projectId":"102","type":"zt","status":0}
        LogUtils.e(TAG, "读取状态 ======="+"doReadStatus == " +(new Gson().toJson(status)));
            DeviceInfo info = OptionUtils.changeStatus(status);
            if (info == null) {
                retryTask();
                return;
            }
            ControlInfo controlInfo = getTaskInfo();
            if (controlInfo.getProtocalId().contains("0")) {
                doOptionControl(controlInfo, info.getValveDeviceSwitchList().get(0),0,status.elect);
            }else if (controlInfo.getProtocalId().contains("1")) {
                doOptionControl(controlInfo, info.getValveDeviceSwitchList().get(1),1,status.elect);
            }
    }

    /**
     *
     * @param taskInfo   当前执行任务的阀门
     * @param info       根据数据返回的烦闷
     * @param postion    当前阀门属于第一个 还是第二个  0 第一个  1第二个
     * @param elect      当前的电量
     */
    public void doOptionControl(ControlInfo taskInfo, ControlInfo info,int postion,String elect) {
        final DeviceInfo deviceInfo = DeviceInfoSql.queryDeviceById(taskInfo.getDevice_id());
        if (deviceInfo == null) {
            LogUtils.e(TAG, "---131---无法查询的设备id"+taskInfo.getDevice_id());
            return;
        }
        int type = -1;
        int code = info.getValve_status();
        int taskType = getTaskType();
        int valveStatus = 0;
        int imageId = 0;
        /**
         *   如果是开启状态查询
         */
        if (taskType == TaskEntiy.TASK_OPTION_OPEN_READ) {
            switch (code) {
                case Entiy.CONTROL_STATUS＿CONNECT:
                    //   设备处于链接状态, 说明打开开关失败
                    valveStatus = Entiy.CONTROL_STATUS＿ERROR;
                    imageId = R.mipmap.lighe_3;
                    type = SendUtils.OPTION_OPEN_FAILE;
                    break;
                case Entiy.CONTROL_STATUS＿RUN:
                    //   如果设备处于运行状态  说明状态正常
                    valveStatus = Entiy.CONTROL_STATUS＿RUN;
                    imageId = R.mipmap.lighe_2;
                    type = SendUtils.OPTION_OPEN_SUCESS;
                    break;
                case Entiy.CONTROL_STATUS＿NOTCLOSE:
                    //   如果设备处于运行状态  说明设备无法打开
                    valveStatus = Entiy.CONTROL_STATUS＿ERROR;
                    imageId = R.mipmap.lighe_3;
                    type = SendUtils.OPTION_OPEN_ERROR;
                    break;
                case Entiy.CONTROL_STATUS＿DISCONNECT:
                    //   阀门线未连接
                    valveStatus = Entiy.CONTROL_STATUS＿ERROR;
                    imageId = R.mipmap.lighe_3;
                    type = SendUtils.OPTION_OPEN_DIS;
                    break;
                 default:
                     //   其他异常
                     valveStatus = Entiy.CONTROL_STATUS＿ERROR;
                     imageId = R.mipmap.lighe_3;
                     type = SendUtils.OPTION_OPEN_OTHER;
                    break;
            }

            if(type != SendUtils.OPTION_OPEN_SUCESS) {
                // 开阀异常报警
                EventBus.getDefault().post(new VideoPlayEcent(Entiy.VIDEO_OPEN_ERROR));
            }
            /**
             *   如果是关闭状态查询
             */
        }else if (taskType == TaskEntiy.TASK_OPTION_CLOSE_READ) {
            switch (code) {
                case Entiy.CONTROL_STATUS＿CONNECT:
                    //   设备处于链接状态, 说明关闭成功
                    valveStatus = Entiy.CONTROL_STATUS＿CONNECT;
                    taskInfo.setValve_imgage_id(R.mipmap.lighe_1);
                    type = SendUtils.OPTION_CLOSE_SUCESS;
                    break;
                case Entiy.CONTROL_STATUS＿RUN:
                    //   如果设备处于运行状态  关闭失败
                    valveStatus = Entiy.CONTROL_STATUS＿ERROR;
                    imageId = R.mipmap.lighe_3;
                    type = SendUtils.OPTION_CLOSE_FAILE;
                    break;
                case Entiy.CONTROL_STATUS＿NOTCLOSE:
                    //   如果设备处于其他状态, 说明设备无法关闭
                    valveStatus = Entiy.CONTROL_STATUS＿ERROR;
                    imageId = R.mipmap.lighe_3;
                    type = SendUtils.OPTION_CLOSE_ERROR;
                    break;
                case Entiy.CONTROL_STATUS＿DISCONNECT:
                    //   阀门线未连接
                    valveStatus = Entiy.CONTROL_STATUS＿ERROR;
                    imageId = R.mipmap.lighe_3;
                    type = SendUtils.OPTION_CLOSE_DIS;
                    break;
                default:
                    //   其他异常
                    valveStatus = Entiy.CONTROL_STATUS＿ERROR;
                    imageId = R.mipmap.lighe_3;
                    type = SendUtils.OPTION_CLOSE_OTHER;
                    break;
            }

            if(type != SendUtils.OPTION_OPEN_SUCESS) {
                // 关阀异常报警
                EventBus.getDefault().post(new VideoPlayEcent(Entiy.VIDEO_CLOSE_ERROR));
            }
            /**
             *   如果是单独的查询
             */
        } else if (taskType == TaskEntiy.TASK_OPTION_READ) {
            if(taskInfo.getValve_status() != info.getValve_status() && taskInfo.getValve_status() != Entiy.CONTROL_STATUS＿ERROR) {
                // 关阀异常报警
                EventBus.getDefault().post(new VideoPlayEcent(Entiy.VIDEO_READ_ERROR));
                type = SendUtils.OPTION_READ_ERROR;
            }
            valveStatus = info.getValve_status();
            imageId = info.getValve_imgage_id();
        }

        ControlInfo controlInfo = null;
        if (postion == 0) {
            controlInfo = deviceInfo.getValveDeviceSwitchList().get(0);
        }else if (postion == 1) {
            controlInfo = deviceInfo.getValveDeviceSwitchList().get(1);
        }

        if (controlInfo == null) {
            LogUtils.e(TAG, "阀门设备异常 postion = "+postion);
            return;
        }

        controlInfo.setValve_status(valveStatus);
        controlInfo.setValve_imgage_id(imageId);
        if (TextUtils.isEmpty(elect)) {
            try {
                deviceInfo.setElectricQuantity(Integer.valueOf(elect));
            }catch (Exception e) {

            }
        }
        /**
         *  发送通信结束
         */
        SendUtils.sendReadEnd(type, taskInfo);
        /**
         *  更新设备信息
         */
        DeviceInfoSql.updateDevice(deviceInfo);

    }


    public GroupInfo getGroupInfo() {
        return groupInfo;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }
}
