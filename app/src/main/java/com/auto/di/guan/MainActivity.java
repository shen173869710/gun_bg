package com.auto.di.guan;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.LevelInfo;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.LevelInfoSql;
import com.auto.di.guan.entity.AdapterEvent;
import com.auto.di.guan.entity.CmdStatus;
import com.auto.di.guan.entity.ControlOptionEvent;
import com.auto.di.guan.entity.ElecEvent;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.GroupOptionEvent;
import com.auto.di.guan.entity.MessageEvent;
import com.auto.di.guan.entity.OptionStatus;
import com.auto.di.guan.entity.PollingEvent;
import com.auto.di.guan.entity.ReadEvent;
import com.auto.di.guan.entity.UpdateEvent;
import com.auto.di.guan.jobqueue.TaskManger;
import com.auto.di.guan.jobqueue.TestEvent;
import com.auto.di.guan.utils.ActionUtil;
import com.auto.di.guan.utils.FloatWindowUtil;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.OptionUtils;
import com.auto.di.guan.utils.PollingUtils;
import com.auto.di.guan.utils.SendUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 */
public class MainActivity extends SerialPortActivity {
    /**
     *  是否执行过一次
     */
    private boolean hasRunCmd = false;

    private final String TAG = "------" + MainActivity.class.getSimpleName();
    private Button button;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TextView textView;

    private List<GroupInfo> groupInfos;
    /**
     * 定时任务时间   你自己在这里修改
     *  5 分钟
     **/
    public static final int ALERM_TIME = 2 * 60 * 1000;
    private static final int HANDLER_WHAT_FALG = 1;

    private MediaPlayer mp;

    /**当前执行的操作**/
    public int CMD_TYPE;
    public static final int TYPE_READ = 0;
    public static final int TYPE_OPEN = 1;
    public static final int TYPE_CLOSE = 2;
    /** 执行单个命令的时间**/
    public static final int RXJAVA_TIME = 25;

    private ControlInfo cur;
    /**是否是自动查询**/
    public boolean isSaveDb;
    /**当前运行的剩余时间***/
    public int curRunTime = 0;

    /**
     *   操作的页面
     *
     *   400  单个手动操作
     *   310  单个分组操作
     *   320  自动分组操作
     */
    public int optionType = 0;
    public static int  FRAGMENT_4 = 400;
    public static int  FRAGMENT_31 = 310;
    public static int  FRAGMENT_32 = 320;
    public static int  FRAGMENT_0 = 1000;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GroupInfo groupInfo = null;

