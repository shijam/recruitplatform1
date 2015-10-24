package flytv.sound.control.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import flytv.compos.run.R;

public class PopSettingsWindowAdapter extends BaseAdapter {
	private Context context;
	private ViewHolder viewHolder;
	private String[] array ;
	private int seleMIndex;
	boolean isGrade;
	public PopSettingsWindowAdapter(Context context,int seleMIndex,String[]array) {
		this.context = context;
		this.seleMIndex = seleMIndex;
		this.array = array;
	}
	public PopSettingsWindowAdapter(Context context,int seleMIndex) {
		this.context = context;
		this.seleMIndex = seleMIndex;
	}

	@Override
	public int getCount() {
		return array.length;
	}

	@Override
	public Object getItem(int position) {
		return array[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		String title = array[position];
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.setings_item, null);
			viewHolder = new ViewHolder();
			viewHolder.text_title = (TextView) convertView
					.findViewById(R.id.liveTvName);
			viewHolder.layout_bg= (LinearLayout) convertView
					.findViewById(R.id.layout_bg);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.text_title.setText(title);
		if(position== seleMIndex){
			viewHolder.layout_bg.setBackgroundResource(R.drawable.tool_box_left_bg_select);
		}else{
			viewHolder.layout_bg.setBackgroundResource(R.drawable.tool_box_left_bg_normal);
		}
		return convertView;
	}

	class ViewHolder {
		TextView text_title;
		LinearLayout layout_bg;
	}

}
