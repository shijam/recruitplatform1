package flytv.compos.run.bean;

import java.io.Serializable;

/**
 * 通用分页管理类
 * 
 * @author nike
 * 
 */
public class CirclePageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int currentPage;
	private int message;
	private int pageSize;
	private int totalCount;
	private int totalPage;

	private String items ;
	private String webService;
	private String fileService;// 文件ַurl


	public String getItems() {
		return items;
	}

	public void setItems(String items) {
		this.items = items;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public String getWebService() {
		return webService;
	}

	public void setWebService(String webService) {
		this.webService = webService;
	}

	public String getFileService() {
		return fileService;
	}

	public void setFileService(String fileService) {
		this.fileService = fileService;
	}

}
