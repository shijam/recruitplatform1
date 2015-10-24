package flytv.qaonline.entity;

import java.io.Serializable;
import java.util.List;

public class QATeacherListEntity implements Serializable {
	private static final long serialVersionUID = 1L;

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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String currentPage;
	private String fileService;
	private String message;
	private String pageSize;
	private String totalCount;
	private String totalPage;
	private List<TeacherItemEntity> items;

	public List<TeacherItemEntity> getItems() {
		return items;
	}

	public void setItems(List<TeacherItemEntity> items) {
		this.items = items;
	}
}
