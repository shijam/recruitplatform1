package flytv.qaonline.entity;

import java.util.ArrayList;

import flytv.compos.run.bean.FileBean;

public class TestQuestBean {
	private String content;
	private int id;
	private int questionId;
	public ArrayList<FileBean> fileList = new ArrayList<FileBean>(); 
	public ArrayList<TestQuestAswerBean> testAnswerList = new ArrayList<TestQuestAswerBean>();
	public TestQuestBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TestQuestBean(String content, int id, int questionId,
			ArrayList<FileBean> fileList,
			ArrayList<TestQuestAswerBean> testAnswerList) {
		super();
		this.content = content;
		this.id = id;
		this.questionId = questionId;
		this.fileList = fileList;
		this.testAnswerList = testAnswerList;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	public ArrayList<FileBean> getFileList() {
		return fileList;
	}
	public void setFileList(ArrayList<FileBean> fileList) {
		this.fileList = fileList;
	}
	public ArrayList<TestQuestAswerBean> getTestAnswerList() {
		return testAnswerList;
	}
	public void setTestAnswerList(ArrayList<TestQuestAswerBean> testAnswerList) {
		this.testAnswerList = testAnswerList;
	}
	@Override
	public String toString() {
		return "TestQuestBean [content=" + content + ", id=" + id
				+ ", questionId=" + questionId + ", fileList=" + fileList
				+ ", testAnswerList=" + testAnswerList + "]";
	}
	 
	 
}
