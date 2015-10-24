package flytv.ext.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import flytv.compos.run.R;
import flytv.ext.tools.AlertTools;
import flytv.run.bean.TVCodeBean;
import flytv.run.bean.UserBean;

public class AppUtil {

	public static int checkIndex = 0;
	public static int checkFlag = 0;
	public static int tearFlag = 0;
	public static int loginType = 0;
	public final static String test_imageTAG = "test_image";
	public final static String test_TAG = "test_tag";
//	public static String UPLOADPATH = "http://edudemo.flytv.com.cn/love_elearning_files/upload1";
	public static String UPLOADPATH = "http://182.92.118.91:8088/love_elearning_files/upload1";
	public static String timer = null;
	private static Context myContext;
	public static HashMap<Integer, String> questionMap = new HashMap<Integer, String>();
	public static String[] arrayType = new String[] {};
	static {
		questionMap.put(3, "填空题");
		questionMap.put(5, "简答题");
		questionMap.put(6, "选择题");
		questionMap.put(7, "计算题");
		questionMap.put(8, "实验题");
		questionMap.put(9, "应用题");
		questionMap.put(10, "写作题");
		questionMap.put(11, "PDF试题");
	}

	public static String getType(Context context, int indexType) {
		arrayType = new String[] {
				context.getString(R.string.main_text_compos_mark_type1),
				context.getString(R.string.main_text_compos_mark_type2),
				context.getString(R.string.main_text_compos_mark_type3) };
		if (arrayType.length > 0) {
			return arrayType[indexType];
		}
		return null;
	}
	public static String getComposType( int indexType) {
		String [] arrayType = new String[] {
				"一、",	"二、",	"三、",	"四、",	"五、",	"六、","七、" };
		if (arrayType.length > 0) {
			return arrayType[indexType];
		}
		return null;
	}

