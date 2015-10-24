package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class EndQuestionRequest {
	private Context mContext;
	private static EndQuestionRequest mLiveInfoRequest;

	public static EndQuestionRequest endQuestion(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new EndQuestionRequest(context);
		}
		return mLiveInfoRequest;
	}

	private EndQuestionRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(String id) {
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_end_question);
		requestUrlStr.append(methodUrl).append("?id=" +id);
		return requestUrlStr.toString();
	}

	public String[] endQuestionData(String id) {
		String url = getRequestHttpUrl(id);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[]{HttpServer.HTTPSTATE_SUCCESS,requestData};
	}
}
