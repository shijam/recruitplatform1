package flytv.compos.run.adapter;

import in.srain.cube.image.CubeImageView;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
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
import flytv.ext.view.inter.OnImageListener;

public class AdComposAdd extends BaseAdapter {

	private Context context;
	private ArrayList<FileBean> items;
	private OnImageListener onImageListener;
	private ViewHolderImage viewHolderImage;
	private boolean isShow = false;
	private BitmapUtils bitmapUtils;
	public void setOnImageListener(OnImageListener onImageListener) {
		this.onImageListener = onImageListener;

	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public AdComposAdd(Context context, ArrayList<FileBean> items) {
		super();
		this.context = context;
		this.items = items;
		this.bitmapUtils = new BitmapUtils(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		if (arg1 == null) {
			viewHolderImage = new ViewHolderImage();
			arg1 = View.inflate(context, R.layout.layout_add_image_item, null);
			viewHolderImage.cubeImageView = (CubeImageView) arg1
					.findViewById(R.id.im_backgourd);
			viewHolderImage.image_delete = (ImageView) arg1
					.findViewById(R.id.image_delte);
			viewHolderImage.image_type = (ImageView) arg1
					.findViewById(R.id.img_type);
			arg1.setTag(viewHolderImage);
		}else{
			viewHolderImage = (ViewHolderImage)arg1.getTag();
		}
		final FileBean entity  = items.get(arg0);
		if(entity.getImg_typeId()==1){
			viewHolderImage.cubeImageView.setImageResource(R.drawable.im_add_image);
			viewHolderImage.image_type.setVisibility(View.INVISIBLE);
		}else{
//			cubeImageView
			if(entity.getExtType().equals("img")||entity.getExtType().equals("png")||entity.getExtType().equals("jpg")){
				viewHolderImage.image_type.setVisibility(View.INVISIBLE);
			}else{
				viewHolderImage.image_type.setVisibility(View.VISIBLE);
			}
			
			final Bitmap bitmap = BitmapFactory.decodeResource(
					context.getResources(), R.drawable.icon_user_image_nor);
			String imageHttp = entity.getFileUrl();
			if (!AppUtil.isStrNull(entity.getFileUrl())) {
				boolean isShow = imageHttp.contains("http");
				String showImageUrl = null;
				if(!isShow){
					showImageUrl = AppUtil.UPLOADPATH + imageHttp;
				}else{
					showImageUrl =  imageHttp;
				}
				final String imageUrl = AppUtil.getIPSplit(context,showImageUrl);
				viewHolderImage.cubeImageView.setTag(imageUrl);
				bitmapUtils.configDefaultLoadingImage(bitmap);
				bitmapUtils.display(viewHolderImage.cubeImageView, imageUrl,
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
				viewHolderImage.cubeImageView.setImageBitmap(bitmap);
			}
		}
		// 点击删除
		viewHolderImage.image_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		return arg1;
	}

	class ViewHolderImage {
		CubeImageView cubeImageView;
		ImageView image_delete,image_type;
	}

}
