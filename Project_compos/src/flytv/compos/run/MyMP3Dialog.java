package flytv.compos.run;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.kubility.demo.MP3Recorder;

import flytv.ext.view.RecordButton;
import flytv.ext.view.inter.OnRecordGetFileLister;

public class MyMP3Dialog extends Activity implements OnRecordGetFileLister {
	private MP3Recorder mp3Recorder = new MP3Recorder();
	private ImageView mPic;
	private static int[] res = { R.drawable.mic_2, R.drawable.mic_3,
			R.drawable.mic_4, R.drawable.mic_5 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mp3_dialog);
		mPic = (ImageView) findViewById(R.id.image_num);
		RecordButton recorButton = (RecordButton) findViewById(R.id.recordButton);
		recorButton.setMp3Recorder(mp3Recorder);
		recorButton.setOnRecordGetFileLister(this);
	}

	@Override
	public void resultAMRPathLister(String filePath) {
		Intent intent = new Intent();
		intent.putExtra("FileImage", filePath);
		setResult(RESULT_OK, intent);
		finish();

	}

	@Override
	public void resultNumPathLister(int num) {
		mPic.setImageResource(res[num]);
	}

	@Override
	public void resultVolumeChanged(double num) {
		// TODO Auto-generated method stub
		
	}
}
