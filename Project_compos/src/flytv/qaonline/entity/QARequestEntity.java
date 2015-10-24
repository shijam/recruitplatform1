package flytv.qaonline.entity;

import java.io.Serializable;
import java.util.List;

public class QARequestEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private int currentPage;
	private String fileService;
	private List<QAItemEntity> items;
	private String message;
	private String obj;
	private int pageSize;
	private int totalCount;
	private int totalPage;
	private String webService;
	public QARequestEntity() {
		super();
	}
	public QARequestEntity(int currentPage, String fileService,
			List<QAItemEntity> items, String message, String obj, int pageSize,
			int totalCount, int totalPage, String webService) {
		super();
		this.currentPage = currentPage;
		this.fileService = fileService;
		this.items = items;
		this.message = message;
		this.obj = obj;
		this.pageSize = pageSize;
		this.totalCount = totalCount;
		this.totalPage = totalPage;
		this.webService = webService;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public String getFileService() {
		return fileService;
	}
	public void setFileService(String fileService) {
		this.fileService = fileService;
	}
	public List<QAItemEntity> getItems() {
		return items;
	}
	public void setItems(List<QAItemEntity> items) {
		this.items = items;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getObj() {
		return obj;
	}
	public void setObj(String obj) {
		this.obj = obj;
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
	@Override
	public String toString() {
		return "QARequestEntity [currentPage=" + currentPage + ", fileService="
				+ fileService + ", items=" + items + ", message=" + message
				+ ", obj=" + obj + ", pageSize=" + pageSize + ", totalCount="
				+ totalCount + ", totalPage=" + totalPage + ", webService="
				+ webService + "]";
	}
	
}
