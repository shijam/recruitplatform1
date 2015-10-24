package flytv.compos.run.control.compos.stu;

import in.srain.cube.image.CubeImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import flytv.compos.run.adapter.stu.AdComposAnswer;
import flytv.compos.run.bean.AnswerBean;
import flytv.compos.run.bean.CommonMsg;
import flytv.compos.run.bean.ComposBean;
import flytv.compos.run.bean.ComposMarkBean;
import flytv.compos.run.bean.FileBean;
import flytv.compos.run.bean.QuestionBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AlertHelp;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.FileUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.SWDateUtil;
import flytv.ext.utils.UserShareUtils;
import flytv.ext.view.inter.ItemCommentClickLister;
import flytv.run.bean.TVCodeBean;

/**
 * 学生回答以及查看作业
 * 
 * @author nike
 * 
 */
public class AyComposStuAnswer extends BaseActivity implements
		ItemCommentClickLister {

	private boolean isEdit = false;
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

	@ViewInject(R.id.title_add)
	private LinearLayout layout_add;


	private ListView listView_pop;

	@ViewInject(R.id.btn_marl_save)
	private Button btn_marl_save;
	private ComposBean composItem;
	private TVCodeBean tvCodeBean;

	private BitmapUtils bitmapUtils;

	@OnClick({ R.id.ivTitleBtnLeft, R.id.btn_sercher, R.id.tx_right,
			R.id.btn_compos_add, R.id.ed_class, R.id.ed_subject,
			R.id.btn_marl_save })
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
		case R.id.btn_marl_save:
			initComposFile();
			addIndex = 0;
			sendComposShow();
			break;
		}
	}

	private TVCodeBean loginBean;

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		bitmapUtils = new BitmapUtils(context);

		listView_pop = (ListView)findViewById(R.id.listView_pop);
		listView_pop.setDivider(null);
		listView_pop.setDividerHeight(10);
		adComposQuestion = new AdComposAnswer(context,
				items, bitmapUtils, isEdit);
		adComposQuestion
				.setItemCommentClickLister(AyComposStuAnswer.this);
		listView_pop.setAdapter(adComposQuestion);

	}

	ArrayList<QuestionBean> items = new ArrayList<QuestionBean>();
	private AdComposAnswer adComposQuestion;

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.ad_compos_add);
		ViewUtils.inject(this);
		ivTitleName.setText(getResources().getString(R.string.app_tab_compos));
		button_grade.setVisibility(View.INVISIBLE);
		tx_right.setVisibility(View.VISIBLE);
		ivTitleBtnRight.setVisibility(View.GONE);
		layout_add.setVisibility(View.GONE);
		loginBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				context);
		composItem = (ComposBean) getIntent().getSerializableExtra(
				"composStudent");
		if (composItem.getMarkStatus() == 0) {
			isEdit = true;
			btn_marl_save.setVisibility(View.VISIBLE);
		} else {
			isEdit = false;
			btn_marl_save.setVisibility(View.INVISIBLE);
			tx_right.setVisibility(View.INVISIBLE);
			// show()
		}
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub
		showQuestion();

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	void showQuestion() {
		showDataDialog();
		int homeworkId = 0;
		// 是否是编辑

		homeworkId = composItem.getId();
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(R.string.compos_tab_all_answer_loadQuestion)
				+ "?homeworkId=" + homeworkId + "&userId="
				+ loginBean.getUserId();
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
								AppUtil.UPLOADPATH = msgBean.getFileService();
								items.clear();
								items.addAll(msgBean.items);
								adComposQuestion.notifyDataSetChanged();
								// admin
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

	/**
	 * 老师批阅作业
	 */
	HashMap<Integer, ComposMarkBean> mapFile = new HashMap<Integer, ComposMarkBean>();
	HashMap<Integer, FileBean> fileLists = new HashMap<Integer, FileBean>();
	private int index =-1;
	void initComposFile() {
		mapFile.clear();
		fileUps.clear();
		
		int itemCount = listView_pop.getCount();
		HashMap<Integer,String> map= adComposQuestion.getMapMsg();
		for (int i = 0; i < itemCount; i++) {
//			View getView = listView_pop.getChildAt(i);
//			EditText edit_msg = (EditText) getView
//					.findViewById(R.id.edit_comment);
//			LinearLayout layout_file = (LinearLayout) getView
//					.findViewById(R.id.layout_file);
//			int fileCount = layout_file.getChildCount();
//			String msgText = AppUtil.getEditValue(edit_msg);
			QuestionBean entity = items.get(i);
			ArrayList<AnswerBean> answerList = entity.answerList;
			//
			String msgText  = map.get(i);
			if (!AppUtil.isStrNull(msgText) || answerList.size() > 0) {
				//
				QuestionBean questionBean = items.get(i);
				ComposMarkBean composMarkBean = new ComposMarkBean();
				composMarkBean.setQuestionId(questionBean.getId());
				if (answerList.size() > 0) {
					AnswerBean answerBean = answerList.get(0);
					if(!AppUtil.isStrNull(msgText)){
						composMarkBean.setMsg(msgText);
						if (!answerBean.getUserAnswer().equals(msgText)) {
							// test
							mapFile.put(answerBean.getId(), composMarkBean);
							LogUtils.print("item=" + answerBean.getId());
						}
					}
					if(answerBean.answerFiles.size()>0){
						FileBean fileBean = answerBean.answerFiles.get(0);
						if (fileBean != null) {
							composMarkBean.setFileBean(fileBean);
							composMarkBean.setQuestionId(questionBean.getId());
							mapFile.put(answerBean.getId(), composMarkBean);
							LogUtils.print("item=" + answerBean.getId());
							fileUps.add(fileBean);
						}
					}
				} else {
					composMarkBean.setMsg(msgText);
					mapFile.put(index, composMarkBean);
					index--;
					
				}
			}else{
				AlertTools.showTips(context, R.drawable.tips_warning, "还有试题未完成!");
				break;
			}
		}
	}

	ArrayList<Integer> strId = new ArrayList<Integer>();
	ArrayList<FileBean> fileUps = new ArrayList<FileBean>();
	private int addIndex = 0;
	private int itemIndex = 0;
	void sendComposShow() {
		// 学生ID
		// 明天需要测试 debug
		HashMap<Integer, String> mapMsg = adComposQuestion.getMapMsg();
		if (mapFile.size() > 0 || mapMsg.size() > 0) {
			Iterator iter = mapFile.entrySet().iterator();
			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				// Object val = entry.getValue();//文件
				strId.add(Integer.parseInt(key.toString()));
			}
			// 判断是否存在 
			int composId = strId.get(addIndex);
			final String mesText = mapFile.get(composId).getMsg();
			String imagesIds = "";
			int questionId =0;
			if (mapFile.get(composId) != null) {
				questionId =mapFile.get(composId).getQuestionId();
				FileBean fileBean = mapFile.get(composId).getFileBean();
				if (fileBean != null) {
					imagesIds = fileBean.getFileId() + "";
				}
			} else {
				imagesIds = "";
			}
			if(composId<0){
				composId = 0;
			}
			String mothodUrl = getString(R.string.app_host)
					+ getString(R.string.compos_tab_answer_student_compos)
					+ "?userId=" + loginBean.getUserId() + "&answerId="
					+ composId + "&questionId="+questionId+"&imagesIds=" + imagesIds + "&userAnswer="
					+ mesText+"&homeworkId="+composItem.getId();
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
									AlertTools
											.showTips(context,
													R.drawable.tips_warning,
													"服务器连接失败!");
									return;
								}
								tvCodeBean = AppUtil.getPerson(
										responseInfo.result, TVCodeBean.class);
								if (tvCodeBean.getMessage() == 1) {
									// 添加成功后 返回当前实体
									if (addIndex <= mapFile.size() - 1) {
										ArrayList<AnswerBean> answerList = items.get(addIndex).answerList;
										if(answerList.size()==0){
											AnswerBean answerBean = new AnswerBean();
											answerBean.setUserAnswer(mesText);
											answerList.add(answerBean);
										}
										items.get(addIndex).answerList.get(answerList.size()-1).setId(tvCodeBean.getAnswerId());
										addIndex++;
										sendComposShow();
									} else {
										addIndex = 0;
										// 可以刷新数据
										AlertTools.showTips(context, R.drawable.tips_warning, "预存成功!");
									}
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

	}

	void sendCompos() {
		// 学生ID
		String mothodUrl = getString(R.string.app_host)
				+ getString(R.string.compos_tab_answer_stu_finishMark)
				+ "?userId=" + loginBean.getUserId() + "&id="
				+ composItem.getId();
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
							tvCodeBean = AppUtil.getPerson(responseInfo.result,
									TVCodeBean.class);
							if (tvCodeBean.getMessage() == 1) {
								// 添加成功后 返回当前实体
								Intent intent = new Intent();
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
	public void onBottomComment(int position, int commendId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBottomPraise(int position) {
		// TODO Auto-generated method stub

	}

	private int indexImg = 0;

	@Override
	public void onBottomDeleLog(int position) {
		// TODO Auto-generated method stub
		// 添加图片
		indexImg = position;
		QuestionBean entity = items.get(position);
		//
		AlertHelp.showDialogExt(AyComposStuAnswer.this);

	}

	@Override
	public void onBottomEditLog(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBottomEdit(String message, String logId, int index,
			int commendId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtils.print(1, "onActivityResult()" + this.getClass().getName());
		switch (requestCode) {
		case 100:
			if (resultCode == RESULT_OK) {
				showQuestion();
			}
			break;
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
								FileBean fileBean = new FileBean();
								fileBean.setImg_typeId(0);
								fileBean.setFileUrl(AppUtil.getIPSplit(
										context,
										AppUtil.UPLOADPATH + "/"
												+ tvCodeBean.getFilePath()));
								if (fileName.contains("jpg")
										|| fileName.contains("png")|| fileName.contains("img")) {
									fileBean.setThumbPath(AppUtil.getIPSplit(
											context, AppUtil.UPLOADPATH + "/"
													+ tvCodeBean.getFilePath()));
									fileBean.setExtType("png");
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
								// 没有批阅 没有 回答数据
								if (items.get(indexImg).answerList.size() <= 0) {
									AnswerBean answerBean = new AnswerBean();
									answerBean.setMarkContent("");
									items.get(indexImg).answerList
											.add(answerBean);
								}
								ArrayList<AnswerBean> answerLists = items.get(indexImg).answerList;
								items.get(indexImg).answerList.get(answerLists.size()-1).answerFiles.clear();
								items.get(indexImg).answerList.get(answerLists.size()-1).answerFiles
										.add(fileBean);
								// 更新视图 //
								LogUtils.print("indexImg="+indexImg);
								adComposQuestion.notifyDataSetChanged();
								/*
								View view = listView_pop.getChildAt(indexImg);
								if(view!=null){
									LinearLayout layout_file = (LinearLayout) view
											.findViewById(R.id.layout_file);
									Button button_add = (Button) view
											.findViewById(R.id.btn_file_add);
									initImage(layout_file, button_add, fileBean,
											true);
									fileLists.put(indexImg, fileBean);
								}else{
									LogUtils.print("indexImg="+indexImg+"=new=show view==null");
									try {
										throw new Throwable();
									} catch (Throwable e) {
										e.printStackTrace();
									}
								}*/
							

							}
						}
					});
		}

	}

	void initImage(LinearLayout layout_file, Button btn_file_add,
			FileBean fileBean, boolean isValue) {
		if (isValue) {
			btn_file_add.setVisibility(View.GONE);
			layout_file.removeAllViews();
		} else {
			btn_file_add.setVisibility(View.VISIBLE);
		}
		View view = View.inflate(context, R.layout.layout_add_image_item, null);
		CubeImageView cubeImageView = (CubeImageView) view
				.findViewById(R.id.im_backgourd);
		ImageView image_type = (ImageView) view.findViewById(R.id.img_type);
		String httpImageUrl = null;
		if (fileBean.getExtType().equals("png")
				|| fileBean.getExtType().equals("jpg")
				|| fileBean.getExtType().equals("img")) {
			httpImageUrl = fileBean.getFileUrl();
			image_type.setVisibility(View.INVISIBLE);
		} else if (fileBean.getExtType().equals("mp4")) {
			httpImageUrl = fileBean.getThumbPath();
			image_type.setVisibility(View.VISIBLE);
		} else if (fileBean.getExtType().equals("mp3")) {
			httpImageUrl = fileBean.getFileUrl();
			image_type.setVisibility(View.VISIBLE);
			// 可以换张图片 代表音频
		}
		final Bitmap bitmap = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.icon_user_image_nor);
		if (!AppUtil.isStrNull(httpImageUrl)) {
			if (!httpImageUrl.contains("http://")) {
				// Test
				httpImageUrl = AppUtil.UPLOADPATH + httpImageUrl;
			}
			final String imageUrl = httpImageUrl;
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
		layout_file.addView(view);
	}

}
