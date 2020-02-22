package com.auto.di.guan.jobqueue.task;

import android.text.TextUtils;
import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.OptionStatus;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.TaskManger;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.OptionUtils;
import com.auto.di.guan.utils.SendUtils;
import com.google.gson.Gson;
import java.io.OutputStream;

/**
 *   读取设备的状态
 */
public class ReadTask extends BaseTask{
    private final String TAG = BASETAG+"ReadTask";
    private String taskCmd;

    @Override
    public void startTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "读取状态 开始=======  cmd =="+getTaskCmd());

        SendUtils.sendRead(getTaskCmd(), getTaskInfo());
        writeCmd(mOutputStream, getTaskCmd());
    }

    @Override
    public void errorTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "读取状态 错误 ======="+"errorTask()");
        TaskManger.getInstance().doNextTask();
    }

    /**
     *  rs 012 004 0 01
     *  zt 012 004 xxxx
     * @param receive
     * @param mOutputStream
     */
    @Override
    public void endTask(String receive, OutputStream mOutputStream) {
        LogUtils.e(TAG, "读取状态 结束 ======="+"endTask()"+"====收到信息 =="+receive);
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
            retryTask(mOutputStream);
        }else {
            /**
             *    解析返回的数据
             */
            OptionStatus status = OptionUtils.receive(receive);
            //  通信成功上报数据
            //  SendUtils.sendMiddle(receive, cur.getValve_id(),cur.getValve_alias());
            // 解析失败
            if(status == null) {
                retryTask(mOutputStream);
            }else {
                /**
                 *  解析数据的状态
                 */
                doReadStatus(status,mOutputStream);
            }
        }
    }

    @Override
    public void retryTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "读取状态 重试======="+"retryTask()  cmd =="+getTaskCmd()+ " 当前重试次数 = "+getTaskCount());
        if(getTaskCount() == 2) {
            setTaskCount(1);
            writeCmd(mOutputStream, getTaskCmd());
        }else {
            errorTask(mOutputStream);
        }

    }

    public String getTaskCmd() {
        return taskCmd;
    }

    public void setTaskCmd(String taskCmd) {
        this.taskCmd = taskCmd;
    }


    public void doReadStatus(OptionStatus status,OutputStream mOutputStream) {
        //status = {"allCmd":"zt 102 002 1100 090\n\u0000","code":"1100","deviceId":"002","elect":"090","projectId":"102","type":"zt","status":0}
        LogUtils.e(TAG, "读取状态 ======="+"doReadStatus == " +(new Gson().toJson(status)));
        try {
            DeviceInfo info = OptionUtils.changeStatus(status);
            if (info == null) {
                retryTask(mOutputStream);
                return;
            }
            ControlInfo controlInfo = getTaskInfo();
            final DeviceInfo deviceInfo = DeviceInfoSql.queryDeviceById(Integer.valueOf(controlInfo.getDevice_id()));
            if (TextUtils.isEmpty(status.elect)) {
                try {
                    deviceInfo.setElectricQuantity(Integer.valueOf(status.elect));
                }catch (Exception e) {

                }
            }
            if (controlInfo.getProtocalId().contains("0")) {
                doCompleControl(controlInfo, deviceInfo.getValveDeviceSwitchList().get(0));
            }else if (controlInfo.getProtocalId().contains("1")) {
                doCompleControl(controlInfo, deviceInfo.getValveDeviceSwitchList().get(1));
            }
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
        }
    }

    /**
     * @param info        当前操作的     info
     * @param info  根据返回的数据创建的  statusInfo
     */
    public int doCompleControl(ControlInfo info, ControlInfo statusInfo) {
        int type = -1;
        int code = statusInfo.getValve_status();
        int status = getTaskType();
        /**
         *   如果是开启状态查询
         */
        if (status == TaskEntiy.TASK_READ_OPEN_STATUS) {
            switch (code) {
                case Entiy.CONTROL_STATUS＿CONNECT:
                    //   设备处于链接状态, 说明打开开关失败
                    info.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                    info.setValve_imgage_id(R.mipmap.lighe_3);
                    break;
                case Entiy.CONTROL_STATUS＿RUN:
                    //   如果设备处于运行状态  说明状态正常
                    info.setValve_status(Entiy.CONTROL_STATUS＿RUN);
                    info.setValve_imgage_id(R.mipmap.lighe_2);
                    break;
                case Entiy.CONTROL_STATUS＿NOTCLOSE:
                    //   如果设备处于运行状态  说明设备无法打开
                    info.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                    info.setValve_imgage_id(R.mipmap.lighe_3);
                    break;
                 default:
                     //   其他异常
                    info.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                    info.setValve_imgage_id(R.mipmap.lighe_3);
                    break;
            }
        }else if (status == TaskEntiy.TASK_READ_ClOSE_STATUS) {
            switch (code) {
                case Entiy.CONTROL_STATUS＿CONNECT:
                    //   设备处于链接状态, 说明关闭成功
                    info.setValve_status(Entiy.CONTROL_STATUS＿CONNECT);
                    info.setValve_imgage_id(R.mipmap.lighe_1);
                    break;
                case Entiy.CONTROL_STATUS＿RUN:
                    //   如果设备处于运行状态  关闭失败
                    info.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                    info.setValve_imgage_id(R.mipmap.lighe_3);
                    break;
                case Entiy.CONTROL_STATUS＿NOTCLOSE:
                    //   如果设备处于其他状态, 说明设备无法关闭
                    info.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                    info.setValve_imgage_id(R.mipmap.lighe_3);
                    break;
                default:
                    //   其他异常
                    info.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                    info.setValve_imgage_id(R.mipmap.lighe_3);
                    break;
            }
        }
        return type;
    }

}
