package flytv.qaonline.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import flytv.compos.run.R;
import flytv.qaonline.entity.QAItemEntity;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.http.HttpServer;
import flytv.qaonline.image.BitmapUtils.BitmapStyle;
import flytv.qaonline.ui.QADetailActivity;

public class ShareQAListAdapter extends BaseAdapter {
	public interface QAListItemListener {
		public void onItemClickListener(int position);
	}

	private final int MESSAGE_CHANGEIMAGE = 0X111;

	private String mFileurl;
	public BitmapUtils bitmapUtils;

	private class ViewHolder {
		public View mainView;
		public TextView titleTv;
		public TextView timeTv;
		public TextView subjectTv;
		public TextView studentNameTv;
		public TextView teacherNameTv;
		public TextView schoolTv;
		public TextView classTv;
		public TextView stateTv;
		public ImageView shareIv;
		public Button openBtn;
	}

	public void setmFileurl(String mFileurl) {
		this.mFileurl = mFileurl;
	}

	private Context mContext;
	private QAListItemListener mQAListItemListener;
	private List<QAItemEntity> mLivesEntitys;
	private RelativeLayout.LayoutParams mViewItemPar;

	public static class ImageObject implements Serializable {
		private static final long serialVersionUID = 1L;
		public ImageView imageIv;
		public Bitmap bitmap;
	}

	private Handler mAdapterHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MESSAGE_CHANGEIMAGE:
				Log.e("Tag", "setimage");
				ImageObject imageObject = (ImageObject) msg.obj;
				imageObject.imageIv.setImageBitmap(imageObject.bitmap);
				imageObject.imageIv.setTag("");
				break;
			default:
				break;
			}
		}
	};

	public ShareQAListAdapter(Context mContext,
			List<QAItemEntity> livesEntitys,
			QAListItemListener qaListItemListener) {
		super();
		this.mContext = mContext;
		this.mLivesEntitys = livesEntitys;
		this.mQAListItemListener = qaListItemListener;
		mViewItemPar = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}

	public void setOnListLvItemListener(QAListItemListener qaListItemListener) {
		this.mQAListItemListener = qaListItemListener;
	}

	@Override
	public int getCount() {
		return mLivesEntitys.size();
	}

	@Override
	public Object getItem(int position) {
		return mLivesEntitys.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.view_item_sharelist,
					null);
			viewHolder.mainView = convertView
					.findViewById(R.id.sharelist_item_layout);
			viewHolder.timeTv = (TextView) convertView
					.findViewById(R.id.tv_item_time);
			viewHolder.subjectTv = (TextView) convertView
					.findViewById(R.id.tv_item_subject);
			viewHolder.studentNameTv = (TextView) convertView
					.findViewById(R.id.tv_item_student);
			viewHolder.teacherNameTv = (TextView) convertView
					.findViewById(R.id.tv_item_teacher);
			viewHolder.shareIv=(ImageView) convertView.findViewById(R.id.iv_item_share);
			viewHolder.classTv = (TextView) convertView
					.findViewById(R.id.tv_item_class);
			viewHolder.openBtn = (Button) convertView
					.findViewById(R.id.bt_open_item);
			viewHolder.mainView.setLayoutParams(mViewItemPar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final QAItemEntity itemEntity = mLivesEntitys.get(position);
		viewHolder.studentNameTv.setText("学生:"
				+ itemEntity.getQuestionUserName());
		viewHolder.teacherNameTv.setText("老师:"
				+ itemEntity.getQuestionTeacherName());
		viewHolder.subjectTv.setText(itemEntity.getQuestionSubjectName());
		// viewHolder.schoolTv.setText(itemEntity.getQuestionSchoolName());
		viewHolder.classTv.setText("(" + itemEntity.getQuestionBclassName() + ")");
		viewHolder.timeTv.setText(itemEntity.getQuestionGmtCreateString());
		viewHolder.openBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, QADetailActivity.class);
				intent.putExtra("questionId", itemEntity.getQuestionId());
				intent.putExtra("isEidtMode",false);
				mContext.startActivity(intent);
			}
		});
		viewHolder.shareIv.setBackgroundResource(R.drawable.image_loading);
		final String imageUrl = mFileurl +"/"+ itemEntity.getFilePath();
		viewHolder.shareIv.setTag(itemEntity.getFilePath());
		HttpEngine.getHttpEngine(mContext).requestImageData(
				viewHolder.shareIv, imageUrl, imageUrl, position,
				new HttpEngineListener() {
					@Override
					public void requestCallBack(final Object data,
							final String resultCode, final int requestCode) {
						final ImageView imageView = (ImageView) viewHolder.mainView
								.findViewWithTag(itemEntity.getFilePath());
						if (imageView != null) {
							if (data != null && requestCode == position && 
									(HttpServer.HTTPSTATE_SUCCESS
									.equals(resultCode))) {
								ImageObject object = new ImageObject();
								object.bitmap = (Bitmap) data;
								object.imageIv = imageView;
								mAdapterHandler.obtainMessage(MESSAGE_CHANGEIMAGE, object).sendToTarget();
							}
						}
					}
				}, BitmapStyle.NULL, true);
		
		
		return convertView;
	}

}
