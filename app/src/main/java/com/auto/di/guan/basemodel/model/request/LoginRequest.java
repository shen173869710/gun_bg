package com.auto.di.guan.basemodel.model.request;

/**
 * 用户登录请求传递参数
 */

public class LoginRequest extends BaseRequest{
    public String id;
    public String username;
    public String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
