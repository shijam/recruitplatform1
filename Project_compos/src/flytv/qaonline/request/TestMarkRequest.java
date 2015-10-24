package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.http.HttpServer;

public class TestMarkRequest {
	private Context mContext;
	private static TestMarkRequest mLiveInfoRequest;

	public static TestMarkRequest testMark(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new TestMarkRequest(context);
		}
		return mLiveInfoRequest;
	}

	private TestMarkRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(String userId, int questionId, int testAnswerId, String content, String imagesIds,
			String audioIds, String videoIds) {
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_test_mark);
		requestUrlStr.append(methodUrl).append("?userId=" + userId)
				.append("&questionId=" + questionId)
				.append("&testAnswerId=" + testAnswerId)
				.append("&content=" + content)
				.append("&imagesIds=" + imagesIds)
				.append("&audioIds=" + audioIds)
				.append("&videoIds=" + videoIds);

		return requestUrlStr.toString();
	}

	public String[] markTestMark(String userId, int questionId, int testAnswerId,String content, String imagesIds,
			String audioIds, String videoIds) {
		String url = getRequestHttpUrl(userId, questionId, testAnswerId, 
				content,imagesIds,audioIds,videoIds);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[] { HttpServer.HTTPSTATE_SUCCESS, requestData };
	}
}
