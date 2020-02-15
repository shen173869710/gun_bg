package com.auto.di.guan.db.sql;

import java.util.List;

/**
 * Created by chenzhaolin on 2019/7/15.
 *
 * 数据库查询的抽象接口
 */

public interface ISql {

    //插入数据
    void insert(Object object);

    void insertOrUpdate(Object object);

    void update(Object object);

    void delete(Object object);

    void deleteByLongKey(Class<? extends Object> classType, Long id);

    void deleteByStringKey(Class<? extends Object> classType, String id);

    void deleteAll(Class<? extends Object> classType);

    <T> T quaryByLongKey(Class<T> classType, Long id);

    <T> T quaryByStringKey(Class<T> classType, String id);


    <T> List<T> quary(Class<T> classType, String where, String... selectionArg);

    <T> List<T> quaryAll(Class<T> classType);
}

