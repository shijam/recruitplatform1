package flytv.qaonline.model;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;
import flytv.compos.run.R;
import flytv.compos.run.adpublic.AyVideoCompoe;
import flytv.qaonline.entity.MyFileEntity;
import flytv.qaonline.entity.VedioItemEntity;
import flytv.qaonline.http.HttpEngine;
import flytv.qaonline.http.HttpEngine.HttpEngineListener;
import flytv.qaonline.http.HttpServer;
import flytv.qaonline.image.BitmapUtils.BitmapStyle;

public class VedioQAListAdapter extends BaseAdapter {
	public interface QAListItemListener {
		public void onItemClickListener(int position);
	}

	private final int MESSAGE_CHANGEIMAGE = 0X11001;

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

	private String mFileserverPath;
	private Context mContext;
	private QAListItemListener mQAListItemListener;
	private List<VedioItemEntity> mLivesEntitys;
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

	public void setPath(String fileString) {
		this.mFileserverPath = fileString;
	}

	public VedioQAListAdapter(Context mContext,
			List<VedioItemEntity> livesEntitys,
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
			convertView = View.inflate(mContext, R.layout.view_item_vediolist,
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
			// viewHolder.schoolTv = (TextView)
			// convertView.findViewById(R.id.qalist_item_school_tv);
			viewHolder.classTv = (TextView) convertView
					.findViewById(R.id.tv_item_class);
			viewHolder.openBtn = (Button) convertView
					.findViewById(R.id.bt_open_item);
			viewHolder.shareIv=(ImageView) convertView.findViewById(R.id.iv_item_share);
			viewHolder.mainView.setLayoutParams(mViewItemPar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		final MyFileEntity myFileEntity = mLivesEntitys.get(position).getFilelist().size() > 0 ?
				mLivesEntitys.get(position).getFilelist().get(mLivesEntitys.get(position).getFilelist().size() - 1): null;
		VedioItemEntity itemEntity = mLivesEntitys.get(position);
		viewHolder.teacherNameTv.setText("标题:" + itemEntity.getTitle());
		viewHolder.studentNameTv.setText("老师:" + itemEntity.getUserName());
		viewHolder.subjectTv.setText(itemEntity.getSubjectName());
		viewHolder.classTv.setText("(" + itemEntity.getGradeName() + ")");
		viewHolder.timeTv.setText(itemEntity.getCreateTimeStr());
		viewHolder.openBtn.setText("查看视频");
		viewHolder.openBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(myFileEntity != null && !StringUtils.isBlank(myFileEntity.getFileUrl())){
					Intent intent = new Intent();
					intent.setClass(mContext, AyVideoCompoe.class); // 意图是打开文件
					intent.putExtra("httpUrl",
							mFileserverPath
							+ mLivesEntitys.get(position).getFilelist()
							.get(0).getFileUrl());
					mContext.startActivity(intent);
				}else{
					Toast.makeText(mContext, "获取视频地址失败", Toast.LENGTH_LONG).show();
				}
			}
		});
		viewHolder.shareIv.setBackgroundResource(R.drawable.image_loading);
		if(myFileEntity != null){
			final String imageUrl= mFileserverPath +"/"+ myFileEntity.getThumbPath();
			viewHolder.shareIv.setTag(imageUrl);
			HttpEngine.getHttpEngine(mContext).requestImageData(
					viewHolder.shareIv, imageUrl, imageUrl,
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
		}
		return convertView;
	}

}
