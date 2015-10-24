package flytv.qaonline.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import flytv.compos.run.MyMP3Dialog;
import flytv.qaonline.image.ImageFileCache;

public class CapturePhoto {
	public interface PhotoIntentListener{
		public void finishPhotoIntent(String filePath,Bitmap fileBitmap,int type);
	}
	public static final int SHOT_IMAGE = 0x101;
	public static final int PICK_IMAGE = 0x102;
	public static final int CORP_IMAGE = 0x103;
	public static final int SHOT_VIDEO = 0x104;
	public static final int SOUND_RECORD = 0x105;
	
	private File tempFile;
	private Uri mPhotoUri;
	private Uri mVideoUri;
	private Activity mActivity;
	private File mCopyFile;
	private PhotoIntentListener mPhotoIntentListener;
	
	public CapturePhoto(Activity activity,PhotoIntentListener photoIntentListener) {
		this.mActivity = activity;
		this.mPhotoIntentListener = photoIntentListener;
	}
	
	public void dispatchPictureIntent(int actionCode){
		Intent pictureIntent = null;
		switch(actionCode) {
			case SHOT_IMAGE:
				tempFile = getOutputMediaFile(SHOT_IMAGE);
				if(tempFile != null && tempFile.exists()){
					mPhotoUri = Uri.fromFile(tempFile);
				}
				pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,mPhotoUri);
				break;
			case PICK_IMAGE :
				pictureIntent = new Intent(Intent.ACTION_PICK,
					     android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				break;
			case SHOT_VIDEO:
				pictureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
				pictureIntent.putExtra(android.provider.MediaStore.EXTRA_SIZE_LIMIT,768000);
				tempFile = getOutputMediaFile(SHOT_VIDEO);
				if(tempFile != null && tempFile.exists()){
					mVideoUri = Uri.fromFile(tempFile);
				}
				// 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null，如果此处指定，则后来的data为null
				// set the image file name
				pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoUri);
				//设置视频时间  毫秒单位
				pictureIntent.putExtra(android.provider.MediaStore.EXTRA_DURATION_LIMIT, 45000);
				break;
			case SOUND_RECORD:
				pictureIntent = new Intent(this.mActivity,MyMP3Dialog.class);
				break;
			default:
				break;			
		}
		if(pictureIntent != null) {
			mActivity.startActivityForResult(pictureIntent,actionCode);
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data){
		switch(requestCode) {
			case CapturePhoto.SHOT_IMAGE:
				if(tempFile != null && tempFile.exists()){
					startCropPic(CapturePhoto.SHOT_IMAGE);
				}else{
					mPhotoIntentListener.finishPhotoIntent("", null,CapturePhoto.SHOT_IMAGE);
				}
				break;
			case CapturePhoto.PICK_IMAGE:
				if(data != null){
					mPhotoUri = data.getData();
					startCropPic(CapturePhoto.PICK_IMAGE);
				}else{
					mPhotoIntentListener.finishPhotoIntent("", null,CapturePhoto.PICK_IMAGE);
				}
				break;
			case CapturePhoto.CORP_IMAGE:
				if(tempFile != null && tempFile.exists()){
					tempFile.delete();
				}
				if (mCopyFile != null && mCopyFile.exists()) {
					mPhotoIntentListener.finishPhotoIntent(mCopyFile.getAbsolutePath(), null,CapturePhoto.CORP_IMAGE);
				}else{
					mPhotoIntentListener.finishPhotoIntent("", null,CapturePhoto.CORP_IMAGE);
				}
			case CapturePhoto.SHOT_VIDEO:
				if (tempFile != null && tempFile.exists()) {
					mPhotoIntentListener.finishPhotoIntent(tempFile.getAbsolutePath(), null,CapturePhoto.SHOT_VIDEO);
				}else{
					mPhotoIntentListener.finishPhotoIntent("", null,CapturePhoto.SHOT_VIDEO);
				}
				break;
			case CapturePhoto.SOUND_RECORD:
				if(data != null){
					mPhotoIntentListener.finishPhotoIntent(data.getStringExtra("FileImage"), null,CapturePhoto.SOUND_RECORD);
				}else{
					mPhotoIntentListener.finishPhotoIntent("", null,CapturePhoto.SOUND_RECORD);
				}
				break;
			default:
				break;
		}
	}
	public void startCropPic(int actionCode){
		mCopyFile = getOutputMediaFile(SHOT_IMAGE);
		Intent pictureIntent = new Intent("com.android.camera.action.CROP");
		pictureIntent.setDataAndType(mPhotoUri, "image/*");
		pictureIntent.putExtra("crop", "true");
		pictureIntent.putExtra("outputX", 600);
		pictureIntent.putExtra("outputY", 600);
		pictureIntent.putExtra("scale", true);
		pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(mCopyFile));
		pictureIntent.putExtra("return-data", false);
		pictureIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
		pictureIntent.putExtra("noFaceDetection", true);
		mActivity.startActivityForResult(pictureIntent,CORP_IMAGE);
	}
	
//	private void doBitmapFile(Intent data,int type){
//		Bundle extras = data.getExtras();
//		if (extras != null){
//			Bitmap bitmap = extras.getParcelable("data");
//			if (bitmap != null){
//				if(mFilePath == null || "".equals(mFilePath)){
//					String filename = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
//					mFilePath = ImageFileCache.getImageDirectory()+filename+".jpg";
//				}
//				BitmapUtils.saveBitmapFile(mFilePath,bitmap, true);
//				galleryAddPic(Uri.fromFile(new File(mFilePath)));
//				if (bitmap != null && bitmap.isRecycled()) {
//					bitmap.recycle();
//				}
//				mPhotoIntentListener.finishPhotoIntent(mFilePath,null,type);
//			}
//		}
//	}
  
	private void galleryAddPic(Uri uri){
	    Intent mediaScanIntent = new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE");
	    mediaScanIntent.setData(uri);
	    mActivity.sendBroadcast(mediaScanIntent);
	}	
	
	private File getOutputMediaFile(int type) {
		File tempFile = null;
		String filename = new SimpleDateFormat("yyMMddHHmmss").format(new Date());
		if (type == SHOT_IMAGE) {
			tempFile = new File(ImageFileCache.getImageDirectory()+filename+".jpg");
		} else{
			tempFile = new File(ImageFileCache.getImageDirectory()+"VID_"+filename+".mp4");
		}
		File parent = new File(tempFile.getParent());
		if(!parent.exists()){
			parent.mkdirs();
		}
		if(!tempFile.exists()){ 
			try {
				 tempFile.createNewFile(); 
			} catch (IOException e) {
				tempFile = null;
			}
		}
		return tempFile;
	}
}
