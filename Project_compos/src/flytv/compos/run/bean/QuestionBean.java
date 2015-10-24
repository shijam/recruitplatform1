package flytv.compos.run.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class QuestionBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	private int id;
	private int allCount;
	private int fillBlanksNum;
	private int fillBlanksType;
	private int homeworkId;
	private int isDelete;
	private int questionType;// 试题类型
	private int rightCount;
	private int sequence; // 顺序号
	private int userType; // 
	private int wrongCount; // 
	private String answer;
	private String createTimeString;
	private String description;
	private String questionTitle;// 试题标题
	private String userId;
	private String userName;
	private boolean isShowType;
	private int composType;
	private int composTypeSum;
	
	
	public boolean isShowType() {
		return isShowType;
	}
	public void setShowType(boolean isShowType) {
		this.isShowType = isShowType;
	}
	public int getComposType() {
		return composType;
	}
	public void setComposType(int composType) {
		this.composType = composType;
	}
	public int getComposTypeSum() {
		return composTypeSum;
	}
	public void setComposTypeSum(int composTypeSum) {
		this.composTypeSum = composTypeSum;
	}
	public ArrayList<FileBean>  files = new ArrayList<FileBean>();
	public ArrayList<AnswerBean>  answerList = new ArrayList<AnswerBean>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAllCount() {
		return allCount;
	}
	public void setAllCount(int allCount) {
		this.allCount = allCount;
	}
	public int getFillBlanksNum() {
		return fillBlanksNum;
	}
	public void setFillBlanksNum(int fillBlanksNum) {
		this.fillBlanksNum = fillBlanksNum;
	}
	public int getFillBlanksType() {
		return fillBlanksType;
	}
	public void setFillBlanksType(int fillBlanksType) {
		this.fillBlanksType = fillBlanksType;
	}
	public int getHomeworkId() {
		return homeworkId;
	}
	public void setHomeworkId(int homeworkId) {
		this.homeworkId = homeworkId;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public int getQuestionType() {
		return questionType;
	}
	public void setQuestionType(int questionType) {
		this.questionType = questionType;
	}
	public int getRightCount() {
		return rightCount;
	}
	public void setRightCount(int rightCount) {
		this.rightCount = rightCount;
	}
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public int getUserType() {
		return userType;
	}
	public void setUserType(int userType) {
		this.userType = userType;
	}
	public int getWrongCount() {
		return wrongCount;
	}
	public void setWrongCount(int wrongCount) {
		this.wrongCount = wrongCount;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getCreateTimeString() {
		return createTimeString;
	}
	public void setCreateTimeString(String createTimeString) {
		this.createTimeString = createTimeString;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getQuestionTitle() {
		return questionTitle;
	}
	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
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
	
	
	
	
}
