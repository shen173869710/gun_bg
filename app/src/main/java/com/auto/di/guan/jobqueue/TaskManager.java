package com.auto.di.guan.jobqueue;

import android.text.TextUtils;

import com.auto.di.guan.jobqueue.task.BaseTask;
import com.auto.di.guan.jobqueue.task.MyTimeTask;
import com.auto.di.guan.utils.LogUtils;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskManager {
    public static BlockingQueue <BaseTask>queue = new LinkedBlockingQueue(200);
    private BaseTask mTask;
    private MyTimeTask myTimeTask;

    private static TaskManager mTaskManager = null;
    public static TaskManager getInstance(){
        if (mTaskManager ==null){
            mTaskManager =new TaskManager();
        }
        return mTaskManager;
    }

    /**
     *   开始执行任务
     */
    public void startTask(){
        if (myTimeTask != null) {
            myTimeTask.stop();
            myTimeTask = null;
        }
        LogUtils.e("BaseTask == ", "队列大小   =  "+queue.size());
        BaseTask task = queue.poll();
        if (task == null ) {
            LogUtils.e("BaseTask == ", "队列为空，任务结束");
            return;
        } else {
            setmTask(task);
            getmTask().startTask();
            LogUtils.e("BaseTask == ", "task ="+task + "   getmTask()=" +getmTask());
            if ( getmTask() != null && !TextUtils.isEmpty(getmTask().getTaskCmd())) {
                myTimeTask = new MyTimeTask(getmTask());
                myTimeTask.start();
            }
            LogUtils.e("BaseTask == ", "队列有数据开始任务  type = "+getmTask().getTaskType()+ "  cmd = " + task);
        }
    }

    /**
     *  添加任务
     */
    public  void addTask(BaseTask task) {

        LogUtils.e("BaseTask == ", "添加任务");
        queue.offer(task);
    }

    /**
     *        收到信息
     * @param receive
     */
    public void endTask(String receive) {
        BaseTask task = getmTask();
        if (task != null) {
            task.endTask(receive);
        }
    }
    /**
     *  执行下一个任务
     */
    public  void doNextTask() {
        LogUtils.e("BaseTask == ", "doNextTask() 执行下一个");
        startTask();
    }

    public boolean hasTask(){
        if (queue.isEmpty()) {
            return false;
        }else {
            return true;
        }
    }

    public BaseTask getmTask() {
        return mTask;
    }

    public void setmTask(BaseTask task) {
        this.mTask = task;
    }

    public void finishTask() {
        LogUtils.e("BaseTask == ", "finishTask() 清空当前task");
        setmTask(null);
        if (myTimeTask != null) {
            myTimeTask.stop();
            myTimeTask = null;
        }
    }
}
