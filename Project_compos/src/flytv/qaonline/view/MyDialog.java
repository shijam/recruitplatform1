package flytv.qaonline.view;

import java.util.ArrayList;
import java.util.List;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import flytv.compos.run.R;
import flytv.qaonline.model.TextItemAdapter;
import flytv.qaonline.model.TextItemAdapter.ListItemClick;
import flytv.qaonline.utils.DensityConfig;

public class MyDialog {
	public interface MyDialogItemClickListener{
		public void clickItemIndex(int index);
	}
	private View parentView;
	private PopupWindow mPopupWindow;
	private List<String> mListData;
	private TextItemAdapter mTextItemAdapter;
	private MyDialogItemClickListener mMyDialogItemClickListener;
	
	public MyDialog(View parentView) {
		super();
		this.parentView = parentView;
		initMyDialog();
	}
	
	private void initMyDialog(){
		mListData =  new ArrayList<String>();
		View popView = View.inflate(parentView.getContext(), R.layout.view_popunwindow_pro, null);
		popView.setFocusable(true);
		popView.setFocusableInTouchMode(false);
		ListView listView = (ListView) popView
				.findViewById(R.id.popupwindow_login_listview);
		mTextItemAdapter = new TextItemAdapter(parentView.getContext(), mListData,
				new ListItemClick() {
					@Override
					public void onItemClickIndex(int index) {
						if(mMyDialogItemClickListener != null)
							mMyDialogItemClickListener.clickItemIndex(index);
						mPopupWindow.dismiss();
					}
				});
		listView.setAdapter(mTextItemAdapter);
		mPopupWindow = new PopupWindow(popView, DensityConfig.getDensityConfig(parentView.getContext()).getScreenWidth()*2/3,LayoutParams.WRAP_CONTENT);
	}
	
	public void showDialog(List<String> listData,MyDialogItemClickListener myDialogItemClickListener){
		if(!mListData.isEmpty())
			mListData.clear();
		mListData.addAll(listData);
		mTextItemAdapter.notifyDataSetChanged();
		this.mMyDialogItemClickListener = myDialogItemClickListener;
		mPopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}
	
	public void showDialog(String[] listData,MyDialogItemClickListener myDialogItemClickListener){
		if(!mListData.isEmpty())
			mListData.clear();
		for(int i = 0;i < listData.length;i++){
			mListData.add(listData[i]);
		}
		mTextItemAdapter.notifyDataSetChanged();
		this.mMyDialogItemClickListener = myDialogItemClickListener;
		mPopupWindow.showAtLocation(parentView, Gravity.CENTER, 0, 0);
	}
	
	
}
