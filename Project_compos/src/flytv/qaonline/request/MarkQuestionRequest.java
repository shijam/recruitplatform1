package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class MarkQuestionRequest {
	private Context mContext;
	private static MarkQuestionRequest mLiveInfoRequest;

	public static MarkQuestionRequest markQuestion(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new MarkQuestionRequest(context);
		}
		return mLiveInfoRequest;
	}

	private MarkQuestionRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(String userId, int questionId, int markId,
			String score, String note, String type, String imagesIds,
			String voiceIds, String videoIds, String postilIds, int submitStatus) {
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_mark);
		requestUrlStr.append(methodUrl).append("?userId=" + userId)
				.append("&questionId=" + questionId)
				.append("&markId=" + markId).append("&score=" + score)
				.append("&content=" + note).append("&type=" + type)
				.append("&imagesIds=" + imagesIds)
				.append("&voiceIds=" + voiceIds)
				.append("&videoIds=" + videoIds)
				.append("&postilIds=" + postilIds)
				.append("&submitStatus=" + submitStatus);

		return requestUrlStr.toString();
	}

	public String[] markQuestionData(String userId, int questionId, int markId,
			String score, String note, String type, String imagesIds,
			String voiceIds, String videoIds, String postilIds, int submitStatus) {
		String url = getRequestHttpUrl(userId, questionId, markId, score,
				note, type,imagesIds,voiceIds,videoIds,postilIds,submitStatus);
		System.out.println("批阅地址为-------"+url);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[] { HttpServer.HTTPSTATE_SUCCESS, requestData };
	}
}
