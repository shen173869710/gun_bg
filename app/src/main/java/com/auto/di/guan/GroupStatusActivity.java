package com.auto.di.guan;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.di.guan.adapter.GroupStatusAdapter;
import com.auto.di.guan.adapter.MyGridAdapter;
import com.auto.di.guan.adapter.RecyclerListAdapter;
import com.auto.di.guan.adapter.StatusAdapter;
import com.auto.di.guan.adapter.helper.OnStartDragListener;
import com.auto.di.guan.adapter.helper.SimpleItemTouchHelperCallback;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.GroupList;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**

 */
public class GroupStatusActivity extends FragmentActivity implements OnStartDragListener {
    private View view;
    private TextView textView;
    private TextView title_bar_status;
    private ItemTouchHelper mItemTouchHelper;
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
        groupInfos = DBManager.getInstance(this).queryGrouplList();

        textView = (TextView) view.findViewById(R.id.title_bar_title);
        textView.setText("自动轮灌");

        findViewById(R.id.title_bar_close).setVisibility(View.VISIBLE);
        findViewById(R.id.title_bar_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatWindowUtil.getInstance().distory();
            }
        });

        findViewById(R.id.title_bar_pull).setVisibility(View.VISIBLE);
        findViewById(R.id.title_bar_pull).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PollingUtils.startPollingService(GroupStatusActivity.this, MainActivity.ALERM_TIME);
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
        adapter = new GroupStatusAdapter(this, groupInfos);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        gridView = (GridView) findViewById(R.id.group_option_gridview);
        myGridAdapter = new StatusAdapter(this, controlInfos);
        gridView.setNumColumns(Entiy.GRID_COLUMNS);
        gridView.setAdapter(myGridAdapter);
    }


    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(UpdateEvent event) {
        if (adapter != null) {
//			Log.e("onMessageEvent", "UpdateEvent");
            groupInfos = DBManager.getInstance(this).queryGrouplList();
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

        groupInfos = DBManager.getInstance(this).queryGrouplList();
        int size = groupInfos.size();
        LogUtils.e("------", "GroupStatusActivity"+size);
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                if (groupInfos.get(i).getGroupStatus() == Entiy.GROUP_STATUS_OPEN) {
                    LogUtils.e("------", "-------------------"+size);
                    controlInfos.clear();
                    List<ControlInfo> clist = DBManager.getInstance(this).queryControlList(groupInfos.get(i).getGroupId());
                    controlInfos.addAll(clist);
                    LogUtils.e("------", "clist-------------------"+clist.size());
                    myGridAdapter.setData(controlInfos);
                }
            }
        }
    }
}
