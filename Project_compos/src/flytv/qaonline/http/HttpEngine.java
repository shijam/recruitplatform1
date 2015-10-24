/**
 * 文件名：HttpEngine
 * 创建时间：2014-04-23 17:09:29
 * @author Jonze
 * @version 1.0.0
 */
package flytv.qaonline.http;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import flytv.compos.run.R;
import flytv.compos.run.bean.FileBean;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.UserShareUtils;
import flytv.qaonline.entity.NewQuestionItemEntity;
import flytv.qaonline.entity.QADetailItemEntity;
import flytv.qaonline.entity.QARequestEntity;
import flytv.qaonline.entity.QATeacherListEntity;
import flytv.qaonline.entity.ResultEntity;
import flytv.qaonline.entity.VedioEntity;
import flytv.qaonline.image.BitmapUtils.BitmapStyle;
import flytv.qaonline.request.AutoTeacherRequest;
import flytv.qaonline.request.EndQuestionRequest;
import flytv.qaonline.request.ExperimentQADataRequest;
import flytv.qaonline.request.FindTeacherRequest;
import flytv.qaonline.request.ImageRequest;
import flytv.qaonline.request.MarkQuestionRequest;
import flytv.qaonline.request.MyQADataRequest;
import flytv.qaonline.request.MyTeacherDataRequest;
import flytv.qaonline.request.NewQuestionRequest;
import flytv.qaonline.request.PublishVedioRequest;
import flytv.qaonline.request.QADetailDataParser;
import flytv.qaonline.request.QADetailDataRequest;
import flytv.qaonline.request.QAListDataParser;
import flytv.qaonline.request.ResultParser;
import flytv.qaonline.request.SetScoreRequest;
import flytv.qaonline.request.ShareQADataRequest;
import flytv.qaonline.request.ShareQuestionRequest;
import flytv.qaonline.request.TeachersParser;
import flytv.qaonline.request.TestAnswerRequest;
import flytv.qaonline.request.TestMarkRequest;
import flytv.qaonline.request.TestQuestionRequest;
import flytv.qaonline.request.VedioListDataParser;
import flytv.run.bean.TVCodeBean;

/**
 * @author Jonze
 * @version 1.0.0
 * @category http引擎
 */
public class HttpEngine {
	public interface HttpEngineListener {
		public void requestCallBack(final Object data, final String resultCode,
				final int requestCode);
	}

	private Context mContext;
	private static HttpEngine mHttpEngine = null;

	private ExecutorService mHttpExecutor; // 网络请求控制
	private ExecutorService mImageExecutor; // 图片加载控制

	private int mScreenWidth;
	private int mScreenHeight;

	public static HttpEngine getHttpEngine(Context context) {
		if (mHttpEngine == null) {
			mHttpEngine = new HttpEngine(context);
		}
		return mHttpEngine;
	}

	private HttpEngine(Context context) {
		mContext = context;
		mHttpExecutor = Executors.newFixedThreadPool(1);
		mImageExecutor = Executors.newFixedThreadPool(9);
		mScreenWidth = mContext.getResources().getDisplayMetrics().widthPixels;
		mScreenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
	}

	public void closeEngine() {
		ImageRequest.getImageRequest(mContext).clear();
		if (mHttpExecutor != null) {
			mHttpExecutor.shutdown();
		}
		if (mImageExecutor != null) {
			mImageExecutor.shutdown();
		}
		System.gc();
	}

	/**
	 * 获取问题接口
	 */
	public void requestQAListData(final String userId, final int pageSize,
			final int currentPage, final String search,
			final HttpEngineListener enginerListener) {
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = MyQADataRequest.getMyQaDataRequest(
						mContext).getQAListData(userId, pageSize, currentPage,
						search);
				QARequestEntity entity = QAListDataParser
						.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}

	/**
	 * 获取随便看看列表接口
	 */
	public void requestShareListData(final String userId, final int pageSize,
			final int currentPage, final String search,
			final HttpEngineListener enginerListener) {
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = ShareQADataRequest
						.getMyshareDataRequest(mContext).getShareListData(
								userId, pageSize, currentPage, search);
				QARequestEntity entity = QAListDataParser
						.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}

	/**
	 * 获取视频列表接口
	 */
	public void requestexperimentListData(final String userId,
			final int pageSize, final int currentPage, final String search,
			final HttpEngineListener enginerListener) {
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = ExperimentQADataRequest
						.getMyshareDataRequest(mContext).getShareListData(
								userId, pageSize, currentPage, search);
				VedioEntity entity = VedioListDataParser
						.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}

