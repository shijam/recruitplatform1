package flytv.qaonline.entity;

import java.io.Serializable;
import java.util.ArrayList;

import flytv.compos.run.bean.FileBean;

public class QADetailBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private int status;
	private int score;
	private int id;
	private String content;
	private int shareStatus;
	private int markStatus;
	public ArrayList<FileBean> fileList = new ArrayList<FileBean>(); 
	public ArrayList<OnLineMarkBean> onlineMarkList = new ArrayList<OnLineMarkBean>(); 
	public ArrayList<QADetailBean> replyQuestionList = new ArrayList<QADetailBean>(); 
	public ArrayList<TestQuestBean> testQuestionList = new ArrayList<TestQuestBean>();
	public QADetailBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QADetailBean(int status, int score, int id, String content,
			int shareStatus, int markStatus, ArrayList<FileBean> fileList,
			ArrayList<OnLineMarkBean> onlineMarkList,
			ArrayList<QADetailBean> replyQuestionList,
			ArrayList<TestQuestBean> testQuestionList) {
		super();
		this.status = status;
		this.score = score;
		this.id = id;
		this.content = content;
		this.shareStatus = shareStatus;
		this.markStatus = markStatus;
		this.fileList = fileList;
		this.onlineMarkList = onlineMarkList;
		this.replyQuestionList = replyQuestionList;
		this.testQuestionList = testQuestionList;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getShareStatus() {
		return shareStatus;
	}
	public void setShareStatus(int shareStatus) {
		this.shareStatus = shareStatus;
	}
	public int getMarkStatus() {
		return markStatus;
	}
	public void setMarkStatus(int markStatus) {
		this.markStatus = markStatus;
	}
	public ArrayList<FileBean> getFileList() {
		return fileList;
	}
	public void setFileList(ArrayList<FileBean> fileList) {
		this.fileList = fileList;
	}
	public ArrayList<OnLineMarkBean> getOnlineMarkList() {
		return onlineMarkList;
	}
	public void setOnlineMarkList(ArrayList<OnLineMarkBean> onlineMarkList) {
		this.onlineMarkList = onlineMarkList;
	}
	public ArrayList<QADetailBean> getReplyQuestionList() {
		return replyQuestionList;
	}
	public void setReplyQuestionList(ArrayList<QADetailBean> replyQuestionList) {
		this.replyQuestionList = replyQuestionList;
	}
	public ArrayList<TestQuestBean> getTestQuestionList() {
		return testQuestionList;
	}
	public void setTestQuestionList(ArrayList<TestQuestBean> testQuestionList) {
		this.testQuestionList = testQuestionList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "QADetailBean [status=" + status + ", score=" + score + ", id="
				+ id + ", content=" + content + ", shareStatus=" + shareStatus
				+ ", markStatus=" + markStatus + ", fileList=" + fileList
				+ ", onlineMarkList=" + onlineMarkList + ", replyQuestionList="
				+ replyQuestionList + ", testQuestionList=" + testQuestionList
				+ "]";
	}
	
}
