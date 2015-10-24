package flytv.run.bean;

import java.io.Serializable;
/**
 * 班级
 * @author nike
 *
 */
public class ClassBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String bclassId;
	private String bclassName;

	public String getBclassId() {
		return bclassId;
	}

	public void setBclassId(String bclassId) {
		this.bclassId = bclassId;
	}

	public String getBclassName() {
		return bclassName;
	}

	public void setBclassName(String bclassName) {
		this.bclassName = bclassName;
	}

}
