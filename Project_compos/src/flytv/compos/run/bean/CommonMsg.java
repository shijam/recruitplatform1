package flytv.compos.run.bean;

import java.io.Serializable;
import java.util.ArrayList;

import flytv.run.bean.StuBean;

/**
 * 
 * @author nike
 * 
 */
public class CommonMsg implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int message;
	private String msgInfo;
	private int answerId;
	public ArrayList<QuestionBean> items = new ArrayList<QuestionBean>();
	public ArrayList<StuComposBean> studentList = new ArrayList<StuComposBean>();
	
	public ArrayList<StuBean> commitNameList = new ArrayList<StuBean>();
	public ArrayList<StuBean> noCommitNameList = new ArrayList<StuBean>();
	private String obj;
	private String fileService;
	
	public String getFileService() {
		return fileService;
	}

	public void setFileService(String fileService) {
		this.fileService = fileService;
	}

	public String getObj() {
		return obj;
	}

	public void setObj(String obj) {
		this.obj = obj;
	}

	public CommonMsg() {
		super();
	}

	public CommonMsg(int message, String msgInfo) {
		super();
		this.message = message;
		this.msgInfo = msgInfo;
	}

	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	public int getMessage() {
		return message;
	}

	public String getMsgInfo() {
		return msgInfo;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}

}
