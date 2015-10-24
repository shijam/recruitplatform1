package com.example.richtest;

import java.io.File;

import android.os.Environment;

public class FileUtil {
	public static String homeFile = Environment.getExternalStorageDirectory()
			+ "/GeHuaPhone";

	public static String noteTheFile = homeFile + "/theme";
	public static String noteImageFile = homeFile + "/image";
	public static String noteSdImageFile = homeFile + "/sdcarImage";
	public static String noteLogFile = homeFile + "/logError";
	public static String noteTheIcFile = noteTheFile + "/icon";

	public static void execuFile() {

		File file = new File(homeFile);

		if (!file.isFile()) {
			boolean isHome = file.mkdir();
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
		}
	}

	private static void execuMkdir(File file) {
		if (!file.exists()) {
			file.mkdir();
		}
	}
}
