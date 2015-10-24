package flytv.ext.utils;

import java.io.IOException;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.widget.Toast;

public class PlayerAmrUtil {
	private static MediaPlayer player = null;

	private PlayerAmrUtil() {
	}

	private onPlayerCompleLister onCompleLister;

	public onPlayerCompleLister getOnCompleLister() {
		return onCompleLister;
	}

	public void setOnCompleLister(onPlayerCompleLister onCompleLister) {
		this.onCompleLister = onCompleLister;
	}

	private static PlayerAmrUtil playerAmrUtil;
	private static Context context;

	public static PlayerAmrUtil getInster(Context mContext) {
		context = mContext;
		if (playerAmrUtil == null) {
			playerAmrUtil = new PlayerAmrUtil();
		}
		return playerAmrUtil;
	}

	public void play(String playUrl, boolean isFile) {
		if (player != null) {
			if (player.isPlaying()) {
				return;
			}
		}
		player = new MediaPlayer();

		try {
			LogUtils.print(playUrl);
			player.setDataSource(playUrl);
			player.prepare();
			player.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer arg0) {
				// TODO Auto-generated method stub
				if (onCompleLister != null) {
					onCompleLister.onPre();
				}
			}
		});
		player.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				if (onCompleLister != null) {
					onCompleLister.onComple();
					stop();
				}
			}
		});
		player.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				Toast.makeText(context, "播放失败!", Toast.LENGTH_LONG).show();
				if (onCompleLister != null) {
					onCompleLister.onError();
				}
				return false;
			}
		});

	}

	public interface onPlayerCompleLister {
		void onPre();

		void onComple();

		void onError();
	}

	public void pauseOrStart() {
		if (player != null) {
			if (player.isPlaying()) {
				player.pause();
			} else {
				player.start();
			}
		}
	}

	public boolean isPlayer() {
		if(player!=null){
			return player.isPlaying();
		}
		return false;
	}

	public void stop() {
		if (player != null) {
			if (player.isPlaying()) {
				player.stop();
			}
			player.reset();
			player.release();
			player = null;
		}
	}

}
