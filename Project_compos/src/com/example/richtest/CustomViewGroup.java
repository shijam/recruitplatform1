package com.example.richtest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class CustomViewGroup extends FrameLayout {
	private static Matrix matrix = new Matrix();
	private static CustomViewGroup instance;

	public CustomViewGroup(Context context) {
		super(context);
		instance = this;
	}

	public CustomViewGroup(Context context, AttributeSet attr) {
		this(context, attr, 0);
		instance = this;
	}

	public CustomViewGroup(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
		instance = this;
	}

	public static void setM(Matrix matrixx) {
		matrix = matrixx;
		instance.invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		canvas.concat(matrix);
		super.onDraw(canvas);
	}
}
