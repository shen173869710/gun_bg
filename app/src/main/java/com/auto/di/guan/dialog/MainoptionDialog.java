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

import com.auto.di.guan.R;


public class MainoptionDialog extends Dialog {

	public Button main_option_status;
	public Button main_option_open;
	public Button main_option_close;
	public TextView main_option_title;

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



	public static void ShowDialog(final Activity context, String title, final ItemClick listener) {
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
