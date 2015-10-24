package flytv.qaonline.entity;

import java.io.Serializable;

public class TeacherItemEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	private String noMarkNum;
	private String teacherDistrictName;
	private String teacherGmtCreateString;
	private String teacherPhoto;
	private String teacherRecruitId;
	private String teacherSchoolName;
	private String teacherUserId;
	private String teacherUserName;

	public String getTeacherUserId() {
		return teacherUserId;
	}

	public String getTeacherUserName() {
		return teacherUserName;
	}

	public void setTeacherUserName(String teacherUserName) {
		this.teacherUserName = teacherUserName;
	}

	public void setTeacherUserId(String teacherUserId) {
		this.teacherUserId = teacherUserId;
	}

	public String getNoMarkNum() {
		return noMarkNum;
	}

	public void setNoMarkNum(String noMarkNum) {
		this.noMarkNum = noMarkNum;
	}

	public String getTeacherDistrictName() {
		return teacherDistrictName;
	}

	public void setTeacherDistrictName(String teacherDistrictName) {
		this.teacherDistrictName = teacherDistrictName;
	}

	public String getTeacherGmtCreateString() {
		return teacherGmtCreateString;
	}

	public void setTeacherGmtCreateString(String teacherGmtCreateString) {
		this.teacherGmtCreateString = teacherGmtCreateString;
	}

	public String getTeacherPhoto() {
		return teacherPhoto;
	}

	public void setTeacherPhoto(String teacherPhoto) {
		this.teacherPhoto = teacherPhoto;
	}

	public String getTeacherRecruitId() {
		return teacherRecruitId;
	}

	public void setTeacherRecruitId(String teacherRecruitId) {
		this.teacherRecruitId = teacherRecruitId;
	}

	public String getTeacherSchoolName() {
		return teacherSchoolName;
	}

	public void setTeacherSchoolName(String teacherSchoolName) {
		this.teacherSchoolName = teacherSchoolName;
	}

	public String getTeacherUserType() {
		return teacherUserType;
	}

	public void setTeacherUserType(String teacherUserType) {
		this.teacherUserType = teacherUserType;
	}

	private String teacherUserType;
}
