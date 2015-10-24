package flytv.ext.network;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public abstract class BaseParser<T> {

	/**
	 * 
	 * @param res
	 * @throws JSONException
	 */
	public String checkResponse(String paramString) throws JSONException {

		String TAG = "BaseParser.checkResponse";
		Log.i(TAG, paramString);

		if (paramString == null) {
			return null;
		} else {
			JSONObject jsonObject = new JSONObject(paramString);
			int result = jsonObject.getInt("message");
			if (result == 1) {
				return paramString;
			} else if (result == 0) {
				return "用户名或密码错误";
			} else {
				return "页面错误";
			}

		}
	}

	public abstract T parseJSON(String paramString) throws JSONException;
}
