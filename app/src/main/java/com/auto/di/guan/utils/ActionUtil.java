package com.auto.di.guan.utils;

import com.auto.di.guan.MainActivity;
import com.auto.di.guan.MyApplication;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.UserAction;
import com.auto.di.guan.entity.Entiy;

import java.util.List;

public class ActionUtil {

	public static void saveAction( ControlInfo info, int com_type, int type,  int optionType) {
		UserAction action = new UserAction();
		String name = info.controlName;
		if (com_type == MainActivity.TYPE_OPEN) {
			action.setActionDesc(name+ "开启");
		}else if (com_type == MainActivity.TYPE_CLOSE) {
			action.setActionDesc(name+ "关闭");
		}else {
			action.setActionDesc(name+ "读取");
		}



		int actionType =0;
		String end = "";
		if (type == SendUtils.TYPE_RUN) {
			end = "操作正常";
		}else if (type == SendUtils.TYPE_DISCONTENT) {
			end = "通信正常, 开关未打开";
		}else if (type == SendUtils.TYPE_CONTENT) {
			end = "通信异常";
			actionType = Entiy.ACTION_TYPE_ERROR;
		}

		if (optionType == MainActivity.FRAGMENT_4) {
			action.setUserName(MyApplication.getInstance().user.getName()+"  手动单个操作");
			actionType = Entiy.ACTION_TYPE_4;
		}else if (optionType == MainActivity.FRAGMENT_31) {
			action.setUserName(MyApplication.getInstance().user.getName()+"  手动分组操作");
			actionType = Entiy.ACTION_TYPE_31;
		}else if (optionType == MainActivity.FRAGMENT_32){
			action.setUserName(MyApplication.getInstance().user.getName()+"  自动操作");
			actionType = Entiy.ACTION_TYPE_32;
		}
		action.setActionEnd(end);
		action.setTime(System.currentTimeMillis());
		action.setActionType(actionType);
		action.setUserId(MyApplication.getInstance().user.getUserId());


		LogUtils.e("------------","插入数据");
		DBManager.getInstance(MyApplication.getInstance()).insertUserAction(action);
		List<UserAction> actions = DBManager.getInstance(MyApplication.getInstance()).queryUserActionlList("");
		if (actions != null ) {
			LogUtils.e("------------","插入数据"+actions.size());
		}
	}
}
