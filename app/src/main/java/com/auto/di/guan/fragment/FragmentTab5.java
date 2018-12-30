package com.auto.di.guan.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.QuareUserAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.UserAction;
import com.auto.di.guan.dialog.ChooseTimeDialog;
import com.auto.di.guan.dialog.MainChooseDialog;
import com.auto.di.guan.dialog.MainChooseIdDialog;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class FragmentTab5 extends BaseFragment {
	private TextView requeryByName, requeryByTime, requeryById;
	private View view;
	private ListView listView;
	private QuareUserAdapter adapter;
	private List<User>users = new ArrayList<>();
	private List<UserAction>userActions = new ArrayList<>();
	private List<ControlInfo>controlInfo = new ArrayList<>();
	int i= 0;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_5, null);
		requeryByName = (TextView) view.findViewById(R.id.querybyname);
		requeryByTime = (TextView) view.findViewById(R.id.querybytime);
		requeryById = (TextView) view.findViewById(R.id.querybyid);

		listView = (ListView) view.findViewById(R.id.querylistview);
		adapter = new QuareUserAdapter(getActivity(), userActions);
		listView.setAdapter(adapter);
		requeryByName.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showChooseDialog(requeryByName);
			}


		});
		requeryByTime.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showChooseTimeDialog(requeryByTime);
			}
		});

		requeryById.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showChooseIdDialog(requeryById);
			}
		});

		return view;
	}

	private void showChooseTimeDialog(final TextView tv) {
		 ChooseTimeDialog.ShowDialog(getActivity(), new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String time = v.getTag().toString();
				if (!TextUtils.isEmpty(time)) {
					showChooseListByTime(time);
					tv.setText(time);
				}
			}
		});
	}

	private void showChooseDialog(final TextView tv) {
		users = DBManager.getInstance(getActivity()).queryUserList();
		if (users == null && users.size() == 0) {
			Toast.makeText(getActivity(), "暂无可以查询的用户", Toast.LENGTH_LONG).show();
			return;
		}
		final  MainChooseDialog chooseDialog = new MainChooseDialog(getActivity(), users);
		chooseDialog.setOnPositiveListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (chooseDialog.currentItem >= 0) {
					String name = users.get(chooseDialog.currentItem).getName();
					tv.setText(name);
					showChooseListByName(chooseDialog.currentItem);
				}else {
					Toast.makeText(getActivity(), "暂无可以查询的信息", Toast.LENGTH_LONG).show();
				}
				chooseDialog.dismiss();
			}


		});
		chooseDialog.setOnNegativeListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseDialog.dismiss();
			}
		});
		chooseDialog.show();
	}


	private void showChooseIdDialog(final TextView tv) {
		List<ControlInfo>controlInfoList = new ArrayList<>();
		ControlInfo info1 = new ControlInfo();
		ControlInfo info2 = new ControlInfo();
		ControlInfo info3 = new ControlInfo();
		ControlInfo info4 = new ControlInfo();
		ControlInfo info5 = new ControlInfo();
		info1.controlName = "全部";
		info2.controlName = "只看手动单个轮灌";
		info3.controlName = "只看手动分组轮灌";
		info4.controlName = "只看自动分组轮灌";
		info5.controlName = "只看报警";
		controlInfoList.add(info1);
		controlInfoList.add(info2);
		controlInfoList.add(info3);
		controlInfoList.add(info4);
		controlInfoList.add(info5);
		final MainChooseIdDialog chooseDialog = new MainChooseIdDialog(getActivity(), controlInfoList);
		chooseDialog.setOnPositiveListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int type = 0;
				switch (chooseDialog.currentItem) {
					case 0:
						type = 0;
						break;
					case 1:
						type = Entiy.ACTION_TYPE_4;
						break;
					case 2:
						type = Entiy.ACTION_TYPE_31;
						break;
					case 3:
						type = Entiy.ACTION_TYPE_32;
						break;
					case 4:
						type = Entiy.ACTION_TYPE_ERROR;
						break;
				}
				showListByType(type);
				chooseDialog.dismiss();
			}


		});
		chooseDialog.setOnNegativeListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				chooseDialog.dismiss();
			}
		});
		chooseDialog.show();
	}

	private void showChooseListByName(int postion) {
		List<UserAction>action = DBManager.getInstance(getActivity()).queryUserActionlList(users.get(postion).getAccount());
		showEnd(action);
	}

	private void showListByType(int type) {
		List<UserAction>action= new ArrayList<>();
		if (type == 0) {
			action	= DBManager.getInstance(getActivity()).queryUserActionlList(type);
		}else {
			action	= DBManager.getInstance(getActivity()).queryUserActionlListByType(type);
		}
		showEnd(action);
	}

	private void showChooseListByTime(String  time) {
		String []data = time.split("-");
		String start = data[0];
		String end = data[1];
		List<UserAction>action = DBManager.getInstance(getActivity()).queryUserActionlList(DateUtils.dataForlong(start), DateUtils.dataForlong(end));
		showEnd(action);
	}

	private void showEnd(List<UserAction>action) {
		if (action.size() == 0) {
			Toast.makeText(getActivity(), "暂无可以查询的信息", Toast.LENGTH_LONG).show();
			userActions.clear();
			adapter.notifyDataSetChanged();
		}else {
			userActions.clear();
			userActions.addAll(action);
			adapter.notifyDataSetChanged();
		}
	}

	private void showChooseListById(int id) {
		List<UserAction>action = DBManager.getInstance(getActivity()).queryUserActionlList(id);
		showEnd(action);
	}

}
