package flytv.run.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import flytv.compos.run.bean.Circle;
import flytv.compos.run.bean.ClassBean;
import flytv.compos.run.bean.GradeBean;
import flytv.compos.run.bean.SchoolBean;
import flytv.compos.run.bean.SectionBean;

public class TVCodeBean implements Serializable {

	/**
	 * 
	 */
	// 机顶盒绑定实体
	private static final long serialVersionUID = 1L;
	private String userToken;
	private String deviceNo;
	private String userCode;
	private String cpmaId;
	private int stbStatus;
	private int stbIsInit;

	// 用户登陆信息

	private String userId;
	private String name;
	private String sex;
	private String userType;
	private String schoolName;
	private String schoolId;
	private String className;
	private String birthday;
	private int gradeNo;
	private String avatar;
	private String appName;
	private String appDesc;
	private AppInfo appVersion;
	private AppInfo versionInfo;
	private String pwd;
	private int score;
	//
	private String userName;
	private int recruitStatus; // 名师状态
	private String photo;
	
	private int message;
	private int point;
	
	private String msgInfo;
	private String CSRQ;
	private int homeworkId;
	public Circle obj = new Circle();
	
	public AppInfo getVersionInfo() {
		return versionInfo;
	}

	public void setVersionInfo(AppInfo versionInfo) {
		this.versionInfo = versionInfo;
	}

	public String getCSRQ() {
		return CSRQ;
	}

	public void setCSRQ(String cSRQ) {
		CSRQ = cSRQ;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getHomeworkId() {
		return homeworkId;
	}

	public void setHomeworkId(int homeworkId) {
		this.homeworkId = homeworkId;
	}
	private int answerId;
	
	public int getAnswerId() {
		return answerId;
	}

	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}
	//
	private String filePath;
	private String fileName;
	private String thumbPath;
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	public String getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}

	public int getRecruitStatus() {
		return recruitStatus;
	}

	public void setRecruitStatus(int recruitStatus) {
		this.recruitStatus = recruitStatus;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	// 音频合成
	private String userPoetryId;
	private String userPoetryUrl;
	private int rate;
	// 班级
	public List<ClassBean> bclassList = new ArrayList<ClassBean>();
	// 年纪
	public List<GradeBean> gradeList = new ArrayList<GradeBean>();
	// 学科
	public List<StuBean> subjectList = new ArrayList<StuBean>();
	public List<SectionBean> sectionList = new ArrayList<SectionBean>();
	public ArrayList<SchoolBean> items = new ArrayList<SchoolBean>();

	public ArrayList<SchoolBean> getItems() {
		return items;
	}

	public void setItems(ArrayList<SchoolBean> items) {
		this.items = items;
	}

	//
	private int IsAuto;
	

	public int getIsAuto() {
		return IsAuto;
	}

	public void setIsAuto(int isAuto) {
		IsAuto = isAuto;
	}

	

	public List<StuBean> getSubjectList() {
		return subjectList;
	}

	public void setSubjectList(List<StuBean> subjectList) {
		this.subjectList = subjectList;
	}

	public int getStbIsInit() {
		return stbIsInit;
	}

	public void setStbIsInit(int stbIsInit) {
		this.stbIsInit = stbIsInit;
	}

	//
	private String originalPoetryId;

	// 点赞状态
	private String thumbsStatus;

	// 理解
	private String ideaUrl;

	// 是否可以阅读

	private int times;
	private String status;

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getGradeNo() {
		return gradeNo;
	}

	public void setGradeNo(int gradeNo) {
		this.gradeNo = gradeNo;
	}

	public String getIdeaUrl() {
		return ideaUrl;
	}

	public void setIdeaUrl(String ideaUrl) {
		this.ideaUrl = ideaUrl;
	}

	public String isThumbsStatus() {
		return thumbsStatus;
	}

	public void setThumbsStatus(String thumbsStatus) {
		this.thumbsStatus = thumbsStatus;
	}

	public ArrayList<PoetryTypeBean> recommendType = new ArrayList<PoetryTypeBean>();

	public String getOriginalPoetryId() {
		return originalPoetryId;
	}

	public void setOriginalPoetryId(String originalPoetryId) {
		this.originalPoetryId = originalPoetryId;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public String getUserPoetryId() {
		return userPoetryId;
	}

	public void setUserPoetryId(String userPoetryId) {
		this.userPoetryId = userPoetryId;
	}

	public String getUserPoetryUrl() {
		return userPoetryUrl;
	}

	public void setUserPoetryUrl(String userPoetryUrl) {
		this.userPoetryUrl = userPoetryUrl;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	// 区县信息
	private String id;
	public ArrayList<TVCodeBean> schools = new ArrayList<TVCodeBean>();
	public ArrayList<TVCodeBean> districts = new ArrayList<TVCodeBean>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppDesc() {
		return appDesc;
	}

	public void setAppDesc(String appDesc) {
		this.appDesc = appDesc;
	}

	public AppInfo getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(AppInfo appVersion) {
		this.appVersion = appVersion;
	}

	public int getStbStatus() {
		return stbStatus;
	}

	public void setStbStatus(int stbStatus) {
		this.stbStatus = stbStatus;
	}

	public String getCpmaId() {
		return cpmaId;
	}

	public void setCpmaId(String cpmaId) {
		this.cpmaId = cpmaId;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getDeviceNo() {
		return deviceNo;
	}

	public void setDeviceNo(String deviceNo) {
		this.deviceNo = deviceNo;
	}

	public String getUserCode() {
		return userCode;
	}

	public void setUserCode(String userCode) {
		this.userCode = userCode;
	}

}
