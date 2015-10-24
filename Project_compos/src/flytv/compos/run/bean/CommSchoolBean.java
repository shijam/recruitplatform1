package flytv.compos.run.bean;

import java.io.Serializable;
import java.util.List;

public class CommSchoolBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private int message;
	private List<SchoolBean> items;

	public int getMessage() {
		return message;
	}
	public void setMessage(int message) {
		this.message = message;
	}
	public List<SchoolBean> getItems() {
		return items;
	}
	public void setItems(List<SchoolBean> items) {
		this.items = items;
	}
	
}
