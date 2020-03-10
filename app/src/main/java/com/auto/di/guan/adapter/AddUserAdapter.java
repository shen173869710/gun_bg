package com.auto.di.guan.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.auto.di.guan.AddUserActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.sql.UserSql;
import com.auto.di.guan.dialog.MainShowDialog;
import com.auto.di.guan.utils.NoFastClickUtils;

import java.util.List;


public class AddUserAdapter extends BaseAdapter {
	private List<User> users;
	private Context ctx;

	public AddUserAdapter(Context ctx, List<User> users) {
		this.ctx = ctx;
		this.users = users;
	}

	@Override
	public int getCount() {
		return users.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vHolder = null;
		if (convertView == null) {
			vHolder = new ViewHolder();
			convertView = LayoutInflater.from(ctx).inflate(
					R.layout.add_user_list_item, null);
			vHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
			vHolder.user_level = (TextView) convertView.findViewById(R.id.user_level);
			vHolder.user_del = (TextView) convertView.findViewById(R.id.user_del);
			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

		bindView(position, vHolder);
		return convertView;
	}

	private void bindView(final int position, final ViewHolder viewHolder) {
		final User user = users.get(position);
		viewHolder.user_name.setText(user.getUserName());
		viewHolder.user_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				MainShowDialog.ShowDialog((Activity) ctx, "删除用户","是否删除当前用户", new View.OnClickListener(){
					@Override
					public void onClick(View v) {
						UserSql.deleteUser(user);
						users.remove(position);
						notifyDataSetChanged();
					}
				});
			}
		});

		viewHolder.user_level.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(NoFastClickUtils.isFastClick()){
					return;
				}
				Intent intent = new Intent(ctx, AddUserActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("user", user);
				intent.putExtras(bundle);
				ctx.startActivity(intent);
			}
		});
	}

	class ViewHolder {
		TextView user_name;
		TextView user_level;
		TextView user_del;
	}

}
