package com.auto.di.guan;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.LevelInfo;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.LevelInfoSql;
import com.auto.di.guan.entity.CmdStatus;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.jobqueue.event.AutoCountEvent;
import com.auto.di.guan.jobqueue.event.AutoTaskEvent;
import com.auto.di.guan.jobqueue.event.SendCmdEvent;
import com.auto.di.guan.jobqueue.event.VideoPlayEcent;
import com.auto.di.guan.jobqueue.task.TaskFactory;
import com.auto.di.guan.utils.FloatWindowUtil;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.PollingUtils;
import com.auto.di.guan.utils.ToastUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends SerialPortActivity {

    private final String TAG = "------" + MainActivity.class.getSimpleName();
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TextView textView;

    private List<GroupInfo> groupInfos;
    /**
     * 定时任务时间   你自己在这里修改
     * 5 分钟
     **/
    public static final int ALERM_TIME = 2 * 60 * 1000;
    private static final int HANDLER_WHAT_FALG = 1;
    private MediaPlayer mp;

    /**
     * 当前执行的操作
     **/
    public int CMD_TYPE;
    public static final int TYPE_READ = 0;
    public static final int TYPE_OPEN = 1;
    public static final int TYPE_CLOSE = 2;

    /**
     * 当前运行的剩余时间
     ***/
    public int curRunTime = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GroupInfo groupInfo = null;
            if (msg.obj != null) {
                groupInfo = (GroupInfo) msg.obj;
                groupInfo.setGroupRunTime(groupInfo.getGroupRunTime() + 1);
                curRunTime = groupInfo.getGroupTime() - groupInfo.getGroupRunTime();
                /**如果运行时间到呢,**/
                if (groupInfo.getGroupTime() <= groupInfo.getGroupRunTime()) {
                    /**
                     *   如果运行时间到呢, 就执行下一组
                     */
                    EventBus.getDefault().post(new AutoCountEvent(groupInfo));
                    TaskFactory.createAutoGroupNextTask(groupInfo);
                } else {
                    Message message = new Message();
                    message.obj = groupInfo;
                    message.what = 1;
                    sendMessageDelayed(message, Entiy.RUN_TIME_COUNT);
                    /**
                     *   每隔 1秒保存一次数据
                     */
                    GroupInfoSql.updateRunGroup(groupInfo);
                    EventBus.getDefault().post(new AutoCountEvent(groupInfo));
                }
            }
        }
    };

    public static int windowTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//		PollingUtils.startPollingService(this, ALERM_TIME);
        EventBus.getDefault().register(this);
        textView = (TextView) findViewById(R.id.title_bar_title);
        textView.setText(BaseApp.getUser().getProjectName());
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        final ArticleListFragment articleListFragment = new ArticleListFragment();
        transaction.add(R.id.center, articleListFragment, "center");
        transaction.commitAllowingStateLoss();
        windowTop = getStatusBarHeight();

        if (LevelInfoSql.queryLevelInfoList().size() == 0) {
            List<LevelInfo> levelInfos = new ArrayList<>();
            for (int i = 1; i < 200; i++) {
                LevelInfo info = new LevelInfo();
                info.setLevelId(i);
                info.setIsGroupUse(false);
                info.setIsLevelUse(false);
                levelInfos.add(info);
            }
            LevelInfoSql.insertLevelInfoList(levelInfos);
        }
    }


    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public void loginOut() {
        MainActivity.this.finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
    }

    public void setTitle(String title) {
        String mainTitle = BaseApp.getUser().getProjectName();
        if (mainTitle == null && TextUtils.isEmpty(mainTitle)) {
            mainTitle = "";
        } else {
            mainTitle = mainTitle + "-";
        }
        title = mainTitle + title;
        textView.setText(title);
    }


