package flytv.qaonline.ui;

import in.srain.cube.image.CubeImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import flytv.compos.run.R;
import flytv.compos.run.bean.FileBean;
import flytv.compos.run.bean.GradeBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.UserShareUtils;
import flytv.qaonline.entity.ResultEntity;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.model.TextItemAdapter;
import flytv.qaonline.model.TextItemAdapter.ListItemClick;
import flytv.qaonline.utils.CapturePhoto;
import flytv.qaonline.utils.CapturePhoto.PhotoIntentListener;
import flytv.qaonline.utils.DensityConfig;
import flytv.qaonline.utils.MyUtils;
import flytv.qaonline.view.TitleView;
import flytv.qaonline.view.TitleView.TitleClickListener;
import flytv.run.bean.StuBean;
import flytv.run.bean.TVCodeBean;

public class SubmitExperimentActivity extends BaseActivity implements
		OnClickListener {
	private CapturePhoto mCapture;
	private RelativeLayout mRlView;
	private EditText mEtTitle;
	private CubeImageView mCbIv;
	private EditText mEtBranch;
	private ImageView mIvAdd;
	private TitleView mTitleView;
	private List<String> dataList = new ArrayList<String>();
	private Button mSubjectBt;
	private PopupWindow mProvincePop;
	private FileBean mFileBean;
	private TVCodeBean mUserBean;
	
	private Button mClassBt;
	private EditText mEtClass;
	private PopupWindow mClassProvincePop;
	private List<String> classList = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_publish_video);
		mUserBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(this);
		for(StuBean stuBean : mUserBean.subjectList){
			dataList.add(stuBean.getName());
		}
		for(GradeBean gradeBean : mUserBean.gradeList){
			classList.add(gradeBean.getNJMC());
		}
		initMainView();
	}

	private void initMainView() {
		mCapture = new CapturePhoto(this, new PhotoIntentListener() {
			@Override
			public void finishPhotoIntent(String filePath, Bitmap fileBitmap,
					int type) {
				switch (type) {
				case CapturePhoto.SHOT_VIDEO:
					showProgressDialog();
					final BitmapUtils	bitmapUtils = new BitmapUtils(SubmitExperimentActivity.this);
					if (filePath != null && !"".equals(filePath)) {
						HttpEngine.getHttpEngine(SubmitExperimentActivity.this)
								.requestFileUploadData(1, new File(filePath),
										new HttpEngineListener() {
											@Override
											public void requestCallBack(
													Object data,
													String resultCode,
													int requestCode) {
												mFileBean = (FileBean) data;
												closeProgressDialog();
												if (mFileBean != null) {
													final Bitmap bitmap = BitmapFactory.decodeResource(
															getResources(), R.drawable.im_add_image);
													if (!AppUtil.isStrNull(mFileBean.getThumbPath())) {
														final String imageUrl = mFileBean.getThumbPath();
														mIvAdd.setTag(imageUrl);
														bitmapUtils.configDefaultLoadingImage(bitmap);
														bitmapUtils.display(mIvAdd, imageUrl,
																new BitmapLoadCallBack<View>() {

																	@Override
																	public void onLoadCompleted(View arg0, String arg1,
																			Bitmap arg2, BitmapDisplayConfig arg3,
																			BitmapLoadFrom arg4) {
																		mRlView.setVisibility(View.VISIBLE);
																		mCbIv.setImageBitmap(arg2);
																		mIvAdd.setVisibility(View.GONE);
																	}

																	@Override
																	public void onLoadFailed(View arg0, String arg1,
																			Drawable arg2) {
																		Toast.makeText(SubmitExperimentActivity.this,"上传失败", Toast.LENGTH_SHORT).show();
																		closeDataDialog();
																	}
																});
													} else {
														mIvAdd.setImageBitmap(bitmap);
													}
												}else {
													Toast.makeText(SubmitExperimentActivity.this,"上传失败", Toast.LENGTH_SHORT).show();
													closeDataDialog();
												}
											}
										});
					}
					break;
				default:
					break;
				}
			}
		});
		mCbIv=(CubeImageView) findViewById(R.id.im_backgourd);
		mRlView=(RelativeLayout) findViewById(R.id.rl_cubeimageview);
		mTitleView = (TitleView) findViewById(R.id.qa_new_title_view);
		mTitleView.setTitleValue(R.string.str_return,
				R.string.str_publish_title, "提交");
		mEtTitle = (EditText) findViewById(R.id.et_title);
		mEtBranch = (EditText) findViewById(R.id.et_branch);
		mEtClass = (EditText) findViewById(R.id.et_class);
		mClassBt = (Button) findViewById(R.id.class_btn);
		mIvAdd = (ImageView) findViewById(R.id.iv_add_image);
		mIvAdd.setOnClickListener(this);
		mSubjectBt = (Button) findViewById(R.id.subject_btn);
		mSubjectBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showProvinceList();
			}
		});
		mClassBt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showClassList();
			}
		});
		mTitleView.setTitleClickListener(new TitleClickListener() {
			@Override
			public void onRightClick(View view) {
				// 提交视频
				submitPublish();
			}

			@Override
			public void onLeftClick(View view) {
				SubmitExperimentActivity.this.finish();
			}
		});
		mRlView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFileBean == null){
					Toast.makeText(SubmitExperimentActivity.this,"视频文件为空",Toast.LENGTH_SHORT).show();
				}else{
					MyUtils.startResource(SubmitExperimentActivity.this,
							AppUtil.getHttpImage(mFileBean.getFileUrl()));
				}
			}
		});
		mRlView.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				showDelDialog();
				return false;
			}
		});
	}
	private void showDelDialog(){
		AlertDialog.Builder alert = new AlertDialog.Builder(SubmitExperimentActivity.this)
		.setMessage("确定删除此视频文件")
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				mFileBean = null;
				mRlView.setVisibility(View.GONE);
				mCbIv.setVisibility(View.GONE);
				mIvAdd.setVisibility(View.VISIBLE);
				dialog.dismiss();
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		alert.show();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.subject_btn:

			break;
		case R.id.iv_add_image:
			mCapture.dispatchPictureIntent(CapturePhoto.SHOT_VIDEO);
			break;
		default:
			break;
		}
	}

	private void submitPublish() {
		if (StringUtils.isEmpty(mEtTitle.getText())) {
			Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
			return;
		}
		if (StringUtils.isEmpty(mEtBranch.getText())) {
			Toast.makeText(this,
					getResources().getString(R.string.str_branch_empty),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (StringUtils.isEmpty(mEtClass.getText())) {
			Toast.makeText(this,"请选择年级",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (mFileBean==null || mFileBean.getId()==0) {
			Toast.makeText(this,"请添加视频文件",Toast.LENGTH_SHORT).show();
			return;
		}
		// 发布视频
		HttpEngine.getHttpEngine(this).publishVedio(mUserBean.getUserId(), mEtTitle.getText().toString(), mEtBranch.getText().toString(), mEtClass.getText().toString(),
				"",mFileBean == null ? "" :mFileBean.getId()+"" , new HttpEngineListener() {
			@Override
			public void requestCallBack(Object data, String resultCode, int requestCode) {
				final ResultEntity obj = (ResultEntity) data;
				SubmitExperimentActivity.this
						.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								if ("1".equals(obj
										.getMessage())) {
									Toast.makeText(
											SubmitExperimentActivity.this,
											"发布成功",
											Toast.LENGTH_SHORT)
											.show();
									finish();
								} else {
									Toast.makeText(
											SubmitExperimentActivity.this,
											obj.getMsgInfo(),
											Toast.LENGTH_SHORT)
											.show();
								}
							}
						});
			}
		});
	}

	private void showProvinceList() {
		if (mProvincePop != null && mProvincePop.isShowing())
			mProvincePop.dismiss();
		View popView = View.inflate(this, R.layout.view_popunwindow_pro, null);
		popView.setFocusable(true);
		popView.setFocusableInTouchMode(true);
		ListView listView = (ListView) popView
				.findViewById(R.id.popupwindow_login_listview);

		TextItemAdapter adapter = new TextItemAdapter(this, dataList,
				new ListItemClick() {

					@Override
					public void onItemClickIndex(int index) {
						// TODO Auto-generated method stub
						mEtBranch.setText(dataList.get(index));
						mProvincePop.dismiss();
					}
				});
		listView.setAdapter(adapter);
		mProvincePop = new PopupWindow(popView,DensityConfig.dip2px(SubmitExperimentActivity.this, 200),
				LayoutParams.WRAP_CONTENT);
		mProvincePop.getContentView().setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mProvincePop.setFocusable(false);
				mProvincePop.dismiss();
				return true;
			}
		});

		mProvincePop.setOutsideTouchable(true);
		mProvincePop.setFocusable(true);
		mProvincePop.setBackgroundDrawable(new BitmapDrawable());
		mProvincePop.showAsDropDown(mSubjectBt);
	}
	
	private void showClassList() {
		if (mClassProvincePop != null && mClassProvincePop.isShowing())
			mClassProvincePop.dismiss();
		View popView = View.inflate(this, R.layout.view_popunwindow_pro, null);
		popView.setFocusable(true);
		popView.setFocusableInTouchMode(true);
		ListView listView = (ListView) popView
				.findViewById(R.id.popupwindow_login_listview);
		
		TextItemAdapter adapter = new TextItemAdapter(this, classList,
				new ListItemClick() {
			@Override
			public void onItemClickIndex(int index) {
				mEtClass.setText(classList.get(index));
				mClassProvincePop.dismiss();
			}
		});
		listView.setAdapter(adapter);
		mClassProvincePop = new PopupWindow(popView,DensityConfig.dip2px(SubmitExperimentActivity.this, 200),
				LayoutParams.WRAP_CONTENT);
		mClassProvincePop.getContentView().setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mClassProvincePop.setFocusable(false);
				mClassProvincePop.dismiss();
				return true;
			}
		});
		mClassProvincePop.setOutsideTouchable(true);
		mClassProvincePop.setFocusable(true);
		mClassProvincePop.setBackgroundDrawable(new BitmapDrawable());
		mClassProvincePop.showAsDropDown(mClassBt);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CapturePhoto.SHOT_IMAGE
				|| requestCode == CapturePhoto.PICK_IMAGE) {
			mCapture.onActivityResult(requestCode, resultCode, data);
		} else {
			if (mCapture != null && resultCode == Activity.RESULT_OK)
				mCapture.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		
	}
}
