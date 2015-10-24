package flytv.compos.run.adpublic;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import flytv.compos.run.R;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.view.VideoView;

@SuppressLint("ClickableViewAccessibility")
public class AyVideoCompoe extends Activity {
	@ViewInject(R.id.player_live_title)
	TextView player_live_title;

	@ViewInject(R.id.player_back)
	ImageView player_back;

	@ViewInject(R.id.player_surface)
	VideoView mSunniplayer;

	@ViewInject(R.id.player_play_pause)
	private ImageButton mPlayPauseBtn;
	@ViewInject(R.id.player_play_time)
	private TextView mPlayTime;
	@ViewInject(R.id.player_duration)
	private TextView mPlayDuration;

	@ViewInject(R.id.player_play_seekbar)
	private SeekBar mPlaySeekbar;

	@ViewInject(R.id.layout_loading)
	private LinearLayout mLoadingLayout;
	@ViewInject(R.id.player_controler_top)
	private LinearLayout player_controler_top;
	@ViewInject(R.id.player_controler)
	private LinearLayout player_controler;

	private String pathUrl;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player_media_view);
		ViewUtils.inject(this);
		pathUrl = getIntent().getStringExtra("httpUrl");
		init();
	}

	void init() {
		player_live_title.setVisibility(View.INVISIBLE);
		player_live_title.setTextColor(getResources().getColor(R.color.White));
		player_live_title.setTextSize(18);
		mSunniplayer.setVisibility(View.VISIBLE);
		mSunniplayer.setVideoPath(pathUrl);
		mSunniplayer.start();
		mHandler.sendEmptyMessage(PLAY_TIME);

		player_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}

		});
		mSunniplayer.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					if (player_controler.getVisibility() == View.INVISIBLE) {
						player_controler.setVisibility(View.VISIBLE);
						player_controler_top.setVisibility(View.VISIBLE);
					} else if (player_controler.getVisibility() == View.VISIBLE) {
						if (!mSunniplayer.isPlaying()) {
							player_controler.setVisibility(View.VISIBLE);
							player_controler_top.setVisibility(View.VISIBLE);
						} else {
							player_controler.setVisibility(View.INVISIBLE);
							player_controler_top.setVisibility(View.INVISIBLE);
						}

					}
					return true;
				} else {
					return true;
				}
			}
		});
		mPlayPauseBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isRemote) {
					if (isPlayCom) {
						isPlayCom = false;
						isPlay = true;
					} else {
					}
				} else {
					if (isPlayCom) {
						isPlayCom = false;
						mSunniplayer.start();
					} else {
						if (mSunniplayer.isPlaying()) {
							// mCurrenPlayTime =
							// mSunniplayer.getCurrentPosition();
							mSunniplayer.pause();
						} else {
							mSunniplayer.start();
						}
					}
				}

			}
		});
		mSunniplayer.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				isPlayCom = false;
				dismissLoadingLayout();
				updateUIPlayState();
				mHandler.sendEmptyMessageDelayed(1, 700);
				mHandler.sendEmptyMessageDelayed(201, 3000);

			}

		});
		mSunniplayer.setOnCompletionListener(new OnCompletionListener() {

			@Override
			public void onCompletion(MediaPlayer arg0) {
				updateUIPlayState();
				isPlayCom = true;
				// mCurrenPlayTime = 0;
				// mDuration = 0;
				mHandler.sendEmptyMessageDelayed(202, 200);
				mPlayTime.setText("00:00");
				mPlaySeekbar.setProgress(0);
				mSunniplayer.start();
				mSunniplayer.pause();
				player_controler.setVisibility(View.VISIBLE);
				player_controler_top.setVisibility(View.VISIBLE);
			}
		});
		mSunniplayer.setOnInfoListener(new OnInfoListener() {

			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
				switch (what) {
				case MediaPlayer.MEDIA_INFO_BUFFERING_START:
					updateUIPlayState();
					break;
				case MediaPlayer.MEDIA_INFO_BUFFERING_END:
					updateUIPlayState();

					break;
				case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
					updateUIPlayState();
					break;
				}
				return true;
			}

		});

		mSunniplayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				if (mSunniplayer.isPlaying()) {
					mSunniplayer.pause();
				}
				mSunniplayer.stopPlayback();
				player_controler.setVisibility(View.VISIBLE);
				player_controler_top.setVisibility(View.VISIBLE);
				return false;
			}
		});

		mPlaySeekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				isPlayCom = false;
				if (isRemote) {
					tv_play_index = arg0.getProgress();

				} else {
					mSunniplayer.pause();
					mSunniplayer.seekTo(arg0.getProgress());
					mSunniplayer.start();
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
				// TODO Auto-generated method stub

			}
		});
	}

	private final static int PLAY_TIME = 1;

	protected void dismissLoadingLayout() {
		// TODO Auto-generated method stub
		mLoadingLayout.setVisibility(View.INVISIBLE);
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case -1:
				break;
			case PLAY_TIME:
				updateUIPlayState();
				mHandler.sendEmptyMessageDelayed(PLAY_TIME, 1000);
				break;
			case 2:
				updateUIPlayTime(0);
				break;
			case 201:
				player_controler.setVisibility(View.INVISIBLE);
				player_controler_top.setVisibility(View.INVISIBLE);
				break;
			case 202:
				player_controler.setVisibility(View.VISIBLE);
				player_controler_top.setVisibility(View.VISIBLE);
				break;
			default:
				updateUIPlayState();
				break;
			}
		}

	};
	String TAG = "AyReaderInfoMp3";
	boolean mIsPlayed = false;
	boolean isPlayCom = false;
	boolean isRemote = false, isPlay = false;

	public void resume() {
		if (!mIsPlayed) {
			mSunniplayer.start();
			mLoadingLayout.setVisibility(View.VISIBLE);
		} else if (!mSunniplayer.isPlaying()) {
			mSunniplayer.start();
		}
		mIsPlayed = true;
	}

	private int tv_play_index;

	protected void updateUIPlayTime(int progress) {
		if (isPlayCom) {
			return;
		}
		// TODO Auto-generated method stub
		int curr = -1;
		int length = 0;
		if (isRemote) {
			if (!isPlay) {
				return;
			}
			tv_play_index++;
			curr = tv_play_index;

		} else {
			length = (int) mSunniplayer.getDuration();
			curr = (int) mSunniplayer.getCurrentPosition();
		}
		if (length >= 0 && curr >= 0) {
			mPlaySeekbar.setMax(length);
			// mDuration = length;
			mPlaySeekbar.setProgress(curr);
			// mCurrenPlayTime = curr;
			String playTimer = null;
			String playEndTimer = null;
			if (!isRemote) {
				playTimer = millisToString(curr);
				playEndTimer = millisToString(length);
			} else {
				playTimer = AppUtil.intToTime(tv_play_index) + "";
				playEndTimer = AppUtil.intToTime(length) + "";
				if (tv_play_index > length) {
					mHandler.removeMessages(PLAY_TIME);
					tv_play_index = 0;
					LogUtils.print("removeMessages()");
					playTimer = AppUtil.intToTime(tv_play_index) + "";
					mPlaySeekbar.setProgress(tv_play_index);
					isPlayCom = true;
					isPlay = false;
				}
			}
			mPlayTime.setText(playTimer);
			mPlayDuration.setText(playEndTimer);

		}
	}

	public static String millisToString(long millis) {
		return millisToString(millis, false);
	}

	public static String millisToText(long millis) {
		return millisToString(millis, true);
	}

	public static long longToTime(long millis, long end) {
		java.util.Date now = new Date(end);
		java.util.Date date = new Date(millis);
		long l = now.getTime() - date.getTime();
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		System.out.println("" + day + "天" + hour + "小时" + min + "分" + s + "秒");
		return l;
	}

	public static int longToTimeAdd(long millis) {

		long l = millis;
		long day = l / (24 * 60 * 60 * 1000);
		long hour = (l / (60 * 60 * 1000) - day * 24);
		long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
		return (int) s;
	}

	private static String millisToString(long millis, boolean text) {
		boolean negative = millis < 0L;
		millis = Math.abs(millis);

		millis /= 1000L;
		int sec = (int) (millis % 60L);
		millis /= 60L;
		int min = (int) (millis % 60L);
		millis /= 60L;
		int hours = (int) millis;

		DecimalFormat format = (DecimalFormat) NumberFormat
				.getInstance(Locale.US);
		format.applyPattern("00");
		String time;
		if (text) {
			if (millis > 0L) {
				time = ((negative) ? "-" : "") + hours + "h"
						+ format.format(min) + "min";
			} else {
				if (min > 0)
					time = ((negative) ? "-" : "") + min + "min";
				else
					time = ((negative) ? "-" : "") + sec + "s";
			}
		} else {
			if (millis > 0L)
				time = ((negative) ? "-" : "") + hours + ":"
						+ format.format(min) + ":" + format.format(sec);
			else
				time = ((negative) ? "-" : "") + min + ":" + format.format(sec);
		}
		return time;
	}

	protected void updateUIPlayState() {
		// TODO Auto-generated method stub
		if (isRemote) {

		} else {
			if (mSunniplayer.isPlaying()) {
				mPlayPauseBtn.setBackgroundResource(R.drawable.img_pause_nor);
			} else {
				mPlayPauseBtn.setBackgroundResource(R.drawable.img_pause_play);
			}
		}
		updateUIPlayTime(0);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mSunniplayer.isPlaying()) {
			mSunniplayer.pause();
		}
		mSunniplayer.stopPlayback();
		mHandler.removeMessages(PLAY_TIME);

	}

}
