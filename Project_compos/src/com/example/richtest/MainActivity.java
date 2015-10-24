package com.example.richtest;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.example.richtest.bean.CommentInfo;
import com.example.richtest.bean.EditorFile;
import com.example.richtest.bean.ExtResult;
import com.example.richtest.bean.MarkEditor;
import com.example.richtest.bean.PicCompostion;
import com.example.richtest.bean.Result;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import flytv.compos.run.R;
import flytv.compos.run.bean.AnswerBean;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.UserShareUtils;
import flytv.run.bean.TVCodeBean;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private String fileService;
	public static MainActivity instance;
	public static boolean readOnly;
	RelativeLayout relative;
	Button submitBtn;
	RichEditText ret;
	private LinearLayout mEffectsItemLayout;
	private ArrayList<String> amrPaths = new ArrayList<String>();

	private ArrayList<Object> oldTextArray;
	public static ArrayList<PicCompostion> oldPicArrays;

	public static String userId = "91601429";
	//1403
	private String textMarkId = "1703";
	public static boolean isHD = false;
	private AnswerBean markNotation;
	private boolean isEdited = false;

	public static HashMap<Integer, ArrayList<Path>> pathss = new HashMap<Integer, ArrayList<Path>>();
	public static HashMap<Integer, HashMap<Integer, PointEntity>> pointss = new HashMap<Integer, HashMap<Integer, PointEntity>>();
	public static HashMap<Integer, String> imageNames = new HashMap<Integer, String>();
	public static HashMap<Integer, ArrayList<PointEntity>> pointEntitys = new HashMap<Integer, ArrayList<PointEntity>>();
	public static HashMap<Integer, ArrayList<PointEntity>> pointEntitys_new = new HashMap<Integer, ArrayList<PointEntity>>();
	public static ArrayList<PointEntity> nowOlePoints = new ArrayList<PointEntity>();
	com.nostra13.universalimageloader.core.ImageLoader imageLoader2;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			final ImageView imageview_map = (ImageView) mEffectsItemLayout
					.getChildAt(msg.what);
			if (imageview_map != null) {
				String pa = oldPicArrays.get(msg.what).picname;
				imageview_map.setImageBitmap(SimpleSampleActivity.getSDPic(pa));
			}
		};
	};
	private TVCodeBean tvCodeBean;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.com_activity_main);
		Display mDisplay = getWindowManager().getDefaultDisplay();
		markNotation =(AnswerBean) getIntent().getSerializableExtra("markNotation");
	
		tvCodeBean = (TVCodeBean)UserShareUtils.getInstance().getLoginInfo(MainActivity.this);
		userId = tvCodeBean.getUserId();
		textMarkId = markNotation.getId()+"";
		imageLoader2 = com.nostra13.universalimageloader.core.ImageLoader
				.getInstance();
		imageLoader2.init(ImageLoaderConfiguration.createDefault(this));
		int W = mDisplay.getWidth();
		if (W > 2000) {
			isHD = true;
		}
		instance = this;
		Intent intent = getIntent();
		if(tvCodeBean.getUserType().equals("2")){
			readOnly = true;
		}else{
			if(markNotation.getMarkStatus()==2){
				readOnly = true;
			}else{
				readOnly = false;
			}
		}
		//readOnly = !intent.getBooleanExtra("readOnly", false);
		LogUtils.print("是否可以编辑="+readOnly);
		oldTextArray = new ArrayList<Object>();
		oldPicArrays = new ArrayList<PicCompostion>();

		// imageview_map = new HashMap<Integer, ImageView>();
		// bitmapss = new HashMap<Integer, Bitmap>();
		pathss = new HashMap<Integer, ArrayList<Path>>();
		pointss = new HashMap<Integer, HashMap<Integer, PointEntity>>();

		submitBtn = (Button) findViewById(R.id.submitBtn);
		submitBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				MainActivity.this.textCompsSubmit();
			}
		});
		// 文本布局
		relative = (RelativeLayout) findViewById(R.id.relativeLyout);
		ret = (RichEditText) findViewById(R.id.RichEditText);
		ret.setLineSpacing(10, 1);
		ret.init(this, readOnly, oldTextArray);
		ret.setTextSize(18);
		ret.setCursorVisible(false);

		ret.setGravity(2);

		mEffectsItemLayout = (LinearLayout) this
				.findViewById(R.id.layout_video_effects_title);

		String sss = "";
		String ssss = StaticTools.iosTOand(sss);
		ret.setText(Html.fromHtml(ssss));// 添加本地文本

		if (android.os.Build.VERSION.SDK_INT <= 10) {
			ret.setInputType(InputType.TYPE_NULL);
		} else {
			this.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			try {
				Class<EditText> cls = EditText.class;
				Method setShowSoftInputOnFocus;
				setShowSoftInputOnFocus = cls.getMethod(
						"setShowSoftInputOnFocus", boolean.class);
				setShowSoftInputOnFocus.setAccessible(true);
				setShowSoftInputOnFocus.invoke(ret, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.getTextComposition(this);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("test_tag", "onRestart");
		mEffectsItemLayout.removeAllViews();
		MainActivity.this.refreshListView();
	}

	// 刷新横listview
	private void refreshListView() {
		if (oldPicArrays != null && oldPicArrays.size() > 0) {
			for (int i = 0; i < oldPicArrays.size(); i++) {
				final PicCompostion p = oldPicArrays.get(i);
				if (p != null) {
					final ImageView images = new ImageView(this);
					images.setTag(i);
					ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
							isHD ? 800 : 500, LayoutParams.WRAP_CONTENT);

					String pa = oldPicArrays.get(i).picname;
					images.setImageBitmap(SimpleSampleActivity.getSDPic(pa));
					images.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							int index = (Integer) v.getTag();
							boolean isContains = pointEntitys
									.containsKey(index);
							Log.i("test_tag",
									"pointEntitys.containsKey(index)="
											+ isContains);
							if (isContains) {// 二次编辑
								Log.i("test_tag", "isContains 二次编辑");
								Intent intent = new Intent(MainActivity.this,
										SimpleSampleActivity.class);
								intent.putExtra("DATA", index);

								ArrayList<PointEntity> oldPoints = pointEntitys
										.get(index);
								if (oldPoints != null) {
									nowOlePoints = null;
									nowOlePoints = oldPoints;
								}
								String imagename = imageNames.get(index);
								if (imagename != null) {
									intent.putExtra("imageName", imagename);
								}

								startActivityForResult(intent, 5000);
							} else {
								Log.i("test_tag", "isContains 一次编辑");
								if (p.oldPointEntitys != null
										&& p.oldPointEntitys.size() > 0) {
									Intent intent = new Intent(
											MainActivity.this,
											SimpleSampleActivity.class);

									intent.putExtra("DATA", index);
									startActivityForResult(intent, 5000);
								} else {
									Intent intent = new Intent(
											MainActivity.this,
											RotateActivity.class);

									intent.putExtra("DATA", index);
									startActivityForResult(intent, 5000);
								}
							}

						}
					});
					mEffectsItemLayout.addView(images, layoutParams);
				}
			}
		}
	}

	private int text_id;
	private int location;
	boolean isTextSave = false;
	// 文本提交
	protected void textCompsSubmit() {
		String textCompstion = ret.getRichtextString();
		try {
			if (ret.attArray != null) {
				if (ret.attArray.size() <= 0) {
					textFinish();
					return;
				}
			}
			RequestParams paramsUtil = new RequestParams();
			String url = getString(R.string.app_host)+getString(R.string.compos_save_mark);
			JSONObject jsonObject = new JSONObject();
			ViewText jsonEditor = new ViewText();
			jsonEditor.answerId = Integer.valueOf(textMarkId);
			jsonEditor.markType = 1;
			
			jsonEditor.markContent = textCompstion;
			String jsoneditor = jsonObject.toJSONString(jsonEditor);
			HttpUtils http = new HttpUtils();
			http.configTimeout(10000);
			paramsUtil.addBodyParameter("userId", userId);
			paramsUtil.addBodyParameter("id", text_id + "");
			paramsUtil.addBodyParameter("jsonEditor", jsoneditor);
			Log.i("test_tag","url="+url);
			Log.i("test_tag", "submit =  userId = " + userId + " |id = "
					+ text_id + "|jsonEditor" + jsoneditor);
			http.send(HttpRequest.HttpMethod.POST, url, paramsUtil,
					new RequestCallBack<String>() {
						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {

						}
						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							Result result = new Gson().fromJson(
									responseInfo.result, Result.class);
							Log.i("test_tag", "textCompsSubmit result="
									+ responseInfo.result);
							if (result.message == 1) {
								int editorId = result.editorId;
								ArrayList<Object> mOldAtt = new ArrayList<Object>();
								ArrayList<Object> oldAtt = ret.getoldAttArray();
								mOldAtt.addAll(oldAtt);
								mOldAtt.addAll(ret.attArray);
								isTextSave = true;
//								oldAtt.addAll(ret.attArray);
								
								// 提交成功后 开始 提交图片
								TextAttachmentUploadDouble textupload = new TextAttachmentUploadDouble(
										MainActivity.this, text_id+"",
										editorId, mOldAtt);
								textupload.start();
							}
						}

						@Override
						public void onStart() {

						}

						@Override
						public void onFailure(HttpException error, String msg) {

						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 图片提交
	protected void picCompsSubmit() {
		try {
			if(pointEntitys_new.size()<=0){
				if(isTextSave){
					Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
					finish();
				}else{
					Toast.makeText(this, "没有编辑的图片!", Toast.LENGTH_SHORT).show();
				}
				return;
			}
			ArrayList<PointEntity> addPoint =  pointEntitys_new.get(location);
			if(addPoint!=null){
				// 这儿需要判断 多张图片的情况
			}else{
				picFinish();
				return;
			}
			String url = getString(R.string.app_host)+getString(R.string.compos_save_mark);
			JSONObject jsonObject = new JSONObject();
			ViewText jsonEditor = new ViewText();
			jsonEditor.answerId = Integer.valueOf(textMarkId);
			jsonEditor.markType = 2;
			jsonEditor.markContent = "";
			String jsoneditor = jsonObject.toJSONString(jsonEditor);
			String s = MainActivity.ALBUM_PATH + "/" + imageNames.get(0);
			File mfile = new File(s);
			String pa;
			if (mfile.exists()) {// 若该文件存在
				pa = s;
			} else {
				String name = oldPicArrays.get(0).picname;
				pa = MainActivity.ALBUM_PATH + "/" + name;
			}
			File picfile = new File(pa);
			RequestParams paramsUtil = new RequestParams();
			paramsUtil.addBodyParameter("userId", userId);
			final MarkEditor markEntity = markEditor.get(location);
			paramsUtil.addBodyParameter("id", markEntity.id + "");
			paramsUtil.addBodyParameter("jsonEditor", jsoneditor);
			// 该图片 是我随便在手机照的一张图片
			// 请修改
			File file1 = new File(pa);
			paramsUtil.addBodyParameter(file1.getName(), file1);
			paramsUtil.addBodyParameter("fileName", picfile.getName());
			Log.i("test_tag", file1.getPath() + " | " + file1.exists() + " | "
					+ file1.getName() + "|url = " + url);
			HttpUtils http = new HttpUtils();
			http.send(HttpRequest.HttpMethod.POST, url, paramsUtil,
					new RequestCallBack<String>() {

						@Override
						public void onStart() {

						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
						}

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							LogUtils.print("test="+responseInfo.result);
							Result result = new Gson().fromJson(
									responseInfo.result, Result.class);
							if (result.message == 1) {

								int editorId = result.editorId;

								ArrayList<PointEntity> points = pointEntitys
										.get(0);
								if (points != null) {
									PicAttachmentUploadDouble picupload = new PicAttachmentUploadDouble(
											MainActivity.this, textMarkId,
											markEntity.id + "", editorId, points);
									picupload.start();
								}
							}

						}

						@Override
						public void onFailure(HttpException error, String msg) {
							LogUtils.print("test="+msg);
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		SimpleSampleActivity.tempBm = null;
		oldTextArray = null;
		oldPicArrays = null;
		pathss = null;
		pointss = null;
		pointEntitys.clear();
		pointEntitys_new.clear();
		ret = null;
		File file = new File(MainActivity.ALBUM_PATH);
		if (file.exists()) {
			delete(file);
			Log.i("test_tag", "delete()");
		}
	}
	public  void delete(File file) {
		if (file.isFile()) {
			file.delete();
			return;
		}

		if (file.isDirectory()) {
			File[] childFiles = file.listFiles();
			if (childFiles == null || childFiles.length == 0) {
				file.delete();
				return;
			}

			for (int i = 0; i < childFiles.length; i++) {
				delete(childFiles[i]);
			}
			file.delete();
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}


	private String amrPath = null;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 2000:
			if (resultCode == RESULT_OK) {
				amrPath = data.getStringExtra("FileImage");
				int start = data.getIntExtra("start", -1);
				int end = data.getIntExtra("end", -1);
				ret.saveRecord(start, end, amrPath);
				upLoad();
			}
			break;
		}
	}

	public void textFinish() {
		this.picCompsSubmit();
	}

	public void picFinish() {
		if(location+1<=markEditor.size()){
			location++;
			picCompsSubmit();
		}
		else{
			Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	private void upLoad() {
		amrPaths.add(amrPath);
	}

	// 获取文本作文
	public void getTextComposition(Context context) {
		
		//  
		
		// 192.168.1.59:8080/Composition
		String url = getString(R.string.app_host)+getString(R.string.compos_save_mark_show)+"?userId="
				+ userId + "&answerId=" + textMarkId;
		Log.i("test_tag", "url=" + url);
		HttpUtils http = new HttpUtils();
		http.configTimeout(20000);
		http.configCurrentHttpCacheExpiry(4);
		http.send(HttpRequest.HttpMethod.GET, url,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

					}
					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						Log.i("test_tag", "url=" + responseInfo.result);
						CommentInfo commentInfo = new Gson().fromJson(
								responseInfo.result, CommentInfo.class);
						fileService = commentInfo.fileService;
						String textContent = null;
						MarkEditor[] markEditors = commentInfo.markEditors;
						ArrayList<MarkEditor> imagePoint = new ArrayList<MarkEditor>();
						MarkEditor textPoint = new MarkEditor();
						if (markEditors.length > 0) {
							for (int i = 0; i < markEditors.length; i++) {
								MarkEditor markEditor = markEditors[i];
								if (markEditor != null) {
									int markType = Integer
											.parseInt(markEditor.markType);
									if (markType == 1) {
										textPoint = markEditor;
									} else if (markType == 2) {
										imagePoint.add(markEditor);
									}
								}
								// 设置作文 标注信息

							}
							if (textPoint.markContent != null) {
								textContent = textPoint.markContent;
							}
							initTextPoint(textPoint);
							initImagePoint(imagePoint);
							text_id = Integer.parseInt(textPoint.id);
							// wd
							String content = StaticTools.iosTOand(textContent);
							ret.setText(Html.fromHtml(content));
							ret.setoldAttArray(oldTextArray);
						}
					}

					@Override
					public void onStart() {

					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}
				});
	}

	private void initTextPoint(MarkEditor markEditor) {
		if (markEditor.extResults != null) {
			for (int j = 0; j < markEditor.extResults.length; j++) {
				PointEntity pointEntity = new PointEntity();

				ExtResult extResult = markEditor.extResults[j];
				if (extResult.pointId != null) {
					pointEntity.pointID = Integer.valueOf(extResult.pointId);
				}
				if (extResult.note != null && extResult.note.length() > 0) {// 文本批注
					pointEntity.textContent = extResult.note;
				}
				if (extResult.editorExtFile != null
						&& extResult.editorExtFile.fileUrl != null) {
					pointEntity.mp3Name = fileService
							+ extResult.editorExtFile.fileUrl;
				}
				if (extResult.contentRangeStart != null) {
					int star = Integer.valueOf(extResult.contentRangeStart);
					int end = Integer.valueOf(extResult.contentRangeEnd);
					Range range = new Range();
					range.location = star;
					range.length = end - star;
					pointEntity.commentRange = range;
				}
				if (extResult.pointX != null) {
					Point point = new Point();
					try {
						point.x = (int)Double.parseDouble(extResult.pointX);
						point.y = (int)Double.parseDouble(extResult.pointY);
						pointEntity.point = point;
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				}
				// oldTextArray 代表 文字批阅
				oldTextArray.add(pointEntity);
			}
		}
	}
	private ArrayList<MarkEditor> markEditor = new ArrayList<MarkEditor>();
	private void initImagePoint(ArrayList<MarkEditor> markEditors) {
		markEditor = markEditors;
		Log.i("test_tag", "imagePoint=" + markEditors.size());
		if (markEditors.size() > 0) {
			for (int i = 0; i < markEditors.size(); i++) {
				PicCompostion piccompostion = new PicCompostion();
				ArrayList<PointEntity> oldPicArray = new ArrayList<PointEntity>();
				MarkEditor markEditor = markEditors.get(i);
				if (markEditor != null) {
					if (markEditor.editorFile != null) {
						EditorFile editorfile = markEditor.editorFile;
						piccompostion.path = editorfile.fileUrl;
						piccompostion.picname = editorfile.name;
						getpic(fileService + editorfile.fileUrl,
								editorfile.name, i);
					}
					if (markEditor.extResults != null) {
						for (int j = 0; j < markEditor.extResults.length; j++) {
							PointEntity pointEntity = new PointEntity();

							ExtResult extResult = markEditor.extResults[j];
							if (extResult.pointId != null) {
								pointEntity.pointID = Integer
										.valueOf(extResult.pointId);
							}
							if (extResult.note != null
									&& extResult.note.length() > 0) {// 文本批注
								pointEntity.textContent = extResult.note;
							}
							if (extResult.editorExtFile != null
									&& extResult.editorExtFile.fileUrl != null) {
								pointEntity.mp3Name = fileService
										+ extResult.editorExtFile.fileUrl;
							}

							if (extResult.pointX != null) {
								Point point = new Point();
								point.x = (int)Double.parseDouble(extResult.pointX);
								point.y = (int)Double.parseDouble(extResult.pointY);
								pointEntity.point = point;
							}
							oldPicArray.add(pointEntity);
						}
						piccompostion.oldPointEntitys = oldPicArray;
					}
				}
				// 获取 图片批阅
				oldPicArrays.add(piccompostion);
			}
		}
		MainActivity.this.refreshListView();
	}

	public void uploadtextComs(String editorId) {

	}

	class ViewText implements Serializable {

		private static final long serialVersionUID = 1L;
		public int imageHeight = 700;
		public int imageWidth = 1024;
		public String markContent = null;
		public int answerId = 0;
		public int markType = 0;

		// public int newAttachId= 0;
		// public int oldAttachId = 0;
	}

	public final static String ALBUM_PATH = Environment
			.getExternalStorageDirectory() + "/image_point";

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return byte[]
	 * @throws Exception
	 */
	public byte[] getImage(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(5 * 1000);
		conn.setRequestMethod("GET");
		InputStream inStream = conn.getInputStream();
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return readStream(inStream);
		}
		return null;
	}

	/**
	 * Get image from newwork
	 * 
	 * @param path
	 *            The path of image
	 * @return InputStream
	 * @throws Exception
	 */
	public InputStream getImageStream(String path) throws Exception {
		URL url = new URL(path);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(15 * 1000);
		conn.setRequestMethod("GET");
		if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
			return conn.getInputStream();
		}
		return null;
	}

	/**
	 * Get data from stream
	 * 
	 * @param inStream
	 * @return byte[]
	 * @throws Exception
	 */
	public static byte[] readStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		outStream.close();
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws IOException
	 */
	public static void saveFile(Bitmap bm, String fileName) throws IOException {
		File dirFile = new File(ALBUM_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}

		// File myCaptureFile = new File(ALBUM_PATH + "/" + fileName + ".jpg");
		File myCaptureFile = new File(ALBUM_PATH + "/" + fileName);
		if (myCaptureFile.exists()) {
			myCaptureFile.delete();
		}
		BufferedOutputStream bos = new BufferedOutputStream(
				new FileOutputStream(myCaptureFile));
		bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
		bos.flush();
		bos.close();
	}

	// 开线程获取图片 通过handler 传递
	private void getpic(final String filePath, final String fileName,
			final int index) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Bitmap mBitmap = null;

					byte[] data = getImage(filePath);
					if (data != null) {
						mBitmap = BitmapFactory.decodeByteArray(data, 0,
								data.length);// bitmap
					} else {
					}
					saveFile(mBitmap, fileName);
					handler.sendEmptyMessage(index);
					Log.d(TAG, "set image ...");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}).start();
	}
}
