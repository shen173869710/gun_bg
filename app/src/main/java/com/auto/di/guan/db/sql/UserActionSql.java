package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.UserAction;
import com.auto.di.guan.db.greendao.DaoMaster;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.db.greendao.UserActionDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class UserActionSql extends BaseSql {


    /**
     * 插入一条数据
     *
     * @param info
     */
    public static void insertUserAction(UserAction info) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao dao = daoSession.getUserActionDao();
        dao.insert(info);
    }


    public static List<UserAction> queryUserActionlList(String account) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.orderDesc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }

    public static List<UserAction> queryUserActionlListByType(int type) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.ActionType.eq(type)).orderDesc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }


    /**
     * 删除所有
     *
     */
    public static void deleteUserActionAll() {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        userDao.deleteAll();
    }

    /**
     * 删除所有
     *
     */
    public static void deleteUserActionType() {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.ActionEnd.notEq("操作正常")).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                userDao.delete(list.get(i));
            }
        }

    }

    public static List<UserAction> queryUserActionlListByEnd(String type) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.ActionEnd.notEq(type)).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }


    public static List<UserAction> queryUserActionlList(long start, long end) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.Time.between(start, end)).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }

    public static List<UserAction> queryUserActionlList(int id) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.ControlId.eq(id)).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }

    public static List<UserAction> queryUserActionlList(String account, int id) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.UserAccount.eq(account),UserActionDao.Properties.ControlId.eq(id)).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }

    public static List<UserAction> queryUserActionlList(String account, int id, long start, long end) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.UserAccount.eq(account),UserActionDao.Properties.ControlId.eq(id),UserActionDao.Properties.Time.between(start, end)).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }

    public static List<UserAction> queryUserActionlList(int id, long start, long end) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.ControlId.eq(id),UserActionDao.Properties.Time.between(start, end)).orderAsc(UserActionDao.Properties.Time);
        List<UserAction> list = qb.list();
        return list;
    }
}
