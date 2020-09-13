package com.auto.di.guan;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.di.guan.db.User;
import com.auto.di.guan.db.sql.UserSql;
import com.auto.di.guan.entity.Entiy;

import java.util.List;

/**

 */
public class OptionSettingActivity extends Activity implements View.OnClickListener{

	private EditText add_user_name;
	private EditText add_user_pwd;
	private EditText add_user_account;
	private LinearLayout add_user_choose_layout_1;
	private TextView add_user_choose_layout_1_tv;
	private LinearLayout add_user_choose_layout_2;
	private TextView add_user_choose_layout_2_tv;
	private LinearLayout add_user_choose_layout_3;
	private TextView add_user_choose_layout_3_tv;
	private Button add_user_add;
	private boolean item_1 = false;
	private boolean item_2 = false;
	private boolean item_3 = false;
	View view;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user);

		user = (User) getIntent().getSerializableExtra("user");
		if (user != null) {
			add_user_name.setText(user.getUserName());
			add_user_account.setText(user.getLoginName());
			add_user_pwd.setText(user.getPassword());
		}


		view = findViewById(R.id.title_bar);
		view.findViewById(R.id.title_bar_back_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		add_user_name = (EditText)findViewById(R.id.add_user_name);
		add_user_pwd = (EditText)findViewById(R.id.add_user_pwd);
		add_user_account = (EditText)findViewById(R.id.add_user_account);



		add_user_choose_layout_1 = (LinearLayout)findViewById(R.id.add_user_choose_layout_1);
		add_user_choose_layout_1_tv = (TextView) findViewById(R.id.add_user_choose_layout_1_tv);
		add_user_choose_layout_2 = (LinearLayout)findViewById(R.id.add_user_choose_layout_2);
		add_user_choose_layout_2_tv = (TextView) findViewById(R.id.add_user_choose_layout_2_tv);
		add_user_choose_layout_3 = (LinearLayout)findViewById(R.id.add_user_choose_layout_3);
		add_user_choose_layout_3_tv = (TextView) findViewById(R.id.add_user_choose_layout_3_tv);

		add_user_add = (Button)findViewById(R.id.add_user_add);

		add_user_choose_layout_1.setOnClickListener(this);
		add_user_choose_layout_2.setOnClickListener(this);
		add_user_choose_layout_3.setOnClickListener(this);
		add_user_add.setOnClickListener(this);
		user = new User();
	}


	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
			case R.id.add_user_choose_layout_1:
				item_1 = !item_1;
				if (item_1) {
					add_user_choose_layout_1_tv.setBackgroundResource(R.drawable.img_selected);
				}else {
					add_user_choose_layout_1_tv.setBackgroundResource(R.drawable.img_unselected);
				}
				break;
			case R.id.add_user_choose_layout_2:
				item_2 = !item_2;
				if (item_2) {
					add_user_choose_layout_2_tv.setBackgroundResource(R.drawable.img_selected);
				}else {
					add_user_choose_layout_2_tv.setBackgroundResource(R.drawable.img_unselected);
				}
				break;
			case R.id.add_user_choose_layout_3:
				item_3 = !item_3;
				if (item_3) {
					add_user_choose_layout_3_tv.setBackgroundResource(R.drawable.img_selected);
				}else {
					add_user_choose_layout_3_tv.setBackgroundResource(R.drawable.img_unselected);
				}
				break;
			case R.id.add_user_add:
				addUser();
				break;
		}
	}

	private void addUser() {
		String name = add_user_name.getText().toString();
		if (TextUtils.isEmpty(name)) {
			Toast.makeText(this, "用户名不能为空",Toast.LENGTH_LONG).show();
			return;
		}
		String account = add_user_account.getText().toString();
		if (TextUtils.isEmpty(account)) {
			Toast.makeText(this, "用户账号不能为空",Toast.LENGTH_LONG).show();
			return;
		}
		String pwd = add_user_pwd.getText().toString();
		if (TextUtils.isEmpty(pwd)) {
			Toast.makeText(this, "用户密码不能为空",Toast.LENGTH_LONG).show();
			return;
		}

		if (!item_1&& !item_2 && !item_3) {
			Toast.makeText(this, "请设置用户权限",Toast.LENGTH_LONG).show();
			return;
		}
		int level = 0;
		if (item_1) {
			level = Entiy.LEVEL_2;
		}
		if (item_2) {
			level = Entiy.LEVEL_3;
		}

		if (item_3) {
			level = Entiy.LEVEL_4;
		}

		if (item_1 && item_2) {
			level = Entiy.LEVEL_2_3;
		}
		if (item_1 && item_3) {
			level = Entiy.LEVEL_2_4;
		}
		if (item_2 && item_3) {
			level = Entiy.LEVEL_3_4;
		}

		if (item_2 && item_3 && item_1) {
			level = Entiy.LEVEL_2_3_4;
		}

		List<User>users = UserSql.queryUserList(account);
		if (users != null && users.size() != 0) {
			Toast.makeText(this, "用户账号已经存在",Toast.LENGTH_LONG).show();
			return;
		}
		UserSql.insertUser(user);
		finish();
	}

	private void showChoose(int i) {
		add_user_choose_layout_1_tv.setBackgroundResource(R.drawable.img_unselected);
		add_user_choose_layout_2_tv.setBackgroundResource(R.drawable.img_unselected);
		add_user_choose_layout_3_tv.setBackgroundResource(R.drawable.img_unselected);
		if (i == 0) {
			add_user_choose_layout_1_tv.setBackgroundResource(R.drawable.img_selected);
		}else if (i == 1) {
			add_user_choose_layout_2_tv.setBackgroundResource(R.drawable.img_selected);
		}else if (i == 2) {
			add_user_choose_layout_3_tv.setBackgroundResource(R.drawable.img_selected);
		}
	}
}
