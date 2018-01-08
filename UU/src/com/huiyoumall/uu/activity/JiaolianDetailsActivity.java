package com.huiyoumall.uu.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.huiyoumall.uu.R;
import com.huiyoumall.uu.base.BaseActivity;
import com.huiyoumall.uu.widget.PullScrollView;

/**
 * 教练详情 界面
 * 
 * @author ASUS
 * 
 */
public class JiaolianDetailsActivity extends BaseActivity implements
		PullScrollView.OnTurnListener {

	private PullScrollView mScrollView;
	private ImageView mHeadImg;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_jiaolian_details);
	}

	@Override
	public void findViewById() {
		mScrollView = (PullScrollView) findViewById(R.id.scroll_view);
		mHeadImg = (ImageView) findViewById(R.id.background_img);
		mScrollView.setHeader(mHeadImg);
		mScrollView.setOnTurnListener(this);
	}

	@Override
	public void initView() {

	}

	@Override
	public void activitybackview(View view) {

		super.activitybackview(view);
	}

	@Override
	public void onTurn() {

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
