package flytv.qaonline.ui;

import java.io.File;
import java.util.ArrayList;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import flytv.compos.run.R;
import flytv.compos.run.bean.FileBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.UserShareUtils;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.entity.ResultEntity;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.utils.CapturePhoto;
import flytv.qaonline.utils.CapturePhoto.PhotoIntentListener;
import flytv.qaonline.utils.Mp3PlayEngine;
import flytv.qaonline.utils.MyUtils;
import flytv.qaonline.view.LocatFileView;
import flytv.qaonline.view.LocatFileView.LocatFileViewClick;
import flytv.qaonline.view.TitleView;
import flytv.qaonline.view.TitleView.TitleClickListener;
import flytv.run.bean.TVCodeBean;



public class TestActivity extends BaseActivity{
	private Button mSubmitBt;
	private EditText mContentEt;
	private LocatFileView mLocatFileView;
	private CapturePhoto mCapture;
	private ImageView mAddView;
	private ArrayList<FileBean> mImageDatas;
	private ArrayList<FileBean> mVideoDatas;
	private ArrayList<FileBean> mRecordDatas;
	
	private String mImageIds="";
	private String mVoiceIds="";
	private String mVideoIds="";
	
	private TitleView mTitleView;
	private TVCodeBean mUserEntity;
	private String mQuestionId;
	private String mReplyMarkId;
	private boolean mIsTest;
	private NewQuestionItemEntity entity;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		mImageDatas = new ArrayList<FileBean>();
		mVideoDatas = new ArrayList<FileBean>();
		mRecordDatas = new ArrayList<FileBean>();
		mUserEntity = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				this);
		mQuestionId = getIntent().getStringExtra("questionId");
		mReplyMarkId = getIntent().getStringExtra("replyMarkId");
		mIsTest = getIntent().getBooleanExtra("istest", true);
		entity = (NewQuestionItemEntity) getIntent().getExtras().getSerializable("entity");
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		mCapture = new CapturePhoto(this, new PhotoIntentListener() {
			@Override
			public void finishPhotoIntent(String filePath, Bitmap fileBitmap,
					int type) {
				switch (type) {
				case CapturePhoto.CORP_IMAGE:
					if (filePath != null && !"".equals(filePath)) {
						showDataDialog();
						HttpEngine.getHttpEngine(TestActivity.this)
								.requestFileUploadData(0, new File(filePath),
										new HttpEngineListener() {
											@Override
											public void requestCallBack(
													Object data,
													String resultCode,
													int requestCode) {
												closeDataDialog();
												FileBean fileBean = (FileBean) data;
												if (fileBean != null) {
													mImageDatas.add(fileBean);
//													mImageIds+=fileBean.getId()+",";
													mLocatFileView.addImageView(fileBean);
												}
											}
										});
					}
					break;
				case CapturePhoto.SHOT_VIDEO:
					showDataDialog();
					if (filePath != null && !"".equals(filePath)) {
						HttpEngine.getHttpEngine(TestActivity.this)
								.requestFileUploadData(1, new File(filePath),
										new HttpEngineListener() {
											@Override
											public void requestCallBack(
													Object data,
													String resultCode,
													int requestCode) {
												closeDataDialog();
												FileBean fileBean = (FileBean) data;
												if (fileBean != null) {
													mVideoDatas.add(fileBean);
//													mVideoIds+=fileBean.getId()+",";
													mLocatFileView.addVideoView(fileBean);
												}
											}
										});
					}
					break;
				case CapturePhoto.SOUND_RECORD:
					showDataDialog();
					if (filePath != null && !"".equals(filePath)) {
						HttpEngine.getHttpEngine(TestActivity.this)
								.requestFileUploadData(2, new File(filePath),
										new HttpEngineListener() {
											@Override
											public void requestCallBack(
													Object data,
													String resultCode,
													int requestCode) {
												closeDataDialog();
												FileBean fileBean = (FileBean) data;
												if (fileBean != null) {
													mRecordDatas.add(fileBean);
//													mVideoIds+=fileBean.getId()+",";
													mLocatFileView.addMp3View(fileBean);
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
		mLocatFileView=(LocatFileView) findViewById(R.id.test_locatfile_view);
		mSubmitBt=(Button) findViewById(R.id.test_bt);
		mContentEt=(EditText) findViewById(R.id.et_content);
		mAddView=(ImageView) findViewById(R.id.iv_add_image);
		mTitleView = (TitleView) findViewById(R.id.qa_new_title_view);
		if (mIsTest) {
			mTitleView.setTitleValue(R.string.str_return, R.string.str_qa_test,"");
		}else {
			mTitleView.setTitleValue(R.string.str_return, R.string.str_qa_nextq,"");
		}
		
		mTitleView.setTitleClickListener(new TitleClickListener() {
			@Override
			public void onRightClick(View view) {

			}

			@Override
			public void onLeftClick(View view) {
				TestActivity.this.finish();
			}
		});
		mAddView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPhotoDialog(TestActivity.this);
			}
		});
		
		mLocatFileView.setLocatFileViewClick(new LocatFileViewClick() {
			@Override
			public void clickVideo(FileBean fileIetmEntity) {
				if (fileIetmEntity != null)
					MyUtils.startResource(TestActivity.this,fileIetmEntity.getFileUrl());
			}

			@Override
			public void clickImage(FileBean fileIetmEntity) {
				if (fileIetmEntity != null)
					MyUtils.startResource(TestActivity.this,fileIetmEntity.getFileUrl());
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
		
		
		mSubmitBt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (mIsTest) {
					submitTest();
				}else {
					submitZhuiWen();
				}
			}

		});
		
		
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
	private void submitTest() {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(mContentEt.getText())) {
			Toast.makeText(this,"请输入解答内容", Toast.LENGTH_LONG).show();
			return;
		}
		getFileIds();
		String userId=mUserEntity.getUserId();
		HttpEngine.getHttpEngine(this).testQuestion(mUserEntity.getUserId(), mContentEt.getText().toString(), mQuestionId, mImageIds, mVoiceIds, mVideoIds, new HttpEngineListener(){
			
			@Override
			public void requestCallBack(Object data, String resultCode, int requestCode) {
				// TODO Auto-generated method stub
				final ResultEntity obj = (ResultEntity) data;
				TestActivity.this
						.runOnUiThread(new Runnable() {
							@Override
							public void run() {
								// TODO
								// Auto-generated
								// method
								// stub
								if (obj!=null) {
									if ("1".equals(obj.getMessage())) {
										Toast.makeText(TestActivity.this,"考一考成功",Toast.LENGTH_SHORT).show();
										setResult(Activity.RESULT_OK);
										finish();
									} else {
										Toast.makeText(TestActivity.this,"考一考失败",Toast.LENGTH_SHORT).show();
									}
								}else {
									Toast.makeText(TestActivity.this,"考一考失败",Toast.LENGTH_SHORT).show();
								}
							}
						});
			}
		});
	}
	
	private void submitZhuiWen(){
		if (StringUtils.isBlank(mContentEt.getText())) {
			Toast.makeText(this,"请输入解答内容", Toast.LENGTH_LONG).show();
			return;
		}
		String userId=mUserEntity.getUserId();
		getFileIds();
		entity.setContent(mContentEt.getText().toString());
		entity.setImagesIds(mImageIds+mVideoIds+mVoiceIds);
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
												TestActivity.this,
												getResources()
														.getString(
																R.string.str_newquestion_success),
												Toast.LENGTH_SHORT).show();
										setResult(RESULT_OK);
										finish();
									} else {
										Toast.makeText(TestActivity.this,
												obj.getMsgInfo(),
												Toast.LENGTH_SHORT).show();
									}
								} else {
									Toast.makeText(
											TestActivity.this,
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
		AlertDialog.Builder alert = new AlertDialog.Builder(context).setItems(
				new String[] { "照片", "拍照", "语音", "视频" },
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
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
				}).setNegativeButton("取消",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		alert.show();
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == CapturePhoto.SHOT_IMAGE || requestCode == CapturePhoto.PICK_IMAGE){
			mCapture.onActivityResult(requestCode, resultCode, data);
		}else{
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
