package flytv.qaonline.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public class DensityConfig {
	private final float SCALE_PAGETITLE_HEIGHT = 0.07F;
	private final float SCALE_QA_ITEM_HEIGHT = 0.15F;
	private final float SCALE_QADETAIL_ITEM_HEIGHT = 0.08F;

	private Context mContext;
	private int mScreenWidth;
	private int mScreenHeight;
	private int mPageTitleHeight;
	private int mQaItemHeight;
	private int mQaDetailItemHeight;
	
	private static DensityConfig mDensityConfig;

	private DensityConfig(Context context) {
		this.mContext = context;
		DisplayMetrics dm = new DisplayMetrics();  
		dm = context.getResources().getDisplayMetrics();
		if(dm.widthPixels > dm.heightPixels){ // 横屏
			mScreenWidth  = dm.heightPixels;     
			mScreenHeight = dm.widthPixels;    
		}else{ // 竖屏
			mScreenWidth  = dm.widthPixels;     
			mScreenHeight = dm.heightPixels;    
		}
		mPageTitleHeight = new Float(SCALE_PAGETITLE_HEIGHT*mScreenHeight).intValue();
		mQaItemHeight = new Float(SCALE_QA_ITEM_HEIGHT*mScreenHeight).intValue();
		mQaDetailItemHeight = new Float(SCALE_QADETAIL_ITEM_HEIGHT*mScreenHeight).intValue();
	}
	
	
	public static DensityConfig getDensityConfig(Context context){
		if(mDensityConfig == null){
			mDensityConfig = new DensityConfig(context);
		}
		return mDensityConfig;
	}
	
	public int getPageTitleHeight() {
		return mPageTitleHeight;
	}
	
	public int getQaItemHeight() {
		return mQaItemHeight;
	}
	
	public int getQaDetailItemHeight() {
		return mQaDetailItemHeight;
	}
	
	public int getScreenWidth(){
		return mScreenWidth;
	}
	
	 /** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    
    /**        
     * 将px值转换为sp值，保证文字大小不变          
     * @param pxValue           
     * @param fontScale    
     * （DisplayMetrics类中属性scaledDensity）          
     * @return          
     */          
    public static int px2sp(Context context, float pxValue) {               
    	final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;               
    	return (int) (pxValue / fontScale + 0.5f);           
    }  
    
    /** 
     * 将sp值转换为px值，保证文字大小不变           *        
     * @param spValue           
     * @param fontScale（DisplayMetrics类中属性scaledDensity）      
     * @return           
     */          
    public static int sp2px(Context context, float spValue){ 
    	final float fontScale = context.getResources().getDisplayMetrics().scaledDensity; 
    	return (int) (spValue * fontScale + 0.5f);      
    } 
	 
}
