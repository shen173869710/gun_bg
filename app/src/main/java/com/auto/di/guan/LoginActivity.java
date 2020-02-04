package com.auto.di.guan;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.User;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mUserNameEditText;// 用户名
    private EditText mPasswordEditText;// 密码
    private Button mConfirmLoginButton;// 登录
    private Button user_root;
    private int type;
    private List<User> users= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mUserNameEditText = (EditText) findViewById(R.id.user_login_name);// 用户名
        mPasswordEditText = (EditText) findViewById(R.id.user_login_pwd);// 密码
        mUserNameEditText.setText("123456");
        mPasswordEditText.setText("123456");
        // 登录
        mConfirmLoginButton = (Button) findViewById(R.id.user_login);
        user_root = (Button) findViewById(R.id.user_root);

        users = DBManager.getInstance(LoginActivity.this).queryUserList();
        if (users.size() == 0) {
            user_root.setVisibility(View.VISIBLE);
        }else {
            user_root.setVisibility(View.GONE);
        }

        mConfirmLoginButton.setOnClickListener(this);
        user_root.setOnClickListener(this);
        findViewById(R.id.text_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ConsoleActivity.class));
            }
        });
    }
    private void go2MainPage(){
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {

//        startActivity(new Intent(LoginActivity.this, ConsoleActivity.class));
        String id = mUserNameEditText.getText().toString().trim();
        if (id == null && TextUtils.isEmpty(id)) {
            Toast.makeText(LoginActivity.this, "请输入账号", Toast.LENGTH_LONG).show();
            return;
        }
        String pwd = mPasswordEditText.getText().toString().trim();
        if (pwd== null && TextUtils.isEmpty(pwd)) {
            Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
            return;
        }


        if (v.getId() == R.id.user_login) {

            int size = users.size();
            for (int i= 0; i < size; i++) {
                if (id.equals(users.get(i).getAccount()) && pwd.equals(users.get(i).getPassword())) {
                    MyApplication.user = users.get(i);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    LoginActivity.this.finish();
                    return;
                }else {
                }
            }


        }else if (v.getId() == R.id.user_root) {
            User user = new User();
            user.setAccount(id);
            user.setPassword(pwd);
            user.setLevel(9999);
            user.setName("超级用户");
            DBManager.getInstance(LoginActivity.this).insertUser(user);
            Toast.makeText(LoginActivity.this, "插入成功", Toast.LENGTH_LONG).show();
            users = DBManager.getInstance(LoginActivity.this).queryUserList();
            if (users.size() == 0) {
                user_root.setVisibility(View.VISIBLE);
            }else {
                user_root.setVisibility(View.GONE);
            }
        }
    }
}
