package com.auto.di.guan.basemodel.model.respone;

/**
 *   用户登录返回相关数据
 */
public class LoginRespone {

    private Integer adminId;
    private String client;
    private String ip;
    private Long loginTIme;
    private String token;

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Long getLoginTIme() {
        return loginTIme;
    }

    public void setLoginTIme(Long loginTIme) {
        this.loginTIme = loginTIme;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
