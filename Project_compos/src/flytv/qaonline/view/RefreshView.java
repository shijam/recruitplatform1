package flytv.qaonline.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import flytv.compos.run.R;

public class RefreshView extends LinearLayout {
	public interface RefreshListener{
		public void onRefreshListener();
	}
	private boolean canReRefreshLoad = false;
	private TextView mNoticeTv;
	private ImageView mFailedIv;
	private ProgressBar mProgressBar;
	private RefreshListener mRefreshListener;
	public RefreshView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public RefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public RefreshView(Context context) {
		super(context);
		initView();
	}

	private void initView(){
		View refreshView = View.inflate(getContext(), R.layout.view_page_refresh, null);
		mNoticeTv = (TextView)refreshView.findViewById(R.id.refresh_notice_tv);
		mFailedIv = (ImageView)refreshView.findViewById(R.id.refresh_failed_iv);
		mProgressBar = (ProgressBar)refreshView.findViewById(R.id.refresh_progressbar);
//		this.setBackgroundResource(R.color.bg_page);
		refreshView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(canReRefreshLoad && mRefreshListener != null)
					mRefreshListener.onRefreshListener();
			}
		});
		this.setGravity(Gravity.CENTER);
		this.addView(refreshView);
		this.setVisibility(View.GONE);
	}
	
	public void setRefreshListener(RefreshListener refreshListener){
		mRefreshListener = refreshListener;
	}
	
	public void startLoading(){
		this.setVisibility(View.VISIBLE);
		mNoticeTv.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.VISIBLE);
		mFailedIv.setVisibility(View.GONE);
		mNoticeTv.setText(R.string.str_tips_loading);
		canReRefreshLoad = false;
	}
	
	public void endLoading(){
		canReRefreshLoad = false;
		mNoticeTv.setText("");
		this.setVisibility(View.GONE);
		mNoticeTv.setVisibility(View.GONE);
		mProgressBar.setVisibility(View.GONE);
	}
	
	public void endLoadingNoData(){
		canReRefreshLoad = true;
		mProgressBar.setVisibility(View.GONE);
		mFailedIv.setVisibility(View.VISIBLE);
		mNoticeTv.setVisibility(View.VISIBLE);
		mFailedIv.setImageResource(R.drawable.icon_nodata);
		mNoticeTv.setText(R.string.str_tips_nodata);
	}
	
	public void endLoadingNoNet(){
		canReRefreshLoad = true;
		mProgressBar.setVisibility(View.GONE);
		mFailedIv.setVisibility(View.VISIBLE);
		mNoticeTv.setVisibility(View.VISIBLE);
		mFailedIv.setImageResource(R.drawable.icon_nonet);
		mNoticeTv.setText(R.string.str_tips_nonet);
	}
	
	
}
