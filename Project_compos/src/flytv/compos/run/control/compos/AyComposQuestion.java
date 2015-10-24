package flytv.compos.run.control.compos;

import in.srain.cube.image.CubeImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import flytv.compos.run.R;
import flytv.compos.run.bean.ComposBean;
import flytv.compos.run.bean.FileBean;
import flytv.compos.run.bean.QuestionBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AlertHelp;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.FileUtil;
import flytv.ext.utils.GeneralView;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.SWDateUtil;
import flytv.ext.utils.UserShareUtils;
import flytv.ext.view.inter.PopWindowItemClickLister;
import flytv.run.bean.TVCodeBean;

public class AyComposQuestion extends BaseActivity {

	private boolean isInit = false, isEdit = false;
	private String subjectName;
	@ViewInject(R.id.ivTitleBtnLeft)
	private ImageButton ivTitleBtnLeft;

	@ViewInject(R.id.ivTitleBtnRight)
	private Button ivTitleBtnRight;

	@ViewInject(R.id.button_grade)
	private Button button_grade;

	@ViewInject(R.id.ivTitleName)
	private TextView ivTitleName;
	@ViewInject(R.id.tx_right)
	private TextView tx_right;

	@ViewInject(R.id.edit_content)
	private EditText ed_title;

	@ViewInject(R.id.ed_question_type)
	private Button ed_question_type;

	@ViewInject(R.id.btn_compos_add)
	private Button btn_compos_add;

	@ViewInject(R.id.btn_file_add)
	private Button btn_file_add;

	@ViewInject(R.id.layout_file)
	private LinearLayout layout_file;

	private ComposBean composItem;
	private TVCodeBean initComposEntity;
	private QuestionBean questionBean;
	private BitmapUtils bitmapUtils;

	private PopupWindow popupWindow;
	@OnClick({ R.id.ivTitleBtnLeft, R.id.btn_sercher, R.id.tx_right,
			R.id.btn_compos_add, R.id.btn_file_add, R.id.ed_question_type})
	public void onClickComment(View view) {
		switch (view.getId()) {
		// 添加评论
		case R.id.btn_sercher:
			break;
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		case R.id.tx_right:
			// 提交
			sendCompos();
			break;
		case R.id.btn_file_add:
			// 多附件
			AlertHelp.showDialogExt(AyComposQuestion.this);
			break;
		case R.id.ed_question_type:
			popupWindow = GeneralView.makePopupWindow(this, qustionIndex,
					questionArray, popWindowItemClickLister);
			int[] xy = new int[2];
			view.getLocationOnScreen(xy);
			popupWindow.showAtLocation(view, Gravity.CENTER
					| Gravity.TOP, xy[0] / 2, xy[1] + view.getHeight()
					+ 5);
			break;
		}
	}
	private PopWindowItemClickLister popWindowItemClickLister = new PopWindowItemClickLister() {

		@Override
		public void onItemClick(int i) {
			popupWindow.dismiss();
			if (qustionIndex != i) {
				qustionIndex = i;
				ed_question_type.setText(questionArray[i]);
			}
		}
	};
	private TVCodeBean loginBean;

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		ivTitleName
				.setText(getString(R.string.main_text_compos_lint_add_compos));

