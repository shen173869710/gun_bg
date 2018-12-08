package com.auto.di.guan;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.dialog.MainShowDialog;
import com.auto.di.guan.dialog.SureLoadDialog;
import com.auto.di.guan.dialog.WaitingDialog;
import com.auto.di.guan.entity.BindEvent;
import com.auto.di.guan.entity.ControlOptionEvent;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.entity.ImageStatus;
import com.auto.di.guan.entity.ReadEvent;
import com.auto.di.guan.utils.HttpUtil;
import com.auto.di.guan.utils.ShareUtil;
import com.auto.di.guan.utils.Task;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.auto.di.guan.R.id.info;

/**
 */
public class ControlBindActivity extends FragmentActivity implements View.OnClickListener,IUrlRequestCallBack{
	private View view;
	private TextView textView;
	private DeviceInfo info;

	private View bind_deivce_item;
	private View bind_group_item;
	private View bind_control_title_1;
	private View bind_control_id_1;
	private EditText bind_control_name_1;
	private CheckBox bind_control_sel_1;

	private View bind_control_title_2;
	private View bind_control_id_2;
	private EditText bind_control_name_2;
	private CheckBox bind_control_sel_2;

	private String text1;
	private String text2;

	private Button bind_deivce_id;
	private Button bind_deivce_group_id;
	private Button bind_cantrol_save;

	private List<ControlInfo> controlInfos;

	private String groupName;
	private WaitingDialog dialog;

	/***是否写入项目ID**/
	private boolean isPeroJectId;
	/***是否写入组ID**/
	private boolean isGroupId;
	private boolean isGroupClick;
	/*** 是否是第一个控制阀写入****/
	private int controlOnclick = 0;

