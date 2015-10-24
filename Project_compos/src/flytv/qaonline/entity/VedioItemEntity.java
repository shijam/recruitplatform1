package flytv.qaonline.entity;

import java.io.Serializable;
import java.util.List;

public class VedioItemEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String content;
	private String createTimeStr;
	private String gradeId;
	private String gradeName;
	private String id;
	private String isDelete;
	private String sectionId;
	private String subjectId;
	private String sectionName;
	private String subjectName;
	private String title;
	private String userId;
	private String userName;
	private String userType;
	private List<MyFileEntity> filelist;
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "VedioItemEntity [content=" + content + ", createTimeStr="
				+ createTimeStr + ", gradeId=" + gradeId + ", gradeName="
				+ gradeName + ", id=" + id + ", isDelete=" + isDelete
				+ ", sectionId=" + sectionId + ", subjectId=" + subjectId
				+ ", sectionName=" + sectionName + ", subjectName="
				+ subjectName + ", title=" + title + ", userId=" + userId
				+ ", userName=" + userName + ", userType=" + userType
				+ ", filelist=" + filelist + "]";
	}
	public String getCreateTimeStr() {
		return createTimeStr;
	}
	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	public String getGradeId() {
		return gradeId;
	}
	public void setGradeId(String gradeId) {
		this.gradeId = gradeId;
	}
	public String getGradeName() {
		return gradeName;
	}
	public void setGradeName(String gradeName) {
		this.gradeName = gradeName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}
	public String getSectionId() {
		return sectionId;
	}
	public void setSectionId(String sectionId) {
		this.sectionId = sectionId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public List<MyFileEntity> getFilelist() {
		return filelist;
	}
	public void setFilelist(List<MyFileEntity> filelist) {
		this.filelist = filelist;
	}
}
