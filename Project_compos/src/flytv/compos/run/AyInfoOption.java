package flytv.compos.run;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import flytv.compos.run.adapter.AdpOptionAd;
import flytv.compos.run.bean.SchoolBean;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AppUtil;

/**
 * 区县信息搜索界面
 * 
 * @author nike
 * 
 */
public class AyInfoOption extends Activity implements OnClickListener{
	@ViewInject(R.id.tv_stb_left)
	private TextView text_cancel;
	@ViewInject(R.id.note_title)
	private TextView note_title;
	@ViewInject(R.id.tv_stb_right)
	private TextView text_confie;

	private int typeId;
	@ViewInject(R.id.list_item)
	private ListView listView;

	private ArrayList<SchoolBean> list;
	private AdpOptionAd adpOptionAd;
	private int selecId;

	private LinearLayout layout_sercher;
	private EditText edit_sercher;
	private ImageView img_sercher;

	private ArrayList<SchoolBean> listSercher;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_info);
		ViewUtils.inject(this);

		layout_sercher = (LinearLayout) findViewById(R.id.layout_sercher);
		edit_sercher = (EditText)findViewById(R.id.pop_info_edit);
		img_sercher = (ImageView) findViewById(R.id.btn_sercher);
		img_sercher.setOnClickListener(this);
		typeId = getIntent().getIntExtra("typeId", 1);
		selecId = getIntent().getIntExtra("selecId", 0);
		list = (ArrayList<SchoolBean>) getIntent()
				.getSerializableExtra("items");
		text_cancel.setText("取消");
		String msg = null;
		if (typeId == 0) {
			msg = "区县";
			layout_sercher.setVisibility(View.GONE);
		} else {
			msg = "学校";
			layout_sercher.setVisibility(View.VISIBLE);
		}
		note_title.setText(msg);
		text_confie.setText("确定");
		adpOptionAd = new AdpOptionAd(AyInfoOption.this, list);
		adpOptionAd.setIndex(selecId);
		listView.setAdapter(adpOptionAd);
		listView.setSelection(selecId);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				selecId = arg2;
				adpOptionAd.setIndex(selecId);
				adpOptionAd.notifyDataSetChanged();
			}
		});

	}

	@OnClick({ R.id.tv_stb_left, R.id.tv_stb_right })
	public void on_text(View view) {
		switch (view.getId()) {
		case R.id.tv_stb_left:
			finish();
			break;
		case R.id.tv_stb_right:
			Intent intent = new Intent();
			if(listSercher==null){
				intent.putExtra("selecId", selecId);
			}else{
				if(listSercher.size()>=1){
					SchoolBean tvcode= listSercher.get(selecId);
					for (int i = 0; i < list.size(); i++) {
						SchoolBean entity= list.get(i);
						if(entity.getXXMC().equals(tvcode.getXXMC())){
							intent.putExtra("selecId", i);
							break;
						}
					}
				}else{
					AlertTools.showTips(AyInfoOption.this, R.drawable.tips_warning, "没有选中学校");
					return;
				}
				
			}
			intent.putExtra("typeId", typeId);
			setResult(RESULT_OK, intent);
			finish();
			break;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.btn_sercher:
			String editSercher = edit_sercher.getText().toString().trim();
			if(AppUtil.isStrNull(editSercher)){
				listSercher = list;
			}else{
				editSercher = AppUtil.stringFilter(editSercher);
				if(AppUtil.isStrNull(editSercher)){
					AlertTools.showTips(AyInfoOption.this, R.drawable.tips_warning, "不能有特殊字符！");
					return;
				}
			}
			// 解决多次搜索 数组越界
			selecId = 0;
			AlertTools.showTips(AyInfoOption.this, R.drawable.tips_warning, "搜索中...");
			listSercher = new ArrayList<SchoolBean>();
			for (int i = 0; i < list.size(); i++) {
				SchoolBean  entity =  list.get(i);
				if(entity.getXXMC().contains(editSercher)){
					listSercher.add(entity);
				}
			}
			if(listSercher.size()<=0){
				AlertTools.colseTips();
				AlertTools.showTips(AyInfoOption.this, R.drawable.tips_warning, "没有搜索到你要找的学校");
			}
			adpOptionAd = new AdpOptionAd(AyInfoOption.this, listSercher);
			listView.setAdapter(adpOptionAd);
			break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		AlertTools.colseTips();
	}

}
