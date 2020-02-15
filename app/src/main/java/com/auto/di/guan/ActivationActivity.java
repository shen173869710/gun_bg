package com.auto.di.guan;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.basemodel.presenter.LoginPresenter;
import com.auto.di.guan.basemodel.view.ILoginView;
import com.auto.di.guan.entity.ElecEvent;
import com.auto.di.guan.view.XEditText;
import com.trello.rxlifecycle3.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
/**
 * 设备激活
 */
public class ActivationActivity extends IBaseActivity<LoginPresenter> implements ILoginView{

	@BindView(R.id.login_name)
	XEditText loginName;
	@BindView(R.id.activiation)
	Button activiation;

	@Override
	protected int setLayout() {
		return R.layout.activity_activation;
	}

	@Override
	protected void init() {

	}

	@Override
	protected void setListener() {
		activiation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String id = loginName.getText().toString().trim();
				if (id == null && TextUtils.isEmpty(id)) {
					Toast.makeText(ActivationActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
					return;
				}
				mPresenter.doDeviceActivation(id, "");
			}
		});
	}

	@Override
	protected LoginPresenter createPresenter() {
		return new LoginPresenter();
	}



	@Override
	public LifecycleTransformer bindLifecycle() {
		return null;
	}

	@Override
	public Activity getActivity() {
		return this;
	}


	@Override
	public void showDialog() {

	}

	@Override
	public void dismissDialog() {

	}

	@Override
	public void loginSuccess(BaseRespone respone) {

	}

	@Override
	public void loginFail(Throwable error, Integer code, String msg) {

	}

	@Override
	public void activationSuccess(BaseRespone respone) {

	}

	@Override
	public void activationFail(Throwable error, Integer code, String msg) {

	}


	@Subscribe(threadMode = ThreadMode.MAIN)
	public void onElecEvent(ElecEvent elecEvent) {

	}
}
