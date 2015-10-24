/* 
 * Copyright 2012 Share.Ltd  All rights reserved.
 * Share.Ltd PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * @RecordButton.java - 2013-11-25 下午2:24:42 - anonymous
 */

package flytv.ext.view;

import java.io.File;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kubility.demo.MP3Recorder;

import flytv.ext.utils.FileUtil;
import flytv.ext.utils.SWDateUtil;
import flytv.ext.view.inter.OnRecordGetFileLister;


public class RecordButton extends Button {
	private OnRecordGetFileLister onRecordGetFileLister;

	public OnRecordGetFileLister getOnRecordGetFileLister() {
		return onRecordGetFileLister;
	}

	public void setOnRecordGetFileLister(
			OnRecordGetFileLister onRecordGetFileLister) {
		this.onRecordGetFileLister = onRecordGetFileLister;
	}

	public MP3Recorder getMp3Recorder() {
		return mp3Recorder;
	}

	public void setMp3Recorder(MP3Recorder mp3Recorder) {
		this.mp3Recorder = mp3Recorder;
	}

	private MP3Recorder mp3Recorder;
	private final String TEXT_NORMAL = "按下  录音";
	private final String TEXT_PRESSED = "松开  结束";
	private final String TEXT_CANCEL = "松开  取消";
	private final int NORMAL = 10001;
	private final int PRESSED = 10002;
	private final int CANCEL = 10003;
	public static String PATH_NAME;
	private Dialog mDialog;
	private ObtainDecibelThread mDecibelThread;
	private long mStartTime;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == NORMAL) {
				setText(TEXT_NORMAL);
			} else if (msg.what == PRESSED) {
				setText(TEXT_PRESSED);
			} else if (msg.what == CANCEL) {
				setText(TEXT_CANCEL);
			} else {
				if (onRecordGetFileLister != null) {
					onRecordGetFileLister.resultNumPathLister(msg.what);
				}
			}
		};
	};

	public RecordButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public RecordButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RecordButton(Context context) {
		super(context);
		init();
	}

	private void init() {
		mHandler.sendEmptyMessage(NORMAL);

	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startRecord();
			break;
		case MotionEvent.ACTION_MOVE:
			checkPosition(event);
			break;
		case MotionEvent.ACTION_UP:
			if (inRageOfView(this, event)) {
				stopRecord(true);
			} else {
				stopRecord(false);
			}
			break;
		}
		return true;
	}

	private void startRecord() {
		try {
			String filepath = FileUtil.noteLogFile + "/"
					+ SWDateUtil.getFormatDateByOffset("yyMMddHHssmm", 0)
					+ ".mp3";
			Log.i("tag", filepath);
			PATH_NAME = filepath;
			mp3Recorder.setFilePath(PATH_NAME);
			mp3Recorder.start();
			mStartTime = System.currentTimeMillis();
			mHandler.sendEmptyMessage(PRESSED);
			mDecibelThread = new ObtainDecibelThread();
			mDecibelThread.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	private void stopRecord(boolean isSave) {
		if (mDialog != null) {
			mDialog.dismiss();
		}
		if (mDecibelThread != null) {
			mDecibelThread.flag = false;
		}
		if (mp3Recorder != null) {
			try {
				mp3Recorder.stop();
			} catch (Exception e) {
			}
		}
		long intervalTime = System.currentTimeMillis() - mStartTime;
		if (!isSave || intervalTime < 2000) {
			if (isSave) {
				Toast.makeText(getContext(), "录音时间太短！", Toast.LENGTH_SHORT)
						.show();
			
			}
			File file = new File(PATH_NAME);
			if (file.exists()) {
				file.delete();
			}
		} else {
			if (onRecordGetFileLister != null) {
				onRecordGetFileLister.resultAMRPathLister(PATH_NAME);
			}
		}
		mHandler.sendEmptyMessage(NORMAL);
	}

	private void checkPosition(MotionEvent event) {
		if (inRageOfView(this, event)) {
			mHandler.sendEmptyMessage(PRESSED);
		} else {
			mHandler.sendEmptyMessage(CANCEL);
		}
	}

	/**
	 * 判断点击位置是否在指定View上
	 */
	private boolean inRageOfView(View v, MotionEvent ev) {
		int[] loc = new int[2];
		v.getLocationOnScreen(loc);
		int x = loc[0];
		int y = loc[1];
		if (ev.getRawX() > x && ev.getRawY() > y
				&& ev.getRawX() < (x + getWidth())
				&& ev.getRawY() < (y + getHeight())) {
			return true;
		} else {
			return false;
		}
	}

	public static File getExternalCacheDir(Context context) {
		String cacheDir = null;
		if (TextUtils.equals(Environment.getExternalStorageState(),
				Environment.MEDIA_MOUNTED)) {
			if (Build.VERSION.SDK_INT > Build.VERSION_CODES.FROYO
					&& context.getExternalCacheDir() != null) {
				cacheDir = context.getExternalCacheDir().getPath();
			}
			if (TextUtils.isEmpty(cacheDir)) {
				cacheDir = "/Android/data/" + context.getPackageName() + "/";
			}
		} else {
			cacheDir = context.getCacheDir().getPath();
		}
		File file = new File(cacheDir);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file;
	}

	public String getRecordPath() {
		return PATH_NAME;
	}

	int index_end;

	class ObtainDecibelThread extends Thread {
		public boolean flag = true;

		@Override
		public void run() {
			while (flag) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (mp3Recorder == null || !flag) {
					break;
				}
				index_end = index_end + 1;
				int x = index_end;
				if (x == 1) {
					mHandler.sendEmptyMessage(0);
				} else if (x == 2) {
					mHandler.sendEmptyMessage(1);
				} else if (x == 3) {
					mHandler.sendEmptyMessage(2);
				} else {
					mHandler.sendEmptyMessage(3);
					index_end = 0;
				}
				/*
				 * if (x != 0) { int f = (int) (10 * Math.log(x) /
				 * Math.log(10)); if (f < 26) { mHandler.sendEmptyMessage(0); }
				 * else if (f < 32) { mHandler.sendEmptyMessage(1); } else if (f
				 * < 38) { mHandler.sendEmptyMessage(2); } else {
				 * mHandler.sendEmptyMessage(3); } }
				 */
			}
		}

	}

}
