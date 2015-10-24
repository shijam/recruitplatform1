package flytv.qaonline.entity;

import java.util.ArrayList;

import flytv.compos.run.bean.FileBean;

public class QADetailItemEntity {
	private int id;
	private int status;
	private int score;
	private int key;
	private String title;
	private String content;
	private int questionId;
	private int shareStatus;
	private int markStatus;
	private ArrayList<FileBean> fileList;
	public QADetailItemEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public QADetailItemEntity(int id, int status, int score, int key,
			String title, String content, int questionId, int shareStatus,
			int markStatus, ArrayList<FileBean> fileList) {
		super();
		this.id = id;
		this.status = status;
		this.score = score;
		this.key = key;
		this.title = title;
		this.content = content;
		this.questionId = questionId;
		this.shareStatus = shareStatus;
		this.markStatus = markStatus;
		this.fileList = fileList;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
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
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
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
	@Override
	public String toString() {
		return "QADetailItemEntity [id=" + id + ", status=" + status
				+ ", score=" + score + ", key=" + key + ", title=" + title
				+ ", content=" + content + ", questionId=" + questionId
				+ ", shareStatus=" + shareStatus + ", markStatus=" + markStatus
				+ ", fileList=" + fileList + "]";
	}
	 
}
