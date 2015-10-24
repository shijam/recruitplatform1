package flytv.compos.run.control.compos.user;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import flytv.compos.run.R;
import flytv.ext.base.BaseActivity;
import flytv.ext.utils.UserShareUtils;
import flytv.run.bean.TVCodeBean;

public class UserInfoAd extends BaseActivity {
	@ViewInject(R.id.ivTitleName)
	private TextView text_title;
	private Button btn_grade,ivTitleBtnRight;
	private ImageButton  ivTitleBtnLeft;
	

	private TextView text_name,text_option_name,text_school_name,textView_student_year;
	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub

		text_title = (TextView) findViewById(R.id.ivTitleName);
		text_title.setText(getString(R.string.app_user_info));
		text_name = (TextView) findViewById(R.id.textView_name);
		text_option_name = (TextView) findViewById(R.id.textView_class_name);
		text_school_name = (TextView) findViewById(R.id.textView_school);
		
		textView_student_year= (TextView) findViewById(R.id.textView_student_year);
		
		
		TVCodeBean entity =(TVCodeBean) UserShareUtils.getInstance().getLoginInfo(this);
//		text_id.setText(entity.getUserId());
		text_name.setText(entity.getUserName());
		List<flytv.compos.run.bean.ClassBean> classList = entity.bclassList;
		text_option_name.setText(classList.get(0).getSSNJ()+classList.get(0).getBJ());
		text_school_name.setText(entity.getSchoolName());
		textView_student_year.setText(entity.getCSRQ());
		btn_grade = (Button) findViewById(R.id.button_grade);
		btn_grade.setVisibility(View.INVISIBLE);
		ivTitleBtnLeft = (ImageButton) findViewById(R.id.ivTitleBtnLeft);
		ivTitleBtnRight = (Button) findViewById(R.id.ivTitleBtnRight);
		ivTitleBtnLeft.setBackgroundResource(R.drawable.transparent);
		ivTitleBtnLeft.setImageResource(R.drawable.img_back);
		ivTitleBtnRight.setVisibility(View.INVISIBLE);
		ivTitleBtnLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.user_ad_info);
		ViewUtils.inject(this);
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}


}
