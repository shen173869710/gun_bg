package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.DeviceInfo;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.db.greendao.DeviceInfoDao;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;

public class DeviceInfoSql extends BaseSql {
    private final static String TAG = "DeviceInfoSql";
    /**
     * 获取所有设备列表
     */
    public static List<DeviceInfo> queryDeviceList() {
        DaoSession daoSession = getDaoWriteSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        QueryBuilder<DeviceInfo> qb = userDao.queryBuilder();
        List<DeviceInfo> list = qb.list();
        return list;
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
    }
    /**
     * 插入
     * @param users
     */
    public static void insertDeviceInfoList(List<DeviceInfo> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoSession daoSession = getDaoWriteSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        userDao.insertInTx(users);
    }
    /**
     * 更新一条记录
     * @param user
     */
    public static void updateDevice(DeviceInfo user) {
        DaoSession daoSession = getDaoWriteSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        userDao.update(user);
    }

}
