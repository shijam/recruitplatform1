package flytv.qaonline.entity;

import java.io.Serializable;
import java.util.ArrayList;

import flytv.compos.run.bean.FileBean;

public class TestQuestAswerBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String content;
	private int questionId;
	public ArrayList<FileBean> fileList = new ArrayList<FileBean>(); 
	public ArrayList<TestMarkBean> testMarkList = new ArrayList<TestMarkBean>();
	public TestQuestAswerBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TestQuestAswerBean(int id, String content, int questionId,
			ArrayList<FileBean> fileList, ArrayList<TestMarkBean> testMarkList) {
		super();
		this.id = id;
		this.content = content;
		this.questionId = questionId;
		this.fileList = fileList;
		this.testMarkList = testMarkList;
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
	public ArrayList<TestMarkBean> getTestMarkList() {
		return testMarkList;
	}
	public void setTestMarkList(ArrayList<TestMarkBean> testMarkList) {
		this.testMarkList = testMarkList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "TestQuestAswerBean [id=" + id + ", content=" + content
				+ ", questionId=" + questionId + ", fileList=" + fileList
				+ ", testMarkList=" + testMarkList + "]";
	}
	 
}
