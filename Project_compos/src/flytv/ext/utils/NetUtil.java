package flytv.ext.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import flytv.compos.run.R;

public class NetUtil {
	private static final String TAG = "NetUtil";
	private static BasicHeader[] headers = new BasicHeader[10];
	public static boolean isExt =false;
	static {
		headers[0] = new BasicHeader("Appkey", "");
		headers[1] = new BasicHeader("Udid", "");
		headers[2] = new BasicHeader("Os", "");
		headers[3] = new BasicHeader("Osversion", "");
		headers[4] = new BasicHeader("Appversion", "");
		headers[5] = new BasicHeader("Sourceid", "");
		headers[6] = new BasicHeader("Ver", "");
		headers[7] = new BasicHeader("Userid", "");
		headers[8] = new BasicHeader("Usersession", "");
		headers[9] = new BasicHeader("Unique", "");
	}

	/**
	 * 
	 * @param vo
	 * @return
	 */
	public static Object get(RequestVo vo) {
		DefaultHttpClient client = new DefaultHttpClient();
		StringBuffer paramsUrl = new StringBuffer();
		String httpPostStr = null;
		String userHost = null;
		if (vo.netUser == NetUser.STB) {
			userHost = AppUtil.getStringId(vo.context);
		} else if (vo.netUser == NetUser.FLYTV) {
			userHost = vo.context.getString(R.string.app_host);
			userHost += "/";
		}else{
			userHost = vo.context.getString(R.string.app_host);
		}
		if (!vo.isFullUrl) {
			String string = userHost;
			httpPostStr = string.concat(vo.requesStr);
		} else {
			httpPostStr = vo.httpUrl;
		}
		if (vo.requestDataMap != null) {
			HashMap<String, String> map = vo.requestDataMap;
			int i = 0;
			for (Map.Entry<String, String> entry : map.entrySet()) {
				if (i == 0) {
					paramsUrl.append("?" + entry.getKey() + "="
							+ entry.getValue());
					i++;
				} else {
					paramsUrl.append("&" + entry.getKey() + "="
							+ entry.getValue());
				}
			}
			httpPostStr = httpPostStr + paramsUrl.toString();
			httpPostStr = httpPostStr.replaceAll(" ", "%20");
		}
		// Http Request timer
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
		// Http Response timer
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);
		HttpGet get = new HttpGet(httpPostStr);
		LogUtils.print(1, "get" + httpPostStr);
		// get.setHeaders(headers);
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
		// Http Response timer
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);

		Object obj = null;

		try {
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				LogUtils.print(1, NetUtil.class.getSimpleName() + result);
				try {
					obj = vo.jsonParser.parseJSON(result);
				} catch (JSONException e) {
					Log.e(NetUtil.class.getSimpleName(),
							e.getLocalizedMessage(), e);
				}
				return obj;
			} else {
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				Log.i(TAG, response.getStatusLine().getStatusCode()
						+ "response:Error" + result);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查网络
	 * 
	 * @param context
	 * @return
	 */

	public static boolean hasNetwork(Context context) {
		ConnectivityManager con = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo workinfo = con.getActiveNetworkInfo();
		if (workinfo == null || !workinfo.isAvailable()) {
			Log.i(TAG, "wangluo  chucuole  ");
			return false;
		}
		return true;
	}

	/*
	 * 
	 */
	public static Object post(RequestVo vo) {
		DefaultHttpClient client = new DefaultHttpClient();
		String httpPostStr = null;
		String userHost = null;
		if (vo.netUser == NetUser.STB) {
			userHost = AppUtil.getStringId(vo.context);
		} else if (vo.netUser == NetUser.FLYTV) {
			userHost = vo.context.getString(R.string.app_host);
			userHost += "/";
		}else{
			userHost = vo.context.getString(R.string.app_host);
		}
		if (!vo.isFullUrl) {
			String string = userHost;
			httpPostStr = string.concat(vo.requesStr);
		} else {
			httpPostStr = vo.httpUrl;
		}
		HttpPost post = new HttpPost(httpPostStr);
		LogUtils.print(1, "post" + httpPostStr);
		// post.setHeaders(headers);
		client.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
		// Http Response timer
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 15000);

		Object obj = null;
		try {
			if (vo.requestDataMap != null) {
				HashMap<String, String> map = vo.requestDataMap;
				ArrayList<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
				for (Map.Entry<String, String> entry : map.entrySet()) {
					BasicNameValuePair pair = new BasicNameValuePair(
							entry.getKey(), entry.getValue());

					pairList.add(pair);
				}
				HttpEntity entity = new UrlEncodedFormEntity(pairList, "UTF-8");
				LogUtils.print(1, map.toString());
				post.setEntity(entity);
			}
			// Http Request timer
			client.getParams().setParameter(
					CoreConnectionPNames.CONNECTION_TIMEOUT, 15000);
			// Http Response timer
			client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
					15000);
			HttpResponse response = client.execute(post);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				LogUtils.print(1, "result="+result);
				try {
					obj = vo.jsonParser.parseJSON(result);
				} catch (JSONException e) {
					Log.e(NetUtil.class.getSimpleName(),
							e.getLocalizedMessage(), e);
				}
				return obj;
			} else {
				String result = EntityUtils.toString(response.getEntity(),
						"UTF-8");
				Log.i(TAG, response.getStatusLine().getStatusCode()
						+ "@response:Error" + result);
			}
		} catch (ClientProtocolException e) {
			Log.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(), e);
		} catch (IOException e) {
			Log.e(NetUtil.class.getSimpleName(), e.getLocalizedMessage(), e);
		}
		return null;
	}

}
