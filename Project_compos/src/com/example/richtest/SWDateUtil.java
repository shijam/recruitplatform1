
package com.example.richtest;

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
        Date date = new Date();// ȡʱ��
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, offset);// ��������������һ��.����������,������ǰ�ƶ�
        date = calendar.getTime(); // ���ʱ���������������һ��Ľ��
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
            Log.e(TAG, "compareTime ��ʽ����ȷ");
        }
        int result = c1.compareTo(c2);
        if (result < 0)
            return true;
        return false;
    }
}
