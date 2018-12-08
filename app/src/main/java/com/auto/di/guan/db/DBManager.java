package com.auto.di.guan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.auto.di.guan.MyApplication;
import com.auto.di.guan.db.greendao.DaoMaster;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.db.greendao.DeviceInfoDao;
import com.auto.di.guan.db.greendao.GroupInfoDao;
import com.auto.di.guan.db.greendao.LevelInfoDao;
import com.auto.di.guan.db.greendao.UserActionDao;
import com.auto.di.guan.db.greendao.UserDao;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/28.
 */

public class DBManager {
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;

    public DBManager(Context context) {
        this.context = context;
        openHelper = new DaoMaster.DevOpenHelper(context, MyApplication.DB_NAME, null);
    }

    /**
     * 获取单例引用
     *
     * @param context
     * @return
     */
    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }


    /**
     * 获取所有设备列表
     */
    public List<DeviceInfo> queryDeviceList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        QueryBuilder<DeviceInfo> qb = userDao.queryBuilder();
        List<DeviceInfo> list = qb.list();
        return list;
    }

    /**
     * 获取所有设备列表
     */
    public DeviceInfo queryDeviceById(int deviceId) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DeviceInfoDao deviceInfoDao = daoSession.getDeviceInfoDao();
        QueryBuilder<DeviceInfo> qb = deviceInfoDao.queryBuilder();
        qb.where(DeviceInfoDao.Properties.DeviceId.eq(deviceId));
        return qb.unique();
    }
    /**
     * 更新所有设备
     *
     * @param infos
     */
    public void updateDeviceList(List<DeviceInfo> infos) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        userDao.updateInTx(infos);
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public void insertDeviceInfoList(List<DeviceInfo> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        userDao.insertInTx(users);
    }

    /**
     * 更新一条记录
     *
     * @param user
     */
    public void updateDevice(DeviceInfo user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        DeviceInfoDao userDao = daoSession.getDeviceInfoDao();
        userDao.update(user);
    }

    /**
     * 获取可读数据库
     */
    private SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, MyApplication.DB_NAME, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }

    /**
     * 获取可写数据库
     */
    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, MyApplication.DB_NAME, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /**
     * 插入一条记录
     *
     * @param user
     */
    public void insertUser(User user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insert(user);
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public void insertUserList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insertInTx(users);
    }

    /**
     * 删除一条记录
     *
     * @param user
     */
    public void deleteUser(User user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.delete(user);
    }

    /**
     * 更新一条记录
     *
     * @param user
     */
    public void updateUser(User user) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.update(user);
    }



    /**
     * 查询用户列表
     */
    public List<User> queryUserList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        List<User> list = qb.list();
        return list;
    }

    /**
     * 查询用户列表
     */
    public List<User> queryUserList(String account) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.Account.eq(account)).orderAsc(UserDao.Properties.Account);
        List<User> list = qb.list();
        return list;
    }


//    /**
//     * 插入一条数据
//     *
//     * @param info
//     */
//    public void insertControl(ControlInfo info) {
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        ControlInfoDao controlInfoDao = null;
//        controlInfoDao.insert(info);
//    }
//
//    /**
//     * 插入用户集合
//     *
//     * @param users
//     */
//    public void insertControlList(List<ControlInfo> users) {
//        if (users == null || users.isEmpty()) {
//            return;
//        }
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        ControlInfoDao userDao = null;
//        userDao.insertInTx(users);
//    }
//
//
    /**
     * 更新所有数据
     *
     * @param info
     */
    public void updateControl(ControlInfo info) {
        List<DeviceInfo>deviceInfos = queryDeviceList();
        for (int i = 0; i < deviceInfos.size(); i++) {
            if (info.controId == deviceInfos.get(i).getControlInfos().get(0).controId) {
                deviceInfos.get(i).getControlInfos().get(0).groupId = 0;
            }
            if (info.controId == deviceInfos.get(i).getControlInfos().get(1).controId) {
                deviceInfos.get(i).getControlInfos().get(1).groupId = 0;
            }
        }
        updateDeviceList(deviceInfos);
    }
