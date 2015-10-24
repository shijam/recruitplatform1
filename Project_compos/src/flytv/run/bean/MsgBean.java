package flytv.run.bean;

import java.io.Serializable;
/**
 * STB通用返回值
 * @author nike
 *
 */
public class MsgBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String message;
	private String success;
	private String result;
	private String msgInfo;
	
	public String getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}

	private TVCodeBean items ;
	
	
	public TVCodeBean getItems() {
		return items;
	}

	public void setItems(TVCodeBean items) {
		this.items = items;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

}
