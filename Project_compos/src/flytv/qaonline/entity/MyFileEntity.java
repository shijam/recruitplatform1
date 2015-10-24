package flytv.qaonline.entity;

import java.io.Serializable;

public class MyFileEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String fileId;
	@Override
	public String toString() {
		return "MyFileEntity [fileId=" + fileId + ", extType=" + extType
				+ ", thumbPath=" + thumbPath + ", busType=" + busType
				+ ", name=" + name + ", newName=" + newName + ", fileUrl="
				+ fileUrl + "]";
	}
	private String extType;
	private String thumbPath;
	private String busType;
	private String name;
	private String newName;
	private String fileUrl;
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getExtType() {
		return extType;
	}
	public void setExtType(String extType) {
		this.extType = extType;
	}
	public String getThumbPath() {
		return thumbPath;
	}
	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}
	public String getBusType() {
		return busType;
	}
	public void setBusType(String busType) {
		this.busType = busType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	public String getFileUrl() {
		return fileUrl;
	}
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
}
