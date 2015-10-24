
package com.baidu.android.voicedemo;

import java.util.List;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.widget.EditText;

import com.baidu.voicerecognition.android.Candidate;
import com.baidu.voicerecognition.android.DataUploader;
import com.baidu.voicerecognition.android.VoiceRecognitionClient;
import com.baidu.voicerecognition.android.VoiceRecognitionClient.VoiceClientStatusChangeListener;
import com.baidu.voicerecognition.android.VoiceRecognitionConfig;

import flytv.compos.run.R;

/**
 * ʹ�õײ�API��ʽʶ��Demo�������߿��Զ���UI����
 *
 * @author yangliang02
 */
public class ApiDemoActivity extends FragmentActivity {
    private ControlPanelFragment mControlPanel;

    private VoiceRecognitionClient mASREngine;

    /** ����ʶ���� */
    private boolean isRecognition = false;

    /** �������¼�� */
    private static final int POWER_UPDATE_INTERVAL = 100;

    /** ʶ��ص��ӿ� */
    private MyVoiceRecogListener mListener = new MyVoiceRecogListener();

    /** ���߳�Handler */
    private Handler mHandler;

    /**
     * ���չʾ
     */
    private EditText mResult = null;

    /**
     * ������������
     */
    private Runnable mUpdateVolume = new Runnable() {
        public void run() {
            if (isRecognition) {
                long vol = mASREngine.getCurrentDBLevelMeter();
                mControlPanel.volumeChange((int) vol);
                mHandler.removeCallbacks(mUpdateVolume);
                mHandler.postDelayed(mUpdateVolume, POWER_UPDATE_INTERVAL);
            }
        }
    };

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_demo_activity);
        mResult = (EditText) findViewById(R.id.recognition_text);
        mASREngine = VoiceRecognitionClient.getInstance(this);
        mASREngine.setTokenApis(Constants.API_KEY, Constants.SECRET_KEY);
        uploadContacts();
        mHandler = new Handler();
        mControlPanel = (ControlPanelFragment) (getSupportFragmentManager()
                .findFragmentById(R.id.control_panel));
        mControlPanel.setOnEventListener(new ControlPanelFragment.OnEventListener() {

            @Override
            public boolean onStopListening() {
                mASREngine.speakFinish();
                return true;
            }

            @Override
            public boolean onStartListening() {
                mResult.setText(null);
                VoiceRecognitionConfig config = new VoiceRecognitionConfig();
                config.setProp(Config.CURRENT_PROP);
                config.setLanguage(Config.getCurrentLanguage());
                config.enableContacts(); // ����ͨѶ¼
                config.enableVoicePower(Config.SHOW_VOL); // ����������
                if (Config.PLAY_START_SOUND) {
                    config.enableBeginSoundEffect(R.raw.bdspeech_recognition_start); // ����ʶ��ʼ��ʾ��
                }
                if (Config.PLAY_END_SOUND) {
                    config.enableEndSoundEffect(R.raw.bdspeech_speech_end); // ����ʶ�������ʾ��
                }
                config.setSampleRate(VoiceRecognitionConfig.SAMPLE_RATE_8K); // ���ò�����,��Ҫ���ⲿ��Ƶһ��
                // ���淢��ʶ��
                int code = mASREngine.startVoiceRecognition(mListener, config);
                if (code != VoiceRecognitionClient.START_WORK_RESULT_WORKING) {
                    mResult.setText(getString(R.string.error_start, code));
                }

                return code == VoiceRecognitionClient.START_WORK_RESULT_WORKING;
            }

            @Override
            public boolean onCancel() {
                mASREngine.stopVoiceRecognition();
                return true;
            }
        });
    };

    /**
     * ��д���ڴ�������ʶ��ص��ļ�����
     */
    class MyVoiceRecogListener implements VoiceClientStatusChangeListener {

        @Override
        public void onClientStatusChange(int status, Object obj) {
            switch (status) {
            // ����ʶ��ʵ�ʿ�ʼ����������ʼʶ���ʱ��㣬���ڽ�����ʾ�û�˵����
                case VoiceRecognitionClient.CLIENT_STATUS_START_RECORDING:
                    isRecognition = true;
                    mHandler.removeCallbacks(mUpdateVolume);
                    mHandler.postDelayed(mUpdateVolume, POWER_UPDATE_INTERVAL);
                    mControlPanel.statusChange(ControlPanelFragment.STATUS_RECORDING_START);
                    break;
                case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_START: // ��⵽�������
                    mControlPanel.statusChange(ControlPanelFragment.STATUS_SPEECH_START);
                    break;
                // �Ѿ���⵽�����յ㣬�ȴ����緵��
                case VoiceRecognitionClient.CLIENT_STATUS_SPEECH_END:
                    mControlPanel.statusChange(ControlPanelFragment.STATUS_SPEECH_END);
                    break;
                // ����ʶ����ɣ���ʾobj�еĽ��
                case VoiceRecognitionClient.CLIENT_STATUS_FINISH:
                    mControlPanel.statusChange(ControlPanelFragment.STATUS_FINISH);
                    isRecognition = false;
                    updateRecognitionResult(obj);
                    break;
                // ������������
                case VoiceRecognitionClient.CLIENT_STATUS_UPDATE_RESULTS:
                    updateRecognitionResult(obj);
                    break;
                // �û�ȡ��
                case VoiceRecognitionClient.CLIENT_STATUS_USER_CANCELED:
                    mControlPanel.statusChange(ControlPanelFragment.STATUS_FINISH);
                    isRecognition = false;
                    break;
                default:
                    break;
            }

        }

        @Override
        public void onError(int errorType, int errorCode) {
            isRecognition = false;
            mResult.setText(getString(R.string.error_occur, Integer.toHexString(errorCode)));
            mControlPanel.statusChange(ControlPanelFragment.STATUS_FINISH);
        }

        @Override
        public void onNetworkStatusChange(int status, Object obj) {
            // ���ﲻ���κβ�����Ӱ���ʶ��
        }
    }

    /**
     * ��ʶ������µ�UI�ϣ�����ģʽ�������ΪList<String>,����ģʽ�������ΪList<List<Candidate>>
     *
     * @param result
     */
    private void updateRecognitionResult(Object result) {
        if (result != null && result instanceof List) {
            List results = (List) result;
            if (results.size() > 0) {
                if (results.get(0) instanceof List) {
                    List<List<Candidate>> sentences = (List<List<Candidate>>) result;
                    StringBuffer sb = new StringBuffer();
                    for (List<Candidate> candidates : sentences) {
                        if (candidates != null && candidates.size() > 0) {
                            sb.append(candidates.get(0).getWord());
                        }
                    }
                    mResult.setText(sb.toString());
                } else {
                    mResult.setText(results.get(0).toString());
                }
            }
        }
    }

    /**
     * �ϴ�ͨѶ¼
     * */
    private void uploadContacts(){
    	DataUploader dataUploader = new DataUploader(ApiDemoActivity.this);
    	dataUploader.setApiKey(Constants.API_KEY, Constants.SECRET_KEY);

    	String jsonString = "[{\"name\":\"��ά\", \"frequency\":1}, {\"name\":\"������\", \"frequency\":2}]";
    	try{
    		dataUploader.uploadContactsData(jsonString.getBytes("utf-8"));
    	}catch (Exception e){
    		e.printStackTrace();
    	}
    }
}
