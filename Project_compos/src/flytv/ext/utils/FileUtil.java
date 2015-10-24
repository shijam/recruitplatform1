package flytv.ext.utils;

import java.io.File;

import android.content.Context;
import android.os.Environment;

public class FileUtil {
	public static String homeFile = "FlytvLive";

	public static String noteTheFile = homeFile + "/theme";
	public static String noteImageFile = homeFile + "/image";
	public static String noteSdImageFile = homeFile + "/sdcarImage";
	public static String noteLogFile = homeFile + "/logError";
	public static String noteTheIcFile = noteTheFile + "/icon";

	public static void execuFile(Context context) {
		homeFile = "FlytvLive";
		homeFile = Environment.getExternalStorageDirectory() + "/" + homeFile;
		File file = new File(homeFile);
		noteTheFile = homeFile + "/theme";
		noteImageFile = homeFile + "/image";
		noteSdImageFile = homeFile + "/sdcarImage";
		noteLogFile = homeFile + "/logError";
		noteTheIcFile = noteTheFile + "/icon";
		LogUtils.print("file =" + file.exists() + "||" + homeFile);
		if (!file.exists()) {
			boolean isHome = file.mkdir();
			if (!isHome) {
				file = context.getDir("FlytvLive", Context.MODE_WORLD_READABLE);
			}
			homeFile = file.getAbsolutePath();
			isHome = file.mkdir();
			noteTheFile = homeFile + "/theme";
			noteImageFile = homeFile + "/image";
			noteSdImageFile = homeFile + "/sdcarImage";
			noteLogFile = homeFile + "/logError";
			noteTheIcFile = noteTheFile + "/icon";

			File theFile = new File(noteTheFile);
			File noteFile = new File(noteImageFile);
			File logFile = new File(noteLogFile);
			File noteSdFile = new File(noteSdImageFile);
			if (isHome) {
				execuMkdir(theFile);
				execuMkdir(noteFile);
				execuMkdir(logFile);
				execuMkdir(noteSdFile);

			} else {
				execuMkdir(theFile);
				execuMkdir(noteFile);
				execuMkdir(logFile);
				execuMkdir(noteSdFile);
			}
			LogUtils.print("file =" + file.exists() + "" + file.isFile());

		} else {

		}
	}

	private static void execuMkdir(File file) {
		if (!file.exists()) {
			file.mkdir();
		}
	}
}
