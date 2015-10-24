package flytv.compos.run.adapter.stu;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.richtest.MainActivity;
import com.lidroid.xutils.BitmapUtils;

import flytv.compos.run.R;
import flytv.compos.run.adapter.AdFriendImage;
import flytv.compos.run.adapter.AdImageAdd;
import flytv.compos.run.bean.AnswerBean;
import flytv.compos.run.bean.FileBean;
import flytv.ext.utils.AlertHelp;
import flytv.ext.utils.AppUtil;
import flytv.ext.view.MyGridView;
import flytv.ext.view.inter.ItemChinLister;

/**
 * 作文列表适配器
 * 
 * @author ShanXin
 * 
 */
public class AdComposChin extends BaseAdapter {

	private ArrayList<AnswerBean> items;
	private Context context;
	private boolean isStudent;
	private boolean isEdit;
	private ItemChinLister itemChinLister;
	private BitmapUtils bitmapUtils;

	public void setItemChinLister(ItemChinLister itemChinLister) {
		this.itemChinLister = itemChinLister;
	}

	HashMap<Integer, String> mapEditMsg = new HashMap<Integer, String>();
	HashMap<Integer, String> mapTitleMsg = new HashMap<Integer, String>();

	public AdComposChin(ArrayList<AnswerBean> items, Context context,
			boolean isStudent, boolean isEdit) {
		super();
		this.items = items;
		this.context = context;
		this.isStudent = isStudent;
		this.isEdit = isEdit;
		this.bitmapUtils = new BitmapUtils(context);
		for (int i = 0; i < this.items.size(); i++) {
			AnswerBean entity = items.get(i);
			if (entity != null) {
				mapTitleMsg.put(i, entity.getTitle());
				if (this.isStudent) {
					mapEditMsg.put(i, entity.getUserAnswer());
				} else {
					mapEditMsg.put(i, entity.getMarkContent());
				}
			}
		}

	}

