package flytv.ext.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import flytv.compos.run.MyMP3Dialog;
import flytv.compos.run.R;
import flytv.compos.run.adpublic.AyVideoCompoe;
import flytv.ext.tools.AlertTools;
import flytv.ext.view.inter.OnClickDialogListener;
import flytv.qaonline.ui.MyImageActivity;

public class AlertHelp {
	private static final String TAG = "AlertHlep";
	private static String[] items = new String[] { "选择本地图片", "拍照" };
	private static String[] items_ext = new String[] { "选择本地图片", "拍照","视频","录音" };
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	public static Uri fileUri;;
	public static int width,height;
	
	public static void startResource(Context context,String httpUrl){
		Intent intent = new Intent();
		if(AppUtil.isStrNull(httpUrl)){
			return;
		}
		intent.putExtra("httpUrl", httpUrl);
		if(httpUrl.contains("jpg")||httpUrl.contains("png")||httpUrl.contains("img")){
			intent.setClass(context, MyImageActivity.class);
		}else if(httpUrl.contains("mp3")){
			PlayerAmrUtil.getInster(context).play(httpUrl, !httpUrl.contains("http://"));
			return;
		}else if(httpUrl.contains("mp4")){
				intent.setClass(context, AyVideoCompoe.class);
		}else if(httpUrl.contains("pdf")){
			return;
		}
		context.startActivity(intent);
	}
	public static void showVideoIntent(Context context){
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		//设置视频大小
		intent.putExtra(android.provider.MediaStore.EXTRA_SIZE_LIMIT,
		768000);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

		// 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
		// set the image file name
		intent
				.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		//设置视频时间  毫秒单位
		intent.putExtra(
		android.provider.MediaStore.EXTRA_DURATION_LIMIT, 45000);
		((Activity) context).startActivityForResult(intent, 400);
	}
	public static void showAudio(Context context){
		Intent intent = new Intent(context,MyMP3Dialog.class);
		((Activity) context).startActivityForResult(intent, 500);
	}
	
	public static void getDisplay(Context context) {

		Display display = ((Activity) context).getWindow().getWindowManager()
				.getDefaultDisplay();
		width = display.getWidth();
		height = display.getHeight();
	}
	public static void showDialog(final Context context) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context)
				.setTitle("设置图像")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						execuIntent(context,which);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alert.show();
	}
	public static void showDelete(final Context context,final int position,String message,final OnClickDialogListener onClickDialogListener) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context)
				.setTitle("删除提示").setMessage(message)
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(onClickDialogListener!=null){
							onClickDialogListener.cmdIndex(position);
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alert.show();
	}
	public static void showDialogExt(final Context context) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context)
				.setTitle("设置图像")
				.setItems(items_ext, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						execuIntent(context,which);
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alert.show();
	}
	private static void execuIntent(Context context,int which){
		switch (which) {
		case 0:
			Intent intent = new Intent();
			intent.setType("image/*");
			intent.setAction(Intent.ACTION_GET_CONTENT);
			((Activity) context).startActivityForResult(intent,
					200);
			break;
		case 1:
			try {
				// 利用系统自带的相机应用:拍照
				Intent intentPho = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);

				// create a file to save the image
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

				// 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
				// set the image file name
				intentPho
						.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

				((Activity) context).startActivityForResult(
						intentPho, 300);
			} catch (Exception e) {
				e.printStackTrace();
				AlertTools.showTips(context, R.drawable.tips_warning, "无法调用相册 没有SD卡!");
			}
			break;
		case 2:
			showVideoIntent(context);
			break;
		case 3:
			showAudio(context);
			break;
		}
	}

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	public static File mediaFile = null;

	/** Create a File for saving an image or video */
	@SuppressLint("SimpleDateFormat")
	private static File getOutputMediaFile(int type) {
		File mediaStorageDir = null;
		try {

			mediaStorageDir = new File(Environment
					.getExternalStoragePublicDirectory(
							Environment.DIRECTORY_PICTURES).getPath());
			Log.d(TAG, "Successfully created mediaStorageDir: "
					+ mediaStorageDir);

		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "Error in Creating mediaStorageDir: " + mediaStorageDir);
		}

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				// 在SD卡上创建文件夹需要权限：
				// <uses-permission
				// android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
				Log.d(TAG,
						"failed to create directory, check if you have the WRITE_EXTERNAL_STORAGE permission");
				return null;
			}
		}

		// Create a media file name

		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String timeStamp = dataFormat.format(new Date());
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "VID_" + timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}
	
	
	public static int[] getRect(Activity context) {
		int rect[] = new int[2];
		Display display = context.getWindowManager().getDefaultDisplay();
		rect[0] = display.getWidth();
		rect[1] = display.getHeight();
		return rect;
	}

}