//    public void doNext(GroupInfo groupInfo) {
//        final List<ControlInfo> infos = new ArrayList<>();
//        List<DeviceInfo> deveiceInfo = DeviceInfoSql.queryDeviceList();
//        int size = deveiceInfo.size();
//
//        for (int i = 0; i < size; i++) {
//            if (groupInfo.getGroupId() == deveiceInfo.get(i).getValveDeviceSwitchList().get(0).getValve_group_id()) {
//                infos.add(deveiceInfo.get(i).getValveDeviceSwitchList().get(0));
//            }
//            if (groupInfo.getGroupId() == deveiceInfo.get(i).getValveDeviceSwitchList().get(1).getValve_group_id()) {
//                infos.add(deveiceInfo.get(i).getValveDeviceSwitchList().get(1));
//            }
//        }
//        groupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
//        GroupInfoSql.updateGroup(groupInfo);
//
//        Observable.interval(0, RXJAVA_TIME, TimeUnit.SECONDS)
//                .take(infos.size())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableObserver<Long>() {
//                    @Override
//                    public void onNext(Long value) {
//                        int count = value.intValue();
//                        closeCmd(infos.get(count));
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//
//        Observable.interval(infos.size() * RXJAVA_TIME, RXJAVA_TIME, TimeUnit.SECONDS)
//                .take(infos.size())
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new DisposableObserver<Long>() {
//                    @Override
//                    public void onNext(Long value) {
//                        int count = value.intValue();
//                        readCmd(infos.get(count), TYPE_CLOSE);
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        LogUtils.e(TAG, "---关闭完成----" + System.currentTimeMillis());
////                        handler.postDelayed(new Runnable() {
////                            @Override
////                            public void run() {
////
////                            }
////                        }, RXJAVA_TIME*1000);
//                    }
//                });
//    }

//    /**
//     * 关闭 或者开启设备
//     **/
//    private int doRun(boolean isSatrt, GroupInfo groupInfo) {
//        List<ControlInfo> infos = new ArrayList<>();
//        List<DeviceInfo> deveiceInfo = DeviceInfoSql.queryDeviceList();
//        int size = deveiceInfo.size();
//
//        for (int i = 0; i < size; i++) {
//            if (groupInfo.getGroupId() == deveiceInfo.get(i).getValveDeviceSwitchList().get(0).getValve_group_id()) {
//                infos.add(deveiceInfo.get(i).getValveDeviceSwitchList().get(0));
//            }
//            if (groupInfo.getGroupId() == deveiceInfo.get(i).getValveDeviceSwitchList().get(1).getValve_group_id()) {
//                infos.add(deveiceInfo.get(i).getValveDeviceSwitchList().get(1));
//            }
//        }
//        int groupStatus;
//        if (isSatrt) {
//            groupStatus = Entiy.GROUP_STATUS_OPEN;
//            optionContron(infos, TYPE_OPEN);
//        } else {
//            groupStatus = Entiy.GROUP_STATUS_COLSE;
//            optionContron(infos, TYPE_CLOSE);
//        }
//        groupInfo.setGroupStatus(groupStatus);
//        GroupInfoSql.updateGroup(groupInfo);
//        return infos.size();
//    }

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        final String receive = new String(buffer, 0, size);
        int length = receive.trim().length();
//        LogUtils.e(TAG, "收到 -------------------" + receive + "    length = " + length);

        if (TextUtils.isEmpty(receive)) {
            showToastLongMsg("错误命令" + receive);
            return;
        }
        if ((receive.contains("kf")
                || receive.contains("gf")
                || receive.contains("rs"))
                && !receive.contains("ok")) {
//            LogUtils.e(TAG, "过滤回显信息 -------------------"+receive);
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TaskManager.getInstance().endTask(receive);
            }
        });
    }

    /**
     * 显示字符串类型数据
     *
     * @param msg
     */
    public void showToastLongMsg(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }


    public void showDialog() {
        FloatWindowUtil.getInstance().show();
    }