	@Override
	public int getCount() {
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

	private int index;

	@Override
	public View getView(final int arg0, View arg1, ViewGroup arg2) {
		final AnswerBean enCircle = items.get(arg0);
		ViewHolderStudy viewHolderStudy;
		if (arg1 == null) {
			viewHolderStudy = new ViewHolderStudy();
			arg1 = View.inflate(context, R.layout.pop_layout_compos_info, null);
			viewHolderStudy.layout_student = (LinearLayout) arg1
					.findViewById(R.id.layout_student_answer);
			viewHolderStudy.layout_tearcher = (LinearLayout) arg1
					.findViewById(R.id.layout_tearcher_mark);
			viewHolderStudy.layout_edit = (LinearLayout) arg1
					.findViewById(R.id.layout_edit_name);
			viewHolderStudy.layout_text = (LinearLayout) arg1
					.findViewById(R.id.layout_tx_name);

			viewHolderStudy.layout_not_mark = (LinearLayout) arg1
					.findViewById(R.id.layout_not_mark);

			viewHolderStudy.compos_layout_image_bg = (LinearLayout) arg1
					.findViewById(R.id.compos_layout_image_bg);

			viewHolderStudy.layout_btn = (LinearLayout) arg1
					.findViewById(R.id.layout_btn);

			viewHolderStudy.compos_tx_title_name = AppUtil.getTextView(arg1,
					R.id.compos_tx_title_name);
			viewHolderStudy.compos_tx_content = AppUtil.getTextView(arg1,
					R.id.compos_tx_message_name);
			viewHolderStudy.essay_tx_name = AppUtil.getTextView(arg1,
					R.id.essay_tx_name);
			viewHolderStudy.tx_mark_handler = AppUtil.getTextView(arg1,
					R.id.tx_mark_handler);

			viewHolderStudy.myGridView_tearcher = (MyGridView) arg1
					.findViewById(R.id.gridView_composq);
			viewHolderStudy.gv_image_answer = (MyGridView) arg1
					.findViewById(R.id.gridView_answer);

			viewHolderStudy.ratingBar_score = (RatingBar) arg1
					.findViewById(R.id.ratingBar_score);
			viewHolderStudy.expancet_mark_num = (Button) arg1
					.findViewById(R.id.expancet_mark_num);
			viewHolderStudy.edit_title = (EditText) arg1
					.findViewById(R.id.edit_title);
			viewHolderStudy.edit_content = (EditText) arg1
					.findViewById(R.id.edit_content);
			viewHolderStudy.edit_mark = (EditText) arg1
					.findViewById(R.id.essay_tx_make_name);

			viewHolderStudy.btn_save = (Button) arg1
					.findViewById(R.id.imageView_btn_draft);
			viewHolderStudy.btn_submit = (Button) arg1
					.findViewById(R.id.imageView_btn_submit);

			viewHolderStudy.edit_content.setTag(arg0);
			viewHolderStudy.edit_title.setTag(arg0);
			viewHolderStudy.edit_mark.setTag(arg0);
			viewHolderStudy.edit_mark.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						index = (Integer) v.getTag();
					}
					return false;
				}
			});
			viewHolderStudy.edit_title
					.setOnTouchListener(new OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_UP) {
								index = (Integer) v.getTag();
							}
							return false;
						}
					});
			viewHolderStudy.edit_content
					.setOnTouchListener(new OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							if (event.getAction() == MotionEvent.ACTION_UP) {
								index = (Integer) v.getTag();
							}
							return false;
						}
					});
			class MyTextWatcher implements TextWatcher {
				public MyTextWatcher(ViewHolderStudy holder,int typeIndex) {
					mHolder = holder;
					this.typeIndex = typeIndex;
				}

				private ViewHolderStudy mHolder;
				private int typeIndex;
				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
				}

				@Override
				public void afterTextChanged(Editable s) {
					if (s != null && !"".equals(s.toString())) {
						int position = (Integer) mHolder.edit_title.getTag();
						int contentAnswer = (Integer) mHolder.edit_content.getTag();
						// 当EditText数据发生改变的时候存到data变量中
						if(typeIndex==1){
							mapTitleMsg.put(position, s.toString());
						}else{
							mapEditMsg.put(contentAnswer, s.toString());
						}	
					}
				}
			}
			/**
			 * 多editText 怎么取出 重复数据
			 */
			viewHolderStudy.edit_content
					.addTextChangedListener(new MyTextWatcher(viewHolderStudy,2));
			viewHolderStudy.edit_title
					.addTextChangedListener(new MyTextWatcher(viewHolderStudy,1));
			viewHolderStudy.edit_mark.addTextChangedListener(new MyTextWatcher(
					viewHolderStudy,2));
			arg1.setTag(viewHolderStudy);
		} else {
			viewHolderStudy = (ViewHolderStudy) arg1.getTag();
			viewHolderStudy.edit_title.setTag(arg0);
			viewHolderStudy.edit_content.setTag(arg0);
			viewHolderStudy.edit_mark.setTag(arg0);
		}
		viewHolderStudy.btn_save.setTag(arg0);
		viewHolderStudy.btn_submit.setTag(arg0);
		viewHolderStudy.btn_save.setOnClickListener(onClickListener);
		viewHolderStudy.btn_submit.setOnClickListener(onClickListener);
		viewHolderStudy.expancet_mark_num.setText("第" + (arg0 + 1) + "次提交");
		if (isStudent) {
			int status = enCircle.getStatus();
			if (status != 2) {
				isEdit = true;
			} else {
				isEdit = false;
			}
			viewHolderStudy.ratingBar_score.setIsIndicator(true);
			viewHolderStudy.tx_mark_handler.setText("查看批阅");
			// 老师批阅才显示 批阅信息
			viewHolderStudy.edit_title.setText(enCircle.getTitle());
			viewHolderStudy.edit_content.setText(enCircle.getUserAnswer());
			if (isEdit) {
				String title = mapTitleMsg.get(arg0);
				String content = mapEditMsg.get(arg0);
				viewHolderStudy.edit_title.setText(title);
				viewHolderStudy.edit_content.setText(content);
				viewHolderStudy.layout_student.setVisibility(View.VISIBLE);
				viewHolderStudy.layout_tearcher.setVisibility(View.GONE);
				//
				ArrayList<FileBean> fileItems = enCircle.answerFiles;
				if(!enCircle.isAddFire()){
					FileBean fileBean = new FileBean();
					fileBean.setImg_typeId(1);
					if(enCircle.getStatus()!=2){
						fileItems.add(fileBean);
						enCircle.setAddFire(true);
					}
				}
				AdImageAdd admImageAdd = new AdImageAdd(context, fileItems);
				viewHolderStudy.gv_image_answer.setAdapter(admImageAdd);
			} else {
			
				viewHolderStudy.layout_student.setVisibility(View.GONE);
				viewHolderStudy.layout_tearcher.setVisibility(View.VISIBLE);
				initTearcher(enCircle, viewHolderStudy);
				if (enCircle.answerFiles.size() > 0) {
					viewHolderStudy.compos_layout_image_bg
							.setVisibility(View.VISIBLE);
					viewHolderStudy.myGridView_tearcher
							.setAdapter(new AdFriendImage(context, bitmapUtils,
									enCircle.answerFiles));
				} else {
					viewHolderStudy.compos_layout_image_bg
							.setVisibility(View.GONE);
				}
				/**
				 * 是否显示 老师批阅
				 */
				flytv.ext.utils.LogUtils.print("enCircle.getMarkStatus()=="
						+ enCircle.getMarkStatus());
				if (enCircle.getMarkStatus() == 1) {
					viewHolderStudy.layout_not_mark.setVisibility(View.GONE);
				} else {
					viewHolderStudy.layout_not_mark.setVisibility(View.VISIBLE);
				}
			}
		} else {
			viewHolderStudy.layout_student.setVisibility(View.GONE);
			viewHolderStudy.layout_tearcher.setVisibility(View.VISIBLE);
			int status = enCircle.getMarkStatus();
			if (status != 2) {
				isEdit = true;
			} else {
				isEdit = false;
			}
			initTearcher(enCircle, viewHolderStudy);
			
			//
			viewHolderStudy.ratingBar_score.setIsIndicator(true);
			if(isEdit){
				viewHolderStudy.ratingBar_score.setIsIndicator(false);
			}else{
				viewHolderStudy.ratingBar_score.setIsIndicator(true);
			}
			if (enCircle.answerFiles.size() > 0) {
				viewHolderStudy.compos_layout_image_bg
						.setVisibility(View.VISIBLE);
				viewHolderStudy.myGridView_tearcher
						.setAdapter(new AdFriendImage(context, bitmapUtils,
								enCircle.answerFiles));
			} else {
				viewHolderStudy.compos_layout_image_bg
						.setVisibility(View.GONE);
			}
			//
			

		}
		viewHolderStudy.edit_content.clearFocus();
		viewHolderStudy.edit_mark.clearFocus();
		viewHolderStudy.edit_title.clearFocus();
		if (index != -1 && index == arg0) {
			viewHolderStudy.edit_title.requestFocus();
		}
		viewHolderStudy.myGridView_tearcher.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				AlertHelp.startResource(context, AppUtil
						.getHttpImage(enCircle.answerFiles.get(arg2)
								.getFileUrl()));
			}
		});
		viewHolderStudy.gv_image_answer.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> view, View arg1, final int arg2,
					long arg3) {
				if(onClickListener!=null){
					itemChinLister.executeImage(arg0, arg2);
				}
			}
		});
		viewHolderStudy.tx_mark_handler.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,MainActivity.class);
				intent.putExtra("markNotation", items.get(arg0));
				context.startActivity(intent);
			}
		});
		return arg1;

	}

	void initTearcher(AnswerBean enCircle, ViewHolderStudy viewHolderStudy) {
		// 老师
		if (isEdit) {
			viewHolderStudy.layout_text.setVisibility(View.GONE);
			viewHolderStudy.layout_edit.setVisibility(View.VISIBLE);
			viewHolderStudy.edit_mark.setText(enCircle.getMarkContent());
			viewHolderStudy.tx_mark_handler.setText("我要批阅");
			viewHolderStudy.layout_btn.setVisibility(View.VISIBLE);
		} else {
			viewHolderStudy.layout_text.setVisibility(View.VISIBLE);
			viewHolderStudy.layout_edit.setVisibility(View.GONE);
			viewHolderStudy.tx_mark_handler.setText("查看批阅");
			viewHolderStudy.essay_tx_name.setText(enCircle.getMarkContent());
			viewHolderStudy.layout_btn.setVisibility(View.GONE);
		}
		float score = 0;
		try {
			if (AppUtil.isDrc(enCircle.getUserScore())) {
				score = Float.parseFloat(enCircle.getUserScore());
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		viewHolderStudy.ratingBar_score.setRating(score);
		viewHolderStudy.compos_tx_title_name.setText(enCircle.getTitle());
		viewHolderStudy.compos_tx_content.setText(enCircle.getUserAnswer());
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (itemChinLister != null) {
				int cmdIndex = 0;
				if (v.getId() == R.id.imageView_btn_draft) {
					cmdIndex = 1;
				} else {
					cmdIndex = 2;
				}
				int position =Integer.parseInt(v.getTag().toString());
				itemChinLister.execute(cmdIndex,position);
			}
		}
	};

	class ViewHolderStudy {
		LinearLayout layout_student, layout_tearcher, layout_edit, layout_text,
				compos_layout_image_bg, layout_btn, layout_not_mark;
		// 学生图片显示 后者是添加
		MyGridView myGridView_tearcher, gv_image_answer;
		// 学生 老师 数据布局
		TextView compos_tx_title_name, compos_tx_content, tx_mark_handler,
				essay_tx_name;
		RatingBar ratingBar_score;
		Button expancet_mark_num, btn_save, btn_submit;
		EditText edit_content, edit_title, edit_mark;
	}

}
