package com.auto.di.guan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.auto.di.guan.GroupOptionActivity;
import com.auto.di.guan.GroupStatusActivity;
import com.auto.di.guan.OptionSettingActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.GroupExpandableListViewaAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.GroupList;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.dialog.MainShowDialog;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.event.Fragment31Event;
import com.auto.di.guan.jobqueue.event.Fragment32Event;
import com.auto.di.guan.jobqueue.event.GroupStatusEvent;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 */
public class FragmentTab32 extends BaseFragment {
	private View view;
	private ExpandableListView expandableListView;
	private List<GroupList> groupLists = new ArrayList<>();
	private  List<GroupInfo> groupInfos = new ArrayList<>();
	private GroupExpandableListViewaAdapter adapter;
	private Button fragment_3_2_edit;
	private Button fragment_3_2_stop;
	private Button fragment_3_2_start;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_3_2, null);

		expandableListView =(ExpandableListView)view.findViewById(R.id.fragment_3_2_expand);
		adapter = new GroupExpandableListViewaAdapter(getActivity(), groupLists);
		expandableListView.setGroupIndicator(null);
		expandableListView.setAdapter(adapter);
		expandableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(getActivity(), OptionSettingActivity.class);
				intent.putExtra("id",groupLists.get(position).groupInfo.getGroupId());
				startActivity(intent);
			}
		});
		fragment_3_2_start = (Button) view.findViewById(R.id.fragment_3_2_start);
		fragment_3_2_start.setVisibility(View.GONE);
		fragment_3_2_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				MainShowDialog.ShowDialog(getActivity(), "开启轮灌组", "是否开启轮灌组轮灌操作", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
                        int size = groupInfos.size();
                        for (int i = 0; i < size; i++) {
                            doRun(true,groupInfos.get(i));
                        }
					}
				});
			}
		});

		fragment_3_2_edit = (Button) view.findViewById(R.id.fragment_3_2_edit);
		fragment_3_2_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				if (MyApplication.getInstance().isGroupStart()) {
//					return;
//				}
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				activity.startActivity(new Intent(activity, GroupOptionActivity.class));

			}
		});

		fragment_3_2_stop = (Button) view.findViewById(R.id.fragment_3_2_status);
		fragment_3_2_stop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				activity.startActivity(new Intent(activity, GroupStatusActivity.class));

			}
		});
		initData();
		EventBus.getDefault().register(this);
		LogUtils.e("fragmnet31", "time == "+System.currentTimeMillis());
		return view;
	}

    private void doRun(boolean isSatrt, GroupInfo groupInfo) {
        List<DeviceInfo>deveiceInfo = DeviceInfoSql.queryDeviceList();
        int size = deveiceInfo.size();
        int imageId;
        int status;
		int groupStatus;
        if (isSatrt) {
            status = Entiy.CONTROL_STATUS＿RUN;
            imageId = R.mipmap.lighe_2;
			groupStatus = Entiy.GROUP_STATUS_OPEN;
        }else {
            status = Entiy.CONTROL_STATUS＿CONNECT;
            imageId = R.mipmap.lighe_1;
			groupStatus = Entiy.GROUP_STATUS_COLSE;
		}
		groupInfo.setGroupStatus(groupStatus);
        for (int i =0 ; i < size; i++) {

        	DeviceInfo deviceInfo = deveiceInfo.get(i);
        	ControlInfo controlInfo_0 = deviceInfo.getValveDeviceSwitchList().get(0);
			ControlInfo controlInfo_1 = deviceInfo.getValveDeviceSwitchList().get(1);

            if (groupInfo.getGroupId() == controlInfo_0.getValve_group_id()) {
				controlInfo_0.setValve_status(status);
				controlInfo_0.setValve_imgage_id(imageId);
            }
            if (groupInfo.getGroupId() == controlInfo_1.getValve_group_id()) {
				controlInfo_1.setValve_status(status);
				controlInfo_1.setValve_imgage_id(imageId);
            }
        }
		DeviceInfoSql.updateDeviceList(deveiceInfo);
		GroupInfoSql.updateGroup(groupInfo);
        initData();
    }


	private void initData() {
		groupInfos.clear();
		groupLists.clear();
		groupInfos = GroupInfoSql.queryGroupSettingList();
		int size = groupInfos.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				List<ControlInfo>clist = ControlInfoSql.queryControlList(groupInfos.get(i).getGroupId());
				if (clist.size() > 0) {
					GroupList list = new GroupList();
					list.groupInfo = groupInfos.get(i);
					list.controlInfos.addAll(clist);
					groupLists.add(list);
				}
			}
			if (adapter != null) {
				adapter.setData(groupLists);
			}
		}
	}

	@Override
	public void adapterUpdate() {
		super.adapterUpdate();
		adapter.notifyDataSetChanged();
	}


	@Override
	public void refreshData() {
		LogUtils.e("-------------", "323232");
		initData();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onFragment32Update(Fragment32Event event) {
		if (adapter != null) {
			initData();
		}
	};
	/**
	 *        自动轮灌组状态更新
	 * @param event
	 */
	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onGroupStatusEvent(GroupStatusEvent event) {
		if (adapter != null) {
			initData();
		}
	};
}
