package flytv.ext.utils;

import java.util.HashMap;

import android.content.Context;
import flytv.ext.network.BaseParser;



public class RequestVo {
	public int requestUrl;
	public String requesStr;
	public Context context;
	public HashMap<String, String> requestDataMap;
	public BaseParser<?> jsonParser;
	public boolean isFullUrl = false;
	public boolean isGetUrl = false;
	public String httpUrl = null;
	public NetUser netUser;

}

