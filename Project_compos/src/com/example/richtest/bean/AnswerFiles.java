package com.example.richtest.bean;

import java.io.Serializable;

import android.graphics.Bitmap;

public class AnswerFiles implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int fileId; // 附件id
	private String name;// 附件原名称
	private String newName;// 附件新名称
	private String extType;// 附件类型

	/*
	 * CompositionMarkImage作文批阅图片 CompositionMarkVoice作文批阅语音
	 * CompositionMarkPostil作文文字批阅 CompositionQuestionImage作文要求图片
	 */
	private String busType = "CompositionMarkImage";// 附件业务名
	private String fileUrl;// 附件地址

	private Bitmap bitmap;
	private boolean isBit;
	
	//
	private String filePath;
	private String fileIdDescs; // 照片描述
	private String fileIds; // 照片地址
	private String splitImagePath;
	private int id;
	
	
	//
	private int size;
	private int userId;
	private String userName;

	private String httpPath;
	private String httpFullPath;
	private int httpId;
	
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHttpPath() {
		return httpPath;
	}
	public void setHttpPath(String httpPath) {
		this.httpPath = httpPath;
	}
	public String getHttpFullPath() {
		return httpFullPath;
	}
	public void setHttpFullPath(String httpFullPath) {
		this.httpFullPath = httpFullPath;
	}
	public int getHttpId() {
		return httpId;
	}
	public void setHttpId(int httpId) {
		this.httpId = httpId;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public AnswerFiles() {
		super();
	}
	public AnswerFiles(int id,String fileIdDescs, String fileIds, int fileId, Bitmap bitmap,
			boolean isBit) {
		super();
		this.id = id;
		this.fileIdDescs = fileIdDescs;
		this.fileIds = fileIds;
		this.fileId = fileId;
		this.bitmap = bitmap;
		this.isBit = isBit;
	}
	public AnswerFiles(int id,String fileUrl) {
		super();
		this.fileId = id;
		this.fileUrl = fileUrl;
	
	}
	public String getFileIdDescs() {
		return fileIdDescs;
	}

	public void setFileIdDescs(String fileIdDescs) {
		this.fileIdDescs = fileIdDescs;
	}

	public String getFileIds() {
		return fileIds;
	}

	public void setFileIds(String fileIds) {
		this.fileIds = fileIds;
	}

	public String getSplitImagePath() {
		return splitImagePath;
	}

	public void setSplitImagePath(String splitImagePath) {
		this.splitImagePath = splitImagePath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public boolean isBit() {
		return isBit;
	}

	public void setBit(boolean isBit) {
		this.isBit = isBit;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
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

	public String getExtType() {
		return extType;
	}

	public void setExtType(String extType) {
		this.extType = extType;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
}
