package flytv.compos.run.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 朋友圈详细信息
 * 
 * @author nike
 * 
 */
public class Circle implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int circleType;
	private int commentNum;
	private int id;
	private int isDelete;
	private int subjectId;
	private int userType;
	private int zanStatus;
	private String content;
	private String createTimeStr;
	private String subjectName;
	private String title;
	private String userId;
	private String userName;
	private String zanNames;// 赞
	private int message;
	private String msgInfo;
	
	private String myHeader;
	
	
	public String getMyHeader() {
		return myHeader;
	}

	public void setMyHeader(String myHeader) {
		this.myHeader = myHeader;
	}

	public ArrayList<FileBean> fileList = new ArrayList<FileBean>();
	public ArrayList<CircleComment> circleCommentList = new ArrayList<CircleComment>();

	public int getMessage() {
		return message;
	}

	public void setMessage(int message) {
		this.message = message;
	}

	public String getMsgInfo() {
		return msgInfo;
	}

	public void setMsgInfo(String msgInfo) {
		this.msgInfo = msgInfo;
	}

	public int getCircleType() {
		return circleType;
	}

	public void setCircleType(int circleType) {
		this.circleType = circleType;
	}

	public int getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(int commentNum) {
		this.commentNum = commentNum;
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

	public int getZanStatus() {
		return zanStatus;
	}

	public void setZanStatus(int zanStatus) {
		this.zanStatus = zanStatus;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getZanNames() {
		return zanNames;
	}

	public void setZanNames(String zanNames) {
		this.zanNames = zanNames;
	}

}
