package flytv.ext.base;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothClass.Device;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

import flytv.compos.run.R;
import flytv.ext.tools.AlertTools;
import flytv.ext.tools.LoadingDataDialog;
import flytv.ext.utils.Constant;
import flytv.ext.utils.FileUtil;
import flytv.ext.utils.NetUtil;
import flytv.ext.utils.RequestVo;
import flytv.ext.utils.ThreadPoolManager;

/**
 * 所有activity基类
 * 
 * 
 * 
 */
public abstract class BaseActivity extends Activity implements
		View.OnClickListener {

	class BaseHandler extends Handler {
		private Context context;
		private DataCallback callBack;
		private RequestVo reqVo;

		public BaseHandler(Context context, DataCallback callBack,
				RequestVo reqVo) {
			this.context = context;
			this.callBack = callBack;
			this.reqVo = reqVo;
		}

		public void handleMessage(Message msg) {
			closeProgressDialog();
			closeDataDialog();
			if (msg.what == Constant.SUCCESS) {
				if (msg.obj == null) {
					callBack.processData(msg.obj, false);
				} else {
					callBack.processData(msg.obj, true);
				}
			} else if (msg.what == Constant.NET_FAILED) {
				AlertTools.showTips(context, R.drawable.tips_error,
						R.string.net_error);
			}
		}
	}

	class BaseTask implements Runnable {
		private Context context;
		private RequestVo reqVo;
		private Handler handler;

		public BaseTask(Context context, RequestVo reqVo, Handler handler) {
			this.context = context;
			this.reqVo = reqVo;
			this.handler = handler;
		}

		@Override
		public void run() {
			Object obj = null;
			Message msg = new Message();
			if (NetUtil.hasNetwork(context)) {
				if (reqVo.isGetUrl) {
					// NetMothold net =new NetMothold();
					obj = NetUtil.get(reqVo);
				} else {
					obj = NetUtil.post(reqVo);
				}
				msg.what = Constant.SUCCESS;
				msg.obj = obj;
				handler.sendMessage(msg);
			} else {
				msg.what = Constant.NET_FAILED;
				msg.obj = obj;
				handler.sendMessage(msg);
			}
		}

	}

	public abstract interface DataCallback<T> {
		public abstract void processData(T paramObject, boolean paramBoolean);
	}

	protected int activityCase;
	protected ProgressDialog progressDialog;
	private LoadingDataDialog loadingDataDialog;
	protected TextView textShopCarNum;

	protected Context context;
	protected static ArrayList<Device> listDmr = new ArrayList<Device>();
	private ThreadPoolManager threadPoolManager;

	public BaseActivity() {
		threadPoolManager = ThreadPoolManager.getInstance();
	}

	protected void alertExit() {

		SharedPreferences share = getSharedPreferences("DefualtAlert", 0);
		boolean is = share.getBoolean("isContinue", true);
		if (is) {
			/*
			 * Intent intent = new Intent(context, HomeBackAlertActivity.class);
			 * startActivityForResult(intent, 1000);
			 */
		}
	}

	/**
	 * 关闭进度条
	 */
	protected void closeProgressDialog() {
		if (this.progressDialog != null)
			this.progressDialog.dismiss();
	}

	protected void closeDataDialog() {
		if (this.loadingDataDialog != null && !this.isFinishing())
			if (loadingDataDialog.isShowing()) {
				this.loadingDataDialog.dismiss();
			}

	}

	/**
	 * 
	 */
	protected abstract void findViewById();

	/**
	 * 全局访问网络方法
	 * 
	 * 
	 */
	protected void getDataFromServer(RequestVo reqVo, DataCallback callBack) {
		BaseHandler handler = new BaseHandler(this, callBack, reqVo);
		BaseTask taskThread = new BaseTask(this, reqVo, handler);
		this.threadPoolManager.addTask(taskThread);
	}

	/**
	 * 
	 */
	private void initView() {
		/*
		 * if (!LibsChecker.checkVitamioLibs(this)) return;
		 */
		loadingDataDialog = new LoadingDataDialog(this, R.style.MyDialogStyle);
		initDefault();
		loadViewLayout();
		findViewById();
		setListener();
		processLogic();
	}

	protected String fileUploadUrl;

	void initDefault() {
		fileUploadUrl = getApplicationContext().getString(R.string.app_host)
				+ getApplicationContext().getString(
						R.string.home_file_upload_url);
		if (fileUploadUrl == null) {
			fileUploadUrl = context.getString(R.string.app_host)
					+ context.getString(R.string.home_file_upload_url);
			Log.i("test", fileUploadUrl + "");
		}
		//
		FileUtil.execuFile(context);
		StatService.setAppKey("056fbbfd61");//
		StatService.setAppChannel(this, "Flytv_so", true);
		StatService.setOn(this, StatService.EXCEPTION_LOG);
		StatService.setDebugOn(false);
		StatService.setLogSenderDelayed(10);

	}

	protected boolean isGehuaHml() {
		if (listDmr.size() > 0) {
			return true;
		}
		AlertTools.showTips(context, R.drawable.tips_warning, "没有找到机顶盒 请稍后重试");
		return false;
	}

	/**
	 * 
	 */
	protected abstract void loadViewLayout();

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}

	}

	/**
	 * Android
	 */
	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		context = this.getApplicationContext();
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		StatService.onResume(this);

	}

	@Override
	protected void onPause() {
		super.onPause();
//		StatService.onPause(this);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 获取手机当前音量值
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 
	 */
	protected abstract void processLogic();

	/**
	 *
	 */
	protected abstract void setListener();

	/**
	 * 获取数据
	 */
	protected void showDataDialog() {
		try {
			if (!this.isFinishing()) {
				loadingDataDialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void showProgressDialog() {
		if ((!isFinishing()) && (this.progressDialog == null)) {
			this.progressDialog = new ProgressDialog(this);
		}
		this.progressDialog.setTitle(getString(R.string.loadTitle));
		this.progressDialog.setMessage(getString(R.string.LoadContent));
		progressDialog.setCancelable(false);
		this.progressDialog.show();
	}

	protected void showProgress(String data) {
		this.progressDialog.setMessage(getString(R.string.LoadContent) + data);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closeDataDialog();
	}

}
