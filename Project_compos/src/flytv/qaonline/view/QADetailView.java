package flytv.qaonline.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.compos.run.bean.FileBean;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.http.HttpServer;
import flytv.qaonline.image.BitmapUtils.BitmapStyle;
import flytv.qaonline.utils.DensityConfig;

public class QADetailView extends LinearLayout {
	public interface QADetailViewClick{
		public void clickImage(FileBean fileIetmEntity);
		public void clickVideo(FileBean fileIetmEntity);
		public void clickMp3(FileBean fileIetmEntity);
	}
	public class ImageObject implements Serializable{
		private static final long serialVersionUID = 1L;
		public ImageView imageIv;
		public Bitmap bitmap;
	}
	public class ViewHolder{
		public LinearLayout mainView;
		public List<View> imageView;
	}
	private LinearLayout.LayoutParams mItemPar;
	private LinearLayout.LayoutParams mTitlePar;
	private LinearLayout.LayoutParams mContentPar;
	private LinearLayout.LayoutParams mImagePar;
	private LinearLayout.LayoutParams mMp3Par;
	private QADetailViewClick mQADetailViewClick;
	private long mDoubleClickTime;
	
	private Handler mViewHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1101:
					ViewHolder viewHolder = (ViewHolder)msg.obj;
					if(viewHolder.mainView != null && viewHolder.imageView != null 
							&& !viewHolder.imageView.isEmpty()){
						for(int i = 0;i < viewHolder.imageView.size();i++){
							viewHolder.mainView.addView(viewHolder.imageView.get(i),mImagePar);
						}
					}
					break;
				case 1102:
					ViewHolder viewHolder1 = (ViewHolder)msg.obj;
					if(viewHolder1.mainView != null && viewHolder1.imageView != null 
							&& !viewHolder1.imageView.isEmpty()){
						for(int i = 0;i < viewHolder1.imageView.size();i++){
							viewHolder1.mainView.addView(viewHolder1.imageView.get(i));
						}
					}
					break;
				case 1103:
					ImageObject imageObject = (ImageObject)msg.obj;
					imageObject.imageIv.setImageBitmap(imageObject.bitmap);
					imageObject.imageIv.setTag("");
					break;
				case 1104:
					ImageObject imageObject1 = (ImageObject)msg.obj;
					imageObject1.imageIv.setBackground(new BitmapDrawable(imageObject1.bitmap));
					imageObject1.imageIv.setTag("");
					break;
			default:
				break;
			}
		}
	};
	
	public QADetailView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViewData();
	}

	public QADetailView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViewData();
	}

	public QADetailView(Context context) {
		super(context);
		initViewData();
	}

	private void initViewData(){
		int dp4 = DensityConfig.dip2px(getContext(), 2);
		int dp6 = DensityConfig.dip2px(getContext(), 6);
		int dp20 = DensityConfig.dip2px(getContext(), 20);
		int dp200 = DensityConfig.dip2px(getContext(), 120);
		mTitlePar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		mTitlePar.topMargin = dp4;
		mTitlePar.bottomMargin = dp4;
		mContentPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		mContentPar.leftMargin = dp6;
		mContentPar.rightMargin = dp6;
		mItemPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		mItemPar.topMargin = dp4;
		mImagePar = new LinearLayout.LayoutParams(dp200,dp200);
		mImagePar.leftMargin = dp6;
		mImagePar.rightMargin = dp6;
		mMp3Par = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		mMp3Par.rightMargin = dp20;
	}
	
	public void setQADetailViewClick(QADetailViewClick qaDetailViewClick){
		this.mQADetailViewClick = qaDetailViewClick;
	}
	
	public void addItem(String title,String content,final List<FileBean> imageList,
			final List<FileBean> videoList,final List<FileBean> mp3List){
		if((title == null || "".equals(title)) && (content == null || "".equals(content))
				&& (imageList == null || imageList.isEmpty()) 
				&& (videoList == null || videoList.isEmpty()))
			return;
		LinearLayout mainLayout = new LinearLayout(getContext());
		mainLayout.setOrientation(LinearLayout.VERTICAL);
		mainLayout.setBackgroundResource(R.drawable.item_bg);
		final int dp4 = DensityConfig.dip2px(getContext(), 4);
		final int dp10 = DensityConfig.dip2px(getContext(), 10);
		mainLayout.setPadding(dp4,dp4,dp4,dp4);
		if(title != null && !"".equals(title)){
			TextView titleTv = new TextView(getContext());
			titleTv.setTextColor(getResources().getColor(R.color.theme_color));
			titleTv.setSingleLine(true);
			titleTv.setTextSize(16);
			titleTv.setText(title);
			titleTv.setPadding(dp4, 0, 0, 0);
			mainLayout.addView(titleTv,mTitlePar);
		}
		if(content != null && !"".equals(content)){
			TextView contentTv = new TextView(getContext());
			contentTv.setTextColor(getResources().getColor(R.color.black));
			contentTv.setTextSize(14);
			contentTv.setText(content);
			contentTv.setGravity(Gravity.CENTER_VERTICAL);
			mainLayout.addView(contentTv,mContentPar);
		}
		HorizontalScrollView ivScrollView = new HorizontalScrollView(getContext());
		ivScrollView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
		final LinearLayout imageLayout = new LinearLayout(getContext());
		imageLayout.setOrientation(LinearLayout.HORIZONTAL);
		ivScrollView.addView(imageLayout);
		final List<View> imageViews = new ArrayList<View>();
		new Thread(){
			public void run() {
				for(int i = 0;i< imageList.size();i++){
					final int index = i;
					final String imageUrl = getContext().getResources().getString(R.string.flytv_file) + imageList.get(i).getFileUrl();
					final ImageView imageView = new ImageView(getContext());
					imageView.setScaleType(ScaleType.CENTER_CROP);
					imageView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(mQADetailViewClick != null)
								mQADetailViewClick.clickImage(imageList.get(index));
						}
					});
					imageViews.add(imageView);
					imageView.setTag(imageUrl);
		    		HttpEngine.getHttpEngine(getContext()).requestImageData(imageView,imageUrl, imageUrl,index,
							new HttpEngineListener() {
						@Override
						public void requestCallBack(final Object data, final String resultCode, final int requestCode) {
							final ImageView iv = (ImageView)imageView.findViewWithTag(imageUrl);
							if(iv != null ){
								if(data != null && requestCode == index && (HttpServer.HTTPSTATE_SUCCESS.equals(resultCode))){
									ImageObject object = new ImageObject();
									object.bitmap = (Bitmap)data;
									object.imageIv = iv;
									mViewHandler.obtainMessage(1103,object).sendToTarget();
								}
							}
					}
				  },BitmapStyle.NULL,true);
				}
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.mainView = imageLayout;
				viewHolder.imageView = imageViews;
				mViewHandler.obtainMessage(1101,viewHolder).sendToTarget();
			};
		}.start();
		
		HorizontalScrollView vdScrollView = new HorizontalScrollView(getContext());
		vdScrollView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
		final LinearLayout vdLayout = new LinearLayout(getContext());
		vdLayout.setOrientation(LinearLayout.HORIZONTAL);
		vdScrollView.addView(vdLayout);
		final List<View> vdViews = new ArrayList<View>();
		new Thread(){
			public void run() {
				for(int i = 0;i< videoList.size();i++){
					final int index = i;
					final String imageUrl = getContext().getResources().getString(R.string.flytv_file) + videoList.get(i).getThumbPath();
					final ImageView imageView = new ImageView(getContext());
					imageView.setScaleType(ScaleType.CENTER_INSIDE);
					imageView.setImageResource(R.drawable.im_play_nor);
					imageView.setPadding(dp10, dp10, dp10, dp10);
					imageView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(mQADetailViewClick != null)
								mQADetailViewClick.clickVideo(videoList.get(index));
						}
					});
					vdViews.add(imageView);
					imageView.setTag(imageUrl);
		    		HttpEngine.getHttpEngine(getContext()).requestImageData(imageView,imageUrl, imageUrl,index,
							new HttpEngineListener() {
						@Override
						public void requestCallBack(final Object data, final String resultCode, final int requestCode) {
							final ImageView iv = (ImageView)imageView.findViewWithTag(imageUrl);
							if(iv != null ){
								if(data != null && requestCode == index && (HttpServer.HTTPSTATE_SUCCESS.equals(resultCode))){
									ImageObject object = new ImageObject();
									object.bitmap = (Bitmap)data;
									object.imageIv = iv;
									mViewHandler.obtainMessage(1104,object).sendToTarget();
								}
							}
					}
				  },BitmapStyle.NULL,false);
				}
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.mainView = vdLayout;
				viewHolder.imageView = vdViews;
				mViewHandler.obtainMessage(1101,viewHolder).sendToTarget();
			};
		}.start();
		
		final LinearLayout mp3Layout = new LinearLayout(getContext());
		mp3Layout.setOrientation(LinearLayout.VERTICAL);
		final List<View> mp3Views = new ArrayList<View>();
		new Thread(){
			public void run() {
				for(int i = 0;i< mp3List.size();i++){
				    LinearLayout mp3ItemLayout = new LinearLayout(getContext());
				    mp3ItemLayout.setOrientation(LinearLayout.HORIZONTAL);
				    mp3ItemLayout.setGravity(Gravity.CENTER_VERTICAL);
					final int index = i;
					final ImageView imageView = new ImageView(getContext());
					imageView.setScaleType(ScaleType.CENTER_INSIDE);
					imageView.setImageResource(R.drawable.icon_mp3);
					mp3ItemLayout.addView(imageView,mMp3Par);
					if(mp3List.get(i) != null && !"".equals(mp3List.get(i))){
						final TextView tv = new TextView(getContext());
						tv.setText(mp3List.get(i).getName());
						tv.setTextColor(getContext().getResources().getColor(R.color.black));
						tv.setTextSize(14);
						mp3ItemLayout.addView(tv);
					}
					imageView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							if(mQADetailViewClick != null)
								mQADetailViewClick.clickMp3(mp3List.get(index));
						}
					});
					mp3Views.add(mp3ItemLayout);
				}
				ViewHolder viewHolder = new ViewHolder();
				viewHolder.mainView = mp3Layout;
				viewHolder.imageView = mp3Views;
				mViewHandler.obtainMessage(1102,viewHolder).sendToTarget();
			};
		}.start();
		mainLayout.addView(ivScrollView,mTitlePar);
		mainLayout.addView(vdScrollView,mTitlePar);
		mainLayout.addView(mp3Layout,mTitlePar);
		this.addView(mainLayout,mItemPar);	
	}
	
	
	
	
}
