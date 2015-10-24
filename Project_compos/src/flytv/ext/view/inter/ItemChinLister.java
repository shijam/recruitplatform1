package flytv.ext.view.inter;

public interface ItemChinLister {
	/**
	 * 1 预存 2 提交
	 * 
	 * @param cmdIndex
	 */
	public void execute(int cmdIndex,int position);
	
	/**
	 * 添加作业
	 * @param position
	 * @param gridPosition
	 */
	
	public void executeImage(final int position,final int gridPosition);

	/**
	 * 跳转界面
	 * 
	 * @param position
	 */
	public void cmdStartIntent(int position);
}
