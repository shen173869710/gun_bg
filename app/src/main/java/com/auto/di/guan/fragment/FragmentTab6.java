package com.auto.di.guan.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.auto.di.guan.AddUserActivity;
import com.auto.di.guan.R;
import com.auto.di.guan.adapter.AddUserAdapter;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.sql.UserSql;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FragmentTab6 extends BaseFragment {
	private Button user_add;
	private ListView user_listview;
	private AddUserAdapter addUserAdapter;
	private View view;
	private List<User>users = new ArrayList<>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_6, null);
		user_add = (Button) view.findViewById(R.id.user_add);
		user_listview = (ListView) view.findViewById(R.id.user_listview);
		addUserAdapter = new AddUserAdapter(getActivity(), users);
		user_listview.setAdapter(addUserAdapter);
		user_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().startActivity(new Intent(getActivity(), AddUserActivity.class));
			}
		});
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		 List<User> userList = UserSql.queryUserList();
		if (userList != null && userList.size() > 0) {
			users.clear();
			users.addAll(userList);
			if (addUserAdapter != null) {
				addUserAdapter.notifyDataSetChanged();
			}

		}
	}
}
