package flytv.compos.run.adapter;

import in.srain.cube.image.CubeImageView;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

import flytv.compos.run.R;
import flytv.compos.run.bean.AnswerBean;
import flytv.compos.run.bean.FileBean;
import flytv.compos.run.bean.QuestionBean;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.view.MyGridView;
import flytv.ext.view.inter.ItemCommentClickLister;

/**
 * 老师批阅作业
 * 
 * @author nike
 * 
 */
public class AdComposMark extends BaseAdapter {

	private ItemCommentClickLister itemCommentClickLister;
	private Context context;
	private ArrayList<QuestionBean> items;
	private boolean isEdit;
	private BitmapUtils bitmapUtils;
	HashMap<Integer, String> mapMsg = new HashMap<Integer, String>();

	public void setItemCommentClickLister(
			ItemCommentClickLister itemCommentClickLister) {
		this.itemCommentClickLister = itemCommentClickLister;
	}

	public HashMap<Integer, String> getMapMsg() {
		return mapMsg;
	}

	public AdComposMark(Context context, ArrayList<QuestionBean> items,
			BitmapUtils bitmapUtils, boolean isEdit) {
		super();
		this.context = context;
		this.items = items;
		this.isEdit = isEdit;
		this.bitmapUtils = bitmapUtils;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		initArray();
	}

	void initArray() {
		typeIndex = 0;
		typePersentIndex = 0;
		for (int i = 0; i < this.items.size(); i++) {
			QuestionBean entity = items.get(i);
			if (entity.answerList.size() > 0) {
				AnswerBean answerBean = entity.answerList.get(0);
				mapMsg.put(i, answerBean.getMarkContent());
			}
			boolean isShow = false;
			if (i == 0) {
				typePersentIndex = 0;
				isShow = true;
			} else {
				//
				if (entity.getQuestionType() != items.get(i - 1)
						.getQuestionType()) {
					typeIndex++;
					isShow = true;
					typePersentIndex = 0;
				} else {
					// typeIndex++;
					isShow = false;
					typePersentIndex++;
					//

				}

			}
			entity.setShowType(isShow);
			entity.setComposType(typeIndex);
			entity.setComposTypeSum(typePersentIndex);
		}
	}

