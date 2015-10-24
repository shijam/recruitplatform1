package flytv.qaonline.utils;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.content.Intent;
import flytv.compos.run.adpublic.AyVideoCompoe;
import flytv.ext.utils.AppUtil;
import flytv.qaonline.ui.MyImageActivity;

public class MyUtils {
	public static void startResource(Context context,String httpUrl){
		Intent intent = new Intent();
		if(StringUtils.isBlank(httpUrl)){
			return;
		}
		intent.putExtra("httpUrl", httpUrl);
		if(httpUrl.contains("jpg")||httpUrl.contains("png")){
			intent.setClass(context, MyImageActivity.class);
		}else if(httpUrl.contains("mp4")){
			intent.setClass(context, AyVideoCompoe.class);
		}
		context.startActivity(intent);
	}
}
