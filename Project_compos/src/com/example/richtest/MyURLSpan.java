package com.example.richtest;

import android.text.TextPaint;
import android.text.style.URLSpan;
import android.view.View;

public class MyURLSpan extends URLSpan {

	public MyURLSpan(String url) {
		super(url);
		// TODO Auto-generated constructor stub
	}

	@Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(ds.linkColor);
//        ds.setColor(Color.BLUE);
        ds.setUnderlineText(true);
    }
	
	@Override
    public void onClick(View widget) {

    }
}
