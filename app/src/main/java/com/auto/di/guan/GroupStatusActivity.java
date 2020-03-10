package com.auto.di.guan;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.auto.di.guan.adapter.GroupStatusAdapter;
import com.auto.di.guan.adapter.StatusAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.jobqueue.event.AutoCountEvent;
import com.auto.di.guan.jobqueue.event.GroupStatusEvent;
import com.auto.di.guan.jobqueue.task.TaskFactory;
import com.auto.di.guan.utils.DiffStatusCallback;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.auto.di.guan.utils.PollingUtils;
import com.auto.di.guan.utils.ToastUtils;
import com.google.gson.Gson;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;

/**
 *   轮灌设置
 */
public class GroupStatusActivity extends FragmentActivity  {
    private View view;
    private TextView textView;
    private TextView title_bar_status;
    private RecyclerView recyclerView;
    private List<GroupInfo> groupInfos = new ArrayList<>();
    private GroupStatusAdapter adapter;

    private StatusAdapter openAdapter;
    private RecyclerView openList;
    private StatusAdapter closeAdapter;
    private RecyclerView closeList;

    private List<ControlInfo> openInfos = new ArrayList<>();
    private List<ControlInfo> closeInfos = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_status_layout);
        view = findViewById(R.id.title_bar);
        EventBus.getDefault().register(this);
        groupInfos = GroupInfoSql.queryGroupSettingList();

        textView = (TextView) view.findViewById(R.id.title_bar_title);
        textView.setText("轮灌操作");

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }
                groupInfos = GroupInfoSql.queryGroupList();
                for (int i = 0; i < groupInfos.size(); i++) {
                    GroupInfo info = groupInfos.get(i);
                    info.setGroupRunTime(0);
                    info.setGroupTime(0);
                    info.setGroupLevel(0);
                    info.setGroupStatus(Entiy.GROUP_STATUS_COLSE);
                }
                GroupInfoSql.updateGroupList(groupInfos);
            }
        });

        final TextView title_bar_pull = (TextView) findViewById(R.id.title_bar_pull);
        title_bar_pull.setVisibility(View.VISIBLE);
        if (PollingUtils.isStart) {
            title_bar_pull.setText("关闭自动查询");
        }else {
            title_bar_pull.setText("开启自动查询");
        }
        title_bar_pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }
                if(PollingUtils.isStart) {
                    PollingUtils.stopPollingService(GroupStatusActivity.this);
                }else {
                    PollingUtils.startPollingService(GroupStatusActivity.this, MainActivity.ALERM_TIME);
                }
                if (PollingUtils.isStart) {
                    title_bar_pull.setText("关闭自动查询");
                }else {
                    title_bar_pull.setText("开启自动查询");
                }
            }
        });

        title_bar_status = (TextView) view.findViewById(R.id.title_bar_status);
        title_bar_status.setVisibility(View.VISIBLE);
        title_bar_status.setText("开启轮灌");
        title_bar_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(NoFastClickUtils.isFastClick()){
                    return;
                }
                if (groupInfos != null && groupInfos.size() > 0) {
                    if (GroupInfoSql.queryOpenGroupList() != null) {
                        ToastUtils.showLongToast("自动轮灌正在运行当中, 无法再次开启自动轮灌");
                        return;
                    }
                    TaskFactory.createAutoGroupOpenTask(groupInfos.get(0));
                    TaskManager.getInstance().startTask();
                }
            }
        });
        view.findViewById(R.id.title_bar_back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GroupStatusActivity.this.finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.group_option_view);
        adapter = new GroupStatusAdapter(groupInfos);
        adapter.setDiffCallback(new DiffStatusCallback());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        openList = findViewById(R.id.group_option_open);
        openList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        openAdapter = new StatusAdapter(openInfos);
        openList.setAdapter(openAdapter);

        closeList = findViewById(R.id.group_option_close);
        closeList.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        closeAdapter = new StatusAdapter(closeInfos);
        closeList.setAdapter(closeAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

        @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAutoCountEvent(AutoCountEvent event) {
        if (adapter != null && event != null && event.getGroupInfo() != null) {
            GroupInfo groupInfo = event.getGroupInfo();
            int groupId = groupInfo.getGroupId();
            int size = groupInfos.size();
            int positin = 0;
            for (int i = 0; i < size; i++) {
                if (groupId == groupInfos.get(i).getGroupId()) {
                    positin = i;
                }
            }
            adapter.getData().set(positin, groupInfo);
            adapter.notifyItemChanged(positin, positin);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGroupStatusEvent(GroupStatusEvent event) {
        LogUtils.e("GroupStatusActivity",  "更新设备-----------------------------\n"+(new Gson().toJson(event)));
        if (event != null && event.getGroupInfo() != null) {
            GroupInfo info = event.getGroupInfo();
            int status = info.getGroupStatus();
            int groupId = info.getGroupId();
            List<ControlInfo> infos = ControlInfoSql.queryControlList(groupId);
            if (status == 1) {
                openAdapter.setData(infos);
            }else {
                closeAdapter.setData(infos);
            }
        }else {
            LogUtils.e("GroupStatusActivity",  "更新设备失败     设备信息为空-----------------------------");
        }
    }
}
