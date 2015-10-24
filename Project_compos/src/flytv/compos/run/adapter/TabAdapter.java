package flytv.compos.run.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import flytv.compos.run.R;

public class TabAdapter extends BaseAdapter {

	private Context context;
	private String[] strArray = null;
	private Integer[] imgArray = null;
	private ViewTab viewTab;

	public TabAdapter(Context context, String[] strArray, Integer[] imgArray) {
		this.context = context;
		this.strArray = strArray;
		this.imgArray = imgArray;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strArray.length;
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
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		if (arg1 == null) {
			viewTab = new ViewTab();
			arg1 = View.inflate(context, R.layout.grid_item, null);
			viewTab.image_icon = (ImageView) arg1.findViewById(R.id.image_icon);
			viewTab.text_title = (TextView) arg1.findViewById(R.id.text_title);
			arg1.setTag(viewTab);
		} else {
			viewTab = (ViewTab) arg1.getTag();
		}
		viewTab.image_icon.setBackgroundResource(imgArray[arg0]);
		viewTab.text_title.setText(strArray[arg0]);
		return arg1;
	}

	class ViewTab {
		ImageView image_icon;
		TextView text_title;
	}

}
