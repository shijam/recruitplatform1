package flytv.qaonline.model;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.qaonline.entity.QAItemEntity;
import flytv.qaonline.entity.TeacherItemEntity;
import flytv.qaonline.entity.VedioItemEntity;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpServer;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.image.BitmapUtils.BitmapStyle;
import flytv.qaonline.model.VedioQAListAdapter.ImageObject;
import flytv.qaonline.utils.DensityConfig;

public class TeachersListAdapter extends BaseAdapter {
	public interface TeachersIvListener {
		public void onIvClickListener(int position);
	}
	private final int MESSAGE_CHANGEIMAGE = 0X10001;

	private class ViewHolder {
		public View mainView;
		public TextView teacherNameTv;
		public TextView teacherSchoolTv;
		public TextView teachetTypeTv;
		public TextView studentNameTv;
		public ImageView selectIv;
		public ImageView IconIv;
	}
	private String mFileserverPath;
	private int mIndex=-1;
	public int getmIndex() {
		return mIndex;
	}
	public void setmIndex(int mIndex) {
		this.mIndex = mIndex;
	}

	private Context mContext;
	private TeachersIvListener mListener;
	private List<TeacherItemEntity> mLivesEntitys;
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
				ImageObject imageObject = (ImageObject) msg.obj;
				imageObject.imageIv.setImageBitmap(imageObject.bitmap);
				imageObject.imageIv.setTag("");
				break;
			default:
				break;
			}
		}
	};
	public void setPath(String fileString){
		this.mFileserverPath=fileString;
	}
	public TeachersListAdapter(Context mContext,
			List<TeacherItemEntity> livesEntitys,
			TeachersIvListener teacherItemListener) {
		super();
		this.mListener=teacherItemListener;
		this.mContext = mContext;
		this.mLivesEntitys = livesEntitys;
		mViewItemPar = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	public void setOnListLvItemListener(TeachersIvListener itemListener) {
		this.mListener = itemListener;
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
			convertView = View.inflate(mContext, R.layout.view_item_teachers,
					null);
			viewHolder.mainView = convertView
					.findViewById(R.id.teacherlist_item_layout);
			viewHolder.teacherNameTv = (TextView) convertView
					.findViewById(R.id.tv_name_teacher);
			viewHolder.teacherSchoolTv = (TextView) convertView
					.findViewById(R.id.tv_school_teacher);
			viewHolder.teachetTypeTv = (TextView) convertView
					.findViewById(R.id.tv_type_teacher);
			viewHolder.selectIv=(ImageView) convertView.findViewById(R.id.iv_select_teacher);
			viewHolder.IconIv=(ImageView) convertView.findViewById(R.id.iv_icon_teacher);
			viewHolder.mainView.setLayoutParams(mViewItemPar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		TeacherItemEntity teacherItemEntity = mLivesEntitys.get(position);
		
		viewHolder.teacherNameTv.setText(teacherItemEntity.getTeacherUserName());
		viewHolder.teacherSchoolTv.setText(teacherItemEntity.getTeacherSchoolName());
		viewHolder.teachetTypeTv.setText(teacherItemEntity.getTeacherUserType());
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListener.onIvClickListener(position);
			}
		});
		if (position==mIndex) {
			viewHolder.selectIv.setBackgroundResource(R.drawable.select_teacher);
		}else {
			viewHolder.selectIv.setBackgroundResource(R.drawable.not_select_teacher);
		}
		viewHolder.IconIv.setBackgroundResource(R.drawable.default_logo);
		final String imageUrl=mFileserverPath+"/"+mLivesEntitys.get(position).getTeacherPhoto();
		viewHolder.IconIv.setTag(imageUrl);
		HttpEngine.getHttpEngine(mContext).requestImageData(
				viewHolder.IconIv, imageUrl, imageUrl,
				position, new HttpEngineListener() {
					@Override
					public void requestCallBack(final Object data,
							final String resultCode, final int requestCode) {
						final ImageView imageView = (ImageView) viewHolder.mainView
								.findViewWithTag(imageUrl);
						if (imageView != null) {
							if (data != null
									&& requestCode == position
									&& (HttpServer.HTTPSTATE_SUCCESS
											.equals(resultCode))) {
								ImageObject object = new ImageObject();
								object.bitmap = (Bitmap) data;
								object.imageIv = imageView;
								mAdapterHandler.obtainMessage(
										MESSAGE_CHANGEIMAGE, object)
										.sendToTarget();
							}
						}
					}
				}, BitmapStyle.NULL, true);
		
		return convertView;
	}

}
