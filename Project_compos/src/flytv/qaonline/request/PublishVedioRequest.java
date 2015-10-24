package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class PublishVedioRequest {
	private Context mContext;
	private static PublishVedioRequest mLiveInfoRequest;

	public static PublishVedioRequest submitExperiment(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new PublishVedioRequest(context);
		}
		return mLiveInfoRequest;
	}

	private PublishVedioRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(String userId,String title,String subjectName,String sectionName,String gradeName,String fileIds) {
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_submit_experiment);
		requestUrlStr.append(methodUrl).append("?userId=" +userId)
				.append("&title=" + title)
				.append("&subjectName=" + subjectName)
				.append("&sectionName=" +sectionName)
				.append("&gradeName=" +gradeName)
				.append("&fileIds=" + fileIds);
		return requestUrlStr.toString();
	}

	public String[] submitExperimentData(String userId,String title,String subjectName,String sectionName,String  gradeName,String fileIds) {
		String url = getRequestHttpUrl(userId,title,subjectName,sectionName,gradeName,fileIds);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[]{HttpServer.HTTPSTATE_SUCCESS,requestData};
	}
}
