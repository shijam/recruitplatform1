package flytv.compos.run.control.compos;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import flytv.compos.run.R;
import flytv.compos.run.adapter.AdComposQuestion;
import flytv.compos.run.bean.ClassBean;
import flytv.compos.run.bean.CommonMsg;
import flytv.compos.run.bean.ComposBean;
import flytv.compos.run.bean.QuestionBean;
import flytv.ext.base.BaseActivity;
import flytv.ext.tools.AlertTools;
import flytv.ext.utils.AppUtil;
import flytv.ext.utils.GeneralView;
import flytv.ext.utils.LogUtils;
import flytv.ext.utils.UserShareUtils;
import flytv.ext.view.inter.ItemCommentClickLister;
import flytv.ext.view.inter.PopWindowItemClickLister;
import flytv.run.bean.MsgBean;
import flytv.run.bean.StuBean;
import flytv.run.bean.TVCodeBean;

public class AyComposAdd extends BaseActivity implements ItemCommentClickLister {

	private boolean isInit = false;
	private boolean isAddEdit = false;
	private boolean isInitAdd = false;
	@ViewInject(R.id.ivTitleBtnLeft)
	private ImageButton ivTitleBtnLeft;

	@ViewInject(R.id.ivTitleBtnRight)
	private Button ivTitleBtnRight;

	@ViewInject(R.id.button_grade)
	private Button button_grade;

	@ViewInject(R.id.ivTitleName)
	private TextView ivTitleName;
	@ViewInject(R.id.tx_right)
	private TextView tx_right;

	@ViewInject(R.id.ed_title)
	private EditText ed_title;

	@ViewInject(R.id.ed_class)
	private Button ed_class;

	@ViewInject(R.id.ed_subject)
	private Button ed_subject;

	@ViewInject(R.id.listView_pop)
	private ListView listView_pop;

	private ComposBean composItem;
	private TVCodeBean tvCodeBean;

	private PopupWindow popupWindow;
	boolean isClass = true;

	@OnClick({ R.id.ivTitleBtnLeft, R.id.btn_sercher, R.id.tx_right,
			R.id.btn_compos_add, R.id.ed_class, R.id.ed_subject })
	public void onClickComment(View view) {
		switch (view.getId()) {
		// 添加评论
		case R.id.btn_sercher:
			break;
		case R.id.ivTitleBtnLeft:
			finish();
			break;
		case R.id.ed_class:
			isClass = true;
			popupWindow = GeneralView.makePopupWindow(this, classIndex,
					classArray, popWindowItemClickLister);
			int[] xy = new int[2];
			view.getLocationOnScreen(xy);
			popupWindow.showAtLocation(ed_class, Gravity.CENTER | Gravity.TOP,
					xy[0] / 2, xy[1] + ed_class.getHeight() + 5);
			break;
		case R.id.ed_subject:
			isClass = false;
			popupWindow = GeneralView.makePopupWindow(this, subjectIndex,
					subjectArray, popWindowItemClickLister);
			int[] xyS = new int[2];
			view.getLocationOnScreen(xyS);
			popupWindow.showAtLocation(ed_subject,
					Gravity.CENTER | Gravity.TOP, xyS[0] / 2, xyS[1]
							+ ed_subject.getHeight() + 5);
			break;
		case R.id.tx_right:
			// 提交
			sendCompos();
			break;
		case R.id.btn_compos_add:
			if (isInit && !isAddEdit) {
				sendCompos();
			} else {
				if(subjectArray[subjectIndex].equals("语文")){
					if(items.size()>0){
						AlertTools.showTips(context, R.drawable.tips_warning, "作文只能发布一道题！");
						return;
					}
				}
				Intent intent = new Intent(AyComposAdd.this,
						AyComposQuestion.class);
				intent.putExtra("subjectName", subjectList.get(subjectIndex)
						.getName());
				isInitAdd = false;

				intent.putExtra("isEdit", isInitAdd);
				if (isAddEdit && isInit) {
					intent.putExtra("isInit", true);
					intent.putExtra("initComposEntity", tvCodeBean);
				} else {
					intent.putExtra("composItem", composItem);
					intent.putExtra("isInit", false);
				}

				startActivityForResult(intent, 100);
			}
			break;
		}
	}

	private PopWindowItemClickLister popWindowItemClickLister = new PopWindowItemClickLister() {

		@Override
		public void onItemClick(int i) {
			popupWindow.dismiss();
			if (isClass) {
				if (classIndex != i) {
					classIndex = i;
					ed_class.setText(classArray[i]);
				}
			} else {
				if (subjectIndex != i) {
					subjectIndex = i;
					ed_subject.setText(subjectArray[i]);
				}
			}
		}
	};

