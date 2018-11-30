package com.auto.di.guan.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.db.UserAction;
import com.auto.di.guan.utils.DateUtils;

import java.util.List;


public class QuareUserAdapter extends BaseAdapter {
	private List<UserAction> userActions;
	private Context ctx;

	public QuareUserAdapter(Context ctx, List<UserAction> users) {
		this.ctx = ctx;
		this.userActions = users;
	}

	@Override
	public int getCount() {
		return userActions.size();
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
					R.layout.quare_user_list_item, null);

			vHolder.quare_user_name = (TextView) convertView.findViewById(R.id.quare_user_name);
			vHolder.quare_user_end = (TextView) convertView.findViewById(R.id.quare_user_desc);
			vHolder.quare_user_end =  (TextView) convertView.findViewById(R.id.quare_user_end);
			vHolder.quare_user_time = (TextView) convertView.findViewById(R.id.quare_user_time);
			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}

		bindView(position, vHolder);
		return convertView;
	}

	private void bindView(final int position, final ViewHolder vHolder) {
		UserAction action = userActions.get(position);
		vHolder.quare_user_name.setText(action.getUserName()+"");
		vHolder.quare_user_action.setText(action.getActionDesc()+"");
		vHolder.quare_user_end.setText(action.getActionEnd()+"");
		vHolder.quare_user_time.setText(DateUtils.times(action.getTime()));
	}

	class ViewHolder {
		TextView quare_user_time;
		TextView quare_user_name;
		TextView quare_user_action;
		TextView quare_user_end;
	}

}
