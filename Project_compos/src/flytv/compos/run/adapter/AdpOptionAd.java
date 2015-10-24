package flytv.compos.run.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.compos.run.bean.SchoolBean;

public class AdpOptionAd extends BaseAdapter {

	Context context;
	ArrayList<SchoolBean> list;
	private int index;

	public AdpOptionAd(Context context, ArrayList<SchoolBean> list) {
		this.context = context;
		this.list = list;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
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
		// TODO Auto-generated method stub
		SchoolBean enBean = list.get(arg0);
		TextItem textItem;
		if (arg1 == null) {
			arg1 = View.inflate(context, R.layout.option_item, null);
			textItem = new TextItem();
			textItem.text_item = (TextView) arg1.findViewById(R.id.tx_name);
			textItem.image_array = (ImageView) arg1.findViewById(R.id.im_array);
			arg1.setTag(textItem);
		} else {
			textItem = (TextItem) arg1.getTag();
		}
		textItem.text_item.setText(enBean.getXXMC());
		if(index==arg0){
			textItem.image_array.setVisibility(View.VISIBLE);
		}else{
			textItem.image_array.setVisibility(View.GONE);
		}
		return arg1;
	}

	class TextItem {
		TextView text_item;
		ImageView image_array;

	}

}
