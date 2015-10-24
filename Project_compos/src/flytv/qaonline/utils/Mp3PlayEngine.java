package flytv.qaonline.utils;

import android.media.MediaPlayer;

public class Mp3PlayEngine {
	private String mPlayUrl;
	private MediaPlayer mMediaPlayer; 

	private static Mp3PlayEngine mMp3PlayEngine;

	public Mp3PlayEngine() {
		super();
		mMediaPlayer = new MediaPlayer();
	}

	public static Mp3PlayEngine getMp3PlayEngine(){
		if(mMp3PlayEngine == null){
			mMp3PlayEngine = new Mp3PlayEngine();
		}
		return mMp3PlayEngine;
	}
	
	public void playSound(String url){
		boolean canPlay = false;
		if(!mMediaPlayer.isPlaying()){
			canPlay = true;
		}else{
			if(!url.equals(mPlayUrl)){
				canPlay = true;
			}
		}
		if(canPlay){
			try {  
				mPlayUrl = url;
				mMediaPlayer.reset();//把各项参数恢复到初始状态  
				mMediaPlayer.setDataSource(url);  
				mMediaPlayer.prepare();  //进行缓冲  
				mMediaPlayer.start();
			}catch (Exception e) {  
				e.printStackTrace();  
			}  	
	}  
	}
	
    public void pause() {  
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {  
        	mMediaPlayer.pause();  
        }  
    }  
      
    public void stop(){  
        if(mMediaPlayer != null) {  
        	mMediaPlayer.stop();  
            try {  
            	mMediaPlayer.prepare(); 
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
    
    public void destroy() {  
        if(mMediaPlayer != null){  
        	mMediaPlayer.stop();  
        	mMediaPlayer.release();  
        }  
    }
}
