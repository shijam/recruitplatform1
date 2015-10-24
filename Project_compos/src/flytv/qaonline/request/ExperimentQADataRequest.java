package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class ExperimentQADataRequest {
	private Context mContext;
	private static ExperimentQADataRequest mLiveInfoRequest;
	
	public static ExperimentQADataRequest getMyshareDataRequest(Context context){
		if(mLiveInfoRequest == null){
			mLiveInfoRequest = new ExperimentQADataRequest(context);
		}
		return mLiveInfoRequest;
	}
	
	private ExperimentQADataRequest(Context context){
		mContext = context;
	}
	
	private String getRequestHttpUrl(String liveId,int currentPage){
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl =mContext.getString(R.string.app_host)+mContext.getString(R.string.flytv_experiment_list);
		System.out.println(methodUrl);
		requestUrlStr.append(methodUrl).append("?userId="+liveId).append("&currentPage="+currentPage);
		return requestUrlStr.toString();
	}
	
	public String[] getShareListData(final String userId,final int pageSize,final int currentPage,final String search){
		String url = getRequestHttpUrl(userId,currentPage);
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
