package flytv.compos.run.control.compos;

import java.util.ArrayList;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import flytv.compos.run.adapter.AdStuCompos;
import flytv.compos.run.adapter.AdStudent;
import flytv.compos.run.bean.CommonMsg;
import flytv.compos.run.bean.ComposBean;
import flytv.compos.run.bean.QuestionBean;
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
public class AyComposStuden extends BaseActivity implements IXListViewListener,
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
	private ArrayList<QuestionBean> compos_items = new ArrayList<QuestionBean>();

	private TVCodeBean loginBean;
	private ComposBean composStudent;

	@OnClick({ R.id.ivTitleBtnLeft, R.id.btn_sercher, R.id.ivTitleBtnRight,
			R.id.ivTitleName })
	public void onClickComment(View view) {
		switch (view.getId()) {
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		case R.id.ivTitleBtnRight:
			break;
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		bitmapUtils = new BitmapUtils(this);
		button_grade.setVisibility(View.INVISIBLE);
		ivTitleBtnRight.setVisibility(View.INVISIBLE);
		ivTitleName.setText(getString(R.string.app_tab_compos));

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
		tabPageIndicator.setVisibility(View.VISIBLE);
		xListView = (XListView) findViewById(R.id.listView_pop);
		xListView.setXListViewListener(this);
		xListView.setPullRefreshEnable(false);
		xListView.setPullLoadEnable(false);
		xListView.setDivider(null);
		xListView.setDividerHeight(10);

	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.fragment_group);
		ViewUtils.inject(this);
		composStudent = (ComposBean) getIntent().getSerializableExtra(
				"composStudent");
		loginBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				context);
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	QuestionBean itemLogin;

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		xListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (tabIndex == 1) {
					int position = arg2 - 1;
					Intent intent = new Intent(AyComposStuden.this,
							AyComposMarkStu.class);
					// 这儿需要加上普通作业 PDF
					//

					if (itemLogin.getQuestionType() == 11) {
						intent = new Intent(AyComposStuden.this,
								AyComposPDF.class);
					} else if ((composStudent.getSubjectName().equals("语文") || composStudent
							.getSubjectName().equals("英语"))
							&& itemLogin.getQuestionType() == 10) {
						intent = new Intent(AyComposStuden.this,
								AyComposChin.class);
						intent.putExtra("questionItem", compos_items);
					} else {
						intent = new Intent(AyComposStuden.this,
								AyComposMarkStu.class);
					}
					intent.putExtra("msgCompos",
							msgBean.studentList.get(position));
					intent.putExtra("composStudent", composStudent);
					startActivityForResult(intent, 100);

				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 100:
			showQuestion();
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

	private int tabIndex = 0;

	@Override
	public void ItemClick(int index) {
		// TODO Auto-generated method stub
		if (tabIndex != 0 && items.size() > 0) {
			// 分别切换视图
			this.tabIndex = index;
			switchView();
			return;
		}
		this.tabIndex = index;
		showQuestion();

	}

	void switchView() {
		if (tabIndex == 2) {
			layout_gridview.setVisibility(View.VISIBLE);
			layout_listview.setVisibility(View.GONE);
			grid_not_send_view.setAdapter(new AdStudent(
					msgBean.noCommitNameList, AyComposStuden.this));
			grid_send_view.setAdapter(new AdStudent(msgBean.commitNameList,
					AyComposStuden.this));
		} else {
			layout_gridview.setVisibility(View.GONE);
			layout_listview.setVisibility(View.VISIBLE);
			xListView.setDivider(null);
			xListView.setDividerHeight(10);
			xListView.setPadding(10, 10, 10, 10);
			xListView.setAdapter(new AdStuCompos(msgBean.studentList, context));
		}

	}

	private CommonMsg msgBean;

	void showQuestion() {
		int httpUrl = 0;
		if (tabIndex == 0) {
			httpUrl = R.string.compos_tab_all_answer_loadQuestion;
		} else {
			httpUrl = R.string.compos_tab_all_answer_listt;

		}
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
							msgBean = JSON.parseObject(responseInfo.result,
									CommonMsg.class);
							if (msgBean != null) {
								items.clear();
								items.addAll(msgBean.items);
								if (tabIndex == 0) {
									init();
									compos_items.clear();
									compos_items.addAll(items);
									itemLogin = items.get(0);
									baseAdapter = new AdComposQuestion(AyComposStuden.this,
											items, false);
									xListView.setDivider(null);
									xListView.setDividerHeight(2);
									xListView.setPadding(0, 0, 0, 0);
									xListView.setAdapter(baseAdapter);
									layout_gridview.setVisibility(View.GONE);
									layout_gridview.setVisibility(View.GONE);
									layout_listview.setVisibility(View.VISIBLE);

								} else {
									//
									switchView();
								}
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

	void init() {
		int typePersentIndex = 0, typeIndex = 0;
		for (int i = 0; i < this.items.size(); i++) {
			QuestionBean entity = items.get(i);
			boolean isShow = false;
			if (i == 0) {
				typePersentIndex = 0;
				isShow = true;
			} else {
				//
				if (entity.getQuestionType() != items.get(i - 1)
						.getQuestionType()) {
					typeIndex++;
					isShow = true;
					typePersentIndex = 0;
				} else {
					// typeIndex++;
					isShow = false;
					typePersentIndex++;
					//
				}

			}
			entity.setShowType(isShow);
			entity.setComposType(typeIndex);
			entity.setComposTypeSum(typePersentIndex);
		}
	}

}
