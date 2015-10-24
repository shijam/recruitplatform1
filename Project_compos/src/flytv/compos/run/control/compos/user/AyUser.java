package flytv.compos.run.control.compos.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import flytv.compos.run.AyRegisterLogin;
import flytv.compos.run.R;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.ActivityUtils;
import flytv.ext.utils.AlertHelp;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.FileUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.MyApplication;
import flytv.ext.utils.SWDateUtil;
import flytv.ext.utils.SWUpdateUtil;
import flytv.ext.utils.UserShareUtils;
import flytv.ext.view.RoundedImageView;
import flytv.run.bean.TVCodeBean;

public class AyUser extends BaseActivity {

	@ViewInject(R.id.ivTitleBtnLeft)
	private ImageButton ivTitleBtnLeft;

	@ViewInject(R.id.ivTitleBtnRight)
	private Button ivTitleBtnRight;

	@ViewInject(R.id.button_grade)
	private Button button_grade;

	@ViewInject(R.id.ivTitleName)
	private TextView ivTitleName;

	@ViewInject(R.id.tx_name)
	private TextView tx_name;

	@ViewInject(R.id.tx_score)
	private TextView tx_score;

	@ViewInject(R.id.imageView_icon)
	private RoundedImageView imageView_icon;
	private TVCodeBean tvCodeBean;
	private BitmapUtils bitmapUtils;

