package flytv.ext.utils;

import android.util.Log;

public class LogUtils {

	private static final String LOG_TAG = LogUtils.class.getName();
	private static final boolean isPrint = true;
	private static final boolean isReordPrint = false;
	private static final boolean isLog = true;

	public static void print(int log, String message) {
		if (!isPrint() || AppUtil.isStrNull(message)) {
			return;
		}
		switch (log) {
		case 1:
			Log.i(LOG_TAG, message);
			break;
		default:
			Log.e(LOG_TAG, message);
			break;
		}
	}

	public static void print(String message, int log) {
		if (!isShow()) {
			return;
		}
		switch (log) {
		case 1:
			Log.i(LOG_TAG, message);
			break;
		default:
			Log.e(LOG_TAG, message);
			break;
		}
	}

	public static void print(String message) {
		if (!isPint()) {
			return;
		}
		Log.i(LOG_TAG, message);

	}

	private static boolean isPrint() {
		return isPrint;
	}

	private static boolean isShow() {
		return isReordPrint;
	}

	private static boolean isPint() {
		return isLog;
	}
}
