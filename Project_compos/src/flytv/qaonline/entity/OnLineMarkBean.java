package flytv.qaonline.entity;

import java.io.Serializable;
import java.util.ArrayList;

import flytv.compos.run.bean.FileBean;

public class OnLineMarkBean implements Serializable{
	private static final long serialVersionUID = 8876921336617433373L;
	private int status;
	private int id;
	private String content;
	private int questionId;
	public ArrayList<FileBean> markFileList = new ArrayList<FileBean>();
	public OnLineMarkBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public OnLineMarkBean(int status, int id, String content, int questionId,
			ArrayList<FileBean> markFileList) {
		super();
		this.status = status;
		this.id = id;
		this.content = content;
		this.questionId = questionId;
		this.markFileList = markFileList;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
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
	public ArrayList<FileBean> getMarkFileList() {
		return markFileList;
	}
	public void setMarkFileList(ArrayList<FileBean> markFileList) {
		this.markFileList = markFileList;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "OnLineMarkBean [status=" + status + ", id=" + id + ", content="
				+ content + ", questionId=" + questionId + ", markFileList="
				+ markFileList + "]";
	}
	
  
	
}
