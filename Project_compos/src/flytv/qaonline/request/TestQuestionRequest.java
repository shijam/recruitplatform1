package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class TestQuestionRequest {
	private Context mContext;
	private static TestQuestionRequest mLiveInfoRequest;

	public static TestQuestionRequest testQuestion(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new TestQuestionRequest(context);
		}
		return mLiveInfoRequest;
	}

	private TestQuestionRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(String userId,String content,String questionId,String imagesIds,String audioIds,String videoIds) {
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_submitTest_question);
		requestUrlStr.append(methodUrl).append("?userId=" +userId)
				.append("&content=" + content)
				.append("&questionId=" +questionId)
				.append("&imagesIds=" +imagesIds)
				.append("&audioIds=" +audioIds)
				.append("&videoIds=" +videoIds);
		return requestUrlStr.toString();
	}

	public String[] testQuestionData(String userId,String content,String questionId,String imagesIds,String audioIds,String videoIds) {
		String url = getRequestHttpUrl(userId,content,questionId,imagesIds,audioIds,videoIds);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[]{HttpServer.HTTPSTATE_SUCCESS,requestData};
	}
}
