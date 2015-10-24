package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class TestAnswerRequest {
	private Context mContext;
	private static TestAnswerRequest mLiveInfoRequest;

	public static TestAnswerRequest testAnswer(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new TestAnswerRequest(context);
		}
		return mLiveInfoRequest;
	}

	private TestAnswerRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(String userId, int questionId, int testQuestionId, String content, String imagesIds,
			String audioIds, String videoIds) {
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_test_answer);
		requestUrlStr.append(methodUrl).append("?userId=" + userId)
				.append("&questionId=" + questionId)
				.append("&testQuestionId=" + testQuestionId)
				.append("&content=" + content)
				.append("&imagesIds=" + imagesIds)
				.append("&audioIds=" + audioIds)
				.append("&videoIds=" + videoIds);

		return requestUrlStr.toString();
	}

	public String[] testAnswerData(String userId, int questionId, int testQuestionId,String content, String imagesIds,
			String audioIds, String videoIds) {
		String url = getRequestHttpUrl(userId, questionId, testQuestionId, 
				content,imagesIds,audioIds,videoIds);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[] { HttpServer.HTTPSTATE_SUCCESS, requestData };
	}
}