	/**
	 * 新建问题接口
	 */
	public void requestNewQuestion(final NewQuestionItemEntity entity,
			final HttpEngineListener enginerListener) {
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String requestData[] = NewQuestionRequest.submitNewQuestion(
						mContext).getShareListData(entity);
				ResultEntity entity = ResultParser
						.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}

	/**
	 * 名师列表接口
	 */
	public void requestTeacherList(final String userId, final int pageSize,
			final int currentPage, final String subjectname,
			final String search, final HttpEngineListener enginerListener) {
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				System.out.println("------------------");
				String requestData[] = MyTeacherDataRequest
						.getMyTeacherRequest(mContext).getTeacherListData(
								userId, pageSize, subjectname, currentPage,
								search);
				QATeacherListEntity entity = TeachersParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}

	/**
	 * 问题详情
	 */
	public void requestQADetailtData(final String userId,final String questionId,final HttpEngineListener enginerListener){
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = QADetailDataRequest.getQaDetailDataRequest(mContext).getQADetailData(userId, questionId);
				List<QADetailItemEntity> entity =  QADetailDataParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}
	
	/**
	 * 回答考一考
	 */
	public void aswTestQuestion(final String userId,final int questionId,final int testQuestionId,
			final String content,final String imagesIds,
			final String audioIds,final String videoIds,final HttpEngineListener enginerListener){
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = TestAnswerRequest.testAnswer(mContext).testAnswerData(userId, 
						questionId, testQuestionId, 
						content, imagesIds,
						audioIds, videoIds);
				ResultEntity entity = ResultParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}
	
	/**
	 * 评语
	 */
	public void markTestQuestion(final String userId,final int questionId,final int testAnswerId,
			final String content,final String imagesIds,
			final String audioIds,final String videoIds,final HttpEngineListener enginerListener){
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = TestMarkRequest.testMark(mContext).markTestMark(userId, questionId, testAnswerId, content, imagesIds, audioIds, videoIds);
				ResultEntity entity = ResultParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}
	
	/**
	 * 打分
	 */
	public void setQuestionScore(final int questionId,final int score,final HttpEngineListener enginerListener){
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = SetScoreRequest.setScore(mContext).setScoreData(questionId, score);
				ResultEntity entity = ResultParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}
	/**
	 * 选择名师提问
	 */
	public void requestTeacherQaData(final String userId,final String questionId,final String markUserId,final HttpEngineListener enginerListener){
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = FindTeacherRequest.submitFindTeacer(mContext).submitFindTeacher(userId, markUserId,questionId);
				ResultEntity entity = ResultParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}
	/**
	 * 自动提交问题
	 */
	public void autoTeacherQaData(final String userId,final String questionId,final HttpEngineListener enginerListener){
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = AutoTeacherRequest.autoFindTeacer(mContext).autoFindTeacher(userId,questionId);
				ResultEntity entity = ResultParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}
	/**
	 * 发布视频
	 */
	public void publishVedio(final String userId,final String title,final String subjectName,final String sectionName,final String gradeName,final String fileIds,final HttpEngineListener enginerListener){
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = PublishVedioRequest.submitExperiment(mContext).submitExperimentData(userId, title, subjectName, sectionName, gradeName,fileIds);
				ResultEntity entity = ResultParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}
	/**
	 * 分享问题接口
	 */
	public void shareQuestion(final String id,final HttpEngineListener enginerListener){
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = ShareQuestionRequest.sharaQuestion(mContext).sharaQuestionData(id);
				ResultEntity entity = ResultParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}
	/**
	 * 学会了接口
	 */
	public void endQuestion(final String id,final HttpEngineListener enginerListener){
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = EndQuestionRequest.endQuestion(mContext).endQuestionData(id);
				ResultEntity entity = ResultParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}
	/**
	 * 考一考接口
	 */
	public void testQuestion(final String userId,final String content,final String questionId,final String imagesIds, final String audioIds,final String videoIds,final HttpEngineListener enginerListener){
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = TestQuestionRequest.testQuestion(mContext).testQuestionData(userId, content, questionId, imagesIds, audioIds, videoIds);
				ResultEntity entity = ResultParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}
	/**
	 * 回答问题接口
	 */
	public void markQuestion(final String userId,final int questionId,final int markId,
			final String score,final String note,final String type,final String imagesIds,
			final String voiceIds,final String videoIds,final String postilIds,final int submitStatus,final HttpEngineListener enginerListener){
		mHttpExecutor.execute(new Runnable() {
			@Override
			public void run() {
				String[] requestData = MarkQuestionRequest.markQuestion(mContext).markQuestionData(userId, questionId, markId, score, note, type, imagesIds, voiceIds, videoIds, postilIds, submitStatus);
				ResultEntity entity = ResultParser.parserRequestData(requestData[1]);
				enginerListener.requestCallBack(entity, requestData[0], 0);
			}
		});
	}
	
