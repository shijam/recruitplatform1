package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class NewQuestionRequest {
	private Context mContext;
	private static NewQuestionRequest mLiveInfoRequest;

	public static NewQuestionRequest submitNewQuestion(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new NewQuestionRequest(context);
		}
		return mLiveInfoRequest;
	}

	private NewQuestionRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(NewQuestionItemEntity entity) {
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_submit_question);
		String imagesIds="";
		if (!"".equals(entity.getImagesIds())) {
			imagesIds = entity.getImagesIds().substring(0,entity.getImagesIds().length()-1);
		}
		
		requestUrlStr.append(methodUrl).append("?userId=" + entity.getUserId())
				.append("&id=" + entity.getId())
				.append("&subjectName=" + entity.getSubjectName())
				.append("&replyQuestionId=" + Integer.parseInt(entity.getReplyQuestionId()))
				.append("&replyMarkId=" + Integer.parseInt(entity.getReplyMarkId()))
				.append("&title=" + entity.getTitle())
				.append("&content=" + entity.getContent())
				.append("&keywords=" + entity.getKeywords())
				.append("&imagesIds=" + imagesIds)
				.append("&type=" + entity.getType())
				.append("&submitStatus=" + entity.getSubmitStatus());
		return requestUrlStr.toString();
	}

	public String[] getShareListData(NewQuestionItemEntity entity) {
		String url = getRequestHttpUrl(entity);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[]{HttpServer.HTTPSTATE_SUCCESS,requestData};
	}
}
