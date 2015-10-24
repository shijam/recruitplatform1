package flytv.ext.utils;

import android.app.Activity;
import android.content.Intent;

public class ActivityUtils {
	/**
	 * 打开activity
	 */
	public static void startActivity(Activity context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
	}

	/**
	 * 打开activity 并关闭自己
	 */
	public static void startActivityAndFinish(Activity context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
		context.finish();
	}
}
