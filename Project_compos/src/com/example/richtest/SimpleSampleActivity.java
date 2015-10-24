package com.example.richtest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.baidu.android.voicedemo.Config;
import com.baidu.android.voicedemo.Constants;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;
import com.example.richtest.PhotoViewAttacher.OnMatrixChangedListener;
import com.example.richtest.PhotoViewAttacher.OnPhotoTapListener;
import com.example.richtest.bean.PicCompostion;

import flytv.compos.run.R;

@SuppressLint("NewApi")
public class SimpleSampleActivity extends Activity {

	static final String PHOTO_TAP_TOAST_STRING = "Photo Tap! X: %.2f %% Y:%.2f %% ID: %d";
	static final String SCALE_TOAST_STRING = "Scaled to: %.2ff";

	public static boolean isFirst = true;
	private PhotoView mImageView;
	private DrawingWithBezier mCustomView;
	// private TextView mCurrMatrixTv;
	private FrameLayout rootLayout;
	private RelativeLayout buttom;

	private PhotoViewAttacher mAttacher;
	private boolean isRedo = false;
	private Toast mCurrentToast;
	public static boolean isHD = true;
	private static int btnW = 20;
	private Matrix mCurrentDisplayMatrix = null;
	public static float scale = 1f;
	public static float xxx = 0f;
	public static float yyy = 0f;
	public static boolean isEdit = false;
	// private ArrayList<PointF> points = new ArrayList<PointF>();
	private ArrayList<PointEntity> olds = new ArrayList<PointEntity>();
	private HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
	public static int imageWidth;
	public static int imageHeight;
	public static int viewWidth;
	public static int viewHeight;
	private Button revoke;
	private Button read;
	private Button write;
	private Button rotate;
	private Button save;
	private Button cancel;
	private String picname;

