package flytv.ext.utils;


import android.app.Service;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Vibrator;

public class VibratorUtil {
	public static void vibrator(Context context){
		SharedPreferences sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		boolean isVibrator = sp.getBoolean(ConstantValue.ISSHAKE, true);
		if(isVibrator){
			LogUtils.print("震动");
			Vibrator vib = (Vibrator) context.getSystemService(Service.VIBRATOR_SERVICE);
			vib.vibrate(50);
		}
	}
}
