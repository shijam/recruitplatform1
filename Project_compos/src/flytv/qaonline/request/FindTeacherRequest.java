package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class FindTeacherRequest {
	private Context mContext;
	private static FindTeacherRequest mLiveInfoRequest;

	public static FindTeacherRequest submitFindTeacer(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new FindTeacherRequest(context);
		}
		return mLiveInfoRequest;
	}

	private FindTeacherRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(String userId,String teacherId,String questionId) {
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_find_teacher);
		requestUrlStr.append(methodUrl).append("?userId=" + userId)
				.append("&markUserId=" + teacherId)
				.append("&questionId=" + questionId);
		return requestUrlStr.toString();
	}

	public String[] submitFindTeacher(String userId,String teacherId,String questionId) {
		String url = getRequestHttpUrl(userId,teacherId,questionId);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[]{HttpServer.HTTPSTATE_SUCCESS,requestData};
	}
}
