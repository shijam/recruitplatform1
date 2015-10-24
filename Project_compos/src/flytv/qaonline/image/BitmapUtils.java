package flytv.qaonline.image;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

public class BitmapUtils {
	public static enum BitmapStyle{
		NULL,CIRCLE,ROUND,REFLECTION,SHADOW
	}
	/**
	 * saveBitmap
	 */
	public static void saveBitmapFile(String path,Bitmap bitmap, boolean isDelete) {
		File file = new File(path);
		File parent = new File(file.getParent());
		if (!parent.exists()) {
			parent.mkdirs();
		}
		if (isDelete) {
			if (file.exists()) {
				file.delete();
			}
		}
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
				out.flush();
			}
		}catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static Bitmap getFileBitmap(File file,int deswidth,int desheight) {
		InputStream optionsInputStream = null;
		InputStream bitmapinputStream = null;
		Bitmap bitmap = null;
		try {
			optionsInputStream = new FileInputStream(file);
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds=true;
			BitmapFactory.decodeStream(optionsInputStream, null, options);
			int width = options.outWidth;
			int height = options.outHeight;
			int sampleSize = 1;
			while ((width/sampleSize > deswidth) || (height/sampleSize > desheight) ) {
				sampleSize *=2;
			}
			bitmapinputStream = new FileInputStream(file);
			options.inJustDecodeBounds = false;
			options.inInputShareable = true;
			options.inPurgeable = true;
			options.inSampleSize = sampleSize;
			bitmap = BitmapFactory.decodeStream(bitmapinputStream, null, options);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if (optionsInputStream!=null) {
				try {
					optionsInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bitmapinputStream!=null) {
				try {
					bitmapinputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}
	
	public static Bitmap getBitmap(byte[] data,int desWidth,int desHeight) throws Exception{
		Options options = new Options();
		options.inJustDecodeBounds=true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);
		int x = options.outWidth;
		int y = options.outHeight;
		int sample = 1;
		while ( (x/sample > desWidth) || (y/sample > desHeight) ) {
			sample *= 2; 
		}
		options.inJustDecodeBounds = false;
		options.inInputShareable = true;
		options.inPurgeable = true;
		options.inSampleSize = sample;
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}
	
	/**
	* 放大缩小图片
	*/
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
	   int width = bitmap.getWidth();
	   int height = bitmap.getHeight();
	   Matrix matrix = new Matrix();
	   float scaleWidht = ((float) w / width);
	   float scaleHeight = ((float) h / height);
	   matrix.postScale(scaleWidht, scaleHeight);
	   Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
	   return newbmp;
	}

	/**
	* 将Drawable转化为Bitmap
	*/
	public static Bitmap drawableToBitmap(Drawable drawable) {
	   int width = drawable.getIntrinsicWidth();
	   int height = drawable.getIntrinsicHeight();
	   Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
	   Canvas canvas = new Canvas(bitmap);
	   drawable.setBounds(0, 0, width, height);
	   drawable.draw(canvas);
	   return bitmap;
	}

	/**
	* 获得圆角图片的方法
	*/
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
	   Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
	   Canvas canvas = new Canvas(output);

	   final int color = 0xff424242;
	   final Paint paint = new Paint();
	   final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	   final RectF rectF = new RectF(rect);

	   paint.setAntiAlias(true);
	   canvas.drawARGB(0, 0, 0, 0);
	   paint.setColor(color);
	   canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	   paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	   canvas.drawBitmap(bitmap, rect, rect, paint);
	   return output;
	}

	/**
	* 获得带倒影的图片方法
	*/
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
	   final int reflectionGap = 4;
	   int width = bitmap.getWidth();
	   int height = bitmap.getHeight();

	   Matrix matrix = new Matrix();
	   matrix.preScale(1, -1);

	   Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);

	   Bitmap bitmapWithReflection = Bitmap.createBitmap(width, (height + height / 2), Config.ARGB_8888);

	   Canvas canvas = new Canvas(bitmapWithReflection);
	   canvas.drawBitmap(bitmap, 0, 0, null);
	   Paint deafalutPaint = new Paint();
	   canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

	   canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

	   Paint paint = new Paint();
	   LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0, bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
	   paint.setShader(shader);
	   // Set the Transfer mode to be porter duff and destination in
	   paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
	   // Draw a rectangle using the paint with our linear gradient
	   canvas.drawRect(0, height, width, bitmapWithReflection.getHeight() + reflectionGap, paint);
	   return bitmapWithReflection;
  }
	
	/**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();
            float roundPx;
            float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
            if (width <= height) {
                roundPx = width / 2;
                top = 0;
                bottom = width;
                left = 0;
                right = width;
                height = width;
                dst_left = 0;
                dst_top = 0;
                dst_right = width;
                dst_bottom = width;
            } else {
                roundPx = height / 2;
                float clip = (width - height) / 2;
                left = clip;
                right = width - clip;
                top = 0;
                bottom = height;
                width = height;
                dst_left = 0;
                dst_top = 0;
                dst_right = height;
                dst_bottom = height;
            }
            
            Bitmap output = Bitmap.createBitmap(width,height, Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            
            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
            final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
            final RectF rectF = new RectF(dst);

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
            canvas.drawBitmap(bitmap, src, dst, paint);
            return output;
    }
	
    /**

     * add shadow to bitmap
     * @param originalBitmap
     * @return
     */
    public static Bitmap drawImageDropShadow(Bitmap originalBitmap) {
        BlurMaskFilter blurFilter = new BlurMaskFilter(1,
                BlurMaskFilter.Blur.NORMAL);
        Paint shadowPaint = new Paint();
        shadowPaint.setAlpha(50);
        shadowPaint.setColor(Color.parseColor("#FFFF0000"));
        shadowPaint.setMaskFilter(blurFilter);
        int[] offsetXY = new int[2];
        Bitmap shadowBitmap = originalBitmap.extractAlpha(shadowPaint, offsetXY);
        Bitmap shadowImage32 = shadowBitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(shadowImage32);
        c.drawBitmap(originalBitmap, offsetXY[0], offsetXY[1], null);
        return shadowImage32;
    }

    /**
     *压缩图片大小
     */
  	public static Bitmap compressImage(Bitmap image){
  		ByteArrayOutputStream baos = new ByteArrayOutputStream();
  		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
  		int options = 100;
  		while ( baos.toByteArray().length / 1024>100) {	//循环判断如果压缩后图片是否大于100kb,大于继续压缩		
  			baos.reset();//重置baos即清空baos
  			image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
  			options -= 10;//每次都减少10
  		}
  		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
  		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
  		return bitmap;
  	}
  	
  	/** 
     * 将彩色图转换为灰度图 
     * @param img 位图 
     * @return  返回转换好的位图 
     */  
    public Bitmap convertGreyImg(Bitmap img) {  
        int width = img.getWidth();         //获取位图的宽  
        int height = img.getHeight();       //获取位图的高  
        int []pixels = new int[width * height]; //通过位图的大小创建像素点数组  
        img.getPixels(pixels, 0, width, 0, 0, width, height);  
        int alpha = 0xFF << 24;   
        for(int i = 0; i < height; i++)  {  
            for(int j = 0; j < width; j++) {  
                int grey = pixels[width * i + j];  
                int red = ((grey  & 0x00FF0000 ) >> 16);  
                int green = ((grey & 0x0000FF00) >> 8);  
                int blue = (grey & 0x000000FF);  
                grey = (int)((float) red * 0.3 + (float)green * 0.59 + (float)blue * 0.11);  
                grey = alpha | (grey << 16) | (grey << 8) | grey;  
                pixels[width * i + j] = grey;  
            }  
        }  
        Bitmap result = Bitmap.createBitmap(width, height, Config.RGB_565);  
        result.setPixels(pixels, 0, width, 0, 0, width, height);  
        return result;  
    } 
      
 
      
}