		if (isEdit) {
			ed_title.setText(questionBean.getQuestionTitle());
			// 题目类型
			for (int i = 0; i < questionArray.length; i++) {
				int typeId = questionBean.getQuestionType();
				String array = AppUtil.questionMap.get(typeId);
				if(array.equals(questionArray[i])){
					qustionIndex = i;
					break;
				}
			}
			ed_question_type.setText(questionArray[qustionIndex]);
			// 附件
			if (questionBean.files.size() > 0) {
				fileBean = new FileBean();
				fileBean.setImg_typeId(0);
				FileBean fileEdit = questionBean.files.get(0);
				fileBean.setFileUrl(AppUtil.getIPSplit(context,
						AppUtil.UPLOADPATH + "/" + fileEdit.getFileUrl()));
				if (fileEdit.getName().contains("jpg")
						|| fileEdit.getName().contains("png")) {
					fileBean.setThumbPath(AppUtil.getIPSplit(context,
							AppUtil.UPLOADPATH + "/" + fileEdit.getFileUrl()));
					fileBean.setExtType("img");
				} else if (fileEdit.getName().contains("mp4")) {
					fileBean.setThumbPath(AppUtil.getIPSplit(context,
							AppUtil.UPLOADPATH + "/" + fileEdit.getThumbPath()));
					fileBean.setExtType("video");
				} else if (fileEdit.getName().contains("mp3")) {
					fileBean.setExtType("audio");
				}
				fileBean.setName(fileEdit.getName());
				fileBean.setId(fileEdit.getId());
				// 更新视图 //
				initImage(false);
			}
		} else {
			ed_question_type.setText(questionArray[qustionIndex]);
		}
	}

	String[] questionArray;

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.ad_compos_question_add);
		ViewUtils.inject(this);
		button_grade.setVisibility(View.INVISIBLE);
		tx_right.setVisibility(View.VISIBLE);
		ivTitleBtnRight.setVisibility(View.GONE);
		loginBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				context);
		isInit = getIntent().getBooleanExtra("isInit", true);
		isEdit = getIntent().getBooleanExtra("isEdit", true);
		composItem = (ComposBean) getIntent()
				.getSerializableExtra("composItem");
		subjectName = getIntent().getStringExtra("subjectName");
		initComposEntity = (TVCodeBean) getIntent().getSerializableExtra(
				"initComposEntity");
		questionBean = (QuestionBean) getIntent().getSerializableExtra(
				"Questionentity");

		questionArray = AppUtil.getSubjectType(context, subjectName);
		bitmapUtils = new BitmapUtils(this);

	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	private int qustionIndex;
	private FileBean fileBean;

	void sendCompos() {
		//
		String msgConText = ed_title.getText().toString().trim();
		msgConText =msgConText.replaceAll(" ", "");
		if (AppUtil.isStrNull(msgConText)) {
			AlertTools.showTips(context, R.drawable.tips_warning, "请输入试题的题目！");
			return;
		}
		String qustionType = questionArray[qustionIndex];
		String qustionId= AppUtil.valueGetKey(qustionType);
		// 需要更具 qustionType 判断题目类型
		if (AppUtil.isStrNull(qustionType)) {
			AlertTools.showTips(context, R.drawable.tips_warning, "试题类型必须选择！");
		}
		int homeworkId = 0, questionId = 0;
		//
		if (isInit) {
			homeworkId = initComposEntity.getHomeworkId();
			if (isEdit) {
				questionId = questionBean.getId();
			} else {
				questionId = 0;
			}
		} else {
			homeworkId = composItem.getId();
			// 不是新建的情况
			if (isEdit) {
				questionId = questionBean.getId();
			} else {
				questionId = 0;
			}

		}
		String mothodUrl = getString(R.string.app_host)
				+ getString(R.string.compos_tab_answer_addwork_question)
				+ "?userId=" + loginBean.getUserId() + "&questionTitle="
				+ msgConText + "&questionId=" + questionId + "&questionType="
				+ qustionId + "&homeworkId=" + homeworkId;
		if (fileBean != null) {
			mothodUrl = mothodUrl + "&imagesIds=" + fileBean.getId();
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
								Intent intent = new Intent();
								intent.putExtra("initComposEntity", tvCodeBean);
								setResult(RESULT_OK, intent);
								finish();
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
			if (resultCode == Activity.RESULT_OK) {
				Uri fileUri = Uri.fromFile(AlertHelp.mediaFile);
				if (fileUri != null) {
					String filePath = fileUri.getPath();
					upload(filePath);
				}
			}
			break;
		case 500:
			if (resultCode == Activity.RESULT_OK) {
				String filePath = data.getStringExtra("FileImage");
				upload(filePath);
			}
			break;
		}
	}

	void upload(String filePath) {
		LogUtils.print(1, "filePath=" + filePath);
		TVCodeBean entity = (TVCodeBean) UserShareUtils.getInstance()
				.getLoginInfo(context);
		final String fileName = filePath
				.substring(filePath.lastIndexOf("/") + 1);
		if (AppUtil.isStrNull(filePath)) {
			AlertTools.showTips(context, R.drawable.tips_warning, "上传路径错误");
			return;
		}
		String fileUploadUrl = null;
		if (fileName.contains("jpg") || fileName.contains("png")
				|| fileName.contains("mp4") || fileName.contains("mp3")) {
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
								fileBean = new FileBean();
								fileBean.setImg_typeId(0);
								fileBean.setFileUrl(AppUtil.getIPSplit(
										context,
										AppUtil.UPLOADPATH + "/"
												+ tvCodeBean.getFilePath()));
								if (fileName.contains("jpg")
										|| fileName.contains("png")) {
									fileBean.setThumbPath(AppUtil.getIPSplit(
											context, AppUtil.UPLOADPATH + "/"
													+ tvCodeBean.getFilePath()));
									fileBean.setExtType("img");
								} else if (fileName.contains("mp4")) {
									fileBean.setThumbPath(AppUtil.getIPSplit(
											context, AppUtil.UPLOADPATH + "/"
													+ tvCodeBean.getThumbPath()));
									fileBean.setExtType("video");
								} else if (fileName.contains("mp3")) {
									fileBean.setExtType("audio");
								}
								fileBean.setName(tvCodeBean.getFileName());
								fileBean.setId(Integer.parseInt(tvCodeBean
										.getId()));
								fileBean.setFileId(Integer.parseInt(tvCodeBean
										.getId()));

								// 更新视图 //
								initImage(false);
							}
						}
					});
		}

	}

	void initImage(boolean isValue) {
		layout_file.removeAllViews();
		if (isValue) {
			btn_file_add.setVisibility(View.GONE);
		} else {
			btn_file_add.setVisibility(View.VISIBLE);
		}
		View view = View.inflate(context, R.layout.layout_add_image_item, null);
		CubeImageView cubeImageView = (CubeImageView) view
				.findViewById(R.id.im_backgourd);
		ImageView image_type = (ImageView) view.findViewById(R.id.img_type);
		if (fileBean.getExtType().equals("img")) {
			image_type.setVisibility(View.INVISIBLE);
		} else if (fileBean.getExtType().equals("mp4")) {
			image_type.setVisibility(View.VISIBLE);
		} else if (fileBean.getExtType().equals("mp3")||fileBean.getExtType().equals("audio")) {
			image_type.setVisibility(View.VISIBLE);
			// 可以换张图片 代表音频
		}
		final Bitmap bitmap = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.icon_user_image_nor);
		if (!AppUtil.isStrNull(fileBean.getThumbPath())) {
			final String imageUrl = fileBean.getThumbPath();
			LogUtils.print("" + imageUrl);
			cubeImageView.setTag(imageUrl);
			bitmapUtils.configDefaultLoadingImage(bitmap);
			bitmapUtils.display(cubeImageView, imageUrl,
					new BitmapLoadCallBack<View>() {

						@Override
						public void onLoadCompleted(View arg0, String arg1,
								Bitmap arg2, BitmapDisplayConfig arg3,
								BitmapLoadFrom arg4) {
							CubeImageView roundedImageView = (CubeImageView) arg0
									.findViewWithTag(arg1);
							if (roundedImageView != null) {
								roundedImageView.setImageBitmap(arg2);
							}
						}

						@Override
						public void onLoadFailed(View arg0, String arg1,
								Drawable arg2) {
							CubeImageView roundedImageView = (CubeImageView) arg0
									.findViewWithTag(arg1);
							if (roundedImageView != null) {
								roundedImageView.setImageBitmap(bitmap);
							}
						}
					});
		} else {
			cubeImageView.setImageBitmap(bitmap);
		}
		cubeImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertHelp.startResource(AyComposQuestion.this, AppUtil
						.getHttpImage(AppUtil.isHttpImage(fileBean.getFileUrl())));
			}
		});
		layout_file.addView(view);
	}

}
