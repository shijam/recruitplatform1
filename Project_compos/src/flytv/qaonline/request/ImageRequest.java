package flytv.qaonline.request;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import flytv.qaonline.http.HttpServer;
import flytv.qaonline.image.BitmapUtils;
import flytv.qaonline.image.ImageFileCache;
import flytv.qaonline.image.ImageMemoryCache;
import flytv.qaonline.image.BitmapUtils.BitmapStyle;

public class ImageRequest {
	public static final String IMAGE_SUFFIX = ".pic";
	private String mImageKey;
	private static ImageRequest mImageRequest;
	private Map<String, String> mDownLoadList;
	private ImageMemoryCache mMemoryCache;
	private ImageFileCache mFileCache;
	
	public static ImageRequest getImageRequest(Context context) {
		if(mImageRequest == null){
			mImageRequest = new ImageRequest(context);
		}
		return mImageRequest;
	}
	
	private ImageRequest(Context context) {
		mDownLoadList = new HashMap<String,String>();
		mMemoryCache = new ImageMemoryCache(context);
		mFileCache = new ImageFileCache();  
	}
	
	public Bitmap getImageDataFromMemory(String imageUrl,final BitmapStyle bitmapStyle){
		if(imageUrl == null || "".equals(imageUrl)){
			return null;
		}
		Bitmap bitmap = mMemoryCache.getBitmapFromCache(imageUrl+"_"+bitmapStyle);
		return bitmap;
	}
	
	public Bitmap getImageData(String imageUrl,boolean isSave,int width,int height,BitmapStyle bitmapStyle){
		if(imageUrl == null || "".equals(imageUrl)){
			return null;
		}
        // 文件缓存中获取
    	Bitmap bitmap = mFileCache.getImage(imageUrl,width,height);
        if (bitmap == null){
        	if(!mDownLoadList.containsKey(mImageKey)){ // 防止重复下载
        		bitmap = HttpServer.downloadBitmap(imageUrl,width,height);
        		if (bitmap != null) {
        			if(isSave){
        				mFileCache.saveBitmap(bitmap, imageUrl);
        			}
        			switch (bitmapStyle) {
	    				case CIRCLE:
	    					bitmap = BitmapUtils.toRoundBitmap(bitmap);
	    					break;
	    				case ROUND:
	    					bitmap = BitmapUtils.getRoundedCornerBitmap(bitmap, 8);
	    					break;
	    				case REFLECTION:
	    					bitmap = BitmapUtils.createReflectionImageWithOrigin(bitmap);
	    					break;
	    				case SHADOW:
	    					bitmap = BitmapUtils.drawImageDropShadow(bitmap);
	    					break;
	    				default:
	    					break;
	    			}
        			mMemoryCache.addBitmapToCache(imageUrl+"_"+bitmapStyle, bitmap);
        		}
        	}
        } else {// 添加到内存缓存
        	switch (bitmapStyle) {
				case CIRCLE:
					bitmap = BitmapUtils.toRoundBitmap(bitmap);
					break;
				case ROUND:
					bitmap = BitmapUtils.getRoundedCornerBitmap(bitmap, 15);
					break;
				case REFLECTION:
					bitmap = BitmapUtils.createReflectionImageWithOrigin(bitmap);
					break;
				default:
					break;
			}
        	mMemoryCache.addBitmapToCache(imageUrl+"_"+bitmapStyle, bitmap);
        }
	    return bitmap;
	}
	
	public void clear(){
		if(mMemoryCache != null)
			mMemoryCache.clearCache();
		if(mDownLoadList != null)
			mDownLoadList.clear();
	}
}
