package com.auto.di.guan.jobqueue;

import com.auto.di.guan.jobqueue.task.BaseTask;
import com.auto.di.guan.utils.LogUtils;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskManger {
    public static BlockingQueue <BaseTask>queue = new LinkedBlockingQueue(200);
    private BaseTask mTask;

    private static TaskManger mTaskManger = null;
    public static TaskManger getInstance(){
        if (mTaskManger==null){
            mTaskManger=new TaskManger();
        }
        return mTaskManger;
    }

    /**
     *   开始执行任务
     */
    public void startTask(){
        if (queue.isEmpty()) {
            LogUtils.e("BaseTask == ", "队列为空，任务结束");
            return;
        }
        setmTask(queue.poll());
        getmTask().startTask();
        LogUtils.e("BaseTask == ", "队列有数据开始任务");
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
        setmTask(null);
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

    public void setmTask(BaseTask mTask) {
        this.mTask = mTask;
    }

}
