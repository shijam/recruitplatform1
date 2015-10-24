package flytv.compos.run.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import flytv.compos.run.R;
import flytv.compos.run.bean.CircleComment;
import flytv.ext.utils.AppUtil;

public class AdFriendComment extends BaseAdapter {

	
	public Context context;
	private ArrayList<CircleComment> items;
	private ViewHolderComment viewHolderComment;
	public AdFriendComment(Context context, ArrayList<CircleComment> items) {
		super();
		this.context = context;
		this.items = items;
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
		CircleComment entity = items.get(position);
		if(convertView==null){
			viewHolderComment = new ViewHolderComment();
			convertView = View.inflate(context, R.layout.layout_comment_item, null);
			viewHolderComment.tx_send_name = (TextView)convertView.findViewById(R.id.tx_send_name);
			viewHolderComment.tx_reply_name = (TextView)convertView.findViewById(R.id.tx_reply_name);
			viewHolderComment.tx_reply_lint = (TextView)convertView.findViewById(R.id.tx_reply_lint);
			viewHolderComment.tx_send_time = (TextView)convertView.findViewById(R.id.tx_send_time);
			viewHolderComment.tx_content = (TextView)convertView.findViewById(R.id.tx_send_content);
			convertView.setTag(viewHolderComment);
		}else{
			viewHolderComment = (ViewHolderComment)convertView.getTag();
		}
		if(AppUtil.isStrNull(entity.getReplyUserId())){
			viewHolderComment.tx_reply_name.setVisibility(View.GONE);
			viewHolderComment.tx_reply_lint.setVisibility(View.GONE);
		}else{
			viewHolderComment.tx_reply_name.setVisibility(View.VISIBLE);
			viewHolderComment.tx_reply_lint.setVisibility(View.VISIBLE);
			viewHolderComment.tx_reply_name.setText(entity.getReplyUserName());
		}
		
		viewHolderComment.tx_send_name.setText(entity.getUserName());
		viewHolderComment.tx_send_time.setText(entity.getCreateTimeStr());
		viewHolderComment.tx_content.setText(entity.getContent());
		return convertView;
	}
	class ViewHolderComment{
		TextView tx_send_time,tx_content,tx_reply_lint, tx_send_name,tx_reply_name;
		
	}

}
