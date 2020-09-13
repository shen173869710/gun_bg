package com.auto.di.guan;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ActivityStackUtil {

	private static List<Activity> activityList = new CopyOnWriteArrayList<Activity>();

	//	private static List<Activity> activityList = new ArrayList<Activity>();

	public static List<Activity> getList() {
		synchronized (activityList) {
			return activityList;
		}
	}

	public static Activity getCurActivity() {
		synchronized (activityList) {
			int size = activityList.size();
			if (size < 1)
				return null;
			return activityList.get(size - 1);
		}
	}

	public static void remove(Activity activity) {
		synchronized (activityList) {
			activityList.remove(activity);
		}
	}

	public static void add(Activity activity) {
		synchronized (activityList) {
			activityList.add(activity);
		}
	}

	public static void finishProgram() {
		synchronized (activityList) {
			for (Activity activity : activityList) {
				activity.finish();
			}
		}
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static void exchangeView() {
		synchronized (activityList) {
			for (Activity activity : activityList) {
				activity.finish();
			}
		}
	}

	public synchronized static void cleanActivity(Class cls) {
		synchronized (activityList) {
			int len = activityList.size();
			Activity act = null;
			List<Integer> indexList = new ArrayList<Integer>();
			for (int i = 0; i < len; i++) {
				act = activityList.get(i);
				if (act.getClass() == cls)
					continue;
				if (act == null)
					continue;
				act.finish();
				indexList.add(i);
				act = null;
			}
			for (Integer index : indexList) {
				int i = index;
				if (i >= activityList.size())
					return;
				activityList.remove(i);

			}
		}
		//		IApplication.activities.clear();
	}
}
