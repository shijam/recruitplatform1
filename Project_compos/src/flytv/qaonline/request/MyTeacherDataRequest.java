package flytv.qaonline.request;

import android.content.Context;
import flytv.compos.run.R;
import flytv.qaonline.http.HttpDbOperater;
import flytv.qaonline.http.HttpServer;

public class MyTeacherDataRequest {
	private Context mContext;
	private static MyTeacherDataRequest mLiveInfoRequest;

	public static MyTeacherDataRequest getMyTeacherRequest(Context context) {
		if (mLiveInfoRequest == null) {
			mLiveInfoRequest = new MyTeacherDataRequest(context);
		}
		return mLiveInfoRequest;
	}

	private MyTeacherDataRequest(Context context) {
		mContext = context;
	}

	private String getRequestHttpUrl(String liveId,
			String subjectname, int pageSize,int pageNo) {
		StringBuffer requestUrlStr = new StringBuffer();
		String mothodUrl = mContext.getString(R.string.app_host)
				+ mContext.getString(R.string.flytv_getteachers_list);
		requestUrlStr.append(mothodUrl).append("?userId=" + liveId)
				.append("&subjectName=" + subjectname)
				.append("&pageSize=" + pageSize)
				.append("&pageNo=" + pageNo);
		return requestUrlStr.toString();
	}

	public String[] getTeacherListData(final String userId, final int pageSize,final String subjectname,final int currentPage, final String search) {
		String url = getRequestHttpUrl(userId,subjectname,pageSize,currentPage);
		String requestData = HttpServer.getHttpServer(mContext)
				.requestByHttpGet(url, "");
		return new String[] { HttpServer.HTTPSTATE_SUCCESS, requestData };
	}
}
