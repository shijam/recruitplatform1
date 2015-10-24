package flytv.compos.run.bean;

import java.io.Serializable;

public class ComposMarkBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String msg;
	private int questionId;
	private FileBean fileBean;

	
	public int getQuestionId() {
		return questionId;
	}

	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public FileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}

}
