package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class SetScoreRequest {
	private Context mContext;
	private static SetScoreRequest mLiveInfoRequest;

	public static SetScoreRequest setScore(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new SetScoreRequest(context);
		}
		return mLiveInfoRequest;
	}

	private SetScoreRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(int id,int score) {
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_setscore_question);
		requestUrlStr.append(methodUrl).append("?id=" +id).append("&score=" +score);
		return requestUrlStr.toString();
	}

	public String[] setScoreData(int id,int score) {
		String url = getRequestHttpUrl(id,score);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[]{HttpServer.HTTPSTATE_SUCCESS,requestData};
	}
}
