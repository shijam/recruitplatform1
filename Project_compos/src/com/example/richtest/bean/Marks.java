package com.example.richtest.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class Marks implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int markId;// 批阅id
	private String markUserId;// 批阅人id
	private int markUserType;// 批阅人类型
	private String markUserName;// 批阅人姓名
	private String markUserSchoolName;// 批阅人学校
	private String score;// 分数
	private String note;// 文字批阅
	private int shareStatus;// 推荐状态 0为不推荐，1为待审核，2为已推荐
	private int shareTarget;// 推荐目标 0为不推荐，1为班级，2为公开
	private String shareReason;// 推荐理由
	private int markStatus;// 批阅状态 0为未批阅，1为草稿，2为已批阅
	private String markGmtMarkString;// 批阅时间
	public ArrayList<AnswerFiles> markFiles = new ArrayList<AnswerFiles>();// 批阅附件列表

	public ArrayList<AnswerFiles> getMarkFiles() {
		return markFiles;
	}

	public void setMarkFiles(ArrayList<AnswerFiles> markFiles) {
		this.markFiles = markFiles;
	}

	public int getMarkId() {
		return markId;
	}

	public void setMarkId(int markId) {
		this.markId = markId;
	}

	public String getMarkUserId() {
		return markUserId;
	}

	public void setMarkUserId(String markUserId) {
		this.markUserId = markUserId;
	}

	public int getMarkUserType() {
		return markUserType;
	}

	public void setMarkUserType(int markUserType) {
		this.markUserType = markUserType;
	}

	public String getMarkUserName() {
		return markUserName;
	}

	public void setMarkUserName(String markUserName) {
		this.markUserName = markUserName;
	}

	public String getMarkUserSchoolName() {
		return markUserSchoolName;
	}

	public void setMarkUserSchoolName(String markUserSchoolName) {
		this.markUserSchoolName = markUserSchoolName;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getShareStatus() {
		return shareStatus;
	}

	public void setShareStatus(int shareStatus) {
		this.shareStatus = shareStatus;
	}

	public int getShareTarget() {
		return shareTarget;
	}

	public void setShareTarget(int shareTarget) {
		this.shareTarget = shareTarget;
	}

	public String getShareReason() {
		return shareReason;
	}

	public void setShareReason(String shareReason) {
		this.shareReason = shareReason;
	}

	public int getMarkStatus() {
		return markStatus;
	}

	public void setMarkStatus(int markStatus) {
		this.markStatus = markStatus;
	}

	public String getMarkGmtMarkString() {
		return markGmtMarkString;
	}

	public void setMarkGmtMarkString(String markGmtMarkString) {
		this.markGmtMarkString = markGmtMarkString;
	}

}
