package com.example.richtest;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

public class DrawingWithBezier extends ImageView {
	private float mX;
	private float mY;
	private float endX;
	private float endY;

	private final Paint mGesturePaint = new Paint();
	private Path mPath;
	private ArrayList<Path> pathList = new ArrayList<Path>();
	private OnDrawFinishListener onfinishListener;
	private float scale;
	private final int LINE = 2;
	private RectF displayRect;
	private Context context;
	private SimpleSampleActivity activity;
	private boolean isError = false;

	public DrawingWithBezier(Context context) {
		this(context, null);
		mGesturePaint.setAntiAlias(true);
		mGesturePaint.setStyle(Style.STROKE);
		mGesturePaint.setStrokeWidth(5);
		mGesturePaint.setColor(Color.RED);
		this.context = context;
	}

	public DrawingWithBezier(Context context, AttributeSet attr) {
		this(context, attr, 0);
		mGesturePaint.setAntiAlias(true);
		mGesturePaint.setStyle(Style.STROKE);
		mGesturePaint.setStrokeWidth(5);
		mGesturePaint.setColor(Color.RED);
		this.context = context;
	}

	public DrawingWithBezier(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		mGesturePaint.setAntiAlias(true);
		mGesturePaint.setStyle(Style.STROKE);
		mGesturePaint.setStrokeWidth(5);
		mGesturePaint.setColor(Color.RED);
		this.context = context;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mPath = new Path();
			touchDown(event);
			break;
		case MotionEvent.ACTION_MOVE:
			if (!isError) {
				touchMove(event);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (!isError) {
				pathList.add(mPath);
				PointF end = new PointF(endX, endY);
				activity.addTips(this, end);
			}
			isError = false;
			break;
		}
		
		invalidate();
		return true;
	}

	public void saveBmp() {
		Bitmap mbmp = Bitmap.createBitmap(getWidth(), getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(mbmp);
		draw(canvas);
		canvas.save(Canvas.ALL_SAVE_FLAG);
		
		canvas.restore();
//		getOnfinishListener().onFinish(mbmp, pathList);
	}

	public void uodoLine() {
		if (mPath != null) {
			mPath.reset();
		}
		if (pathList != null && !pathList.isEmpty()) {
			pathList.remove(pathList.size() - 1);
			invalidate();
		}
//		activity.delLastTempButton(SimpleSampleActivity.news
//				.get(SimpleSampleActivity.news.size() - 1));
	}

	public void delLine(int index) {
		if (mPath != null) {
			mPath.reset();
		}
		if (pathList != null && !pathList.isEmpty()) {
			pathList.remove(index);
			invalidate();
		}
	}

	public void delLastLine() {
		if (mPath != null) {
			mPath.reset();
		}
		if (pathList != null && !pathList.isEmpty()) {
			pathList.remove(pathList.size() - 1);
			invalidate();
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		canvas.drawPath(mPath, mGesturePaint);
		// canvas.drawText("jsflkjslkfjsljflks", 0, 20, mGesturePaint);
		for (Path pp : pathList) {
			// 閫氳繃鐢诲竷缁樺埗澶氱偣褰㈡垚鐨勫浘褰�
			canvas.drawPath(pp, mGesturePaint);
		}
	}

	// 鎵嬫寚鐐逛笅灞忓箷鏃惰皟鐢�
	private void touchDown(MotionEvent event) {

		// mPath.rewind();
		// setInit();
		float x = event.getX();
		float y = event.getY();

		if (displayRect.left > 0) {
			if (x < displayRect.left) {
				Toast.makeText(context, "errror! displayRect.left ="+x +"|"+ displayRect.left, Toast.LENGTH_SHORT).show();
				isError = true;
				return;
			} else if (x > displayRect.right) {
				Toast.makeText(context, "errror displayRect.right ="+x +"|"+ displayRect.right, Toast.LENGTH_SHORT).show();
				isError = true;
				return;
			}
		}
		if (displayRect.top > 0) {
			if (y < displayRect.top) {
				Toast.makeText(context, "errror top="+x +"|"+ displayRect.top, Toast.LENGTH_SHORT).show();
				isError = true;
				return;
			} else if (y > displayRect.bottom) {
				Toast.makeText(context, "errror bottom="+x +"|"+ displayRect.bottom, Toast.LENGTH_SHORT).show();
				isError = true;
				return;
			}
		}

		mX = x;
		mY = y;
		// // mPath缁樺埗鐨勭粯鍒惰捣鐐�
		// mPath.moveTo(x, y);
		endX = x;
		endY = y;
	}

	// 鎵嬫寚鍦ㄥ睆骞曚笂婊戝姩鏃惰皟鐢�
	private void touchMove(MotionEvent event) {
		final float x = event.getX();
		final float y = event.getY();

		if (displayRect.left > 0) {
			if (x < displayRect.left) {
				return;
			} else if (x > displayRect.right) {
				return;
			}
		}
		if (displayRect.top > 0) {
			if (y < displayRect.top) {
				return;
			} else if (y > displayRect.bottom) {
				return;
			}
		}

		// final float previousX = mX;
		// final float previousY = mY;
		final float previousX = endX;
		final float previousY = endY;

		final float dx = Math.abs(x - previousX);
		final float dy = Math.abs(y - previousY);

		// 涓ょ偣涔嬮棿鐨勮窛绂诲ぇ浜庣瓑浜�鏃讹紝鐢熸垚璐濆灏旂粯鍒舵洸绾�
		if (dx >= 3 || dy >= 3) {
			// // 璁剧疆璐濆灏旀洸绾跨殑鎿嶄綔鐐逛负璧风偣鍜岀粓鐐圭殑涓�崐
			// float cX = (x + previousX) / 2;
			// float cY = (y + previousY) / 2;
			//
			// // 浜屾璐濆灏旓紝瀹炵幇骞虫粦鏇茬嚎锛沺reviousX, previousY涓烘搷浣滅偣锛宑X, cY涓虹粓鐐�
			// mPath.quadTo(previousX, previousY, cX, cY);
			//
			// // 绗簩娆℃墽琛屾椂锛岀涓�缁撴潫璋冪敤鐨勫潗鏍囧�灏嗕綔涓虹浜屾璋冪敤鐨勫垵濮嬪潗鏍囧�
			// mX = x;
			// mY = y;
			mPath = new Path();
			mPath.moveTo(mX, mY);
			mPath.lineTo(x, y);
			endX = x;
			endY = y;
		}
	}

	public OnDrawFinishListener getOnfinishListener() {
		return onfinishListener;
	}

	public void setOnfinishListener(OnDrawFinishListener onfinishListener) {
		this.onfinishListener = onfinishListener;
	}

	public void setInit() {
		// 閲嶇疆缁樺埗璺嚎锛屽嵆闅愯棌涔嬪墠缁樺埗鐨勮建杩�
		// mPath.reset();
		mPath = new Path();
		pathList = new ArrayList<Path>();
		isError = false;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
		mGesturePaint.setStrokeWidth(LINE * scale);
	}

	public RectF getDisplayRect() {
		return displayRect;
	}

	public void setDisplayRect(RectF displayRect) {
		this.displayRect = displayRect;
	}

	public SimpleSampleActivity getActivity() {
		return activity;
	}

	public void setActivity(SimpleSampleActivity activity) {
		this.activity = activity;
	}

	public ArrayList<Path> getPathList() {
		return pathList;
	}

	public void setPathList(ArrayList<Path> pathList) {
		this.pathList = pathList;
		invalidate();
	}
}
