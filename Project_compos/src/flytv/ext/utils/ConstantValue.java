package flytv.ext.utils;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

public interface ConstantValue {
	
	String CLOUD_ERROR_CODE = "error_code";
	
	int CLOUD_INIT_SUCCESS = 0x6001;

	int CLOUD_INIT_FAIL = 0x6002;
	
	int CLOUD_START_SUCCESS = 0x6003;
	 
	int CLOUD_START_FAIL = 0x6004;
	
	int NOT_FOUND_STB = 0x6005;
	
	int QR_SCAN_FAIL = 0x2001;
	
	int GET_MAC_FAIL = 0X2002;
	
	/**
	 * 获取到手机MAC地址作为手机唯一标识
	 */
	int CPMA_ID_MAC = 0x1011;
	
	Map<String, Activity> ACTIVITY_MAP = new HashMap<String, Activity>();

	String HIDAPPSERVICE_URL = null;
	/**
	 * 声音是否开启
	 */
	String ISVOICE = "isvoice";
	/**
	 * 震动是否开启
	 */
	String ISSHAKE = "isshake";

	int TERMINAL_DISCONNECT = 0x5001;
	
	int STB_UNLINE = 0x3001;
	int NETWORK_DISCONNECT = 0x3002;
	int NETWORK_CONNECT = 0X3003;
}
