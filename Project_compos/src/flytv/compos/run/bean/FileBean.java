package flytv.compos.run.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

/**
 * 文件
 * 
 * @author nike
 * 
 */
public class FileBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int fileId;
	private int id;
	private String extType;
	private String thumbPath;// 资源缩略图
	private String busType;
	private String name;
	private String newName;
	private String fileUrl; // 资源url

	//
	private String filePath; // 上传路径
	private Bitmap bitmap;
	private int img_typeId;
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImg_typeId() {
		return img_typeId;
	}

	public void setImg_typeId(int img_typeId) {
		this.img_typeId = img_typeId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
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
