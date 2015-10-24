package flytv.ext.view;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;
import flytv.compos.run.R;

@SuppressLint("ResourceAsColor")
public class TabPageIndicator extends HorizontalScrollView {
	private IcsLinearLayout mTabLayout;
	private int mMaxTabWidth;
	private int mSelectedTabIndex;
	private String[] ARRAY = new String[] {};
	private static final CharSequence EMPTY_TITLE = "";
	private TabOnItemTitleSelectClickLister tabOnItemTitleSelectClickLister;

	public void setTabOnItemTitleSelectClickLister(
			TabOnItemTitleSelectClickLister tabOnItemTitleSelectClickLister) {
		this.tabOnItemTitleSelectClickLister = tabOnItemTitleSelectClickLister;
	}

	public interface TabOnItemTitleSelectClickLister {
		void ItemClick(int index);
	}

	public TabPageIndicator(Context context) {
		this(context, null);
	}

	public void init(String[] ARRAY) {
		this.ARRAY = ARRAY;
	}

	public TabPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		setHorizontalScrollBarEnabled(false);
		mTabLayout = new IcsLinearLayout(context,
				R.attr.vpiTabPageIndicatorStyle);
		addView(mTabLayout, new ViewGroup.LayoutParams(WRAP_CONTENT,
				MATCH_PARENT));
	}

	private Runnable mTabSelector;

	private final OnClickListener mTabClickListener = new OnClickListener() {
		public void onClick(View view) {
			TabView tabView = (TabView) view;
			final int newSelected = tabView.getIndex();
			setCurrentItem(newSelected);
			// if (oldSelected == newSelected && mTabReselectedListener != null)
			// {
			// mTabReselectedListener.onTabReselected(newSelected);
			// }
		}
	};

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		final boolean lockedExpanded = widthMode == MeasureSpec.EXACTLY;
		setFillViewport(lockedExpanded);

		final int childCount = mTabLayout.getChildCount();
		if (childCount > 1
				&& (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST)) {
			if (childCount > 2) {
				mMaxTabWidth = (int) (MeasureSpec.getSize(widthMeasureSpec) * 0.4f);
			} else {
				mMaxTabWidth = MeasureSpec.getSize(widthMeasureSpec) / 2;
			}
		} else {
			mMaxTabWidth = -1;
		}

		final int oldWidth = getMeasuredWidth();
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		final int newWidth = getMeasuredWidth();

		if (lockedExpanded && oldWidth != newWidth) {
			// Recenter the tab display if we're at a new (scrollable) size.
			//setCurrentItem(mSelectedTabIndex);
		}
	}

	private void animateToTab(final int position) {
		final View tabView = mTabLayout.getChildAt(position);
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
		mTabSelector = new Runnable() {
			public void run() {
				final int scrollPos = tabView.getLeft()
						- (getWidth() - tabView.getWidth()) / 2;
				smoothScrollTo(scrollPos, 0);
				mTabSelector = null;
			}
		};
		post(mTabSelector);
	}

	private void setCurrentItem(int item) {

		mSelectedTabIndex = item;
		if(tabOnItemTitleSelectClickLister!=null){
			tabOnItemTitleSelectClickLister.ItemClick(item);
		}
		final int tabCount = mTabLayout.getChildCount();
		for (int i = 0; i < tabCount; i++) {
			final View child = mTabLayout.getChildAt(i);
			final boolean isSelected = (i == item);
//			if(isSelected){
//				((TextView)child).setTextColor(R.color.style__background_holo_dark);
//				Log.i("setCurrentItem",""+isSelected);
//			}else{
//				((TextView)child).setTextColor(R.color.style__background_holo_light);
//				Log.i("setCurrentItem",""+isSelected);
//			}
			child.setSelected(isSelected);
			if (isSelected) {
				animateToTab(item);
			}
		}
	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mTabSelector != null) {
			// Re-post the selector we saved
			post(mTabSelector);
		}
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mTabSelector != null) {
			removeCallbacks(mTabSelector);
		}
	}
	public void initTab() {
		mTabLayout.removeAllViews();
		final int count = ARRAY.length;
		for (int i = 0; i < count; i++) {
			CharSequence title = ARRAY[i];
			if (title == null) {
				title = EMPTY_TITLE;
			}
			int iconResId = 0;
//			if (iconAdapter != null) {
//				iconResId = iconAdapter.getIconResId(i);
//			}
			addTab(i, title, iconResId);
		}
		if (mSelectedTabIndex > count) {
			mSelectedTabIndex = count - 1;
		}
		setCurrentItem(mSelectedTabIndex);
		requestLayout();
	}

	private void addTab(int index, CharSequence text, int iconResId) {
		final TabView tabView = new TabView(getContext());
		tabView.mIndex = index;
		tabView.setFocusable(true);
		tabView.setOnClickListener(mTabClickListener);
		tabView.setText(text);

		if (iconResId != 0) {
			tabView.setCompoundDrawablesWithIntrinsicBounds(iconResId, 0, 0, 0);
		}

		mTabLayout.addView(tabView, new LinearLayout.LayoutParams(WRAP_CONTENT,
				MATCH_PARENT, 1));
	}


	private class TabView extends TextView {
		private int mIndex;

		public TabView(Context context) {
			super(context, null, R.attr.vpiTabPageIndicatorStyle);
		}

		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);

			// Re-measure if we went beyond our maximum size.
			if (mMaxTabWidth > 0 && getMeasuredWidth() > mMaxTabWidth) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(mMaxTabWidth,
						MeasureSpec.EXACTLY), heightMeasureSpec);
			}
		}

		public int getIndex() {
			return mIndex;
		}
	}

}
