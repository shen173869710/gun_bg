package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.MyGridOpenAdapter;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.event.Fragment4Event;
import com.auto.di.guan.utils.LogUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.util.ArrayList;
import java.util.List;
/**
 */
public class FragmentTab4 extends BaseFragment {
	private GridView mGridView;
	private View view;
	private MyGridOpenAdapter adapter;
	private List<DeviceInfo> deviceInfos = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_0, null);
		mGridView = (GridView) view.findViewById(R.id.fragment_0_gridview);
		deviceInfos = DeviceInfoSql.queryDeviceList();
		adapter = new MyGridOpenAdapter(getActivity(), deviceInfos);
		mGridView.setAdapter(adapter);
		mGridView.setNumColumns(Entiy.GRID_COLUMNS);
		EventBus.getDefault().register(this);
		return view;
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onFragment4Update(Fragment4Event event) {
			if (adapter != null) {
				adapter.setData(DeviceInfoSql.queryDeviceList());
			}
	};

	@Override
	public void refreshData() {
		LogUtils.e("-------------", "444444");
		deviceInfos = DeviceInfoSql.queryDeviceList();
		if (adapter != null) {
			adapter.setData(deviceInfos);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

}
