package flytv.qaonline.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;
import flytv.compos.run.R;

public class NotifyEngine {
	private AlertDialog mHttpLinkDialog;
	private Context mContext;
	
	public NotifyEngine(Context context){
		this.mContext = context;
	}
	
	/**
	 * 显示网络请求对话框
	 * 
	 * @param 无
	 * @return 无
	 */
	public void showHttpDialog(String content) {
		if (mHttpLinkDialog == null) {
			mHttpLinkDialog = new AlertDialog.Builder(mContext).create();// 创建对话框
			mHttpLinkDialog.setCancelable(false);// 设置对话框不可取消
		}
		if (!mHttpLinkDialog.isShowing()) {
			mHttpLinkDialog.show();// 显示对话框
		}
		View view = View.inflate(mContext, R.layout.view_dialog_loading, null);
		TextView contentTv = (TextView)view.findViewById(R.id.dialog_loading_content_tv);
		if (content != null && !"".equals(content)) {
			contentTv.setText(content);
			contentTv.setVisibility(View.VISIBLE);
	    }else{
	    	contentTv.setVisibility(View.GONE);
	    }
		mHttpLinkDialog.getWindow().setContentView(view);// 设置对话框样式
	}

	/**
	 * 关闭网络请求对话框
	 * 
	 * @param 无
	 * @return 无
	 */
	public void disMissHttpDialog() {
		if (mHttpLinkDialog != null && mHttpLinkDialog.isShowing())
			mHttpLinkDialog.dismiss();
	}
	
}
