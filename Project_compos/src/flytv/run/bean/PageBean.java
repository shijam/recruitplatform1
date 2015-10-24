package flytv.run.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class PageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<PoetryInfo> list = new ArrayList<PoetryInfo>();

	private int size;
	private int page;
	private int total;
	private int totalPage;
	private int actualSize;

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getActualSize() {
		return actualSize;
	}

	public void setActualSize(int actualSize) {
		this.actualSize = actualSize;
	}

}
