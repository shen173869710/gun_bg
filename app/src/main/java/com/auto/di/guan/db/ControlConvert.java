package com.auto.di.guan.db;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by czl on 2017/11/28.
 */

public class ControlConvert implements PropertyConverter<ArrayList<ControlInfo>,String> {

    private final Gson gson;
    public ControlConvert() {
        gson = new Gson();
    }

    @Override
    public ArrayList<ControlInfo> convertToEntityProperty(String databaseValue) {
        Type type = new TypeToken<ArrayList<ControlInfo>>() {}.getType();
        ArrayList<ControlInfo> itemList= gson.fromJson(databaseValue, type);
        return itemList;
    }

    @Override
    public String convertToDatabaseValue(ArrayList<ControlInfo> entityProperty) {
        String dbString = gson.toJson(entityProperty);
        return dbString;
    }
}