	/**
	 * 上传文件
	 */
	public void requestFileUploadData(final int updataType, final File file,
			final HttpEngineListener enginerListener) {
		String filePath = file.getAbsolutePath();
		TVCodeBean entity = (TVCodeBean) UserShareUtils.getInstance()
				.getLoginInfo(mContext);
		String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
		if (AppUtil.isStrNull(filePath)) {
			AlertTools.showTips(mContext, R.drawable.tips_warning, "上传路径错误");
			return;
		}
		String fileUploadUrl = null;
		if (fileName.toLowerCase().contains("jpg") || fileName.toLowerCase().contains("png")
				|| fileName.toLowerCase().contains("mp4") || fileName.toLowerCase().contains("mp3")) {
			fileUploadUrl = AppUtil.getStringId(mContext)
					+ mContext.getResources()
							.getString(R.string.flytv_user_file_uploadImage)
							.replace("{userId}", entity.getUserId());
			RequestParams params = new RequestParams();
			params.addBodyParameter("fileName", fileName);
			TVCodeBean entityFile = UserShareUtils.getInstance().getTVInfo(
					mContext);
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
						}

						@Override
						public void onLoading(long total, long current,
								boolean isUploading) {
						}

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							enginerListener.requestCallBack(null,
									HttpServer.HTTPSTATE_ERROR, 100);
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							TVCodeBean tvCodeBean = (TVCodeBean) AppUtil
									.getJSONBean(arg0.result, TVCodeBean.class);
							if (tvCodeBean != null) {
								FileBean fileBean = new FileBean();
								fileBean.setImg_typeId(0);
								fileBean.setFileUrl(AppUtil.getIPSplit(mContext, AppUtil.UPLOADPATH + "/" + tvCodeBean.getFilePath()));
								switch (updataType) {
									case 0:
										fileBean.setThumbPath(AppUtil.getIPSplit(
												mContext, AppUtil.UPLOADPATH + "/"
														+ tvCodeBean.getFilePath()));
										fileBean.setExtType("img");
										break;
									case 1:
										fileBean.setThumbPath(AppUtil.getIPSplit(
												mContext, AppUtil.UPLOADPATH + "/"
														+ tvCodeBean.getThumbPath()));
										fileBean.setExtType("video");
										break;
									case 2:
										fileBean.setThumbPath(AppUtil.getIPSplit(
												mContext, AppUtil.UPLOADPATH + "/"
														+ tvCodeBean.getThumbPath()));
										fileBean.setExtType("mp3");
										break;
									default:
										break;
								}
								fileBean.setName(tvCodeBean.getFileName());
								try {
									fileBean.setId(Integer.parseInt(tvCodeBean
											.getId()));
								} catch (Exception e) {
									// TODO: handle exception
									enginerListener.requestCallBack(fileBean,HttpServer.HTTPSTATE_ERROR, 100);
								}
								enginerListener.requestCallBack(fileBean,HttpServer.HTTPSTATE_SUCCESS, 100);
							}
						}
					});
		}
	}

	/**
	 * 请求图片接口
	 */
	@SuppressLint("NewApi")
	public void requestImageData(ImageView imageView, final String imageKey,
			final String imageUrl, final int position,
			final HttpEngineListener enginerListener,
			final BitmapStyle bitmapStyle, boolean isRsc) {
		Bitmap bitmapData = ImageRequest.getImageRequest(mContext)
				.getImageDataFromMemory(imageUrl, bitmapStyle);
		if (bitmapData != null) {
			enginerListener.requestCallBack(bitmapData,
					HttpServer.HTTPSTATE_SUCCESS, position);
		} else {
			if (isRsc) {
				imageView.setImageBitmap(null);
			} else {
				imageView.setBackground(null);
			}
			mImageExecutor.execute(new Runnable() {
				@Override
				public void run() { // max 448*252
					Bitmap requestData = ImageRequest.getImageRequest(mContext)
							.getImageData(imageUrl, true, mScreenWidth,
									mScreenHeight, bitmapStyle);
					enginerListener.requestCallBack(requestData,
							requestData == null ? HttpServer.HTTPSTATE_ERROR
									: HttpServer.HTTPSTATE_SUCCESS, position);
				}
			});
		}
	}

}

// end of the file