	public static String valueGetKey(String value) {
		Set set = questionMap.entrySet();
		Iterator it = set.iterator();
		String key = "";
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			if (entry.getValue().equals(value)) {
				key = entry.getKey().toString();
				break;
			}
		}
		return key;
	}

	public static String getIPSplit(Context context, String httpUrl) {

		return httpUrl.replace("http://edudemo.flytv.com.cn",
				context.getString(R.string.app_image));
	}

	public static <T> T getPerson(String jsonString, Class<T> cls) {
		T t = null;
		try {
			Gson gson = new Gson();
			t = gson.fromJson(jsonString, cls);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	public static <T, cls> T getPersonEntity(String jsonString, Class<T> cls) {
		T t = null;
		try {
			Gson gson = new Gson();
			java.lang.reflect.Type type = new TypeToken<cls>() {
			}.getType();
			t = gson.fromJson(jsonString, type);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return t;
	}

	// public static <T> List<T> getPersons(String jsonString, Class<T> clsss) {
	// List<T> list = new ArrayList<T>();
	// try {
	// Gson gson = new Gson();
	// list = gson.fromJson(jsonString, new TypeToken<List<clsss>>() {
	// }.getType());
	// } catch (Exception e) {
	// }
	// return list;
	// }

	public static List<Map<String, Object>> listKeyMaps(String jsonString) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			Gson gson = new Gson();
			list = gson.fromJson(jsonString,
					new TypeToken<List<Map<String, Object>>>() {
					}.getType());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return list;
	}

	/**
	 * 保存 程序的 context对象
	 * 
	 * @param context
	 */
	public static void setContext(Context context) {
		synchronized (AppUtil.class) {
			if (context != null) {
				myContext = context;
			}
		}
	}

	public static String getStringId(Context context) {
		return context.getResources().getString(R.string.app_host);
		/*
		 * if(NetUtil.isExt){ return
		 * context.getResources().getString(R.string.app_stb_ext_host); }else{
		 * return context.getResources().getString(R.string.app_stb_host); }
		 */
	}

	public static String getSplitServerIP(Context context, String httpUrl) {
		return httpUrl;
		/*
		 * String appId =
		 * context.getResources().getString(R.string.app_stb_resource); String
		 * appExtId =
		 * context.getResources().getString(R.string.app_stb_ext_resource);
		 * if(NetUtil.isExt){ return httpUrl.replace(appId, appExtId); }else{
		 * return httpUrl.replace(appExtId, appId); }
		 */
	}

	// 是否
	@SuppressLint("SimpleDateFormat")
	public static boolean isYearDayBig(String dayTimer, String netTimer) {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		Date date2 = null;
		try {
			date1 = formatter1.parse(dayTimer);
			date2 = formatter1.parse(netTimer);
			if (date1.getTime() <= date2.getTime()) {
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static String getHttpImage(String fileUrl) {
		if(!fileUrl.contains("http://")){
			return AppUtil.UPLOADPATH + "/" + fileUrl;
		}
		return fileUrl;
	}

	public static boolean isNetWork(Context context) {
		if (!NetUtil.hasNetwork(context)) {
			AlertTools.showTips(context, R.drawable.tips_success,
					R.string.net_http_not);
			return false;
		}
		return true;
	}

	@SuppressLint("SimpleDateFormat")
	public static String getYear() {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		return formatter1.format(date);
	}

	public static String intToTime(int num) {
		int mm = num / 60;
		int ss = num % 60;
		return getString(mm) + ":" + getString(ss);
	}

	public static String getString(int num) {
		if ((num + "").length() == 1) {
			return 0 + "" + num;
		} else {
			return num + "";
		}
	}

	/**
	 * 取的程序全局 context对象
	 * 
	 * @return
	 */
	public static Context getContext() {
		if (myContext != null) {
			return myContext;
		}
		return null;
	}
	
	public static String stringFilter(String str) {
		// 只允许字母和数字
		// String regEx = "[^a-zA-Z0-9]";
		// 清除掉所有特殊字符
		try {
			String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……& amp;*（）——+|{}【】‘；：”“’。，、？]";
			Pattern p = Pattern.compile(regEx);
			Matcher m = p.matcher(str);
			return m.replaceAll("").trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}

	public static void getInfo(Context context) {
		TelephonyManager tm = (TelephonyManager) context
				.getSystemService(context.TELEPHONY_SERVICE);
		String Android_ID = android.provider.Settings.System.getString(
				context.getContentResolver(),
				android.provider.Settings.System.ANDROID_ID);
		UserBean.DEVICEID = Android_ID;
		UserBean.MODEL = android.os.Build.MODEL;
		UserBean.VERSION_RELEASE = android.os.Build.VERSION.RELEASE;
		String sdk = android.os.Build.VERSION.SDK;
		UserBean.VERSION_SDK = sdk;
		UserBean.CPU_ABI = android.os.Build.CPU_ABI;
		UserBean.MANUFACTURER = android.os.Build.MANUFACTURER;

	}

	public static boolean isDrc(String fileName) {
		if (AppUtil.isStrNull(fileName)) {
			return false;
		}
		File file = new File(fileName);
		LogUtils.print("fileName" + fileName + "|file size =" + file.length());
		if (file.exists() && (file.length() < 150 || fileName.contains("mp3"))) {
			LogUtils.print("字幕 是 null");
			file.delete();
			return false;
		} else {
			LogUtils.print("字幕 不是 null");
			return true;
		}
	}

	/**
	 * 读取 Layout
	 * 
	 * @param layoutId
	 * @return
	 */
	public static View getLayout(int layoutId) {
		View view = LayoutInflater.from(AppUtil.getContext()).inflate(layoutId,
				null);
		return view;

	}

	public static String getEditValue(EditText editText) {
		if (editText == null) {
			return null;
		}
		return editText.getText().toString().trim();
	}

	public static TextView getTextView(View view, int resourcdId) {

		TextView textView = (TextView) view.findViewById(resourcdId);
		return textView;
	}

	public static EditText getEditView(View view, int resourcdId) {
		EditText editText = (EditText) view.findViewById(resourcdId);
		return editText;
	}

	public static Object getJSONBean(String jsonString, Class<?> className) {
		Object itemList;
		try {
			itemList = JSON.parseObject(jsonString, className);
			return itemList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取机顶盒 绑定状态
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTVBindStatus(Context context) {
		TVCodeBean tvCodeBean = (TVCodeBean) UserShareUtils.getInstance()
				.getTVInfo(context);
		if (tvCodeBean.getStbStatus() == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 序列化对象
	 * 
	 * @param person
	 * @return
	 * @throws IOException
	 */
	public static String serialize(Object person) throws IOException {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ObjectOutputStream objectOutputStream = new ObjectOutputStream(
				byteArrayOutputStream);
		objectOutputStream.writeObject(person);
		String serStr = byteArrayOutputStream.toString("ISO-8859-1");
		serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
		objectOutputStream.close();
		byteArrayOutputStream.close();
		return serStr;
	}

	/**
	 * 反序列化对象
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object deSerialization(String str) throws IOException,
			ClassNotFoundException {
		String redStr = java.net.URLDecoder.decode(str, "UTF-8");
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
				redStr.getBytes("ISO-8859-1"));
		ObjectInputStream objectInputStream = new ObjectInputStream(
				byteArrayInputStream);
		Object person = (Object) objectInputStream.readObject();
		objectInputStream.close();
		byteArrayInputStream.close();
		return person;
	}

	public static String getSplitUser(String msg, String userId) {
		return msg.replace("{userId}", userId);
	}

	public static void alert(Context context, String message) {
		Dialog dialog = new AlertDialog.Builder(context).setTitle("提示")
				.setMessage("您选择的不是有效的图片")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

					}
				}).create();
		dialog.show();
	}

	public static Bitmap scaleImg(Bitmap bm, int newWidth, int newHeight) {
		// 图片源
		// Bitmap bm = BitmapFactory.decodeStream(getResources()
		// .openRawResource(id));
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 设置想要的大小
		int newWidth1 = newWidth;
		int newHeight1 = newHeight;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth1) / width;
		float scaleHeight = ((float) newHeight1) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;

	}

	public static String[] getSubjectType(Context context, String subjectName) {
		if (AppUtil.isStrNull(subjectName)) {
			return null;
		}
		Resources resources = context.getResources();
		String[] array = new String[] {};
		if (subjectName.equals("语文")) {
			array = new String[] { resources
					.getString(R.string.main_text_compos_question_writer) };
		} else if (subjectName.equals("数学")) {
			array = new String[] {
					resources
							.getString(R.string.main_text_compos_question_select),
					resources
							.getString(R.string.main_text_compos_question_fill_blanks),
					resources
							.getString(R.string.main_text_compos_question_calcultion) };
		} else if (subjectName.equals("英语")) {
			array = new String[] { resources
					.getString(R.string.main_text_compos_question_writer) };
		} else if (subjectName.equals("物理")) {
			array = new String[] {
					resources
							.getString(R.string.main_text_compos_question_select),
					resources
							.getString(R.string.main_text_compos_question_fill_blanks),
					resources
							.getString(R.string.main_text_compos_question_exper),
					resources
							.getString(R.string.main_text_compos_question_application) };
		} else if (subjectName.equals("化学")) {
			array = new String[] {
					resources
							.getString(R.string.main_text_compos_question_select),
					resources
							.getString(R.string.main_text_compos_question_fill_blanks),
					resources
							.getString(R.string.main_text_compos_question_exper),
					resources
							.getString(R.string.main_text_compos_question_calcultion) };
		}
		return array;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		Log.i("appTAG", "params.height=" + params.height);
		listView.setLayoutParams(params);
	}

	public static boolean isStrNull(String message) {
		if (message == null || message.equals("")) {
			return true;
		}
		return false;
	}

	/**
	 * format html
	 * 
	 * @param html
	 * @return
	 */
	public static String isStrHtml(String html) {
		String sourceHtml = null;
		if (!AppUtil.isStrNull(html)) {
			sourceHtml = Html.fromHtml(html).toString();
			return sourceHtml;
		}
		return html;

	}

	public static String formetFileSize(long fileS) {// 转换文件大小
		DecimalFormat df = new DecimalFormat("#.00");
		String fileSizeString = "";
		if (fileS < 1024) {
			fileSizeString = df.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = df.format((double) fileS / 1024) + "K";
		} else if (fileS < 1073741824) {
			fileSizeString = df.format((double) fileS / 1048576) + "M";
		} else {
			fileSizeString = df.format((double) fileS / 1073741824) + "G";
		}
		return fileSizeString;
	}

	public static String getPoetryTime(String date) {
		if (AppUtil.isStrNull(date)) {
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			date = format1.format(format.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}

	public static String strYear(int time) {
		String date_time = time + "";
		if (date_time.length() == 1) {
			date_time = 0 + date_time;
		}
		return date_time;
	}

	public static String stripHtml(String content) {
		// <p>段落替换为换行
		content = content.replaceAll("<p .*?>", "\r\n");
		// <br><br/>替换为换行
		content = content.replaceAll("<br\\s*/?>", "\r\n");
		// 去掉其它的<>之间的东西
		content = content.replaceAll("\\<.*?>", "");
		// 还原HTML
		// content = HTMLDecoder.decode(content);
		return content;
	}

	public static String stripRepleHtml(String content) {
		// <p>段落替换为换行
		content = content.replaceAll("<p .*?>", "\r");
		// <br><br/>替换为换行
		// content = content.replaceAll("<br\\s*/?>", "\r\n");
		// 去掉其它的<>之间的东西
		// content = content.replaceAll("\\<.*?>", "");
		// 还原HTML
		// content = HTMLDecoder.decode(content);
		return content;
	}
	public static String isHttpImage(String httpUrl) {
		
		if (!AppUtil.isStrNull(httpUrl)) {
			if(!httpUrl.contains("http://")){
				return AppUtil.UPLOADPATH+""+httpUrl;
			}
			return httpUrl;
		}
		return null;
	}
	
	public static String isSplitHtml(String html) {
		String sourceHtml = null;
		if (!AppUtil.isStrNull(html)) {
			sourceHtml = stripHtml(html);
			return sourceHtml;
		}
		return html;
	}

	public static String isSplitOrHtml(String html) {
		String sourceHtml = null;
		if (!AppUtil.isStrNull(html)) {
			sourceHtml = stripRepleHtml(html);
			return sourceHtml;
		}
		return html;
	}

	// decodes image and scales it to reduce memory consumption
	public static Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			FileInputStream stream1 = new FileInputStream(f);
			BitmapFactory.decodeStream(stream1, null, o);
			stream1.close();

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			FileInputStream stream2 = new FileInputStream(f);
			Bitmap bitmap = BitmapFactory.decodeStream(stream2, null, o2);
			stream2.close();
			return bitmap;
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Bitmap getBitmap(String url) {
		File f = new File(FileUtil.noteTheIcFile);
		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;
		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			conn.disconnect();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Throwable ex) {
			ex.printStackTrace();
			if (ex instanceof OutOfMemoryError)
				return null;
		}
		return null;
	}

	public static void compressBmpToFile(Bitmap bmp, File file) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 90;
		int degree = readPictureDegree(file.getPath());
		bmp = rotateBitmap(bmp, degree);
		bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		while (baos.toByteArray().length / 1024 > 320) {
			baos.reset();
			options -= 10;
			bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(baos.toByteArray());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int rotate) {
		if (bitmap == null)
			return null;

		int w = bitmap.getWidth();
		int h = bitmap.getHeight();

		// Setting post rotate to 90
		Matrix mtx = new Matrix();
		mtx.postRotate(rotate);
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 480);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	public static Bitmap getSmallBitmap(String filePath, int width, int height) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, width, height);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	public static List<String> getImageUrl(String strHTML) {
		List<String> oList = new ArrayList<String>();
		int nIndex = 0;
		String strURL = "";
		nIndex = strHTML.indexOf("<img", nIndex);
		while (nIndex != -1) {
			nIndex = strHTML.indexOf("src=", nIndex);
			nIndex = strHTML.indexOf("\"", nIndex);
			strURL = strHTML.substring(nIndex + 1,
					strHTML.indexOf("\"", nIndex + 1));
			oList.add(strURL);
			nIndex = strHTML.indexOf("<img", nIndex);
		}
		return oList;
	}

	public static String html2Text(String inputString) {
		String htmlStr = inputString; // 含html标签的字符串
		String textStr = "";
		java.util.regex.Pattern p_script;
		java.util.regex.Matcher m_script;
		java.util.regex.Pattern p_style;
		java.util.regex.Matcher m_style;
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;

		try {
			String regEx_script = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>
																										// }
			String regEx_style = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>
																									// }
			String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

			p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
			m_script = p_script.matcher(htmlStr);
			htmlStr = m_script.replaceAll(""); // 过滤script标签

			p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
			m_style = p_style.matcher(htmlStr);
			htmlStr = m_style.replaceAll(""); // 过滤style标签

			p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(htmlStr);
			htmlStr = m_html.replaceAll(""); // 过滤html标签

			textStr = htmlStr;

		} catch (Exception e) {
			System.err.println("Html2Text: " + e.getMessage());
		}

		return textStr;// 返回文本字符串
	}

	public static Bitmap ResizeBitmap(Bitmap bitmap, int newWidth) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float temp = ((float) height) / ((float) width);
		int newHeight = (int) ((newWidth) * temp);
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		Matrix matrix = new Matrix();
		// resize the bit map
		matrix.postScale(scaleWidth, scaleHeight);
		// matrix.postRotate(45);
		Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		bitmap.recycle();
		return resizedBitmap;
	}

	public static int[] getRect(Activity context) {
		int rect[] = new int[2];
		Display display = context.getWindowManager().getDefaultDisplay();
		rect[0] = display.getWidth();
		rect[1] = display.getHeight();
		return rect;
	}

}
