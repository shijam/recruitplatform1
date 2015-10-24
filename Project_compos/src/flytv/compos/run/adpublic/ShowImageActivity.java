package flytv.compos.run.adpublic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import flytv.compos.run.R;
import flytv.ext.base.BaseActivity;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;

public class ShowImageActivity extends BaseActivity {

	ImageView imageview_background;
	String httpImageUrl = null;

	private BitmapUtils bitmapUtils;

	@ViewInject(R.id.imageView2)
	ImageView imageColse;

	@OnClick({ R.id.imageView2 })
	public void click(View view) {
		switch (view.getId()) {
		case R.id.imageView2:
			finish();
			break;
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		imageview_background = (ImageView) findViewById(R.id.imageView1);
		this.bitmapUtils = new BitmapUtils(ShowImageActivity.this);
		final Bitmap bitmap = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.icon_user_image_nor);
		if (!AppUtil.isStrNull(httpImageUrl)) {
			imageview_background.setTag(httpImageUrl);
			bitmapUtils.configDefaultLoadingImage(bitmap);
			bitmapUtils.display(imageview_background, httpImageUrl,
					new BitmapLoadCallBack<View>() {

						@Override
						public void onLoadCompleted(View arg0, String arg1,
								Bitmap arg2, BitmapDisplayConfig arg3,
								BitmapLoadFrom arg4) {
							LogUtils.print("onLoadCompleted()");
							ImageView roundedImageView = (ImageView) arg0
									.findViewWithTag(arg1);
							if (roundedImageView != null) {
								roundedImageView.setImageBitmap(arg2);
							}
						}

						@Override
						public void onLoadFailed(View arg0, String arg1,
								Drawable arg2) {
							LogUtils.print("onLoadFailed()");
							ImageView roundedImageView = (ImageView) arg0
									.findViewWithTag(arg1);
							if (roundedImageView != null) {
								roundedImageView.setImageBitmap(bitmap);
							}
						}
					});
		} else {
			imageview_background.setImageBitmap(bitmap);
		}

	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.image_show);
		ViewUtils.inject(this);
		httpImageUrl = getIntent().getStringExtra("httpUrl");
		LogUtils.print("image="+httpImageUrl);
	}

	@Override
	protected void processLogic() {
		

	}

	@Override
	protected void setListener() {
		imageview_background.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
	}
}
