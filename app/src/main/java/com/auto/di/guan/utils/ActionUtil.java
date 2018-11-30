package com.auto.di.guan.utils;

import com.auto.di.guan.MainActivity;
import com.auto.di.guan.MyApplication;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.DBManager;
import com.auto.di.guan.db.UserAction;

public class ActionUtil {

	public static void saveAction( ControlInfo info, int com_type, int type) {
		UserAction action = new UserAction();
		String name = info.controlName;
		if (com_type == MainActivity.TYPE_OPEN) {
			action.setActionDesc(name+ "开启");
		}else if (com_type == MainActivity.TYPE_CLOSE) {
			action.setActionDesc(name+ "关闭");
		}else {
			action.setActionDesc(name+ "读取");
		}

		String end = "";
		if (type == SendUtils.TYPE_RUN) {
			end = "操作正常";
		}else if (type == SendUtils.TYPE_DISCONTENT) {
			end = "通信正常, 开关未打开";
		}else if (type == SendUtils.TYPE_CONTENT) {
			end = "通信异常";
		}
		action.setActionEnd(end);
		action.setTime(System.currentTimeMillis());
		action.setUserName(MyApplication.getInstance().user.getName());
		action.setUserId(MyApplication.getInstance().user.getUserId());
		DBManager.getInstance(MyApplication.getInstance()).insertUserAction(action);
	}
}
