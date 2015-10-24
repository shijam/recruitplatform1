package flytv.qaonline.model;

import java.util.List;

import flytv.compos.run.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TextItemAdapter extends BaseAdapter {
	public interface ListItemClick {
		public void onItemClickIndex(int index);
	}

	private Context mContext;
	private List<String> mProvinceData;
	private ListItemClick mListItemClick;

	public TextItemAdapter(Context mContext, List<String> mProvinceData,
			ListItemClick mListItemClick) {
		super();
		this.mContext = mContext;
		this.mProvinceData = mProvinceData;
		this.mListItemClick = mListItemClick;
	}

	@Override
	public int getCount() {
		return mProvinceData.size();
	}

	@Override
	public Object getItem(int position) {
		return mProvinceData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TextView textView;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.view_item_text, null);
			textView = (TextView) convertView
					.findViewById(R.id.view_item_text_tv);
			textView.setTextColor(mContext.getResources().getColor(
					R.color.black));
			convertView.setTag(textView);
		} else {
			textView = (TextView) convertView.getTag();
		}
		textView.setText(mProvinceData.get(position));
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListItemClick.onItemClickIndex(position);
			}
		});
		return convertView;
	}

}
