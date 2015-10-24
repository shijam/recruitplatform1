package flytv.compos.run;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.ListIterator;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import flytv.compos.run.adapter.AdImageAdd;
import flytv.compos.run.bean.Circle;
import flytv.compos.run.bean.CommonMsg;
import flytv.compos.run.bean.FileBean;
import flytv.compos.run.bean.GradeBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AlertHelp;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.FileUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.SWDateUtil;
import flytv.ext.utils.UserShareUtils;
import flytv.run.bean.StuBean;
import flytv.run.bean.TVCodeBean;

public class AyFriendAdd extends BaseActivity {

	@ViewInject(R.id.rgp_type)
	private RadioGroup rgp_type;

	@ViewInject(R.id.ivTitleBtnLeft)
	private ImageButton ivTitleBtnLeft;

	@ViewInject(R.id.ivTitleBtnRight)
	private Button ivTitleBtnRight;
	@ViewInject(R.id.button_grade)
	private Button button_grade;

	@ViewInject(R.id.ivTitleName)
	private TextView ivTitleName;

	@ViewInject(R.id.layout_type)
	private LinearLayout layout_grade;

	@ViewInject(R.id.edit_content)
	private EditText edit_msg;

	@ViewInject(R.id.grid_image)
	private GridView grid_image;

	private TVCodeBean loginBean;

	private ArrayList<FileBean> fileItems = new ArrayList<FileBean>();
	private AdImageAdd admImageAdd;

	private StuBean stuBean;
	private ArrayList<RadioButton> gradeList = new ArrayList<RadioButton>();
	
	private ArrayList<GradeBean> gradeListItem = new ArrayList<GradeBean>();

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		button_grade.setVisibility(View.INVISIBLE);
		ivTitleBtnRight.setBackgroundResource(R.drawable.transparent);
		ivTitleBtnRight.setText("发布");
		ivTitleName.setText("发布说说");

