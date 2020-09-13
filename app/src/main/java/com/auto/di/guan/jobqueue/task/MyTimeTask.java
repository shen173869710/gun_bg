package com.auto.di.guan.jobqueue.task;

import com.auto.di.guan.utils.LogUtils;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimeTask {

    private final String TAG = "MyTimeTask";
    private Timer timer;
    private TimerTask task;
    private BaseTask baseTask;
    public MyTimeTask(BaseTask baseTask) {
        this.baseTask = baseTask;
        this.baseTask.setTaskLife(30);
        stop();
        if (timer == null){
            timer = new Timer();
        }

        if (task == null) {
            task = new TimerTask() {
                @Override
                public void run() {
                    int life = baseTask.getTaskLife();
                    if (life == 0) {
                        LogUtils.e(TAG, "########当前生命周期结束  执行下一个任务 == "+life);
                        stop();
                        // 有问题
                        baseTask.errorTask();
                    }else {
                        life = life - 1;
                        baseTask.setTaskLife(life);
                        LogUtils.e(TAG, "当前生命周期 =="+baseTask.getTaskLife());
                    }
                }
            };
        }else {
            task.cancel();
        }
    }

    public void start(){
        timer.schedule(task, 0, 1000);//每隔time时间段就执行一次
    }

    public void stop(){

        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }
}
