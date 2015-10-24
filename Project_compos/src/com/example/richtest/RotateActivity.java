package com.example.richtest;

/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.example.richtest.bean.PicCompostion;

import flytv.compos.run.R;

public class RotateActivity extends Activity {

	private PhotoViewOther mImageView;

	private PhotoViewAttacherO mAttacher;
    public static boolean isHD = false;
	public static int imageWidth;
	public static int imageHeight;
	public static int viewWidth;
	public static int viewHeight;
	private Button rotate;
	private Button save;
	private Button cancel;
	private int r = 0;
	private String picname;
	private int index;
	com.nostra13.universalimageloader.core.ImageLoader imageLoader2;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.com_activity_main2);
		Display mDisplay = getWindowManager().getDefaultDisplay();

        if (mDisplay.getWidth() > 2000) {
            isHD = true;
        }
        imageLoader2 = com.nostra13.universalimageloader.core.ImageLoader
				.getInstance();
		index = getIntent().getIntExtra("DATA", 0);
		if (MainActivity.oldPicArrays != null
				&& MainActivity.oldPicArrays.size() > 0) {
			PicCompostion piccompostion = MainActivity.oldPicArrays.get(index);
			picname = piccompostion.picname;
		}
		// 旋转按钮
		rotate = (Button) findViewById(R.id.rotate);
		rotate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAttacher.setRotationBy(90);
				r = r + 1;
			}
		});
		//保存按钮
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int rr = r % 4;
				testNeedRotate(MainActivity.ALBUM_PATH + "/" + picname, rr);
				Intent intent = new Intent(RotateActivity.this,
						SimpleSampleActivity.class);
				intent.putExtra("DATA", index);
				startActivity(intent);
				finish();
			}
		});
		//取消按钮
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mImageView = (PhotoViewOther) findViewById(R.id.iv_photo);
		Log.i("test_tag","picname="+picname);
		Drawable bitmap = SimpleSampleActivity.getDefaultPic(picname);
		imageWidth = bitmap.getIntrinsicWidth();
		imageHeight = bitmap.getIntrinsicHeight();
		mImageView.setImageDrawable(bitmap);
		// The MAGIC happens here!
		mAttacher = new PhotoViewAttacherO(mImageView);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Need to call clean-up
		mAttacher.cleanup();
	}

	public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                w, h, matrix, true);
        return resizedBitmap;
    }

    public static Bitmap scaleImageView(Bitmap bitmap) {
        // 创建新的图片
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        // 计算缩放比例
        float scaleWidth = 1024f / w;
        float scaleHeight = 700f / h;
        float scale = scaleWidth < scaleHeight ? scaleWidth : scaleHeight;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                w, h, matrix, true);
        return resizedBitmap;
    }

    public static void testNeedRotate(String filePath, int degree) {
        if (degree != 0) {
            try {
                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                Bitmap cameraBitmap = BitmapFactory.decodeFile(filePath,
                        bitmapOptions);
                cameraBitmap = rotaingImageView(degree * 90, cameraBitmap);
                Bitmap bitmap = scaleImageView(cameraBitmap);
                Bitmap result = createBitmapWithPortrait(bitmap);
                File file = new File(filePath);
                if (file.exists()) {
                    file.delete();
                }
                BufferedOutputStream bos = new BufferedOutputStream(
                        new FileOutputStream(filePath));
                result.compress(Bitmap.CompressFormat.JPEG, 100, bos);// 将图片压缩到流中
                bos.flush();// 输出
                bos.close();// 关闭
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    public static Bitmap createBitmapWithPortrait(Bitmap portrait) {
        if (portrait != null) {
            // 图片的大小
            int portrait_W = portrait.getWidth();
            int portrait_H = portrait.getHeight();


            int left = (1024 - portrait_W) / 2;
            int top = (700 - portrait_H) / 2;
            int right = left + portrait_W;
            int bottom = top + portrait_H;

            RectF rect1 = new RectF(left, top, right, bottom);

            Bitmap bitmap = Bitmap.createBitmap(1024, 700, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawColor(0xffffffff);
            // 设置我们要绘制的范围大小
            Rect rect2 = new Rect(0, 0, portrait.getWidth(), portrait.getHeight());
            // 开始绘制
            canvas.drawBitmap(portrait, rect2, rect1, null);
            return bitmap;
        }
        return null;
    }
}
