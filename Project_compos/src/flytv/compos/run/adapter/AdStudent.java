package flytv.compos.run.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.run.bean.StuBean;

public class AdStudent extends BaseAdapter {

	private ArrayList<StuBean> items;
	private Context context;
	private ViewStudenHolder viewStudenHolder;
	public AdStudent(ArrayList<StuBean> items, Context context) {
		super();
		this.items = items;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null){
			viewStudenHolder = new ViewStudenHolder();
			convertView =  View.inflate(context, R.layout.layout_student_name_item, null);
			viewStudenHolder.text_bg =(TextView) convertView.findViewById(R.id.tx_student_name);
			convertView.setTag(viewStudenHolder);
		}else{
			viewStudenHolder = (ViewStudenHolder)convertView.getTag();
		}
		StuBean entity = items.get(position);
		viewStudenHolder.text_bg.setText(entity.getName());
		return convertView;
	}
	class ViewStudenHolder{
		TextView text_bg;
	}

}
