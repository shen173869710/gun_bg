package com.auto.di.guan.utils;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.MainActivity;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.UserAction;
import com.auto.di.guan.db.sql.UserActionSql;
import com.auto.di.guan.entity.Entiy;

import java.util.List;

public class ActionUtil {

	public static void saveAction( ControlInfo info, int com_type, int type,  int optionType) {
		UserAction action = new UserAction();

		int operateResult = -1;
		String name = info.getValve_name();
		if (com_type == MainActivity.TYPE_OPEN) {
			operateResult =1;
			action.setActionDesc(name+ "开启");
		}else if (com_type == MainActivity.TYPE_CLOSE) {
			action.setActionDesc(name+ "关闭");
			operateResult =0;
		}else {
			action.setActionDesc(name+ "读取");
		}
		int operateType=0;
		int actionType =0;
		String userName = BaseApp.getUser().getUserName();

		if (optionType == Entiy.FRAGMENT_4) {
			action.setUserName(userName);
			actionType = Entiy.ACTION_TYPE_4;
			action.setUserAccount("手动单个操作");
			operateType = 2;
		}else if (optionType == Entiy.FRAGMENT_31) {
			action.setUserName(userName);
			actionType = Entiy.ACTION_TYPE_31;
			action.setUserAccount("手动分组操作");
			operateType = 1;
		}else if (optionType == Entiy.FRAGMENT_32){
			action.setUserName(userName);
			actionType = Entiy.ACTION_TYPE_32;
			action.setUserAccount("自动轮灌操作");
			operateType = 0;
		}


		String end = "";

		switch (type) {
			case SendUtils.OPTION_OPEN_SUCESS:
				end = SendUtils.OPTION_OPEN_SUCESS_VALUE;
				operateResult = 3;
				break;
			case SendUtils.OPTION_OPEN_ERROR:
				end = SendUtils.OPTION_OPEN_ERROR_VALUE;
				operateResult = 3;
				break;
			case SendUtils.OPTION_OPEN_FAILE:
				end = SendUtils.OPTION_OPEN_FAILE_VALUE;
				operateResult = 3;
				break;
			case SendUtils.OPTION_CLOSE_SUCESS:
				end = SendUtils.OPTION_CLOSE_SUCESS_VALUE;
				operateResult = 3;
				break;
			case SendUtils.OPTION_CLOSE_ERROR:
				end = SendUtils.OPTION_CLOSE_ERROR_VALUE;
				operateResult = 3;
				break;
			case SendUtils.OPTION_CLOSE_FAILE:
				end = SendUtils.OPTION_CLOSE_FAILE_VALUE;
				operateResult = 3;
				break;

		}

//		if (type == SendUtils.TYPE_RUN) {
//			end = "操作正常";
//		}else if (type == SendUtils.TYPE_DISCONTENT) {
//			end = "通信正常, 阀门未打开";
//			operateResult = 3;
//		}else if (type == SendUtils.TYPE_CONTENT) {
//			end = "通信异常";
//			operateResult = 3;
//		}else if (type == SendUtils.TYPE_NOT_CLOSE) {
//			operateResult = 3;
//			end = "通信正常, 阀门未关闭";
//		}
		action.setActionEnd(end);
		action.setTime(System.currentTimeMillis());
		action.setActionType(actionType);

		LogUtils.e("------------","插入数据");
		UserActionSql.insertUserAction(action);
		List<UserAction> actions = UserActionSql.queryUserActionlList("");
		if (actions != null ) {
			LogUtils.e("------------","插入数据"+actions.size());
		}

		if (operateResult < 0) {
			return;
		}
		PostUtil.PostData data = new PostUtil.PostData();
		data.operateResult = operateResult;
		data.operateType = operateType;
		data.operateName = action.getUserAccount();
		data.projectName = ShareUtil.getStringLocalValue(BaseApp.getInstance(), "title_name");
		data.valveCode = info.getValve_id()+"";
		data.valveName = info.getValve_name();
		data.projectId = ShareUtil.getStringLocalValue(BaseApp.getInstance(),"group_name");
		PostUtil.post(data);
	}
}
