package flytv.qaonline.request;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import flytv.qaonline.entity.QAItemEntity;
import flytv.qaonline.entity.QARequestEntity;
import flytv.qaonline.entity.ResultEntity;

public class ResultParser {
	/**
	 * 解析列表数据 
	 */
	public static ResultEntity parserRequestData(String jsonStr){
		if(jsonStr == null || "".equals(jsonStr)){
			return null;
		}
		try{
			ResultEntity qaRequestEntity = new ResultEntity();
			JSONObject jsonObj = new JSONObject(jsonStr);
			if(jsonObj.has("message"))
				qaRequestEntity.setMessage(jsonObj.getString("message"));
			if(jsonObj.has("msgInfo"))
				qaRequestEntity.setMsgInfo(jsonObj.getString("msgInfo"));
			if(jsonObj.has("questionId"))
				qaRequestEntity.setQuestioned(jsonObj.getString("questionId"));
			return qaRequestEntity;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
 
}
