package com.huiyoumall.uu.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.huiyoumall.uu.R;

public class SharePopupWindow extends BasePopupWindow implements
		OnClickListener {

	private LinearLayout mwechat;
	private LinearLayout mwechatMoment;
	private LinearLayout mqq;
	private LinearLayout mqzone;
	private LinearLayout msina;
	private Activity mContext;

	private onShareItemClickListener mOnBarItemClickListener;

	public SharePopupWindow(Activity context, int width, int height) {
		super(LayoutInflater.from(context).inflate(R.layout.view_share, null),
				width, height);
		this.mContext = context;
		this.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				WindowManager.LayoutParams params = mContext.getWindow()
						.getAttributes();
				params.alpha = 1f;
				mContext.getWindow().setAttributes(params);
			}
		});
	}

	@Override
	public void initViews() {
		mwechat = (LinearLayout) findViewById(R.id.wechat);
		mwechatMoment = (LinearLayout) findViewById(R.id.wechatmoment);
		mqq = (LinearLayout) findViewById(R.id.qq);
		mqzone = (LinearLayout) findViewById(R.id.qzone);
		msina = (LinearLayout) findViewById(R.id.sinaweibo);
	}

	@Override
	public void initEvents() {
		mwechat.setOnClickListener(this);
		mwechatMoment.setOnClickListener(this);
		mqq.setOnClickListener(this);
		mqzone.setOnClickListener(this);
		msina.setOnClickListener(this);
	}

	@Override
	public void init() {

	}

	// µ¯³ö¿ò
	public void showAtLocation(View parent, int gravity, int x, int y) {
		super.showAtLocation(parent, gravity, x, y);
		WindowManager.LayoutParams params1 = mContext.getWindow()
				.getAttributes();
		params1.alpha = 0.5f;
		mContext.getWindow().setAttributes(params1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.wechat:
			if (mOnBarItemClickListener != null) {
				mOnBarItemClickListener.onWechatButtonClick();
			}
			break;

		case R.id.wechatmoment:
			if (mOnBarItemClickListener != null) {
				mOnBarItemClickListener.onWechatmomentButtonClick();
			}
			break;

		case R.id.qq:
			if (mOnBarItemClickListener != null) {
				mOnBarItemClickListener.onQqButtonClick();
			}
			break;
		case R.id.qzone:
			if (mOnBarItemClickListener != null) {
				mOnBarItemClickListener.onQzoneButtonClick();
			}
			break;
		case R.id.sinaweibo:
			if (mOnBarItemClickListener != null) {
				mOnBarItemClickListener.onSinaweiboButtonClick();
			}
			break;
		}
		dismiss();
	}

	public void setOnShareItemClickListener(onShareItemClickListener listener) {
		mOnBarItemClickListener = listener;
	}

	public interface onShareItemClickListener {
		void onWechatButtonClick();

		void onWechatmomentButtonClick();

		void onQqButtonClick();

		void onQzoneButtonClick();

		void onSinaweiboButtonClick();
	}
}
