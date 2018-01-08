package com.huiyoumall.uu.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class VScrollView extends ScrollView {

	public VScrollView(Context context) {
		this(context, null);
	}

	public VScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public VScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return false;
	}

}
