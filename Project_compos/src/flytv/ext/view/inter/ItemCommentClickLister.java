package flytv.ext.view.inter;

public interface ItemCommentClickLister {
	void onBottomComment(int position,int commendId);
	void onBottomPraise(int position);
	/**
	 * 删除标志
	 * @param position
	 */
	void onBottomDeleLog(int position);
	void onBottomEditLog(int position);
	void onBottomEdit(String message, String logId,int index,int commendId);
}
