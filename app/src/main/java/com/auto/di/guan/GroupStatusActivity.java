package com.auto.di.guan;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.adapter.GroupStatusAdapter;
import com.auto.di.guan.adapter.StatusAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.entity.AdapterEvent;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.MessageEvent;
import com.auto.di.guan.entity.UpdateEvent;
import com.auto.di.guan.utils.FloatWindowUtil;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.PollingUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**

 */
public class GroupStatusActivity extends FragmentActivity  {
    private View view;
    private TextView textView;
    private TextView title_bar_status;
    private RecyclerView recyclerView;
    private List<GroupInfo> groupInfos = new ArrayList<>();
    private GroupStatusAdapter adapter;

    private StatusAdapter myGridAdapter;
    private GridView gridView;
    private List<ControlInfo> controlInfos = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_status_layout);
        view = findViewById(R.id.title_bar);
        EventBus.getDefault().register(this);
        groupInfos = GroupInfoSql.queryGrouplList();
        Iterator<GroupInfo> iterator = groupInfos.iterator();
        while (iterator.hasNext()) {
            GroupInfo info = iterator.next();
            if (info.getGroupTime() == 0) {
                iterator.remove();
            }
        }

        textView = (TextView) view.findViewById(R.id.title_bar_title);
        textView.setText("关闭轮灌");
        textView.setTextColor(Color.parseColor("#FF0000"));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupInfos = GroupInfoSql.queryGrouplList();
                for (int i = 0; i < groupInfos.size(); i++) {
                    groupInfos.get(i).setGroupRunTime(0);
                    groupInfos.get(i).setGroupTime(0);
                    groupInfos.get(i).setGroupLevel(0);
                    groupInfos.get(i).setGroupStatus(Entiy.GROUP_STATUS_COLSE);
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
                if (groupInfos != null && groupInfos.size() > 0) {
//					if(MyApplication.getInstance().isGroupStart()) {
//						showToastLongMsg("设备运行中");
//						return;
//					}
                    EventBus.getDefault().post(new MessageEvent(Entiy.GROUP_START, groupInfos.get(0)));
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
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        gridView = (GridView) findViewById(R.id.group_option_gridview);
        myGridAdapter = new StatusAdapter(this, controlInfos);
        gridView.setNumColumns(Entiy.GRID_COLUMNS);
        gridView.setAdapter(myGridAdapter);
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateEvent event) {
        if (adapter != null) {
            List<GroupInfo> infos = GroupInfoSql.queryGrouplList();
            int size = infos.size();
            for (int i = 0; i < size; i++) {
                int gSize = groupInfos.size();
                for (int m =  0; m < gSize; m++) {
                    if (infos.get(i).groupId == groupInfos.get(m).groupId) {
                        groupInfos.get(m).groupRunTime = infos.get(i).groupRunTime;
                        groupInfos.get(m).groupStatus = infos.get(i).groupStatus;
                        groupInfos.get(m).groupLevel = infos.get(i).groupLevel;
                        groupInfos.get(m).groupTime = infos.get(i).groupTime;
                    }
                }
            }
            adapter.setData(groupInfos);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 显示字符串类型数据
     *
     * @param msg
     */
    public void showToastLongMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAdapterUpdate(AdapterEvent event) {
        List<GroupInfo> datas = GroupInfoSql.queryGrouplList();
        int size = datas.size();
        LogUtils.e("------", "GroupStatusActivity"+size);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (datas.get(i).groupId == event.groupId ) {
                    List<ControlInfo> clist = ControlInfoSql.queryControlList(datas.get(i).getGroupId());
                    if (clist != null && clist.size() > 0) {
                        ArrayList<ControlInfo> infos = new ArrayList<>();
                        infos.addAll(ControlInfoSql.queryControlList(datas.get(i).getGroupId()));
                        myGridAdapter.setData(infos);
                    }
                }
            }
        }
    }
}
