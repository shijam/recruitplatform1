package flytv.compos.run.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class ComposBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// 作业信息
	private String createTimeStr;
	private String endTime;
	private String name;
	private String schoolName;
	private String subjectName;
	private String userId;
	private String userName;
	private int allNum;
	private int commitNum;
	private int id;
	private int homeworkId;
	private int isDelete;
	private int markStatus;
	private int readNum;
	private int schoolId;
	private int status;
	private int subjectId;
	private int userType;
	private int type;
	

	// 班级信息

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ArrayList<ClassBean> bclassList = new ArrayList<ClassBean>();

	//

	public int getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(int homeworkId) {
		this.homeworkId = homeworkId;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getAllNum() {
		return allNum;
	}

	public void setAllNum(int allNum) {
		this.allNum = allNum;
	}

	public int getCommitNum() {
		return commitNum;
	}

	public void setCommitNum(int commitNum) {
		this.commitNum = commitNum;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public int getMarkStatus() {
		return markStatus;
	}

	public void setMarkStatus(int markStatus) {
		this.markStatus = markStatus;
	}

	public int getReadNum() {
		return readNum;
	}

	public void setReadNum(int readNum) {
		this.readNum = readNum;
	}

	public int getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(int schoolId) {
		this.schoolId = schoolId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

}
