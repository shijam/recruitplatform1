package flytv.compos.run.control.compos.user;

import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import flytv.compos.run.adapter.AdSocreSort;
import flytv.compos.run.bean.CirclePageBean;
import flytv.compos.run.bean.ScoreSrotBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.UserShareUtils;
import flytv.ext.view.TabPageIndicator;
import flytv.ext.view.TabPageIndicator.TabOnItemTitleSelectClickLister;
import flytv.ext.view.XListView;
import flytv.ext.view.XListView.IXListViewListener;
import flytv.run.bean.TVCodeBean;

public class AyUserSort extends BaseActivity implements
		TabOnItemTitleSelectClickLister, IXListViewListener {

	@ViewInject(R.id.ivTitleBtnLeft)
	private ImageButton ivTitleBtnLeft;

	@ViewInject(R.id.ivTitleBtnRight)
	private Button ivTitleBtnRight;

	@ViewInject(R.id.button_grade)
	private Button button_grade;

	@ViewInject(R.id.ivTitleName)
	private TextView ivTitleName;

	@ViewInject(R.id.tx_name)
	private TextView tx_name;

	@ViewInject(R.id.tx_score)
	private TextView tx_score;

	private TVCodeBean tvCodeBean;

	@ViewInject(R.id.listView_pop)
	private XListView xListView;

	@ViewInject(R.id.tabPageIndicator)
	private TabPageIndicator tabPageIndicator;

	private AdSocreSort baseAdapter;
	@OnClick({ R.id.ivTitleBtnLeft, R.id.ivTitleBtnRight, R.id.ivTitleName,
			R.id.layout_info, R.id.layout_collection_abole })
	public void onClickComment(View view) {
		switch (view.getId()) {
		// 添加评论
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		}
	}
	private BitmapUtils bitmapUtils;
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		button_grade.setVisibility(View.INVISIBLE);
		ivTitleBtnRight.setVisibility(View.INVISIBLE);
		ivTitleName.setText(getString(R.string.app_user_sort_score));
		
		String[] top_array = new String[] {
				getString(R.string.main_text_compos_user_sort1),
				getString(R.string.main_text_compos_user_sort2) };
		tabPageIndicator.init(top_array);
		tabPageIndicator.setTabOnItemTitleSelectClickLister(this);
		tabPageIndicator.initTab();
		tabPageIndicator.setVisibility(View.VISIBLE);
		xListView = (XListView) findViewById(R.id.listView_pop);
		xListView.setXListViewListener(this);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,
				false, true));
		xListView.setDivider(null);
		xListView.setDividerHeight(10);
		baseAdapter = new AdSocreSort(this, items,bitmapUtils);
		xListView.setAdapter(baseAdapter);

	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.fragment_group);
		ViewUtils.inject(this);
		tvCodeBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				this);
		bitmapUtils = new BitmapUtils(context);

	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

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
		// TODO Auto-generated method stub
		if(index==0){
			httpUrl = R.string.compos_user_school;
		}else{
			httpUrl = R.string.compos_user_region;
		}
		ref(0);
	}
	ArrayList<ScoreSrotBean> items = new ArrayList<ScoreSrotBean>();
	int httpUrl;
	private int page = 1, pageSize = 15, index;

	void ref(final int typeId) {
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
				+ getString(httpUrl) + "?schoolId=" + tvCodeBean.getSchoolId()
				+ "&circleType=1" + "&currentPage=" + page + "&pageSize="
				+ pageSize ;
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
						onLoad();
						try {
							LogUtils.print("responseInfo ="
									+ responseInfo.result);
							if (typeId != 2) {
								items.clear();
							}
							CirclePageBean pageBean = null;
							pageBean = JSON.parseObject(responseInfo.result,
									CirclePageBean.class);
							ArrayList<ScoreSrotBean> comoBean = (ArrayList<ScoreSrotBean>) JSON.parseArray(pageBean.getItems(),ScoreSrotBean.class);
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
						} catch (Exception e) {
							e.printStackTrace();
							AlertTools.showTips(context, R.drawable.tips_warning, "数据解析错误");
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

}
