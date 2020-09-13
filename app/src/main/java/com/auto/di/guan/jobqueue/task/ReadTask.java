package com.auto.di.guan.jobqueue.task;
import android.text.TextUtils;
import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.OptionStatus;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.event.VideoPlayEcent;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.OptionUtils;
import com.auto.di.guan.utils.SendUtils;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
/**
 *   读取设备的状态
 *   rs 012 002 0 01
 *   zt 102 002 1100 090
 */
public class ReadTask extends BaseTask{
    private final String TAG = BASETAG+"ReadTask";

    private int actionType;


    public ReadTask(int taskType, String taskCmd) {
        super(taskType, taskCmd);
    }

    public ReadTask(int taskType, String taskCmd, ControlInfo taskInfo, int actionType) {
        super(taskType, taskCmd, taskInfo);
        this.actionType = actionType;
    }

    @Override
    public void startTask() {
        LogUtils.e(TAG, "读取状态 开始 =========================================  cmd =="+getTaskCmd());
        SendUtils.sendReadStart(getTaskCmd(), getTaskInfo());
        writeCmd(getTaskCmd());
    }

    @Override
    public void errorTask() {
        LogUtils.e(TAG, "读取状态 错误 ======="+"errorTask()");
        SendUtils.sendReadEnd(getTaskInfo(), getTaskType(),getActionType(),SendUtils.OPTION_READ_FAILE,true);
        EventBus.getDefault().post(new VideoPlayEcent(Entiy.VIDEO_CLOSE_ERROR));
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
        if (receive.equals("rs")) {
            LogUtils.e(TAG, "读取状态 通信成功 过滤回显");
            return;
        }
        setReceive(receive);
        if (!receive.toLowerCase().contains("zt")) {
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
                finishTask();
            }
        }
    }

    @Override
    public void retryTask() {
        LogUtils.e(TAG, "读取状态 重试======="+"retryTask()  cmd =="+getTaskCmd()+ " 当前重试次数 = "+getTaskCount());
        /**
         *  未知的命令 如果count == 2 重试一次
         *                   如果count == 1 进入错误
         */
        if(getTaskCount() == 2) {
            setTaskCount(1);
            SendUtils.sendReadTryMiddle(getReceive(), getTaskInfo());
            writeCmd(getTaskCmd());
        }else {
            errorTask();
        }
    }


    public void  doReadStatus(String receive,OptionStatus status) {
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
                    imageId = R.mipmap.lighe_1;
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

            if(type != SendUtils.OPTION_CLOSE_SUCESS) {
                // 关阀异常报警
                EventBus.getDefault().post(new VideoPlayEcent(Entiy.VIDEO_CLOSE_ERROR));
            }
            /**
             *   如果是单独的查询
             */
        } else if (taskType == TaskEntiy.TASK_OPTION_READ) {
            switch (code) {
                case Entiy.CONTROL_STATUS＿CONNECT:
                    //   设备处于链接状态
                    valveStatus = Entiy.CONTROL_STATUS＿CONNECT;
                    imageId = R.mipmap.lighe_1;
                    type = SendUtils.OPTION_READ_CONNECT;
                    break;
                case Entiy.CONTROL_STATUS＿RUN:
                    //   如果设备处于运行状态
                    valveStatus = Entiy.CONTROL_STATUS＿RUN;
                    imageId = R.mipmap.lighe_2;
                    type = SendUtils.OPTION_READ_RUN;
                    break;
                case Entiy.CONTROL_STATUS＿NOTCLOSE:
                    //  阀门无法关闭
                    valveStatus = Entiy.CONTROL_STATUS＿ERROR;
                    imageId = R.mipmap.lighe_3;
                    type = SendUtils.OPTION_READ_ERROR;
                    break;
                case Entiy.CONTROL_STATUS＿DISCONNECT:
                    //   阀门线未连接
                    valveStatus = Entiy.CONTROL_STATUS＿ERROR;
                    imageId = R.mipmap.lighe_3;
                    type = SendUtils.OPTION_READ_DIS;
                    break;
                default:
                    //   其他异常
                    valveStatus = Entiy.CONTROL_STATUS＿ERROR;
                    imageId = R.mipmap.lighe_3;
                    type = SendUtils.OPTION_READ_FAILE;
                    break;
            }
            /**
             *   如果设备不处于
             */
            if(type == SendUtils.OPTION_READ_ERROR
                    || type == SendUtils.OPTION_READ_DIS
                    || type == SendUtils.OPTION_READ_DIS) {
                // 异常报警
                EventBus.getDefault().post(new VideoPlayEcent(Entiy.VIDEO_CLOSE_ERROR));
            }
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
        if (!TextUtils.isEmpty(elect)) {
            try {
                deviceInfo.setElectricQuantity(Integer.valueOf(elect));
            }catch (Exception e) {

            }
        }
        /**
         *  更新设备信息
         */
        LogUtils.e(TAG, "更新设备信息"+(new Gson().toJson(deviceInfo)));
        DeviceInfoSql.updateDevice(deviceInfo);
        /**
         *  发送通信结束
         */
        SendUtils.sendReadEnd(getTaskInfo(), getTaskType(),getActionType(),type,true);
    }


    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }
}
