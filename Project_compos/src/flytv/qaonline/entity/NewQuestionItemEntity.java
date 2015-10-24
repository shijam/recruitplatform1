package flytv.qaonline.entity;

import java.io.Serializable;

public class NewQuestionItemEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String userId;
	private String id;
	private String subjectName;
	private String replyQuestionId;
	private String replyMarkId;
	private String title;
	private String content;
	private String keywords;
	private String imagesIds;
	private String type;
	private String submitStatus;
	public NewQuestionItemEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public NewQuestionItemEntity(String userId, String id,
			String subjectName, String replyQuestionId,
			String replyMarkId, String title,
			String content, String keywords,String imagesIds,String type,String submitStatus) {
		super();
		this.userId = userId;
		this.id = id;
		this.subjectName = subjectName;
		this.replyQuestionId = replyQuestionId;
		this.replyMarkId = replyMarkId;
		this.title = title;
		this.content = content;
		this.keywords = keywords;
		this.imagesIds=imagesIds;
		this.type=type;
		this.submitStatus=submitStatus;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getReplyQuestionId() {
		return replyQuestionId;
	}
	public void setReplyQuestionId(String replyQuestionId) {
		this.replyQuestionId = replyQuestionId;
	}
	public String getReplyMarkId() {
		return replyMarkId;
	}
	public void setReplyMarkId(String replyMarkId) {
		this.replyMarkId = replyMarkId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getImagesIds() {
		return imagesIds;
	}
	public void setImagesIds(String imagesIds) {
		this.imagesIds = imagesIds;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSubmitStatus() {
		return submitStatus;
	}
	public void setSubmitStatus(String submitStatus) {
		this.submitStatus = submitStatus;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
