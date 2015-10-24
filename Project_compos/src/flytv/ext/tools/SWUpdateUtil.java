package flytv.ext.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.util.Log;
import flytv.ext.utils.AppUtil;
import flytv.run.bean.AppInfo;
/**
 * 更新提示工具包
 * @author nike
 *
 */
public class SWUpdateUtil {

	private static Activity mContext = null;
	private static String verMessage = "";
	private static String verName = "";
	public static boolean isCommit = false;
	public static final String TAG = "SWUpdateUtil";
	private static String updateUrl = "";

	public static void checkVersion(Activity context, AppInfo appInfo) {
		mContext = context;
		AppUtil.getInfo(mContext);
		Log.i(TAG, "checkVersion");
		String newVersion = appInfo.getVersionName();
		PackageManager pm = mContext.getPackageManager();// 取得包管理器
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			String oldVersion = pi.versionName;
			if (null != oldVersion && oldVersion.length() > 0) {
				Log.i(TAG, "oldVersion:" + oldVersion + " newVersion:"
						+ newVersion);
				Log.i(TAG, "compare:" + oldVersion.compareTo(newVersion));
				if (oldVersion.compareTo(newVersion) < 0) {
					verName = appInfo.getVersionName();
					verMessage = appInfo.getVersionDescription();
					updateUrl = appInfo.getProgramUrl();
					Log.i(TAG, updateUrl);
					downHtml();
				} else {

				}
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// 获取包的详细信息

	}

	private static void downHtml() {
		Log.i(TAG, "downHtml");
		new AlertDialog.Builder(mContext)
				.setMessage(
						"爱作文 " + verName + " for Android 更新内容如下:" + "\n"
								+ verMessage)
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
						})
				.setNegativeButton("取消更新",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).setCancelable(false).show();

	}

}
