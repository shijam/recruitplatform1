package flytv.compos.run.control.compos;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import flytv.compos.run.R;
import flytv.compos.run.adapter.AdComposAdd;
import flytv.compos.run.bean.AnswerBean;
import flytv.compos.run.bean.CommonMsg;
import flytv.compos.run.bean.ComposBean;
import flytv.compos.run.bean.FileBean;
import flytv.compos.run.bean.QuestionBean;
import flytv.compos.run.bean.StuComposBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AlertHelp;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.FileUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.SWDateUtil;
import flytv.ext.utils.UserShareUtils;
import flytv.run.bean.TVCodeBean;

/**
 * 添加/查看 pdf 信息(student/tearcher)
 * 
 * @author nike
 * 
 */
public class AyComposPDFMark extends BaseActivity {
	@ViewInject(R.id.ivTitleBtnLeft)
	private ImageButton ivTitleBtnLeft;

	@ViewInject(R.id.ivTitleBtnRight)
	private Button ivTitleBtnRight;

	@ViewInject(R.id.button_grade)
	private Button button_grade;

	@ViewInject(R.id.btn_marl_save)
	private Button btn_marl_save;

	@ViewInject(R.id.ivTitleName)
	private TextView ivTitleName;
	@ViewInject(R.id.tx_right)
	private TextView tx_right;
	private TVCodeBean loginInfo;
	private ComposBean composStudent;

	@ViewInject(R.id.grid_image)
	private GridView grid_image;

	@ViewInject(R.id.grid_all_image)
	private GridView grid_all_image;

	@ViewInject(R.id.edit_mark_context)
	private EditText edit_mark_context;

	@ViewInject(R.id.layout_mark_compos)
	private LinearLayout layout_mark_compos;
	
	private int httpFinish, httpMark;
	private StuComposBean msgCompos;
	@OnClick({ R.id.ivTitleBtnLeft, R.id.btn_sercher, R.id.tx_right,
			R.id.btn_compos_add, R.id.ed_class, R.id.ed_subject,
			R.id.btn_marl_save })
	public void onClickComment(View view) {
		switch (view.getId()) {
		// 添加评论
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		case R.id.btn_marl_save:
			sendComposShow();
			break;
		case R.id.tx_right:
			// 提交
			sendCompos();
			break;
		}
	}

	boolean isShow = false;
	boolean isStudent = false;

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

		FileBean fileBean = new FileBean();
		fileBean.setImg_typeId(1);

		adImageAdd = new AdComposAdd(context, answerList);
		adImageMark = new AdComposAdd(context, markFiles);

		grid_image.setAdapter(adImageAdd);
		grid_all_image.setAdapter(adImageMark);

