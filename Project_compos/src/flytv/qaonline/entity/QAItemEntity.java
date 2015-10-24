package flytv.qaonline.entity;

import java.io.Serializable;

public class QAItemEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String questionGmtCreateString;
	private String questionId;
	private String questionTitle;
	private String questionUserId;
	private String questionUserName;
	private String questionSchoolName;
	private String questionUserType;
	private String markStatus;
	private String filePath;
	private String questionBclassName;
	private String questionKeywords;
	private String questionSubjectName;
	private String questionTeacherName;
	
	public String getQuestionSchoolName() {
		return questionSchoolName;
	}
	public void setQuestionSchoolName(String questionSchoolName) {
		this.questionSchoolName = questionSchoolName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getQuestionBclassName() {
		return questionBclassName;
	}
	public void setQuestionBclassName(String questionBclassName) {
		this.questionBclassName = questionBclassName;
	}
	public String getQuestionKeywords() {
		return questionKeywords;
	}
	public void setQuestionKeywords(String questionKeywords) {
		this.questionKeywords = questionKeywords;
	}
	public String getQuestionSubjectName() {
		return questionSubjectName;
	}
	public void setQuestionSubjectName(String questionSubjectName) {
		this.questionSubjectName = questionSubjectName;
	}
	public String getQuestionTeacherName() {
		return questionTeacherName;
	}
	public void setQuestionTeacherName(String questionTeacherName) {
		this.questionTeacherName = questionTeacherName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public QAItemEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QAItemEntity(String questionGmtCreateString, String questionId,
			String questionTitle, String questionUserId,
			String questionUserName, String questionUserSchoolName,
			String questionUserType, String markStatus,String filePath,String questionBclassName,String questionKeywords,String questionSubjectName,String questionTeacherName) {
		super();
		this.questionGmtCreateString = questionGmtCreateString;
		this.questionId = questionId;
		this.questionTitle = questionTitle;
		this.questionUserId = questionUserId;
		this.questionUserName = questionUserName;
		this.questionSchoolName = questionUserSchoolName;
		this.questionUserType = questionUserType;
		this.markStatus = markStatus;
		this.filePath=filePath;
		this.questionBclassName=questionBclassName;
		this.questionKeywords=questionKeywords;
		this.questionSubjectName=questionSubjectName;
		this.questionTeacherName=questionTeacherName;
	}
	public String getQuestionGmtCreateString() {
		return questionGmtCreateString;
	}
	public void setQuestionGmtCreateString(String questionGmtCreateString) {
		this.questionGmtCreateString = questionGmtCreateString;
	}
	public String getQuestionId() {
		return questionId;
	}
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
	public String getQuestionTitle() {
		return questionTitle;
	}
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}
	public String getQuestionUserId() {
		return questionUserId;
	}
	public void setQuestionUserId(String questionUserId) {
		this.questionUserId = questionUserId;
	}
	public String getQuestionUserName() {
		return questionUserName;
	}
	public void setQuestionUserName(String questionUserName) {
		this.questionUserName = questionUserName;
	}
	public String getQuestionUserType() {
		return questionUserType;
	}
	public void setQuestionUserType(String questionUserType) {
		this.questionUserType = questionUserType;
	}
	public String getMarkStatus() {
		return markStatus;
	}
	public void setMarkStatus(String markStatus) {
		this.markStatus = markStatus;
	}
	@Override
	public String toString() {
		return "QAItemEntity [questionGmtCreateString="
				+ questionGmtCreateString + ", questionId=" + questionId
				+ ", questionTitle=" + questionTitle + ", questionUserId="
				+ questionUserId + ", questionUserName=" + questionUserName
				+ ", questionUserSchoolName=" + questionUserName
				+ ", questionUserType=" + questionUserType + ", markStatus="
				+ markStatus + "]";
	}
	
}
