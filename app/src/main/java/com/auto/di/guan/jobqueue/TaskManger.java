package com.auto.di.guan.jobqueue;

import com.auto.di.guan.jobqueue.task.BaseTask;
import com.auto.di.guan.utils.LogUtils;

import java.io.OutputStream;
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
    public void startTask(OutputStream mOutputStream){
        if (queue.isEmpty()) {
            LogUtils.e("BaseTask == ", "队列为空，任务结束");
            return;
        }
        setmTask(queue.poll());
        getmTask().startTask(mOutputStream);
    }

    /**
     *  添加任务
     */
    public  void addTask(BaseTask task) {
        queue.offer(task);
    }
    /**
     *        收到信息
     * @param receive
     */
    public void endTask(String receive,OutputStream mOutputStream) {
        BaseTask task = getmTask();
        if (task != null) {
            task.endTask(receive,mOutputStream);
        }
    }
    /**
     *  执行下一个任务
     */
    public  void doNextTask(OutputStream mOutputStream) {
        startTask(mOutputStream);
    }

    public BaseTask getmTask() {
        return mTask;
    }

    public void setmTask(BaseTask mTask) {
        this.mTask = mTask;
    }
}
