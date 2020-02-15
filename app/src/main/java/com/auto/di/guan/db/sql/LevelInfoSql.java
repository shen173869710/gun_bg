package com.auto.di.guan.db.sql;

import com.auto.di.guan.db.LevelInfo;
import com.auto.di.guan.db.greendao.DaoMaster;
import com.auto.di.guan.db.greendao.DaoSession;
import com.auto.di.guan.db.greendao.LevelInfoDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

public class LevelInfoSql extends BaseSql {


    public static List<LevelInfo> queryLevelInfoList() {
        DaoSession daoSession = getDaoWriteSession();
        LevelInfoDao levelInfoDao = daoSession.getLevelInfoDao();
        QueryBuilder<LevelInfo> qb = levelInfoDao.queryBuilder();
        List<LevelInfo> list = qb.list();
        return list;
    }

    public static void delLevelInfoList() {
        DaoSession daoSession = getDaoWriteSession();
        LevelInfoDao levelInfoDao = daoSession.getLevelInfoDao();
        levelInfoDao.deleteAll();
    }


    public static void insertLevelInfo(LevelInfo info) {
        DaoSession daoSession = getDaoWriteSession();
        LevelInfoDao controlInfoDao = daoSession.getLevelInfoDao();
        controlInfoDao.insert(info);
    }


    public static void updateLevelInfo(LevelInfo info) {
        DaoSession daoSession = getDaoWriteSession();
        LevelInfoDao dao = daoSession.getLevelInfoDao();
        dao.update(info);
    }


    public static void insertLevelInfoList(List<LevelInfo> users) {
        if (users == null || users.isEmpty()) {
            return;
        }
        DaoSession daoSession = getDaoWriteSession();
        LevelInfoDao dao = daoSession.getLevelInfoDao();
        dao.insertInTx(users);
    }

    public static List<LevelInfo> queryUserLevelInfoListByGroup(boolean isUse) {
        DaoSession daoSession = getDaoWriteSession();
        LevelInfoDao userDao = daoSession.getLevelInfoDao();
        QueryBuilder<LevelInfo> qb = userDao.queryBuilder();
        qb.where(LevelInfoDao.Properties.IsGroupUse.eq(isUse)).orderAsc(LevelInfoDao.Properties.LevelId);
        List<LevelInfo> list = qb.list();
        return list;
    }
    public static List<LevelInfo> queryUserLevelInfoListByLevel(boolean isUse) {
        DaoSession daoSession = getDaoWriteSession();
        LevelInfoDao userDao = daoSession.getLevelInfoDao();
        QueryBuilder<LevelInfo> qb = userDao.queryBuilder();
        qb.where(LevelInfoDao.Properties.IsGroupUse.eq(isUse)).orderAsc(LevelInfoDao.Properties.LevelId);
        List<LevelInfo> list = qb.list();
        return list;
    }
}
