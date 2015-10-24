package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class AutoTeacherRequest {
	private Context mContext;
	private static AutoTeacherRequest mLiveInfoRequest;

	public static AutoTeacherRequest autoFindTeacer(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new AutoTeacherRequest(context);
		}
		return mLiveInfoRequest;
	}

	private AutoTeacherRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(String userId,String questionId) {
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_auto_teacher);
		requestUrlStr.append(methodUrl).append("?userId=" + userId)
				.append("&questionId=" + questionId);
		return requestUrlStr.toString();
	}

	public String[] autoFindTeacher(String userId,String questionId) {
		String url = getRequestHttpUrl(userId,questionId);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[]{HttpServer.HTTPSTATE_SUCCESS,requestData};
	}
}
