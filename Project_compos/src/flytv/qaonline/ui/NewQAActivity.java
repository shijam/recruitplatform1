package flytv.qaonline.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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

import flytv.compos.run.R;
import flytv.compos.run.bean.FileBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.utils.UserShareUtils;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.entity.ResultEntity;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.model.TextItemAdapter;
import flytv.qaonline.model.TextItemAdapter.ListItemClick;
import flytv.qaonline.utils.CapturePhoto;
import flytv.qaonline.utils.CapturePhoto.PhotoIntentListener;
import flytv.qaonline.utils.DensityConfig;
import flytv.qaonline.utils.Mp3PlayEngine;
import flytv.qaonline.utils.MyUtils;
import flytv.qaonline.view.LocatFileView;
import flytv.qaonline.view.LocatFileView.LocatFileViewClick;
import flytv.qaonline.view.MyDialog;
import flytv.qaonline.view.MyDialog.MyDialogItemClickListener;
import flytv.qaonline.view.TitleView;
import flytv.qaonline.view.TitleView.TitleClickListener;
import flytv.run.bean.TVCodeBean;

public class NewQAActivity extends BaseActivity implements OnClickListener {
	private TitleView mTitleView;
	private EditText mEtBranch;
	// private EditText mEtTitle;
	private EditText mEtDifficultPoint;
	private EditText mEtContent;
	private ImageView mIvAddContent;
	private LocatFileView mLocatFileView;
	private Button mBtMyteacher;
	private Button mBtFindTeacher;
	private Button mBtAutoSubmit;
	private List<String> dataList = new ArrayList<String>();
	private CapturePhoto mCapture;
	private RelativeLayout rl_cubeimageview;
	private ImageView im_play;
	private TVCodeBean mUserBean;
	private String mImageIds = "";
	private ArrayList<FileBean> mImageDatas;
	private ArrayList<FileBean> mVideoDatas;
	private ArrayList<FileBean> mRecordDatas;
	private String mQuestionId = "0";
	private FileBean mFileBean = new FileBean();
	private Button mSubjectBt;
	private PopupWindow mProvincePop;
	private MyDialog myDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_creatquestion);
		mUserBean = (TVCodeBean) UserShareUtils.getInstance()
				.getLoginInfo(this);
		mImageDatas = new ArrayList<FileBean>();
		mVideoDatas = new ArrayList<FileBean>();
		mRecordDatas = new ArrayList<FileBean>();
		dataList.add("语文");
		dataList.add("数学");
		dataList.add("英语");
		dataList.add("物理");
		dataList.add("化学");
		initMainView();
	}

	private void initMainView() {
		myDialog = new MyDialog(findViewById(R.id.qa_new_main_view));
		mCapture = new CapturePhoto(this, new PhotoIntentListener() {
			@Override
			public void finishPhotoIntent(String filePath, Bitmap fileBitmap,
					int type) {
				switch (type) {
				case CapturePhoto.CORP_IMAGE:
					if (filePath != null && !"".equals(filePath)) {
						showProgressDialog();
						HttpEngine.getHttpEngine(NewQAActivity.this)
								.requestFileUploadData(0, new File(filePath),
										new HttpEngineListener() {
											@Override
											public void requestCallBack(
													Object data,
													String resultCode,
													int requestCode) {
												closeProgressDialog();
												FileBean fileBean = (FileBean) data;
												if (fileBean != null) {
													mImageDatas.add(fileBean);
//													mImageIds += fileBean
//															.getId() + ",";
													mLocatFileView
															.addImageView(fileBean);
												}
											}
										});
					}
					break;
				case CapturePhoto.SHOT_VIDEO:
					showProgressDialog();
					if (filePath != null && !"".equals(filePath)) {
						HttpEngine.getHttpEngine(NewQAActivity.this)
								.requestFileUploadData(1, new File(filePath),
										new HttpEngineListener() {
											@Override
											public void requestCallBack(
													Object data,
													String resultCode,
													int requestCode) {
												closeProgressDialog();
												FileBean fileBean = (FileBean) data;
												if (fileBean != null) {
													mVideoDatas.add(fileBean);
//													mImageIds += fileBean
//															.getId() + ",";
													mLocatFileView
															.addVideoView(fileBean);
												}
											}
										});
					}
					break;
				case CapturePhoto.SOUND_RECORD:
					showProgressDialog();
					if (filePath != null && !"".equals(filePath)) {
						HttpEngine.getHttpEngine(NewQAActivity.this)
								.requestFileUploadData(2, new File(filePath),
										new HttpEngineListener() {
											@Override
											public void requestCallBack(
													Object data,
													String resultCode,
													int requestCode) {
												closeProgressDialog();
												FileBean fileBean = (FileBean) data;
												if (fileBean != null) {
													mRecordDatas.add(fileBean);
//													mImageIds += fileBean
//															.getId() + ",";
													mLocatFileView
															.addMp3View(fileBean);
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
		mLocatFileView = (LocatFileView) findViewById(R.id.test_locatfile_view);
		mLocatFileView.setLocatFileViewClick(new LocatFileViewClick() {
			@Override
			public void clickVideo(FileBean fileIetmEntity) {
				if (fileIetmEntity != null)
					MyUtils.startResource(NewQAActivity.this,
							fileIetmEntity.getFileUrl());
			}

			@Override
			public void clickImage(FileBean fileIetmEntity) {
				if (fileIetmEntity != null)
					MyUtils.startResource(NewQAActivity.this,
							fileIetmEntity.getFileUrl());
			}

			@Override
			public void clickMp3(FileBean fileIetmEntity) {
				if (fileIetmEntity != null)
					Mp3PlayEngine.getMp3PlayEngine().playSound(
							fileIetmEntity.getFileUrl());
			}

			@Override
			public void delFile(FileBean fileIetmEntity, int type) {
				switch (type) {
				case 1:
					mImageDatas.remove(fileIetmEntity);
					break;
				case 2:
					mVideoDatas.remove(fileIetmEntity);
					break;
				case 3:
					mRecordDatas.remove(fileIetmEntity);
					break;
				default:
					break;
				}
			}
		});
		rl_cubeimageview = (RelativeLayout) findViewById(R.id.rl_cubeimageview);
		im_play = (ImageView) findViewById(R.id.im_play);
		mTitleView = (TitleView) findViewById(R.id.qa_new_title_view);
		mTitleView

		.setTitleValue(R.string.str_return, R.string.str_qa_newqa, "");

		mEtBranch = (EditText) findViewById(R.id.et_branch);
		mSubjectBt = (Button) findViewById(R.id.subject_btn);
		// mEtTitle = (EditText) findViewById(R.id.et_title);
		mEtDifficultPoint = (EditText) findViewById(R.id.et_diffcult_point);
		mEtContent = (EditText) findViewById(R.id.et_content);
		mIvAddContent = (ImageView) findViewById(R.id.iv_add_image);
		mBtMyteacher = (Button) findViewById(R.id.bt_myteacher);
		mBtAutoSubmit = (Button) findViewById(R.id.bt_auto_assign);
		mBtFindTeacher = (Button) findViewById(R.id.bt_find_teacher);
		mBtAutoSubmit.setOnClickListener(this);
		mBtFindTeacher.setOnClickListener(this);
		mBtMyteacher.setOnClickListener(this);
		mIvAddContent.setOnClickListener(this);
		mTitleView.setTitleClickListener(new TitleClickListener() {
			@Override
			public void onRightClick(View view) {
			}

			@Override
			public void onLeftClick(View view) {
				NewQAActivity.this.finish();
			}
		});

		mSubjectBt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showProvinceList();
			}
		});
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_myteacher:
			myTeacherClick();
			break;
		case R.id.bt_find_teacher:
			// 跳转到名师列表提交
			toTeachers();
			break;
		case R.id.bt_auto_assign:
			autoAssignClick();
			break;
		case R.id.iv_add_image:
			showPhotoDialog(NewQAActivity.this);
			break;

		default:
			break;
		}
	}

	private void autoAssignClick() {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(mEtBranch.getText())) {
			Toast.makeText(this,
					getResources().getString(R.string.str_branch_empty),
					Toast.LENGTH_SHORT).show();

			return;
		}
		if (!dataList.contains(mEtBranch.getText().toString())) {
			Toast.makeText(this,
					getResources().getString(R.string.str_branch_correct),
					Toast.LENGTH_SHORT).show();
			return;
		}
		// if (StringUtils.isEmpty(mEtTitle.getText())) {
		// Toast.makeText(this,
		// getResources().getString(R.string.str_title_empty),
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		if (StringUtils.isEmpty(mEtDifficultPoint.getText())) {
			Toast.makeText(
					this,
					getResources().getString(R.string.str_difficultpoint_empty),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (StringUtils.isEmpty(mEtContent.getText())) {
			Toast.makeText(this,
					getResources().getString(R.string.str_content_empty),
					Toast.LENGTH_SHORT).show();
			return;
		}
		NewQuestionItemEntity entity = new NewQuestionItemEntity();
		entity.setUserId(mUserBean.getUserId());
		entity.setId("0");
		entity.setSubjectName(mEtBranch.getText().toString());
		entity.setReplyQuestionId(mQuestionId);
		entity.setReplyMarkId("0");
		// entity.setTitle(mEtTitle.getText().toString());
		entity.setContent(mEtContent.getText().toString());
		entity.setKeywords(mEtDifficultPoint.getText().toString());
		getImageIds();
		entity.setImagesIds(mImageIds);
		entity.setType("1");
		entity.setSubmitStatus("0");
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
														NewQAActivity.this)
												.autoTeacherQaData(
														mUserBean.getUserId(),
														obj.getQuestioned(),
														new HttpEngineListener() {
															@Override
															public void requestCallBack(
																	Object data,
																	String resultCode,
																	int requestCode) {
																final ResultEntity obj = (ResultEntity) data;
																NewQAActivity.this
																		.runOnUiThread(new Runnable() {
																			@Override
																			public void run() {
																				// TODO
																				// Auto-generated
																				// method
																				// stub
																				if ("1".equals(obj
																						.getMessage())) {
																					Toast.makeText(
																							NewQAActivity.this,
																							getResources()
																									.getString(
																											R.string.str_newquestion_success),
																							Toast.LENGTH_SHORT)
																							.show();
																					setResult(RESULT_OK);
																					finish();
																				} else {
																					Toast.makeText(
																							NewQAActivity.this,
																							obj.getMsgInfo(),
																							Toast.LENGTH_SHORT)
																							.show();
																				}
																			}
																		});

															}
														});
									} else {
										Toast.makeText(NewQAActivity.this,
												"保存草稿失败", Toast.LENGTH_SHORT)
												.show();
									}
								} else {
									Toast.makeText(
											NewQAActivity.this,
											getResources().getString(
													R.string.str_tips_nonet),
											Toast.LENGTH_SHORT).show();
								}

							}
						});
					}
				});

	}

	private void toTeachers() {
		if (StringUtils.isEmpty(mEtBranch.getText())) {
			Toast.makeText(this,
					getResources().getString(R.string.str_branch_empty),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!dataList.contains(mEtBranch.getText().toString())) {
			Toast.makeText(this,
					getResources().getString(R.string.str_branch_correct),
					Toast.LENGTH_SHORT).show();
			return;
		}
		// if (StringUtils.isEmpty(mEtTitle.getText())) {
		// Toast.makeText(this,
		// getResources().getString(R.string.str_title_empty),
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		if (StringUtils.isEmpty(mEtDifficultPoint.getText())) {
			Toast.makeText(
					this,
					getResources().getString(R.string.str_difficultpoint_empty),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (StringUtils.isEmpty(mEtContent.getText())) {
			Toast.makeText(this,
					getResources().getString(R.string.str_content_empty),
					Toast.LENGTH_SHORT).show();
			return;
		}

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		NewQuestionItemEntity entity = new NewQuestionItemEntity();
		entity.setUserId(mUserBean.getUserId());
		entity.setId("0");
		entity.setSubjectName(mEtBranch.getText().toString());
		entity.setReplyQuestionId(mQuestionId);
		entity.setReplyMarkId("0");
		// entity.setTitle(mEtTitle.getText().toString());
		entity.setContent(mEtContent.getText().toString());
		entity.setKeywords(mEtDifficultPoint.getText().toString());
		getImageIds();
		entity.setImagesIds(mImageIds);
		entity.setType("1");
		entity.setSubmitStatus("0");
		bundle.putSerializable("entity", entity);
		intent.putExtras(bundle);
		intent.setClass(this, QAGoodTeacherListActivity.class);
		startActivityForResult(intent, 11);
	}
	private void getImageIds(){
		mImageIds="";
		for (int i = 0; i < mRecordDatas.size(); i++) {
			mImageIds += mRecordDatas.get(i).getId() + ",";
		}
		for (int i = 0; i < mVideoDatas.size(); i++) {
			mImageIds += mVideoDatas.get(i).getId() + ",";
		}
		for (int i = 0; i < mImageDatas.size(); i++) {
			mImageIds += mImageDatas.get(i).getId() + ",";
		}
	}
	private void myTeacherClick() {
		// TODO Auto-generated method stub
		if (StringUtils.isEmpty(mEtBranch.getText())) {
			Toast.makeText(this,
					getResources().getString(R.string.str_branch_empty),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!dataList.contains(mEtBranch.getText().toString())) {
			Toast.makeText(this,
					getResources().getString(R.string.str_branch_correct),
					Toast.LENGTH_SHORT).show();
			return;
		}
		// if (StringUtils.isEmpty(mEtTitle.getText())) {
		// Toast.makeText(this,
		// getResources().getString(R.string.str_title_empty),
		// Toast.LENGTH_SHORT).show();
		// return;
		// }
		if (StringUtils.isEmpty(mEtDifficultPoint.getText())) {
			Toast.makeText(
					this,
					getResources().getString(R.string.str_difficultpoint_empty),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (StringUtils.isEmpty(mEtContent.getText())) {
			Toast.makeText(this,
					getResources().getString(R.string.str_content_empty),
					Toast.LENGTH_SHORT).show();
			return;
		}
		NewQuestionItemEntity entity = new NewQuestionItemEntity();
		entity.setUserId(mUserBean.getUserId());
		entity.setId("0");
		entity.setSubjectName(mEtBranch.getText().toString());
		entity.setReplyQuestionId(mQuestionId);
		entity.setReplyMarkId("0");
		// entity.setTitle(mEtTitle.getText().toString());
		entity.setContent(mEtContent.getText().toString());
		entity.setKeywords(mEtDifficultPoint.getText().toString());
		getImageIds();
		entity.setImagesIds(mImageIds);
		entity.setType("1");
		entity.setSubmitStatus("1");
		submitNewRuestion(entity);
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
										Toast.makeText(
												NewQAActivity.this,
												getResources()
														.getString(
																R.string.str_newquestion_success),
												Toast.LENGTH_SHORT).show();
										setResult(RESULT_OK);
										finish();
									} else {
										Toast.makeText(NewQAActivity.this,
												obj.getMsgInfo(),
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(
											NewQAActivity.this,
											getResources().getString(
													R.string.str_tips_nonet),
											Toast.LENGTH_SHORT).show();
								}

							}
						});
					}
				});
	}

	public void showPhotoDialog(final Context context) {
		myDialog.showDialog(new String[] { "照 片", "拍 照", "语 音", "视 频", "取 消" },
				new MyDialogItemClickListener() {
					@Override
					public void clickItemIndex(int index) {
						switch (index) {
						case 0:
							mCapture.dispatchPictureIntent(CapturePhoto.PICK_IMAGE);
							break;
						case 1:
							mCapture.dispatchPictureIntent(CapturePhoto.SHOT_IMAGE);
							break;
						case 2:
							mCapture.dispatchPictureIntent(CapturePhoto.SOUND_RECORD);
							break;
						case 3:
							mCapture.dispatchPictureIntent(CapturePhoto.SHOT_VIDEO);
							break;
						default:
							break;
						}
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == 11) {
			setResult(RESULT_OK);
			finish();
			return;
		}
		if (requestCode == CapturePhoto.SHOT_IMAGE
				|| requestCode == CapturePhoto.PICK_IMAGE) {
			mCapture.onActivityResult(requestCode, resultCode, data);
		} else {
			if (mCapture != null && resultCode == Activity.RESULT_OK)
				mCapture.onActivityResult(requestCode, resultCode, data);
		}
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
		mProvincePop = new PopupWindow(popView, DensityConfig.dip2px(
				NewQAActivity.this, 200), LayoutParams.WRAP_CONTENT);
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

	public void addFile(String filePath) {
		showProgressDialog();
		final BitmapUtils bitmapUtils = new BitmapUtils(NewQAActivity.this);
		if (filePath != null && !"".equals(filePath)) {
			HttpEngine.getHttpEngine(NewQAActivity.this).requestFileUploadData(
					1, new File(filePath), new HttpEngineListener() {
						@Override
						public void requestCallBack(Object data,
								String resultCode, int requestCode) {
							mFileBean = (FileBean) data;
							closeProgressDialog();

						}
					});
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
