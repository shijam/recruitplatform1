package flytv.compos.run.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.compos.run.bean.ComposBean;
import flytv.ext.view.inter.ItemCommentClickLister;

public class AdCompos extends BaseAdapter {

	private Context context;
	private ArrayList<ComposBean> items;
	private ViewHolderStudy viewHolderStudy;
	private ItemCommentClickLister itemCommentClickLister;

	public ItemCommentClickLister getItemCommentClickLister() {
		return itemCommentClickLister;
	}

	public void setItemCommentClickLister(
			ItemCommentClickLister itemCommentClickLister) {
		this.itemCommentClickLister = itemCommentClickLister;
	}

	public AdCompos(Context context, ArrayList<ComposBean> items) {
		super();
		this.context = context;
		this.items = items;
	}

	@Override
	public int getCount() {
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ComposBean enCircle = items.get(position);
		if (convertView == null) {
			viewHolderStudy = new ViewHolderStudy();
			convertView = View.inflate(context, R.layout.layout_compos_item,
					null);
			viewHolderStudy.tx_title = (TextView) convertView
					.findViewById(R.id.tx_title);
			viewHolderStudy.tx_send_time = (TextView) convertView
					.findViewById(R.id.tx_compos_submit_time);
			viewHolderStudy.tx_submit_num = (TextView) convertView
					.findViewById(R.id.tx_compos_submit_num);
			viewHolderStudy.tx_mark_num = (TextView) convertView
					.findViewById(R.id.tx_compos_mark_num);

			viewHolderStudy.tx_edit = (TextView) convertView
					.findViewById(R.id.tx_compos_edit);
			viewHolderStudy.tx_delete = (TextView) convertView
					.findViewById(R.id.tx_compos_dele);
			convertView.setTag(viewHolderStudy);
		} else {
			viewHolderStudy = (ViewHolderStudy) convertView.getTag();
		}
		viewHolderStudy.tx_title.setText(enCircle.getName());
		viewHolderStudy.tx_send_time.setText(enCircle.getCreateTimeStr());
		viewHolderStudy.tx_submit_num.setText(context.getResources().getString(
				R.string.main_text_compos_submit_num)+"  "
				+ enCircle.getCommitNum() + "/" + enCircle.getAllNum());
		viewHolderStudy.tx_mark_num.setText(context.getResources().getString(
				R.string.main_text_compos_mark_num)+"  "
				+ enCircle.getReadNum() + "/" + enCircle.getAllNum());
		if (enCircle.getStatus() ==2) {
			viewHolderStudy.tx_edit.setVisibility(View.INVISIBLE);
		} else {
			viewHolderStudy.tx_edit.setVisibility(View.VISIBLE);
		}
		viewHolderStudy.tx_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				itemCommentClickLister.onBottomDeleLog(position);
			}
		});
		viewHolderStudy.tx_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				itemCommentClickLister.onBottomEditLog(position);
			}
		});
		return convertView;
	}

	class ViewHolderStudy {
		TextView tx_title, tx_send_time, tx_submit_num, tx_mark_num, tx_edit,
				tx_delete;

	}

}
