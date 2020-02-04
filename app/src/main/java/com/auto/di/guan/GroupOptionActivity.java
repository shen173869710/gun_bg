package com.auto.di.guan;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.adapter.RecyclerListAdapter;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.GroupInfo;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class GroupOptionActivity extends Activity  {
	private View view;
	private TextView textView;
	private TextView title_bar_status;
	private ItemTouchHelper mItemTouchHelper;
	private RecyclerView recyclerView;
	private List<GroupInfo> groupInfos = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_option_layout);
		view = findViewById(R.id.title_bar);

		groupInfos = DBManager.getInstance(this).queryGrouplList();
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
		DBManager.getInstance(GroupOptionActivity.this).updateGroupList(groupInfos);
		title_bar_status.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DBManager.getInstance(GroupOptionActivity.this).updateGroupList(groupInfos);
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
