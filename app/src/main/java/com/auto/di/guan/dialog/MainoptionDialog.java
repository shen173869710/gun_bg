package com.auto.di.guan.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.R;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.utils.ShareUtil;


public class MainoptionDialog extends Dialog {

	public Button main_option_status;
	public Button main_option_open;
	public Button main_option_close;
	public TextView main_option_title;

	public TextView main_option_1;
	public TextView main_option_2;
	public TextView main_option_3;
	public TextView main_option_4;
	public TextView main_option_5;

	public MainoptionDialog(Context context) {
		super(context, R.style.UpdateDialog);
		setCustomView();
	}

	public MainoptionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, R.style.UpdateDialog);
		this.setCancelable(cancelable);
		this.setOnCancelListener(cancelListener);
		setCustomView();
	}

	public MainoptionDialog(Context context, int theme) {
		super(context, R.style.UpdateDialog);
		setCustomView();
	}

	private void setCustomView() {
		View mView = LayoutInflater.from(getContext()).inflate(R.layout.main_option_dialog, null);
		main_option_status = (Button) mView.findViewById(R.id.main_option_status);
		main_option_open = (Button) mView.findViewById(R.id.main_option_open);
		main_option_close = (Button) mView.findViewById(R.id.main_option_close);
		main_option_title = (TextView) mView.findViewById(R.id.main_option_title);

		main_option_1 = mView.findViewById(R.id.main_option_1);
		main_option_2 = mView.findViewById(R.id.main_option_2);
		main_option_3 = mView.findViewById(R.id.main_option_3);
		main_option_4 = mView.findViewById(R.id.main_option_4);
		main_option_5 = mView.findViewById(R.id.main_option_5);
		mView.setBackgroundResource(R.drawable.bg_d_rect_border);
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



	public static void ShowDialog(final Activity context, ControlInfo info, String title, String status,final ItemClick listener) {
		final MainoptionDialog dialog = new MainoptionDialog(context);
		dialog.main_option_title.setText(title);
		dialog.main_option_status.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onItemClick(0);
				dialog.dismiss();
			}
		});
		dialog.main_option_open.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onItemClick(1);
				dialog.dismiss();
			}
		});
		dialog.main_option_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onItemClick(2);
				dialog.dismiss();
			}
		});

		dialog.main_option_1.setText(info.getValve_name());
		dialog.main_option_2.setText(info.getValve_alias());
		dialog.main_option_3.setText(info.getValve_id()+"");
		dialog.main_option_4.setText(""+ BaseApp.getUser().getProjectName());
		dialog.main_option_5.setText(status);
		LayoutParams lay = dialog.getWindow().getAttributes();
		DisplayMetrics dm = new DisplayMetrics();// 获取屏幕分辨率
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);//
		Rect rect = new Rect();
		View view = context.getWindow().getDecorView();
		view.getWindowVisibleDisplayFrame(rect);
		lay.width = dm.widthPixels * 9 / 10;
		dialog.show();
	}

	public interface ItemClick {
		public void  onItemClick(int index);
	}
}
