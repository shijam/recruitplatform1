package flytv.compos.run.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.compos.run.bean.StuComposBean;
import flytv.ext.utils.AppUtil;

public class AdStuCompos extends BaseAdapter {

	private ArrayList<StuComposBean> items;
	private Context context;
	private VieStuComposHolder viewStudenHolder;
	

	public AdStuCompos(ArrayList<StuComposBean> items, Context context) {
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
	public Object getItem(int position) {
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
		// TODO Auto-generated method stub
		if(convertView==null){
			viewStudenHolder  = new VieStuComposHolder();
			convertView = View.inflate(context, R.layout.layout_stu_compos_item, null);
			viewStudenHolder.tx_student_name =AppUtil.getTextView(convertView, R.id.tx_student_name);
			viewStudenHolder.tx_send_time =AppUtil.getTextView(convertView, R.id.tx_student_time);
			viewStudenHolder.txt_title =AppUtil.getTextView(convertView, R.id.tx_compos_content);
			viewStudenHolder.tx_mark_status =AppUtil.getTextView(convertView, R.id.tx_compos_dele);
			convertView.setTag(viewStudenHolder);
		}else{
			viewStudenHolder = (VieStuComposHolder)convertView.getTag();
		}
		StuComposBean entity = items.get(position);
		viewStudenHolder.tx_student_name.setText(entity.getStudentName());
		viewStudenHolder.txt_title.setText(entity.getHomeworkName());
		viewStudenHolder.tx_send_time.setText(context.getString(R.string.main_text_compos_mark_time)+": "+entity.getUpdateTimeStr());
		String type = AppUtil.getType(context, entity.getStatus());
		viewStudenHolder.tx_mark_status.setText(type);
		return convertView;
	}
	class VieStuComposHolder{
		
		TextView tx_student_name,txt_title,tx_send_time,tx_mark_status;
	}
	

}
