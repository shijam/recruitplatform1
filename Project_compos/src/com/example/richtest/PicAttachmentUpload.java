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

public class PicAttachmentUpload {
	int editorId;
	MainActivity context;
	ArrayList<PointEntity> array;
	String textMarkId;
	String id;
	int location;
	String url = "http://edudemo.flytv.com.cn/user/commonPad/Composition/markEditor/saveMarkEditorExt2.do?";

	PicAttachmentUpload(MainActivity context, String textMarkId, String id,
			int editorId, ArrayList<PointEntity> array) {
		this.editorId = editorId;
		this.array = array;
		this.id = id;
		this.textMarkId = textMarkId;
		this.context = context;
		location = 0;
	}

	public void start() {
		PointEntity pointentity = (PointEntity) array.get(location);
		RequestParams paramsUtil = new RequestParams();
		JSONObject jsonObject = new JSONObject();
		jsonEditorExt jsonEditorExt = new jsonEditorExt();
		// test
		jsonEditorExt.markEditId = String.valueOf(editorId);
		jsonEditorExt.pointId = String.valueOf(pointentity.pointID);
		if (pointentity.commentRange != null
				&& pointentity.commentRange.location > 0) {
			jsonEditorExt.contentRangeStart = pointentity.commentRange.location;
			jsonEditorExt.contentRangeStart = pointentity.commentRange.location
					+ pointentity.commentRange.length;
		}
		if (pointentity.point != null) {
			jsonEditorExt.pointX = String.valueOf(pointentity.point.x);
			jsonEditorExt.pointY = String.valueOf(pointentity.point.y);
		}
		if (pointentity.textContent != null) {
			jsonEditorExt.note = pointentity.textContent;
		}
		if (pointentity.mp3Name != null) {
			String ipath = pointentity.mp3Name;
			File file = new File(ipath);
			paramsUtil.addBodyParameter(file.getName(), file);
			paramsUtil.addBodyParameter("fileName", file.getName());
			Log.i("test_tag", file.getPath() + " | " + file.exists() + " | "
					+ file.getName() + "|url = " + url);
		}
		String jsoneditor = jsonObject.toJSONString(jsonEditorExt);
		paramsUtil.addBodyParameter("jsonEditorExt", jsoneditor);
		paramsUtil.addBodyParameter("userId", MainActivity.userId);
		paramsUtil.addBodyParameter("markEditId",editorId+"");
		paramsUtil.addBodyParameter("id", textMarkId + "");
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
						Result result = new Gson().fromJson(
								responseInfo.result, Result.class);
						if (result.message == 1) {
							if (location < array.size()) {
								PicAttachmentUpload.this.start();
							} else {
								context.picFinish();
								System.out.println("成功");
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						
					}
				});
		location++;
	}

	class jsonEditorExt implements Serializable {

		private static final long serialVersionUID = 1L;
		public String markEditId;
		public String pointId;
		public String pointX;
		public String pointY;
		public String note;
		public int contentRangeStart;
		public int contentRangeEnd;

	}
}
