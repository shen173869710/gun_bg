package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.UserAction;
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


    public static List<UserAction> queryUserActionlList() {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.orderDesc(UserActionDao.Properties.ActionTime);
        List<UserAction> list = qb.list();
        return list;
    }

    public static List<UserAction> queryUserActionlListByType(int type) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.ActionType.eq(type)).orderDesc(UserActionDao.Properties.ActionTime);
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
        qb.where(UserActionDao.Properties.ActionStatus.notEq("操作正常")).orderDesc(UserActionDao.Properties.ActionTime);
        List<UserAction> list = qb.list();
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                userDao.delete(list.get(i));
            }
        }

    }

    public static List<UserAction> queryUserActionlListByEnd(boolean type) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.ActionStatus.eq(type)).orderDesc(UserActionDao.Properties.ActionTime);
        List<UserAction> list = qb.list();
        return list;
    }


//    public static List<UserAction> queryUserActionlList(long start, long end) {
//        DaoSession daoSession = getDaoWriteSession();
//        UserActionDao userDao = daoSession.getUserActionDao();
//        QueryBuilder<UserAction> qb = userDao.queryBuilder();
//        qb.where(UserActionDao.Properties.ActionTime.between(start, end)).orderAsc(UserActionDao.Properties.ActionTime);
//        List<UserAction> list = qb.list();
//        return list;
//    }

    public static List<UserAction> queryUserActionlList(int id) {
        DaoSession daoSession = getDaoWriteSession();
        UserActionDao userDao = daoSession.getUserActionDao();
        QueryBuilder<UserAction> qb = userDao.queryBuilder();
        qb.where(UserActionDao.Properties.ControlId.eq(id)).orderDesc(UserActionDao.Properties.ActionTime);
        List<UserAction> list = qb.list();
        return list;
    }

//    public static List<UserAction> queryUserActionlList(String account, int id) {
//        DaoSession daoSession = getDaoWriteSession();
//        UserActionDao userDao = daoSession.getUserActionDao();
//        QueryBuilder<UserAction> qb = userDao.queryBuilder();
//        qb.where(UserActionDao.Properties.UserAccount.eq(account),UserActionDao.Properties.ControlId.eq(id)).orderAsc(UserActionDao.Properties.Time);
//        List<UserAction> list = qb.list();
//        return list;
//    }

//    public static List<UserAction> queryUserActionlList(String account, int id, long start, long end) {
//        DaoSession daoSession = getDaoWriteSession();
//        UserActionDao userDao = daoSession.getUserActionDao();
//        QueryBuilder<UserAction> qb = userDao.queryBuilder();
//        qb.where(UserActionDao.Properties.UserAccount.eq(account),UserActionDao.Properties.ControlId.eq(id),UserActionDao.Properties.Time.between(start, end)).orderAsc(UserActionDao.Properties.Time);
//        List<UserAction> list = qb.list();
//        return list;
//    }

//    public static List<UserAction> queryUserActionlList(int id, long start, long end) {
//        DaoSession daoSession = getDaoWriteSession();
//        UserActionDao userDao = daoSession.getUserActionDao();
//        QueryBuilder<UserAction> qb = userDao.queryBuilder();
//        qb.where(UserActionDao.Properties.ControlId.eq(id),UserActionDao.Properties.Time.between(start, end)).orderAsc(UserActionDao.Properties.Time);
//        List<UserAction> list = qb.list();
//        return list;
//    }
}
