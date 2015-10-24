package flytv.qaonline.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import flytv.compos.run.R;
import flytv.compos.run.bean.FileBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.UserShareUtils;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.entity.QADetailItemEntity;
import flytv.qaonline.entity.ResultEntity;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.utils.CapturePhoto;
import flytv.qaonline.utils.CapturePhoto.PhotoIntentListener;
import flytv.qaonline.utils.Mp3PlayEngine;
import flytv.qaonline.utils.MyUtils;
import flytv.qaonline.view.LocatFileView;
import flytv.qaonline.view.LocatFileView.LocatFileViewClick;
import flytv.qaonline.view.QADetailView;
import flytv.qaonline.view.QADetailView.QADetailViewClick;
import flytv.qaonline.view.TitleView;
import flytv.qaonline.view.TitleView.TitleClickListener;
import flytv.run.bean.TVCodeBean;

public class QADetailActivity extends BaseActivity {
	private TitleView mTitleView;
	private ImageView mLeftIv;
	private ImageView mRightIv;
	private View mTeachControlView;
	private Button mTakePhotoBtn;
	private Button mRecordBtn;
	private Button mVideoBtn;

	private View mQADifView;
	private ImageView mIvEasy;
	private ImageView mIvNomal;
	private ImageView mIvDifficult;
	private EditText mEtReply;
	private LinearLayout mBottomLl;
	private LinearLayout mReplyLl;
	private LocatFileView mLocatFileView;
	private TextView mTvReplyTitle;
	
	private ArrayList<FileBean> mImageDatas;
	private ArrayList<FileBean> mVideoDatas;
	private ArrayList<FileBean> mRecordDatas;

	private Button mBtSubmit;
	private Button mStudBtSubmit;
	private QADetailView mContentView;
	private TVCodeBean mUserEntity;
	private boolean isTeacherType = false;
	private CapturePhoto mCapture;
	private List<QADetailItemEntity> mQADetailEntitys;
	
	private String mImageIds="";
	private String mVoiceIds="";
	private String mVideoIds="";
	
