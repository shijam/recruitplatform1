package flytv.compos.run.control.compos;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import flytv.compos.run.R;
import flytv.compos.run.adapter.AdComposQuestion;
import flytv.compos.run.bean.CommonMsg;
import flytv.compos.run.bean.ComposBean;
import flytv.compos.run.bean.QuestionBean;
import flytv.compos.run.bean.StuComposBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.UserShareUtils;
import flytv.run.bean.TVCodeBean;
/**
 * 查看pdf (student/tearcher) 
 * @author nike
 */
public class AyComposPDF extends BaseActivity   {

    
    Integer pageNumber = 1;
	@ViewInject(R.id.ivTitleBtnLeft)
	private ImageButton ivTitleBtnLeft;

	@ViewInject(R.id.ivTitleBtnRight)
	private Button ivTitleBtnRight;

	@ViewInject(R.id.button_grade)
	private Button button_grade;

	@ViewInject(R.id.ivTitleName)
	private TextView ivTitleName;
	@ViewInject(R.id.tx_right)
	private TextView tx_right;

	private TVCodeBean loginInfo;
	private ComposBean composStudent;
	private StuComposBean msgCompos;
	@OnClick({ R.id.ivTitleBtnLeft, R.id.btn_sercher, R.id.tx_right,
			R.id.btn_compos_add, R.id.ed_class, R.id.ed_subject })
	public void onClickComment(View view) {
		switch (view.getId()) {
		// 添加评论
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		case R.id.tx_right:
			// 提交
			sendCompos();
			break;
		}
	}
	void sendCompos (){
		Intent intent = new Intent(AyComposPDF.this,AyComposPDFMark.class);
		if(items.size()>0){
			QuestionBean answerBean = items.get(0);
			intent.putExtra("answerBean", answerBean);
		}
		
		intent.putExtra("msgCompos", msgCompos);
		intent.putExtra("composStudent", composStudent);
		startActivityForResult(intent, 100);
		
	}
	private AdComposQuestion baseAdapter;
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		String rightTitle = null;
		if(loginInfo.getUserType().equals("2")){
			if(composStudent.getMarkStatus()==0){
				rightTitle = "我要作答";
			}else if(composStudent.getMarkStatus()==1){
				rightTitle = "查看回答";
			}else if(composStudent.getMarkStatus()==2){
				rightTitle = "查看批阅";
			}
		}else if(loginInfo.getUserType().equals("1")){
			if(composStudent.getMarkStatus()==0){
				rightTitle = "我要批阅";
			}else if(composStudent.getMarkStatus()==1){
				rightTitle = "我要批阅";
			}else if(composStudent.getMarkStatus()==2){
				rightTitle = "查看批阅";
			}
		}
		tx_right.setText(rightTitle);
		baseAdapter = new AdComposQuestion(context,
				items, false);
		
	}
	

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.ad_compos_pdf);
		ViewUtils.inject(this);
		loginInfo = (TVCodeBean)UserShareUtils.getInstance().getLoginInfo(context);
		button_grade.setVisibility(View.INVISIBLE);
		tx_right.setVisibility(View.VISIBLE);
		ivTitleBtnRight.setVisibility(View.GONE);
		ivTitleName.setText(getString(R.string.app_tab_compos));
		composStudent = (ComposBean)getIntent().getSerializableExtra("composStudent");
		msgCompos = (StuComposBean)getIntent().getSerializableExtra("msgCompos");
		
	}
	ArrayList<QuestionBean> items = new ArrayList<QuestionBean>();
	void showQuestion() {
		showDataDialog();
		int homeworkId = composStudent.getId();
		// 是否是编辑
		int userId  = 0 ;
		if(msgCompos!=null){
			userId = Integer.parseInt(msgCompos.getStudentId());
		}else{
			userId = Integer.parseInt(loginInfo.getUserId());
		}
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(R.string.compos_tab_all_answer_loadQuestion)
				+ "?homeworkId=" + homeworkId + "&userId="
				+ userId;
		LogUtils.print(1, "urlString" + urlString);
		HttpUtils http = new HttpUtils();
		http.configTimeout(15000);
		http.configCurrentHttpCacheExpiry(2000);
		http.send(HttpRequest.HttpMethod.GET, urlString,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						closeDataDialog();
						try {
							LogUtils.print("" + responseInfo.result);

							CommonMsg msgBean = JSON.parseObject(
									responseInfo.result, CommonMsg.class);
							if (msgBean != null) {
								items.clear();
								items.addAll(msgBean.items);
								baseAdapter.notifyDataSetChanged();
								// 显示pdf
								// admin
							} else {
								AlertTools.showTips(context,
										R.drawable.tips_error, "获取失败!");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						closeDataDialog();
						LogUtils.print("" + msg);
						AppUtil.isNetWork(context);
					}
				});
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub
		if(composStudent.getMarkStatus()==0){
			showQuestion();
		}
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case 100:
			if(resultCode==RESULT_OK){
				if(composStudent.getMarkStatus()==0){
					showQuestion();
				}
			}
			break;
		}
	}

}
