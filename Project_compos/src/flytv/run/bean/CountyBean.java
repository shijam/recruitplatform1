package flytv.run.bean;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 区县信息
 * @author nike
 *
 */
public class CountyBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String name;
	public ArrayList<CountyBean> schools = new ArrayList<CountyBean>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