	private boolean isEditMode = true;
	private int id;
	private int questionId;
	private int status;
	private int markStatus;
	private int score;
	private int shareStatus;
	private boolean isReturnRefresh = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_qadetail_main);
		isEditMode = getIntent().getBooleanExtra("isEidtMode",true);
		initPageView();
	}

	private void initPageView() {
		mCapture = new CapturePhoto(this, new PhotoIntentListener() {
			@Override
			public void finishPhotoIntent(String filePath, Bitmap fileBitmap,
					int type) {
				switch (type) {
				case CapturePhoto.CORP_IMAGE:
					if (filePath != null && !"".equals(filePath)) {
						showProgressDialog();
						HttpEngine.getHttpEngine(QADetailActivity.this)
								.requestFileUploadData(0, new File(filePath),
										new HttpEngineListener() {
											@Override
											public void requestCallBack(
													Object data,
													String resultCode,
													int requestCode) {
												final FileBean fileBean = (FileBean) data;
												if (fileBean != null) {
													runOnUiThread(new Runnable() {
														public void run() {
															closeProgressDialog();
															mImageDatas.add(fileBean);
															mLocatFileView.addImageView(fileBean);
														}
													});
												}
											}
										});
					}
					break;
				case CapturePhoto.SHOT_VIDEO:
					if (filePath != null && !"".equals(filePath)) {
						showProgressDialog();
						HttpEngine.getHttpEngine(QADetailActivity.this)
								.requestFileUploadData(1, new File(filePath),
										new HttpEngineListener() {
											@Override
											public void requestCallBack(
													Object data,
													String resultCode,
													int requestCode) {
												final FileBean fileBean = (FileBean) data;
												if (fileBean != null) {
													runOnUiThread(new Runnable() {
														public void run() {
															closeProgressDialog();
															mVideoDatas.add(fileBean);
															mLocatFileView.addVideoView(fileBean);
														}
													});
												}
											}
										});
					}
					break;
				case CapturePhoto.SOUND_RECORD:
					if (filePath != null && !"".equals(filePath)) {
						showProgressDialog();
						HttpEngine.getHttpEngine(QADetailActivity.this)
								.requestFileUploadData(2, new File(filePath),
										new HttpEngineListener() {
											@Override
											public void requestCallBack(
													Object data,
													String resultCode,
													int requestCode) {
												final FileBean fileBean = (FileBean) data;
												if (fileBean != null) {
													runOnUiThread(new Runnable() {
														public void run() {
															closeProgressDialog();
															mRecordDatas.add(fileBean);
															mLocatFileView.addMp3View(fileBean);
														}
													});
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

		mTitleView = (TitleView) findViewById(R.id.qa_detaile_title_view);
		mLeftIv = (ImageView) findViewById(R.id.qadetail_s_finish_iv);
		mRightIv = (ImageView) findViewById(R.id.qadetail_s_next_iv);
		mTeachControlView = findViewById(R.id.qadetail_teachcon_layout);
		mTvReplyTitle=(TextView) findViewById(R.id.tv_title_reply);
		mImageDatas = new ArrayList<FileBean>();
		mVideoDatas = new ArrayList<FileBean>();
		mRecordDatas = new ArrayList<FileBean>();
		mQADifView = findViewById(R.id.qa_detaile_dif_view);
		mIvEasy = (ImageView) findViewById(R.id.iv_easy);
		mIvNomal = (ImageView) findViewById(R.id.iv_nomal);
		mIvDifficult = (ImageView) findViewById(R.id.iv_diffcult);
		mBtSubmit = (Button) findViewById(R.id.bt_submit);
		mStudBtSubmit = (Button) findViewById(R.id.qadetail_student_reassubmit_btn);
		mEtReply = (EditText) findViewById(R.id.et_replycontent);
		mTakePhotoBtn = (Button) findViewById(R.id.qadetail_t_takephoto_btn);
		mRecordBtn = (Button) findViewById(R.id.qadetail_t_record_btn);
		mVideoBtn = (Button) findViewById(R.id.qadetail_t_video_btn);
		mBottomLl = (LinearLayout) findViewById(R.id.qa_detaile_bottom_view);
		mReplyLl = (LinearLayout) findViewById(R.id.ll_replycontent);
		mContentView = (QADetailView) findViewById(R.id.qadetail_content_view);
		mLocatFileView = (LocatFileView) findViewById(R.id.qadetail_locatfile_view);

		mTitleView.setTitleValue(R.string.str_return, R.string.str_qa_detail,"");
		mUserEntity = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				this);
		if (mUserEntity != null && "1".equals(mUserEntity.getUserType())) {// teacher
			isTeacherType = true;
		} else {
			isTeacherType = false;
		}

		mTitleView.setTitleClickListener(new TitleClickListener() {
			@Override
			public void onRightClick(View view) {

			}

			@Override
			public void onLeftClick(View view) {
				QADetailActivity.this.finish();
			}
		});

		mContentView.setQADetailViewClick(new QADetailViewClick() {
			@Override
			public void clickVideo(FileBean fileIetmEntity) {
				if (fileIetmEntity != null)
					MyUtils.startResource(QADetailActivity.this,AppUtil.UPLOADPATH + "/"+fileIetmEntity.getFileUrl());
			}

			@Override
			public void clickImage(FileBean fileIetmEntity) {
				if (fileIetmEntity != null)
					MyUtils.startResource(QADetailActivity.this,AppUtil.UPLOADPATH + "/"+fileIetmEntity.getFileUrl());
			}

			@Override
			public void clickMp3(FileBean fileIetmEntity) {
				 if(fileIetmEntity != null)
					 Mp3PlayEngine.getMp3PlayEngine().playSound(AppUtil.UPLOADPATH + "/"+fileIetmEntity.getFileUrl());
			}
		});
		mLocatFileView.setLocatFileViewClick(new LocatFileViewClick() {
			@Override
			public void clickVideo(FileBean fileIetmEntity) {
				if (fileIetmEntity != null)
					MyUtils.startResource(QADetailActivity.this,fileIetmEntity.getFileUrl());
			}

			@Override
			public void clickImage(FileBean fileIetmEntity) {
				if (fileIetmEntity != null)
					MyUtils.startResource(QADetailActivity.this,fileIetmEntity.getFileUrl());
			}

			@Override
			public void clickMp3(FileBean fileIetmEntity) {
				 if(fileIetmEntity != null)
					 Mp3PlayEngine.getMp3PlayEngine().playSound(fileIetmEntity.getFileUrl());
			}
			
			@Override
			public void delFile(FileBean fileIetmEntity,int type) {
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
		mVideoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCapture.dispatchPictureIntent(CapturePhoto.SHOT_VIDEO);
			}
		});
		mRecordBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mCapture.dispatchPictureIntent(CapturePhoto.SOUND_RECORD);
			}
		});
		mTakePhotoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPhotoDialog(QADetailActivity.this);
			}
		});
		loadDetailData();
	}
	
	private void loadDetailData() {
		showDataDialog();
		mContentView.removeAllViews();
		HttpEngine.getHttpEngine(this).requestQADetailtData(
				mUserEntity != null ? mUserEntity.getUserId() : "",
				getIntent().getStringExtra("questionId"),
				new HttpEngineListener() {
					@Override
					public void requestCallBack(Object data, String resultCode,
							int requestCode) {
						closeDataDialog();
						mQADetailEntitys = (List<QADetailItemEntity>) data;
						if (mQADetailEntitys != null
								&& !mQADetailEntitys.isEmpty()) {
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									status = mQADetailEntitys.get(0).getStatus();
									score = mQADetailEntitys.get(0).getScore();
									shareStatus = mQADetailEntitys.get(0).getShareStatus();
									markStatus = mQADetailEntitys.get(0).getMarkStatus();
									if (mQADetailEntitys.size() >= 1) {
										for (int i = 0; i < mQADetailEntitys
												.size(); i++) {
											String title = mQADetailEntitys
													.get(i).getTitle();
											String content = mQADetailEntitys
													.get(i).getContent();
											List<FileBean> images = new ArrayList<FileBean>();
											List<FileBean> videos = new ArrayList<FileBean>();
											List<FileBean> mp3s = new ArrayList<FileBean>();
											for (FileBean fileEntity : mQADetailEntitys
													.get(i).getFileList()) {
												if (fileEntity
														.getName()
														.toLowerCase()
														.contains("mp4")) {
													videos.add(fileEntity); // getString(R.string.flytv_file)
																			// +
																			// fileEntity.thumbPath
												} else if(fileEntity
														.getName()
														.toLowerCase()
														.contains("mp3")) {
													mp3s.add(fileEntity);// fileEntity.name
												} else {
													images.add(fileEntity);// getString(R.string.flytv_file)
																			// +
																			// fileEntity.fileUrl
												}
											}
											if ("".equals(content)
													&& images.size() == 0
													&& videos.size() == 0
													&& mp3s.size() == 0) {
												mContentView.addItem("", "",
														images, videos, mp3s);
											} else {
												mContentView.addItem(title,
														content, images,
														videos, mp3s);
											}
										}
									}
									setViewDisPlay();
								}
							});
						}else{
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(QADetailActivity.this, "获取数据失败", Toast.LENGTH_SHORT).show();
								}
							});
						}
					}
				});
	}

	private void setViewDisPlay(){
		if(isEditMode){
			boolean hasTestAnswer = false;
			boolean hasKaoyiKao = false;
			boolean hasAswKaoyiKao = false;
			if(mQADetailEntitys != null && !mQADetailEntitys.isEmpty()){
				QADetailItemEntity itemEntity =  mQADetailEntitys.get(mQADetailEntitys.size() -1);
				if(itemEntity.getKey() == 5){
					hasTestAnswer = true;
				}
				if(itemEntity.getKey() == 4){
					hasKaoyiKao = true;
				}
				if(itemEntity.getKey() == 5){
					hasAswKaoyiKao = true;
				}
				id = itemEntity.getId();
				questionId = itemEntity.getQuestionId();
			}
			final boolean hasTestAnswer1 = hasTestAnswer;
			if(isTeacherType){
				if(markStatus == 1){ // 未解决
					mBottomLl.setVisibility(View.GONE);
					mQADifView.setVisibility(View.VISIBLE);
					mReplyLl.setVisibility(View.VISIBLE);
					checkDfIv(score);
					if(hasTestAnswer){ //设置成评语
						mTvReplyTitle.setText("评语");
					}
					mIvEasy.setEnabled(true);
					mIvNomal.setEnabled(true);
					mIvDifficult.setEnabled(true);
					mBtSubmit.setVisibility(View.VISIBLE);
					mIvEasy.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							checkDfIv(0);
							submitScore(0);
						}
					});
					mIvNomal.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							checkDfIv(1);
							submitScore(1);
						}
					});
					mIvDifficult.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							checkDfIv(2);
							submitScore(2);
						}
					});
					mBtSubmit.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) { // 提交问题答案
							if(hasTestAnswer1){
								markTestQuestion();
							}else{
								markQuestion();
							}
						}
					});
				}else if(markStatus == 2){ // 已解决
					mReplyLl.setVisibility(View.GONE);
					mQADifView.setVisibility(View.VISIBLE);
					mBtSubmit.setVisibility(View.GONE);
					mIvEasy.setEnabled(false);
					mIvNomal.setEnabled(false);
					mIvDifficult.setEnabled(false);
					checkDfIv(score);
					if(status == 1){ // 分享
						mBottomLl.setVisibility(View.GONE);
					}else if(status == 2){
						mQADifView.setVisibility(View.VISIBLE);
						mReplyLl.setVisibility(View.GONE);
						if(shareStatus == 1){
							mBottomLl.setVisibility(View.GONE);
						}else{
							mBottomLl.setVisibility(View.VISIBLE);
							mLeftIv.setImageResource(R.drawable.qa_test);
							mRightIv.setImageResource(R.drawable.qa_share);
							if(hasKaoyiKao){
								mRightIv.setVisibility(View.GONE);
								if(hasAswKaoyiKao){
									mLeftIv.setVisibility(View.VISIBLE);
								}else{
									mLeftIv.setVisibility(View.GONE);
									mBottomLl.setVisibility(View.GONE);
								}
							}else{
								mRightIv.setVisibility(View.VISIBLE);
								mLeftIv.setVisibility(View.VISIBLE);
							}
							mLeftIv.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(View v) {
									testQuestion();
								}
							});	
							mRightIv.setOnClickListener(new OnClickListener(){
								@Override
								public void onClick(View v) {
									shareQuestion();
								}
							});	
						}
					}else{ // 分享-考一考
						mBottomLl.setVisibility(View.VISIBLE);
						mRightIv.setVisibility(View.VISIBLE);
						mLeftIv.setVisibility(View.VISIBLE);
						mLeftIv.setImageResource(R.drawable.qa_test);
						mRightIv.setImageResource(R.drawable.qa_share);
						mRightIv.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View v) {
								shareQuestion();
							}
						});	
						mLeftIv.setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View v) {
								testQuestion();
							}
						});	
					}
				}else if(markStatus == 3){ // 回答追问
					mBottomLl.setVisibility(View.GONE);
					mQADifView.setVisibility(View.GONE);
					mReplyLl.setVisibility(View.GONE);
					mBtSubmit.setVisibility(View.GONE);
				}else{
					mReplyLl.setVisibility(View.GONE);
					if (status == 2) {
						mBtSubmit.setVisibility(View.GONE);
						mQADifView.setVisibility(View.VISIBLE);
						mBottomLl.setVisibility(View.GONE);
					}else {
						mBottomLl.setVisibility(View.VISIBLE);
						mQADifView.setVisibility(View.VISIBLE);
					}
					checkDfIv(score);
					mRightIv.setVisibility(View.GONE);
					mLeftIv.setVisibility(View.VISIBLE);
					mLeftIv.setImageResource(R.drawable.qa_share);
					mRightIv.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							testQuestion();
						}
					});	
					mLeftIv.setOnClickListener(new OnClickListener(){
						@Override
						public void onClick(View v) {
							shareQuestion();
						}
					});	
				}
			}else{
				mBtSubmit.setVisibility(View.GONE);
				mQADifView.setVisibility(View.GONE);
				if(markStatus == 2 && status!=2){
					mReplyLl.setVisibility(View.GONE);
					mBottomLl.setVisibility(View.VISIBLE);
					mLeftIv.setVisibility(View.VISIBLE);
					mRightIv.setVisibility(View.VISIBLE);
					mLeftIv.setImageResource(R.drawable.qa_finish);
					mRightIv.setImageResource(R.drawable.qa_next);
					mRightIv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							addQuestion();
						}
					});
					mLeftIv.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							finishQuestion();
						}
					});
				}else if(markStatus == 3){// 显示对话框 缺少个提交按钮
					mReplyLl.setVisibility(View.VISIBLE);
					mBottomLl.setVisibility(View.GONE);
					mStudBtSubmit.setVisibility(View.VISIBLE);
					mStudBtSubmit.setOnClickListener(new OnClickListener() { // 
						@Override
						public void onClick(View v) {
							aswTestQuestion();
						}
					});
				}else{
					mReplyLl.setVisibility(View.GONE);
					mBottomLl.setVisibility(View.GONE);
				} 
			}
		}else{
			mReplyLl.setVisibility(View.GONE);
			mQADifView.setVisibility(View.GONE);
			mBottomLl.setVisibility(View.GONE);
		}
	}
	
	private void checkDfIv(int score){
		switch (score) {
		 	case 0:
		 		mIvEasy.setImageResource(R.drawable.bg_select);
		 		mIvNomal.setImageResource(R.drawable.bg_select_not);
		 		mIvDifficult.setImageResource(R.drawable.bg_select_not);
		 		break;
		 	case 1:
		 		mIvEasy.setImageResource(R.drawable.bg_select_not);
		 		mIvNomal.setImageResource(R.drawable.bg_select);
		 		mIvDifficult.setImageResource(R.drawable.bg_select_not);
		 		break;
		 	case 2:
		 		mIvEasy.setImageResource(R.drawable.bg_select_not);
		 		mIvNomal.setImageResource(R.drawable.bg_select_not);
		 		mIvDifficult.setImageResource(R.drawable.bg_select);
		 		break;
		 	default:
		 		break;
		}
	}
	
	private void addQuestion(){
		Intent intent =new Intent();
		intent.setClass(QADetailActivity.this, TestActivity.class);
		NewQuestionItemEntity entity = new NewQuestionItemEntity();
		entity.setUserId(mUserEntity.getUserId());
		entity.setId("0");
		entity.setSubjectName(getIntent().getStringExtra("subjectname"));
		entity.setReplyQuestionId(getIntent().getStringExtra("questionId"));
		entity.setReplyMarkId(""+id);
		entity.setTitle("");
		entity.setType("2");
		entity.setSubmitStatus("1");
		intent.putExtra("entity", entity);
		intent.putExtra("istest", false);
		startActivityForResult(intent, 2);
	}
	private void getFileIds(){
		mImageIds="";
		mVoiceIds="";
		mVideoIds="";
		for (int i = 0; i < mImageDatas.size(); i++) {
			mImageIds+=mImageDatas.get(i).getId()+",";
		}
		for (int i = 0; i < mRecordDatas.size(); i++) {
			mVoiceIds+=mRecordDatas.get(i).getId()+",";
		}
		for (int i = 0; i < mVideoDatas.size(); i++) {
			mVideoIds+=mVideoDatas.get(i).getId()+",";
		}
	}
	/*
	 *评语 
	 */
	private void markTestQuestion(){
		if (StringUtils.isBlank(mEtReply.getText())) {
			Toast.makeText(this,"请输入评语内容", Toast.LENGTH_LONG).show();
			return;
		}
		showDataDialog();
		getFileIds();
		HttpEngine.getHttpEngine(this).markTestQuestion(mUserEntity.getUserId(), questionId, 
				id, mEtReply.getText().toString(),
				mImageIds, mVoiceIds, mVideoIds, new HttpEngineListener() {
			@Override
			public void requestCallBack(Object data, String resultCode, int requestCode) {
				final ResultEntity obj = (ResultEntity) data;
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						closeDataDialog();
						if (obj != null) {
							if ("1".equals(obj.getMessage())) {
								Toast.makeText(
										QADetailActivity.this,"评论成功",
										Toast.LENGTH_SHORT).show();
								setResult(RESULT_OK);
								QADetailActivity.this.finish();
							} else {
								Toast.makeText(QADetailActivity.this,
										obj.getMsgInfo(),
										Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(
									QADetailActivity.this,
									getResources().getString(
											R.string.str_tips_nonet),
											Toast.LENGTH_SHORT).show();
						}
					}
				});
				
			}
		});
	}
	
	private void aswTestQuestion(){
		if (StringUtils.isBlank(mEtReply.getText())) {
			Toast.makeText(this,"请输入解答内容", Toast.LENGTH_LONG).show();
			return;
		}
		showDataDialog();
		getFileIds();
		HttpEngine.getHttpEngine(this).aswTestQuestion(mUserEntity.getUserId(), questionId, 
				id, mEtReply.getText().toString(),
				mImageIds, mVoiceIds, mVideoIds, new HttpEngineListener() {
					@Override
					public void requestCallBack(Object data, String resultCode, int requestCode) {
						final ResultEntity obj = (ResultEntity) data;
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								closeDataDialog();
								if (obj != null) {
									if ("1".equals(obj.getMessage())) {
										Toast.makeText(
												QADetailActivity.this,"解答成功",
												Toast.LENGTH_SHORT).show();
										setResult(RESULT_OK);
										QADetailActivity.this.finish();
									} else {
										Toast.makeText(QADetailActivity.this,
												obj.getMsgInfo(),
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(
											QADetailActivity.this,
											getResources().getString(
													R.string.str_tips_nonet),
													Toast.LENGTH_SHORT).show();
								}
							}
						});

					}
				});
	}
	
	private void testQuestion(){
		Intent intent=new Intent();
		intent.setClass(QADetailActivity.this,TestActivity.class);
		intent.putExtra("questionId", getIntent().getStringExtra("questionId"));
		startActivityForResult(intent, 1);
	}
	
	/**
	 * 解答问题提交
	 */
	private void markQuestion(){
		if (StringUtils.isBlank(mEtReply.getText())) {
			Toast.makeText(this,"请输入解答内容", Toast.LENGTH_LONG).show();
			return;
		}
		String userId=mUserEntity.getUserId();
//		String questionId = getIntent().getStringExtra("questionId");
		showDataDialog();
		getFileIds();
		HttpEngine.getHttpEngine(this).markQuestion(userId, questionId, id,"", mEtReply.getText().toString(), "1", mImageIds, mVideoIds, mVideoIds, "", 2, new HttpEngineListener() {
			@Override
			public void requestCallBack(Object data, String resultCode, int requestCode) {
				ResultEntity entity  = (ResultEntity)data;
				if(entity != null && "1".equals(entity.getMessage())){
					runOnUiThread(new Runnable() {
						public void run() {
							closeDataDialog();
							Toast.makeText(QADetailActivity.this, "批阅成功", Toast.LENGTH_SHORT).show();
							loadDetailData();
							setResult(RESULT_OK);
							finish();
						}
					});
				}else{
					runOnUiThread(new Runnable() {
						public void run() {
							closeDataDialog();
							Toast.makeText(QADetailActivity.this, "批阅失败", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		});
	}
	
	private void submitScore(int score){
		showDataDialog();
		HttpEngine.getHttpEngine(QADetailActivity.this).setQuestionScore(
				Integer.parseInt(getIntent().getStringExtra("questionId")),
				score, new HttpEngineListener() {
					@Override
					public void requestCallBack(Object data, String resultCode, int requestCode) {
						ResultEntity entity  = (ResultEntity)data;
						if(entity != null && "1".equals(entity.getMessage())){
							runOnUiThread(new Runnable() {
								public void run() {
									closeDataDialog();
									Toast.makeText(QADetailActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
								}
							});
						}else{
							runOnUiThread(new Runnable() {
								public void run() {
									closeDataDialog();
									Toast.makeText(QADetailActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
								}
							});
						}
					}
				});
	}
	
	private void finishQuestion(){
		showDataDialog();
		HttpEngine.getHttpEngine(QADetailActivity.this).endQuestion(getIntent().getStringExtra("questionId"),
				new HttpEngineListener() {
					@Override
					public void requestCallBack(Object data,
							String resultCode, int requestCode) {
						// TODO Auto-generated method stub
						final ResultEntity obj = (ResultEntity) data;
						QADetailActivity.this
								.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										// TODO
										// Auto-generated
										// method
										// stub
										closeDataDialog();
										if ("1".equals(obj.getMessage())) {
											Toast.makeText(
													QADetailActivity.this,
													"恭喜你已经学会了！",
													Toast.LENGTH_SHORT)
													.show();
											setResult(RESULT_OK);
											finish();
										} else {
											Toast.makeText(
													QADetailActivity.this,
													obj.getMsgInfo(),
													Toast.LENGTH_SHORT)
													.show();
										}
									}
								});
					}
				});
	}
	
	private void shareQuestion(){
		showDataDialog();
		HttpEngine.getHttpEngine(QADetailActivity.this).shareQuestion(
				getIntent().getStringExtra("questionId"),
				new HttpEngineListener() {
					@Override
					public void requestCallBack(Object data,
							String resultCode, int requestCode) {
						// TODO Auto-generated method stub
						final ResultEntity obj = (ResultEntity) data;
						QADetailActivity.this
								.runOnUiThread(new Runnable() {
									@Override
									public void run() {
										// TODO
										// Auto-generated
										// method
										// stub
										closeDataDialog();
										if ("1".equals(obj.getMessage())) {
											Toast.makeText(
													QADetailActivity.this,
													"分享成功",
													Toast.LENGTH_SHORT)
													.show();
											finish();
										} else {
											Toast.makeText(
													QADetailActivity.this,
													obj.getMsgInfo(),
													Toast.LENGTH_SHORT)
													.show();
										}
									}
								});
					}
				});
	}
	
	public void showPhotoDialog(final Context context) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context)
				.setTitle("设置图像")
				.setItems(new String[] { "选择本地图片", "拍照" },
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								switch (which) {
								case 0:
									mCapture.dispatchPictureIntent(CapturePhoto.PICK_IMAGE);
									break;
								case 1:
									mCapture.dispatchPictureIntent(CapturePhoto.SHOT_IMAGE);
									break;
								default:
									break;
								}
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			if(requestCode == 1){ // 考一考
//				loadDetailData();
//				QAMainActivity.mustRefresh = true;
				finish();
				return;
			}else if(requestCode == 2){
//				QAMainActivity.mustRefresh = true;
//				loadDetailData();
				finish();
			}
		}
		if (requestCode == CapturePhoto.SHOT_IMAGE
				|| requestCode == CapturePhoto.PICK_IMAGE) {
			mCapture.onActivityResult(requestCode, resultCode, data);
		} else {
			if (mCapture != null && resultCode == Activity.RESULT_OK)
				mCapture.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		Mp3PlayEngine.getMp3PlayEngine().stop();
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
