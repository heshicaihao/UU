package com.huiyoumall.uu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class HScrollView extends HorizontalScrollView {

	public HScrollView(Context context) {
		this(context, null);
	}

	public HScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return true;
	}

}
