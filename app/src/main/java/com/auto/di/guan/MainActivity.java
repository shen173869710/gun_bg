package com.auto.di.guan;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
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
import com.auto.di.guan.entity.PollingEvent;
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
    /**
     * 定时任务时间   你自己在这里修改
     * 5 分钟
     **/
    public static final int ALERM_TIME = 2 * 60 * 1000;
    private static final int HANDLER_WHAT_FALG = 1;
    private MediaPlayer mp;
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
                    TaskFactory.createAutoGroupNextTask(groupInfo);
                    EventBus.getDefault().post(new AutoCountEvent(groupInfo));
                } else {
                    Message message = new Message();
                    message.obj = groupInfo;
                    message.what = HANDLER_WHAT_FALG;
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

        LogUtils.e("time", "time == "+System.currentTimeMillis());
        setContentView(R.layout.activity_main);
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
        LogUtils.e("time", "time == "+System.currentTimeMillis());
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
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

    @Override
    protected void onDataReceived(byte[] buffer, int size) {
        final String receive = new String(buffer, 0, size);
        int length = receive.trim().length();
//        LogUtils.e(TAG, "收到 -------------------" + receive + "    length = " + length);

        if (TextUtils.isEmpty(receive)) {
            ToastUtils.showLongToast("错误命令" + receive);
            return;
        }
        if ((receive.contains("kf")
                || receive.contains("gf")
                || receive.contains("rs"))
                && !receive.contains("ok")) {
            LogUtils.e(TAG, "过滤回显信息 -------------------"+receive);
            return;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TaskManager.getInstance().endTask(receive);
            }
        });
    }

    public void showDialog() {
        FloatWindowUtil.getInstance().show();
    }

    private void play() {
        try {
            mp = MediaPlayer.create(MainActivity.this, R.raw.alert);
            mp.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoPlayEvent(VideoPlayEcent event) {
        play();
    }

    /**
     *   接收自动轮灌相关操作
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoTaskEvent(AutoTaskEvent event) {
        if(handler != null && event != null) {
            /**
             *  停止计时
             */
            if (event.getType() == Entiy.RUN_DO_STOP) {
                handler.removeMessages(HANDLER_WHAT_FALG);
                LogUtils.e(TAG, "---------暂停轮灌计时--------- ");
            }else if (event.getType() == Entiy.RUN_DO_START){
                LogUtils.e(TAG, "---------开启轮灌计时--------- ");
                handler.removeMessages(HANDLER_WHAT_FALG);
                Message message = new Message();
                message.obj = event.getGroupInfo();
                message.what = HANDLER_WHAT_FALG;
                handler.sendMessage(message);
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

    /**
     *  自动轮灌查询功能
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onPollingEvent(PollingEvent event) {
        LogUtils.e(TAG,"自动轮灌查询");
        List<GroupInfo> groupInfos  = GroupInfoSql.queryOpenGroupList();
        if (groupInfos != null && groupInfos.size() == 1) {
            GroupInfo groupInfo  = groupInfos.get(0);
            int time = groupInfo.getGroupTime() - groupInfo.getGroupRunTime();
            if (time > 600) {
                TaskFactory.createPullTask(groupInfo);
            }else {
                PollingUtils.stopPollingService(MainActivity.this);

            }
        }
    }

    private long firstTime=0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK && event.getAction()==KeyEvent.ACTION_DOWN){
            if (System.currentTimeMillis()-firstTime>2000){
                ToastUtils.showToast("再按一次退出");
                firstTime=System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
