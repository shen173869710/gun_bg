package com.auto.di.guan;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.LevelInfo;
import com.auto.di.guan.dialog.SureLoadDialog;
import com.auto.di.guan.entity.AdapterEvent;
import com.auto.di.guan.entity.BindEvent;
import com.auto.di.guan.entity.CmdStatus;
import com.auto.di.guan.entity.ControlOptionEvent;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.GroupOptionEvent;
import com.auto.di.guan.entity.MessageEvent;
import com.auto.di.guan.entity.OptionStatus;
import com.auto.di.guan.entity.PollingEvent;
import com.auto.di.guan.entity.ReadEvent;
import com.auto.di.guan.entity.UpdateEvent;
import com.auto.di.guan.utils.ActionUtil;
import com.auto.di.guan.utils.FloatWindowUtil;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.OptionUtils;
import com.auto.di.guan.utils.PollingUtils;
import com.auto.di.guan.utils.SendUtils;
import com.auto.di.guan.utils.ShareUtil;
import com.yongchun.library.view.ImageSelectorActivity;

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

    private final String TAG = "------" + MainActivity.class.getSimpleName();
    private Button button;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private TextView textView;
    private TextView textCode;
    private TextView textStatus;

    private List<GroupInfo> groupInfos;
    private final String TITLE_NAME = "title_name";
    /**
     * 定时任务时间
     **/
    public static final int ALERM_TIME = 1 * 60 * 1000;
    private static final int HANDLER_WHAT_FALG = 1;

    private MediaPlayer mp;

    /**当前执行的操作**/
    public int CMD_TYPE;
    public static final int TYPE_READ = 0;
    public static final int TYPE_OPEN = 1;
    public static final int TYPE_CLOSE = 2;

    public static final int RXJAVA_TIME = 9;

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

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            GroupInfo groupInfo = null;

            if (msg.obj != null) {
                groupInfo = (GroupInfo) msg.obj;
                List<GroupInfo> groupInfos = DBManager.getInstance(MainActivity.this).queryGroupInfoById(groupInfo.getGroupId());
                if (groupInfos != null && groupInfos.size() > 0) {
                    groupInfo.setGroupTime(groupInfos.get(0).groupTime);
                }
                groupInfo.setGroupRunTime(groupInfo.getGroupRunTime() + 1);
                curRunTime = groupInfo.getGroupTime() - groupInfo.getGroupRunTime();
                if (groupInfo.getGroupTime() < groupInfo.getGroupRunTime()) {
                    groupInfo.setGroupTime(0);
                    groupInfo.setGroupRunTime(0);
                    groupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
                    DBManager.getInstance(MainActivity.this).updateGroup(groupInfo);
                    EventBus.getDefault().post(new UpdateEvent());
                    EventBus.getDefault().post(new MessageEvent(Entiy.GROUP_NEXT, groupInfo));
                } else {
                    Message message = new Message();
                    message.obj = groupInfo;
                    message.what = 1;
                    sendMessageDelayed(message, 1000);
                    groupInfo.setGroupStatus(Entiy.GROUP_STATUS_OPEN);
                    DBManager.getInstance(MainActivity.this).updateGroup(groupInfo);
                    EventBus.getDefault().post(new UpdateEvent());
                }
            }
        }
    };

    private void closeCmd(ControlInfo info) {
        CMD_TYPE = TYPE_CLOSE;
        showDialog();
        cur = info;
        final String cmd = Entiy.cmdClose(MyApplication.getProjectId(), info.deviceId, info.name);
        SendUtils.sendClose(cmd,cur.controId,cur.controlName);
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
        final String cmd = Entiy.cmdOpen(MyApplication.getProjectId(), info.deviceId, info.name);
        SendUtils.sendopen(cmd,cur.controId,cur.controlName);
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
        final String cmd = Entiy.cmdRead(MyApplication.getProjectId(), info.deviceId, info.name);
        SendUtils.sendRead(cmd,cur.controId,cur.controlName);
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
        textCode = (TextView) findViewById(R.id.title_bar_code);
        textStatus = (TextView) findViewById(R.id.title_bar_status);

        String mainTitle = ShareUtil.getStringLocalValue(this, TITLE_NAME);
        if (mainTitle == null && TextUtils.isEmpty(mainTitle)) {
            mainTitle = "点击设置项目名称";
        }
        textView.setText(mainTitle);

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
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SureLoadDialog.ShowDialog(MainActivity.this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tag = v.getTag().toString();
                        ShareUtil.setStringLocalValue(MainActivity.this, TITLE_NAME, tag);
                        textView.setText(tag);
                    }
                });
            }
        });
        windowTop = getStatusBarHeight();

        if (DBManager.getInstance(this).queryLevelInfoList().size() == 0) {
            List<LevelInfo> levelInfos = new ArrayList<>();
            for (int i = 1; i < Entiy.GEID_ALL_ITEM; i++) {
                LevelInfo info = new LevelInfo();
                info.setLevelId(i);
                info.setIsGroupUse(false);
                info.setIsLevelUse(false);
                levelInfos.add(info);
            }
            DBManager.getInstance(this).insertLevelInfoList(levelInfos);
        }

        textStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                articleListFragment.showPosition();
