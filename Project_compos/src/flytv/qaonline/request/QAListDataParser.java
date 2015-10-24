package flytv.qaonline.request;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import flytv.qaonline.entity.QAItemEntity;
import flytv.qaonline.entity.QARequestEntity;

public class QAListDataParser {
	/**
	 * 解析列表数据 
	 */
	public static QARequestEntity parserRequestData(String jsonStr){
		if(jsonStr == null || "".equals(jsonStr)){
			return null;
		}
		try{
			QARequestEntity qaRequestEntity = new QARequestEntity();
			List<QAItemEntity> qaitemEntitys = new ArrayList<QAItemEntity>();
			JSONObject jsonObj = new JSONObject(jsonStr);
			if(jsonObj.has("currentPage"))
				qaRequestEntity.setCurrentPage(jsonObj.getInt("currentPage"));
			if(jsonObj.has("fileService"))
				qaRequestEntity.setFileService(jsonObj.getString("fileService"));
			if(jsonObj.has("message"))
				qaRequestEntity.setMessage(jsonObj.getString("message"));
			if(jsonObj.has("obj"))
				qaRequestEntity.setMessage(jsonObj.getString("obj"));
			if(jsonObj.has("pageSize"))
				qaRequestEntity.setPageSize(jsonObj.getInt("pageSize"));
			if(jsonObj.has("totalCount"))
				qaRequestEntity.setTotalCount(jsonObj.getInt("totalCount"));
			if(jsonObj.has("totalPage"))
				qaRequestEntity.setTotalPage(jsonObj.getInt("totalPage"));
			if(jsonObj.has("webService"))
				qaRequestEntity.setWebService(jsonObj.getString("webService"));
			if(jsonObj.has("items")){
				JSONArray resultsList = jsonObj.getJSONArray("items");
				for(int i = 0;i < resultsList.length();i++){
					JSONObject jsonItem = resultsList.getJSONObject(i);
					QAItemEntity itemEntity = new QAItemEntity();
					if(jsonItem.has("questionGmtCreateString"))
						itemEntity.setQuestionGmtCreateString(jsonItem.getString("questionGmtCreateString"));
					if(jsonItem.has("questionId"))
						itemEntity.setQuestionId(jsonItem.getString("questionId"));
					if(jsonItem.has("questionTitle"))
						itemEntity.setQuestionTitle(jsonItem.getString("questionTitle"));
					if(jsonItem.has("questionUserId"))
						itemEntity.setQuestionUserId(jsonItem.getString("questionUserId"));
					if(jsonItem.has("questionUserName"))
						itemEntity.setQuestionUserName(jsonItem.getString("questionUserName"));
					if(jsonItem.has("questionSchoolName"))
						itemEntity.setQuestionSchoolName(jsonItem.getString("questionSchoolName"));
					if(jsonItem.has("questionUserType"))
						itemEntity.setQuestionUserType(jsonItem.getString("questionUserType"));
					if(jsonItem.has("markStatus"))
						itemEntity.setMarkStatus(jsonItem.getString("markStatus"));
					if (jsonItem.has("filePath")) 
						itemEntity.setFilePath(jsonItem.getString("filePath"));
					if (jsonItem.has("questionBclassName")) 
						itemEntity.setQuestionBclassName(jsonItem.getString("questionBclassName"));
					if (jsonItem.has("questionKeywords")) 
						itemEntity.setQuestionKeywords(jsonItem.getString("questionKeywords"));
					if (jsonItem.has("questionSubjectName")) 
						itemEntity.setQuestionSubjectName(jsonItem.getString("questionSubjectName"));
					if (jsonItem.has("questionTeacherName")) 
						itemEntity.setQuestionTeacherName(jsonItem.getString("questionTeacherName"));
					qaitemEntitys.add(itemEntity);
				}
				qaRequestEntity.setItems(qaitemEntitys);
			}
			return qaRequestEntity;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
 
}
