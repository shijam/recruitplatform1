package flytv.qaonline.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import flytv.compos.run.R;
import flytv.ext.base.BaseActivity;
import flytv.ext.utils.AppUtil;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.http.HttpServer;
import flytv.qaonline.image.BitmapUtils.BitmapStyle;

public class MyImageActivity extends BaseActivity {
	ImageView imageview_background;
	String httpImageUrl = null;	

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
		imageview_background = (ImageView) findViewById(R.id.imageView1);
	}

	@Override
	protected void loadViewLayout() {
		setContentView(R.layout.image_show);
		ViewUtils.inject(this);
		httpImageUrl = getIntent().getStringExtra("httpUrl");

	}

	@Override
	protected void processLogic() {
		final Bitmap bitmap = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.icon_user_image_nor);
		if (!AppUtil.isStrNull(httpImageUrl)) {
			imageview_background.setTag(httpImageUrl);
			HttpEngine.getHttpEngine(MyImageActivity.this).requestImageData(
					imageview_background, httpImageUrl, httpImageUrl, 0,
					new HttpEngineListener() {
						@Override
						public void requestCallBack(final Object data,
								final String resultCode, final int requestCode) {
							final ImageView imageView = (ImageView) imageview_background
									.findViewWithTag(httpImageUrl);
							if (imageView != null) {
								if (data != null
										&& requestCode == 0
										&& (HttpServer.HTTPSTATE_SUCCESS
												.equals(resultCode))) {
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											imageView
													.setImageBitmap((Bitmap) data);
										}
									});
								}
							}
						}
					}, BitmapStyle.NULL, true);
		} else {
			imageview_background.setImageBitmap(bitmap);
		}

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
