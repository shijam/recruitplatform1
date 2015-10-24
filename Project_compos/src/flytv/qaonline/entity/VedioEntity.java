package flytv.qaonline.entity;

import java.io.Serializable;
import java.util.List;

public class VedioEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private String currentPage;
	private String fileService;
	private List<VedioItemEntity> items;
	private String message;
	public String getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
	public String getFileService() {
		return fileService;
	}
	public void setFileService(String fileService) {
		this.fileService = fileService;
	}
	public List<VedioItemEntity> getItems() {
		return items;
	}
	public void setItems(List<VedioItemEntity> items) {
		this.items = items;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(String totalCount) {
		this.totalCount = totalCount;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
	public String getWebService() {
		return webService;
	}
	public void setWebService(String webService) {
		this.webService = webService;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	private String pageSize;
	private String totalCount;
	private String totalPage;
	private String webService;
	private String subjectId;
}
