package flytv.compos.run.control.compos;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.PauseOnScrollListener;
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
import flytv.ext.view.MyGridView;
import flytv.ext.view.TabPageIndicator;
import flytv.ext.view.TabPageIndicator.TabOnItemTitleSelectClickLister;
import flytv.ext.view.XListView;
import flytv.ext.view.XListView.IXListViewListener;
import flytv.run.bean.TVCodeBean;

/**
 * 查看作业界面
 * 
 * @author nike
 * 
 */
public class AyComposChin extends BaseActivity implements IXListViewListener,
		TabOnItemTitleSelectClickLister {
	@ViewInject(R.id.layout_gridview)
	private LinearLayout layout_gridview;
	@ViewInject(R.id.layout_listview)
	private RelativeLayout layout_listview;

	@ViewInject(R.id.grid_not_send_view)
	private MyGridView grid_not_send_view;

	@ViewInject(R.id.grid_send_view)
	private MyGridView grid_send_view;
	@ViewInject(R.id.listView_pop)
	private XListView xListView;
	@ViewInject(R.id.layout_sercher)
	private LinearLayout layout_sercher;
	@ViewInject(R.id.pop_info_edit)
	private EditText pop_info_edit;
	@ViewInject(R.id.btn_sercher)
	private Button btn_sercher;

	@ViewInject(R.id.ivTitleBtnLeft)
	private ImageButton ivTitleBtnLeft;

	@ViewInject(R.id.ivTitleBtnRight)
	private Button ivTitleBtnRight;

	@ViewInject(R.id.button_grade)
	private Button button_grade;

	@ViewInject(R.id.ivTitleName)
	private TextView ivTitleName;

	@ViewInject(R.id.tabPageIndicator)
	private TabPageIndicator tabPageIndicator;
	private BitmapUtils bitmapUtils;

	private AdComposQuestion baseAdapter;
	private ArrayList<QuestionBean> items = new ArrayList<QuestionBean>();

	private TVCodeBean loginBean;
	private ComposBean composStudent;
	private StuComposBean msgCompos;

	@OnClick({ R.id.ivTitleBtnLeft, R.id.btn_sercher, R.id.ivTitleBtnRight,
			R.id.ivTitleName })
	public void onClickComment(View view) {
		switch (view.getId()) {
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		case R.id.ivTitleBtnRight:
			//
			Intent intent = new Intent(AyComposChin.this,
					AyComposChinMark.class);
			intent.putExtra("questionItem", items.get(0));
			intent.putExtra("msgCompos", msgCompos);
			intent.putExtra("composStudent", composStudent);
			startActivityForResult(intent, 100);
			break;
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		bitmapUtils = new BitmapUtils(this);
		button_grade.setVisibility(View.INVISIBLE);
		ivTitleBtnRight.setVisibility(View.VISIBLE);
		ivTitleBtnRight.setBackgroundResource(R.drawable.transparent);
		ivTitleName.setText(getString(R.string.app_tab_compos));
		String titleRigth = "";
		if (loginBean.getUserType().equals("1")) {
			if(composStudent.getMarkStatus()!=2){
				titleRigth = "批阅";
			}else{
				titleRigth = "查看批阅";
			}
			
		} else {
			if(composStudent.getMarkStatus()!=2){
				titleRigth = "答题";
			}else{
				titleRigth = "查看答题";
			}
			
		}
		ivTitleBtnRight.setText(titleRigth);
		xListView = (XListView) findViewById(R.id.listView_pop);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.setXListViewListener(this);
		xListView.setDivider(null);
		xListView.setDividerHeight(10);
		//
		xListView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,
				false, true));

		//
		String[] top_array = new String[] {
				getString(R.string.main_text_compos_mark_look),
				getString(R.string.main_text_compos_mark_student_submit),
				getString(R.string.main_text_compos_mark_student_list) };
		tabPageIndicator.init(top_array);
		tabPageIndicator.setTabOnItemTitleSelectClickLister(this);
		tabPageIndicator.initTab();
		tabPageIndicator.setVisibility(View.GONE);
		xListView = (XListView) findViewById(R.id.listView_pop);
		xListView.setXListViewListener(this);
		xListView.setPullRefreshEnable(false);
		xListView.setPullLoadEnable(false);
		xListView.setDivider(null);
		xListView.setDividerHeight(10);
		baseAdapter = new AdComposQuestion(AyComposChin.this, items, false);
		xListView.setDivider(null);
		xListView.setDividerHeight(2);
		xListView.setPadding(0, 0, 0, 0);
		xListView.setAdapter(baseAdapter);
		layout_gridview.setVisibility(View.GONE);
		layout_gridview.setVisibility(View.GONE);
		layout_listview.setVisibility(View.VISIBLE);
		if(items.size()<=0){
			showQuestion();
		}
	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.fragment_group);
		ViewUtils.inject(this);
		composStudent = (ComposBean) getIntent().getSerializableExtra(
				"composStudent");
		msgCompos = (StuComposBean) getIntent().getSerializableExtra(
				"msgCompos");
		items = (ArrayList<QuestionBean>) getIntent().getSerializableExtra(
				"questionItem");
		loginBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				context);
		if(items==null){
			items  = new ArrayList<QuestionBean>();
		}
		
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}
	void showQuestion() {
		int httpUrl = 0;
		httpUrl = R.string.compos_tab_all_answer_loadQuestion;
		showDataDialog();
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(httpUrl) + "?homeworkId=" + composStudent.getId()
				+ "&userId=" + loginBean.getUserId();
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
							CommonMsg	msgBean = JSON.parseObject(responseInfo.result,
									CommonMsg.class);
							if (msgBean != null) {
								items.clear();
								items.addAll(msgBean.items);
								baseAdapter.notifyDataSetChanged();
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

	QuestionBean itemLogin;

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 100:
			if (resultCode == RESULT_OK) {
				Intent intent =new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
			break;
		}
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub

	}

	@Override
	public void ItemClick(int index) {

	}

}
