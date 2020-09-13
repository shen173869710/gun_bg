package com.auto.di.guan.utils;

import com.auto.di.guan.BaseApp;
import com.auto.di.guan.MainActivity;
import com.auto.di.guan.db.ControlInfo;
import com.auto.di.guan.db.UserAction;
import com.auto.di.guan.db.sql.UserActionSql;
import com.auto.di.guan.entity.Entiy;
import com.auto.di.guan.jobqueue.TaskEntiy;

import java.util.List;
/**
 *     保存用户操作信息
 */
public class ActionUtil {

	public static final String TAG = "ActionUtil";
	/**
	 * @param info           设备信息
	 * @param com_type       执行的动作
	 * @param optionType     执行的类型
	 * @param isNormal        是否操作正常
	 */
	public static void saveAction( ControlInfo info, int com_type, int optionType, String desc, boolean isNormal) {
		UserAction action = new UserAction();
		int operateResult = -1;
		String name = info.getValve_alias();
		if (com_type == TaskEntiy.TASK_OPTION_OPEN_READ) {
			operateResult =1;
			action.setActionName(name+ "开启");
		}else if (com_type == TaskEntiy.TASK_OPTION_CLOSE_READ) {
			action.setActionName(name+ "关闭");
			operateResult =0;
		}else if (com_type == TaskEntiy.TASK_OPTION_READ){
			action.setActionName(name+ "读取");
		}
		int operateType=0;
		int actionType =0;
		String userName = BaseApp.getUser().getUserName();
		if (optionType == Entiy.ACTION_TYPE_4) {
			action.setUserName(userName);
			actionType = Entiy.ACTION_TYPE_4;
			action.setActionTypeName("手动单个操作");
			operateType = 2;
		}else if (optionType == Entiy.ACTION_TYPE_31) {
			action.setUserName(userName);
			actionType = Entiy.ACTION_TYPE_31;
			action.setActionTypeName("手动分组操作");
			operateType = 1;
		}else if (optionType == Entiy.ACTION_TYPE_32){
			action.setUserName(userName);
			actionType = Entiy.ACTION_TYPE_32;
			action.setActionTypeName("自动轮灌操作");
			operateType = 0;
		}
		action.setActionStatusName(desc);
		action.setActionTime(System.currentTimeMillis());
		action.setActionType(actionType);
		action.setActionStatus(isNormal);
		LogUtils.e(TAG,"----------插入数据");
		UserActionSql.insertUserAction(action);
		List<UserAction> actions = UserActionSql.queryUserActionlList();
		if (actions != null ) {
			LogUtils.e(TAG,"------插入数据"+actions.size());
		}

		if (operateResult < 0) {
			return;
		}
//		PostUtil.PostData data = new PostUtil.PostData();
//		data.operateResult = operateResult;
//		data.operateType = operateType;
//		data.operateName = action.getActionName();
//		data.projectName = BaseApp.getUser().getProjectName();
//		data.valveCode = info.getValve_id()+"";
//		data.valveName = info.getValve_name();
//		data.projectId = BaseApp.getUser().getProjectId();
//		PostUtil.post(data);
	}

}
