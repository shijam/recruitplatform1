
package com.baidu.android.voicedemo;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.baidu.voicerecognition.android.ui.DialogRecognitionListener;

import flytv.compos.run.R;

/**
 * ʶ��Ի�����ʾ��Demo��ͨ�� {@link BaiduASRDigitalDialog#getParams()}��ȡ��ʶ�����,����
 * Bundle.putStringArray({@link BaiduASRDigitalDialog#PARAM_TIPS}, String[])
 * ����������ʾ���б?������ʾ�����ʶ��Ի������Ͻǻ��С�������ť���������ʾ��ʾ���б?������ʾ�������ͨ��
 * {@link Bundle#putBoolean(String, boolean)}�����������²������ع���
 * <table border="1">
 * <tr>
 * <th>������</th>
 * <th>��������</th>
 * <th>Ĭ��ֵ</th>
 * <th>����</th>
 * </tr>
 * <tr>
 * <td>{@link BaiduASRDigitalDialog#PARAM_SHOW_TIPS_ON_START}</td>
 * <td>boolean</td>
 * <td>false</td>
 * <td>�Ի�����ʾʱ������ʶ��չʾ��ʾ���б�</td>
 * </tr>
 * <tr>
 * <td>{@link BaiduASRDigitalDialog#PARAM_SHOW_TIP}</td>
 * <td>boolean</td>
 * <td>false</td>
 * <td>ʶ������3���δ��⵽����������Ч�����·�չʾһ����ʾ��</td>
 * </tr>
 * <tr>
 * <td>{@link BaiduASRDigitalDialog#PARAM_SHOW_HELP_ON_SILENT}</td>
 * <td>boolean</td>
 * <td>false</td>
 * <td>����δ��⵽�������쳣����ʱ���滻ȡ��ťΪ����ť���û������չʾ��ʾ���б�</td>
 * </tr>
 * </table>
 *
 * @author yangliang02
 */
public class DialogTipsDemoActivity extends PreferenceActivity {
    private static final String SHOW_TIPS_ONSTART = "dialog_show_tips_onstart";

    private static final String SHOW_TIP_ONSILENT = "dialog_show_tip_onsilent";

    private static final String SHOW_HELP_ONSILENT = "dialog_show_help_onsilent";

    private static final int RECOGNITION_DIALOG = 1;

    private static final String INTENT_ACTION_START = "baidu.voicedemo.intent.action.START";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(getApplication(), R.xml.dialog_tips, false);
        addPreferencesFromResource(R.xml.dialog_tips);
        startRecognition(getIntent());
    }

    private void startRecognition(Intent intent) {
        if (INTENT_ACTION_START.equals(intent.getAction())) {
            showDialog(RECOGNITION_DIALOG);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startRecognition(intent);
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        if (id == RECOGNITION_DIALOG) {
            Bundle params = new Bundle();
            params.putString(BaiduASRDigitalDialog.PARAM_API_KEY, Constants.API_KEY);
            params.putString(BaiduASRDigitalDialog.PARAM_SECRET_KEY, Constants.SECRET_KEY);
            params.putInt(BaiduASRDigitalDialog.PARAM_DIALOG_THEME, Config.DIALOG_THEME);
            BaiduASRDigitalDialog mDialog = new BaiduASRDigitalDialog(this, params);
            mDialog.setDialogRecognitionListener(new DialogRecognitionListener() {

                @Override
                public void onResults(Bundle arg0) {
                    // TODO Auto-generated method stub

                }
            });
            return mDialog;
        }
        return super.onCreateDialog(id);
    }

    @Override
    @Deprecated
    protected void onPrepareDialog(int id, Dialog dialog) {
        if (id == RECOGNITION_DIALOG) {
            BaiduASRDigitalDialog mDialog = (BaiduASRDigitalDialog) dialog;
            // ������ʾ���б?������ʾ�����ʶ��Ի������Ͻǻ��С�������ť���������ʾ��ʾ���б?
            mDialog.getParams().putStringArray(BaiduASRDigitalDialog.PARAM_TIPS,
                    getResources().getStringArray(R.array.command_tips));
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(getApplication());
            // ����ΪTrue���Ի�����ʾʱ������ʶ��չʾ��ʾ���б�
            mDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_SHOW_TIPS_ON_START,
                    preferences.getBoolean(SHOW_TIPS_ONSTART, false));
            // ����ΪTrue��ʶ������3���δ��⵽����������Ч�����·�չʾһ����ʾ��
            mDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_SHOW_TIP,
                    preferences.getBoolean(SHOW_TIP_ONSILENT, false));
            // ����ΪTrue��δ��⵽�������쳣����ʱ���滻ȡ��ťΪ����ť���û������չʾ��ʾ���б?
            mDialog.getParams().putBoolean(BaiduASRDigitalDialog.PARAM_SHOW_HELP_ON_SILENT,
                    preferences.getBoolean(SHOW_HELP_ONSILENT, false));
        }
        super.onPrepareDialog(id, dialog);
    }
}
