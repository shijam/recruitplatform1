package flytv.ext.utils;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import flytv.run.bean.AppBean;
import flytv.run.bean.TVCodeBean;

public class UserShareUtils {
	public static final String STBUSERID = "90110679";
	private UserShareUtils() {

	}

	private static UserShareUtils userShareUtils;
	private static SharedPreferences sharedPreferences;

	public static UserShareUtils getInstance() {
		synchronized (UserShareUtils.class) {
			if (userShareUtils == null) {
				userShareUtils = new UserShareUtils();
			}
			return userShareUtils;
		}
	}

	public final static String TV_CONFIG = "config";
	public final static String HELP_TEST_CONFIG = "help_spire_config";

	public TVCodeBean getTVInfo(Context context) {
		sharedPreferences = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		TVCodeBean tvCodeBean = new TVCodeBean();
		tvCodeBean.setCpmaId(sharedPreferences.getString("cpmaId", ""));
		tvCodeBean.setDeviceNo(sharedPreferences.getString("deviceNo", ""));
		tvCodeBean.setUserCode(sharedPreferences.getString("userCode", ""));
		tvCodeBean.setUserToken(sharedPreferences.getString("userToken", ""));
		tvCodeBean.setStbStatus(sharedPreferences.getInt("userSTB", 0));
		return tvCodeBean;
	}
	public void setTVInfo(Context context,int status,String deviceNo) {
		sharedPreferences = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		Editor editor =  sharedPreferences.edit();
		editor.putInt("userSTB",status);
		editor.putString("deviceNo", deviceNo);
		editor.commit();
		
	}
	public void clearInfo(Context context) {
		sharedPreferences = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		Editor editor =  sharedPreferences.edit();
		editor.clear();
		editor.commit();
		
	}
	/**
	 * 注册后 返回用户信息
	 * @param context
	 * @param tvCodeBean
	 */
	public void setLoginInfo(Context context,TVCodeBean tvCodeBean) {
		sharedPreferences = context.getSharedPreferences("config_user_info",
				Context.MODE_PRIVATE);
		Editor editor =  sharedPreferences.edit();
		String serStr;
		try {
			serStr = AppUtil.serialize(tvCodeBean);
			editor.putString("userStr",serStr );
			editor.commit();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Object getLoginInfo(Context context) {
		sharedPreferences = context.getSharedPreferences("config_user_info",
				Context.MODE_PRIVATE);
		Object serStr;
		try {
			serStr = AppUtil.deSerialization(sharedPreferences.getString("userStr", ""));
			return serStr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	public void clearLoginInfo(Context context) {
		sharedPreferences = context.getSharedPreferences("config_user_info",
				Context.MODE_PRIVATE);
		Editor editor =  sharedPreferences.edit();
		editor.clear();
		editor.commit();
		
	}
	public void setHelpInfo(Context context,AppBean tvCodeBean) {
		sharedPreferences = context.getSharedPreferences(HELP_TEST_CONFIG,
				Context.MODE_PRIVATE);
		Editor editor =  sharedPreferences.edit();
		String serStr;
		try {
			serStr = AppUtil.serialize(tvCodeBean);
			editor.putString("userHelp",serStr);
			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public Object getHelpInfo(Context context) {
		sharedPreferences = context.getSharedPreferences(HELP_TEST_CONFIG,
				Context.MODE_PRIVATE);
		Object serStr;
		try {
			serStr = AppUtil.deSerialization(sharedPreferences.getString("userHelp", ""));
			return serStr;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return new AppBean();
	}
	public void clearHelpInfo(Context context) {
		sharedPreferences = context.getSharedPreferences(HELP_TEST_CONFIG,
				Context.MODE_PRIVATE);
		Editor editor =  sharedPreferences.edit();
		editor.clear();
		editor.commit();
		
	}
}
