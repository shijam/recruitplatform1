package flytv.qaonline.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class HttpUtils {
	public interface LoginCallback{
		public void loginFinish();
	}
	private Context mContext;
	private static HttpUtils mHttpUtils;
	
	private HttpUtils(Context mContext) {
		this.mContext = mContext;	
	}
	
	public static HttpUtils getHttpUtils(Context context){
		if(mHttpUtils == null){
			mHttpUtils = new HttpUtils(context);
		}
		return mHttpUtils;
	}
	
	public int getNetWorkType(){
		ConnectivityManager connectMgr = (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectMgr.getActiveNetworkInfo();
		if(info != null){
			if(info.getType() == ConnectivityManager.TYPE_WIFI){
				return 3;
			}else if(info.getType() ==  ConnectivityManager.TYPE_MOBILE){
				switch (info.getSubtype()) {
				case TelephonyManager.NETWORK_TYPE_1xRTT:
				    return 1; // 2G ~ 50-100 kbps
			    case TelephonyManager.NETWORK_TYPE_CDMA:
			       return 1; // 2G ~ 14-64 kbps
			    case TelephonyManager.NETWORK_TYPE_EDGE:
			       return 1; // 2G ~ 50-100 kbps
			    case TelephonyManager.NETWORK_TYPE_GPRS:
			       return 1; // 2G ~ 100 kbps
			    case TelephonyManager.NETWORK_TYPE_EVDO_0:
			       return 2; // 3G ~ 400-1000 kbps
			    case TelephonyManager.NETWORK_TYPE_EVDO_A:
			       return 2; // 3G ~ 600-1400 kbps
			    case TelephonyManager.NETWORK_TYPE_EVDO_B:
			       return 2; // 3G ~ 600-1400 kbps
			    case TelephonyManager.NETWORK_TYPE_HSDPA:
			       return 2; // 3G ~ 2-14 Mbps
			    case TelephonyManager.NETWORK_TYPE_HSPA:
			       return 2; // 3G ~ 700-1700 kbps
			    case TelephonyManager.NETWORK_TYPE_HSUPA:
			       return 2; // 3G ~ 1-23 Mbps
			    case TelephonyManager.NETWORK_TYPE_UMTS:
			       return 2; // 3G ~ 400-7000 kbps
			    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
			       return 0;
				default:
					break;
				}
			}else{
				return 0;
			}
		}
		return 0;
	}
	
}
