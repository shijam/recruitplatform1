package flytv.qaonline.view;

import java.io.Serializable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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

public class LocatFileView extends LinearLayout {
	public interface LocatFileViewClick{
		public void clickImage(FileBean fileIetmEntity);
		public void clickVideo(FileBean fileIetmEntity);
		public void clickMp3(FileBean fileIetmEntity);
		public void delFile(FileBean fileIetmEntity,int type);
	}
	public class ImageObject implements Serializable{
		private static final long serialVersionUID = 1L;
		public ImageView imageIv;
		public Bitmap bitmap;
	}
	private LinearLayout.LayoutParams mItemPar;
	private LinearLayout.LayoutParams mImagePar;
	private LinearLayout.LayoutParams mMp3Par;
	private LinearLayout.LayoutParams mp3IvPar;
	private LocatFileViewClick mLocatFileViewClick;
	private LinearLayout imageLayout;
	private LinearLayout videoLayout;
	private LinearLayout mp3Layout;
	private MyDialog myDialog;
	
	private Handler mViewHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case 1110:
					ImageObject imageObject = (ImageObject)msg.obj;
					imageObject.imageIv.setImageBitmap(imageObject.bitmap);
					imageObject.imageIv.setTag("");
					break;
				case 1111:
					ImageObject imageObject1 = (ImageObject)msg.obj;
					imageObject1.imageIv.setBackground(new BitmapDrawable(imageObject1.bitmap));
					imageObject1.imageIv.setTag("");
					break;
			default:
				break;
			}
		}
	};
	
	public LocatFileView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initViewData();
	}

	public LocatFileView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initViewData();
	}

	public LocatFileView(Context context) {
		super(context);
		initViewData();
	}

	private void initViewData(){
		myDialog = new MyDialog(this);
		mItemPar = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		mMp3Par = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		int dp6 = DensityConfig.dip2px(getContext(), 6);
		int dp10 = DensityConfig.dip2px(getContext(), 10);
		int dp50 = DensityConfig.dip2px(getContext(), 30);
		int dp180 = DensityConfig.dip2px(getContext(), 120);
		
		mp3IvPar = new LinearLayout.LayoutParams(dp50,dp50);
		mImagePar = new LinearLayout.LayoutParams(dp180,dp180);
		mImagePar.rightMargin = dp10;
 
		HorizontalScrollView ivScrollView = new HorizontalScrollView(getContext());
		ivScrollView.setPadding(0,dp6, DensityConfig.dip2px(getContext(), 10),dp6);
		ivScrollView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
		imageLayout = new LinearLayout(getContext());
		imageLayout.setOrientation(LinearLayout.HORIZONTAL);
		ivScrollView.addView(imageLayout);
		
		HorizontalScrollView videoScrollView = new HorizontalScrollView(getContext());
		videoScrollView.setPadding(0,dp6,dp10, dp6);
		videoScrollView.setScrollBarStyle(SCROLLBARS_OUTSIDE_OVERLAY);
		videoLayout = new LinearLayout(getContext());
		videoLayout.setOrientation(LinearLayout.HORIZONTAL);
		videoScrollView.addView(videoLayout);
		
		mp3Layout = new LinearLayout(getContext());
		videoScrollView.setPadding(0, 0, dp10, 0);
		mp3Layout.setOrientation(LinearLayout.VERTICAL);
		
		this.addView(ivScrollView,mItemPar);		
		this.addView(videoScrollView,mItemPar);		
		this.addView(mp3Layout,mItemPar);	
	}
	
	public void addImageView(final FileBean fileBean){
		final String imageUrl = fileBean.getFileUrl();
		final ImageView imageView = new ImageView(getContext());
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mLocatFileViewClick != null)
					mLocatFileViewClick.clickImage(fileBean);
			}
		});
		imageView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				showDelDialog(fileBean,1,v);
				return false;
			}
		});
		imageLayout.addView(imageView,mImagePar);
		imageView.setTag(imageUrl);
		HttpEngine.getHttpEngine(getContext()).requestImageData(imageView,imageUrl, imageUrl,0,
				new HttpEngineListener() {
			@Override
			public void requestCallBack(final Object data, final String resultCode, final int requestCode) {
				final ImageView iv = (ImageView)imageLayout.findViewWithTag(imageUrl);
				if(iv != null ){
					if(data != null && (HttpServer.HTTPSTATE_SUCCESS.equals(resultCode))){
						ImageObject object = new ImageObject();
						object.bitmap = (Bitmap)data;
						object.imageIv = iv;
						mViewHandler.obtainMessage(1110,object).sendToTarget();
					}
				}
		}
	  },BitmapStyle.NULL,true);
	}
	
	public void addVideoView(final FileBean fileBean){
		final String imageUrl = fileBean.getThumbPath();
		final ImageView imageView = new ImageView(getContext());
		imageView.setScaleType(ScaleType.CENTER_INSIDE);
		imageView.setImageResource(R.drawable.im_play_nor);
		int dp10 = DensityConfig.dip2px(getContext(), 10);
		imageView.setPadding(dp10,dp10,dp10,dp10);
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mLocatFileViewClick != null)
					mLocatFileViewClick.clickVideo(fileBean);
			}
		});
		imageView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				showDelDialog(fileBean,2,v);
				return false;
			}
		});
		videoLayout.addView(imageView,mImagePar);
		imageView.setTag(imageUrl);
		HttpEngine.getHttpEngine(getContext()).requestImageData(imageView,imageUrl, imageUrl,0,
				new HttpEngineListener() {
			@Override
			public void requestCallBack(final Object data, final String resultCode, final int requestCode) {
				final ImageView iv = (ImageView)videoLayout.findViewWithTag(imageUrl);
				if(iv != null ){
					if(data != null && (HttpServer.HTTPSTATE_SUCCESS.equals(resultCode))){
						ImageObject object = new ImageObject();
						object.bitmap = (Bitmap)data;
						object.imageIv = iv;
						mViewHandler.obtainMessage(1111,object).sendToTarget();
					}
				}
			}
		},BitmapStyle.NULL,false);
	}
		
	public void addMp3View(final FileBean fileBean){
		final LinearLayout mp3ItemLayout = new LinearLayout(getContext());
		mp3ItemLayout.setPadding(0, DensityConfig.dip2px(getContext(), 4), 0, DensityConfig.dip2px(getContext(), 4));
	    mp3ItemLayout.setOrientation(LinearLayout.HORIZONTAL);
	    mp3ItemLayout.setGravity(Gravity.CENTER_VERTICAL);
		final ImageView imageView = new ImageView(getContext());
		imageView.setScaleType(ScaleType.CENTER_INSIDE);
		imageView.setImageResource(R.drawable.icon_mp3);
		mp3ItemLayout.addView(imageView,mp3IvPar);
		if(fileBean != null){
			final TextView tv = new TextView(getContext());
			tv.setText(fileBean.getName());
			tv.setTextColor(getContext().getResources().getColor(R.color.black));
			tv.setTextSize(13);
			tv.setPadding(DensityConfig.dip2px(getContext(), 10), 0, 0, 0);
			mp3ItemLayout.addView(tv);
		}
		imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mLocatFileViewClick != null)
					mLocatFileViewClick.clickMp3(fileBean);
			}
		});
		imageView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				showDelDialog(fileBean,3,mp3ItemLayout);
				return false;
			}
		});
		mp3Layout.addView(mp3ItemLayout,mMp3Par);
	}	
	
	public void setLocatFileViewClick(LocatFileViewClick qaDetailViewClick){
		this.mLocatFileViewClick = qaDetailViewClick;
	}
	
	private void showDelDialog(final FileBean fileBean,final int type,final View view){
		AlertDialog.Builder alert = new AlertDialog.Builder(getContext())
		.setMessage("确认删除此文件")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (type) {
					case 1:
						imageLayout.removeView(view);
						break;
					case 2:
						videoLayout.removeView(view);
					case 3:
						mp3Layout.removeView(view);
						break;
					default:
						break;
				}
				if(mLocatFileViewClick != null)
					mLocatFileViewClick.delFile(fileBean,type);
				dialog.dismiss();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}
}
