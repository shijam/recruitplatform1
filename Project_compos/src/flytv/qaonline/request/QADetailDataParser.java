package flytv.qaonline.request;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

import flytv.qaonline.entity.OnLineMarkBean;
import flytv.qaonline.entity.QADetailBean;
import flytv.qaonline.entity.QADetailItemEntity;
import flytv.qaonline.entity.TestMarkBean;
import flytv.qaonline.entity.TestQuestAswerBean;
import flytv.qaonline.entity.TestQuestBean;

public class QADetailDataParser {
	/**
	 * 解析列表数据 
	 */
	public static List<QADetailItemEntity> parserRequestData(String jsonStr){
		if(jsonStr == null || "".equals(jsonStr)){
			return null;
		}
		QADetailBean qaDetailBean = JSON.parseObject(jsonStr,QADetailBean.class);
		List<QADetailItemEntity> qaDetailDatas = new ArrayList<QADetailItemEntity>();
		if(qaDetailBean != null){
			QADetailItemEntity titleRequestEntity = new QADetailItemEntity();
			titleRequestEntity.setStatus(qaDetailBean.getStatus());
			titleRequestEntity.setContent(qaDetailBean.getContent());
			titleRequestEntity.setTitle("请老师解答");
			titleRequestEntity.setScore(qaDetailBean.getScore());
			titleRequestEntity.setId(qaDetailBean.getId());
			titleRequestEntity.setQuestionId(qaDetailBean.getId());
			titleRequestEntity.setKey(0);
			titleRequestEntity.setShareStatus(qaDetailBean.getShareStatus());
			titleRequestEntity.setFileList(qaDetailBean.fileList);
			titleRequestEntity.setMarkStatus(qaDetailBean.getMarkStatus());
			qaDetailDatas.add(titleRequestEntity);
			if(qaDetailBean.getOnlineMarkList() != null && !qaDetailBean.getOnlineMarkList().isEmpty()){
				for(OnLineMarkBean onLineMarkBean : qaDetailBean.getOnlineMarkList()){
					QADetailItemEntity onlineMarkEntiy = new QADetailItemEntity();
					onlineMarkEntiy.setStatus(onLineMarkBean.getStatus());
					onlineMarkEntiy.setContent(onLineMarkBean.getContent());
					onlineMarkEntiy.setTitle("解答内容");
					onlineMarkEntiy.setKey(1);
					onlineMarkEntiy.setQuestionId(onLineMarkBean.getQuestionId());
					onlineMarkEntiy.setId(onLineMarkBean.getId());
					onlineMarkEntiy.setFileList(onLineMarkBean.markFileList);
					qaDetailDatas.add(onlineMarkEntiy);
				}
			}
			if(qaDetailBean.getReplyQuestionList() != null && !qaDetailBean.getReplyQuestionList().isEmpty()){
				for(QADetailBean reQADetailBean : qaDetailBean.getReplyQuestionList()){
					QADetailItemEntity replyQAEntity = new QADetailItemEntity();
					replyQAEntity.setStatus(reQADetailBean.getStatus());
					replyQAEntity.setContent(reQADetailBean.getContent());
					replyQAEntity.setTitle("学生追问");
					replyQAEntity.setKey(2);
					replyQAEntity.setQuestionId(reQADetailBean.getId());
					replyQAEntity.setId(reQADetailBean.getId());
					replyQAEntity.setFileList(reQADetailBean.fileList);
					qaDetailDatas.add(replyQAEntity);
					for(OnLineMarkBean onLineMarkBean : reQADetailBean.getOnlineMarkList()){
						QADetailItemEntity onlineMarkEntiy = new QADetailItemEntity();
						onlineMarkEntiy.setStatus(onLineMarkBean.getStatus());
						onlineMarkEntiy.setContent(onLineMarkBean.getContent());
						onlineMarkEntiy.setTitle("解答内容");
						onlineMarkEntiy.setKey(3);
						onlineMarkEntiy.setId(onLineMarkBean.getId());
						onlineMarkEntiy.setQuestionId(onLineMarkBean.getQuestionId());
						onlineMarkEntiy.setFileList(onLineMarkBean.markFileList);
						qaDetailDatas.add(onlineMarkEntiy);
					}
				}
			}
	
			if(qaDetailBean.getTestQuestionList() != null && !qaDetailBean.getTestQuestionList().isEmpty()){
				for(TestQuestBean testQuestBean : qaDetailBean.getTestQuestionList()){
					QADetailItemEntity testQAEntity = new QADetailItemEntity();
					testQAEntity.setContent(testQuestBean.getContent());
					testQAEntity.setTitle("考一考");
					testQAEntity.setKey(4);
					testQAEntity.setFileList(testQuestBean.fileList);
					testQAEntity.setId(testQuestBean.getId());
					testQAEntity.setQuestionId(testQuestBean.getQuestionId());
					qaDetailDatas.add(testQAEntity);
					for(TestQuestAswerBean testQuestAswerBean : testQuestBean.getTestAnswerList()){
						QADetailItemEntity onlineMarkEntiy = new QADetailItemEntity();
						onlineMarkEntiy.setContent(testQuestAswerBean.getContent());
						onlineMarkEntiy.setTitle("学生回答");
						onlineMarkEntiy.setKey(5);
						onlineMarkEntiy.setId(testQuestAswerBean.getId());
						onlineMarkEntiy.setQuestionId(testQuestAswerBean.getQuestionId());
						onlineMarkEntiy.setFileList(testQuestAswerBean.getFileList());
						qaDetailDatas.add(onlineMarkEntiy);
						for(TestMarkBean testOnLineMarkBean : testQuestAswerBean.getTestMarkList()){
							QADetailItemEntity testOnlineMarkEntiy = new QADetailItemEntity();
							testOnlineMarkEntiy.setContent(testOnLineMarkBean.getContent());
							testOnlineMarkEntiy.setTitle("解答内容");
							testOnlineMarkEntiy.setKey(7);
							testOnlineMarkEntiy.setId(testOnLineMarkBean.getTestAnswerId());
							testOnlineMarkEntiy.setQuestionId(testOnLineMarkBean.getQuestionId());
							testOnlineMarkEntiy.setFileList(testOnLineMarkBean.getMarkFileList());
							qaDetailDatas.add(testOnlineMarkEntiy);
						}
					}
				}
			}
		}
		return qaDetailDatas;
	}
 
//	private static void parserData(String title,List<QADetailItemEntity> content,String jsonStr){
//		try {
//			JSONObject jsonObject = new JSONObject(jsonStr);
//			QADetailItemEntity itemEntity = new QADetailItemEntity();
//			content.add(itemEntity);
//			itemEntity.title = title;
//			if(jsonObject.has("status"))
//				itemEntity.status = jsonObject.getInt("status");
//			if(jsonObject.has("content"))
//				itemEntity.content = jsonObject.getString("content");
//			if(jsonObject.has("fileList")){
//				JSONArray item = jsonObject.getJSONArray("fileList");
//				List<FileIetmEntity> titleList = new ArrayList<QADetailEntity.FileIetmEntity>();
//				for(int i = 0;i < item.length();i++){
//					FileIetmEntity fileIetmEntity = new FileIetmEntity();
//					JSONObject jsonItem = item.getJSONObject(i);
//					if(jsonItem.has("fileId"))
//						fileIetmEntity.fileId = jsonItem.getInt("fileId");
//					if(jsonItem.has("extType"))
//						fileIetmEntity.extType = jsonItem.getString("extType");
//					if(jsonItem.has("thumbPath"))
//						fileIetmEntity.thumbPath = jsonItem.getString("thumbPath");
////					if(jsonItem.has("busType"))
////						fileIetmEntity.busType = jsonItem.getString("busType");
//					if(jsonItem.has("name"))
//						fileIetmEntity.name = jsonItem.getString("name");
////					if(jsonItem.has("newName"))
////						fileIetmEntity.newName = jsonItem.getString("newName");
//					if(jsonItem.has("fileUrl"))
//						fileIetmEntity.fileUrl = jsonItem.getString("fileUrl");
//					titleList.add(fileIetmEntity);
//				}
//				itemEntity.fileList = titleList;
//			}
//			if(jsonObject.has("onlineMarkList")){
//				JSONArray itemArr = jsonObject.getJSONArray("onlineMarkList");
//				for(int i = 0;i < itemArr.length();i++){
//					JSONObject jsonItem = itemArr.getJSONObject(i);
//					QADetailItemEntity itemMarkEntity = new QADetailItemEntity();
//					itemMarkEntity.title = "解答内容";
//					content.add(itemMarkEntity);
//					if(jsonItem.has("content"))
//						itemMarkEntity.content = jsonItem.getString("content");
//					if(jsonItem.has("markFileList")){
//						List<FileIetmEntity> fileList = new ArrayList<QADetailEntity.FileIetmEntity>();
//						JSONArray item = jsonItem.getJSONArray("markFileList");
//						for(int j = 0;j < item.length();j++){
//							FileIetmEntity fileIetmEntity = new FileIetmEntity();
//							JSONObject jsonItemObj= item.getJSONObject(j);
//							if(jsonItemObj.has("fileId"))
//								fileIetmEntity.fileId = jsonItemObj.getInt("fileId");
//							if(jsonItemObj.has("extType"))
//								fileIetmEntity.extType = jsonItemObj.getString("extType");
//							if(jsonItemObj.has("thumbPath"))
//								fileIetmEntity.thumbPath = jsonItemObj.getString("thumbPath");
////							if(jsonItemObj.has("busType"))
////								fileIetmEntity.busType = jsonItemObj.getString("busType");
//							if(jsonItemObj.has("name"))
//								fileIetmEntity.name = jsonItemObj.getString("name");
////							if(jsonItemObj.has("newName"))
////								fileIetmEntity.newName = jsonItemObj.getString("newName");
//							if(jsonItemObj.has("fileUrl"))
//								fileIetmEntity.fileUrl = jsonItemObj.getString("fileUrl");
//							fileList.add(fileIetmEntity);
//						}
//						itemMarkEntity.fileList = fileList;
//					}
//				}
//			}
//
//			if(jsonObject.has("replyQuestionList")){
//				JSONArray itemArr1 = jsonObject.getJSONArray("replyQuestionList");
//				for(int i = 0;i < itemArr1.length();i++){
//					JSONObject jsonItem1 = itemArr1.getJSONObject(i);
//					QADetailItemEntity itemMarkEntity = new QADetailItemEntity();
//					itemMarkEntity.title = "学生追问";
//					content.add(itemMarkEntity);
//					if(jsonItem1.has("content"))
//						itemMarkEntity.content = jsonItem1.getString("content");
//					if(jsonItem1.has("fileList")){
//						List<FileIetmEntity> fileList = new ArrayList<QADetailEntity.FileIetmEntity>();
//						JSONArray item = jsonItem1.getJSONArray("fileList");
//						for(int j = 0;j < item.length();j++){
//							FileIetmEntity fileIetmEntity = new FileIetmEntity();
//							JSONObject jsonItemObj= item.getJSONObject(j);
//							if(jsonItemObj.has("fileId"))
//								fileIetmEntity.fileId = jsonItemObj.getInt("fileId");
//							if(jsonItemObj.has("extType"))
//								fileIetmEntity.extType = jsonItemObj.getString("extType");
//							if(jsonItemObj.has("thumbPath"))
//								fileIetmEntity.thumbPath = jsonItemObj.getString("thumbPath");
////							if(jsonItemObj.has("busType"))
////								fileIetmEntity.busType = jsonItemObj.getString("busType");
//							if(jsonItemObj.has("name"))
//								fileIetmEntity.name = jsonItemObj.getString("name");
////							if(jsonItemObj.has("newName"))
////								fileIetmEntity.newName = jsonItemObj.getString("newName");
//							if(jsonItemObj.has("fileUrl"))
//								fileIetmEntity.fileUrl = jsonItemObj.getString("fileUrl");
//							fileList.add(fileIetmEntity);
//						}
//						itemMarkEntity.fileList = fileList;
//					}
//					if(jsonItem1.has("onlineMarkList")){	
//						JSONArray item = jsonItem1.getJSONArray("onlineMarkList");
//						for(int j = 0;j < item.length();j++){
//							JSONObject jsonItemObj1= item.getJSONObject(j);
//							QADetailItemEntity itemMarkEntity2 = new QADetailItemEntity();
//							List<FileIetmEntity> fileList = new ArrayList<QADetailEntity.FileIetmEntity>();
//							content.add(itemMarkEntity2);
//							itemMarkEntity2.title = "解答内容";
//							itemMarkEntity2.fileList = fileList;
//							itemMarkEntity2.content = jsonItemObj1.getString("content");
//							if(jsonItemObj1.has("markFileList")){
//								JSONArray jsonArray2 = jsonItemObj1.getJSONArray("markFileList");
//								for(int k = 0;k < item.length();k++){
//									JSONObject jsonItemObj = jsonArray2.getJSONObject(k);
//									FileIetmEntity fileIetmEntity = new FileIetmEntity();
//									if(jsonItemObj.has("fileId"))
//										fileIetmEntity.fileId = jsonItemObj.getInt("fileId");
//									if(jsonItemObj.has("extType"))
//										fileIetmEntity.extType = jsonItemObj.getString("extType");
//									if(jsonItemObj.has("thumbPath"))
//										fileIetmEntity.thumbPath = jsonItemObj.getString("thumbPath");
////									if(jsonItemObj.has("busType"))
////										fileIetmEntity.busType = jsonItemObj.getString("busType");
//									if(jsonItemObj.has("name"))
//										fileIetmEntity.name = jsonItemObj.getString("name");
////									if(jsonItemObj.has("newName"))
////										fileIetmEntity.newName = jsonItemObj.getString("newName");
//									if(jsonItemObj.has("fileUrl"))
//										fileIetmEntity.fileUrl = jsonItemObj.getString("fileUrl");
//									fileList.add(fileIetmEntity);
//								}
//							}
//						}
//					}
//				}
//			}
//			if(jsonObject.has("testQuestionList")){
//				JSONArray itemArr2 = jsonObject.getJSONArray("testQuestionList");
//				for(int i = 0;i < itemArr2.length();i++){
//					JSONObject jsonItem = itemArr2.getJSONObject(i);
//					QADetailItemEntity itemMarkEntity3 = new QADetailItemEntity();
//					itemMarkEntity3.title = "考一考";
//					content.add(itemMarkEntity3);
//					if(jsonItem.has("content"))
//						itemMarkEntity3.content = jsonItem.getString("content");
//					if(jsonItem.has("fileList")){
//						List<FileIetmEntity> fileList = new ArrayList<QADetailEntity.FileIetmEntity>();
//						JSONArray item = jsonItem.getJSONArray("fileList");
//						for(int j = 0;j < item.length();j++){
//							FileIetmEntity fileIetmEntity = new FileIetmEntity();
//							JSONObject jsonItemObj= item.getJSONObject(j);
//							if(jsonItemObj.has("fileId"))
//								fileIetmEntity.fileId = jsonItemObj.getInt("fileId");
//							if(jsonItemObj.has("extType"))
//								fileIetmEntity.extType = jsonItemObj.getString("extType");
//							if(jsonItemObj.has("thumbPath"))
//								fileIetmEntity.thumbPath = jsonItemObj.getString("thumbPath");
////							if(jsonItemObj.has("busType"))
////								fileIetmEntity.busType = jsonItemObj.getString("busType");
//							if(jsonItemObj.has("name"))
//								fileIetmEntity.name = jsonItemObj.getString("name");
////							if(jsonItemObj.has("newName"))
////								fileIetmEntity.newName = jsonItemObj.getString("newName");
//							if(jsonItemObj.has("fileUrl"))
//								fileIetmEntity.fileUrl = jsonItemObj.getString("fileUrl");
//							fileList.add(fileIetmEntity);
//						}
//						itemMarkEntity3.fileList = fileList;
//					}
//					if(jsonItem.has("testAnswerList")){	
//						JSONArray item = jsonItem.getJSONArray("testAnswerList");
//						for(int j = 0;j < item.length();j++){
//							JSONObject jsonItemObj1= item.getJSONObject(j);
//							QADetailItemEntity itemMarkEntity4 = new QADetailItemEntity();
//							List<FileIetmEntity> fileList = new ArrayList<QADetailEntity.FileIetmEntity>();
//							content.add(itemMarkEntity4);
//							itemMarkEntity4.title = "解答内容";
//							itemMarkEntity4.fileList = fileList;
//							itemMarkEntity4.content = jsonItemObj1.getString("content");
//							if(jsonItemObj1.has("markFileList")){
//								JSONArray jsonArray2 = jsonItemObj1.getJSONArray("markFileList");
//								for(int k = 0;k < item.length();k++){
//									JSONObject jsonItemObj = jsonArray2.getJSONObject(k);
//									FileIetmEntity fileIetmEntity = new FileIetmEntity();
//									if(jsonItemObj.has("fileId"))
//										fileIetmEntity.fileId = jsonItemObj.getInt("fileId");
//									if(jsonItemObj.has("extType"))
//										fileIetmEntity.extType = jsonItemObj.getString("extType");
//									if(jsonItemObj.has("thumbPath"))
//										fileIetmEntity.thumbPath = jsonItemObj.getString("thumbPath");
////									if(jsonItemObj.has("busType"))
////										fileIetmEntity.busType = jsonItemObj.getString("busType");
//									if(jsonItemObj.has("name"))
//										fileIetmEntity.name = jsonItemObj.getString("name");	
////									if(jsonItemObj.has("newName"))
////										fileIetmEntity.newName = jsonItemObj.getString("newName");
//									if(jsonItemObj.has("fileUrl"))
//										fileIetmEntity.fileUrl = jsonItemObj.getString("fileUrl");
//									fileList.add(fileIetmEntity);
//								}
//							}
//						}
//					}
//				}
//			}
//		} catch (Exception e) {
//		}
//	}
	
}
