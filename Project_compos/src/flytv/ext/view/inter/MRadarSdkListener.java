package flytv.ext.view.inter;


public interface MRadarSdkListener {
	int RECORD_INIT_FAIL = 0;
	
	public abstract boolean isNeedComputeVolume();
	public abstract void onVolumeChanged(double volume);
	public abstract void onError(int errorcode, String msg);
	public abstract void onRecording(byte[] data);
	public abstract void onRecordEnd();
	public abstract void onFinish();
}
