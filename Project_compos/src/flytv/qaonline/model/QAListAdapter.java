package flytv.qaonline.model;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.ext.utils.UserShareUtils;
import flytv.qaonline.entity.QAItemEntity;
import flytv.qaonline.utils.DensityConfig;
import flytv.run.bean.TVCodeBean;

public class QAListAdapter extends BaseAdapter {
	private TVCodeBean mUserBean;
	public interface QAListItemListener {
		public void onItemClickListener(int position);
	}

	private final int MESSAGE_CHANGEIMAGE = 0X10001;

	private class ViewHolder {
		public View mainView;
		public TextView titleTv;
		public TextView timeTv;
		public TextView subjectTv;
		public TextView nameTv;
		public TextView schoolTv;
		public TextView classTv;
		public TextView stateTv;
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
				ImageObject imageObject = (ImageObject) msg.obj;
				imageObject.imageIv.setImageBitmap(imageObject.bitmap);
				imageObject.imageIv.setTag("");
				break;
			default:
				break;
			}
		}
	};

	public QAListAdapter(Context mContext, List<QAItemEntity> livesEntitys,
			QAListItemListener qaListItemListener) {
		super();
		this.mContext = mContext;
		mUserBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				mContext);
		this.mLivesEntitys = livesEntitys;
		this.mQAListItemListener = qaListItemListener;
		mViewItemPar = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
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
			convertView = View.inflate(mContext, R.layout.view_item_qalist,
					null);
			viewHolder.mainView = convertView
					.findViewById(R.id.qalist_item_layout);
			viewHolder.titleTv = (TextView) convertView
					.findViewById(R.id.qalist_item_title_tv);
			viewHolder.timeTv = (TextView) convertView
					.findViewById(R.id.qalist_item_time_tv);
			viewHolder.subjectTv = (TextView) convertView
					.findViewById(R.id.qalist_item_subject_tv);
			viewHolder.nameTv = (TextView) convertView
					.findViewById(R.id.qalist_item_name_tv);
			viewHolder.schoolTv = (TextView) convertView
					.findViewById(R.id.qalist_item_school_tv);
			viewHolder.classTv = (TextView) convertView
					.findViewById(R.id.qalist_item_class_tv);
			viewHolder.stateTv = (TextView) convertView
					.findViewById(R.id.qalist_item_state_tv);
			viewHolder.mainView.setLayoutParams(mViewItemPar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		QAItemEntity itemEntity = mLivesEntitys.get(position);
		viewHolder.nameTv.setText(itemEntity.getQuestionUserName());
		viewHolder.subjectTv.setText(itemEntity.getQuestionSubjectName());
		viewHolder.schoolTv.setText(itemEntity.getQuestionSchoolName());
		viewHolder.classTv.setText(itemEntity.getQuestionBclassName());
		viewHolder.titleTv.setText(itemEntity.getQuestionKeywords());
		viewHolder.timeTv.setText(itemEntity.getQuestionGmtCreateString());
		if ("1".equals(itemEntity.getMarkStatus())) {
			viewHolder.stateTv.setText("未解决");
		} else if ("2".equals(itemEntity.getMarkStatus())) {
			viewHolder.stateTv.setText("已解决");
		}else if ("3".equals(itemEntity.getMarkStatus()) && "1".equals(mUserBean.getUserType())) {
			viewHolder.stateTv.setText("未解决");
		}else if("3".equals(itemEntity.getMarkStatus()) && "2".equals(mUserBean.getUserType())){
			viewHolder.stateTv.setText("未作答");
		}else{
			viewHolder.stateTv.setText("已解决");
		}
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mQAListItemListener != null)
					mQAListItemListener.onItemClickListener(position);
			}
		});
		return convertView;
	}

}
