package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.GroupExpandableListViewaAdapter31;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.GroupList;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.dialog.Main31Dialog;
import com.auto.di.guan.jobqueue.TaskManager;
import com.auto.di.guan.jobqueue.event.Fragment31Event;
import com.auto.di.guan.jobqueue.event.GroupStatusEvent;
import com.auto.di.guan.jobqueue.task.TaskFactory;
import com.auto.di.guan.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**

 *
 */
public class FragmentTab31 extends BaseFragment {

	private View view;
	private ExpandableListView expandableListView;
	private GroupExpandableListViewaAdapter31 adapter;
	private List<GroupList> groupLists = new ArrayList<>();
	private  List<GroupInfo> groupInfos = new ArrayList<>();
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_3_1, null);
		expandableListView =(ExpandableListView)view.findViewById(R.id.fragment_3_1_expand);
		adapter = new GroupExpandableListViewaAdapter31(getActivity(), groupLists, this);
		expandableListView.setAdapter(adapter);
		expandableListView.setGroupIndicator(null);
		EventBus.getDefault().register(this);
		initData();
		LogUtils.e("fragmnet31", "time == "+System.currentTimeMillis());
		return view;
	}



	public void startWork(final GroupInfo groupInfo) {
		String title;
		if (groupInfo.getGroupStatus() == 0) {
			title = "当前分组处于关闭状态";
		}else {
			title = "当前分组处于运行状态";
		}

		Main31Dialog.ShowDialog(getActivity(),title, new Main31Dialog.ItemClick(){

			@Override
			public void onItemClick(int index) {
				if (index == 1) {
					TaskFactory.createGroupOpenTask(groupInfo);
					TaskManager.getInstance().startTask();
				}else if (index == 2) {
					TaskFactory.createGroupCloseTask(groupInfo);
					TaskManager.getInstance().startTask();
				}
			}
		});
	}

	public void initData() {
		groupInfos.clear();
		groupLists.clear();
		groupInfos = GroupInfoSql.queryGroupList();
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



	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onFragment31Update(Fragment31Event event) {
		if (adapter != null) {
			initData();
		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void refreshData() {
		LogUtils.e("-------------", "313131");
		if (adapter != null) {
			initData();
		}
	}

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
