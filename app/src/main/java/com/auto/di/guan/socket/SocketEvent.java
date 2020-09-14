package com.auto.di.guan.socket;

public class SocketEvent {
    private int code;
    private String data;

    public SocketEvent(int code) {
        this.code = code;
    }


    public SocketEvent(int code, String data) {
        this.code = code;
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
