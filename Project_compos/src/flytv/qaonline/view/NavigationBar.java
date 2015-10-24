package flytv.qaonline.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.qaonline.utils.DensityConfig;

public class NavigationBar extends LinearLayout {
	public interface OnItemClickListener {
		public void itemSelect(int index);
	}
	
	private int PADDING_BOTTOM = DensityConfig.dip2px(getContext(), 4);
	private Paint mPaint;
	private RectF curRectF;
	private RectF tarRectF;
	private int mCurrentIndex;
	private List<TextView> mTabViews;
	private LinearLayout.LayoutParams mNavTabPar;
	
	public NavigationBar(Context context) {
		super(context);
		initViewData();
	}
	
	public NavigationBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViewData();
	}
	public NavigationBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViewData();
	}
	

	public void setNaviIndex(int index) {
		if(mTabViews == null || mTabViews.size() == 0)
			return;
		if (tarRectF == null) {
			tarRectF = new RectF();
		}
		mCurrentIndex = index;
		tarRectF.left = mTabViews.get(index).getLeft();
		tarRectF.right = mTabViews.get(index).getRight();
		tarRectF.top = mTabViews.get(index).getBottom() - PADDING_BOTTOM;
		tarRectF.bottom = mTabViews.get(index).getBottom();
		focusTvIndex(index);
		invalidate();
	}

	public int getCurrentIndex(){
		return mCurrentIndex;
	}

	private void initViewData(){
		this.setOrientation(LinearLayout.HORIZONTAL);
		setWillNotDraw(false);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setColor(getResources().getColor(R.color.theme_color));
		mNavTabPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		mNavTabPar.gravity = Gravity.CENTER;
		mNavTabPar.weight = 1;
		curRectF = null;
		tarRectF = null;
	}
	
	public void addNaviView(TextView tabView){
		if(tabView == null)
			return;
		if(mTabViews == null)
			mTabViews = new ArrayList<TextView>();
		mTabViews.add(tabView);
		this.addView(tabView, mNavTabPar);
	}
	
	public void setNaviView(List<TextView> tabViews){
		mTabViews = tabViews;
		if(mTabViews == null || mTabViews.isEmpty())
			return;
		for(int i = 0;i < mTabViews.size();i++){
			this.addView(mTabViews.get(i), mNavTabPar);
		}
		focusTvIndex(0);
	}

	public void focusTvIndex(int index){
		if(mTabViews == null || mTabViews.isEmpty())
			return;
		for(int i = 0;i < mTabViews.size();i++){
			if(index == i){
				mTabViews.get(i).setTextColor(getResources().getColor(R.color.txt_nav_pre));
			}else{
				mTabViews.get(i).setTextColor(getResources().getColor(R.color.txt_nav_nor));
			}
		}
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(mTabViews != null && !mTabViews.isEmpty()){
			int step = getWidth() / 10;
			if (curRectF == null)
				curRectF = new RectF(mTabViews.get(0).getLeft(),
						mTabViews.get(0).getBottom() - PADDING_BOTTOM,
						mTabViews.get(0).getRight(), mTabViews.get(0).getBottom());
			if (tarRectF == null)
				tarRectF = new RectF(mTabViews.get(0).getLeft(),
						mTabViews.get(0).getBottom() - PADDING_BOTTOM,
						mTabViews.get(0).getRight(), mTabViews.get(0).getBottom());
			
			if (Math.abs(curRectF.left - tarRectF.left) < step) {
				curRectF.left = tarRectF.left;
				curRectF.right = tarRectF.right;
			}
			if (curRectF.left > tarRectF.left) {
				curRectF.left -= step;
				curRectF.right -= step;
				invalidate();
			} else if (curRectF.left < tarRectF.left) {
				curRectF.left += step;
				curRectF.right += step;
				invalidate();
			}
			canvas.drawRect(curRectF, mPaint);
		}
	}

}

// end of the file