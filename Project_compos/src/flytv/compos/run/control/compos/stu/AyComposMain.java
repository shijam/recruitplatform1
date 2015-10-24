package flytv.compos.run.control.compos.stu;

import java.util.ArrayList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import flytv.compos.run.adapter.stu.AdStuComposMa;
import flytv.compos.run.adapter.stu.AdStuComposMa.QAListItemListener;
import flytv.compos.run.bean.CirclePageBean;
import flytv.compos.run.bean.CommonMsg;
import flytv.compos.run.bean.ComposBean;
import flytv.compos.run.bean.QuestionBean;
import flytv.compos.run.control.compos.AyComposChin;
import flytv.compos.run.control.compos.AyComposPDF;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.UserShareUtils;
import flytv.ext.view.TabPageIndicator;
import flytv.ext.view.XListView;
import flytv.ext.view.XListView.IXListViewListener;
import flytv.run.bean.TVCodeBean;

public class AyComposMain extends BaseActivity implements IXListViewListener,
		QAListItemListener {
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
	private ArrayList<ComposBean> items = new ArrayList<ComposBean>();
	private AdStuComposMa baseAdapter;

	@OnClick({ R.id.ivTitleBtnLeft })
	public void onView(View view) {
		switch (view.getId()) {
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		button_grade.setVisibility(View.INVISIBLE);
		ivTitleBtnRight.setVisibility(View.GONE);
		xListView = (XListView) findViewById(R.id.listView_pop);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.setXListViewListener(this);
		Drawable drawable = getResources().getDrawable(R.drawable.sc_linebg);
		xListView.setDivider(drawable);
		xListView.setDividerHeight(1);
		xListView.setPadding(10, 10, 10, 10);
		layout_sercher.setVisibility(View.VISIBLE);

		String[] top_array = new String[] {
				getString(R.string.main_text_compos_mark_look),
				getString(R.string.main_text_compos_mark_student_submit),
				getString(R.string.main_text_compos_mark_student_list) };
		tabPageIndicator.init(top_array);
		tabPageIndicator.initTab();
		tabPageIndicator.setVisibility(View.GONE);
		//
		baseAdapter = new AdStuComposMa(this, items, this);
		xListView.setAdapter(baseAdapter);
		ivTitleName.setText(getString(R.string.app_tab_compos));
	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.fragment_group);
		ViewUtils.inject(this);
		loginInfo = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				context);
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

		ref(0);
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemClickListener(int position) {
		// TODO Auto-generated method stub
		//
		clickIntent(position);
	}

	void clickIntent(int position) {

		ComposBean entity = items.get(position);
		showQuestion(entity);

	}

	void showQuestion(final ComposBean entity) {
		int httpUrl = 0;
		httpUrl = R.string.compos_tab_all_answer_loadQuestion;
		showDataDialog();
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(httpUrl) + "?homeworkId=" + entity.getId()
				+ "&userId=" + loginInfo.getUserId();
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
								AppUtil.UPLOADPATH = msgBean.getFileService();
								ArrayList<QuestionBean> itemsList = msgBean.items;
								int questInt = itemsList.get(0)
										.getQuestionType();
								Intent intent = new Intent();
								if (questInt == 11) {
									// pdf
									intent.setClass(AyComposMain.this,
											AyComposPDF.class);
								} else if ((entity.getSubjectName().equals("语文")||entity.getSubjectName().equals("英语"))
										&& questInt == 10) {
									// 作文分类
									intent.setClass(AyComposMain.this,
											AyComposChin.class);
								} else {
									intent.setClass(AyComposMain.this,
											AyComposStuAnswer.class);
								}
								intent.putExtra("composStudent", entity);
								startActivityForResult(intent, 100);

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
	public void onRefresh() {
		// TODO Auto-generated method stub
		ref(1);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		ref(2);
	}

	int httpUrl;
	private int page = 1, pageSize = 15, index;
	TVCodeBean loginInfo;

	void ref(final int typeId) {
		httpUrl = R.string.compos_tab_all_list;
		if (typeId == 0) {
			page = 1;
			index = 0;
		} else if (typeId == 1) {
			index = 0;
			page = 1;
		} else if (typeId == 2) {
			index = items.size();
		}
		showDataDialog();
		if (!AppUtil.isNetWork(context)) {
			closeDataDialog();
			onLoad();
			return;
		}
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(httpUrl) + "?userId=" + loginInfo.getUserId()
				+ "&circleType=1" + "&currentPage=" + page + "&pageSize="
				+ pageSize;
		// + "&week=" + seleTypeIndex;
		LogUtils.print(1, "urlString" + urlString);
		HttpUtils http = new HttpUtils();
		http.configTimeout(10000);
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
							LogUtils.print("responseInfo ="
									+ responseInfo.result);
							if (typeId != 2) {
								items.clear();
							}
							CirclePageBean pageBean = null;
							pageBean = JSON.parseObject(responseInfo.result,
									CirclePageBean.class);
							ArrayList<ComposBean> comoBean = (ArrayList<ComposBean>) JSON
									.parseArray(pageBean.getItems(),
											ComposBean.class);
							items.addAll(comoBean);
							baseAdapter.notifyDataSetChanged();
							xListView.setSelection(index);
							if (pageBean != null) {
								if (pageBean.getCurrentPage() < pageBean
										.getTotalPage()) {
									xListView.setPullLoadEnable(true);
									page++;
								} else {
									xListView.setPullLoadEnable(false);
								}
							}
							onLoad();
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

	private void onLoad() {
		xListView.stopRefresh();
		xListView.stopLoadMore();
		xListView.setRefreshTime("刚刚");

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch(requestCode){
		case 100:
			if(resultCode==RESULT_OK){
				ref(0);
			}
			break;
		}
	}
	

}