	private View bind_deivce_name;
	private EditText bind_deivce_name_edit;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_control_bind);
		dialog = new WaitingDialog(this, R.style.dialog);
		isPeroJectId = false;
		isGroupId = false;
		init();
		setListener();
	}

	protected void init() {
        EventBus.getDefault().register(this);
		controlInfos = DBManager.getInstance(this).queryControlList();
		view = findViewById(R.id.title_bar);
		textView = (TextView)view.findViewById(R.id.title_bar_title);
		textView.setText("绑定阀门");
		view.findViewById(R.id.title_bar_back_layout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		info = (DeviceInfo) getIntent().getSerializableExtra("info");


		bind_deivce_name = findViewById(R.id.bind_deivce_name);
		bind_deivce_name_edit = (EditText) bind_deivce_name.findViewById(R.id.item_desc);
		((TextView)(bind_deivce_name.findViewById(R.id.item_title))).setText("设备名称");
		bind_deivce_item = findViewById(R.id.bind_deivce_item);
		((TextView)(bind_deivce_item.findViewById(R.id.item_title))).setText("项目ID");
		groupName = ShareUtil.getStringLocalValue(this,Entiy.GROUP_NAME);
		((TextView)(bind_deivce_item.findViewById(R.id.item_desc))).setText(groupName+"");

		bind_group_item = findViewById(R.id.bind_group_item);
		((TextView)(bind_group_item.findViewById(R.id.item_title))).setText("阀控器ID");
		((TextView)(bind_group_item.findViewById(R.id.item_desc))).setText(info.getDeviceId()+"");

		bind_control_title_1 = findViewById(R.id.bind_control_title_1);
		bind_control_id_1 = findViewById(R.id.bind_control_id_1);
		((TextView)(bind_control_title_1.findViewById(R.id.item_title))).setText("阀门");
		((TextView)(bind_control_title_1.findViewById(R.id.item_desc))).setText("1");
		((TextView)(bind_control_id_1.findViewById(R.id.item_title))).setText("阀门编号");
//		((EditText)(bind_control_id_1.findViewById(R.id.item_desc))).setText(Entiy.getBid(info.getDeviceId()+"")+"-"+"1");
		bind_control_name_1 = ((EditText)bind_control_id_1.findViewById(R.id.item_desc));
		bind_control_sel_1 = (CheckBox) findViewById(R.id.bind_control_sel_1);

		bind_control_title_2 = findViewById(R.id.bind_control_title_2);
		bind_control_id_2 = findViewById(R.id.bind_control_id_2);
		((TextView)(bind_control_title_2.findViewById(R.id.item_title))).setText("阀门");
		((TextView)(bind_control_title_2.findViewById(R.id.item_desc))).setText("2");
		((TextView)(bind_control_id_2.findViewById(R.id.item_title))).setText("阀门编号");
		bind_control_name_2 = ((EditText)(bind_control_id_2.findViewById(R.id.item_desc)));
		bind_control_sel_2 = (CheckBox) findViewById(R.id.bind_control_sel_2);

//
//		if (info.controlInfos.get(0).controId != 0) {
//			editText1.setText(""+info.controlInfos.get(0).controId);
//		}
//		if (info.controlInfos.get(1).controId != 0) {
//			editText2.setText(""+info.controlInfos.get(1).controId);
//		}

		bind_cantrol_save = (Button) findViewById(R.id.bind_cantrol_save);
		bind_deivce_id = (Button) findViewById(R.id.bind_deivce_id);
		bind_deivce_group_id = (Button) findViewById(R.id.bind_deivce_control_id);

		bind_deivce_name_edit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (!TextUtils.isEmpty(s)) {
					bind_control_name_1.setText(s + "-1");
					bind_control_name_2.setText(s + "-2");
				}
			}
		});
	}


	protected void setListener() {
		bind_cantrol_save.setOnClickListener(this);
		bind_deivce_id.setOnClickListener(this);
		bind_deivce_group_id.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bind_cantrol_save:
				String controlName1 =bind_control_name_1.getText().toString().trim();
				String controlName2 =bind_control_name_2.getText().toString().trim();
				String deviceName = bind_deivce_name_edit.getText().toString().trim();

				if (TextUtils.isEmpty(deviceName)) {
					showToastLongMsg("请输入设备的名称");
					return;
				}

				if (TextUtils.isEmpty(controlName1)) {
					showToastLongMsg("请输入控制阀 1 的名称");
					return;
				}
				if (TextUtils.isEmpty(controlName2)) {
					showToastLongMsg("请输入控制阀 2 的名称");
					return;
				}

				if (!isPeroJectId) {
					showToastLongMsg("项目ID未写入");
					return;
				}
				if (!isGroupId) {
					showToastLongMsg("组ID未写入");
					return;
				}
				info.setDeviceName(deviceName);
				if (bind_control_sel_1.isChecked()) {
					info.controlInfos.get(0).imageId = R.mipmap.lighe_1;
					info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿CONNECT;
					info.controlInfos.get(0).controId = Integer.valueOf(info.deviceId)*2-1;
					info.controlInfos.get(0).deviceId = Entiy.getBid(info.getDeviceId()+"");
					info.controlInfos.get(0).controlName = controlName1;
					DBManager.getInstance(ControlBindActivity.this).updateDevice(info);
				}else {
					info.controlInfos.get(0).imageId = 0;
					info.controlInfos.get(0).status = 0;
					info.controlInfos.get(0).controId = 0;
				}
				if (bind_control_sel_2.isChecked()) {
					info.controlInfos.get(1).imageId = R.mipmap.lighe_1;
					info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿CONNECT;
					info.controlInfos.get(1).controId = Integer.valueOf(info.deviceId)*2;
					info.controlInfos.get(1).deviceId = Entiy.getBid(info.getDeviceId()+"");
					info.controlInfos.get(1).controlName = controlName2;
					DBManager.getInstance(ControlBindActivity.this).updateDevice(info);
				}else {
					info.controlInfos.get(1).imageId = 0;
					info.controlInfos.get(1).status = 0;
					info.controlInfos.get(1).controId = 0;
				}
				finish();
			    break;
			case R.id.bind_deivce_id:
//				try {
					isGroupClick = true;
//					mOutputStream.write(new String(Entiy.writeGid(groupName)).getBytes());
//					mOutputStream.write('\n');
					EventBus.getDefault().post(new ReadEvent(Entiy.writeGid(groupName),0));
					if(dialog != null && !dialog.isShowing()) {
						dialog.show();
					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				break;
			case R.id.bind_deivce_control_id:
//				try {
					isGroupClick = false;
//					mOutputStream.write(new String(Entiy.writeBid(info.getDeviceId()+"")).getBytes());
//					mOutputStream.write('\n');
				EventBus.getDefault().post(new ReadEvent(Entiy.writeBid(info.getDeviceId()+""),1));
					if(dialog != null && !dialog.isShowing()) {
						dialog.show();
					}
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				break;
		}
	}

	/**
	 * 显示字符串类型数据
	 *
	 * @param msg
	 */
	public void showToastLongMsg(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private boolean checkId(int id) {
		controlInfos = DBManager.getInstance(this).queryControlList();
		int size = controlInfos.size();
		for (int i = 0; i < size; i++) {
			if (controlInfos.get(i).controId == id) {
				return true;
			}
		}
		return false;
	}

	public void doTask(int id) {
		Task task = new Task();
        task.setId(id);
        HttpUtil.getInstance().doHttpTask(this,task,this);
	}

	@Override
	public void urlRequestStart(Task result) {

	}

	@Override
	public void urlRequestEnd(Task result) {

		if (result.getId() == 1) {
			info.controlInfos.get(0).imageId = R.mipmap.lighe_1;
			info.controlInfos.get(0).status = Entiy.CONTROL_STATUS＿CONNECT;
			info.controlInfos.get(0).controId = Integer.valueOf(text1);
			info.controlInfos.get(0).deviceId = Entiy.getBid(info.getDeviceId()+"");
			DBManager.getInstance(ControlBindActivity.this).updateDevice(info);
			showToastLongMsg("写入成功");
		}else if (result.getId() == 2) {
			info.controlInfos.get(1).imageId = R.mipmap.lighe_1;
			info.controlInfos.get(1).status = Entiy.CONTROL_STATUS＿CONNECT;
			info.controlInfos.get(1).controId = Integer.valueOf(text2);
			info.controlInfos.get(1).deviceId = Entiy.getBid(info.getDeviceId()+"");
			DBManager.getInstance(ControlBindActivity.this).updateDevice(info);
			showToastLongMsg("写入成功");
		}
	}

	@Override
	public void urlRequestException(Task result) {

	}

//	@Override
//	protected void onDataReceived(byte[] buffer, int size) {
//		final String receive = new String(buffer, 0, size);
//		if (receive != null && !TextUtils.isEmpty(receive)) {
//			if (receive.contains("ok") && isGroupClick) {
//				isPeroJectId = true;
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						showToastLongMsg("写入项目ID成功");
//					}
//				});
//
//
//
//			}else if (receive.contains("ok")) {
//				isGroupId = true;
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						showToastLongMsg("写入组ID成功");;
//					}
//				});
//			}else {
//				runOnUiThread(new Runnable() {
//					@Override
//					public void run() {
//						showToastLongMsg("写入失败");
//					}
//				});
//			}
//		}else {
//			runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					showToastLongMsg("写入失败");
//				}
//			});
//
//		}
//		if(dialog != null) {
//			dialog.dismiss();
//		}
//	}


	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onControlStatusEvent(BindEvent event) {
		String receive = event.status;
		if (receive != null && !TextUtils.isEmpty(receive)) {
			if (receive.contains("gid") && isGroupClick) {
				isPeroJectId = true;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showToastLongMsg("写入项目ID成功");
					}
				});
			}else if (receive.contains("bid")) {
				isGroupId = true;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showToastLongMsg("写入组ID成功");;
					}
				});
			}else {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						showToastLongMsg("写入失败");
					}
				});
			}
		}else {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					showToastLongMsg("写入失败");
				}
			});

		}
		if(dialog != null ) {
			dialog.dismiss();
		}
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