	public static HashMap<Integer, PointEntity> news = new HashMap<Integer, PointEntity>();
	public static ArrayList<Path> tempPathList = new ArrayList<Path>();
	public static Bitmap tempBm;
	private int indexs;
	public int windowW;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.com_activity_main1);
		Display mDisplay = getWindowManager().getDefaultDisplay();
		if(news!=null){
			news.clear();	
		}else{
			 news = new HashMap<Integer, PointEntity>();
		}
		if (mDisplay.getWidth() > 2000) {
			windowW = mDisplay.getWidth();
			isHD = true;
			btnW = 40;
		}

		indexs = getIntent().getIntExtra("DATA", 0);

		String imageName = getIntent().getStringExtra("imageName");
		if (imageName != null && imageName.length() > 0) {// 二次编辑
			olds = null;
			olds = MainActivity.nowOlePoints;
			picname = imageName;
			isRedo = true;
		} else {
			if (MainActivity.oldPicArrays != null
					&& MainActivity.oldPicArrays.size() > 0) {

				PicCompostion piccompostion = MainActivity.oldPicArrays
						.get(indexs);
				olds = null;
				olds = piccompostion.oldPointEntitys;
				picname = piccompostion.picname;
			}
		}

		if (tempPathList == null) {
			tempPathList = new ArrayList<Path>();
		}

		if (news == null) {
			news = new HashMap<Integer, PointEntity>();
		}
		tempBm = null;
		// 工具栏
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.main);
		View view = LayoutInflater.from(this).inflate(
				R.layout.com_activity_main1_buttom, null, false);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				mDisplay.getWidth(), 200);
		layoutParams.leftMargin = 0;
		layoutParams.topMargin = mDisplay.getHeight() - 200;

		layoutParams.rightMargin = 0;
		layoutParams.bottomMargin = 0;
		layout.addView(view, layoutParams);

		revoke = (Button) view.findViewById(R.id.revoke);// 撤销
		revoke.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mImageView.uodoLine();
			}
		});
		read = (Button) view.findViewById(R.id.read);// 非编辑状态
		read.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isEdit = false;
				mImageView.refreshMatrix();
				read.setSelected(true);
				write.setSelected(false);

			}
		});
		write = (Button) view.findViewById(R.id.write);// 编辑状态
		write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				read.setSelected(false);
				write.setSelected(true);
				mImageView.refreshMatrix();
				tempBm = null;

				isEdit = true;
				mImageView.setInit();
				calc();
				mImageView.setMyscale(scale);
			}

		});

		save = (Button) view.findViewById(R.id.save);// 保存
		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mAttacher.setScale(1, 0, 0, false);// 手机
				mImageView.refreshMatrix();
				Bitmap bm = mImageView.saveBmp(isHD);
				SimpleSampleActivity.tempBm = bm;

				String pa = MainActivity.oldPicArrays.get(indexs).picname;
				if (pa.length() > 0) {
					MainActivity.oldPicArrays.get(indexs).picname = null;
					MainActivity.oldPicArrays.get(indexs).picname = "r_"
							+ picname;
				}
				if (MainActivity.imageNames.containsKey(indexs)) {
					MainActivity.imageNames.remove(indexs);
				}
				MainActivity.imageNames.put(indexs, "r_" + picname);
				ArrayList<PointEntity> dayImageNews = new ArrayList<PointEntity>();
				if (news != null && !news.isEmpty()) {
					Set<Integer> keys = news.keySet();
					for (Integer key : keys) {
						PointEntity pointEntity = news.get(key);
						olds.add(pointEntity);
						dayImageNews.add(pointEntity);
					}
				}
				if (MainActivity.pointEntitys.containsKey(indexs)) {
					MainActivity.pointEntitys.remove(indexs);
				}
				MainActivity.pointEntitys_new.put(indexs, dayImageNews);
				MainActivity.pointEntitys.put(indexs, olds);
				try {
					MainActivity.saveFile(bm, "r_" + picname);
				} catch (IOException e) {
					e.printStackTrace();
				}
				mImageView.setInit(); //解决多张图片标注重复出现bug
				isEdit = false;
				finish();

			}
		});
		cancel = (Button) view.findViewById(R.id.cancel);// 取消
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		if (MainActivity.readOnly) {
			save.setVisibility(View.INVISIBLE);
			revoke.setVisibility(View.GONE);
			write.setVisibility(View.GONE);
		}
		rootLayout = (FrameLayout) findViewById(R.id.root);
		mImageView = (PhotoView) findViewById(R.id.iv_photo);// 图片View
		mImageView.setActivity(SimpleSampleActivity.this);
		mCustomView = (DrawingWithBezier) findViewById(R.id.ic_photo);
		mCustomView.setVisibility(View.GONE);
		mImageView.setOnfinishListener(new OnDrawFinishListener() {

			@Override
			public void onFinish(Bitmap bmp) {

			}
		});
		Log.i("test_tag", "picname=" + picname);
		Drawable bitmap = getDefaultPic(picname);
		imageWidth = bitmap.getIntrinsicWidth();
		imageHeight = bitmap.getIntrinsicHeight();
		mImageView.setImageDrawable(bitmap);

		// The MAGIC happens here!
		mAttacher = new PhotoViewAttacher(mImageView);// 图片编辑工具

		// scale = mAttacher.getScale();

		// Lets attach some listeners, not required though!
		mAttacher.setOnMatrixChangeListener(new MatrixChangeListener());
		mAttacher.setOnPhotoTapListener(new PhotoTapListener());
		read.setSelected(true);
		if (tempBm != null) {
			mImageView.setImageBitmap(tempBm);
			mAttacher.setScale(1, 0, 0, false);
			mImageView.invalidate();
		}

	}

	public static Drawable getDefaultPic(String picname) {
		Bitmap bmp = getSD2Pic(picname);
		return new BitmapDrawable(bmp);
	}

	/**
	 * 图片文件路径
	 * 打印Environment.getExternalStorageDirectory()得到："/mnt/sdcard"，即找到了sd卡的根目录
	 */
	public static Bitmap getSDPic(String name) {
		// File mfile = new File(MainActivity.ALBUM_PATH + "/" + name+".jpg");
		File mfile = new File(MainActivity.ALBUM_PATH + "/" + name);
		if (mfile.exists()) {// 若该文件存在

			Bitmap bm = BitmapFactory.decodeFile(MainActivity.ALBUM_PATH + "/"
					+ name);
			return bm;
		}
		return null;
	}

	// 获取SD卡图片
	public static Bitmap getSD2Pic(String name) {
		File mfile = new File(MainActivity.ALBUM_PATH + "/" + name);
		if (mfile.exists()) {// 若该文件存在
			Bitmap bm = BitmapFactory.decodeFile(MainActivity.ALBUM_PATH + "/"
					+ name);
			return bm;
		}
		return null;
	}

	// 刷新按钮
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		viewWidth = mImageView.getWidth();
		viewHeight = mImageView.getHeight();
		mAttacher.setPhoneSize();
		addButton();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		tempPathList.clear();
		news.clear();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mImageView = null;
		mCustomView = null;
		rootLayout = null;

		// Need to call clean-up
		isFirst = true;
		mAttacher.cleanup();
		mAttacher = null;

		tempPathList = null;
		news = null;
		System.out.println("onDestroy");
	}

	private class PhotoTapListener implements OnPhotoTapListener {

		@Override
		public void onPhotoTap(View view, float x, float y) {

		}
	}

	private void showToast(CharSequence text) {
		if (null != mCurrentToast) {
			mCurrentToast.cancel();
		}

		mCurrentToast = Toast.makeText(SimpleSampleActivity.this, text,
				Toast.LENGTH_SHORT);
		mCurrentToast.show();
	}

	// 矩阵对象变化监听事件
	private class MatrixChangeListener implements OnMatrixChangedListener {

		@Override
		public void onMatrixChanged(RectF rect) {
			// mCurrMatrixTv.setText(rect.toString());
			calc();
			addButton();
		}
	}

	// 通过矩阵变化计算比例尺
	private void calc() {
		RectF displayRect = mAttacher.getDisplayRect();
		float w = (int) (Math.abs(displayRect.right - displayRect.left));
		float h = (int) (Math.abs(displayRect.bottom - displayRect.top));

		xxx = displayRect.left;
		yyy = displayRect.top;
		float scaleW = w / viewWidth;
		float scaleH = h / viewHeight;
		scale = (scaleW > scaleH ? scaleW : scaleH);
	}

	// 绘制图片
	public Bitmap drawImage(Bitmap b2) {
		Drawable drawable = mImageView.getDrawable();
		Bitmap bmp = ((BitmapDrawable) drawable).getBitmap();

		Bitmap newBitmap = Bitmap.createBitmap(bmp.copy(
				Bitmap.Config.ARGB_8888, true));
		Canvas canvas = new Canvas(newBitmap);
		Rect src = new Rect(0, 0, b2.getWidth(), b2.getHeight());
		Rect dst = new Rect(0, 0, newBitmap.getWidth(), newBitmap.getHeight());
		RectF displayRect = mAttacher.getDisplayRect();
		if (displayRect.left <= 0) {
			dst.left = (int) (Math.abs(displayRect.left) / scale);
			dst.right = (int) ((Math.abs(displayRect.left) + b2.getWidth()) / scale);
		} else {
			src.left = (int) (displayRect.left);
			src.right = (int) (displayRect.right);
		}
		if (displayRect.top <= 0) {
			dst.top = (int) (Math.abs(displayRect.top) / scale);
			dst.bottom = (int) ((Math.abs(displayRect.top) + b2.getHeight()) / scale);
		} else {
			src.top = (int) (displayRect.top);
			src.bottom = (int) (displayRect.bottom);
		}
		canvas.drawBitmap(b2, src, dst, null);// 叠加新图b2
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();

		return newBitmap;
	}

	// 添加点按钮
	private void addButton() {
		rootLayout.removeAllViews();
		if (olds == null) {
			olds = new ArrayList<PointEntity>();
		}
		if (olds != null && !olds.isEmpty()) {
			for (int i = 0; i < olds.size(); i++) {
				PointEntity pointEntity = olds.get(i);
				addOldButton(pointEntity.point, pointEntity);
			}
		}
		if (news == null) {
			news = new HashMap<Integer, PointEntity>();
		}
		if (news != null && !news.isEmpty()) {
			Set<Integer> keys = news.keySet();
			for (Integer key : keys) {
				PointEntity pointEntity = news.get(key);
				addTempButton(pointEntity.point, pointEntity, key);
			}
		}
	}

	// 以前的点信息
	private void addOldButton(final Point point, final PointEntity pointEntity) {
		Button button = new Button(SimpleSampleActivity.this);
		button.setBackgroundResource(R.drawable.excbtn);

		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				(int) (btnW * scale), (int) (btnW * scale));

		int pointx = isHD ? point.x * 2 : point.x;
		int pointy = isHD ? point.y * 2 : point.y;
		final int x = (int) (pointx * scale + xxx);
		final int y = (int) (pointy * scale + yyy);

		layoutParams.leftMargin = x;
		layoutParams.topMargin = y - (int) (btnW * scale / 2);

		layoutParams.rightMargin = 0;
		layoutParams.bottomMargin = 0;

		rootLayout.addView(button, layoutParams);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showOldPopupWindow(rootLayout, new Point(x, y), pointEntity);
			}

		});
	}

	// 添加本次编辑的点按钮
	private void addTempButton(final Point point,
			final PointEntity pointEntity, final int index) {
		Button button = new Button(SimpleSampleActivity.this);
		button.setBackgroundResource(R.drawable.excbtn);

		FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
				(int) (btnW * scale), (int) (btnW * scale));
		int pointx = isHD ? point.x * 2 : point.x;
		int pointy = isHD ? point.y * 2 : point.y;
		final int x = (int) (pointx * scale + xxx);
		final int y = (int) (pointy * scale + yyy);
		layoutParams.leftMargin = x;
		layoutParams.topMargin = y - (int) (btnW * scale / 2);

		layoutParams.rightMargin = 0;
		layoutParams.bottomMargin = 0;

		rootLayout.addView(button, layoutParams);
		buttons.put(index, button);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopupWindow(rootLayout, new Point(x, y), pointEntity, index);
			}

		});
	}

	// 添加标注
	public void addTips(View view, final PointF point) {
		showEditPopupWindow(view, point);
	}

	// 撤销点按钮操作
	public void delLastTempButton(PointEntity pointEntity, int index) {
		mImageView.delLine(index);
		if (pointEntity != null && buttons.size() > 0 && index > -1) {
			Button tButton = buttons.get(index);
			rootLayout.removeView(tButton);
			buttons.remove(index);
			news.remove(index);
		}
	}

	// 弹出框
	public void showOldPopupWindow(View view, Point point,
			PointEntity pointEntity) {

		View contentView = LayoutInflater.from(this).inflate(
				R.layout.pop_window, null);
		EditText textview = (EditText) contentView.findViewById(R.id.textview);
		Button deleteBtn = (Button) contentView.findViewById(R.id.deleteBtn);
		Button submitBtn = (Button) contentView.findViewById(R.id.submitBtn);
		Button playBtn = (Button) contentView.findViewById(R.id.playBtn);

		deleteBtn.setVisibility(View.GONE);
		submitBtn.setVisibility(View.GONE);
		int width = isHD ? 300 * 2 : 300;
		int height = isHD ? 250 * 2 : 250;
		int edit_height = isHD ? 200 * 2 : 200;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, edit_height);
		lp.gravity = Gravity.CENTER;
		textview.setLayoutParams(lp);
		final PopupWindow popupWindow = new PopupWindow(contentView, width,
				height, true);
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.setOutsideTouchable(false);
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, point.x,
				point.y + 50);// AsDropDown(view);
		if (pointEntity.textContent != null
				&& pointEntity.textContent.length() > 0) {
			textview.setText(pointEntity.textContent);
			textview.setSelection(pointEntity.textContent.length());
			textview.setEnabled(false);
		} else if (pointEntity.mp3Name != null
				&& pointEntity.mp3Name.length() > 0) {
			textview.setVisibility(View.GONE);
			submitBtn.setVisibility(View.GONE);

			playBtn.setVisibility(View.VISIBLE);
			playBtn.setOnClickListener(new commentRecordPlay_listener(
					pointEntity.mp3Name));

		}

	}

	// 弹出框
	public void showPopupWindow(View view, Point point,
			PointEntity pointEntity, int index) {

		View contentView = LayoutInflater.from(this).inflate(
				R.layout.pop_window, null);
		EditText textview = (EditText) contentView.findViewById(R.id.textview);
		Button deleteBtn = (Button) contentView.findViewById(R.id.deleteBtn);

		Button submitBtn = (Button) contentView.findViewById(R.id.submitBtn);
		Button playBtn = (Button) contentView.findViewById(R.id.playBtn);

		int width = isHD ? 300 * 2 : 300;
		int height = isHD ? 250 * 2 : 250;
		int edit_height = isHD ? 200 * 2 : 200;
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, edit_height);
		lp.gravity = Gravity.CENTER;
		textview.setLayoutParams(lp);
		final PopupWindow popupWindow = new PopupWindow(contentView, width,
				height, true);
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.setOutsideTouchable(false);
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, point.x,
				point.y + 50);// AsDropDown(view);
		if (pointEntity.textContent != null
				&& pointEntity.textContent.length() > 0) {
			textview.setText(pointEntity.textContent);
			if (MainActivity.readOnly || isRedo) {
				deleteBtn.setVisibility(View.GONE);
				submitBtn.setVisibility(View.GONE);
				textview.setEnabled(false);
			} else {
				deleteBtn.setOnClickListener(new commentDelete_listener(
						pointEntity, popupWindow, index));
				submitBtn.setOnClickListener(new commentChange_listener(
						pointEntity, popupWindow, textview));

			}
		} else if (pointEntity.mp3Name != null
				&& pointEntity.mp3Name.length() > 0) {
			textview.setVisibility(View.GONE);
			submitBtn.setVisibility(View.GONE);

			playBtn.setVisibility(View.VISIBLE);
			playBtn.setOnClickListener(new commentRecordPlay_listener(
					pointEntity.mp3Name));
			if (MainActivity.readOnly) {
				deleteBtn.setVisibility(View.GONE);
			} else {
				deleteBtn.setVisibility(View.VISIBLE);
				deleteBtn.setOnClickListener(new commentDelete_listener(
						pointEntity, popupWindow, index));
			}

		}

	}

	// 编辑弹出框 popwindow操作
	public void showEditPopupWindow(View view, final PointF point) {
		final int x = (int) (point.x * scale + xxx);
		final int y = (int) (point.y * scale + yyy);

		View contentView = LayoutInflater.from(this).inflate(
				R.layout.edit_pop_window, null);
		final PopupWindow popupWindow = new PopupWindow(contentView, 450, 200,
				true);
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.setOutsideTouchable(false);
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		final LinearLayout selectLyout = (LinearLayout) contentView
				.findViewById(R.id.selectView);
		selectLyout.setBackground(this.getResources().getDrawable(
				R.drawable.pop));
		final Button textBtn = (Button) contentView.findViewById(R.id.textBtn);
		final Button recordBtn = (Button) contentView
				.findViewById(R.id.recordBtn);
		final LinearLayout editLayout = (LinearLayout) contentView
				.findViewById(R.id.editLayout);
		final EditText textEditView = (EditText) contentView
				.findViewById(R.id.textEditView);
		final Button saveTextBtn = (Button) contentView
				.findViewById(R.id.saveTextBtn);
		final Button micBtn = (Button) contentView.findViewById(R.id.micBtn);
		micBtn.setOnClickListener(new commentMIC_listener(textEditView));
		textBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectLyout.setVisibility(View.INVISIBLE);
				editLayout.setVisibility(View.VISIBLE);
				popupWindow.update(600, 400);
			}
		});
		saveTextBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (textEditView.getText().toString().length() > 0) {
					String stringid = StaticTools.getTimeformat("ddHHmmss");
					int pointID = Integer.parseInt(stringid);
					PointEntity pointEntity = new PointEntity();
					pointEntity.pointID = pointID;
					pointEntity.point = new Point();
					int pointx = isHD ? (int) point.x / 2 : (int) point.x;
					int pointy = isHD ? (int) point.y / 2 : (int) point.y;
					pointEntity.point.x = pointx;
					pointEntity.point.y = pointy;
					String msgContext = textEditView.getText().toString();
					pointEntity.textContent = msgContext;
					pointEntity.textEditContent = msgContext;
					news.put(tempPathList.size() - 1, pointEntity);

					addTempButton(pointEntity.point, pointEntity,
							tempPathList.size() - 1);
					popupWindow.dismiss();
				}
			}
		});

		recordBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				selectLyout.setVisibility(View.INVISIBLE);

				Intent intent = new Intent(SimpleSampleActivity.this,
						MyMP3Dialog.class);
				intent.putExtra("start", (int) point.x);
				intent.putExtra("end", (int) point.y);
				SimpleSampleActivity.this.startActivityForResult(intent, 2000);
			}
		});

		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y + 50);
	}

	// onActivityResult生命周期 处理回调添加编辑点按钮
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 2000:
			if (resultCode == RESULT_OK) {
				String amrPath = data.getStringExtra("FileImage");
				int start = data.getIntExtra("start", -1);
				int end = data.getIntExtra("end", -1);

				String stringid = StaticTools.getTimeformat("ddHHmmss");
				int pointID = Integer.parseInt(stringid);
				PointEntity pointEntity = new PointEntity();
				pointEntity.pointID = pointID;
				pointEntity.point = new Point();
				pointEntity.point.x = start;
				pointEntity.point.y = end;
				pointEntity.mp3Name = amrPath;
				news.put(tempPathList.size() - 1, pointEntity);

				addTempButton(pointEntity.point, pointEntity,
						tempPathList.size() - 1);
			}
			break;
		}
	}

	// 语音播放
	class commentRecordPlay_listener implements Button.OnClickListener {
		String path;

		commentRecordPlay_listener(String path) {
			this.path = path;
		}

		@Override
		public void onClick(View v) {
			Uri u = Uri.parse(path);
			final MediaPlayer player = MediaPlayer.create(
					SimpleSampleActivity.this, u);

			if (!player.isPlaying()) {
				player.start();

				player.setOnErrorListener(new OnErrorListener() {

					@Override
					public boolean onError(MediaPlayer mp, int what, int extra) {
						return false;
					}
				});

				player.setOnCompletionListener(new OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer mp) {
						player.reset();
						player.release();
					}
				});
			}

		}
	}

	// 标注删除
	class commentDelete_listener implements Button.OnClickListener {
		private PointEntity pointEntity;
		private PopupWindow popupWindow;
		private int index;

		commentDelete_listener(PointEntity pointEntity,
				PopupWindow popupWindow, int index) {
			this.pointEntity = pointEntity;
			this.popupWindow = popupWindow;
			this.index = index;
		}

		public void onClick(View v) {

			delLastTempButton(pointEntity, index);
			popupWindow.dismiss();
		}

	}

	// 标注更改
	class commentChange_listener implements Button.OnClickListener {
		private PointEntity pointEntity;
		private PopupWindow popupWindow;
		private EditText textview;

		commentChange_listener(PointEntity pointEntity,
				PopupWindow popupWindow, EditText textview) {
			this.pointEntity = pointEntity;
			this.popupWindow = popupWindow;
			this.textview = textview;
		}

		public void onClick(View v) {
			if (textview.getText().toString().length() > 0) {
				textview.refreshDrawableState();
				pointEntity.textEditContent = pointEntity.textContent;
				pointEntity.textContent = "";
				pointEntity.textContent = textview.getText().toString();
				popupWindow.dismiss();
			}
		}

	}

	private BaiduASRDigitalDialog mDialog = null;

	// 录音
	class commentMIC_listener implements Button.OnClickListener {
		private EditText textview;

		commentMIC_listener(EditText textview) {
			this.textview = textview;
		}

		public void onClick(View v) {

			Bundle params = new Bundle();
			params.putString(BaiduASRDigitalDialog.PARAM_API_KEY,
					Constants.API_KEY);
			params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY,
					Constants.SECRET_KEY);
			params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME,
					Config.DIALOG_THEME);
			mDialog = new BaiduASRDigitalDialog(SimpleSampleActivity.this,
					params);
			mDialog.setDialogRecognitionListener(new DialogRecognitionListener() {

				@Override
				public void onResults(Bundle results) {
					ArrayList<String> rs = results != null ? results
							.getStringArrayList(RESULTS_RECOGNITION) : null;
					if (rs != null && rs.size() > 0) {
						textview.setText(rs.get(0));
					}

				}
			});

			mDialog.getParams().putInt(BaiduASRDigitalDialog.PARAM_PROP,
					Config.CURRENT_PROP);
			mDialog.getParams().putString(BaiduASRDigitalDialog.PARAM_LANGUAGE,
					Config.getCurrentLanguage());
			Log.e("DEBUG", "Config.PLAY_START_SOUND = "
					+ Config.PLAY_START_SOUND);
			mDialog.getParams().putBoolean(
					BaiduASRDigitalDialog.PARAM_START_TONE_ENABLE,
					Config.PLAY_START_SOUND);
			mDialog.getParams().putBoolean(
					BaiduASRDigitalDialog.PARAM_END_TONE_ENABLE,
					Config.PLAY_END_SOUND);
			mDialog.getParams().putBoolean(
					BaiduASRDigitalDialog.PARAM_TIPS_TONE_ENABLE,
					Config.DIALOG_TIPS_SOUND);
			mDialog.show();
		}
	}

}
