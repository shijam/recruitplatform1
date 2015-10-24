package com.example.richtest;

import org.xml.sax.XMLReader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.Html.TagHandler;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

@SuppressLint("DefaultLocale")
public class MxgsaTagHandler implements TagHandler {
	private int sIndex = 0;
	private int eIndex = 0;
	private final Context mContext;
//	private MainActivity main;

	public MxgsaTagHandler(Context context) {
		this.mContext = context;
//		this.main = main;
	}


	@Override
	public void handleTag(boolean opening, String tag, Editable output,
			XMLReader xmlReader) {
		// TODO Auto-generated method stub
		
		
		if (tag.toLowerCase().equals("comment")) {
			Log.e("taghandler", "taghandler");
			if (opening) {
				sIndex = output.length();
			} else {
				eIndex = output.length();
				output.setSpan(new MxgsaSpan(sIndex, eIndex), sIndex, eIndex,
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
	}

	private class MxgsaSpan extends ClickableSpan implements OnClickListener {
		int start, end;
		public MxgsaSpan(int start, int end){
			this.start = start;
			this.end = end;
//			Log.e("start", String.valueOf(start));
//			Log.e("end",  String.valueOf(end));
		}
		@Override
		public void onClick(View widget) {
			// TODO Auto-generated method stub
			// ������룬��������תҳ�棬�����ǵ����Ի�����������תҳ��
//			mContext.startActivity(new Intent(mContext, MainActivity.class));
			
//			main.showPopupWindow(widget,start,end);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
