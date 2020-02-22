package com.auto.di.guan.jobqueue;

import com.auto.di.guan.jobqueue.task.BaseTask;
import com.auto.di.guan.utils.LogUtils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskManger {
    public static BlockingQueue <BaseTask>queue = new LinkedBlockingQueue(200);

    private BaseTask mTask;

    private OutputStream  outputStream = null;

    private static TaskManger mTaskManger = null;
    public static TaskManger getInstance(){
        if (mTaskManger==null){
            mTaskManger=new TaskManger();
        }
        return mTaskManger;
    }

    public void init(OutputStream mOutputStream) {
        setOutputStream(mOutputStream);
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
        getmTask().startTask(outputStream);
    }

    /**
     *  添加任务
     */
    public  void addTask(BaseTask task) {
        queue.offer(task);
    }


    /**
     *  添加任务
     */
    public  void addTask(ArrayList<BaseTask> tasks) {
        if (tasks == null) {
            return;
        }
        int size = tasks.size();
        for (int i = 0; i < size; i++) {
            queue.offer(tasks.get(i));
        }

    }
    /**
     *        收到信息
     * @param receive
     */
    public void endTask(String receive) {
        BaseTask task = getmTask();
        if (task != null) {
            task.endTask(receive,getOutputStream());
        }
    }
    /**
     *  执行下一个任务
     */
    public  void doNextTask() {
        LogUtils.e("BaseTask == ", "doNextTask() 执行下一个");
        startTask();
    }

    public BaseTask getmTask() {
        return mTask;
    }

    public void setmTask(BaseTask mTask) {
        this.mTask = mTask;
    }

    public OutputStream getOutputStream() {
        return outputStream;
    }

    public void setOutputStream(OutputStream outputStream) {
        this.outputStream = outputStream;
    }
}
