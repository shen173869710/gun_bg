package com.auto.di.guan.jobqueue.task;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.TaskManger;
import com.auto.di.guan.jobqueue.event.BindSucessEvent;
import com.auto.di.guan.jobqueue.event.ReadIdEvent;
import com.auto.di.guan.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.OutputStream;

public class ReadIdTask extends BaseTask{
    private final String TAG = BASETAG+"ReadTask";
    private String taskCmd;

    @Override
    public void pushEvnt() {
        LogUtils.e(TAG, "发送事件ReadIdTask=======  pushEvnt ==");
        EventBus.getDefault().post(new ReadIdEvent(getTaskType(),getTaskCmd()));
    }

    @Override
    public void startTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "读取 id 开始=======  cmd =="+getTaskCmd());
        writeCmd(mOutputStream, getTaskCmd());
    }

    @Override
    public void errorTask(OutputStream mOutputStream) {
        LogUtils.e(TAG, "读取 id 错误 ======="+"errorTask()");
        TaskManger.getInstance().doNextTask(mOutputStream);
    }

    @Override
    public void endTask(String receive, OutputStream mOutputStream) {
        LogUtils.e(TAG, "读取 id 结束 ======="+"endTask()"+"====收到信息 =="+receive);
        if (receive.toLowerCase().contains("gid")) {
            LogUtils.e(TAG, "读取 gid 正常 =======");
            EventBus.getDefault().post(new BindSucessEvent(getTaskType()));
        }else if (receive.toLowerCase().contains("bid")) {
            LogUtils.e(TAG, "读取 bid 正常 =======");
            EventBus.getDefault().post(new BindSucessEvent(getTaskType()));
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
        LogUtils.e(TAG, "读取id 重试======="+"retryTask()  cmd =="+getTaskCmd());
        writeCmd(mOutputStream, getTaskCmd());
    }

    public String getTaskCmd() {
        return taskCmd;
    }

    public void setTaskCmd(String taskCmd) {
        this.taskCmd = taskCmd;
    }
}
