package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.User;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.db.greendao.UserDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class UserSql extends BaseSql {
    /**
     * 插入一条记录
     *
     * @param user
     */
    public static void insertUser(User user) {
        DaoSession daoSession = getDaoWriteSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insert(user);
    }

    /**
     * 插入用户集合
     *
     * @param users
     */
    public static void insertUserList(List<User> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoSession daoSession = getDaoWriteSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.insertInTx(users);
    }

    /**
     * 删除一条记录
     *
     * @param user
     */
    public static void deleteUser(User user) {
        DaoSession daoSession = getDaoWriteSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.delete(user);
    }

    /**
     * 更新一条记录
     *
     * @param user
     */
    public static void updateUser(User user) {
        DaoSession daoSession = getDaoWriteSession();
        UserDao userDao = daoSession.getUserDao();
        userDao.update(user);
    }



    /**
     * 查询用户列表
     */
    public static List<User> queryUserList() {
        DaoSession daoSession = getDaoWriteSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        return qb.list();
    }

    /**
     * 查询用户列表
     */
    public static List<User> queryUserList(String account) {
        DaoSession daoSession = getDaoWriteSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.where(UserDao.Properties.LoginName.eq(account)).orderAsc(UserDao.Properties.LoginName);
        List<User> list = qb.list();
        return list;
    }
}
