package flytv.compos.run;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import flytv.compos.run.bean.CommSchoolBean;
import flytv.compos.run.bean.SchoolBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.NetUser;
import flytv.ext.utils.RequestVo;
import flytv.ext.utils.UserShareUtils;
import flytv.run.bean.StuBean;
import flytv.run.bean.TVCodeBean;
import flytv.run.parser.MsgPa;

/**
 * 用户认证界面
 * 
 * @author nike
 * 
 */
public class AyRegisterLogin extends BaseActivity {

	@ViewInject(R.id.edit_name)
	private EditText edit_name;

	@ViewInject(R.id.edit_time)
	private Button edit_time;

	@ViewInject(R.id.edit_school)
	private Button edit_school;

	private int mYear;
	private int mMonth;
	private int mDay;

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		ViewUtils.inject(this);
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR) - 6;
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		UserShareUtils.getInstance().clearInfo(context);
		TVCodeBean tvCodeBean = (TVCodeBean) UserShareUtils.getInstance()
				.getLoginInfo(this);
		if (tvCodeBean != null) {
			Intent intent = new Intent(AyRegisterLogin.this, MainActivity.class);
			intent.putExtra("isAutoLogin", true);
			tvCodeBean.setIsAuto(1);
			UserShareUtils.getInstance().setLoginInfo(context, tvCodeBean);
			startActivity(intent);
			finish();
		} else {
			getInfo();
		}
		if (tvCodeBean != null) {
			tvCodeBean.setStbIsInit(1);
			UserShareUtils.getInstance().setLoginInfo(context, tvCodeBean);
		}

	}

	private void updateDisplay() {
		edit_time.setText(new StringBuilder().append(mYear).append("-")
				.append(AppUtil.strYear(mMonth + 1)).append("-")
				.append(AppUtil.strYear(mDay)));
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDisplay();
		}
	};

	ArrayList<SchoolBean> list = new ArrayList<SchoolBean>();

	private int school_index;

	@OnClick({ R.id.btn_login, R.id.edit_time, R.id.edit_school })
	public void test_on(View view) {
		switch (view.getId()) {
		case R.id.btn_login:
			loginInfo();
			break;
		case R.id.edit_time:
			new DatePickerDialog(AyRegisterLogin.this, mDateSetListener, mYear,
					mMonth, mDay).show();
			break;
		case R.id.edit_school:
			if (list.size() > 0) {
				edit_school.setEnabled(false);
				Intent intentSchool = new Intent(AyRegisterLogin.this,
						AyInfoOption.class);
				intentSchool.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				intentSchool.putExtra("typeId", 1);
				intentSchool.putExtra("items", list);
				intentSchool.putExtra("selecId", school_index);
				startActivityForResult(intentSchool, 100);
			} else {
				getInfo();
			}
			break;
		}
	}

	void getInfo() {
		RequestVo vo = new RequestVo();
		vo.isGetUrl = false;
		vo.isFullUrl = false;
		vo.netUser = NetUser.STB;
		String mothodUrl = getString(R.string.flytv_user_login_school_all);
		vo.requesStr = mothodUrl;
		vo.jsonParser = new MsgPa();
		vo.context = context;
		vo.requestDataMap = null;
		getDataFromServer(vo, new DataCallback<String>() {
			@Override
			public void processData(String paramObject, boolean paramBoolean) {
				if (paramObject != null) {
					// 需要信息 到本地
					try {
						CommSchoolBean entity = AppUtil.getPerson(paramObject, CommSchoolBean.class);
						List<SchoolBean> lists = entity.getItems();
						list.addAll(lists);
						if (list.size() > 0) {
							//edit_school.setText(list.get(school_index).getXXMC());
						}
						LogUtils.print(""+entity.getMessage());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	void loginInfo() {
		String name = edit_name.getText().toString().trim();
		String time = edit_time.getText().toString();
		String school = edit_school.getText().toString();
		if(AppUtil.isStrNull(name)){
			AlertTools.showTips(context, R.drawable.tips_warning, "用户名不能为空!");
			return;
		}
		if(AppUtil.isStrNull(time)){
			AlertTools.showTips(context, R.drawable.tips_warning, "生日不能为空!");
			return;
		}
		if(AppUtil.isStrNull(school)){
			AlertTools.showTips(context, R.drawable.tips_warning, "学校不能为空!");
			return;
		}
		showDataDialog();
		String mothodUrl = getString(R.string.app_host) + "/epad/loveLogin?XM="
				+ name + "&CSRQ=" + time + "&XXDM=" + list.get(school_index).getXXDM();
		LogUtils.print("test=" + mothodUrl);
		HttpUtils http = new HttpUtils();
		http.configTimeout(10000);
		http.configCurrentHttpCacheExpiry(2000);
		http.send(HttpRequest.HttpMethod.GET, mothodUrl,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						closeDataDialog();
						try {
							LogUtils.print("responseInfo ="
									+ responseInfo.result);
							closeDataDialog();
							if (AppUtil.isStrNull(responseInfo.result)) {
								AlertTools.showTips(context,
										R.drawable.tips_warning, "服务器连接失败!");
								return;
							}
							LogUtils.print("获取登陆信息=" + responseInfo.result);
							// 需要信息 到本地
							// TVCodeBean tvCodeBean = (TVCodeBean) AppUtil
							// .getJSONBean(responseInfo.result,
							// TVCodeBean.class);

							TVCodeBean tvCodeBean = AppUtil.getPerson(
									responseInfo.result, TVCodeBean.class);
							if (tvCodeBean.getMessage() == 1) {
								if (tvCodeBean.getUserType().equals("2")) {
									List<StuBean> list = new ArrayList<StuBean>();
									StuBean stuBean1 = new StuBean();
									StuBean stuBean2 = new StuBean();
									StuBean stuBean3 = new StuBean();
									StuBean stuBean4 = new StuBean();
									StuBean stuBean5 = new StuBean();
									stuBean1.setName("语文");
									stuBean2.setName("数学");
									stuBean3.setName("英语");
									stuBean4.setName("物理");
									stuBean5.setName("化学");
									list.add(stuBean1);
									list.add(stuBean2);
									list.add(stuBean3);
									list.add(stuBean4);
									list.add(stuBean5);
									tvCodeBean.subjectList = list;
								}
								UserShareUtils.getInstance().setLoginInfo(
										context, tvCodeBean);
								UserShareUtils.getInstance().clearInfo(context);
								// 认证成功 去初始化密码
								Intent intent = new Intent(
										AyRegisterLogin.this, MainActivity.class);
								intent.putExtra("tvCodeBean", tvCodeBean);
								startActivity(intent);
								finish();
							} else {
								AlertTools.showTips(context,
										R.drawable.tips_warning,
										tvCodeBean.getMsgInfo());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						closeDataDialog();
						LogUtils.print("" + msg);
						AppUtil.isNetWork(context);
					}
				});
	}

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.register_info_login);
	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 100:
			edit_school.setEnabled(true);
			if (resultCode == RESULT_OK) {
				try {
					int selecId = data.getIntExtra("selecId", 0);
					school_index = selecId;
					edit_school.setText(list.get(school_index).getXXMC());
				} catch (Exception e) {
					e.printStackTrace();
					AlertTools.showTips(context, R.drawable.tips_warning,
							"选择区县失败！");
				}
			}
			break;
		case 200:

			break;
		}
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

}