	private TVCodeBean loginBean;

	@Override
	protected void findViewById() {
		// TODO Auto-generated method stub
		ed_class.setText(classList.get(classIndex).getBJ());
		ed_subject.setText(subjectList.get(subjectIndex).getName());
	}

	List<ClassBean> classList = new ArrayList<ClassBean>();
	List<StuBean> subjectList = new ArrayList<StuBean>();
	ArrayList<QuestionBean> items = new ArrayList<QuestionBean>();
	private int subjectIndex, classIndex;
	private String[] subjectArray, classArray;
	private AdComposQuestion adComposQuestion;

	@Override
	protected void loadViewLayout() {
		// TODO Auto-generated method stub
		setContentView(R.layout.ad_compos_add);
		ViewUtils.inject(this);
		ivTitleName.setText(getString(R.string.app_tab_compos));
		button_grade.setVisibility(View.INVISIBLE);
		tx_right.setVisibility(View.VISIBLE);
		ivTitleBtnRight.setVisibility(View.GONE);
		loginBean = (TVCodeBean) UserShareUtils.getInstance().getLoginInfo(
				context);
		isInit = getIntent().getBooleanExtra("isInit", true);
		composItem = (ComposBean) getIntent()
				.getSerializableExtra("composItem");
		classList = loginBean.bclassList;
		subjectList = loginBean.subjectList;
		classArray = new String[classList.size()];
		subjectArray = new String[subjectList.size()];
		for (int i = 0; i < classList.size(); i++) {
			classArray[i] = classList.get(i).getBJ();
		}
		for (int i = 0; i < subjectList.size(); i++) {
			subjectArray[i] = subjectList.get(i).getName();
		}
		adComposQuestion = new AdComposQuestion(AyComposAdd.this, items, true);
		adComposQuestion.setItemCommentClickLister(this);
		listView_pop.setDivider(null);
		listView_pop.setDividerHeight(10);
		listView_pop.setAdapter(adComposQuestion);

	}

	@Override
	protected void processLogic() {
		// TODO Auto-generated method stub
		if (!isInit) {
			// 查看题目列表
			showQuestion();
			isAddEdit = true;
			ed_title.setText(composItem.getName());
		} else {

		}
	}

	@Override
	protected void setListener() {
		// TODO Auto-generated method stub

	}

