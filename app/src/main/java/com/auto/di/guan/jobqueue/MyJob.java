package com.auto.di.guan.jobqueue;

import android.os.SystemClock;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.auto.di.guan.utils.LogUtils;
import com.birbit.android.jobqueue.Job;
import com.birbit.android.jobqueue.Params;
import com.birbit.android.jobqueue.RetryConstraint;

public class MyJob extends Job {

    public String TAG = "MyJob";
    public String tag;


    /**
     * 默认构造器传入的是int(priority)参数是该任务的优先级，优先级越高，越优先执行。
     *<p>
     * requireNetwork(): 设置该任务要求访问网络；
     *<p>
     * groupBy(String groupId)：设置组ID，被设置相同组ID的任务，将会按照顺序执行；
     *<p>
     * persist()：设置任务为可持久化的，持久化要求Job类为序列化的，这一点并不意外，
     * 因为一个类的内容只有序列化之后才能变成字节模式保存在硬盘上；
     * <p>
     *delayInMs(long delayMs)：设置延迟时间，ms为单位，在该时间之后再放入任务队列中。
     * <p>
     * addTags :添加标记取消任务时使用
     */
    protected MyJob(int i) {
        super(new Params(i));
        LogUtils.e(TAG, "MyJob" + "初始化");
    }

    @Override
    public void onAdded() {
        LogUtils.e(TAG, "onAdded()");
    }

    @Override
    public void onRun() throws Throwable {

        LogUtils.e(TAG, "====>onRun==任务开始执行");
        SystemClock.sleep(15000);

        //此处抛出异常后，jobqueue将立即自动进入shouldReRunOnThrowable
    }

    @Override
    protected void onCancel(int cancelReason, @Nullable Throwable throwable) {
        LogUtils.e(TAG, "onCancel    "+cancelReason);
    }


    //如果重试超过限定次数，将onCancel.
    //如果用户主动放弃删掉这个任务，也一样进入onCancel
    @Override
    protected RetryConstraint shouldReRunOnThrowable(@NonNull Throwable throwable, int runCount, int maxRunCount) {
        LogUtils.e(TAG, "runCount   = "+runCount + "   maxRunCount"+maxRunCount);
        return RetryConstraint.RETRY;
    }


    @Override
    protected int getRetryLimit() {
        return 1;
    }
}
