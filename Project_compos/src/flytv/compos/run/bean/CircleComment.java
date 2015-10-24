package flytv.compos.run.bean;

import java.io.Serializable;
/**
 * 讨论
 * @author nike
 *
 */
public class CircleComment implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String content;
	private String createTimeStr;
	private int hasChildren;
	private int id;
	private int isDelete;
	private int logId;
	private int replyId;
	private String myHeader;
	private String replyUserId;
	private String replyUserName;
	private String userId;
	private String userName;
	private int userType;

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

	public int getHasChildren() {
		return hasChildren;
	}

	public void setHasChildren(int hasChildren) {
		this.hasChildren = hasChildren;
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

	public int getLogId() {
		return logId;
	}

	public void setLogId(int logId) {
		this.logId = logId;
	}

	public int getReplyId() {
		return replyId;
	}

	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}

	public String getMyHeader() {
		return myHeader;
	}

	public void setMyHeader(String myHeader) {
		this.myHeader = myHeader;
	}

	public String getReplyUserId() {
		return replyUserId;
	}

	public void setReplyUserId(String replyUserId) {
		this.replyUserId = replyUserId;
	}

	public String getReplyUserName() {
		return replyUserName;
	}

	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
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

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

}