//                FloatWindowUtil.getInstance().distory();
                PollingUtils.startPollingService(MainActivity.this, ALERM_TIME);
                showToastLongMsg("开启自动查询");
            }
        });


        textCode = (TextView) findViewById(R.id.title_bar_code);
        textCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ImageSelectorActivity.start(MainActivity.this, 1, ImageSelectorActivity.MODE_SINGLE, true,true,false);
            }
        });
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
        String mainTitle = ShareUtil.getStringLocalValue(this, TITLE_NAME);
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
        List<DeviceInfo> deveiceInfo = DBManager.getInstance(this).queryDeviceList();
        int size = deveiceInfo.size();

        for (int i = 0; i < size; i++) {
            if (groupInfo.getGroupId() == deveiceInfo.get(i).controlInfos.get(0).groupId) {
                infos.add(deveiceInfo.get(i).controlInfos.get(0));
            }
            if (groupInfo.getGroupId() == deveiceInfo.get(i).controlInfos.get(1).groupId) {
                infos.add(deveiceInfo.get(i).controlInfos.get(1));
            }
        }
        groupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
        DBManager.getInstance(this).updateGroup(groupInfo);

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
        List<DeviceInfo> deveiceInfo = DBManager.getInstance(this).queryDeviceList();
        int size = deveiceInfo.size();

        for (int i = 0; i < size; i++) {
            if (groupInfo.getGroupId() == deveiceInfo.get(i).controlInfos.get(0).groupId) {
                infos.add(deveiceInfo.get(i).controlInfos.get(0));
            }
            if (groupInfo.getGroupId() == deveiceInfo.get(i).controlInfos.get(1).groupId) {
                infos.add(deveiceInfo.get(i).controlInfos.get(1));
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
        DBManager.getInstance(this).updateGroup(groupInfo);
        return infos.size();
    }

    String show = null;

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        final String receive = new String(buffer, 0, size);
        show = "";
        Log.e(TAG, "接收命令 -----"+ receive);

        if (TextUtils.isEmpty(receive)) {
            showToastLongMsg("错误命令"+receive);
            return;
        }
        if (receive.contains("gid=") || receive.contains("bid=")){
            EventBus.getDefault().post(new BindEvent(receive));
            return;
        }

        if (receive.length() == 3 || receive.length() == 2) {
            Log.e(TAG, "发送接收成功 -----"+ receive.length());
//            EventBus.getDefault().post(new BindEvent(receive));
            if (isGid == -1) {
                return;
            }
            String cmd = "";
            if (isGid == 0) {
                cmd = "rgid";
            }else if (isGid == 1) {
                cmd = "rbid";
            }
            try {
                mOutputStream.write(cmd.getBytes());
                mOutputStream.write('\n');
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }


        if (receive.toLowerCase().contains("fail") || receive.toLowerCase().contains("cmd")) {
            showToastLongMsg("错误命令"+receive+ "重新操作");
            if (CMD_TYPE == TYPE_CLOSE) {
                closeCmd(cur);
            }else if (CMD_TYPE == TYPE_OPEN) {
                openCmd(cur);
            }else if (CMD_TYPE == TYPE_READ) {
                readCmd(cur,TYPE_READ);
            }
            return;
        }

        if (cur == null) {
            return;
        }

        if (receive.contains("ok") || receive.contains("zt")) {
            OptionStatus status = OptionUtils.receive(receive);
            SendUtils.sendMiddle(receive, cur.controId,cur.controlName);
            if (status == null) {
                showToastLongMsg("未知错误+="+receive);
//                SendUtils.sendError("未知错误"+receive,cur.controId);
                LogUtils.e(TAG,"************************"+receive);
                play();
                return;
            }
            if (status.type.equals( OptionUtils.ZT)) {
                doReadOption(status,cur.deviceId);
            } else if (status.type.equals( OptionUtils.KF) ){
                doOpenOption(status);
            } else if (status.type.equals( OptionUtils.GF)) {
                doCloseOption(status);
            }
        }

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
        groupInfos = DBManager.getInstance(this).queryGrouplList();
        if (event.flag == Entiy.GROUP_STOP) {
            handler.removeMessages(HANDLER_WHAT_FALG);
            doRun(false, event.groupInfo);
        }else if (event.flag == Entiy.GROUP_NEXT) {
            handler.removeMessages(HANDLER_WHAT_FALG);

            event.groupInfo.setGroupTime(0);
            event.groupInfo.setGroupRunTime(0);
            event.groupInfo.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
            DBManager.getInstance(MainActivity.this).updateGroup(event.groupInfo);
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
    public void onGroupStatusEvent(GroupOptionEvent event) {
        isSaveDb = true;
        optionType = FRAGMENT_31;
        LogUtils.e(TAG, "onGroupStatusEvent");
        ArrayList<ControlInfo> controlInfos = new ArrayList<>();
        List<DeviceInfo> deveiceInfos = DBManager.getInstance(this).queryDeviceList();
        GroupInfo groupInfo = event.groupInfo;
        int groupId = groupInfo.groupId;
        int size = deveiceInfos.size();
        for (int i = 0; i < size; i++) {
            if (deveiceInfos.get(i).controlInfos.get(0).groupId == groupId) {
                controlInfos.add(deveiceInfos.get(i).controlInfos.get(0));
            }

            if (deveiceInfos.get(i).controlInfos.get(1).groupId == groupId) {
                controlInfos.add(deveiceInfos.get(i).controlInfos.get(1));
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
                if (deveiceInfos.get(i).controlInfos.get(0).groupId == id) {
                    closeCinfo.add(deveiceInfos.get(i).controlInfos.get(0));
                }

                if (deveiceInfos.get(i).controlInfos.get(1).groupId == id) {
                    closeCinfo.add(deveiceInfos.get(i).controlInfos.get(1));
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
        if (event.isStart) {
            optionContron(list, TYPE_OPEN);
        } else {
            optionContron(list, TYPE_CLOSE);
        }
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
            return;
        }
        if (FloatWindowUtil.getInstance().isShow()) {
            LogUtils.e(TAG, "------"+FloatWindowUtil.getInstance().isShow());
            return;
        }
        optionType = 0;
        List<GroupInfo> infos = DBManager.getInstance(this).queryGrouplList();
        GroupInfo groupInfo = null;
        if(infos.size() > 0) {
            for (int i = 0; i < infos.size(); i++) {
                if (infos.get(i).getGroupStatus() == Entiy.GROUP_STATUS_OPEN) {
                    groupInfo = infos.get(i);
                    break;
                }
            }
        }
        List<ControlInfo> controlInfos = DBManager.getInstance(this).queryControlList();
        int size = controlInfos.size();
        if (groupInfo == null) {
            LogUtils.e(TAG, "没有在运行的任务");
        }else {
            LogUtils.e(TAG, groupInfo.getGroupId()+"运行的任务");
            if (groupInfo != null) {
                if (controlInfos != null) {
                    temp = new ArrayList<>();
                    for (int i = 0; i < size; i++) {
                        if (controlInfos.get(i).groupId == groupInfo.getGroupId()) {
                            temp.add(controlInfos.get(i));
                        }
                    }
                    if(temp.size() > 0) {
                        optionContron(temp, TYPE_READ);
                        isSaveDb = false;
                        return;
                    }
                }
            }
        }

        List<ControlInfo> oneTemp = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            if (controlInfos.get(i).status== Entiy.CONTROL_STATUS＿RUN || controlInfos.get(i).status ==Entiy.CONTROL_STATUS＿ERROR) {
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
            final DeviceInfo deviceInfo = DBManager.getInstance(this).queryDeviceById(Integer.valueOf(deviceId));
            int controlId = cur.controId;
            int type = -1;

            ControlInfo controlInfo = null;

            if (info != null && cur!= null && cur.name.contains("0")) {
                controlInfo = info.controlInfos.get(0);
                int code = info.controlInfos.get(0).status;
                if (code == Entiy.CONTROL_STATUS＿RUN) {
                    deviceInfo.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿RUN;
                    deviceInfo.controlInfos.get(0).imageId = R.mipmap.lighe_2;
                    type = 0;
                    LogUtils.e(TAG, ""+code);

                }else if (code == Entiy.CONTROL_STATUS＿CONNECT) {
                    if (optionType == 0) {
                        deviceInfo.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿ERROR;
                        deviceInfo.controlInfos.get(0).imageId = R.mipmap.lighe_3;
                        type = -1;
                        play();
                    }else {
                        if (CMD_TYPE == TYPE_OPEN) {
                            deviceInfo.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿ERROR;
                            deviceInfo.controlInfos.get(0).imageId = R.mipmap.lighe_3;
                            type = -1;
                            play();
                        }else {
                            deviceInfo.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿CONNECT;
                            deviceInfo.controlInfos.get(0).imageId = R.mipmap.lighe_1;
                            type = 0;
                        }
                        LogUtils.e(TAG, "connect"+code);
                    }
                }else {
                    deviceInfo.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿ERROR;
                    deviceInfo.controlInfos.get(0).imageId = R.mipmap.lighe_3;
                    type = -1;
                    play();
                }
            }else if (info != null && cur!= null && cur.name.contains("1")) {
                int code = info.controlInfos.get(1).status;
                controlInfo = info.controlInfos.get(1);
                if (code == Entiy.CONTROL_STATUS＿RUN) {
                    deviceInfo.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿RUN;
                    deviceInfo.controlInfos.get(1).imageId = R.mipmap.lighe_2;
                    type = 0;
                    LogUtils.e(TAG, ""+code);
                }else if (code == Entiy.CONTROL_STATUS＿CONNECT) {
                    if (optionType == 0) {
                        deviceInfo.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿ERROR;
                        deviceInfo.controlInfos.get(1).imageId = R.mipmap.lighe_3;
                        type = -1;
                        play();
                    }else {
                        LogUtils.e(TAG, "CMD_TYPE =="+CMD_TYPE);
                        if (CMD_TYPE == TYPE_OPEN) {
                            deviceInfo.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿ERROR;
                            deviceInfo.controlInfos.get(1).imageId = R.mipmap.lighe_3;
                            type = -1;
                            play();
                        }else {
                            deviceInfo.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿CONNECT;
                            deviceInfo.controlInfos.get(1).imageId = R.mipmap.lighe_1;
                            type = 0;
                        }
                    }
//                    play();
                }else {
                    deviceInfo.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿ERROR;
                    deviceInfo.controlInfos.get(1).imageId = R.mipmap.lighe_3;
                    type = -1;
                    play();
                }
            }

            if (isSaveDb && controlInfo != null) {
                ActionUtil.saveAction(cur, CMD_TYPE, type, optionType);
        }
            SendUtils.sendEnd(controlId, type, cur.controlName);
            DBManager.getInstance(MainActivity.this).updateDevice(deviceInfo);
            EventBus.getDefault().post(new AdapterEvent());
        } catch (Exception e) {
            LogUtils.e(TAG, e.toString());
        }
    }

    /**
     * 执行开启
     */
    public void doOpenOption(OptionStatus status) {
//        try {
//            int deviceId = Integer.parseInt(status.deviceId);
//            DeviceInfo info = DBManager.getInstance(this).queryDeviceById(deviceId);
//            CmdStatus cmdStatus = new CmdStatus();
//                if ("0".equals(status.name)) {
//                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿RUN;
//                    info.controlInfos.get(0).imageId = R.mipmap.lighe_2;
//                    DBManager.getInstance(this).updateDevice(info);
//                    LogUtils.e(TAG, Entiy.LOG_OPEN_END + status.allCmd);
//                    cmdStatus.control_id = info.controlInfos.get(0).controId+"";
//                    EventBus.getDefault().post(new AdapterEvent());
//                } else if ("1".equals(status.name)) {
//                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿RUN;
//                    info.controlInfos.get(1).imageId = R.mipmap.lighe_2;
//                    DBManager.getInstance(this).updateDevice(info);
//                    LogUtils.e(TAG, Entiy.LOG_OPEN_END + status.allCmd);
//                    EventBus.getDefault().post(new AdapterEvent());
//                    cmdStatus.control_id = info.controlInfos.get(1).controId+"";
//                }
//                cmdStatus.cmd_end = Entiy.LOG_OPEN_END + status.allCmd;
//                EventBus.getDefault().post(cmdStatus);
//            }
//        } catch (Exception e) {
//            LogUtils.e(TAG, e.toString());
//        }
    }

    /**
     * 执行关闭
     */
    public void doCloseOption(OptionStatus status) {
//        try {
//            int deviceId = Integer.parseInt(status.deviceId);
//            DeviceInfo info = DBManager.getInstance(this).queryDeviceById(deviceId);
//            if (info != null) {
//                CmdStatus cmdStatus = new CmdStatus();
//                if ("0".equals(status.name)) {
//                    info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿CONNECT;
//                    info.controlInfos.get(0).imageId = R.mipmap.lighe_1;
//                    DBManager.getInstance(this).updateDevice(info);
//                    LogUtils.e(TAG, Entiy.LOG_CLOSE_END + status.allCmd);
//                    cmdStatus.control_id = info.controlInfos.get(0).controId+"";
//                    EventBus.getDefault().post(new AdapterEvent());
//                } else if ("1".equals(status.name)) {
//                    info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿CONNECT;
//                    info.controlInfos.get(1).imageId = R.mipmap.lighe_1;
//                    DBManager.getInstance(this).updateDevice(info);
//                    LogUtils.e(TAG, Entiy.LOG_CLOSE_END + status.allCmd);
//                    cmdStatus.control_id = info.controlInfos.get(1).controId+"";
//                    EventBus.getDefault().post(new AdapterEvent());
//                }
//                cmdStatus.cmd_end = Entiy.LOG_CLOSE_END + status.allCmd;
//                EventBus.getDefault().post(cmdStatus);
//            }
//        } catch (Exception e) {
//            LogUtils.e(TAG, e.toString());
//        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onStatsuEvent(CmdStatus event) {

        FloatWindowUtil.getInstance().onStatsuEvent(event);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE){
            ArrayList<String> images = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            if (images != null &&images.size() >0) {
                Log.e("-----------", "image"+images.get(0));

//                Glide.with(MainActivity.this).load(images.get(0))
//                        .error(R.drawable.jc_play_normal).into(imageviewvideo);
                LinearLayout linearLayout = (LinearLayout) findViewById(R.id.right);
                linearLayout.setBackground(Drawable.createFromPath(images.get(0)));
            }

        }
    }

}
