package flytv.qaonline.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import flytv.compos.run.R;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.UserShareUtils;
import flytv.qaonline.entity.QAItemEntity;
import flytv.qaonline.entity.QARequestEntity;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.http.HttpServer;
import flytv.qaonline.model.QAListAdapter;
import flytv.qaonline.model.QAListAdapter.QAListItemListener;
import flytv.qaonline.view.PullToRefreshView;
import flytv.qaonline.view.PullToRefreshView.OnFooterRefreshListener;
import flytv.qaonline.view.PullToRefreshView.OnHeaderRefreshListener;
import flytv.qaonline.view.RefreshView;
import flytv.qaonline.view.RefreshView.RefreshListener;
import flytv.run.bean.TVCodeBean;

public class MyQAFragment extends Fragment implements OnHeaderRefreshListener,
		OnFooterRefreshListener {
	private View mFragView;
	private ListView mFragListView;
	private QAListAdapter mQAListAdapter;
	private List<QAItemEntity> mQAListData;
	private RefreshView mRefreshView;
	private PullToRefreshView mPullToRefreshView;
	private long mDoubleClickTime;
	private int mPageIndex = 1;
	private TVCodeBean mUserBean;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mFragView == null) {
			mQAListData = new ArrayList<QAItemEntity>();
			mQAListAdapter = new QAListAdapter(getActivity(), mQAListData, null);
			mFragView = inflater.inflate(R.layout.fragment_myqa_view, null);
			mPullToRefreshView = (PullToRefreshView) mFragView
					.findViewById(R.id.myqa_pull_refresh_view);
			mRefreshView = (RefreshView) mFragView
					.findViewById(R.id.myqa_refresh_view);
			mRefreshView.setRefreshListener(new RefreshListener() {
				@Override
				public void onRefreshListener() {
					loadLivesData(false);
				}
			});
			mUserBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
					getActivity());
			mFragListView = (ListView) mFragView
					.findViewById(R.id.fragment_myqa_listview);
			mFragListView.setAdapter(mQAListAdapter);
			mQAListAdapter.setOnListLvItemListener(new QAListItemListener() {
				@Override
				public void onItemClickListener(int position) {
					if ((System.currentTimeMillis() - mDoubleClickTime) > 1000) {
						mDoubleClickTime = System.currentTimeMillis();
						if (getActivity() != null) {
							Intent intent = new Intent(getActivity(),
									QADetailActivity.class);
							intent.putExtra("questionId",
									mQAListData.get(position).getQuestionId());
							intent.putExtra("isEidtMode",true);
							intent.putExtra("subjectname", mQAListData.get(position).getQuestionSubjectName());
							startActivityForResult(intent, 1);
						}
					}
				}
			});
			mPullToRefreshView.setTimePullRefreshKey("live2");
			mPullToRefreshView.setShowRefreshTime(true);
			mPullToRefreshView.setOnHeaderRefreshListener(this);
			mPullToRefreshView.setOnFooterRefreshListener(this);
			mQAListData.clear();
			loadLivesData(false);
		}
		return mFragView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		((ViewGroup) getView()).removeAllViews();
	}

	public void onResume() {
		super.onResume();

	}

	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (mQAListData != null && !mQAListData.isEmpty()) {
			mQAListData.clear();
			mQAListData = null;
		}
		mFragView = null;
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		loadLivesData(true);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		refreshLisView();
	}

	private void loadLivesData(final boolean isAddMore) {
		if (!isAddMore)
			mRefreshView.startLoading();
		HttpEngine.getHttpEngine(getActivity()).requestQAListData(
				mUserBean.getUserId(), 10, mPageIndex, "",
				new HttpEngineListener() {
					@Override
					public void requestCallBack(final Object data,
							final String resultCode, int requestCode) {
						if (getActivity() != null) {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									QARequestEntity obj = (QARequestEntity) data;
									if (obj != null
											&& obj.getTotalPage() < mPageIndex) {
										AppUtil.UPLOADPATH=obj.getFileService();
										if (isAddMore) {
											mPullToRefreshView.onHeaderRefreshComplete();
											mPullToRefreshView.onFooterRefreshComplete();
										}
									} else {
										if (obj!=null) {
											AppUtil.UPLOADPATH=obj.getFileService();
										}
										mPageIndex++;
										if (obj != null
												&& obj.getItems() != null
												&& !obj.getItems().isEmpty()) {
											if ("1".equals(mUserBean
													.getUserType())) {
												for (int i = 0; i < obj
														.getItems().size(); i++) {
													QAItemEntity entityItem = obj
															.getItems().get(i);
													if (entityItem
															.getQuestionSubjectName() != null
															&& !"".equals(entityItem
																	.getQuestionBclassName())) {
														if (entityItem
																.getQuestionSubjectName()
																.equals(mUserBean
																		.getSubjectList()
																		.get(0)
																		.getName())) {
															mQAListData
																	.add(entityItem);
														}
													}
												}
											} else {
												mQAListData.addAll(obj
														.getItems());
											}

											mQAListAdapter
													.notifyDataSetChanged();
											if (!isAddMore)
												mRefreshView.endLoading();
										} else {
											if (!isAddMore) {
												if (HttpServer.HTTPSTATE_NONET
														.equals(resultCode)) {
													mRefreshView
															.endLoadingNoNet();
												} else {
													mRefreshView
															.endLoadingNoData();
												}
											}
										}
										if (isAddMore) {
											mPullToRefreshView.onHeaderRefreshComplete();
											mPullToRefreshView
													.onFooterRefreshComplete();
										}
									}
								}
							});
						}
					}
				});
	}
	
	public void refreshLisView(){
		mPageIndex = 1;
		if(mQAListData != null && !mQAListData.isEmpty())
			mQAListData.clear();
		loadLivesData(true);
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == 1){
				refreshLisView();
			}
		}
	}
}