            if (msg.obj != null) {
                groupInfo = (GroupInfo) msg.obj;
                List<GroupInfo> groupInfos = GroupInfoSql.queryGroupInfoById(groupInfo.getGroupId());
                if (groupInfos != null && groupInfos.size() > 0) {
                    groupInfo.setGroupTime(groupInfos.get(0).groupTime);
                }
                groupInfo.setGroupRunTime(groupInfo.getGroupRunTime() + 1);
                curRunTime = groupInfo.getGroupTime() - groupInfo.getGroupRunTime();
                if (groupInfo.getGroupTime() < groupInfo.getGroupRunTime()) {
                    groupInfo.setGroupTime(0);
                    groupInfo.setGroupRunTime(0);
                    groupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
                    GroupInfoSql.updateGroup(groupInfo);
                    EventBus.getDefault().post(new UpdateEvent());
                    EventBus.getDefault().post(new MessageEvent(Entiy.GROUP_NEXT, groupInfo));
                } else {
                    Message message = new Message();
                    message.obj = groupInfo;
                    message.what = 1;
                    sendMessageDelayed(message, 1000);
                    groupInfo.setGroupStatus(Entiy.GROUP_STATUS_OPEN);
                    GroupInfoSql.updateGroup(groupInfo);
                    EventBus.getDefault().post(new UpdateEvent());
                }
            }
        }
    };

    private void closeCmd(ControlInfo info) {
        CMD_TYPE = TYPE_CLOSE;
        showDialog();
        cur = info;
        final String cmd = Entiy.cmdClose(BaseApp.getProjectId(), info.getProtocalId(), info.getValve_name());
        SendUtils.sendClose(cmd,cur);
        Log.e(TAG, Entiy.LOG_CLOSE_START + cmd);
		try {
			mOutputStream.write(new String(cmd).getBytes());
			mOutputStream.write('\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void openCmd(ControlInfo info) {
        CMD_TYPE = TYPE_OPEN;
        showDialog();
        cur = info;
        final String cmd = Entiy.cmdOpen(BaseApp.getProjectId(), info.getProtocalId(), info.getValve_name());
        SendUtils.sendopen(cmd,cur);
        Log.e(TAG, Entiy.LOG_OPEN_START + cmd);
		try {
			mOutputStream.write(new String(cmd).getBytes());
			mOutputStream.write('\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    private void readCmd(ControlInfo info, int type) {
        CMD_TYPE = type;
         showDialog();
        cur = info;
        final String cmd = Entiy.cmdRead(BaseApp.getProjectId(), info.getProtocalId());
        SendUtils.sendRead(cmd,cur);
        Log.e("-------读取设备", cmd + "       " + System.currentTimeMillis());
        try {
			mOutputStream.write(new String(cmd).getBytes());
			mOutputStream.write('\n');
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    public static int windowTop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//		PollingUtils.startPollingService(this, ALERM_TIME);
        EventBus.getDefault().register(this);
        textView = (TextView) findViewById(R.id.title_bar_title);
//        textCode = (TextView) findViewById(R.id.title_bar_code);
        textView.setText(BaseApp.getUser().getProjectName());

        findViewById(R.id.title_bar_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowUtil.getInstance().distory();
            }
        });

        manager = getSupportFragmentManager();

        transaction = manager.beginTransaction();
        final ArticleListFragment articleListFragment = new ArticleListFragment();
        transaction.add(R.id.center, articleListFragment, "center");
        transaction.commitAllowingStateLoss();

        windowTop = getStatusBarHeight();

        if (LevelInfoSql.queryLevelInfoList().size() == 0) {
            List<LevelInfo> levelInfos = new ArrayList<>();
            for (int i = 1; i < 2000; i++) {
                LevelInfo info = new LevelInfo();
                info.setLevelId(i);
                info.setIsGroupUse(false);
                info.setIsLevelUse(false);
                levelInfos.add(info);
            }
            LevelInfoSql.insertLevelInfoList(levelInfos);
        }

//        textStatus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                articleListFragment.showPosition();
////                FloatWindowUtil.getInstance().distory();
//                PollingUtils.startPollingService(MainActivity.this, ALERM_TIME);
//                showToastLongMsg("开启自动查询");
//            }
//        });


//        textCode = (TextView) findViewById(R.id.title_bar_code);
//        textCode.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!MyApplication.isGroupStart()) {
//                    List<ControlInfo> controlInfos = DBManager.getInstance(MainActivity.this).queryBindControlList();
//                    optionType = FRAGMENT_0;
//                    optionContron(controlInfos, TYPE_READ);
//                }
//            }
//        });

        TaskManger.getInstance().init(mOutputStream);
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
    protected void  onSaveInstanceState(Bundle outState) {
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


    public void doNext(GroupInfo groupInfo) {
        final List<ControlInfo> infos = new ArrayList<>();
        List<DeviceInfo> deveiceInfo = DeviceInfoSql.queryDeviceList();
        int size = deveiceInfo.size();

        for (int i = 0; i < size; i++) {
            if (groupInfo.getGroupId() == deveiceInfo.get(i).getValveDeviceSwitchList().get(0).getValve_group_id()) {
                infos.add(deveiceInfo.get(i).getValveDeviceSwitchList().get(0));
            }
            if (groupInfo.getGroupId() == deveiceInfo.get(i).getValveDeviceSwitchList().get(1).getValve_group_id()) {
                infos.add(deveiceInfo.get(i).getValveDeviceSwitchList().get(1));
            }
        }
        groupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
        GroupInfoSql.updateGroup(groupInfo);

        Observable.interval(0, RXJAVA_TIME, TimeUnit.SECONDS)
                .take(infos.size())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long value) {
                        int count = value.intValue();
                        closeCmd(infos.get(count));
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        Observable.interval(infos.size() * RXJAVA_TIME, RXJAVA_TIME, TimeUnit.SECONDS)
                .take(infos.size())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long value) {
                        int count = value.intValue();
                        readCmd(infos.get(count), TYPE_CLOSE);

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        LogUtils.e(TAG, "---关闭完成----"+System.currentTimeMillis());
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        }, RXJAVA_TIME*1000);
                    }
                });
    }
    /**
     * 关闭 或者开启设备
     **/
    private int doRun(boolean isSatrt, GroupInfo groupInfo) {
        List<ControlInfo> infos = new ArrayList<>();
        List<DeviceInfo> deveiceInfo = DeviceInfoSql.queryDeviceList();
        int size = deveiceInfo.size();

        for (int i = 0; i < size; i++) {
            if (groupInfo.getGroupId() == deveiceInfo.get(i).getValveDeviceSwitchList().get(0).getValve_group_id()) {
                infos.add(deveiceInfo.get(i).getValveDeviceSwitchList().get(0));
            }
            if (groupInfo.getGroupId() == deveiceInfo.get(i).getValveDeviceSwitchList().get(1).getValve_group_id()) {
                infos.add(deveiceInfo.get(i).getValveDeviceSwitchList().get(1));
            }
        }
        int groupStatus;
        if (isSatrt) {
            groupStatus = Entiy.GROUP_STATUS_OPEN;
            optionContron(infos, TYPE_OPEN);
        } else {
            groupStatus = Entiy.GROUP_STATUS_COLSE;
            optionContron(infos, TYPE_CLOSE);
        }
        groupInfo.setGroupStatus(groupStatus);
        GroupInfoSql.updateGroup(groupInfo);
        return infos.size();
    }

    String show = null;

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        final String receive = new String(buffer, 0, size);
        show = "";
        int length = receive.trim().length();
        Log.e(TAG, "接收命令 -------------------"+ receive+"    length = "+length);

        if (TextUtils.isEmpty(receive)) {
            showToastLongMsg("错误命令"+receive);
            return;
        }

        TaskManger.getInstance().endTask(receive);
