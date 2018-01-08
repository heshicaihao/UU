package com.huiyoumall.uu.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huiyoumall.uu.R;

public class ViewPopupWindow extends BasePopupWindow implements OnClickListener {

	private LinearLayout vLeftView;
	private LinearLayout vRightView;
	private TextView vLeftText;
	private TextView vRightText;
	private Activity context;

	private onViewItemClickListener mOnViewItemClickListener;

	public ViewPopupWindow(Activity context_, int width, int height) {
		super(LayoutInflater.from(context_).inflate(R.layout.view_popwindow,
				null), width, height);
		this.context = context_;
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = context.getWindow()
						.getAttributes();
				params.alpha = 1f;
				context.getWindow().setAttributes(params);
			}
		});
	}

	@Override
	public void initViews() {
		vLeftView = (LinearLayout) findViewById(R.id.left_view);
		vRightView = (LinearLayout) findViewById(R.id.right_view);
		vLeftText = (TextView) findViewById(R.id.left_text);
		vRightText = (TextView) findViewById(R.id.right_text);
	}

	public void setText(String left, String right) {
		vLeftText.setText(left);
		vRightText.setText(right);
	}

	// µ¯³ö¿ò
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		WindowManager.LayoutParams params1 = context.getWindow()
				.getAttributes();
		params1.alpha = 0.5f;
		context.getWindow().setAttributes(params1);
	}

	@Override
	public void initEvents() {
		vLeftView.setOnClickListener(this);
		vRightView.setOnClickListener(this);
	}

	@Override
	public void init() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_view:
			if (mOnViewItemClickListener != null) {
				mOnViewItemClickListener.onLeftClickListener();
			}
			break;

		case R.id.right_view:
			if (mOnViewItemClickListener != null) {
				mOnViewItemClickListener.onRightClickListener();
			}
			break;
		}
		dismiss();
	}

	public void setonViewItemClickListener(onViewItemClickListener listener) {
		mOnViewItemClickListener = listener;
	}

	public interface onViewItemClickListener {
		void onLeftClickListener();

		void onRightClickListener();
	}
}
