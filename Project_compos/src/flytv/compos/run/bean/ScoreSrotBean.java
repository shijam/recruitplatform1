package flytv.compos.run.bean;

import java.io.Serializable;

public class ScoreSrotBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String allScore;
	private String bclassId;
	private String bclassName;
	private String schoolId;
	private String schoolName;
	private String userId;
	private String userName;
	private int id;
	private int userType;
	private String myHeader;
	

	public String getMyHeader() {
		return myHeader;
	}

	public void setMyHeader(String myHeader) {
		this.myHeader = myHeader;
	}

	public String getAllScore() {
		return allScore;
	}

	public void setAllScore(String allScore) {
		this.allScore = allScore;
	}

	public String getBclassId() {
		return bclassId;
	}

	public void setBclassId(String bclassId) {
		this.bclassId = bclassId;
	}

	public String getBclassName() {
		return bclassName;
	}

	public void setBclassName(String bclassName) {
		this.bclassName = bclassName;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

}