//    /**
//     * 进行批量分组操作
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(final MessageEvent event) {
//        isSaveDb = true;
//        optionType = FRAGMENT_32;
//        groupInfos = GroupInfoSql.queryGroupList();
//        if (event.flag == Entiy.GROUP_STOP) {
//            handler.removeMessages(HANDLER_WHAT_FALG);
////            doRun(false, event.groupInfo);
//        } else if (event.flag == Entiy.GROUP_NEXT) {
//            handler.removeMessages(HANDLER_WHAT_FALG);
//            event.groupInfo.setGroupTime(0);
//            event.groupInfo.setGroupRunTime(0);
//            event.groupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
//            GroupInfoSql.updateGroup(event.groupInfo);
//            EventBus.getDefault().post(new UpdateEvent());
//            GroupInfo groupInfo = null;
//            for (int i = 0; i < groupInfos.size(); i++) {
//                if (groupInfos.get(i).getGroupTime() > 0) {
//                    groupInfo = groupInfos.get(i);
//                    break;
//                }
//            }
//
//            int size = 0;
//            if (groupInfo != null) {
//                Message message = new Message();
//                message.obj = groupInfo;
//                message.what = HANDLER_WHAT_FALG;
//                handler.sendMessage(message);
//                size = doRun(true, groupInfo);
//            } else {
//                PollingUtils.stopPollingService(MainActivity.this);
//                doRun(false, event.groupInfo);
//                showToastLongMsg("轮灌结束， 关闭自动查询");
//            }
//
//            if (size > 0) {
//                Observable.timer(size * 2 * RXJAVA_TIME, TimeUnit.SECONDS).subscribe(new DisposableObserver<Long>() {
//                    @Override
//                    public void onNext(Long value) {
//                        doNext(event.groupInfo);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//            }
//        } else if (event.flag == Entiy.GROUP_START) {
//            GroupInfo groupInfo = null;
//            for (int i = 0; i < groupInfos.size(); i++) {
//                if (groupInfos.get(i).getGroupTime() > 0) {
//                    groupInfo = groupInfos.get(i);
//                    break;
//                }
//            }
//            if (groupInfo != null) {
//                Message message = new Message();
//                message.obj = groupInfo;
//                message.what = HANDLER_WHAT_FALG;
//                handler.sendMessage(message);
//                doRun(true, groupInfo);
//            } else {
//                PollingUtils.stopPollingService(MainActivity.this);
//                showToastLongMsg("轮灌结束， 关闭自动查询");
//            }
//        }
//    }


//    /**
//     * 定时轮询任务
//     **/
//    private List<ControlInfo> temp;
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onPollingEvent(PollingEvent event) {
//        if (curRunTime < 5 * 60) {
//            LogUtils.e(TAG, "剩余时间不够完成轮询");
//            Toast.makeText(MainActivity.this, "剩余时间不够完成查询操作", Toast.LENGTH_LONG).show();
//            PollingUtils.stopPollingService(this);
//            return;
//        }
//        if (FloatWindowUtil.getInstance().isShow()) {
//            LogUtils.e(TAG, "------" + FloatWindowUtil.getInstance().isShow());
//            return;
//        }
//        optionType = 0;
//        List<GroupInfo> infos = GroupInfoSql.queryGroupList();
//        GroupInfo groupInfo = null;
//        if (infos.size() > 0) {
//            for (int i = 0; i < infos.size(); i++) {
//                if (infos.get(i).getGroupStatus() == Entiy.GROUP_STATUS_OPEN) {
//                    groupInfo = infos.get(i);
//                    break;
//                }
//            }
//        }
//        List<ControlInfo> controlInfos = ControlInfoSql.queryControlList();
//        int size = controlInfos.size();
//        if (groupInfo == null) {
//            LogUtils.e(TAG, "没有在运行的任务");
//        } else {
//            LogUtils.e(TAG, groupInfo.getGroupId() + "运行的任务");
//            if (groupInfo != null) {
//                if (controlInfos != null) {
//                    temp = new ArrayList<>();
//                    for (int i = 0; i < size; i++) {
//                        if (controlInfos.get(i).getValve_group_id() == groupInfo.getGroupId()) {
//                            temp.add(controlInfos.get(i));
//                        }
//                    }
//                    if (temp.size() > 0) {
//                        isSaveDb = false;
//                        optionContron(temp, TYPE_READ);
//                        return;
//                    }
//                }
//            }
//        }
//
//        List<ControlInfo> oneTemp = new ArrayList<>();
//        for (int i = 0; i < size; i++) {
//            if (controlInfos.get(i).getValve_status() == Entiy.CONTROL_STATUS＿RUN || controlInfos.get(i).getValve_status() == Entiy.CONTROL_STATUS＿ERROR) {
//                oneTemp.add(controlInfos.get(i));
//            }
//        }
//        if (oneTemp.size() > 0) {
//            isSaveDb = false;
//            optionContron(oneTemp, TYPE_READ);
//        }
//    }

    private void play() {
        try {
            mp = MediaPlayer.create(MainActivity.this, R.raw.alert);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


//    public void optionContron(final List<ControlInfo> infos, final int type) {
//        if (infos.size() == 0) {
//            return;
//        }
//        if (type == TYPE_READ) {
//            Observable.interval(0, RXJAVA_TIME, TimeUnit.SECONDS)
//                    .take(infos.size())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new DisposableObserver<Long>() {
//                        @Override
//                        public void onNext(Long value) {
//                            int count = value.intValue();
//                            ControlInfo controlInfo = infos.get(count);
//                            Gson gson = new Gson();
//                            String str = gson.toJson(controlInfo);
//                            Log.e("---TYPE_READ ---", "count = " + count + "controlInfo = " + str);
//                            readCmd(infos.get(count), TYPE_READ);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
//        } else {
//            Observable.interval(0, RXJAVA_TIME, TimeUnit.SECONDS)
//                    .take(infos.size())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new DisposableObserver<Long>() {
//                        @Override
//                        public void onNext(Long value) {
//                            int count = value.intValue();
//                            if (type == TYPE_OPEN) {
//                                openCmd(infos.get(count));
//                            } else if (type == TYPE_CLOSE) {
//                                closeCmd(infos.get(count));
//                            }
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
//            Observable.interval(infos.size() * RXJAVA_TIME, RXJAVA_TIME, TimeUnit.SECONDS)
//                    .take(infos.size())
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new DisposableObserver<Long>() {
//                        @Override
//                        public void onNext(Long value) {
//                            int count = value.intValue();
//                            readCmd(infos.get(count), type);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
//        }
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (handler != null) {
            handler.removeMessages(HANDLER_WHAT_FALG);
            handler = null;
        }
        PollingUtils.stopPollingService(this);
        FloatWindowUtil.getInstance().distory();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStatsuEvent(CmdStatus event) {
        FloatWindowUtil.getInstance().onStatsuEvent(event);
    }


//    /**
//     * 查询电量
//     *
//     * @param event
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onElecEvent(ElecEvent event) {
//        if (!BaseApp.isGroupStart()) {
//            List<ControlInfo> controlInfos = ControlInfoSql.queryBindControlList();
//            optionType = FRAGMENT_0;
//            optionContron(controlInfos, TYPE_READ);
//        }
//    }


    /**
     *        接收taks 发送过来的命令 写入
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSendCmdEvent(SendCmdEvent event) {
        if (event == null) {
            return;
        }
        String cmd = event.getCmd();
        if (TextUtils.isEmpty(cmd)) {
            ToastUtils.showLongToast("无效的命令格式");
            return;
        }

        if (!cmd.contains("bid") && !cmd.contains("gid")) {
            showDialog();
        }
        LogUtils.e(TAG, "-----写入命令" + event.getCmd());
        try {
            mOutputStream.write(new String(event.getCmd()).getBytes());
            mOutputStream.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 异常报警
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoPlayEvent(VideoPlayEcent event) {
//        play();
    }

    /**
     *   接收自动轮灌相关操作
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoTaskEvent(AutoTaskEvent event) {
        if(handler != null && event != null) {
            if (event.getType() == Entiy.RUN_DO_NEXT) {
                handler.removeMessages(HANDLER_WHAT_FALG);
                LogUtils.e(TAG, "---------暂停轮灌--------- ");
            }else {
                handler.removeMessages(HANDLER_WHAT_FALG);
                Message message = new Message();
                message.obj = event.getGroupInfo();
                message.what = HANDLER_WHAT_FALG;
                handler.sendMessage(message);
                LogUtils.e(TAG, "----当前运行的groupinfo "+(new Gson().toJson(event.getGroupInfo())));
            }
        }
    }
}
