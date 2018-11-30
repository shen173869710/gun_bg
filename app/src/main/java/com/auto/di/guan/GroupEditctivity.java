package com.auto.di.guan;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.auto.di.guan.adapter.GroupEditListAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.dialog.SureLoadDialog;
import com.auto.di.guan.utils.ShareUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**

 */
public class GroupEditctivity extends Activity {
	private Button group_edit_add;
	private View view;
	private TextView textView;
	private ListView group_edit_listview;
	private GroupEditListAdapter adapter;
	private int groupId;
	private List<ControlInfo> controls = new ArrayList<>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group_layout);
		view = findViewById(R.id.title_bar);

		textView = (TextView)view.findViewById(R.id.title_bar_title);
		textView.setText("编辑用户组");
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
//				startActivity(new Intent(GroupEditctivity.this, ChooseGroupctivity.class));

				Intent intent = new Intent(GroupEditctivity.this, ChooseGroupctivity.class);
				intent.putExtra("groupId", groupId);
				startActivity(intent);
				GroupEditctivity.this.finish();
			}
		});
		controls = DBManager.getInstance(this).queryControlList(groupId);
		group_edit_listview = (ListView)findViewById(R.id.group_edit_listview);
		adapter = new GroupEditListAdapter(this, controls);
		group_edit_listview.setAdapter(adapter);
	}
}