//        if (receive.toLowerCase().contains("ok") && length == 2){
//            Log.e(TAG, "绑定成功 -----");
//            EventBus.getDefault().post(new BindEvent(receive));
//            return;
//        }
//
//        if (receive.length() == 3 || receive.length() == 2) {
//            Log.e(TAG, "发送接收成功 -----"+ receive.length());
////            EventBus.getDefault().post(new BindEvent(receive));
//            if (isGid == -1) {
//                return;
//            }
//
//            String cmd = "";
//            if (isGid == 0) {
//                cmd = "rgid";
//            }else if (isGid == 1) {
//                cmd = "rbid";
//            }
//            try {
//                mOutputStream.write(cmd.getBytes());
//                mOutputStream.write('\n');
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return;
//        }
//        if (!receive.toLowerCase().contains("zt")&&
//        !receive.toLowerCase().contains("rs")
//                && !receive.toLowerCase().contains("gf")
//                && !receive.toLowerCase().contains("kf")) {
//            showToastLongMsg("错误命令"+receive+ "重新操作");
//
//            LogUtils.e("------hasnRunCmd====",""+hasRunCmd);
//            if (hasRunCmd){
//                hasRunCmd = false;
//                SendUtils.sendError("通讯失败",cur.getValve_id(), cur.getValve_alias());
//                play();
//                return;
//            }
//            hasRunCmd = true;
//            if (CMD_TYPE == TYPE_CLOSE) {
//                LogUtils.e("------TYPE_CLOSE====","第二次执行");
//                closeCmd(cur);
//            }else if (CMD_TYPE == TYPE_OPEN) {
//                LogUtils.e("------TYPE_OPEN====","第二次执行");
//                openCmd(cur);
//            }else if (CMD_TYPE == TYPE_READ) {
//                LogUtils.e("------TYPE_READ====","第二次执行");
//                readCmd(cur,TYPE_READ);
//            }
//            return;
//        }
//        if (cur == null) {
//            return;
//        }
//        if (receive.contains("ok") || receive.contains("zt")) {
//            OptionStatus status = OptionUtils.receive(receive);
//            SendUtils.sendMiddle(receive, cur.getValve_id(),cur.getValve_alias());
//            if (status == null) {
//                showToastLongMsg("未知错误+="+receive);
////                SendUtils.sendError("未知错误"+receive,cur.controId);
//                LogUtils.e(TAG,"************************"+receive);
//                play();
//                return;
//            }
//            if (status.type.equals( OptionUtils.ZT)) {
//                doReadOption(status,cur.getProtocalId());
//            } else if (status.type.equals( OptionUtils.KF) ){
//                doOpenOption(status);
//            } else if (status.type.equals( OptionUtils.GF)) {
//                doCloseOption(status);
//            }
//        }

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

    /**
     * 进行批量分组操作
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(final MessageEvent event) {
        isSaveDb = true;
        optionType = FRAGMENT_32;
        groupInfos = GroupInfoSql.queryGrouplList();
        if (event.flag == Entiy.GROUP_STOP) {
            handler.removeMessages(HANDLER_WHAT_FALG);
//            doRun(false, event.groupInfo);
        }else if (event.flag == Entiy.GROUP_NEXT) {
            handler.removeMessages(HANDLER_WHAT_FALG);
            event.groupInfo.setGroupTime(0);
            event.groupInfo.setGroupRunTime(0);
            event.groupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
            GroupInfoSql.updateGroup(event.groupInfo);
            EventBus.getDefault().post(new UpdateEvent());
            GroupInfo groupInfo = null;
            for (int i = 0; i < groupInfos.size(); i++) {
                if (groupInfos.get(i).getGroupTime() > 0) {
                    groupInfo = groupInfos.get(i);
                    break;
                }
            }

            int size = 0;
            if(groupInfo != null) {
                Message message = new Message();
                message.obj = groupInfo;
                message.what = HANDLER_WHAT_FALG;
                handler.sendMessage(message);
                size = doRun(true, groupInfo);
            }else {
                PollingUtils.stopPollingService(MainActivity.this);
                doRun(false, event.groupInfo);
                showToastLongMsg("轮灌结束， 关闭自动查询");
            }

            if (size > 0) {
                Observable.timer(size * 2 * RXJAVA_TIME, TimeUnit.SECONDS).subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onNext(Long value) {
                        doNext(event.groupInfo);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }else if (event.flag == Entiy.GROUP_START) {
            GroupInfo groupInfo = null;
            for (int i = 0; i < groupInfos.size(); i++) {
                if (groupInfos.get(i).getGroupTime() > 0) {
                    groupInfo = groupInfos.get(i);
                    break;
                }
            }
            if(groupInfo != null) {
                Message message = new Message();
                message.obj = groupInfo;
                message.what = HANDLER_WHAT_FALG;
                handler.sendMessage(message);
                doRun(true, groupInfo);
            }else {
                PollingUtils.stopPollingService(MainActivity.this);
                showToastLongMsg("轮灌结束， 关闭自动查询");
            }
        }
    }
    /**
     * 分组手动操作
     **/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupStatusEvent(final GroupOptionEvent event) {
        isSaveDb = true;
        optionType = FRAGMENT_31;
        LogUtils.e(TAG, "onGroupStatusEvent");
        ArrayList<ControlInfo> controlInfos = new ArrayList<>();
        List<DeviceInfo> deveiceInfos = DeviceInfoSql.queryDeviceList();
        GroupInfo groupInfo = event.groupInfo;
        int groupId = groupInfo.groupId;
        int size = deveiceInfos.size();
        for (int i = 0; i < size; i++) {
            if (deveiceInfos.get(i).getValveDeviceSwitchList().get(0).getValve_group_id() == groupId) {
                controlInfos.add(deveiceInfos.get(i).getValveDeviceSwitchList().get(0));
            }

            if (deveiceInfos.get(i).getValveDeviceSwitchList().get(1).getValve_group_id() == groupId) {
                controlInfos.add(deveiceInfos.get(i).getValveDeviceSwitchList().get(1));
            }
        }

        if (event.isStart) {
            optionContron(controlInfos, TYPE_OPEN);
        } else {
            optionContron(controlInfos, TYPE_CLOSE);
        }

        if (event.isStart && event.closeInfo != null) {
            final List<ControlInfo>closeCinfo = new ArrayList<>();
            int id = event.closeInfo.groupId;
            for (int i = 0; i < size; i++) {
                if (deveiceInfos.get(i).getValveDeviceSwitchList().get(0).getValve_group_id() == id) {
                    closeCinfo.add(deveiceInfos.get(i).getValveDeviceSwitchList().get(0));
                }

                if (deveiceInfos.get(i).getValveDeviceSwitchList().get(1).getValve_group_id() == id) {
                    closeCinfo.add(deveiceInfos.get(i).getValveDeviceSwitchList().get(1));
                }
            }

            Observable.timer(controlInfos.size() * 2 * RXJAVA_TIME, TimeUnit.SECONDS).subscribe(new DisposableObserver<Long>() {
                @Override
                public void onNext(Long value) {
                    optionContron(closeCinfo, TYPE_CLOSE);
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {
                    event.closeInfo.setGroupTime(0);
                    event.closeInfo.setGroupRunTime(0);
                    event.closeInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
                    GroupInfoSql.updateGroup(event.closeInfo);
                    EventBus.getDefault().post(new UpdateEvent());
                }
            });
        }
    }

    /**
     * 单个手动操作
     **/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onControlStatusEvent(ControlOptionEvent event) {
        isSaveDb = true;
        optionType = FRAGMENT_4;
        List<ControlInfo>list = new ArrayList<>();
        list.add(event.controlInfo);

        if (event.type == 0) {
            readCmd(event.controlInfo,TYPE_READ);
        }else if (event.type == 1) {
            optionContron(list, TYPE_OPEN);
        }else if (event.type == 2) {
            optionContron(list, TYPE_CLOSE);
        }
//        if(event.controlInfo.status ==  Entiy.CONTROL_STATUS＿ERROR) {
//            readCmd(event.controlInfo,TYPE_READ);
//            return;
//        }
//
//        if (event.isStart) {
//            optionContron(list, TYPE_OPEN);
//        } else {
//            optionContron(list, TYPE_CLOSE);
//        }
    }

    private  int isGid = -1;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onControlStatusEvent(ReadEvent event) {
        isGid = event.isGid;
        LogUtils.e(TAG,"发送命令"+event.cmd);
        try {
            mOutputStream.write(event.cmd.getBytes());
            mOutputStream.write('\n');
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 定时轮询任务
     **/
    private List<ControlInfo> temp;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPollingEvent(PollingEvent event) {
        if (curRunTime < 5 * 60) {
            LogUtils.e(TAG, "剩余时间不够完成轮询");
            Toast.makeText(MainActivity.this, "剩余时间不够完成查询操作", Toast.LENGTH_LONG).show();
            PollingUtils.stopPollingService(this);
            return;
        }
        if (FloatWindowUtil.getInstance().isShow()) {
            LogUtils.e(TAG, "------"+FloatWindowUtil.getInstance().isShow());
            return;
        }
        optionType = 0;
        List<GroupInfo> infos = GroupInfoSql.queryGrouplList();
        GroupInfo groupInfo = null;
        if(infos.size() > 0) {
            for (int i = 0; i < infos.size(); i++) {
                if (infos.get(i).getGroupStatus() == Entiy.GROUP_STATUS_OPEN) {
                    groupInfo = infos.get(i);
                    break;
                }
            }
        }
        List<ControlInfo> controlInfos = ControlInfoSql.queryControlList();
        int size = controlInfos.size();
        if (groupInfo == null) {
            LogUtils.e(TAG, "没有在运行的任务");
        }else {
            LogUtils.e(TAG, groupInfo.getGroupId()+"运行的任务");
            if (groupInfo != null) {
                if (controlInfos != null) {
                    temp = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        if (controlInfos.get(i).getValve_group_id() == groupInfo.getGroupId()) {
                            temp.add(controlInfos.get(i));
                        }
                    }
                    if(temp.size() > 0) {
                        isSaveDb = false;
                        optionContron(temp, TYPE_READ);
                        return;
                    }
                }
            }
        }

        List<ControlInfo> oneTemp = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (controlInfos.get(i).getValve_status()== Entiy.CONTROL_STATUS＿RUN || controlInfos.get(i).getValve_status() ==Entiy.CONTROL_STATUS＿ERROR) {
                oneTemp.add(controlInfos.get(i));
            }
        }
        if(oneTemp.size() > 0) {
            isSaveDb = false;
            optionContron(oneTemp, TYPE_READ);
        }
    }
    private void play() {
        try {
            mp = MediaPlayer.create(MainActivity.this, R.raw.alert);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void optionContron(final List<ControlInfo> infos, final int type) {
        if (infos.size() == 0) {
            return;
        }
        if (type == TYPE_READ) {
            Observable.interval(0, RXJAVA_TIME, TimeUnit.SECONDS)
                    .take(infos.size())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Long>() {
                        @Override
                        public void onNext(Long value) {
                            int count = value.intValue();
                            ControlInfo controlInfo = infos.get(count);
                            Gson gson = new Gson();
                            String str = gson.toJson(controlInfo);
                            Log.e("---TYPE_READ ---", "count = "+count + "controlInfo = "+str);
                            readCmd(infos.get(count),TYPE_READ);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }else {
            Observable.interval(0, RXJAVA_TIME, TimeUnit.SECONDS)
                    .take(infos.size())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Long>() {
                        @Override
                        public void onNext(Long value) {
                            int count = value.intValue();
                            if (type == TYPE_OPEN) {
                                openCmd(infos.get(count));
                            } else if (type == TYPE_CLOSE) {
                                closeCmd(infos.get(count));
                            }

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
            Observable.interval(infos.size() * RXJAVA_TIME, RXJAVA_TIME, TimeUnit.SECONDS)
                    .take(infos.size())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new DisposableObserver<Long>() {
                        @Override
                        public void onNext(Long value) {
                            int count = value.intValue();
                            readCmd(infos.get(count),type);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        handler.removeMessages(HANDLER_WHAT_FALG);
        PollingUtils.stopPollingService(this);
        FloatWindowUtil.getInstance().distory();
    }
    /**
     * 执行读处理
     */
    public int count = 0;
    public void doReadOption(OptionStatus status, String deviceId) {
        Log.e(" ----doReadOption---", "status = "+new Gson().toJson(status));
        //status = {"allCmd":"zt 102 002 1100 090\n\u0000","code":"1100","deviceId":"002","elect":"090","projectId":"102","type":"zt","status":0}
        try {
            DeviceInfo info = OptionUtils.changeStatus(status);
            if (count == 1) {
                count = 0;
            }
            if (info == null) {
                readCmd(cur, TYPE_READ);
                count++;
                showToastLongMsg("无法解析的命令，重发命令");
                return;
            }
            final DeviceInfo deviceInfo = DeviceInfoSql.queryDeviceById(Integer.valueOf(deviceId));
            int controlId = cur.getValve_id();
            int type = -1;

            if (TextUtils.isEmpty(status.elect)) {
                try {
                    deviceInfo.setElectricQuantity(Integer.valueOf(status.elect));
                }catch (Exception e) {

                }
            }


            ControlInfo controlInfo = null;
            Log.e(" ----开关0---", "cur = "+new Gson().toJson(cur) + "  info = "+new Gson().toJson(info)+ " optionType ="+optionType+"  CMD_TYPE"+CMD_TYPE);
            if (info != null && cur!= null && cur.getValve_name().contains("0")) {
                controlInfo = info.getValveDeviceSwitchList().get(0);
                int code = controlInfo.getValve_status();
                if (code == Entiy.CONTROL_STATUS＿RUN) {
                    controlInfo.setValve_status(Entiy.CONTROL_STATUS＿RUN);
                    controlInfo.setValve_imgage_id(R.mipmap.lighe_2);
                    type = 0;
                    LogUtils.e(TAG, ""+code);

                }else if (code == Entiy.CONTROL_STATUS＿CONNECT) {
                    if (optionType == 0) {
                        controlInfo.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                        controlInfo.setValve_imgage_id(R.mipmap.lighe_3);
                        type = -1;
                        play();
                    }else {
                        if (CMD_TYPE == TYPE_OPEN) {
                            controlInfo.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                            controlInfo.setValve_imgage_id(R.mipmap.lighe_3);
                            type = -1;
                            play();
                            Log.e(" ----887---", "-----------------");
                        }else {
                            controlInfo.setValve_status(Entiy.CONTROL_STATUS＿CONNECT);
                            controlInfo.setValve_imgage_id(R.mipmap.lighe_1);
                            type = 0;
                            Log.e(" ----892---", "-----------------");
                        }
                        LogUtils.e(TAG, "connect"+code);
                    }
                }else if (code == Entiy.CONTROL_STATUS＿NOTCLOSE){

                    controlInfo.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                    controlInfo.setValve_imgage_id(R.mipmap.lighe_3);
                    play();
                    type = -2;
                    Log.e(" ----901---", "-----------------");
                }else {
                    if (CMD_TYPE == TYPE_OPEN) {
                        controlInfo.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                        controlInfo.setValve_imgage_id(R.mipmap.lighe_3);
                        type = -1;
                        play();
                        Log.e(" ----887---", "-----------------");
                    }else {
                        controlInfo.setValve_status(Entiy.CONTROL_STATUS＿CONNECT);
                        controlInfo.setValve_imgage_id(R.mipmap.lighe_1);
                        type = 0;
                        Log.e(" ----892---", "-----------------");
                    }
                    controlInfo.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                    controlInfo.setValve_imgage_id(R.mipmap.lighe_3);
                    play();
                    type = -1;
                    Log.e(" ----907---", "-----------------");
                }
            }else if (info != null && cur!= null && cur.getValve_name().contains("1")) {
                controlInfo = info.getValveDeviceSwitchList().get(1);
                int code = controlInfo.getValve_status();

                if (code == Entiy.CONTROL_STATUS＿RUN) {
                    controlInfo.setValve_status(Entiy.CONTROL_STATUS＿RUN);
                    controlInfo.setValve_imgage_id(R.mipmap.lighe_2);
                    type = 0;
                    LogUtils.e(TAG, ""+code);
                }else if (code == Entiy.CONTROL_STATUS＿CONNECT) {
                    if (optionType == 0) {
                        controlInfo.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                        controlInfo.setValve_imgage_id(R.mipmap.lighe_3);
                        type = -1;
                        play();
                        Log.e(" ----923---", "-----------------");
                    }else {
                        LogUtils.e(TAG, "CMD_TYPE =="+CMD_TYPE);
                        if (CMD_TYPE == TYPE_OPEN) {
                            controlInfo.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                            controlInfo.setValve_imgage_id(R.mipmap.lighe_3);
                            type = -1;
                            play();
                            Log.e(" ----931---", "-----------------");
                        }else {
                            controlInfo.setValve_status(Entiy.CONTROL_STATUS＿CONNECT);
                            controlInfo.setValve_imgage_id(R.mipmap.lighe_1);
                            type = 0;
                        }
                    }
//                    play();
                }else if (code == Entiy.CONTROL_STATUS＿NOTCLOSE){
                    controlInfo.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                    controlInfo.setValve_imgage_id(R.mipmap.lighe_3);
                        type = -2;
                        play();
                    Log.e(" ----944---", "-----------------");
                }else {
                    controlInfo.setValve_status(Entiy.CONTROL_STATUS＿ERROR);
                    controlInfo.setValve_imgage_id(R.mipmap.lighe_3);
                    type = -1;
                    play();
                    Log.e(" ----950---", "-----------------");
                }
            }

            if (isSaveDb && controlInfo != null) {
                ActionUtil.saveAction(cur, CMD_TYPE, type, optionType);
            }
            SendUtils.sendEnd(controlId, type, cur.getValve_alias());
            DeviceInfoSql.updateDevice(deviceInfo);
            String str = new Gson().toJson(DeviceInfoSql.queryDeviceList());
            LogUtils.e("------", "main updatte = "+str);
            EventBus.getDefault().post(new AdapterEvent(cur.getValve_group_id()));
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStatsuEvent(CmdStatus event) {
        FloatWindowUtil.getInstance().onStatsuEvent(event);
    }


    /**
     *        查询电量
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onElecEvent(ElecEvent event) {
        if(!BaseApp.isGroupStart()) {
            List<ControlInfo> controlInfos = ControlInfoSql.queryBindControlList();
            optionType = FRAGMENT_0;
            optionContron(controlInfos, TYPE_READ);
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onTestEvent (TestEvent event) {
       TaskManger.getInstance().endTask(event.recive);
    }

}
