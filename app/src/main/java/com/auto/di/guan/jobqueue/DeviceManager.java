package com.auto.di.guan.jobqueue;


/**
 *      设备管理器
 */
public class DeviceManager {

    private static DeviceManager mDeviceManager= null;
    public static DeviceManager getInstance(){
        if (mDeviceManager ==null){
            mDeviceManager =new DeviceManager();
        }
        return mDeviceManager;
    }
}
