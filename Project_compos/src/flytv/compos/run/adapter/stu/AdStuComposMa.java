package flytv.compos.run.adapter.stu;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.compos.run.bean.ComposBean;
import flytv.qaonline.utils.DensityConfig;

public class AdStuComposMa extends BaseAdapter {
	public interface QAListItemListener {
		public void onItemClickListener(int position);
	}
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
	private List<ComposBean> mLivesEntitys;
	private RelativeLayout.LayoutParams mViewItemPar;

	public AdStuComposMa(Context mContext, List<ComposBean> livesEntitys,
			QAListItemListener qaListItemListener) {
		super();
		this.mContext = mContext;
		this.mLivesEntitys = livesEntitys;
		this.mQAListItemListener = qaListItemListener;
		mViewItemPar = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, DensityConfig.getDensityConfig(
						mContext).getQaItemHeight());
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
			convertView = View.inflate(mContext, R.layout.layout_item_compos_stu,
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
		ComposBean itemEntity = mLivesEntitys.get(position);
		viewHolder.subjectTv.setText(itemEntity.getSubjectName());
		viewHolder.schoolTv.setText(itemEntity.getSchoolName());
		viewHolder.titleTv.setText(itemEntity.getName());
		viewHolder.timeTv.setText(itemEntity.getCreateTimeStr());
		Resources resource =  mContext.getResources();
		if (itemEntity.getMarkStatus()==1) {
			viewHolder.stateTv.setText("未批阅");
			viewHolder.stateTv.setTextColor(resource.getColor(R.color.red));
		} else if (itemEntity.getMarkStatus()==2) {
			viewHolder.stateTv.setText("已批阅");
			viewHolder.stateTv.setTextColor(resource.getColor(R.color.base_btn_add_color_norssed));
		}else if (itemEntity.getMarkStatus()==0) {
			viewHolder.stateTv.setText("未作答");
			viewHolder.stateTv.setTextColor(resource.getColor(R.color.red));
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