	@OnClick({ R.id.ivTitleBtnLeft, R.id.ivTitleBtnRight, R.id.ivTitleName,
			R.id.layout_info, R.id.layout_collection_abole, R.id.btn_out,
			R.id.imageView_icon })
	public void onClickComment(View view) {
		switch (view.getId()) {
		// 添加评论
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		case R.id.layout_info:
			Intent intent = new Intent(AyUser.this, UserInfoAd.class);
			startActivity(intent);
			break;
		case R.id.layout_collection_abole:
			Intent intentAd = new Intent(AyUser.this, AyUserSort.class);
			startActivity(intentAd);
			break;
		case R.id.btn_out:
			MyApplication.getInstance().exit(false);
			UserShareUtils.getInstance().clearLoginInfo(context);
			ActivityUtils.startActivity(AyUser.this, AyRegisterLogin.class);
			break;
		case R.id.imageView_icon:
			AlertHelp.showDialog(AyUser.this);
			break;

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.print(1, "onActivityResult()" + this.getClass().getName());
		switch (requestCode) {
		case 200:
			if (resultCode == Activity.RESULT_OK) {
				try {
					final Uri uri = data.getData();
					Cursor cursor = context.getContentResolver().query(uri,
							null, null, null, null);
					if (cursor != null) {
						int colunm_index = cursor
								.getColumnIndex(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String filePath = cursor.getString(colunm_index);
						cursor.close();
						upload(filePath);
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Bitmap bm = null;
						// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
						ContentResolver resolver = context.getContentResolver();
						// 此处的用于判断接收的Activity是不是你想要的那个
						Uri originalUri = data.getData(); // 获得图片的uri
						bm = MediaStore.Images.Media.getBitmap(resolver,
								originalUri);
						Bitmap bitmap = AppUtil.ResizeBitmap(bm, 720);
						if (bm.isRecycled()) {
							bm.recycle();
							System.gc();
						}
						File file = new File(FileUtil.noteSdImageFile);
						if (!file.exists()) {
							file.mkdir();
						}
						String filepath = FileUtil.noteSdImageFile
								+ "/"
								+ SWDateUtil.getFormatDateByOffset(
										"yyMMddHHssmm", 0) + ".jpg";
						File imageUrl = new File(filepath);
						if (!imageUrl.exists()) {
							imageUrl.createNewFile();
						}
						AppUtil.compressBmpToFile(bitmap, imageUrl);
						upload(filepath);
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
						AlertTools.showTips(context, R.drawable.tips_warning,
								"请选择拍照功能！选择本地图片失败！");
					} catch (IOException ee) {
						ee.printStackTrace();
						AlertTools.showTips(context, R.drawable.tips_warning,
								"请选择拍照功能！选择本地图片失败！");
					} // 显得到bitmap图片
				}

			}
			break;
		case 300:
			if (resultCode == Activity.RESULT_OK) {
				Uri fileUri = Uri.fromFile(AlertHelp.mediaFile);
				if (fileUri != null) {
					String filePath = fileUri.getPath();
					upload(filePath);
				}
			}
			break;
		case 400:
			if (resultCode == Activity.RESULT_OK) {
				Uri fileUri = Uri.fromFile(AlertHelp.mediaFile);
				if (fileUri != null) {
					String filePath = fileUri.getPath();
					upload(filePath);
				}
			}
			break;
		case 500:
			if (resultCode == Activity.RESULT_OK) {
				String filePath = data.getStringExtra("FileImage");
				upload(filePath);
			}
			break;
		}
	}

	void upload(String filePath) {
		LogUtils.print(1, "filePath=" + filePath);
		TVCodeBean entity = (TVCodeBean) UserShareUtils.getInstance()
				.getLoginInfo(context);
		final String fileName = filePath
				.substring(filePath.lastIndexOf("/") + 1);
		if (AppUtil.isStrNull(filePath)) {
			AlertTools.showTips(context, R.drawable.tips_warning, "上传路径错误");
			return;
		}
		String fileUploadUrl = null;
		if (fileName.contains("jpg") || fileName.contains("png")
				|| fileName.contains("mp4") || fileName.contains("mp3")) {
			showDataDialog();
			fileUploadUrl = AppUtil.getStringId(context)
					+ getString(R.string.flytv_user_file_uploadImage).replace(
							"{userId}", entity.getUserId());
			RequestParams params = new RequestParams();
			params.addBodyParameter("fileName", fileName);
			TVCodeBean entityFile = UserShareUtils.getInstance().getTVInfo(
					context);
			params.addBodyParameter("deviceNo", entityFile.getDeviceNo());
			params.addBodyParameter("userId", entity.getUserId());
			if (fileName.contains("jpg")) {
				Bitmap bitmap = AppUtil.getSmallBitmap(filePath);
				AppUtil.compressBmpToFile(bitmap, new File(filePath));
				if (bitmap.isRecycled()) {
					bitmap.recycle();
					System.gc();
				}
			}
			File file = new File(filePath);
			long fileSize = file.length();
			long kbSize = fileSize / 1024;
			LogUtils.print(1, "kbSize=" + kbSize + "KB");
			params.addBodyParameter("file", file);
			HttpUtils http = new HttpUtils();
			http.configTimeout(60000);
			LogUtils.print(1, "url=" + fileUploadUrl);
			http.send(HttpRequest.HttpMethod.POST, fileUploadUrl, params,
					new RequestCallBack<String>() {
						@Override
						public void onStart() {
							// msgTextview.setText("conn...");

						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
							if (isUploading) {
								// msgTextview.setText("upload: " + current +
								// "/"+ total);
							} else {
								// msgTextview.setText("reply: " + current +
								// "/"+ total);
							}
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							LogUtils.print(arg0.getMessage());
							closeDataDialog();
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
							LogUtils.print("arg0=" + arg0.result);
							closeDataDialog();
							updateImagePath = (TVCodeBean) AppUtil.getJSONBean(
									arg0.result, TVCodeBean.class);
							if (updateImagePath != null) {
								updateImage();
							}
						}
					});
		}

	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		MyApplication.getInstance().addActivity(this);
		button_grade.setVisibility(View.INVISIBLE);
		ivTitleBtnRight.setVisibility(View.INVISIBLE);
		ivTitleName.setText(getString(R.string.app_user_center));
		bitmapUtils = new BitmapUtils(this);
		if(tvCodeBean.getUserType().equals("2")){
			tx_score.setVisibility(View.VISIBLE);
		}else{
			tx_score.setVisibility(View.GONE);
		}
		initImage();
		loginInfo();

	}

	public void initImage() {
		Intent intent = new Intent();
		intent.setAction("uploadImage_flytv.com");
		sendBroadcast(intent);
		String imageHttp = tvCodeBean.getPhoto();
		final Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.default_logo);
		if (!AppUtil.isStrNull(imageHttp)) {
			showDataDialog();
			boolean isShow = imageHttp.contains("http");
			String showImageUrl = null;
			if (!isShow) {
				showImageUrl = AppUtil.UPLOADPATH +"/"+imageHttp;
			} else {
				showImageUrl = imageHttp;
			}
			final String imageUrl = AppUtil.getIPSplit(AyUser.this,
					showImageUrl);
			imageView_icon.setTag(imageUrl);
			;
			bitmapUtils.configDefaultLoadingImage(bitmap);
			bitmapUtils.display(imageView_icon, imageUrl,
					new BitmapLoadCallBack<View>() {

						@Override
						public void onLoadCompleted(View arg0, String arg1,
								Bitmap arg2, BitmapDisplayConfig arg3,
								BitmapLoadFrom arg4) {
							closeDataDialog();
							RoundedImageView roundedImageView = (RoundedImageView) arg0
									.findViewWithTag(arg1);
							if (roundedImageView != null) {
								roundedImageView.setImageBitmap(arg2);
							}
						}

						@Override
						public void onLoadFailed(View arg0, String arg1,
								Drawable arg2) {
							closeDataDialog();
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

	void loginInfo() {
		AppUtil.getInfo(context);
		String mothodUrl = getString(R.string.app_host)
				+ getString(R.string.flytv_user_login_info) + "?userId="
				+ tvCodeBean.getUserId()+"&deviceType=Android_Phone";
		try {
			PackageManager pm = context.getPackageManager();// 取得包管理器
			PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);// 获取包的详细信息
			int oldVersion = pi.versionCode;
			mothodUrl = mothodUrl+"&versionCode="+oldVersion;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		LogUtils.print("url="+mothodUrl);
		HttpUtils http = new HttpUtils();
		http.configTimeout(10000);
		http.configCurrentHttpCacheExpiry(2000);
		http.send(HttpRequest.HttpMethod.GET, mothodUrl,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						closeDataDialog();
						try {
							LogUtils.print("responseInfo ="
									+ responseInfo.result);
							closeDataDialog();
							if (AppUtil.isStrNull(responseInfo.result)) {
								AlertTools.showTips(context,
										R.drawable.tips_warning, "服务器连接失败!");
								return;
							}
							// 需要信息 到本地
							TVCodeBean entity = AppUtil.getPerson(
									responseInfo.result, TVCodeBean.class);
							if (entity.getMessage() == 1) {
								int point = entity.getPoint();
								tvCodeBean.setPoint(point);
								tvCodeBean.setCSRQ(entity.getCSRQ());
								tvCodeBean.setAppVersion(entity.getAppVersion());
								tx_score.setText(getString(R.string.main_text_compos_user_score)
										+ ": " + point);
								//
								// 版本信息
								if(entity.getVersionInfo()!=null){
									boolean isUpdate = SWUpdateUtil.isUpdate(context,
											entity.getVersionInfo());
									if(isUpdate){
										SWUpdateUtil.isUpdate(AyUser.this, entity.getVersionInfo());
									}
								}
								UserShareUtils.getInstance().setLoginInfo(
										context, tvCodeBean);
								if(!entity.getPhoto().equals(tvCodeBean.getPhoto())){
									tvCodeBean.setPhoto(entity.getPhoto());
									UserShareUtils.getInstance().setLoginInfo(
											context, tvCodeBean);
									initImage();
									
								}
								Intent intent = new Intent();
								intent.setAction("uploadImage_flytv.com");
								sendBroadcast(intent);
							} else {
								AlertTools.showTips(context,
										R.drawable.tips_warning,
										entity.getMsgInfo());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						closeDataDialog();
						LogUtils.print("" + msg);
						AppUtil.isNetWork(context);
					}
				});
	}

	TVCodeBean updateImagePath = null;

	void updateImage() {
		String mothodUrl = getString(R.string.app_host)
				+ getString(R.string.flytv_user_login_info_header) + "?userId="
				+ tvCodeBean.getUserId() + "&filePath="
				+ updateImagePath.getFilePath();
		HttpUtils http = new HttpUtils();
		closeDataDialog();
		http.configTimeout(10000);
		http.configCurrentHttpCacheExpiry(2000);
		http.send(HttpRequest.HttpMethod.GET, mothodUrl,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						closeDataDialog();
						try {
							LogUtils.print("responseInfo ="
									+ responseInfo.result);
							closeDataDialog();
							if (AppUtil.isStrNull(responseInfo.result)) {
								AlertTools.showTips(context,
										R.drawable.tips_warning, "服务器连接失败!");
								return;
							}
							LogUtils.print("获取登陆信息=" + responseInfo.result);
							// 需要信息 到本地
							TVCodeBean entity = AppUtil.getPerson(
									responseInfo.result, TVCodeBean.class);
							if (entity.getMessage() == 1) {
								String imagePath = updateImagePath.getFilePath();
								tvCodeBean.setPhoto(imagePath);
								UserShareUtils.getInstance().setLoginInfo(
										context, tvCodeBean);
								initImage();
							} else {
								AlertTools.showTips(context,
										R.drawable.tips_warning,
										entity.getMsgInfo());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						closeDataDialog();
						LogUtils.print("" + msg);
						AppUtil.isNetWork(context);
					}
				});
	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.user_ad);
		ViewUtils.inject(this);
		tvCodeBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				this);

		tx_name.setText(tvCodeBean.getUserName());
		tx_score.setText(getString(R.string.main_text_compos_user_score) + ": "
				+ tvCodeBean.getPoint());
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

}
