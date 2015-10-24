package flytv.ext.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppFirstHelp {

	private static AppFirstHelp userShareUtils;
	private static SharedPreferences sharedPreferences;

	public static AppFirstHelp getInstance() {
		synchronized (UserShareUtils.class) {
			if (userShareUtils == null) {
				userShareUtils = new AppFirstHelp();
			}
			return userShareUtils;
		}
	}

	public final static String TV_CONFIG = "first_config";

	public boolean getFirstTV(Context context) {
		sharedPreferences = context.getSharedPreferences(TV_CONFIG,
				Context.MODE_PRIVATE);
		int tv_tag = sharedPreferences.getInt("tv_tag", 0);
		if (tv_tag == 1) {
			return false;
		}
		return true;
	}

	public boolean getFirstPoetry(Context context) {
		sharedPreferences = context.getSharedPreferences(TV_CONFIG,
				Context.MODE_PRIVATE);
		int tv_tag = sharedPreferences.getInt("poetry_tag", 0);
		if (tv_tag == 1) {
			return false;
		}
		return true;
	}

	public void setTVInfo(Context context, int tv_tag, int poetry_tag) {
		sharedPreferences = context.getSharedPreferences(TV_CONFIG,
				Context.MODE_PRIVATE);
		Editor editor = sharedPreferences.edit();
		editor.putInt("tv_tag", tv_tag);
		editor.putInt("poetry_tag", poetry_tag);
		editor.commit();
	}
}