		if (answerBean != null) {
			ArrayList<AnswerBean> answerList= answerBean.answerList;
			if(answerList.size()>0){
				initImage(answerBean.answerList
						.get(answerBean.answerList.size() - 1));
			}
		}
		if (loginInfo.getUserType().equals("2")) {
			isStudent = true;
			if (composStudent.getMarkStatus() == 0) {
				answerList.add(fileBean);
				layout_mark_compos.setVisibility(View.GONE);
			} else {
				isShow = true;
				layout_mark_compos.setVisibility(View.VISIBLE);
			}
			// 学生不需要总评
			httpFinish = R.string.compos_tab_answer_stu_finishMark;
			httpMark = R.string.compos_tab_answer_student_compos;
		} else {
			httpFinish = R.string.compos_tab_answer_tear_finishMark;
			httpMark = R.string.compos_tab_answer_tear_mark;
			isStudent = false;
			if (msgCompos.getStatus() == 0) {
				markFiles.add(fileBean);
			} else {
				if(msgCompos.getStatus() == 2){
					isShow = true;
				}
			
			}
			edit_mark_context.setVisibility(View.VISIBLE);
		}
		// 如果不能比及的话
		if (isShow) {
			tx_right.setVisibility(View.INVISIBLE);
			btn_marl_save.setVisibility(View.GONE);
			edit_mark_context.setEnabled(false);
		}

	}

	@Override
	protected void loadViewLayout() {
		// layout item
		setContentView(R.layout.ad_compos_pdf_mark);
		ViewUtils.inject(this);
		loginInfo = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				context);
		button_grade.setVisibility(View.INVISIBLE);
		tx_right.setVisibility(View.VISIBLE);
		ivTitleBtnRight.setVisibility(View.GONE);
		ivTitleName.setText(getString(R.string.app_tab_compos));
		composStudent = (ComposBean) getIntent().getSerializableExtra(
				"composStudent");
		answerBean = (QuestionBean) getIntent().getSerializableExtra(
				"answerBean");
		msgCompos = (StuComposBean)getIntent().getSerializableExtra("msgCompos");
	}

	private AdComposAdd adImageAdd, adImageMark;

	ArrayList<QuestionBean> items = new ArrayList<QuestionBean>();
	ArrayList<FileBean> answerList = new ArrayList<FileBean>();
	ArrayList<FileBean> markFiles = new ArrayList<FileBean>();
	QuestionBean answerBean;

	void showQuestion() {
		showDataDialog();
		int homeworkId = composStudent.getId();
		// 是否是编辑
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(R.string.compos_tab_all_answer_loadQuestion)
				+ "?homeworkId=" + homeworkId + "&userId="
				+ loginInfo.getUserId();
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
							CommonMsg msgBean = JSON.parseObject(
									responseInfo.result, CommonMsg.class);
							if (msgBean != null) {
								items.clear();
								items.addAll(msgBean.items);
								// admin
								AnswerBean answerBean = items.get(0).answerList
										.get(items.get(0).answerList.size() - 1);
								initImage(answerBean);
								String userAnswer = items.get(0).answerList.get(items.get(0).answerList.size() - 1).getMarkContent();
								edit_mark_context.setText(userAnswer);
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

	void initImage(AnswerBean answerBean) {
		String msgTest = answerBean.getMarkContent();
		edit_mark_context.setText(msgTest);
		answerList.addAll(answerBean.answerFiles);
		markFiles.addAll(answerBean.markFiles);
		if (answerList != null) {
			adImageAdd.notifyDataSetChanged();
		}
		if (markFiles != null) {
			adImageMark.notifyDataSetChanged();
		} else {

		}
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub
		if (isShow)
			showQuestion();
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		grid_image.setOnItemClickListener(onItemClickListener);
		grid_all_image.setOnItemClickListener(onItemClickListener);

	}

	private OnItemClickListener onItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			int fileSize = 0;
			if (isStudent) {
				fileSize = answerList.size();
			} else {
				fileSize = markFiles.size();
			}
			if (fileSize - 1 == arg2 && !isShow) {
				AlertHelp.showDialog(AyComposPDFMark.this);
			} else {
				// 查看图片
				FileBean fileBean;
			
				if(arg0.getId()==R.id.grid_image){
					fileBean = answerList.get(arg2);
				}else{
					fileBean = markFiles.get(arg2);
				}
				AlertHelp.startResource(AyComposPDFMark.this,
						AppUtil.getHttpImage(fileBean.getFileUrl()));
			}
		}
	};
	TVCodeBean loginResult;

	void sendCompos() {
		// 学生ID
		//
		String homeworkId = null;
		String userId = null;
		if (isStudent) {
			homeworkId = "&id=" + composStudent.getId();
			userId = loginInfo.getUserId();
		} else {
			homeworkId = "&homeworkId=" + composStudent.getId();
			userId = msgCompos.getStudentId();
		}
		String mothodUrl = getString(R.string.app_host) + getString(httpFinish)
				+ "?userId=" + userId + homeworkId;
		showDataDialog();
		LogUtils.print("test=" + mothodUrl);
		HttpUtils http = new HttpUtils();
		http.configTimeout(60000);
		http.configCurrentHttpCacheExpiry(2000);
		http.send(HttpRequest.HttpMethod.GET, mothodUrl,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							closeDataDialog();
							LogUtils.print("获取信息=" + responseInfo.result);
							if (AppUtil.isStrNull(responseInfo.result)) {
								AlertTools.showTips(context,
										R.drawable.tips_warning, "服务器连接失败!");
								return;
							}
							loginResult = AppUtil.getPerson(
									responseInfo.result, TVCodeBean.class);
							if (loginResult.getMessage() == 1) {
								// 添加成功后 返回当前实体
								Intent intent = new Intent();
								setResult(RESULT_OK, intent);
								finish();
							} else {
								AlertTools.showTips(context,
										R.drawable.tips_warning,
										loginResult.getMsgInfo());
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

	int fileIndex = 0;

	void sendComposShow() {
		// 学生ID
		// 明天需要测试 debug

		// loginBean
		if (answerBean == null) {
			return;
		}
		String msgText = edit_mark_context.getText().toString().trim();
		StringBuffer imagesIds = new StringBuffer();
		ArrayList<FileBean> fileLists = new ArrayList<FileBean>();

		if (isStudent) {
			fileLists.addAll(answerList);
		} else {
			fileLists.addAll(markFiles);
		}
		for (int i = 0; i < fileLists.size() - 1; i++) {
			FileBean files = fileLists.get(i);
			if (files.getImg_typeId() != 1) {
				if (fileIndex == 0) {
					imagesIds.append(files.getFileId());
				} else {
					imagesIds.append("," + files.getFileId());
				}
				fileIndex++;
			}
		}

		String mothodUrl = getString(R.string.app_host) + getString(httpMark)
				+ "?userId=" + loginInfo.getUserId() + "&imagesIds="
				+ imagesIds.toString() + "&homeworkId=" + composStudent.getId();
		if (isStudent) {
			// 试题ID
			mothodUrl = mothodUrl + "&questionId=" + answerBean.getId();
			ArrayList<AnswerBean> listAnswer = answerBean.answerList;
			if (listAnswer.size() > 0) {
				mothodUrl = mothodUrl + "&answerId="
						+ listAnswer.get(listAnswer.size() - 1).getId();
			}
		} else {
			mothodUrl = mothodUrl + "&markContent=" + msgText ;
			ArrayList<AnswerBean> listAnswer = answerBean.answerList;
			if (listAnswer.size() > 0) {
				mothodUrl = mothodUrl + "&answerId="
						+ listAnswer.get(listAnswer.size() - 1).getId();
			}

		}
		showDataDialog();
		LogUtils.print("test=" + mothodUrl);
		HttpUtils http = new HttpUtils();
		http.configTimeout(60000);
		http.configCurrentHttpCacheExpiry(2000);
		http.send(HttpRequest.HttpMethod.GET, mothodUrl,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							closeDataDialog();
							LogUtils.print("获取信息=" + responseInfo.result);
							if (AppUtil.isStrNull(responseInfo.result)) {
								AlertTools.showTips(context,
										R.drawable.tips_warning, "服务器连接失败!");
								return;
							}
							TVCodeBean tvCodeBean = AppUtil.getPerson(
									responseInfo.result, TVCodeBean.class);
							if (tvCodeBean.getMessage() == 1) {
								// 添加成功后 返回当前实体
								AlertTools.showTips(context,
										R.drawable.tips_warning,
										tvCodeBean.getMsgInfo());
							} else {
								AlertTools.showTips(context,
										R.drawable.tips_warning,
										tvCodeBean.getMsgInfo());
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.print(1, "onActivityResult()" + this.getClass().getName());
		switch (requestCode) {
		case 200:
			if (resultCode == Activity.RESULT_OK) {
				try {
					final Uri uri = data.getData();
					Cursor cursor = context.getContentResolver().query(uri,
							null, null, null, null);
					if (cursor != null) {
						int colunm_index = cursor
								.getColumnIndex(MediaStore.Images.Media.DATA);
						cursor.moveToFirst();
						String filePath = cursor.getString(colunm_index);
						cursor.close();
						upload(filePath);
					}
				} catch (Exception e) {
					e.printStackTrace();
					try {
						Bitmap bm = null;
						// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
						ContentResolver resolver = context.getContentResolver();
						// 此处的用于判断接收的Activity是不是你想要的那个
						Uri originalUri = data.getData(); // 获得图片的uri
						bm = MediaStore.Images.Media.getBitmap(resolver,
								originalUri);
						Bitmap bitmap = AppUtil.ResizeBitmap(bm, 720);
						if (bm.isRecycled()) {
							bm.recycle();
							System.gc();
						}
						File file = new File(FileUtil.noteSdImageFile);
						if (!file.exists()) {
							file.mkdir();
						}
						String filepath = FileUtil.noteSdImageFile
								+ "/"
								+ SWDateUtil.getFormatDateByOffset(
										"yyMMddHHssmm", 0) + ".jpg";
						File imageUrl = new File(filepath);
						if (!imageUrl.exists()) {
							imageUrl.createNewFile();
						}
						AppUtil.compressBmpToFile(bitmap, imageUrl);
						upload(filepath);
					} catch (FileNotFoundException ex) {
						ex.printStackTrace();
						AlertTools.showTips(context, R.drawable.tips_warning,
								"请选择拍照功能！选择本地图片失败！");
					} catch (IOException ee) {
						ee.printStackTrace();
						AlertTools.showTips(context, R.drawable.tips_warning,
								"请选择拍照功能！选择本地图片失败！");
					} // 显得到bitmap图片
				}

			}
			break;
		case 300:
			if (resultCode == Activity.RESULT_OK) {
				Uri fileUri = Uri.fromFile(AlertHelp.mediaFile);
				if (fileUri != null) {
					String filePath = fileUri.getPath();
					upload(filePath);
				}
			}
			break;
		case 400:
			Uri fileUri = Uri.fromFile(AlertHelp.mediaFile);
			if (fileUri != null) {
				String filePath = fileUri.getPath();
				upload(filePath);
			}
			break;
		}
	}

	void upload(String filePath) {
		LogUtils.print(1, "filePath=" + filePath);
		TVCodeBean entity = (TVCodeBean) UserShareUtils.getInstance()
				.getLoginInfo(context);
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		if (AppUtil.isStrNull(filePath)) {
			AlertTools.showTips(context, R.drawable.tips_warning, "上传路径错误");
			return;
		}
		String fileUploadUrl = null;
		if (fileName.contains("jpg") || fileName.contains("png")
				|| fileName.contains("mp4")) {
			showDataDialog();
			fileUploadUrl = AppUtil.getStringId(context)
					+ getString(R.string.flytv_user_file_uploadImage).replace(
							"{userId}", entity.getUserId());
			RequestParams params = new RequestParams();
			params.addBodyParameter("fileName", fileName);
			TVCodeBean entityFile = UserShareUtils.getInstance().getTVInfo(
					context);
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
							if (isUploading) {
								// msgTextview.setText("upload: " + current +
								// "/"+ total);
							} else {
								// msgTextview.setText("reply: " + current +
								// "/"+ total);
							}
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							// TODO Auto-generated method stub
							LogUtils.print(arg0.getMessage());
							closeDataDialog();
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							// TODO Auto-generated method stub
							LogUtils.print("arg0=" + arg0.result);
							closeDataDialog();
							TVCodeBean tvCodeBean = (TVCodeBean) AppUtil
									.getJSONBean(arg0.result, TVCodeBean.class);
							if (tvCodeBean != null) {
								FileBean fileBean = new FileBean();
								fileBean.setImg_typeId(0);
								fileBean.setFileUrl(AppUtil.getIPSplit(
										context,
										AppUtil.UPLOADPATH + "/"
												+ tvCodeBean.getFilePath()));
								fileBean.setThumbPath(AppUtil.getIPSplit(
										context, AppUtil.UPLOADPATH + "/"
												+ tvCodeBean.getFilePath()));
								fileBean.setExtType("img");

								fileBean.setName(tvCodeBean.getFileName());
								int fileId = Integer.parseInt(tvCodeBean
										.getId());
								fileBean.setId(fileId);
								fileBean.setFileId(fileId);
								int addIndex = 0;
								if (isStudent) {
									addIndex = answerList.size() - 1;
									if (addIndex < 0) {
										addIndex = 0;
									}
									answerList.add(addIndex, fileBean);
									adImageAdd.notifyDataSetChanged();
								} else {
									addIndex = markFiles.size() - 1;
									if (addIndex < 0) {
										addIndex = 0;
									}
									markFiles.add(addIndex, fileBean);
									adImageMark.notifyDataSetChanged();
								}
							}
						}
					});
		}

	}

}
