package flytv.qaonline.model;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.compos.run.bean.FileBean;

public class MyRecordAdapter extends BaseAdapter {
	public interface MyRecordListener{
		public void clickIndex(int index);
	}
	private class ViewHolder{
		public ImageView iconIv;
		public TextView nameTv;
	}
	private Context mContext;
	private ArrayList<FileBean> fileBeans;
	private MyRecordListener mMyRecordListener;
	public MyRecordAdapter(Context context, ArrayList<FileBean> fileBeans) {
		super();
		this.mContext = context;
		this.fileBeans = fileBeans;
	}

	public void setmMyRecordListener(MyRecordListener mMyRecordListener) {
		this.mMyRecordListener = mMyRecordListener;
	}

	@Override
	public int getCount() {
		return fileBeans.size();
	}

	@Override
	public Object getItem(int arg0) {
		return fileBeans.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int arg0, View convertView, ViewGroup arg2) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.record_item, null);
			viewHolder.iconIv = (ImageView) convertView.findViewById(R.id.re_item_image_icon);
			viewHolder.nameTv = (TextView) convertView.findViewById(R.id.re_item_text_title);
			convertView.setTag(viewHolder);
		}else{
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.nameTv.setText(fileBeans.get(arg0).getName());
		viewHolder.iconIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mMyRecordListener != null)
					mMyRecordListener.clickIndex(arg0);
			}
		});
		return convertView;
	}

}
