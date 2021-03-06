package com.example.richtest;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.richtest.bean.Result;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

public class TextAttachmentUpload {
	int editorId;
	MainActivity context;
	ArrayList<Object> array;
	int textMarkId;
	int location;
	String url = "http://edudemo.flytv.com.cn/user/commonPad/Composition/markEditor/saveMarkEditorExt2.do?";

	TextAttachmentUpload(MainActivity context, String textMarkId, int editorId,
			ArrayList<Object> array) {
		this.editorId = editorId;
		this.array = array;
		this.textMarkId = Integer.valueOf(textMarkId);
		this.context = context;
		location = 0;
	}

	public void start() {
		if (array.size() < 1) {
			context.textFinish();
			return;
		}
		RequestParams paramsUtil = new RequestParams();
		if (array.size() > 0) {
			PointEntity pointentity = (PointEntity) array.get(location);
			JSONObject jsonObject = new JSONObject();
			jsonEditorExt jsonEditorExt = new jsonEditorExt();

			jsonEditorExt.markEditId = editorId;
			jsonEditorExt.pointId = pointentity.pointID;
			boolean isCont= pointentity.mp3EditName.equals(pointentity.mp3Name);
			boolean isContNull= !pointentity.mp3EditName.equals("");
			if (isCont&&isContNull) {
				String ipath = pointentity.mp3Name;
				File file = new File(ipath);
				paramsUtil.addBodyParameter(file.getName(), file);
				paramsUtil.addBodyParameter("fileName", file.getName());
				Log.i("test_tag", file.getPath() + " | " + file.exists()
						+ " | " + file.getName() + "|url = " + url);
			}else{
				// 这人采用mp3相同的设计 来使用
				boolean isTextCont= pointentity.textEditContent.equals(pointentity.textContent);
				boolean isTextContNull= !pointentity.textEditContent.equals("");
				if (isTextCont&&isTextContNull) {
					jsonEditorExt.note = pointentity.textContent;
				}else{
					if (location+1 < array.size()) {
						location++;
						TextAttachmentUpload.this.start();
						
					} else {
						context.textFinish();
					}
					return;
				}
			}
			if (pointentity.commentRange.location > 0) {
				jsonEditorExt.contentRangeStart = pointentity.commentRange.location;
				jsonEditorExt.contentRangeEnd = pointentity.commentRange.location
						+ pointentity.commentRange.length;
			}
			if (pointentity.point != null) {
				jsonEditorExt.pointX = pointentity.point.x;
				jsonEditorExt.pointY = pointentity.point.y;
			}
			
			String jsoneditor = jsonObject.toJSONString(jsonEditorExt);
			paramsUtil.addBodyParameter("jsonEditorExt", jsoneditor);
			Log.i("test_tag", "submit =  userId = " + MainActivity.userId + " |id = "
					+ textMarkId + "|jsonEditorExt" + jsoneditor);

		}
		paramsUtil.addBodyParameter("userId", MainActivity.userId);
		paramsUtil.addBodyParameter("markEditId",editorId+"");
		paramsUtil.addBodyParameter("id", textMarkId + "");
		Log.i("test_tag","Text  markEditId = "+editorId+" || id" +"= "+textMarkId);
		Log.i("test_tag","Text  url = "+url);
		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, paramsUtil,
				new RequestCallBack<String>() {

					@Override
					public void onStart() {

					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.i("test_tag","textAttach responseInfo.result="+responseInfo.result);
						Result result = new Gson().fromJson(
								responseInfo.result, Result.class);
						if (result.message == 1) {
							for (int i = 0; i < array.size(); i++) {
								PointEntity pointentity = (PointEntity) array.get(i);
								pointentity.mp3EditName="";
								pointentity.textEditContent ="";	
							}
							Log.i("test_tag","array="+array.size());
							if (location < array.size()) {
								TextAttachmentUpload.this.start();
							} else {
								context.textFinish();
							}
						}

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Log.i("test_tag","textAttach error"+msg);
					}
				});

		location++;
	}

	class jsonEditorExt implements Serializable {

		private static final long serialVersionUID = 1L;
		public int markEditId;
		public int pointId;
		public int pointX;
		public int pointY;
		public int contentRangeStart;
		public int contentRangeEnd;
		public String note;
	}

}
