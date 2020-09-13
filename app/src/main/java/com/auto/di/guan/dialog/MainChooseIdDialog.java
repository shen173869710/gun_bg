package com.auto.di.guan.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.auto.di.guan.R;
import com.auto.di.guan.adapter.MainChooseAdapter;
import com.auto.di.guan.adapter.MainChooseIdAdapter;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.User;

import java.util.List;


public class MainChooseIdDialog extends Dialog {

	private Button main_choose_cancle;
	private Button main_choose_sure;
	private TextView main_custom_msg;
	private TextView main_choose_title;
	private ListView main_choose_listview;
	private MainChooseIdAdapter main_choose_adapter;
	private Context context;
	private List<ControlInfo> users;
	public int currentItem = -1;

	public MainChooseIdDialog(Context context, List<ControlInfo> trucks) {
		super(context, R.style.UpdateDialog);
		this.context = context;
		this.users = trucks;
		setCustomView();
	}
	public void setDialogTitle(String title){
		main_choose_title.setText(title);
	}
	public MainChooseIdDialog(Context context, boolean cancelable,
                              OnCancelListener cancelListener) {
		super(context, R.style.UpdateDialog);
		this.setCancelable(cancelable);
		this.setOnCancelListener(cancelListener);
		this.context = context;
		setCustomView();
	}

	public MainChooseIdDialog(Context context, int theme) {
		super(context, R.style.UpdateDialog);
		this.context = context;
		setCustomView();
	}

	public int getSelectedIndex() {
		return main_choose_adapter.getSelectedIndex();
	}

	private void setCustomView() {
		View mView = LayoutInflater.from(getContext()).inflate(
				R.layout.main_choose_dialog, null);
		main_choose_cancle = (Button) mView
				.findViewById(R.id.main_choose_cancle);
		main_choose_sure = (Button) mView.findViewById(R.id.main_choose_sure);
		main_custom_msg = (TextView) mView.findViewById(R.id.main_custom_msg);
		main_choose_title = (TextView) mView
				.findViewById(R.id.main_choose_title);
		main_choose_listview = (ListView) mView
				.findViewById(R.id.main_choose_listview);
		main_choose_adapter = new MainChooseIdAdapter(context, users);
		main_choose_listview.setAdapter(main_choose_adapter);
		mView.setBackgroundResource(R.drawable.bg_d_rect_border);
		main_choose_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				currentItem = position;
				main_choose_adapter.selectedIndex = position;
				main_choose_adapter.notifyDataSetChanged();
			}
		});
		super.setContentView(mView);
	}

	@Override
	public void setCanceledOnTouchOutside(boolean cancel) {
		super.setCanceledOnTouchOutside(cancel);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
	}

	public void setOnPositiveListener(View.OnClickListener listener) {
		main_choose_sure.setOnClickListener(listener);
	}

	public void setOnNegativeListener(View.OnClickListener listener) {
		main_choose_cancle.setOnClickListener(listener);
	}

}
