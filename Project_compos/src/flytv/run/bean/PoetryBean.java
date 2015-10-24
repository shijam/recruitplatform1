package flytv.run.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class PoetryBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int message;
	public ArrayList<PoetryNoteBean> poetryList = new ArrayList<PoetryNoteBean>();

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

}
