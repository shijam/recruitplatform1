package flytv.compos.run.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;

import flytv.compos.run.R;
import flytv.compos.run.bean.FileBean;
import flytv.compos.run.bean.QuestionBean;
import flytv.ext.utils.AlertHelp;
import flytv.ext.utils.AppUtil;
import flytv.ext.view.MyGridView;
import flytv.ext.view.inter.ItemCommentClickLister;

public class AdComposQuestion extends BaseAdapter {

	private ItemCommentClickLister itemCommentClickLister;
	private Context context;
	private ViewHolderStudy viewHolderStudy;
	private ArrayList<QuestionBean> items;
	private boolean isEdit;
	private BitmapUtils bitmapUtils;

	public void setItemCommentClickLister(
			ItemCommentClickLister itemCommentClickLister) {
		this.itemCommentClickLister = itemCommentClickLister;
	}

	public AdComposQuestion(Context context, ArrayList<QuestionBean> items,
			boolean isEdit) {
		super();
		this.context = context;
		this.items = items;
		this.isEdit = isEdit;
		this.bitmapUtils = new BitmapUtils(context);
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
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	int typeIndex = 0;
	int typePersentIndex = 0;
	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		final QuestionBean enCircle = items.get(arg0);
		if (arg1 == null) {
			viewHolderStudy = new ViewHolderStudy();
			arg1 = View
					.inflate(context, R.layout.layout_cm_question_item, null);
			viewHolderStudy.tx_qu_type_num = (TextView) arg1
					.findViewById(R.id.tx_qu_type_num);
			viewHolderStudy.tx_qu_type_name = (TextView) arg1
					.findViewById(R.id.tx_qu_type_name);
			viewHolderStudy.tx_title = (TextView) arg1
					.findViewById(R.id.tx_qu_name);
			viewHolderStudy.layout_handelr = (LinearLayout) arg1
					.findViewById(R.id.layout_handler);
			viewHolderStudy.layout_question_compos_type = (LinearLayout) arg1
					.findViewById(R.id.layout_question_compos_type);
			viewHolderStudy.myGridView = (MyGridView) arg1
					.findViewById(R.id.gv_image_all);
			viewHolderStudy.tx_edit = (TextView) arg1
					.findViewById(R.id.tx_compos_edit);
			viewHolderStudy.tx_delete = (TextView) arg1
					.findViewById(R.id.tx_compos_dele);
			arg1.setTag(viewHolderStudy);
		} else {
			viewHolderStudy = (ViewHolderStudy) arg1.getTag();
		}
		String title = AppUtil.questionMap.get(enCircle.getQuestionType());

		if(enCircle.isShowType()){
			viewHolderStudy.layout_question_compos_type
			.setVisibility(View.VISIBLE);
		}else{
			viewHolderStudy.layout_question_compos_type
			.setVisibility(View.GONE);
		}
		String typeLeft = AppUtil.getComposType(enCircle.getComposType());
		viewHolderStudy.tx_qu_type_num.setText(typeLeft+ "" + title);
		viewHolderStudy.tx_title.setText((enCircle.getComposTypeSum()+1) + ": "
				+ enCircle.getQuestionTitle());
		if (isEdit) {
			viewHolderStudy.layout_handelr.setVisibility(View.VISIBLE);
		} else {
			viewHolderStudy.layout_handelr.setVisibility(View.INVISIBLE);
		}
		ArrayList<FileBean> files = enCircle.files;
		if (files.size() > 0) {
			AdFriendImage adFriendImage = new AdFriendImage(context,
					bitmapUtils, files);
			viewHolderStudy.myGridView.setAdapter(adFriendImage);
			viewHolderStudy.myGridView.setVisibility(View.VISIBLE);
		} else {
			viewHolderStudy.myGridView.setVisibility(View.GONE);
		}
		//
		viewHolderStudy.tx_delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (itemCommentClickLister != null) {
					itemCommentClickLister.onBottomDeleLog(arg0);
				}
			}
		});
		viewHolderStudy.tx_edit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (itemCommentClickLister != null) {
					itemCommentClickLister.onBottomEditLog(arg0);
				}
			}
		});
		viewHolderStudy.myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AlertHelp.startResource(context, AppUtil
						.getHttpImage(enCircle.files.get(arg2)
								.getFileUrl()));
			}
		});
		return arg1;
	}

	class ViewHolderStudy {
		TextView tx_qu_type_num, tx_qu_type_name, tx_title, tx_edit, tx_delete;
		LinearLayout layout_handelr, layout_question_compos_type,viewHolderStudy;
		MyGridView myGridView;

	}

}