	int typeIndex = 0;
	int typePersentIndex = 0;
	private Integer index = -1;

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final QuestionBean enCircle = items.get(arg0);
		ViewHolderStudy viewHolderStudy;
		if (arg1 == null) {
			viewHolderStudy = new ViewHolderStudy();
			arg1 = View
					.inflate(context, R.layout.layout_cm_question_item, null);
			viewHolderStudy.tx_qu_type_num = (TextView) arg1
					.findViewById(R.id.tx_qu_type_num);
			viewHolderStudy.tx_qu_type_name = (TextView) arg1
					.findViewById(R.id.tx_qu_type_name);
			viewHolderStudy.tx_title = (TextView) arg1
					.findViewById(R.id.tx_qu_name);
			viewHolderStudy.tx_student_content = (TextView) arg1
					.findViewById(R.id.tx_student_content);
			viewHolderStudy.edit_comment = (EditText) arg1
					.findViewById(R.id.edit_comment);
			viewHolderStudy.layout_file = (LinearLayout) arg1
					.findViewById(R.id.layout_file);

			viewHolderStudy.btn_file_add = (Button) arg1
					.findViewById(R.id.btn_file_add);

			viewHolderStudy.layout_handelr = (LinearLayout) arg1
					.findViewById(R.id.layout_handler);
			viewHolderStudy.layout_question_compos_type = (LinearLayout) arg1
					.findViewById(R.id.layout_question_compos_type);
			viewHolderStudy.layout_mark_answer = (LinearLayout) arg1
					.findViewById(R.id.layout_mark_answer);

			viewHolderStudy.myGridView = (MyGridView) arg1
					.findViewById(R.id.gv_image_all);
			viewHolderStudy.gv_image_answer = (MyGridView) arg1
					.findViewById(R.id.gv_image_answer);
			viewHolderStudy.edit_comment.setTag(arg0);
			viewHolderStudy.edit_comment
					.setOnTouchListener(new OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_UP) {
								index = (Integer) v.getTag();
							}
							return false;
						}
					});
			class MyTextWatcher implements TextWatcher {
				public MyTextWatcher(ViewHolderStudy holder) {
					mHolder = holder;
				}

				private ViewHolderStudy mHolder;

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (s != null && !"".equals(s.toString())) {
						int position = (Integer) mHolder.edit_comment.getTag();
						// 当EditText数据发生改变的时候存到data变量中
						mapMsg.put(position, s.toString());
					}
				}
			}
			viewHolderStudy.edit_comment
					.addTextChangedListener(new MyTextWatcher(viewHolderStudy));
			arg1.setTag(viewHolderStudy);
		} else {
			viewHolderStudy = (ViewHolderStudy) arg1.getTag();
			viewHolderStudy.edit_comment.setTag(arg0);
		}

		String title = AppUtil.questionMap.get(enCircle.getQuestionType());

		if (enCircle.isShowType()) {
			viewHolderStudy.layout_question_compos_type
					.setVisibility(View.VISIBLE);
		} else {
			viewHolderStudy.layout_question_compos_type
					.setVisibility(View.GONE);
		}
		String typeLeft = AppUtil.getComposType(enCircle.getComposType());
		viewHolderStudy.tx_qu_type_num.setText(typeLeft + "" + title);
		viewHolderStudy.tx_title.setText((arg0 + 1) + ": "
				+ enCircle.getQuestionTitle());
		viewHolderStudy.layout_handelr.setVisibility(View.GONE);
		ArrayList<FileBean> files = enCircle.files;
		viewHolderStudy.layout_file.removeAllViews();
		ArrayList<AnswerBean> answerList = enCircle.answerList;
		if (answerList.size() > 0) {
			AnswerBean entity = answerList.get(0);
			ArrayList<FileBean> answersList = entity.answerFiles;
			viewHolderStudy.tx_student_content.setText(context
					.getString(R.string.compos_mark_comment_answer)
					+ entity.getUserAnswer());
			// test
			//
			AdFriendImage adFriendImage = new AdFriendImage(context,
					bitmapUtils, answersList);
			viewHolderStudy.gv_image_answer.setAdapter(adFriendImage);
			// 老师批阅
			ArrayList<FileBean> markList = entity.markFiles;
			if (markList.size() > 0) {
				initImage(viewHolderStudy.layout_file,
						viewHolderStudy.btn_file_add, markList.get(0), !isEdit);
			} else {
				initImage(viewHolderStudy.layout_file,
						viewHolderStudy.btn_file_add, null, !isEdit);
			}

		} else {

		}
		viewHolderStudy.edit_comment.setText(mapMsg.get(arg0));
		viewHolderStudy.edit_comment.clearFocus();
		if (index != -1 && index == arg0) {
			viewHolderStudy.edit_comment.requestFocus();
		}
		if (files.size() > 0) {
			AdFriendImage adFriendImage = new AdFriendImage(context,
					bitmapUtils, files);
			viewHolderStudy.myGridView.setAdapter(adFriendImage);
			viewHolderStudy.myGridView.setVisibility(View.VISIBLE);
		} else {
			viewHolderStudy.myGridView.setVisibility(View.GONE);
		}
		viewHolderStudy.layout_mark_answer.setVisibility(View.VISIBLE);

		viewHolderStudy.btn_file_add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (itemCommentClickLister != null) {
					itemCommentClickLister.onBottomDeleLog(arg0);
				}
			}
		});
		if (isEdit) {
			viewHolderStudy.edit_comment.setEnabled(true);
		} else {
			viewHolderStudy.edit_comment.setEnabled(false);
		}
		return arg1;
	}

	void initImage(LinearLayout layout_file, Button btn_file_add,
			FileBean fileBean, boolean isValue) {
		LogUtils.print("Adapter()=initImage()");
		if (isValue) {
			btn_file_add.setVisibility(View.GONE);
			layout_file.removeAllViews();
		} else {
			btn_file_add.setVisibility(View.VISIBLE);
		}
		if (fileBean != null) {
			View view = View.inflate(context, R.layout.layout_add_image_item,
					null);
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
				LogUtils.print("imageUrl=" + httpImageUrl.contains("http://"));
				if (!httpImageUrl.contains("http://")) {
					httpImageUrl = AppUtil.UPLOADPATH + httpImageUrl;
				}
				final String imageUrl = httpImageUrl;
				LogUtils.print("imageUrl=" + imageUrl);
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

	class ViewHolderStudy {
		TextView tx_qu_type_num, tx_qu_type_name, tx_title;
		LinearLayout layout_handelr, layout_question_compos_type,
				layout_mark_answer;
		MyGridView myGridView, gv_image_answer;
		// 学生 老师 数据布局
		TextView tx_student_content;
		LinearLayout layout_file;
		Button btn_file_add;
		EditText edit_comment;
	}

}
