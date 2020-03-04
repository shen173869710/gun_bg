package com.auto.di.guan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.auto.di.guan.adapter.GroupEditListAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.facebook.stetho.common.LogUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**

 */
public class GroupEditctivity extends Activity {
	private Button group_edit_add;
	private View view;
	private TextView textView;
	private RecyclerView group_edit_listview;
	private GroupEditListAdapter adapter;
	private int groupId;
	private List<ControlInfo> controls = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_layout);
		view = findViewById(R.id.title_bar);
		textView = (TextView)view.findViewById(R.id.title_bar_title);
		textView.setText("编辑阀门组");
		groupId = getIntent().getIntExtra("groupId", 0);
		view.findViewById(R.id.title_bar_back_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				GroupEditctivity.this.finish();
			}
		});

		group_edit_add = (Button)findViewById(R.id.group_edit_add);
		group_edit_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(GroupEditctivity.this, ChooseGroupctivity.class);
				intent.putExtra("groupId", groupId);
				startActivity(intent);
				GroupEditctivity.this.finish();
			}
		});
		controls = ControlInfoSql.queryControlList(groupId);
		group_edit_listview = (RecyclerView) findViewById(R.id.group_edit_listview);
		group_edit_listview.setLayoutManager(new LinearLayoutManager(this));
		adapter = new GroupEditListAdapter(controls);
		group_edit_listview.setAdapter(adapter);
	}
}
