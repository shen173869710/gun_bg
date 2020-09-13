package com.auto.di.guan.utils;

import android.content.Context;

/**
 * Created by Administrator on 2018/3/21 0021.
 */

public class PollingUtils {

    private static String TAG = PollingUtils.class.getSimpleName();
    //开启轮询服务
    private static boolean isStart;
    public static boolean isRun;

    public static void startPollingService(Context context, int seconds) {
        //获取AlarmManager系统服务
//        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        //包装需要执行Service的Intent
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        intent.setAction(Entiy.ALERM_ACTION);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
//        //触发服务的起始时间
//        long triggerAtTime = SystemClock.elapsedRealtime();
//        //使用AlarmManger的setRepeating方法设置定期执行的时间间隔（seconds秒）和需要执行的Service
////        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, pendingIntent);
//        manager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), seconds, pendingIntent);
        isStart = true;
        LogUtils.e(TAG, "---------------启动自动轮灌查询--------------------");
    }

    //停止轮询服务
    public static void stopPollingService(Context context) {
//        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Intent intent = new Intent(context, AlarmReceiver.class);
//        intent.setAction(Entiy.ALERM_ACTION);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        //取消正在执行的服务
//        manager.cancel(pendingIntent);
        isStart = false;
        isRun = false;
        LogUtils.e(TAG, "----------------------关闭自动轮灌查询------------------------");
    }

    public static boolean isIsStart() {
        return isStart;
    }

    public static void setIsStart(boolean isStart) {
        PollingUtils.isStart = isStart;
    }
}
