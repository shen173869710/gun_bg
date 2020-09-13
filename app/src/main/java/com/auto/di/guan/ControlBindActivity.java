package com.auto.di.guan;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;

import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.User;
import com.auto.di.guan.db.sql.ControlInfoSql;
import com.auto.di.guan.db.sql.DeviceInfoSql;
import com.auto.di.guan.dialog.WaitingDialog;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.TaskEntiy;
import com.auto.di.guan.jobqueue.event.BindIdEvent;
import com.auto.di.guan.jobqueue.event.BindSucessEvent;
import com.auto.di.guan.jobqueue.task.TaskFactory;
import com.auto.di.guan.utils.LogUtils;
import com.auto.di.guan.utils.Task;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 *
 */
public class ControlBindActivity extends FragmentActivity implements View.OnClickListener, IUrlRequestCallBack {
    private View view;
    private TextView textView;
    private DeviceInfo info;

    private View bind_deivce_item;
    private View bind_group_item;


    private View bind_control_title_1;
    private View bind_control_id_1;
    private View bind_control_alias_1;
    private EditText bind_control_nick_1;
    private EditText bind_control_name_1;
    private CheckBox bind_control_sel_1;

    private View bind_control_title_2;
    private View bind_control_id_2;
    private View bind_control_alias_2;
    private EditText bind_control_nick_2;
    private EditText bind_control_name_2;
    private CheckBox bind_control_sel_2;

    private String text1;
    private String text2;

    private Button bind_deivce_id;
    private Button bind_deivce_group_id;
    private Button bind_cantrol_save;

    private List<ControlInfo> controlInfos;

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

    private User mUser;


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
        controlInfos = ControlInfoSql.queryControlList();
        mUser = BaseApp.getUser();
        view = findViewById(R.id.title_bar);
        textView = (TextView) view.findViewById(R.id.title_bar_title);
        textView.setText("绑定阀门");
        view.findViewById(R.id.title_bar_back_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new BindIdEvent());
                finish();
            }
        });
        info = (DeviceInfo) getIntent().getSerializableExtra("info");


        bind_deivce_name = findViewById(R.id.bind_deivce_name);
        bind_deivce_name_edit = (EditText) bind_deivce_name.findViewById(R.id.item_desc);
        ((TextView) (bind_deivce_name.findViewById(R.id.item_title))).setText("设备名称");
        bind_deivce_item = findViewById(R.id.bind_deivce_item);
        ((TextView) (bind_deivce_item.findViewById(R.id.item_title))).setText("项目ID");

        ((TextView) (bind_deivce_item.findViewById(R.id.item_desc))).setText(BaseApp.getUser().getProjectId() + "");

        bind_group_item = findViewById(R.id.bind_group_item);
        ((TextView) (bind_group_item.findViewById(R.id.item_title))).setText("阀控器ID");
        ((TextView) (bind_group_item.findViewById(R.id.item_desc))).setText(info.getProtocalId() + "");


        bind_control_title_1 = findViewById(R.id.bind_control_title_1);
        bind_control_id_1 = findViewById(R.id.bind_control_id_1);
        ((TextView) (bind_control_title_1.findViewById(R.id.item_title))).setText("阀门");
        ((TextView) (bind_control_title_1.findViewById(R.id.item_desc))).setText("1");
        ((TextView) (bind_control_id_1.findViewById(R.id.item_title))).setText("阀门编号");
