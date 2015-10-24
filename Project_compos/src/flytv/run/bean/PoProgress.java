package flytv.run.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class PoProgress implements Serializable {

	private int message;
	private String percent;
	private String syntheticVideo;

	
	// 通用属性
	private String msgInfo;
	
	public String getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public String getPercent() {
		return percent;
	}

	public void setPercent(String percent) {
		this.percent = percent;
	}

	public String getSyntheticVideo() {
		return syntheticVideo;
	}

	public void setSyntheticVideo(String syntheticVideo) {
		this.syntheticVideo = syntheticVideo;
	}

}
