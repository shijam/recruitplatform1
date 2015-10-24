package flytv.qaonline.request;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import flytv.qaonline.entity.MyFileEntity;
import flytv.qaonline.entity.QAItemEntity;
import flytv.qaonline.entity.QARequestEntity;
import flytv.qaonline.entity.VedioEntity;
import flytv.qaonline.entity.VedioItemEntity;

public class VedioListDataParser {
	/**
	 * 解析列表数据
	 */
	public static VedioEntity parserRequestData(String jsonStr) {
		if (jsonStr == null || "".equals(jsonStr)) {
			return null;
		}
		try {
			VedioEntity entity=new VedioEntity();
			JSONObject jsonObj = new JSONObject(jsonStr);
			if (jsonObj.has("currentPage"))
				entity.setCurrentPage(jsonObj.getString("currentPage"));
			if (jsonObj.has("fileService"))
				entity.setFileService(jsonObj
						.getString("fileService"));
			if (jsonObj.has("message"))
				entity.setMessage(jsonObj
						.getString("message"));
			if (jsonObj.has("pageSize"))
				entity.setPageSize(jsonObj
						.getString("pageSize"));
			if (jsonObj.has("totalCount"))
				entity.setTotalCount(jsonObj
						.getString("totalCount"));
			if (jsonObj.has("totalPage"))
				entity.setTotalPage(jsonObj
						.getString("totalPage"));
			if (jsonObj.has("webService"))
				entity.setWebService(jsonObj
						.getString("webService"));
			List<VedioItemEntity> items = new ArrayList<VedioItemEntity>();
			if (jsonObj.has("items")) {
				JSONArray resultsListdatas = jsonObj.getJSONArray("items");
				for (int i = 0; i < resultsListdatas.length(); i++) {
					VedioItemEntity qaRequestEntity = new VedioItemEntity();
					JSONObject jsonItem1 = resultsListdatas.getJSONObject(i);
					if (jsonItem1.has("content"))
						qaRequestEntity
								.setContent(jsonItem1.getString("content"));
					if (jsonItem1.has("createTimeStr"))
						qaRequestEntity.setCreateTimeStr(jsonItem1
								.getString("createTimeStr"));
					if (jsonItem1.has("gradeId"))
						qaRequestEntity
								.setGradeId(jsonItem1.getString("gradeId"));
					if (jsonItem1.has("gradeName"))
						qaRequestEntity.setGradeName(jsonItem1
								.getString("gradeName"));
					if (jsonItem1.has("id"))
						qaRequestEntity.setId(jsonItem1.getString("id"));
					if (jsonItem1.has("isDelete"))
						qaRequestEntity.setIsDelete(jsonItem1
								.getString("isDelete"));
					if (jsonItem1.has("sectionId"))
						qaRequestEntity.setSectionId(jsonItem1
								.getString("sectionId"));
					if (jsonItem1.has("subjectId"))
						qaRequestEntity.setSubjectId(jsonItem1
								.getString("subjectId"));
					if (jsonItem1.has("sectionName"))
						qaRequestEntity.setSectionName(jsonItem1
								.getString("sectionName"));
					if (jsonItem1.has("subjectName"))
						qaRequestEntity.setSubjectName(jsonItem1
								.getString("subjectName"));
					if (jsonItem1.has("title"))
						qaRequestEntity.setTitle(jsonItem1.getString("title"));
					if (jsonItem1.has("userId"))
						qaRequestEntity.setUserId(jsonItem1.getString("userId"));
					if (jsonItem1.has("userName"))
						qaRequestEntity.setUserName(jsonItem1
								.getString("userName"));
					if (jsonItem1.has("userType"))
						qaRequestEntity.setUserType(jsonItem1
								.getString("userType"));
					if (jsonItem1.has("fileList")) {
						JSONArray resultsList = jsonItem1.getJSONArray("fileList");
						List<MyFileEntity> qaitemEntitys = new ArrayList<MyFileEntity>();
						for (int j = 0; j < resultsList.length(); j++) {
							JSONObject jsonItem = resultsList.getJSONObject(j);
							MyFileEntity itemEntity = new MyFileEntity();
							if (jsonItem.has("extType"))
								itemEntity.setExtType(jsonItem
										.getString("extType"));
							if (jsonItem.has("thumbPath"))
								itemEntity.setThumbPath(jsonItem
										.getString("thumbPath"));
							if (jsonItem.has("busType"))
								itemEntity.setBusType(jsonItem
										.getString("busType"));
							if (jsonItem.has("name"))
								itemEntity.setName(jsonItem.getString("name"));
							if (jsonItem.has("newName"))
								itemEntity.setNewName(jsonItem
										.getString("newName"));
							if (jsonItem.has("fileUrl"))
								itemEntity.setFileUrl(jsonItem
										.getString("fileUrl"));
							qaitemEntitys.add(itemEntity);
						}
						qaRequestEntity.setFilelist(qaitemEntitys);
					}
					items.add(qaRequestEntity);
				}
				entity.setItems(items);
			}

			return entity;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

}
