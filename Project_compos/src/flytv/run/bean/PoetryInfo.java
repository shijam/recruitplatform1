package flytv.run.bean;

import java.io.Serializable;

public class PoetryInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 单手排行
	private String drc;
	private String id;
	private String name; // 作者
	private String dynasty; // 年代
	private String author;
	private String userAvatar;
	private String annotation;// 诗词解释
	private int grade;
	private int chantTimes;
	private int duration;

	private String yuanyinSound;
	private String banzouVideo;
	private String pic;
	private String yuanyinVideo;
	private String banzouSound;
	private String info;

	// 全市排行

	private int thumbsUpTimes;
	private String poetryUrl;
	private String poetryIdeaUrl;
	private String userId;
	private String userName;
	private String userSchoolName;
	private String userClassName;
	private String chantTime;

	// 积分

	private String score;
	private String schoolName;
	private String className;
	private String avatar;

	// 最新诵读

	private PoetryInfo teacherInfo;
	private PoetryInfo userPoetry;
	

	public PoetryInfo getTeacherInfo() {
		return teacherInfo;
	}

	public void setTeacherInfo(PoetryInfo teacherInfo) {
		this.teacherInfo = teacherInfo;
	}

	public PoetryInfo getUserPoetry() {
		return userPoetry;
	}

	public void setUserPoetry(PoetryInfo userPoetry) {
		this.userPoetry = userPoetry;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	//
	private String originalPoetryId;

	public String getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(String userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getOriginalPoetryId() {
		return originalPoetryId;
	}

	public void setOriginalPoetryId(String originalPoetryId) {
		this.originalPoetryId = originalPoetryId;
	}

	public int getThumbsUpTimes() {
		return thumbsUpTimes;
	}

	public void setThumbsUpTimes(int thumbsUpTimes) {
		this.thumbsUpTimes = thumbsUpTimes;
	}

	public String getPoetryUrl() {
		return poetryUrl;
	}

	public void setPoetryUrl(String poetryUrl) {
		this.poetryUrl = poetryUrl;
	}

	public String getPoetryIdeaUrl() {
		return poetryIdeaUrl;
	}

	public void setPoetryIdeaUrl(String poetryIdeaUrl) {
		this.poetryIdeaUrl = poetryIdeaUrl;
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

	public String getUserSchoolName() {
		return userSchoolName;
	}

	public void setUserSchoolName(String userSchoolName) {
		this.userSchoolName = userSchoolName;
	}

	public String getUserClassName() {
		return userClassName;
	}

	public void setUserClassName(String userClassName) {
		this.userClassName = userClassName;
	}

	public String getChantTime() {
		return chantTime;
	}

	public void setChantTime(String chantTime) {
		this.chantTime = chantTime;
	}

	public String getYuanyinSound() {
		return yuanyinSound;
	}

	public String getDrc() {
		return drc;
	}

	public void setDrc(String drc) {
		this.drc = drc;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDynasty() {
		return dynasty;
	}

	public void setDynasty(String dynasty) {
		this.dynasty = dynasty;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}

	public int getChantTimes() {
		return chantTimes;
	}

	public void setChantTimes(int chantTimes) {
		this.chantTimes = chantTimes;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public void setYuanyinSound(String yuanyinSound) {
		this.yuanyinSound = yuanyinSound;
	}

	public String getBanzouVideo() {
		return banzouVideo;
	}

	public void setBanzouVideo(String banzouVideo) {
		this.banzouVideo = banzouVideo;
	}

	public String getYuanyinVideo() {
		return yuanyinVideo;
	}

	public void setYuanyinVideo(String yuanyinVideo) {
		this.yuanyinVideo = yuanyinVideo;
	}

	public String getBanzouSound() {
		return banzouSound;
	}

	public void setBanzouSound(String banzouSound) {
		this.banzouSound = banzouSound;
	}

}
