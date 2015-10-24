package flytv.qaonline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.qaonline.utils.DensityConfig;

public class TitleView extends LinearLayout {
	public interface TitleClickListener{
		public void onLeftClick(View view);
		public void onRightClick(View view);
	}
	private int[] mReturnTxtColor;
	private int[] mReturnIvBg;
	private View mTitleMainView;
	private View mReturnView;
	private ImageView mReturnBackIv;
	private TextView mReturnBackTv;
	private TextView mTitleNameTv;
	public TextView mRightTv;
	private View mTitleDisLine;
	private TitleClickListener mTitleClickListener;
	
	public TitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initTitleView();
	}

	public TitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initTitleView();
	}

	public TitleView(Context context) {
		super(context);
		initTitleView();
	}
	
	private void initTitleView(){
		mTitleMainView = View.inflate(getContext(), R.layout.view_page_title, null);
		mReturnView = mTitleMainView.findViewById(R.id.pagetitle_return_layout);
		mReturnBackIv = (ImageView)mTitleMainView.findViewById(R.id.pagetitle_return_iv);
		mReturnBackTv = (TextView)mTitleMainView.findViewById(R.id.pagetitle_return_tv);
		mTitleNameTv = (TextView)mTitleMainView.findViewById(R.id.pagetitle_name_iv);
		mRightTv = (TextView)mTitleMainView.findViewById(R.id.pagetitle_right_iv);
		mTitleDisLine = new View(getContext());
		setViewTheme(); // 设置样式
		mReturnView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				switch (arg1.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mReturnBackIv.setImageResource(mReturnIvBg[0]);
					mReturnBackTv.setTextColor(mReturnTxtColor[0]);
					break;
				case MotionEvent.ACTION_UP:
					mReturnBackIv.setImageResource(mReturnIvBg[1]);
					mReturnBackTv.setTextColor(mReturnTxtColor[1]);
					if(mTitleClickListener != null){
						mTitleClickListener.onLeftClick(TitleView.this);
					} 
					break;
				case MotionEvent.ACTION_MOVE:
					break;
				default:
					mReturnBackIv.setImageResource(mReturnIvBg[1]);
					mReturnBackTv.setTextColor(mReturnTxtColor[1]);
					break;
				}
				return true;
			}
		});
		mRightTv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mTitleClickListener != null){
					mTitleClickListener.onRightClick(TitleView.this);
				}
			}
		});
		
		LinearLayout.LayoutParams titlePar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
    			DensityConfig.getDensityConfig(getContext()).getPageTitleHeight());
		LinearLayout.LayoutParams disLinePar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,2);
		this.setOrientation(LinearLayout.VERTICAL);
		this.addView(mTitleMainView,titlePar);
		this.addView(mTitleDisLine,disLinePar);
	}
	
	public void setTitleClickListener(TitleClickListener titleClickListener){
		this.mTitleClickListener = titleClickListener;
	}
	
	public void setViewTheme(){
		this.setBackgroundResource(R.color.bg_title);
		mReturnTxtColor = new int[]{getResources().getColor(R.color.txt_title_pre),
				getResources().getColor(R.color.txt_title_nor)};
		mReturnIvBg = new int[]{R.drawable.img_back_pre,
				R.drawable.img_back};
    	mTitleDisLine.setBackgroundResource(R.color.bg_title);
		mTitleNameTv.setTextColor(getResources().getColor(R.color.txt_title_nor));
		mRightTv.setTextColor(getResources().getColorStateList(R.drawable.res_qaonline_titleedttv));
		mReturnBackIv.setImageResource(mReturnIvBg[1]);
		mReturnBackTv.setTextColor(mReturnTxtColor[1]);
	}
	
	public void setTitleValue(String backName,String titleName,String rightName){
		mReturnBackTv.setText(backName);
		mTitleNameTv.setText(titleName);
		mRightTv.setText(rightName);
	}
	
	public void setTitleValue(int backName,int titleName,String rightName){
		mReturnBackTv.setText(backName);
		mTitleNameTv.setText(titleName);
		mRightTv.setText(rightName);
	}
	
	public void setTitleValue(int backName,int titleName,int rightName){
		mReturnBackTv.setText(backName);
		mTitleNameTv.setText(titleName);
		mRightTv.setText(rightName);
	}
	
}
