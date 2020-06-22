package com.auto.di.guan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.di.guan.adapter.ChooseGridAdapter;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.LevelInfo;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.db.sql.LevelInfoSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.event.ChooseGroupEvent;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ChooseGroupctivity extends Activity {
	private Button button;
	private GridView gridView;
	private ChooseGridAdapter adapter;
	private List<DeviceInfo> deviceInfos = new ArrayList<>();
	private GroupInfo groupInfo;
	private int groupId;
	View view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_group_layout);

		groupId = getIntent().getIntExtra("groupId", 0);

		view = findViewById(R.id.title_bar);
		((TextView)view.findViewById(R.id.title_bar_title)).setText("设备分组");
		view.findViewById(R.id.title_bar_back_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		button = (Button) findViewById(R.id.choose_ok);
		gridView = (GridView) findViewById(R.id.choose_gridview);
		deviceInfos = DeviceInfoSql.queryDeviceList();
		LogUtils.e("----", ""+(new Gson().toJson(deviceInfos)));
		adapter = new ChooseGridAdapter(this, deviceInfos);
		gridView.setAdapter(adapter);
		groupInfo = new GroupInfo();
		gridView.setNumColumns(Entiy.GRID_COLUMNS);
		gridView.setHorizontalSpacing(0);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		gridView.setLayoutParams(layoutParams);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				int count = 0;
				int size = deviceInfos.size();
				for (int i = 0; i < size; i++) {
					if (deviceInfos.get(i).getValveDeviceSwitchList().get(0).isSelect()) {
						count++;
					}
					if (deviceInfos.get(i).getValveDeviceSwitchList().get(1).isSelect()) {
						count++;
					}
				}
				if (count == 0) {
					Toast.makeText(ChooseGroupctivity.this, "没有选中设备",Toast.LENGTH_LONG).show();
					return;
				}

				List<LevelInfo>levelInfos = LevelInfoSql.queryUserLevelInfoListByGroup(false);
				if (levelInfos != null && levelInfos.size() > 0) {
					if (groupId == 0) {
						groupId = levelInfos.get(0).getLevelId();
						groupInfo.setGroupId(groupId);
						groupInfo.setGroupLevel(groupId);
						groupInfo.setGroupName(levelInfos.get(0).getLevelId()+"");
						levelInfos.get(0).setIsGroupUse(true);
						GroupInfoSql.insertGroup(groupInfo);
						LevelInfoSql.updateLevelInfo(levelInfos.get(0));
					}
					for (int i = 0; i < size; i++) {
						if (deviceInfos.get(i).getValveDeviceSwitchList().get(0).isSelect()) {
							deviceInfos.get(i).getValveDeviceSwitchList().get(0).setValve_group_id(groupId);
						}
						if (deviceInfos.get(i).getValveDeviceSwitchList().get(1).isSelect()) {
							deviceInfos.get(i).getValveDeviceSwitchList().get(1).setValve_group_id(groupId);
						}
					}
					DeviceInfoSql.updateDeviceList(deviceInfos);
					EventBus.getDefault().post(new ChooseGroupEvent());
					finish();
				}else  {
					Toast.makeText(ChooseGroupctivity.this, "没有可用的分组",Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
