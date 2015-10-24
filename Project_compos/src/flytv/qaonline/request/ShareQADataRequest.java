package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class ShareQADataRequest {
	private Context mContext;
	private static ShareQADataRequest mLiveInfoRequest;
	
	public static ShareQADataRequest getMyshareDataRequest(Context context){
		if(mLiveInfoRequest == null){
			mLiveInfoRequest = new ShareQADataRequest(context);
		}
		return mLiveInfoRequest;
	}
	
	private ShareQADataRequest(Context context){
		mContext = context;
	}
	
	private String getRequestHttpUrl(String liveId,int currentPage){
		StringBuffer requestUrlStr = new StringBuffer();
		String methodUrl =mContext.getString(R.string.app_host)+mContext.getString(R.string.flytv_share_list);
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