		admImageAdd = new AdImageAdd(context, fileItems);
		FileBean fileBean = new FileBean();
		fileBean.setImg_typeId(1);
		fileItems.add(fileBean);
		grid_image.setAdapter(admImageAdd);
		initRadio(0);
	}

	void initRadio(int seleIndex) {
		layout_grade.removeAllViews();
		LinkedList<GradeBean> listFile = (LinkedList<GradeBean>)loginBean.gradeList;
		 ListIterator<GradeBean> itr = listFile.listIterator();
		 while (itr.hasNext()) {
			 gradeListItem.add(itr.next());
		 }
		int length = gradeListItem.size();
		int sumLint = length / 3 + length % 3;
		int index = 0;
		for (int i = 0; i < sumLint; i++) {
			LinearLayout layout = new LinearLayout(context);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			layout.setLayoutParams(params);
			int sumJ = 0;
			int numSplit = length % 3;
			if (numSplit == 0) {
				sumJ = 3;
			} else {
				if (i < sumLint - 1) {
					sumJ = 3;
				} else {
					sumJ = numSplit;
				}
			}
			for (int j = 0; j < sumJ; j++) {
				RadioButton radioButton = new RadioButton(context);
				GradeBean gradeBean = gradeListItem.get(index);
				radioButton.setButtonDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				radioButton.setText(gradeBean.getNJMC());
				Drawable drawable = getResources().getDrawable(
						R.drawable.btn_text_bg);
				// / 这一步必须要做,否则不会显示.
				drawable.setBounds(0, 0, drawable.getMinimumWidth(),
						drawable.getMinimumHeight());
				radioButton.setCompoundDrawables(drawable, null, null, null);
				LogUtils.print("gradeBean.getNJMC()" + gradeBean.getNJMC());
				radioButton
						.setTextColor(getResources().getColor(R.color.black));
				LinearLayout.LayoutParams params_note = new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				layout.addView(radioButton);
				layout.setLayoutParams(params_note);
				radioButton.setTag(index);
				gradeList.add(radioButton);
				radioButton
						.setOnCheckedChangeListener(new OnCheckedChangeListener() {

							@Override
							public void onCheckedChanged(
									CompoundButton buttonView, boolean isChecked) {
								// initRadio(seleIndex)
							}
						});
				if (index == seleIndex) {
					radioButton.setChecked(true);
				}
				index++;
			}
			layout_grade.addView(layout);
		}
		layout_grade.setVisibility(View.GONE);
	}

	@OnClick({ R.id.ivTitleBtnLeft, R.id.btn_sercher, R.id.ivTitleBtnRight })
	public void onClickComment(View view) {
		switch (view.getId()) {
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		case R.id.ivTitleBtnRight:
			// 发布
			sendCompos();
			break;
		}
	}

	private int gradeIndex = 0;
	private int sectionIndex = 0;
	private ArrayList<FileBean> fileList = new ArrayList<FileBean>();
	void sendCompos() {
		//
		for (int i = 0; i < gradeList.size(); i++) {

		}
		String msgConText = edit_msg.getText().toString().trim();
		String mothodUrl = getString(R.string.app_host)
				+ getString(R.string.compos_circle_saveLog) + "?userId="
				+ loginBean.getUserId() + "&subjectName=" + stuBean.getName()
				+ "&sectionName="
				+ loginBean.sectionList.get(sectionIndex).getXDMX()
				+ "&gradeName=" + loginBean.gradeList.get(gradeIndex).getNJMC()
				+ "&content=" + msgConText + "&circleType=1";
		StringBuffer fileStr = new StringBuffer("");
		if (isImage) {
			for (int i = 0; i < fileItems.size() - 1; i++) {
				FileBean files = fileItems.get(i);
				if (files.getImg_typeId() != 1) {
					if (i == 0) {
						fileStr.append(files.getId());
					} else {
						fileStr.append("," + files.getId());
					}
					fileList.add(files);
				}
			}
		} else {
			if (fileItems.get(0).getImg_typeId() == 0) {
				fileStr.append(fileItems.get(0).getId());
				fileList.add(fileItems.get(0));
			}

		}
		if (!AppUtil.isStrNull(fileStr.toString())) {
			mothodUrl += "&imagesIds=" + fileStr.toString();
		} else {
			if (AppUtil.isStrNull(msgConText)) {
				AlertTools.showTips(context, R.drawable.tips_warning,
						R.string.compos_flytv_send_text_null_lint);
				return;
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
						closeDataDialog();
						try {
							LogUtils.print("responseInfo ="
									+ responseInfo.result);
							closeDataDialog();
							if (AppUtil.isStrNull(responseInfo.result)) {
								AlertTools.showTips(context,
										R.drawable.tips_warning, "服务器连接失败!");
								return;
							}
							LogUtils.print("获取发布信息=" + responseInfo.result);
							CommonMsg tvCodeBean = (CommonMsg) AppUtil
									.getJSONBean(responseInfo.result,
											CommonMsg.class);
							if (tvCodeBean.getMessage() == 1) {
								// 添加成功后 返回当前实体
								Circle circleComment = (Circle) AppUtil
										.getJSONBean(tvCodeBean.getObj(),
												Circle.class);
								Intent intent = new Intent();
								circleComment.fileList = fileList;
								circleComment.setMyHeader(loginBean.getPhoto());
								intent.putExtra("addEntity", circleComment);
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
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.ad_friend_add);
		ViewUtils.inject(this);
		loginBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				context);
		stuBean = (StuBean) getIntent().getSerializableExtra("SubjectItem");
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	private boolean isImage = true;

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub
		grid_image.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (rgp_type.getCheckedRadioButtonId() == R.id.btn_send_image) {
					isImage = true;

					if (fileItems.size() - 1 == arg2) {
						AlertHelp.showDialog(AyFriendAdd.this);
					} else {
						// 查看图片
						AlertHelp.startResource(AyFriendAdd.this, AppUtil
								.getHttpImage(fileItems.get(arg2).getFileUrl()));
					}
				} else {
					isImage = false;
					if (fileItems.get(arg2).getImg_typeId() == 1) {
						// 添加视频
						AlertHelp.showVideoIntent(AyFriendAdd.this);
					} else {
						// 查看视频
						AlertHelp.startResource(context, AppUtil
								.getHttpImage(fileItems.get(arg2).getFileUrl()));
					}
				}

			}
		});
		rgp_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				fileItems.clear();
				FileBean fileBean = new FileBean();
				fileBean.setImg_typeId(1);
				fileItems.add(fileBean);
				admImageAdd.notifyDataSetChanged();
			}
		});

		grid_image.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				admImageAdd.setShow(true);
				admImageAdd.notifyDataSetChanged();
				return true;
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
								if (isImage) {
									fileBean.setThumbPath(AppUtil.getIPSplit(
											context, AppUtil.UPLOADPATH + "/"
													+ tvCodeBean.getFilePath()));
									fileBean.setExtType("img");
								} else {
									fileBean.setThumbPath(AppUtil.getIPSplit(
											context, AppUtil.UPLOADPATH + "/"
													+ tvCodeBean.getThumbPath()));
									fileBean.setExtType("video");
								}
								fileBean.setName(tvCodeBean.getFileName());
								fileBean.setId(Integer.parseInt(tvCodeBean
										.getId()));
								if (!isImage) {
									fileItems.clear();
								}
								int addIndex = 0;
								addIndex = fileItems.size() - 1;
								if (addIndex < 0) {
									addIndex = 0;
								}
								fileItems.add(addIndex, fileBean);
								admImageAdd.notifyDataSetChanged();
							}
						}
					});
		}

	}

}
