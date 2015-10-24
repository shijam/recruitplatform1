package flytv.ext.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import flytv.compos.run.R;
import flytv.ext.view.inter.PopWindowItemClickLister;
import flytv.sound.control.adapter.PopSettingsWindowAdapter;

public class GeneralView {
	private static List<String> array = new ArrayList<String>();
	static {
		array.add("切换用户");
		array.add("修改密码");
		array.add("帮助");

	}
	private static PopSettingsWindowAdapter popWindowAdapter;
	private static int seleMIndex = 0;

	public static PopupWindow makePopupWindow(Context cx, int seleIndex,String [] array_str,
			final PopWindowItemClickLister popWindowItemClickLister) {
		PopupWindow window;
		seleMIndex = seleIndex;
		window = new PopupWindow(cx);
		View view = View.inflate(cx, R.layout.popview, null);
		ListView listView = (ListView) view.findViewById(R.id.listView1);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				popWindowItemClickLister.onItemClick(arg2);
			}
		});
		popWindowAdapter = new PopSettingsWindowAdapter(cx, seleMIndex,array_str);
		listView.setAdapter(popWindowAdapter);
		window.setAnimationStyle(R.style.AnimationFace);
		window.setContentView(view);

		/*window.setBackgroundDrawable(cx.getResources().getDrawable(
				R.drawable.title_function_bg));*/
		AlertHelp.getDisplay(cx);
		window.setWidth((AlertHelp.width / 2)-50);
		window.setHeight(LayoutParams.WRAP_CONTENT);

		// 设置PopupWindow外部区域是否可触摸
		window.setFocusable(true); // 设置PopupWindow可获得焦点
		window.setTouchable(true); // 设置PopupWindow可触摸
		window.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		return window;
	}
}
