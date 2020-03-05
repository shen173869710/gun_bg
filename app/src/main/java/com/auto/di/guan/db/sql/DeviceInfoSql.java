package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.db.greendao.DeviceInfoDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

public class DeviceInfoSql extends BaseSql {
    private final static String TAG = "DeviceInfoSql";

    public static List<DeviceInfo>deviceInfos = new ArrayList<>();

    /**
     * 获取所有设备列表
     */
    public static List<DeviceInfo> queryDeviceList() {
        if (deviceInfos.size() == 0) {
            DaoSession daoSession = getDaoWriteSession();
            DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
            QueryBuilder<DeviceInfo> qb = userDao.queryBuilder();
            deviceInfos = qb.list();
        }
        return deviceInfos;
    }

    /**
     *  通过ID查询设备
     */
    public static DeviceInfo queryDeviceById(int deviceId) {
        DaoSession daoSession = getDaoWriteSession();
        DeviceInfoDao deviceInfoDao = daoSession.getDeviceInfoDao();
        QueryBuilder<DeviceInfo> qb = deviceInfoDao.queryBuilder();
        qb.where(DeviceInfoDao.Properties.DeviceId.eq(deviceId));
        return qb.unique();
    }
    /**
     * 更新所有设备
     * @param infos
     */
    public static void updateDeviceList(List<DeviceInfo> infos) {
        DaoSession daoSession = getDaoWriteSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        userDao.updateInTx(infos);
        upDateList();
    }
    /**
     * 插入
     * @param deviceInfos
     */
    public static void insertDeviceInfoList(List<DeviceInfo> deviceInfos) {
        if (deviceInfos == null || deviceInfos.isEmpty()) {
            return;
        }
        DaoSession daoSession = getDaoWriteSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        userDao.insertInTx(deviceInfos);
    }
    /**
     * 更新一条记录
     * @param deviceInfo
     */
    public static void updateDevice(DeviceInfo deviceInfo) {
        DaoSession daoSession = getDaoWriteSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        userDao.update(deviceInfo);
        upDateList();
    }


    /**
     * 获取设备的数量
     */
    public static long queryDeviceCount() {
        DaoSession daoSession = getDaoWriteSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        return userDao.count();
    }

    /**
     *  更新缓存的list
     */
    private static void upDateList() {
        DaoSession daoSession = getDaoWriteSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        QueryBuilder<DeviceInfo> qb = userDao.queryBuilder();
        deviceInfos = qb.list();
    }

}