//		((EditText)(bind_control_id_1.findViewById(R.id.item_desc))).setText(Entiy.getBid(info.getDeviceId()+"")+"-"+"1");
        bind_control_name_1 = ((EditText) bind_control_id_1.findViewById(R.id.item_desc));
        bind_control_sel_1 = (CheckBox) findViewById(R.id.bind_control_sel_1);

        bind_control_alias_1 = findViewById(R.id.bind_control_alias_1);
        ((TextView) (bind_control_alias_1.findViewById(R.id.item_title))).setText("阀门简称");
        bind_control_nick_1 = bind_control_alias_1.findViewById(R.id.item_desc);
        bind_control_alias_2 = findViewById(R.id.bind_control_alias_2);
        ((TextView) (bind_control_alias_2.findViewById(R.id.item_title))).setText("阀门简称");
        bind_control_nick_2 = bind_control_alias_2.findViewById(R.id.item_desc);


        bind_control_title_2 = findViewById(R.id.bind_control_title_2);
        bind_control_id_2 = findViewById(R.id.bind_control_id_2);
        ((TextView) (bind_control_title_2.findViewById(R.id.item_title))).setText("阀门");
        ((TextView) (bind_control_title_2.findViewById(R.id.item_desc))).setText("2");
        ((TextView) (bind_control_id_2.findViewById(R.id.item_title))).setText("阀门编号");
        bind_control_name_2 = ((EditText) (bind_control_id_2.findViewById(R.id.item_desc)));
        bind_control_sel_2 = (CheckBox) findViewById(R.id.bind_control_sel_2);


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
                    bind_control_name_1.setText(s + "_1");
                    bind_control_name_2.setText(s + "_2");
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
                String controlName1 = bind_control_name_1.getText().toString().trim();
                String controlName2 = bind_control_name_2.getText().toString().trim();
                String deviceName = bind_deivce_name_edit.getText().toString().trim();

                String nick1 = bind_control_nick_1.getText().toString().trim();
                String nick2 = bind_control_nick_2.getText().toString().trim();

                if (TextUtils.isEmpty(deviceName)) {
                    showToastLongMsg("请输入设备的名称");
                    return;
                }

                if (TextUtils.isEmpty(controlName1) && bind_control_sel_1.isChecked()) {
                    showToastLongMsg("请输入控制阀 1 的名称");
                    return;
                }
                if (TextUtils.isEmpty(controlName2) && bind_control_sel_2.isChecked()) {
                    showToastLongMsg("请输入控制阀 2 的名称");
                    return;
                }

                if (TextUtils.isEmpty(nick1) && bind_control_sel_1.isChecked()) {
                    showToastLongMsg("请输入控制阀 1 的别名");
                    return;
                }
                if (TextUtils.isEmpty(nick2) && bind_control_sel_2.isChecked()) {
                    showToastLongMsg("请输入控制阀 2 的别名");
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
                ControlInfo controlInfo_0 = info.getValveDeviceSwitchList().get(0);
                ControlInfo controlInfo_1 = info.getValveDeviceSwitchList().get(1);
                if (bind_control_sel_1.isChecked()) {
                    controlInfo_0.setValve_imgage_id(R.mipmap.lighe_1);
                    controlInfo_0.setValve_status(Entiy.DEVEICE_BIND);
                    controlInfo_0.setDevice_id(info.getDeviceId());
                    controlInfo_0.setValve_name(controlName1);
                    controlInfo_0.setValve_alias(nick1);
                    controlInfo_0.setValve_id(Integer.valueOf(info.getDeviceId()) * 2 - 1);
                    controlInfo_0.setProtocalId("0");
                    controlInfo_0.setDeviceProtocalId(info.getProtocalId());
//					controlInfo_0.imageId = R.mipmap.lighe_1;
//					controlInfo_0.status = Entiy.DEVEICE_BIND;
//					controlInfo_0.controId = Integer.valueOf(info.getDeviceId())*2-1;
//					controlInfo_0.deviceId = Entiy.getBid(info.getDeviceId()+"");
//					controlInfo_0.controlName = controlName1;
//					controlInfo_0.showName = info.getControl_1();
//					controlInfo_0.nickName = nick1;
//					controlInfo_0.bindId = info.getDeviceId();
                    DeviceInfoSql.updateDevice(info);
                } else {
                    controlInfo_0.setValve_imgage_id(0);
                    controlInfo_0.setValve_status(0);
                    controlInfo_0.setValve_id(0);
//					controlInfo_0.imageId = 0;
//					controlInfo_0.status = 0;
//					controlInfo_0.controId = 0;
                }
                if (bind_control_sel_2.isChecked()) {
                    controlInfo_1.setValve_imgage_id(R.mipmap.lighe_1);
                    controlInfo_1.setValve_status(Entiy.DEVEICE_BIND);
                    controlInfo_1.setDevice_id(info.getDeviceId());
                    controlInfo_1.setValve_name(controlName2);
                    controlInfo_1.setValve_alias(nick2);
                    controlInfo_1.setValve_id(Integer.valueOf(info.getDeviceId()) * 2);
                    controlInfo_1.setDeviceProtocalId(info.getProtocalId());
                    controlInfo_1.setProtocalId("1");
//					controlInfo_1.imageId = R.mipmap.lighe_1;
//					controlInfo_1.status = Entiy.DEVEICE_BIND;
//					controlInfo_1.controId = Integer.valueOf(info.getDeviceId())*2;
//					controlInfo_1.deviceId = Entiy.getBid(info.getDeviceId()+"");
//					controlInfo_1.controlName = controlName2;
//					controlInfo_1.showName = info.getControl_2();
//					controlInfo_1.nickName = nick2;
//					controlInfo_1.bindId = info.getDeviceId();
                    DeviceInfoSql.updateDevice(info);
                } else {
                    controlInfo_1.setValve_imgage_id(0);
                    controlInfo_1.setValve_status(0);
                    controlInfo_1.setValve_id(0);

                }
                EventBus.getDefault().post(new BindIdEvent());
                finish();
                break;
            case R.id.bind_deivce_id:
                isGroupClick = true;
                if(dialog != null && !dialog.isShowing()) {
                    dialog.show();
                }
                TaskFactory.createGidTak();
//				EventBus.getDefault().post(new ReadEvent(Entiy.writeGid(groupName),0));
//                BindEvent event = new BindEvent("ok");
//                onControlStatusEvent(event);
                break;
            case R.id.bind_deivce_control_id:
                isGroupClick = false;
                TaskFactory.createBidTak(info.getProtocalId());
//				EventBus.getDefault().post(new ReadEvent(Entiy.writeBid(info.getDeviceId()+""),1));
//					if(dialog != null && !dialog.isShowing()) {
//						dialog.show();
//					}
//                BindEvent event1 = new BindEvent("ok");
//                onControlStatusEvent(event1);
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

    @Override
    public void urlRequestStart(Task result) {

    }

    @Override
    public void urlRequestEnd(Task result) {

    }

    @Override
    public void urlRequestException(Task result) {

    }



    /**
     *       设置项目GID正常
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBindSucessEvent(BindSucessEvent event) {
        LogUtils.e("bin--------d","event"+event.getType());
        if (dialog != null) {
            dialog.dismiss();
        }

        if (event == null) {
            return;
        }

        if (!event.isOk()) {
            showToastLongMsg("写入失败");
            return;
        }
        LogUtils.e("BaseTask == ", "onBindSucessEvent"+event.getType());
        if (event.getType() == TaskEntiy.TASK_READ_GID) {
            isPeroJectId = true;
//            showToastLongMsg("写入项目ID成功");
            Toast.makeText(ControlBindActivity.this, "写入项目ID成功",Toast.LENGTH_LONG).show();
            LogUtils.e("BaseTask == ", "写入项目ID成功");
        }else if (event.getType() == TaskEntiy.TASK_READ_BID) {
            isGroupId = true;
//            showToastLongMsg("写入组ID成功");
            Toast.makeText(ControlBindActivity.this, "写入组ID成功",Toast.LENGTH_LONG).show();
            LogUtils.e("BaseTask == ", "写入组ID成功");
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
