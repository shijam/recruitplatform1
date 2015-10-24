package flytv.ext.view.inter;

public interface OnTvChangedListener {
	
	public void onStart(boolean isSwitch);
	
	public void onPlayStatu(int playType);

	public void OnCompletion();
}