	void showQuestion() {
		showDataDialog();
		int homeworkId = 0;
		// 是否是编辑
		if (isAddEdit && isInit) {
			homeworkId = tvCodeBean.getHomeworkId();
		} else {
			homeworkId = composItem.getId();
		}
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(R.string.compos_tab_all_answer_loadQuestion)
				+ "?homeworkId=" + homeworkId + "&userId="
				+ loginBean.getUserId();
		LogUtils.print(1, "urlString" + urlString);
		HttpUtils http = new HttpUtils();
		http.configTimeout(15000);
		http.configCurrentHttpCacheExpiry(2000);
		http.send(HttpRequest.HttpMethod.GET, urlString,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						closeDataDialog();
						try {
							LogUtils.print("" + responseInfo.result);

							CommonMsg msgBean = JSON.parseObject(
									responseInfo.result, CommonMsg.class);
							if (msgBean != null) {
								items.clear();
								items.addAll(msgBean.items);
								init();
								adComposQuestion.notifyDataSetChanged();
								// admin
							} else {
								AlertTools.showTips(context,
										R.drawable.tips_error, "获取失败!");
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

	void sendCompos() {
		if(!isInit && !isAddEdit&& items.size()==0){
			AlertTools.showTips(context, R.drawable.tips_warning, "试题不能为空 ！请添加试题");
			return;
		}
		//
		String msgConText = ed_title.getText().toString().trim();
		msgConText =msgConText.replaceAll(" ", "");
		int submitStatus = 0;
		int id = 0;
		// 第一次 第二次编辑
		if (isInit && !isAddEdit) {
			submitStatus = 1;
			id = 0;
		} else {
			submitStatus = 2;
			if (isAddEdit && isInit) {
				id = tvCodeBean.getHomeworkId();
			} else {
				id = composItem.getId();
			}
		}
		if (AppUtil.isStrNull(msgConText)) {
			AlertTools.showTips(context, R.drawable.tips_warning, "请输入试题的题目！");
			return;
		}
		if (classList.size() < 1) {
			AlertTools.showTips(context, R.drawable.tips_warning, "请选择班级！");
		}
		if (subjectList.size() < 1) {
			AlertTools.showTips(context, R.drawable.tips_warning, "请选择学科！");
		}
		String mothodUrl = getString(R.string.app_host)
				+ getString(R.string.compos_tab_answer_addwork) + "?userId="
				+ loginBean.getUserId() + "&subjectName="
				+ subjectList.get(subjectIndex).getName() + "&bclassId="
				+ classList.get(classIndex).getBJID() + "&name=" + msgConText
				+ "&submitStatus=" + submitStatus + "&id=" + id + "&type=1";

		showDataDialog();
		LogUtils.print("test=" + mothodUrl);
		HttpUtils http = new HttpUtils();
		http.configTimeout(60000);
		http.configCurrentHttpCacheExpiry(2000);
		http.send(HttpRequest.HttpMethod.GET, mothodUrl,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {

					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						try {
							closeDataDialog();
							LogUtils.print("获取信息=" + responseInfo.result);
							if (AppUtil.isStrNull(responseInfo.result)) {
								AlertTools.showTips(context,
										R.drawable.tips_warning, "服务器连接失败!");
								return;
							}
							tvCodeBean = AppUtil.getPerson(responseInfo.result,
									TVCodeBean.class);
							if (tvCodeBean.getMessage() == 1) {
								// 添加成功后 返回当前实体
								if (!isAddEdit) {
									Intent intent = new Intent(
											AyComposAdd.this,
											AyComposQuestion.class);
									intent.putExtra("initComposEntity",
											tvCodeBean);
									intent.putExtra("isInit", isInit);
									intent.putExtra("isEdit", isInitAdd);
									intent.putExtra("subjectName", subjectList
											.get(subjectIndex).getName());
									startActivityForResult(intent, 100);
									isAddEdit = true;
								} else {
									Intent intent = new Intent();
									setResult(RESULT_OK, intent);
									finish();
								}

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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case 100:
			if (resultCode == RESULT_OK) {
				showQuestion();
			}
			break;
		}
	}

	@Override
	public void onBottomComment(int position, int commendId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBottomPraise(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBottomDeleLog(final int position) {
		// TODO Auto-generated method stub
		final QuestionBean circle = items.get(position);
		showDataDialog();
		String urlString = AppUtil.getStringId(context) + "/"
				+ getString(R.string.compos_tab_item_dele_question) + "?id="
				+ circle.getId();
		// + "&week=" + seleTypeIndex;
		LogUtils.print(1, "urlString" + urlString);
		HttpUtils http = new HttpUtils();
		http.configTimeout(10000);
		http.configCurrentHttpCacheExpiry(2000);
		http.send(HttpRequest.HttpMethod.GET, urlString,
				new RequestCallBack<String>() {
					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
					}

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						closeDataDialog();
						try {
							LogUtils.print("" + responseInfo.result);
							MsgBean msgBean = JSON.parseObject(
									responseInfo.result, MsgBean.class);
							if (msgBean.getMessage().equals("1")) {
								items.remove(position);
								adComposQuestion.notifyDataSetChanged();
							} else {
								AlertTools.showTips(context,
										R.drawable.tips_error,
										msgBean.getMsgInfo());
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
	public void onBottomEditLog(int position) {
		// TODO Auto-generated method stub
		isInitAdd = true;
		QuestionBean entity = items.get(position);
		Intent intent = new Intent(AyComposAdd.this, AyComposQuestion.class);
		intent.putExtra("Questionentity", entity);
		intent.putExtra("isEdit", isInitAdd);
		if (isAddEdit && isInit) {
			intent.putExtra("isInit", true);
			intent.putExtra("initComposEntity", tvCodeBean);
		}else{
			intent.putExtra("isInit", false);
			intent.putExtra("composItem", composItem);
		}
		intent.putExtra("subjectName", subjectList.get(subjectIndex).getName());
		
		startActivityForResult(intent, 100);
	}

	@Override
	public void onBottomEdit(String message, String logId, int index,
			int commendId) {
		// TODO Auto-generated method stub

	}

	void init() {
		int typePersentIndex = 0, typeIndex = 0;
		for (int i = 0; i < this.items.size(); i++) {
			QuestionBean entity = items.get(i);
			boolean isShow = false;
			if (i == 0) {
				typePersentIndex = 0;
				isShow = true;
			} else {
				//
				if (entity.getQuestionType() != items.get(i - 1)
						.getQuestionType()) {
					typeIndex++;
					isShow = true;
					typePersentIndex = 0;
				} else {
					// typeIndex++;
					isShow = false;
					typePersentIndex++;
					//
				}
			}
			entity.setShowType(isShow);
			entity.setComposType(typeIndex);
			entity.setComposTypeSum(typePersentIndex);
		}
	}

}
