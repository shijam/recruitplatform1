package flytv.ext.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SWDateUtil {

	private static final String TAG = "SWDateUtil";

	public static String getFormatDateByOffset(String formatStr, int offset) {
		Date date = new Date();// 取时间
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, offset);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		SimpleDateFormat formatter = new SimpleDateFormat(formatStr);
		return formatter.format(date);
	}

	public static boolean compareTime(String time) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c1.setTime(new Date());
			c2.setTime(df.parse(time));
		} catch (ParseException e) {
			Log.e(TAG, "compareTime 格式不正确");
		}
		int result = c1.compareTo(c2);
		if (result < 0)
			return true;
		return false;
	}
}
