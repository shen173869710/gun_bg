package com.auto.di.guan.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.db.PersonInfo;
import com.auto.di.guan.db.User;


public class MainChooseAdapter extends BaseAdapter {

	private Context context;
	private List<User> users;
	public int selectedIndex = -1;

	public MainChooseAdapter(Context ctx, List<User> trucks) {
		this.context = ctx;
		this.users = trucks;
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vHolder = null;
		if (convertView == null) {
			vHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.main_choose_list_item, null);
			vHolder.main_choose_name = (TextView) convertView.findViewById(R.id.mian_choose_name);
			vHolder.main_choose_check = (TextView) convertView.findViewById(R.id.main_choose_check);
			convertView.setTag(vHolder);
		} else {
			vHolder = (ViewHolder) convertView.getTag();
		}
		if (selectedIndex == position) {
			vHolder.main_choose_check.setBackgroundResource(R.drawable.img_selected);
		}else {
			vHolder.main_choose_check.setBackgroundResource(R.drawable.img_unselected);
		}
		vHolder.main_choose_name.setText(users.get(position).getName());
		return convertView;
	}

	public int getSelectedIndex() {
		return selectedIndex;
	}



	public static class ViewHolder {
		public TextView main_choose_name;
		public TextView main_choose_check;
	}
}
