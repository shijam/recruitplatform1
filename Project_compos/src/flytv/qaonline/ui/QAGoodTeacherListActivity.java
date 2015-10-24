package flytv.qaonline.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import flytv.compos.run.R;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.entity.QATeacherListEntity;
import flytv.qaonline.entity.ResultEntity;
import flytv.qaonline.entity.TeacherItemEntity;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.http.HttpServer;
import flytv.qaonline.model.TeachersListAdapter;
import flytv.qaonline.model.TeachersListAdapter.TeachersIvListener;
import flytv.qaonline.view.PullToRefreshView;
import flytv.qaonline.view.PullToRefreshView.OnFooterRefreshListener;
import flytv.qaonline.view.PullToRefreshView.OnHeaderRefreshListener;
import flytv.qaonline.view.RefreshView;
import flytv.qaonline.view.RefreshView.RefreshListener;
import flytv.qaonline.view.TitleView;
import flytv.qaonline.view.TitleView.TitleClickListener;

public class QAGoodTeacherListActivity extends Activity implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private ListView mFragListView;
	private TitleView mTitleView;
	private TeacherItemEntity mTeacherEntity;
	private TeachersListAdapter mTeacherListAdapter;
	private List<TeacherItemEntity> mTeacherListData;
	private RefreshView mRefreshView;
	private PullToRefreshView mPullToRefreshView;
	private long mDoubleClickTime;
	private int mPageIndex = 1;
	private int mIndex = -1;

	private NewQuestionItemEntity mData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_teachers_view);
		mTeacherListData = new ArrayList<TeacherItemEntity>();
		Bundle bundle = getIntent().getExtras();
		mData = (NewQuestionItemEntity) bundle.getSerializable("entity");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mPullToRefreshView = (PullToRefreshView) findViewById(R.id.teachers_pull_refresh_view);
		mTitleView = (TitleView) findViewById(R.id.qa_new_title_view);
		mTitleView.setTitleValue(R.string.str_return,
				R.string.str_qa_selectteacher, "确定");
		mTeacherListAdapter = new TeachersListAdapter(this, mTeacherListData,
				new TeachersIvListener() {

					@Override
					public void onIvClickListener(int position) {
						// TODO Auto-generated method stub
						mTeacherEntity = mTeacherListData.get(position);
						mIndex = position;
						mTeacherListAdapter.setmIndex(position);
						mTeacherListAdapter.notifyDataSetChanged();
					}
				});
		mRefreshView = (RefreshView) findViewById(R.id.teacher_refresh_view);
		mRefreshView.setRefreshListener(new RefreshListener() {
			@Override
			public void onRefreshListener() {
				loadLivesData(false);
			}
		});
		mFragListView = (ListView) findViewById(R.id.activity_teachers_listview);
		mFragListView.setAdapter(mTeacherListAdapter);
		mPullToRefreshView.setTimePullRefreshKey("live");
		mPullToRefreshView.setShowRefreshTime(true);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
		mTeacherListData.clear();

		mTitleView.setTitleClickListener(new TitleClickListener() {
			@Override
			public void onRightClick(View view) {
				if (mIndex < 0) {
					Toast.makeText(QAGoodTeacherListActivity.this, "请先选择老师",
							Toast.LENGTH_SHORT).show();
					return;
				}
				submitNewRuestion(mData);
			}

			@Override
			public void onLeftClick(View view) {
				finish();
			}
		});
		loadLivesData(false);
	}

	public void onResume() {
		super.onResume();

	}

	public void onPause() {
		super.onPause();
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		loadLivesData(true);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		refreshList();
	}

	private void submitNewRuestion(NewQuestionItemEntity entity) {
		HttpEngine.getHttpEngine(this).requestNewQuestion(entity,
				new HttpEngineListener() {
					@Override
					public void requestCallBack(final Object data,
							final String resultCode, int requestCode) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								ResultEntity obj = (ResultEntity) data;
								if (obj != null) {
									if ("1".equals(obj.getMessage())) {
										HttpEngine
												.getHttpEngine(
														QAGoodTeacherListActivity.this)
												.requestTeacherQaData(
														"12181033",
														obj.getQuestioned(),
														mTeacherListData
																.get(mIndex)
																.getTeacherUserId(),
														new HttpEngineListener() {

															@Override
															public void requestCallBack(
																	Object data,
																	String resultCode,
																	int requestCode) {
																final ResultEntity obj = (ResultEntity) data;
																QAGoodTeacherListActivity.this.runOnUiThread(new Runnable() {
																	@Override
																	public void run() {
																		// TODO Auto-generated method stub
																		
																		if ("1".equals(obj
																				.getMessage())) {
																			Toast.makeText(
																					QAGoodTeacherListActivity.this,
																					getResources()
																					.getString(
																							R.string.str_newquestion_success),
																							Toast.LENGTH_SHORT)
																							.show();
																					setResult(RESULT_OK);
																			 finish();
																		} else {
																			Toast.makeText(
																					QAGoodTeacherListActivity.this,
																					obj.getMsgInfo(),
																					Toast.LENGTH_SHORT)
																					.show();
																		}
																	}
																});

															}
														});
									} else {
										Toast.makeText(
												QAGoodTeacherListActivity.this,
												"保存草稿失败", Toast.LENGTH_SHORT)
												.show();
									}
								} else {
									Toast.makeText(
											QAGoodTeacherListActivity.this,
											getResources().getString(
													R.string.str_tips_nonet),
											Toast.LENGTH_SHORT).show();
								}

							}
						});
					}
				});
	}

	private void loadLivesData(final boolean isAddMore) {
		if (!isAddMore)
			mRefreshView.startLoading();
		HttpEngine.getHttpEngine(this).requestTeacherList("12181033", 10,
				mPageIndex, mData.getSubjectName(), "",
				new HttpEngineListener() {
					@Override
					public void requestCallBack(final Object data,
							final String resultCode, int requestCode) {
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								QATeacherListEntity obj = (QATeacherListEntity) data;
								if(obj != null){
									if(mPageIndex > Integer.parseInt(obj.getTotalPage())){
										if (isAddMore) {
											mPullToRefreshView.onHeaderRefreshComplete();
											mPullToRefreshView.onFooterRefreshComplete();
										}
									}else{
										mPageIndex++;
										if (obj != null && obj.getItems() != null
												&& !obj.getItems().isEmpty()) {
											mTeacherListAdapter.setPath(obj.getFileService());
											mTeacherListData.addAll(obj.getItems());
											mTeacherListAdapter.notifyDataSetChanged();
											if (!isAddMore)
												mRefreshView.endLoading();
										}else{
											if (!isAddMore) {
												if (HttpServer.HTTPSTATE_NONET.equals(resultCode)) {
													mRefreshView.endLoadingNoNet();
												} else {
													mRefreshView.endLoadingNoData();
												}
											}
										}
										if (isAddMore) {
											mPullToRefreshView.onHeaderRefreshComplete();
											mPullToRefreshView.onFooterRefreshComplete();
										}
									}
								}
							}
						});
					}
				});
	}

	
	private void refreshList(){
		mPageIndex = 1;
		mTeacherListData.clear();
		loadLivesData(true);
	}
}
