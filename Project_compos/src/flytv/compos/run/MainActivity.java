package flytv.compos.run;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lidroid.xutils.view.annotation.event.OnItemClick;

import flytv.compos.run.adapter.TabAdapter;
import flytv.compos.run.control.compos.AyCompos;
import flytv.compos.run.control.compos.stu.AyComposMain;
import flytv.compos.run.control.compos.user.AyUser;
import flytv.ext.utils.ActivityUtils;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.MyApplication;
import flytv.ext.utils.UserShareUtils;
import flytv.ext.view.RoundedImageView;
import flytv.qaonline.ui.QAMainActivity;
import flytv.run.bean.TVCodeBean;

public class MainActivity extends Activity {

	@ViewInject(R.id.grid_home)
	private GridView gridView_home;
	@ViewInject(R.id.imageView_icon)
	private RoundedImageView imageView_icon;
	private BitmapUtils bitmapUtils;
	@ViewInject(R.id.tx_name)
	private TextView text_name;
	Class<?>[] classArray = { AyCompos.class, QAMainActivity.class, AyFriend.class };
	Class<?>[] classArray_stu = { AyComposMain.class, QAMainActivity.class, AyFriend.class };
	TVCodeBean tvCodeBean ;
	@OnClick({R.id.imageView_icon})
	public void onclick(View view){
		ActivityUtils.startActivity(MainActivity.this,AyUser.class);
	}
	@OnItemClick({ R.id.grid_home })
	public void itemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if(tvCodeBean.getUserType().equals("2")){
			ActivityUtils.startActivity(MainActivity.this,
					classArray_stu[position]);
		}else{
			ActivityUtils.startActivity(MainActivity.this,
					classArray[position]);
		}
		
	}
	private BroadcastReceiver broadcastReceiver  = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals("uploadImage_flytv.com")){
				tvCodeBean = (TVCodeBean) UserShareUtils.getInstance()
						.getLoginInfo(MainActivity.this);
				initImage();
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
		MyApplication.getInstance().addActivity(this);
		bitmapUtils = new BitmapUtils(this);
		tvCodeBean = (TVCodeBean) UserShareUtils.getInstance()
				.getLoginInfo(this);
		text_name.setText(tvCodeBean.getUserName());
		gridView_home.setAdapter(new TabAdapter(this, new String[] {
				getString(R.string.app_tab_compos),
				getString(R.string.app_tab_lint),
				getString(R.string.app_tab_friend) }, new Integer[] {
				R.drawable.home_icon_compos, R.drawable.home_icon_lint,
				R.drawable.home_icon_friend }));
		
		IntentFilter intentFliter = new IntentFilter();
		intentFliter.addAction("uploadImage_flytv.com");
		registerReceiver(broadcastReceiver, intentFliter);
		initImage();

	}
	public void initImage(){
		String imageHttp = tvCodeBean.getPhoto();
		LogUtils.print("image="+imageHttp);
		final Bitmap bitmap = BitmapFactory.decodeResource(
				getResources(), R.drawable.default_logo);
		if (!AppUtil.isStrNull(imageHttp)) {
			boolean isShow = imageHttp.contains("http");
			String showImageUrl = null;
			if(!isShow){
				showImageUrl = AppUtil.UPLOADPATH +"/"+imageHttp;
			}else{
				showImageUrl =  imageHttp;
			}
			final String imageUrl = AppUtil.getIPSplit(MainActivity.this,showImageUrl);
			imageView_icon.setTag(imageUrl);;
			bitmapUtils.configDefaultLoadingImage(bitmap);
			bitmapUtils.display(imageView_icon, imageUrl,
					new BitmapLoadCallBack<View>() {

						@Override
						public void onLoadCompleted(View arg0, String arg1,
								Bitmap arg2, BitmapDisplayConfig arg3,
								BitmapLoadFrom arg4) {
							RoundedImageView roundedImageView = (RoundedImageView) arg0
									.findViewWithTag(arg1);
							if (roundedImageView != null) {
								roundedImageView.setImageBitmap(arg2);
							}
						}

						@Override
						public void onLoadFailed(View arg0, String arg1,
								Drawable arg2) {
							RoundedImageView roundedImageView = (RoundedImageView) arg0
									.findViewWithTag(arg1);
							if (roundedImageView != null) {
								roundedImageView.setImageBitmap(bitmap);
							}
						}
					});
		} else {
			imageView_icon.setImageBitmap(bitmap);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	protected void onRestart() {
		super.onRestart();
	
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
}
