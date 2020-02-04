package com.auto.di.guan;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.auto.di.guan.basemodel.model.respone.BaseRespone;
import com.auto.di.guan.basemodel.presenter.LoginPresenter;
import com.auto.di.guan.basemodel.view.ILoginView;
import com.auto.di.guan.view.XEditText;
import com.trello.rxlifecycle3.LifecycleTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends IBaseActivity<LoginPresenter> implements ILoginView, View.OnClickListener {


    @BindView(R.id.user_login_name)
    XEditText userLoginName;
    @BindView(R.id.user_login_pwd)
    XEditText userLoginPwd;
    @BindView(R.id.user_login)
    Button userLogin;

    @Override
    protected int setLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {

    }

    @Override
    protected void setListener() {
        userLogin.setOnClickListener(this);
    }

    @Override
    protected LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    public void onClick(View v) {

        String id = userLoginName.getText().toString().trim();
        if (id == null && TextUtils.isEmpty(id)) {
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
            return;
        }
        String pwd = userLoginPwd.getText().toString().trim();
        if (pwd == null && TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
            return;
        }
        mPresenter.doLogin(id, pwd);
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public void loginSuccess(BaseRespone respone) {

    }

    @Override
    public void loginFail(Throwable error, Integer code, String msg) {

    }
}
