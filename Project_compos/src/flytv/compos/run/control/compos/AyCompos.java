package flytv.compos.run.control.compos;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import flytv.compos.run.adapter.AdCompos;
import flytv.compos.run.bean.CirclePageBean;
import flytv.compos.run.bean.ComposBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.UserShareUtils;
import flytv.ext.view.XListView;
import flytv.ext.view.XListView.IXListViewListener;
import flytv.ext.view.inter.ItemCommentClickLister;
import flytv.run.bean.MsgBean;
import flytv.run.bean.StuBean;
import flytv.run.bean.TVCodeBean;

public class AyCompos extends BaseActivity implements IXListViewListener,
		ItemCommentClickLister {

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

	private BitmapUtils bitmapUtils;
	private AdCompos baseAdapter;
	private ArrayList<ComposBean> items = new ArrayList<ComposBean>();

	private int position, commendId;
	private int seleIndex = 0;
	private List<StuBean> itemListStu = new ArrayList<StuBean>();

	@OnClick({ R.id.ivTitleBtnLeft, R.id.btn_sercher, R.id.ivTitleBtnRight,
			R.id.ivTitleName })
	public void onClickComment(View view) {
		switch (view.getId()) {
		// 添加评论
		case R.id.btn_sercher:
			String ediString = pop_info_edit.getText().toString().trim();
			int logId = items.get(position).getId();
			this.onBottomEdit(ediString, logId + "", position, commendId);
			break;
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		case R.id.ivTitleBtnRight:
			Intent intent = new Intent(context, AyComposAdd.class);
			// 选择要发布的
			intent.putExtra("isInit", true);
			intent.putExtra("SubjectItem", itemListStu.get(seleIndex));
			startActivityForResult(intent, 100);
			break;
		}
	}
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			 final ComposBean entity = items.get(arg2-1);
			if(entity.getStatus() !=2){
				return;
			}
			Intent intent =new Intent(AyCompos.this,AyComposStuden.class);
			intent.putExtra("composStudent", entity);
			startActivity(intent);

		}
	};

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

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		bitmapUtils = new BitmapUtils(this);
		button_grade.setVisibility(View.INVISIBLE);
		ivTitleName.setText(getString(R.string.app_tab_compos));
		xListView = (XListView) findViewById(R.id.listView_pop);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(onItemClickListener);
		xListView.setDivider(null);
		xListView.setDividerHeight(10);
		xListView.setPadding(10, 10, 10, 10);
		//
		xListView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,
				false, true));
		baseAdapter = new AdCompos(this, items);
		baseAdapter.setItemCommentClickLister(this);
		xListView.setAdapter(baseAdapter);
		itemListStu = loginInfo.subjectList;
	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.ad_friend);
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
							AppUtil.UPLOADPATH = pageBean.getFileService();
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
	public void onBottomComment(int position, int commendId) {
		// TODO Auto-generated method stub
		this.position = position;
		this.commendId = commendId;
		if (layout_sercher.getVisibility() == View.GONE) {
			layout_sercher.setVisibility(View.VISIBLE);
		} else {
			layout_sercher.setVisibility(View.GONE);
		}
	}

	@Override
	public void onBottomEdit(String message, String logId, final int index,
			int commendId) {

	}

	@Override
	public void onBottomPraise(final int position) {

	}

	public void onBottomDeleLog(final int position) {
		final ComposBean circle = items.get(position);
		showDataDialog();
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(R.string.compos_tab_all_item_dele) + "?id="
				+ circle.getId();
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
							LogUtils.print("" + responseInfo.result);
							MsgBean msgBean = JSON.parseObject(
									responseInfo.result, MsgBean.class);
							if (msgBean.getMessage().equals("1")) {
								items.remove(position);
								baseAdapter.notifyDataSetChanged();
							} else {
								AlertTools.showTips(context,
										R.drawable.tips_error,
										msgBean.getMsgInfo());
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
	public void onBottomEditLog(int position) {
		// TODO Auto-generated method stub
		// compos_tab_all_item_info
		showQusetion(position, 1);
	}

	public void showQusetion(int position, final int executeIndex) {
		final ComposBean entity = items.get(position);
		showDataDialog();
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(R.string.compos_tab_all_item_info) + "?id="
				+ entity.getId() + "&userId=" + loginInfo.getUserId();
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
							ComposBean msgBean = JSON.parseObject(
									responseInfo.result, ComposBean.class);
							if (msgBean != null) {
								if (executeIndex == 1) {
									Intent intent = new Intent(context,
											AyComposAdd.class);
									// 选择要发布的
									intent.putExtra("isInit", false);
									intent.putExtra("composItem", msgBean);
									startActivityForResult(intent, 100);
								} else {
									// 查看发布的信息
								}
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 100:
			if (resultCode == RESULT_OK) {
				ref(0);
			}
			break;
		}
	}

}
