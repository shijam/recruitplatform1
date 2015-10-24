package flytv.qaonline.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import flytv.compos.run.R;
import flytv.compos.run.bean.FileBean;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.UserShareUtils;
import flytv.qaonline.entity.VedioEntity;
import flytv.qaonline.entity.VedioItemEntity;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.http.HttpServer;
import flytv.qaonline.model.VedioQAListAdapter;
import flytv.qaonline.view.PullToRefreshView;
import flytv.qaonline.view.PullToRefreshView.OnFooterRefreshListener;
import flytv.qaonline.view.PullToRefreshView.OnHeaderRefreshListener;
import flytv.qaonline.view.RefreshView;
import flytv.qaonline.view.RefreshView.RefreshListener;
import flytv.run.bean.TVCodeBean;

public class VideoQAFragment extends Fragment implements
		OnHeaderRefreshListener, OnFooterRefreshListener {
	private View mFragView;
	private ListView mFragListView;
	private VedioQAListAdapter mQAListAdapter;
	private List<VedioItemEntity> mQAListData;
	private RefreshView mRefreshView;
	private PullToRefreshView mPullToRefreshView;
	private long mDoubleClickTime;
	private int mPageIndex = 1;
	private TVCodeBean mUserBean;
	private Button mBtPublishVedio;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mFragView == null) {
			mUserBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
					getActivity());
			mQAListData = new ArrayList<VedioItemEntity>();
			mQAListAdapter = new VedioQAListAdapter(getActivity(), mQAListData,
					null);
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
			mBtPublishVedio = (Button) mFragView
					.findViewById(R.id.bt_publish_vedio);
			if ("1".equals(mUserBean.getUserType())) {
				if (mUserBean!=null) {
					if (isWuli()) {
						mBtPublishVedio.setVisibility(View.VISIBLE);
					}
				}
			}
			mFragListView = (ListView) mFragView
					.findViewById(R.id.fragment_myqa_listview);
			mFragListView.setAdapter(mQAListAdapter);

			mPullToRefreshView.setTimePullRefreshKey("live");
			mPullToRefreshView.setShowRefreshTime(true);
			mPullToRefreshView.setOnHeaderRefreshListener(this);
			mPullToRefreshView.setOnFooterRefreshListener(this);
			mBtPublishVedio.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent=new Intent();
					intent.setClass(getActivity(),SubmitExperimentActivity.class);
					startActivity(intent);
				}
			});
			mQAListData.clear();
			loadLivesData(false);
		}
		return mFragView;
	}

	private boolean isWuli(){
		boolean flag =false;
		if (mUserBean!=null&&mUserBean.subjectList!=null) {
			for (int i = 0; i < mUserBean.subjectList.size(); i++) {
				if ("物理".equals(mUserBean.subjectList.get(i).getName())||"化学".equals(mUserBean.subjectList.get(i).getName())) {
					flag=true;
				}
			}
		}
		return flag;
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
		HttpEngine.getHttpEngine(getActivity()).requestexperimentListData(
				mUserBean.getUserId(), 10, mPageIndex, "",
				new HttpEngineListener() {
					@Override
					public void requestCallBack(final Object data,
							final String resultCode, int requestCode) {
						if (getActivity() != null) {
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									VedioEntity obj = (VedioEntity) data;
									if (obj != null && obj.getItems() != null
											&& !obj.getItems().isEmpty())
										mPageIndex++;
									if (obj != null && obj.getItems() != null
											&& !obj.getItems().isEmpty()) {
										mQAListData.addAll(obj.getItems());
										mQAListAdapter.setPath(obj
												.getFileService());
										mQAListAdapter.notifyDataSetChanged();
										if (!isAddMore)
											mRefreshView.endLoading();

									} else {
										if (!isAddMore) {
											if (HttpServer.HTTPSTATE_NONET
													.equals(resultCode)) {
												mRefreshView.endLoadingNoNet();
											} else {
												mRefreshView.endLoadingNoData();
											}
										}
									}
									if (isAddMore) {
										mPullToRefreshView.onHeaderRefreshComplete();
										mPullToRefreshView
												.onFooterRefreshComplete();
									}
								}
							});
						}
					}
				});
	}

	void upload(String filePath) {
		LogUtils.print(1, "filePath=" + filePath);
		TVCodeBean entity = (TVCodeBean) UserShareUtils.getInstance()
				.getLoginInfo(getActivity());
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		if (AppUtil.isStrNull(filePath)) {
			AlertTools.showTips(getActivity(), R.drawable.tips_warning, "上传路径错误");
			return;
		}
		String fileUploadUrl = null;
		if (fileName.contains("jpg") || fileName.contains("png")
				|| fileName.contains("mp4")) {
			fileUploadUrl = AppUtil.getStringId(getActivity())
					+ getString(R.string.flytv_user_file_uploadImage).replace(
							"{userId}", entity.getUserId());
			RequestParams params = new RequestParams();
			params.addBodyParameter("fileName", fileName);
			TVCodeBean entityFile = UserShareUtils.getInstance()
					.getTVInfo(getActivity());
			params.addBodyParameter("deviceNo", entityFile.getDeviceNo());
			params.addBodyParameter("userId", entity.getUserId());
			if (fileName.contains("jpg")) {
				Bitmap bitmap = AppUtil.getSmallBitmap(filePath);
				AppUtil.compressBmpToFile(bitmap, new File(filePath));
				if (bitmap.isRecycled()) {
					bitmap.recycle();
					System.gc();
				}
			}
			File file = new File(filePath);
			long fileSize = file.length();
			long kbSize = fileSize / 1024;
			LogUtils.print(1, "kbSize=" + kbSize + "KB");
			params.addBodyParameter("file", file);
			HttpUtils http = new HttpUtils();
			http.configTimeout(60000);
			LogUtils.print(1, "url=" + fileUploadUrl);
			http.send(HttpRequest.HttpMethod.POST, fileUploadUrl, params,
					new RequestCallBack<String>() {
						@Override
						public void onStart() {
							// msgTextview.setText("conn...");

						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
//							if (isUploading) {
//							} else {
//							}
							Toast.makeText(getActivity(), "正在提交文件",Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							LogUtils.print(arg0.getMessage());
							Toast.makeText(getActivity(), "提交文件失败",Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
							LogUtils.print("arg0=" + arg0.result);
							TVCodeBean tvCodeBean = (TVCodeBean) AppUtil
									.getJSONBean(arg0.result, TVCodeBean.class);
							if (tvCodeBean != null) {
								FileBean fileBean = new FileBean();
								fileBean.setImg_typeId(0);
								fileBean.setFileUrl(AppUtil.getIPSplit(
										getActivity(),
										AppUtil.UPLOADPATH + "/"
												+ tvCodeBean.getFilePath()));
								if (false) {
									fileBean.setThumbPath(AppUtil.getIPSplit(
											getActivity(),
											AppUtil.UPLOADPATH + "/"
													+ tvCodeBean.getFilePath()));
									fileBean.setExtType("img");
								} else {
									fileBean.setThumbPath(AppUtil
											.getIPSplit(
													getActivity(),
													AppUtil.UPLOADPATH
															+ "/"
															+ tvCodeBean
																	.getThumbPath()));
									fileBean.setExtType("video");
								}
								fileBean.setName(tvCodeBean.getFileName());
								fileBean.setId(Integer.parseInt(tvCodeBean
										.getId()));
								
								
							}
						}
					});
		}
	}
	
	public void refreshLisView(){
		mPageIndex = 1;
		if(mQAListData != null && !mQAListData.isEmpty())
			mQAListData.clear();
		loadLivesData(true);
	}
}
