package com.auto.di.guan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.adapter.RecyclerListAdapter;
import com.auto.di.guan.db.GroupInfo;
import com.auto.di.guan.db.sql.GroupInfoSql;
import com.auto.di.guan.jobqueue.event.Fragment32Event;
import com.auto.di.guan.utils.NoFastClickUtils;
import com.auto.di.guan.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**

 */
public class GroupOptionActivity extends Activity  {
	private View view;
	private TextView textView;
	private TextView title_bar_status;
	private RecyclerView recyclerView;
	private List<GroupInfo> groupInfos = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_option_layout);
		view = findViewById(R.id.title_bar);

		groupInfos = GroupInfoSql.queryGroupList();
		textView = (TextView)view.findViewById(R.id.title_bar_title);
		textView.setText("自动轮灌设置");
		title_bar_status  = (TextView)view.findViewById(R.id.title_bar_status);
		title_bar_status.setVisibility(View.VISIBLE);
		title_bar_status.setText("保存设置");
//		for (int i = 0; i < groupInfos.size(); i++) {
//			groupInfos.get(i).setGroupRunTime(0);
//			groupInfos.get(i).setGroupTime(0);
//			groupInfos.get(i).setGroupLevel(0);
//		}
//		GroupInfoSql.updateGroupList(groupInfos);
		title_bar_status.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				int size = groupInfos.size();
				HashMap<Integer, Integer> lv = new HashMap<>();
				for (int i = 0; i < size; i++) {
					GroupInfo groupInfo = groupInfos.get(i);
					int level = groupInfo.getGroupLevel();
					int time = groupInfo.getGroupTime();

					boolean isJoin = groupInfo.getGroupIsJoin();
					if (isJoin) {
//                        if (time < (1 * Entiy.RUN_TIME)) {
//                            ToastUtils.showLongToast("参与轮灌的组,轮灌时间不能小于3分钟");
//                            return;
//                        }
                    }
					if (lv.containsKey(level)) {
						ToastUtils.showLongToast("不能设置相同的轮灌优先级,或者优先级不能为空");
						return;
					}
					lv.put(level,level);
				}
				GroupInfoSql.updateGroupList(groupInfos);
				EventBus.getDefault().post(new Fragment32Event());
				GroupOptionActivity.this.finish();
			}
		});
		view.findViewById(R.id.title_bar_back_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GroupOptionActivity.this.finish();
			}
		});
		recyclerView = (RecyclerView) findViewById(R.id.group_option_view);
		RecyclerListAdapter adapter = new RecyclerListAdapter(groupInfos);
		recyclerView.setHasFixedSize(true);
		recyclerView.setAdapter(adapter);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));

	}


}
