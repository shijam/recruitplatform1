package flytv.compos.run;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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

import flytv.compos.run.adapter.AdFriend;
import flytv.compos.run.bean.Circle;
import flytv.compos.run.bean.CircleComment;
import flytv.compos.run.bean.CirclePageBean;
import flytv.compos.run.bean.CommonMsg;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AlertHelp;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.GeneralView;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.UserShareUtils;
import flytv.ext.view.XListView;
import flytv.ext.view.XListView.IXListViewListener;
import flytv.ext.view.inter.ItemCommentClickLister;
import flytv.ext.view.inter.OnClickDialogListener;
import flytv.ext.view.inter.PopWindowItemClickLister;
import flytv.run.bean.MsgBean;
import flytv.run.bean.StuBean;
import flytv.run.bean.TVCodeBean;

public class AyFriendUser extends BaseActivity implements IXListViewListener,
		ItemCommentClickLister ,OnClickDialogListener{

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
	private AdFriend baseAdapter;
	private ArrayList<Circle> items = new ArrayList<Circle>();

	private int position, commendId;
	private PopupWindow popupWindow;
	private int seleIndex = 0;
	private List<StuBean> itemListStu = new ArrayList<StuBean>();
	private Circle circle;
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
			Intent intent = new Intent(context, AyFriendAdd.class);
			// 选择要发布的
			intent.putExtra("SubjectItem", itemListStu.get(seleIndex));
			startActivityForResult(intent, 100);
			break;
		case R.id.ivTitleName:
			String[] array_str = new String[itemListStu.size()];
			for (int i = 0; i < itemListStu.size(); i++) {
				array_str[i] = itemListStu.get(i).getName();
			}
			popupWindow = GeneralView.makePopupWindow(this, seleIndex,
					array_str, popWindowItemClickLister);
			int[] xy = new int[2];
			ivTitleName.getLocationOnScreen(xy);
			popupWindow.showAtLocation(ivTitleName, Gravity.CENTER
					| Gravity.TOP, xy[0] / 2, xy[1] + ivTitleName.getHeight()
					+ 5);
			break;
		}
	}

	private PopWindowItemClickLister popWindowItemClickLister = new PopWindowItemClickLister() {

		@Override
		public void onItemClick(int i) {
			popupWindow.dismiss();
			if (seleIndex != i) {
				seleIndex = i;
				ivTitleName.setText(itemListStu.get(i).getName());
				ref(0);
			}
		}
	};
	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {

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
		xListView = (XListView) findViewById(R.id.listView_pop);
		xListView.setPullRefreshEnable(true);
		xListView.setPullLoadEnable(false);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(onItemClickListener);
		xListView.setOnScrollListener(new PauseOnScrollListener(bitmapUtils,
				false, true));
		baseAdapter = new AdFriend(this, items, bitmapUtils);
		baseAdapter.setUser(true);
		baseAdapter.setItemCommentClickLister(this);
		xListView.setAdapter(baseAdapter);
		itemListStu = loginInfo.subjectList;
		ivTitleName.setText(circle.getUserName());
		ivTitleBtnRight.setVisibility(View.GONE);
		button_grade.setVisibility(View.INVISIBLE);
	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.ad_friend);
		ViewUtils.inject(this);
		loginInfo = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				context);
		circle = (Circle)getIntent().getSerializableExtra("circle");
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
		httpUrl = R.string.compos_circle_user_list;
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

		StuBean stuBean = itemListStu.get(seleIndex);
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(httpUrl) + "?userId=" +circle.getUserId()
				+ "&circleType=1" + "&currentPage=" + page + "&pageSize="
				+ pageSize + "&subjectName=" + stuBean.getName();
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
							ArrayList<Circle> entity = (ArrayList<Circle>) JSON
									.parseArray(pageBean.getItems(),
											Circle.class);
							items.addAll(entity);
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
			InputMethodManager imm = (InputMethodManager) pop_info_edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
			pop_info_edit.requestFocus();
		} else {
			layout_sercher.setVisibility(View.GONE);
			pop_info_edit.clearFocus();
		}
	}

	@Override
	public void onBottomEdit(String message, String logId, final int index,
			int commendId) {
		// TODO Auto-generated method stub
		if (AppUtil.isStrNull(message)) {
			layout_sercher.setVisibility(View.GONE);
		} else {
			int replyId = 0;
			if (commendId == -1) {
				replyId = 0;
			} else {
				replyId = items.get(index).circleCommentList.get(commendId)
						.getId();
			}
			showDataDialog();
			String urlString = AppUtil.getStringId(context) + "/"
					+ getString(R.string.compos_circle_saveComment)
					+ "?userId=" + loginInfo.getUserId() + "&logId="
					+ items.get(index).getId() + "&content=" + message;
			if (replyId != 0) {
				urlString = urlString + "&replyId=" + replyId;
			}
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
								LogUtils.print(responseInfo.result);
								CommonMsg entity = JSON.parseObject(
										responseInfo.result,
										CommonMsg.class);
								// 如果 没问题的话
								if (entity != null) {
									CircleComment circleComment = AppUtil.getPerson(
											entity.getObj(), CircleComment.class);
									items.get(index).circleCommentList
											.add(circleComment);
									layout_sercher.setVisibility(View.GONE);
									pop_info_edit.setText("");
									baseAdapter.notifyDataSetChanged();
								}
							} catch (Exception e) {
								e.printStackTrace();
								MsgBean msgBean = JSON.parseObject(
										responseInfo.result, MsgBean.class);
								if (msgBean.getMessage().equals("0")) {
									AlertTools.showTips(context,
											R.drawable.tips_error,
											msgBean.getMsgInfo());
								} else {
								}
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
	}

	@Override
	public void onBottomPraise(final int position) {
		//
		final Circle circle = items.get(position);
		showDataDialog();
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(R.string.compos_circle_praise) + "?userId="
				+ loginInfo.getUserId() + "&logId=" + circle.getId();
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
							MsgBean msgBean = JSON.parseObject(
									responseInfo.result, MsgBean.class);
							if (msgBean.getMessage().equals("1")) {

								if (circle.getZanStatus() == 1) {
									Circle updateEntity = items.get(position);
									updateEntity.setZanStatus(0);
									String zanName = items.get(position)
											.getZanNames();
									zanName = zanName.replace(
											loginInfo.getUserName() + " ", "");
									updateEntity.setZanNames(zanName);
								} else {
									items.get(position).setZanStatus(1);
									Circle updateEntity = items.get(position);
									updateEntity.setZanNames(updateEntity
											.getZanNames()
											+ loginInfo.getUserName() + " ");
								}
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

	public void onBottomDeleLog(final int position) {
		final Circle circle = items.get(position);
		AlertHelp.showDelete(AyFriendUser.this, position,circle.getTitle(), this);
		
	}

	@Override
	public void onBottomEditLog(int position) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 100:
			if (resultCode == RESULT_OK) {
				Circle entity = (Circle) data.getSerializableExtra("addEntity");
				items.add(0, entity);
				baseAdapter.notifyDataSetChanged();
			}
			break;
		}
	}

	@Override
	public void cmdIndex(final int position) {
		// TODO Auto-generated method stub
		final Circle circle = items.get(position);
		showDataDialog();
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(R.string.compos_circle_deleteLog) + "?id="
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
}
