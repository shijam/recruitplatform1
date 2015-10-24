package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class ShareQuestionRequest {
	private Context mContext;
	private static ShareQuestionRequest mLiveInfoRequest;

	public static ShareQuestionRequest sharaQuestion(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new ShareQuestionRequest(context);
		}
		return mLiveInfoRequest;
	}

	private ShareQuestionRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(String id) {
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_share_question);
		requestUrlStr.append(methodUrl).append("?id=" +id);
		return requestUrlStr.toString();
	}

	public String[] sharaQuestionData(String id) {
		String url = getRequestHttpUrl(id);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[]{HttpServer.HTTPSTATE_SUCCESS,requestData};
	}
}
