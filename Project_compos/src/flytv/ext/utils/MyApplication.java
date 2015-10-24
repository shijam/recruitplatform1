package flytv.ext.utils;

import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.app.Application;

/**
 * activity管理类
 * 
 * @author YiShanXin
 */
public class MyApplication extends Application {
	public List<Activity> activityList = new LinkedList<Activity>();
	private static MyApplication instance;

	private MyApplication() {
	}

	// 单例模式中获取唯一的MyApplication实例
	public static MyApplication getInstance() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	// 添加Activity到容器中
	public void addActivity(Activity activity) {
		activityList.add(activity);
	}

	// 遍历所有Activity并finish

	public void exit(boolean isFinish) {
		for (Activity activity : activityList) {
			activity.finish();
		}
		if(isFinish){
			System.exit(0);
		}
	}

}
