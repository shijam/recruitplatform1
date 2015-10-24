package flytv.qaonline.request;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import flytv.qaonline.entity.QAItemEntity;
import flytv.qaonline.entity.QARequestEntity;
import flytv.qaonline.entity.QATeacherListEntity;
import flytv.qaonline.entity.ResultEntity;
import flytv.qaonline.entity.TeacherItemEntity;

public class TeachersParser {
	/**
	 * 解析列表数据 
	 */
	public static QATeacherListEntity parserRequestData(String jsonStr){
		if(jsonStr == null || "".equals(jsonStr)){
			return null;
		}
		try{
			QATeacherListEntity entity = new QATeacherListEntity();
			List<TeacherItemEntity> teacherItemEntitys = new ArrayList<TeacherItemEntity>();
			JSONObject jsonObj = new JSONObject(jsonStr);
			if(jsonObj.has("currentPage"))
				entity.setCurrentPage(jsonObj.getString("currentPage"));
			if(jsonObj.has("fileService"))
				entity.setFileService(jsonObj.getString("fileService"));
			if(jsonObj.has("message"))
				entity.setMessage(jsonObj.getString("message"));
			if(jsonObj.has("pageSize"))
				entity.setPageSize(jsonObj.getString("pageSize"));
			if(jsonObj.has("totalCount"))
				entity.setTotalCount(jsonObj.getString("totalCount"));
			if(jsonObj.has("totalPage"))
				entity.setTotalPage(jsonObj.getString("totalPage"));
			if (jsonObj.has("items")) {
				JSONArray resultsList = jsonObj.getJSONArray("items");
				for (int i = 0; i < resultsList.length(); i++) {
					TeacherItemEntity teacherEntity=new TeacherItemEntity();
					JSONObject jsonItem = resultsList.getJSONObject(i);
					if (jsonItem.has("noMarkNum")) {
						teacherEntity.setNoMarkNum(jsonItem.getString("noMarkNum"));
					}
					if (jsonItem.has("teacherDistrictName")) {
						teacherEntity.setTeacherDistrictName(jsonItem.getString("teacherDistrictName"));
					}
					if (jsonItem.has("teacherGmtCreateString")) {
						teacherEntity.setTeacherGmtCreateString(jsonItem.getString("teacherGmtCreateString"));
					}
					if (jsonItem.has("teacherPhoto")) {
						teacherEntity.setTeacherPhoto(jsonItem.getString("teacherPhoto"));
					}
					if (jsonItem.has("teacherRecruitId")) {
						teacherEntity.setTeacherRecruitId(jsonItem.getString("teacherRecruitId"));
					}
					if (jsonItem.has("teacherSchoolName")) {
						teacherEntity.setTeacherSchoolName(jsonItem.getString("teacherSchoolName"));
					}
					if (jsonItem.has("teacherUserId")) {
						teacherEntity.setTeacherUserId(jsonItem.getString("teacherUserId"));
					}
					if (jsonItem.has("teacherUserName")) {
						teacherEntity.setTeacherUserName(jsonItem.getString("teacherUserName"));
					}
					if (jsonItem.has("teacherLevelString")) {
						teacherEntity.setTeacherUserType(jsonItem.getString("teacherLevelString"));
					}
					teacherItemEntitys.add(teacherEntity);
				}
				entity.setItems(teacherItemEntitys);
			}
			return entity;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
 
}
