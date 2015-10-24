package flytv.ext.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import flytv.compos.run.R;
import flytv.run.bean.AppInfo;
import flytv.run.bean.TVCodeBean;

public class SWUpdateUtil {

	private static Context mContext = null;
	private static String verMessage = "";
	private static String verName = "";
	private static String versionName = "";
	private static int oldDayVersion = 0;
	public static String oldDayVersionName = null;
	public static boolean isCommit = false;
	public static boolean isExit = false;
	public static boolean isUpdate = false;

	public static final String TAG = "SWUpdateUtil";
	private static String updateUrl = "";

	public static void checkVersion(Context context) {

		try {
			mContext = context;
			PackageManager pm = mContext.getPackageManager();// 取得包管理器
			PackageInfo pi = pm.getPackageInfo(mContext.getPackageName(), 0);// 获取包的详细信息
			String oldVersion = pi.versionName;
			oldDayVersionName = oldVersion;
			TVCodeBean tvCodeBean = (TVCodeBean) UserShareUtils.getInstance()
					.getLoginInfo(context);
			oldDayVersion = pi.versionCode;
			AppInfo appInfo = tvCodeBean.getAppVersion();
			if (null != oldVersion && oldVersion.length() > 0
					&& appInfo != null) {
				//
				String[] array_version = oldVersion.split("\\.");
				int dayMajorVersion = Integer.parseInt(array_version[0]);
				int dayMinorVersion = Integer.parseInt(array_version[1]);
				int revision = Integer.parseInt(array_version[2]);
				if (dayMajorVersion < appInfo.getMajor()
						|| dayMinorVersion <  appInfo.getMinor()) {
					isExit = false;
					isUpdate =true;
				}else{
					isExit = true;
					isUpdate = false;
				}
				if (revision < appInfo.getRevision()) {
					isExit = true;
					isUpdate =true;
				}else{
					if(!isUpdate){
						isUpdate = false;
					}
				}
				verName = appInfo.getName();
				versionName = appInfo.getVersionName();
				verMessage = appInfo.getChangeLogs();
				updateUrl = appInfo.getAppUrl();
				Log.i(TAG, updateUrl);
				if(isUpdate){
					downHtml();
				}

			}
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

	}

	public static boolean isUpdate(Context context,AppInfo appInfo) {
		try {
			PackageManager pm = context.getPackageManager();// 取得包管理器
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);// 获取包的详细信息
			String oldVersion = pi.versionName;
			oldDayVersionName = oldVersion;
			oldDayVersion = pi.versionCode;
			String[] array_version = oldVersion.split("\\.");
			int dayMajorVersion = Integer.parseInt(array_version[0]);
			int dayMinorVersion = Integer.parseInt(array_version[1]);
			int revision = Integer.parseInt(array_version[2]);
			if(appInfo==null){
				return false;
			}
			if (dayMajorVersion < appInfo.getMajor()
					|| dayMinorVersion < appInfo.getMinor()) {
				isExit = false;
				isUpdate =true;
			}else{
				isExit = true;
				isUpdate = false;
			}
			if (revision < appInfo.getRevision()) {
				isExit = true;
				isUpdate =true;
			}else{
				if(!isUpdate){
					isUpdate = false;
				}
			}
			return isUpdate;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private static void downHtml() {
		Log.i(TAG, "downHtml");
		String appName = mContext.getString(R.string.app_name);
		AlertDialog.Builder showAlert = new AlertDialog.Builder(mContext);
		showAlert
				.setMessage(
						appName + versionName + " 更新内容如下:" + "\n" + verMessage)
				.setTitle("软件更新")
				.setPositiveButton("确定更新",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Uri uri = Uri.parse(updateUrl);
								Intent it = new Intent(Intent.ACTION_VIEW, uri);
								mContext.startActivity(it);
								
							}
						});
		if (isExit) {
			showAlert.setNegativeButton("取消更新",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		}
		showAlert.setCancelable(false).show();

	}
	public static void downHtml(final Context context,final AppInfo appInfo) {
		Log.i(TAG, "downHtml");
		String appName = context.getString(R.string.app_name);
		AlertDialog.Builder showAlert = new AlertDialog.Builder(context);
		showAlert
				.setMessage(
						appName + appInfo.getVersionName() + " 更新内容如下:" + "\n" + appInfo.getVersionDescription())
				.setTitle("软件更新")
				.setPositiveButton("确定更新",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Uri uri = Uri.parse(appInfo.getProgramUrl());
								Intent it = new Intent(Intent.ACTION_VIEW, uri);
								context.startActivity(it);
								
							}
						});
		if (isExit) {
			showAlert.setNegativeButton("取消更新",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		}
		showAlert.setCancelable(false).show();

	}
}
