package flytv.compos.run.adapter;

import in.srain.cube.image.CubeImageView;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import flytv.compos.run.R;
import flytv.compos.run.bean.FileBean;
import flytv.ext.utils.AppUtil;

public class AdFriendImage extends BaseAdapter {

	private Context context;
	public BitmapUtils bitmapUtils;
	private ArrayList<FileBean> fileBean;
	private ViewImageHolder viewImageHolder;

	public AdFriendImage(Context context, BitmapUtils bitmapUtils,
			ArrayList<FileBean> fileBean) {
		super();
		this.context = context;
		this.bitmapUtils = bitmapUtils;
		this.fileBean = fileBean;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return fileBean.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final FileBean entity = fileBean.get(position);
		if (convertView == null) {
			viewImageHolder = new ViewImageHolder();
			convertView = View.inflate(context,
					R.layout.photo_layout_new_image_item, null);
			viewImageHolder.image_backgroud = (CubeImageView) convertView
					.findViewById(R.id.im_backgourd);
			viewImageHolder.image_play = (ImageView) convertView
					.findViewById(R.id.im_play);
			convertView.setTag(viewImageHolder);
		} else {
			viewImageHolder = (ViewImageHolder) convertView.getTag();
		}
		boolean isMp3 = false;
		String imageHttp = "";
		if (entity.getExtType().equals("video")||entity.getExtType().equals("mp4")) {
			viewImageHolder.image_play.setVisibility(View.VISIBLE);
			imageHttp = entity.getThumbPath();
		}else if(entity.getExtType().equals("mp3")||entity.getExtType().equals("caf")) {
			viewImageHolder.image_play.setVisibility(View.VISIBLE);
			isMp3 = true;
		} else {
			viewImageHolder.image_play.setVisibility(View.GONE);
			imageHttp = entity.getFileUrl();
		}
		final Bitmap bitmap = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.icon_user_image_nor);
		if(isMp3){
			viewImageHolder.image_backgroud.setImageResource(R.drawable.transparent);
			viewImageHolder.image_play.setImageResource(R.drawable.icon_mp3);
		}else{
			if (!AppUtil.isStrNull(imageHttp)) {
				boolean isShow = imageHttp.contains("http");
				String showImageUrl = null;
				if(!isShow){
					showImageUrl = AppUtil.UPLOADPATH + imageHttp;
				}else{
					showImageUrl =  imageHttp;
				}
				final String imageUrl = AppUtil.getIPSplit(context,showImageUrl);
				viewImageHolder.image_backgroud.setTag(imageUrl);
				bitmapUtils.configDefaultLoadingImage(bitmap);
				bitmapUtils.display(viewImageHolder.image_backgroud, imageUrl,
						new BitmapLoadCallBack<View>() {

							@Override
							public void onLoadCompleted(View arg0, String arg1,
									Bitmap arg2, BitmapDisplayConfig arg3,
									BitmapLoadFrom arg4) {
								CubeImageView roundedImageView = (CubeImageView) arg0
										.findViewWithTag(arg1);
								if (roundedImageView != null) {
									roundedImageView.setImageBitmap(arg2);
								}
							}

							@Override
							public void onLoadFailed(View arg0, String arg1,
									Drawable arg2) {
								CubeImageView roundedImageView = (CubeImageView) arg0
										.findViewWithTag(arg1);
								if (roundedImageView != null) {
									roundedImageView.setImageBitmap(bitmap);
								}
							}
						});
				
			} else {
				viewImageHolder.image_backgroud.setImageBitmap(bitmap);
			}
		}
		

		return convertView;
	}

	class ViewImageHolder {
		CubeImageView image_backgroud;
		ImageView image_play;
	}

}
