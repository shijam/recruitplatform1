package flytv.compos.run.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class AnswerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<FileBean> answerFiles = new ArrayList<FileBean>();// 学生图片
	public ArrayList<FileBean> markFiles = new ArrayList<FileBean>();// 老师批阅图片
	private String gmtCreateString;
	private String studentId;
	private String userAnswer; // 学生答案
	private int homeworkId;
	private int id;
	private int isDelete;
	private String markContent;// 老师点评
	private String title;
	private String userScore;
	private boolean isAddFire=false;
	
	private int markStatus;
	private int questionId;
	private int status;
	private int type;

	
	public boolean isAddFire() {
		return isAddFire;
	}

	public void setAddFire(boolean isAddFire) {
		this.isAddFire = isAddFire;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUserScore() {
		return userScore;
	}

	public void setUserScore(String userScore) {
		this.userScore = userScore;
	}

	public String getGmtCreateString() {
		return gmtCreateString;
	}

	public void setGmtCreateString(String gmtCreateString) {
		this.gmtCreateString = gmtCreateString;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getUserAnswer() {
		return userAnswer;
	}

	public void setUserAnswer(String userAnswer) {
		this.userAnswer = userAnswer;
	}

	public int getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(int homeworkId) {
		this.homeworkId = homeworkId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getMarkContent() {
		return markContent;
	}

	public void setMarkContent(String markContent) {
		this.markContent = markContent;
	}

	public int getMarkStatus() {
		return markStatus;
	}

	public void setMarkStatus(int markStatus) {
		this.markStatus = markStatus;
	}

	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
