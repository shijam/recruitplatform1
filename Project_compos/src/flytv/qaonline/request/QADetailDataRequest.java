package flytv.qaonline.request;

import android.content.Context;
import android.util.Log;
import flytv.compos.run.R;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class QADetailDataRequest {
	private Context mContext;
	private static QADetailDataRequest mQADetailRequest;
	
	public static QADetailDataRequest getQaDetailDataRequest(Context context){
		if(mQADetailRequest == null){
			mQADetailRequest = new QADetailDataRequest(context);
		}
		return mQADetailRequest;
	}
	
	private QADetailDataRequest(Context context){
		mContext = context;
	}
	
	private String getRequestHttpUrl(String userId,String questionId){
		StringBuffer requestUrlStr = new StringBuffer();
		String mothodUrl =mContext.getString(R.string.app_host)+mContext.getString(R.string.flytv_question_detail);
		System.out.println(requestUrlStr);
		requestUrlStr.append(mothodUrl).append("?userId="+userId).append("&questionId="+questionId);
		return requestUrlStr.toString();
	}
	
	public String[] getQADetailData(final String userId,final String questionId){
		String url = getRequestHttpUrl(userId,questionId);
		String requestData = HttpServer.getHttpServer(mContext).requestByHttpGet(url,"");
		if(requestData != null 
				&& !"".equals(requestData)
				&& !HttpServer.HTTPSTATE_NONET.equals(requestData)
				&& !HttpServer.HTTPSTATE_ERROR.equals(requestData)
				&& !HttpServer.URLNULL_ERROR.equals(requestData)
				&& !HttpServer.HTTPSTATE_TIMEOUT.equals(requestData)){
			HttpDbOperater.getDbOperater(mContext).insertHttpData(url, requestData, true);
			return new String[]{HttpServer.HTTPSTATE_SUCCESS,requestData};
		}else{
			String oldData = HttpDbOperater.getDbOperater(mContext).getHttpData(url);
			return new String[]{requestData,oldData};
		}
	}
}
