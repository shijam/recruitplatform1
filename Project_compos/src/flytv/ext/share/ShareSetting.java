package flytv.ext.share;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ShareSetting {

	public static void setSettingPoetry(Context context, int index) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"ettingPoetry", 0);
		Editor ed = sharedPreferences.edit();
		ed.putInt("index", index);
		ed.commit();
	}

	public static int getSettingPoetry(Context context) {
		SharedPreferences sharedPreferences = context.getSharedPreferences(
				"ettingPoetry", 0);
		return sharedPreferences.getInt("index", -1);
	}

}