//
//
//    /**
//     * 删除一条记录
//     *
//     * @param user
//     */
//    public void deleteControl(ControlInfo user) {
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        ControlInfoDao userDao = null;
//        userDao.delete(user);
//    }
//
//    /**
//     * 更新一条记录
//     *
//     * @param user
//     */
//    public void updateControl(ControlInfo user) {
//        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        ControlInfoDao userDao = null;
//        userDao.update(user);
//    }
//
//    /**
//     * 查询用户列表
//     */
//    public List<ControlInfo> queryControlList() {
//        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        ControlInfoDao userDao = null;
//        QueryBuilder<ControlInfo> qb = userDao.queryBuilder();
//        List<ControlInfo> list = qb.list();
//        return list;
//    }
//
//    /**
//     * 查询控制组
//     */
//    public List<ControlInfo> queryControlList(int groupId) {
//        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
//        DaoSession daoSession = daoMaster.newSession();
//        ControlInfoDao userDao = null;
//        QueryBuilder<ControlInfo> qb = userDao.queryBuilder();
//        qb.where(ControlInfoDao.Properties.GroupId.eq(groupId)).orderAsc(ControlInfoDao.Properties.GroupId);
//        List<ControlInfo> list = qb.list();
//        return list;
//    }


    /**
     * 查询分组列表
     */
    public List<GroupInfo> queryGrouplList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        QueryBuilder<GroupInfo> qb = dao.queryBuilder().orderAsc(GroupInfoDao.Properties.GroupLevel);
        List<GroupInfo> list = qb.list();
        return list;
    }


    /**
     * 查询分组列表
     */
    public List<ControlInfo> queryControlList(int groupId) {
        ArrayList<ControlInfo>controlInfos = new ArrayList<>();
        List<DeviceInfo>deviceInfos = queryDeviceList();
        for (int i = 0; i < deviceInfos.size(); i++) {
            if (groupId == deviceInfos.get(i).getControlInfos().get(0).groupId)
            {
                controlInfos.add(deviceInfos.get(i).controlInfos.get(0));
            }
            if (groupId == deviceInfos.get(i).getControlInfos().get(1).groupId)
            {
                controlInfos.add(deviceInfos.get(i).controlInfos.get(1));
            }
        }
        return controlInfos;
    }

    /**
     * 查询分组列表
     */
    public List<ControlInfo> queryControlList() {
        ArrayList<ControlInfo>controlInfos = new ArrayList<>();
        List<DeviceInfo>deviceInfos = queryDeviceList();
        for (int i = 0; i < deviceInfos.size(); i++) {
            controlInfos.add(deviceInfos.get(i).controlInfos.get(0));
            controlInfos.add(deviceInfos.get(i).controlInfos.get(1));

        }
        return controlInfos;
    }

    /**
     * 删除一条记录
     *
     * @param info
     */
    public void deleteGroup(GroupInfo info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        dao.delete(info);
    }

    /**
     * 更新一条记录
     *
     * @param info
     */
    public void updateGroup(GroupInfo info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        dao.update(info);
    }


    public void updateGroupList(List<GroupInfo> infos) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GroupInfoDao userDao = daoSession.getGroupInfoDao();
        userDao.updateInTx(infos);
    }

    /**
     * 插入一条数据
     *
     * @param info
     */
    public void insertGroup(GroupInfo info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        dao.insert(info);
    }


    public List<GroupInfo> queryGroupInfoById(int id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GroupInfoDao userDao = daoSession.getGroupInfoDao();
        QueryBuilder<GroupInfo> qb = userDao.queryBuilder();
        qb.where(GroupInfoDao.Properties.GroupId.eq(id));
        List<GroupInfo> list = qb.list();
        return list;
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public void insertGrouplList(List<GroupInfo> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        GroupInfoDao dao = daoSession.getGroupInfoDao();
        dao.insertInTx(users);
    }


    /**
     * 插入一条数据
     *
     * @param info
     */
    public void insertUserAction(UserAction info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserActionDao dao = daoSession.getUserActionDao();
        dao.insert(info);
    }


    public List<UserAction> queryUserActionlList(String account) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }

    public List<UserAction> queryUserActionlList(long start, long end) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.Time.between(start, end)).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }

    public List<UserAction> queryUserActionlList(int id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.ControlId.eq(id)).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }

    public List<UserAction> queryUserActionlList(String account, int id) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.UserAccount.eq(account),UserActionDao.Properties.ControlId.eq(id)).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }

    public List<UserAction> queryUserActionlList(String account, int id, long start, long end) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.UserAccount.eq(account),UserActionDao.Properties.ControlId.eq(id),UserActionDao.Properties.Time.between(start, end)).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }

    public List<UserAction> queryUserActionlList(int id, long start, long end) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.ControlId.eq(id),UserActionDao.Properties.Time.between(start, end)).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }



    /**
     * 查询用户列表
     */
    public List<LevelInfo> queryLevelInfoList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LevelInfoDao levelInfoDao = daoSession.getLevelInfoDao();
        QueryBuilder<LevelInfo> qb = levelInfoDao.queryBuilder();
        List<LevelInfo> list = qb.list();
        return list;
    }
    /**
     * 查询用户列表
     */
    public void delLevelInfoList() {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LevelInfoDao levelInfoDao = daoSession.getLevelInfoDao();
       levelInfoDao.deleteAll();
    }


    /**
     * 插入一条数据
     *
     * @param info
     */
    public void insertLevelInfo(LevelInfo info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LevelInfoDao controlInfoDao = daoSession.getLevelInfoDao();
        controlInfoDao.insert(info);
    }

    /**
     * 更新一条记录
     *
     * @param info
     */
    public void updateLevelInfo(LevelInfo info) {
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LevelInfoDao dao = daoSession.getLevelInfoDao();
        dao.update(info);
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public void insertLevelInfoList(List<LevelInfo> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoMaster daoMaster = new DaoMaster(getWritableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LevelInfoDao dao = daoSession.getLevelInfoDao();
        dao.insertInTx(users);
    }

    public List<LevelInfo> queryUserLevelInfoListByGroup(boolean isUse) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LevelInfoDao userDao = daoSession.getLevelInfoDao();
        QueryBuilder<LevelInfo> qb = userDao.queryBuilder();
        qb.where(LevelInfoDao.Properties.IsGroupUse.eq(isUse)).orderAsc(LevelInfoDao.Properties.LevelId);
        List<LevelInfo> list = qb.list();
        return list;
    }
    public List<LevelInfo> queryUserLevelInfoListByLevel(boolean isUse) {
        DaoMaster daoMaster = new DaoMaster(getReadableDatabase());
        DaoSession daoSession = daoMaster.newSession();
        LevelInfoDao userDao = daoSession.getLevelInfoDao();
        QueryBuilder<LevelInfo> qb = userDao.queryBuilder();
        qb.where(LevelInfoDao.Properties.IsGroupUse.eq(isUse)).orderAsc(LevelInfoDao.Properties.LevelId);
        List<LevelInfo> list = qb.list();
        return list;
    }

}
